package org.eenie.wgj.ui.message;

import android.text.TextUtils;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.message.MessageRequestData;
import org.eenie.wgj.model.response.message.MessageStatus;
import org.eenie.wgj.util.Constants;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/14 at 15:55
 * Email: 472279981@qq.com
 * Des:
 */

public class NoticeDetailActivity extends BaseActivity {
    public static final String INFO = "info";
    private MessageRequestData.DataBean mMeetingNotice;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_notice_content)
    TextView tvContent;
    @BindView(R.id.tv_notice_time)
    TextView tvNoticeTime;


    @Override
    protected int getContentView() {
        return R.layout.activity_notice_info_detail;
    }

    @Override
    protected void updateUI() {
        mMeetingNotice = getIntent().getParcelableExtra(INFO);


        if (mMeetingNotice != null) {
            if (!TextUtils.isEmpty(mMeetingNotice.getTitle())) {
                tvTitle.setText(mMeetingNotice.getTitle());
            }
            if (!TextUtils.isEmpty(mMeetingNotice.getAlert())) {
                tvContent.setText(mMeetingNotice.getAlert());
            }
            if (!TextUtils.isEmpty(mMeetingNotice.getCreated_at())) {
                tvNoticeTime.setText(mMeetingNotice.getCreated_at());
            }
        }


        changeStatus(mMeetingNotice.getId());
    }
    private void changeStatus( int id) {
        MessageStatus request=new MessageStatus(1,id);
        mSubscription=mRemoteService.changeMessageStatus(mPrefsHelper.getPrefs().getString(Constants.TOKEN,""),request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {

                    }
                });
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        onBackPressed();
    }
}
