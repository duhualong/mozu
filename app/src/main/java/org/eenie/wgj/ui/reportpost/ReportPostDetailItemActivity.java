package org.eenie.wgj.ui.reportpost;

import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.response.reportpost.ReportPostItemDetail;

import butterknife.BindView;
import butterknife.OnClick;

import static org.eenie.wgj.R.id.tv_report_time;

/**
 * Created by Eenie on 2017/7/4 at 18:09
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportPostDetailItemActivity extends BaseActivity {
    @BindView(tv_report_time)TextView tvReportTime;
    public static final String INFO="info";
    private ReportPostItemDetail.DataBean mDataBean;
    @BindView(R.id.tv_report_time_sub)TextView tvReportTimeSub;
    @BindView(R.id.tv_report_post_class)TextView tvReportPostClass;
    @BindView(R.id.tv_report_post_name)TextView tvReportPostName;
    @BindView(R.id.tv_report_content)TextView tvReportContent;


    @Override
    protected int getContentView() {
        return R.layout.activity_report_post_detail_item;
    }

    @Override
    protected void updateUI() {
        mDataBean=getIntent().getParcelableExtra(INFO);
        if (mDataBean!=null){
            if (mDataBean.getDate()!=null){
                tvReportTime.setText(mDataBean.getDate());
            }else {
                tvReportTime.setText("无");
            }
            if (mDataBean.getPostsetting()!=null&&mDataBean.getPostsetting().getJetlag()!=null){
                tvReportTimeSub.setText(mDataBean.getPostsetting().getJetlag());
            }else {
                tvReportTimeSub.setText("无");

            }
            if (mDataBean.getPostsetting()!=null&&mDataBean.getPostsetting().getService()!=null){
                tvReportPostClass.setText(mDataBean.getPostsetting().getService());
            }else {
                tvReportPostClass.setText("无");

            }
            if (mDataBean.getPostsetting()!=null&&mDataBean.getPostsetting().getPostsetting_name()!=null){
                tvReportPostName.setText(mDataBean.getPostsetting().getPostsetting_name());
            }else {
                tvReportPostName.setText("无");

            }
            if (mDataBean.getPostsetting()!=null&&mDataBean.getPostsetting().getPostsetting_info()!=null){
                tvReportContent.setText(mDataBean.getPostsetting().getPostsetting_info());
            }else {
                tvReportContent.setText("无");

            }





        }


    }
    @OnClick(R.id.img_back)public void onClick(){
        onBackPressed();
    }
}
