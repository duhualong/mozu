package org.eenie.wgj.ui.message;

import android.text.TextUtils;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.requset.NoticeMessage;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/7/14 at 15:55
 * Email: 472279981@qq.com
 * Des:
 */

public class NoticeDetailActivity extends BaseActivity {
    public static final String INFO = "info";
    private NoticeMessage mMeetingNotice;
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

    }

    @OnClick(R.id.img_back)
    public void onClick() {
        onBackPressed();
    }
}
