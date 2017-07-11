package org.eenie.wgj.ui.meeting;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.meeting.MeetingDetailFeedback;
import org.eenie.wgj.model.response.meeting.MeetingFeedbackResponseList;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/11 at 16:28
 * Email: 472279981@qq.com
 * Des:
 */

public class MeetingFeedbackDetailActivity extends BaseActivity {
    public static final String APPLY_ID = "id";
    @BindView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.meeting_name)
    TextView tvMeetingName;
    @BindView(R.id.tv_meeting_time)
    TextView tvMeetingTime;
    @BindView(R.id.meeting_class)
    TextView tvMeetingClass;
    @BindView(R.id.meeting_purpose)
    TextView tvMeetingPurpose;
    @BindView(R.id.meeting_apply_result)
    TextView tvMeetingResult;
    @BindView(R.id.img_apply_avatar)
    CircleImageView imgApplyAvatar;
    @BindView(R.id.tv_apply_meeting_name)
    TextView tvApplyMeetingName;
    @BindView(R.id.tv_apply_meeting_cause)
    TextView meetingCause;
    @BindView(R.id.tv_apply_result)
    TextView tvApplyResult;
    private String applyId;
    @BindView(R.id.rl_apply_meeting)
    RelativeLayout mRelativeLayout;

    @Override
    protected int getContentView() {
        return R.layout.activity_meeting_feedback_detail;
    }

    @Override
    protected void updateUI() {
        applyId = getIntent().getStringExtra(APPLY_ID);
        if (!TextUtils.isEmpty(applyId)) {
            getData(applyId);
        }


    }

    private void getData(String applyId) {
        mSubscription = mRemoteService.getMeetingDetailInfo(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), applyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            MeetingDetailFeedback mData =
                                    gson.fromJson(jsonArray,
                                            new TypeToken<MeetingFeedbackResponseList>() {
                                            }.getType());
                            if (mData != null) {
                                if (mData.getCheckstatus() == 2) {
                                    mRelativeLayout.setVisibility(View.GONE);
                                    tvMeetingResult.setTextColor(ContextCompat.getColor
                                            (context, R.color.black_light));

                                } else {
                                    mRelativeLayout.setVisibility(View.VISIBLE);
                                    if (mData.getCheckstatus() == 1) {
                                        tvMeetingResult.setTextColor(ContextCompat.getColor
                                                (context, R.color.text_green));

                                    } else if (mData.getCheckstatus() == 3) {
                                        tvMeetingResult.setTextColor(ContextCompat.getColor
                                                (context, R.color.text_red));
                                    }

                                    if (!TextUtils.isEmpty(mData.getCheck_image())) {
                                        Glide.with(context).load(Constant.DOMIN + mData.getCheck_image())
                                                .centerCrop().into(imgApplyAvatar);

                                    }
                                    if (!TextUtils.isEmpty(mData.getCheckusername())) {
                                        tvApplyMeetingName.setText(mData.getCheckusername());
                                    }
                                    if (!TextUtils.isEmpty(mData.getCheck_feedback())) {
                                        meetingCause.setText(mData.getCheck_feedback());
                                    }


                                }
                                if (!TextUtils.isEmpty(mData.getId_card_head_image())) {
                                    Glide.with(context).load(Constant.DOMIN + mData.getId_card_head_image())
                                            .centerCrop().into(imgAvatar);

                                }
                                if (!TextUtils.isEmpty(mData.getUsername())) {
                                    tvName.setText(mData.getUsername());
                                }
                                if (!TextUtils.isEmpty(mData.getCreated_at())) {
                                    tvDate.setText(mData.getCreated_at());
                                }

                                if (!TextUtils.isEmpty(mData.getName())) {
                                    tvMeetingName.setText(mData.getName());

                                }
                                if (!TextUtils.isEmpty(mData.getStart()) && !TextUtils.isEmpty(mData.getEnd())) {
                                    tvMeetingTime.setText(mData.getStart() + "至" + mData.getEnd());
                                }
                                if (!TextUtils.isEmpty(mData.getRoom_name())) {
                                    tvMeetingClass.setText(mData.getRoom_name());
                                }
                                if (!TextUtils.isEmpty(mData.getDetail())) {
                                    tvMeetingPurpose.setText(mData.getDetail());
                                }

                                if (!TextUtils.isEmpty(mData.getCheckname())) {
                                    tvMeetingResult.setText(mData.getCheckname());
                                }
                                tvApplyResult.setText(mData.getCheckname());


                            }
                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }

    @OnClick(R.id.img_back)
    public void onClick() {
        onBackPressed();

    }
}
