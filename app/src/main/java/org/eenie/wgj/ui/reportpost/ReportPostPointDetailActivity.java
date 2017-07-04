package org.eenie.wgj.ui.reportpost;

import android.text.TextUtils;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.response.reportpost.ReportPostItemDetail;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/7/4 at 19:19
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportPostPointDetailActivity extends BaseActivity {
    public static final String INFO="info";
    public static final String POSITION="position";
    public static final String POST_NAME="name";
    @BindView(R.id.tv_title)TextView tvTitle;
    @BindView(R.id.tv_report_time)TextView tvReportTime;
    @BindView(R.id.report_post_name)TextView tvReportPostName;
    @BindView(R.id.tv_address)TextView tvReportAddress;
    private String position;
    private ReportPostItemDetail.planBean mData;
    private String postName;


    @Override
    protected int getContentView() {
        return R.layout.activity_report_point_item_detail;
   }

    @Override
    protected void updateUI() {
        mData=getIntent().getParcelableExtra(INFO);
        position=getIntent().getStringExtra(POSITION);
        postName=getIntent().getStringExtra(POST_NAME);
        if (!TextUtils.isEmpty(postName)){
            tvReportPostName.setText("报岗岗位&#8195;"+postName);
        }else {
            tvReportPostName.setText("报岗岗位&#8195;无");
        }

        if (!TextUtils.isEmpty(position)){
            if (position.length()<=1&&Integer.valueOf(position)<=9){
                tvTitle.setText("点位0"+position);
            }else {
                tvTitle.setText("点位"+position);

            }
        }
        if (mData!=null){
            if (!TextUtils.isEmpty(mData.getTime())){
                tvReportTime.setText("报岗时间&#8195;"+mData.getTime());
            }else {
                tvReportTime.setText("报岗时间&#8195;无");

            }
            if (mData.getActual()!=null&&!TextUtils.isEmpty(mData.getActual().getLocation_name())){
                tvReportAddress.setText(mData.getActual().getLocation_name());
            }else {
                tvReportAddress.setText("无");
            }
        }


    }
    @OnClick(R.id.img_back)public  void onClick(){
        onBackPressed();
    }
}
