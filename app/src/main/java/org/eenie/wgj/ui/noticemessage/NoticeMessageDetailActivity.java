package org.eenie.wgj.ui.noticemessage;

import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.response.noticemessage.NoticeMessageResponse;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/7/31 at 13:58
 * Email: 472279981@qq.com
 * Des:
 */

public class NoticeMessageDetailActivity extends BaseActivity {
    public static final String INFO="info";
    private NoticeMessageResponse mData;
    @BindView(R.id.notice_title)TextView tvTitle;
    @BindView(R.id.tv_date)TextView tvDate;
    @BindView(R.id.tv_content)TextView tvContent;
    @BindView(R.id.tv_notice_people)TextView tvNoticePeople;


    @Override
    protected int getContentView() {
        return R.layout.activity_notice_message_detail;
    }

    @Override
    protected void updateUI() {
        mData=getIntent().getParcelableExtra(INFO);
        if (mData!=null){
            tvTitle.setText(mData.getTitle());
            tvDate.setText(mData.getDate());
            tvContent.setText(mData.getContent());
            tvNoticePeople.setText(mData.getNotice_people());
        }

    }
    @OnClick(R.id.img_back)public void onClick(){
        onBackPressed();
    }
}
