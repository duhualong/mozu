package org.eenie.wgj.ui.routinginspection.report;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.response.ReportRoutingReponse;
import org.eenie.wgj.util.Constant;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/6/23 at 17:16
 * Email: 472279981@qq.com
 * Des:
 */

public class DetailShowActivity extends BaseActivity {
    public static final String INFO = "info";
    private ReportRoutingReponse data;
    @BindView(R.id.report_order)
    TextView tvReportOrder;
    @BindView(R.id.report_name)
    TextView tvReportName;
    @BindView(R.id.report_time)
    TextView tvReportTime;
    @BindView(R.id.report_status)
    TextView tvReportStatus;
    @BindView(R.id.report_address)
    TextView tvReportAddress;
    @BindView(R.id.report_content)
    TextView tvReportContent;
    @BindView(R.id.line_img)
    LinearLayout mLinearLayout;
    @BindView(R.id.img_first)
    ImageView imgFirst;
    @BindView(R.id.img_second)
    ImageView imgSecond;
    @BindView(R.id.img_third)
    ImageView imgThird;

    @Override
    protected int getContentView() {
        return R.layout.activity_routing_report_detail;
    }

    @OnClick(R.id.img_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;

        }
    }

    @Override
    protected void updateUI() {
        data = getIntent().getParcelableExtra(INFO);
        if (data != null) {
            tvReportOrder.setText("报修单号  " + data.getUniqueid());
            if (data.getUser() != null && !TextUtils.isEmpty(data.getUser().getName())) {
                tvReportName.setText("上报人员  " + data.getUser().getName());

            }
            if (data.getTime() != null && data.getTime().length() > 11) {
                tvReportTime.setText(data.getTime().substring(11, data.getTime().length()));
            }
            tvReportStatus.setText("巡检情况  " + "异常");
            if (data.getInspectionpoint().getInspectionname() != null) {
                tvReportAddress.setText("巡检地点  " + data.getInspectionpoint().getInspectionname());

            }
            if (data.getInspectionpoint().getInspectioncontent() != null) {
                tvReportContent.setText(data.getInspectionpoint().getInspectioncontent());
            }
            if (data.getImage() != null) {
                mLinearLayout.setVisibility(View.VISIBLE);
                if (data.getImage().size() <= 3) {
                    if (data.getImage().size() == 1) {
                        Glide.with(context).load(Constant.DOMIN + data.getImage().get(0).getImage()).
                                centerCrop().into(imgFirst);
                    }
                    if (data.getImage().size() == 2) {
                        Glide.with(context).load(Constant.DOMIN + data.getImage().get(0).getImage()).
                                centerCrop().into(imgFirst);
                        Glide.with(context).load(Constant.DOMIN + data.getImage().get(1).getImage()).
                                centerCrop().into(imgSecond);

                    }
                    if (data.getImage().size() == 3) {
                        Glide.with(context).load(Constant.DOMIN + data.getImage().get(0).getImage()).
                                centerCrop().into(imgFirst);
                        Glide.with(context).load(Constant.DOMIN + data.getImage().get(1).getImage()).
                                centerCrop().into(imgSecond);
                        Glide.with(context).load(Constant.DOMIN + data.getImage().get(2).getImage()).
                                centerCrop().into(imgThird);
                    }

                } else {
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(0).getImage()).
                            centerCrop().into(imgFirst);
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(1).getImage()).
                            centerCrop().into(imgSecond);
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(2).getImage()).
                            centerCrop().into(imgThird);
                }

            } else {
                mLinearLayout.setVisibility(View.INVISIBLE);
            }


        }

    }
}
