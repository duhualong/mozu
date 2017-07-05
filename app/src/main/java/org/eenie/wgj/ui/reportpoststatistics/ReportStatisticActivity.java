package org.eenie.wgj.ui.reportpoststatistics;

import android.support.v7.widget.RecyclerView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/7/4 at 20:51
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportStatisticActivity extends BaseActivity {
    @BindView(R.id.rv_report_statistic)
    RecyclerView mRvReportStatistic;

    @Override
    protected int getContentView() {
        return R.layout.activity_recycler_routing_statistic;
    }

    @Override
    protected void updateUI() {

    }
    @OnClick(R.id.img_back)public  void onClick(){
        onBackPressed();
    }
}
