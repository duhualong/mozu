package org.eenie.wgj.ui.reportpoststatistics;

import android.content.Intent;
import android.text.Html;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.eenie.wgj.R;

import java.util.List;



/**
 * Created by Eenie on 2017/2/23 at 10:40
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportStatisticAdapter extends BaseQuickAdapter<ReportStatisticEntity.ResultMessageBean, BaseViewHolder> {


    String projectId;

    public ReportStatisticAdapter(List<ReportStatisticEntity.ResultMessageBean> data, String projectId) {

        super(R.layout.item_report_statistic_layout, data);
        this.projectId = projectId;
    }

    @Override
    protected void convert(BaseViewHolder holder, final ReportStatisticEntity.ResultMessageBean entity) {


        holder.setText(R.id.tv_report_title, String.format("%s报岗完成率", entity.getDate()));
        holder.setText(R.id.tv_report_should, String.format("%s次", entity.getPlan()));
        holder.setText(R.id.tv_report_fact, String.format("%s次", entity.getActual()));


        holder.setText(R.id.tv_report_none, Html.fromHtml("<U>" + String.format("%s次", entity.getNot()) + "</U>"));


        holder.setText(R.id.tv_rate, String.format("%s%%", entity.getRate()));


        holder.getView(R.id.tv_report_none).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ReportDayStatisticActivity.class);
                intent.putExtra("projectId", projectId);
                intent.putExtra("date", entity.getDate());
                mContext.startActivity(intent);
            }
        });
        holder.getView(R.id.btn_report_fact).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(mContext, ReportDayStatisticActivity.class);
                intent.putExtra("projectId", projectId);
                intent.putExtra("date", entity.getDate());
                mContext.startActivity(intent);
            }
        });

        CircularProgressBar bar = holder.getView(R.id.pro_rate);

        bar.setProgress((int) (entity.getRate()));


    }
}
