package org.eenie.wgj.ui.meeting.fragment;


import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.meeting.MeetingEndDetail;
import org.eenie.wgj.ui.message.GalleryActivity;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/11 at 20:58
 * Email: 472279981@qq.com
 * Des:
 */

public class MeetingEndDetailActivity extends BaseActivity {
    public static final String APPLY_ID = "id";
    @BindView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @BindView(R.id.tv_username)
    TextView tvUserName;
    @BindView(R.id.tv_status_meeting)
    TextView tvMeetingStatus;
    @BindView(R.id.tv_meeting_name)
    TextView tvMeetingName;
    @BindView(R.id.tv_meeting_time)
    TextView tvMeetingTime;
    @BindView(R.id.tv_meeting_address)
    TextView tvMeetingAddress;
    @BindView(R.id.tv_meeting_host)
    TextView tvMeetingHost;
    @BindView(R.id.tv_meeting_record)
    TextView tvMeetingRecord;
    @BindView(R.id.tv_meeting_purpose)
    TextView tvMeetingPurpose;
    @BindView(R.id.tv_meeting_plan)
    TextView tvMeetingPlan;
    @BindView(R.id.tv_meeting_record_content)
    TextView tvMeetingRecordContent;
    @BindView(R.id.img_first)
    ImageView imgFirst;
    @BindView(R.id.img_second)
    ImageView imgSecond;
    @BindView(R.id.img_third)
    ImageView imgThird;
    @BindView(R.id.tv_meeting_join)
    TextView tvMeetingJoin;
    private String applyId;
    List<MeetingEndDetail.ImageBean> mImageBean = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_end_meeting_detail;
    }

    @Override
    protected void updateUI() {
        applyId = getIntent().getStringExtra(APPLY_ID);
        if (!TextUtils.isEmpty(applyId)) {
            getData(applyId);
        }


    }

    private void getData(String applyId) {
        mSubscription = mRemoteService.getMeetingArrangeDetail(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                applyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {

                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            MeetingEndDetail mData =
                                    gson.fromJson(jsonArray,
                                            new TypeToken<MeetingEndDetail>() {
                                            }.getType());

                            if (mData != null) {
                                if (!TextUtils.isEmpty(mData.getId_card_head_image())) {
                                    Glide.with(context).load(Constant.DOMIN + mData.getId_card_head_image())
                                            .centerCrop().into(imgAvatar);

                                }
                                if (!TextUtils.isEmpty(mData.getUsername())) {
                                    tvUserName.setText(mData.getUsername());

                                }
                                if (!TextUtils.isEmpty(mData.getState())) {
                                    tvMeetingStatus.setText(mData.getState());
                                }
                                if (!TextUtils.isEmpty(mData.getName())) {
                                    tvMeetingName.setText(mData.getName());
                                }
                                if (!TextUtils.isEmpty(mData.getStart()) && !TextUtils.isEmpty(mData.getEnd())) {
                                    tvMeetingTime.setText(mData.getStart() + "至" + mData.getEnd());
                                }
                                if (mData.getHost().equals("null")) {
                                    tvMeetingHost.setText("无");
                                } else {
                                    tvMeetingHost.setText(mData.getHost());
                                }
                                if (mData.getRecorder().equals("null")) {

                                } else {
                                    if (!TextUtils.isEmpty(mData.getRecorder())) {
                                        tvMeetingRecord.setText(mData.getRecorder());
                                    }
                                }

                                if (!mData.getNumber().isEmpty() && mData.getNumber() != null) {

                                    if (mData.getNumber().size() > 0 && !TextUtils.isEmpty(
                                            mData.getNumber().get(0).getUsername())) {
                                        String joiner = "";
                                        for (int i = 0; i < mData.getNumber().size(); i++) {
                                            if (i == (mData.getNumber().size() - 1)) {
                                                joiner = joiner + mData.getNumber().get(i).getUsername();
                                            } else {
                                                joiner = joiner + mData.getNumber().
                                                        get(i).getUsername() + "/";
                                            }


                                        }
                                        tvMeetingJoin.setText(joiner);
                                    }
                                }
                                if (!TextUtils.isEmpty(mData.getAddress())) {
                                    tvMeetingAddress.setText(mData.getAddress());
                                }

                                if (!TextUtils.isEmpty(mData.getMeeting_purpose())) {
                                    tvMeetingPurpose.setText(mData.getMeeting_purpose());
                                }
                                if (!TextUtils.isEmpty(mData.getMeeting_agenda())) {
                                    tvMeetingPlan.setText(mData.getMeeting_agenda());
                                }
                                if (!TextUtils.isEmpty(mData.getRecord())) {
                                    tvMeetingRecordContent.setText(mData.getRecord());
                                } else {
                                    tvMeetingRecordContent.setText("无");
                                }
                                if (!mData.getImage().isEmpty() && mData.getImage() != null) {
                                    mImageBean = mData.getImage();
                                    if (mData.getImage().size() == 1) {
                                        Glide.with(context).load(Constant.DOMIN + mData.getImage().
                                                get(0).getImage()).centerCrop().into(imgFirst);
                                    } else if (mData.getImage().size() == 2) {
                                        Glide.with(context).load(Constant.DOMIN + mData.getImage().
                                                get(0).getImage()).centerCrop().into(imgFirst);
                                        Glide.with(context).load(Constant.DOMIN + mData.getImage().
                                                get(1).getImage()).centerCrop().into(imgSecond);
                                    } else if (mData.getImage().size() >= 3) {
                                        Glide.with(context).load(Constant.DOMIN + mData.getImage().
                                                get(0).getImage()).centerCrop().into(imgFirst);
                                        Glide.with(context).load(Constant.DOMIN + mData.getImage().
                                                get(1).getImage()).centerCrop().into(imgSecond);
                                        Glide.with(context).load(Constant.DOMIN + mData.getImage().
                                                get(2).getImage()).centerCrop().into(imgThird);

                                    }
                                }


                            }

                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @OnClick({R.id.img_back, R.id.img_first, R.id.img_second, R.id.img_third})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.img_first:
                if (!mImageBean.isEmpty() && mImageBean != null) {
                    if (mImageBean.size() >= 1) {
                        startActivity(
                                new Intent(context, GalleryActivity.class).
                                        putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                                Constant.DOMIN + mImageBean.get(0).getImage()));
                    }
                }


                break;
            case R.id.img_second:
                if (!mImageBean.isEmpty() && mImageBean != null) {
                    if (mImageBean.size() >= 2) {
                        startActivity(
                                new Intent(context, GalleryActivity.class).
                                        putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                                Constant.DOMIN + mImageBean.get(1).getImage()));
                    }
                }


                break;
            case R.id.img_third:
                if (!mImageBean.isEmpty() && mImageBean != null) {
                    if (mImageBean.size() >= 3) {
                        startActivity(
                                new Intent(context, GalleryActivity.class).
                                        putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                                Constant.DOMIN + mImageBean.get(2).getImage()));
                    }
                }
                break;
        }
    }
}
