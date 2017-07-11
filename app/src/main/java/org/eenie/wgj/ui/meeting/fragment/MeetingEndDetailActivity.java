package org.eenie.wgj.ui.meeting.fragment;


import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

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
    private String applyId;


    @Override
    protected int getContentView() {
        return R.layout.activity_end_meeting_detail;
    }

    @Override
    protected void updateUI() {
        applyId=getIntent().getStringExtra(APPLY_ID);
        if (!TextUtils.isEmpty(applyId)){
            getData(applyId);
        }


    }

    private void getData(String applyId) {
        mSubscription=mRemoteService.getMeetingArrangeDetail(mPrefsHelper.getPrefs().getString(Constants.TOKEN,""),
                applyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {

                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode()==200||apiResponse.getResultCode()==0){
                            Gson gson=new Gson();
                          String json=  gson.toJson(apiResponse.getData());
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

                break;
            case R.id.img_second:

                break;
            case R.id.img_third:

                break;
        }
    }
}
