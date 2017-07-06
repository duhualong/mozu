package org.eenie.wgj.ui.reportpoststatistics;

import android.content.Intent;
import android.text.Html;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.eenie.wgj.R;

import java.util.List;


/**
 * Created by Eenie on 2017/2/23 at 10:40
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportStatisticAdapter extends BaseQuickAdapter<ReportStatisticEntity, BaseViewHolder> {


    String projectId;

    public ReportStatisticAdapter(List<ReportStatisticEntity> data, String projectId) {

        super(R.layout.item_report_statistic_layout, data);
        this.projectId = projectId;
    }

    @Override
    protected void convert(BaseViewHolder holder, final ReportStatisticEntity entity) {


        holder.setText(R.id.tv_report_title, String.format("%s报岗完成率", entity.getDate().replace("-", "年") + "月"));
        holder.setText(R.id.tv_report_should, String.format("%s次", entity.getPlan()));
        holder.setText(R.id.tv_report_fact, Html.fromHtml("<U>" +
                String.format("%s次", entity.getActual()) + "</U>"));


        holder.setText(R.id.tv_report_none, Html.fromHtml("<U>" +
                String.format("%s次", entity.getNot()) + "</U>"));
        if (String.valueOf(entity.getRate()).length() >= 5) {
            holder.setText(R.id.tv_rate, String.format("%s%%",
                    String.valueOf(entity.getRate()).substring(0, 4)));

        } else {
            holder.setText(R.id.tv_rate, String.format("%s%%", entity.getRate()));

        }


        holder.getView(R.id.tv_report_none).setOnClickListener(v -> {

            Intent intent = new Intent(mContext, ReportNoDayStatisticActivity.class);
            intent.putExtra(ReportNoDayStatisticActivity.PROJECT_ID, projectId);
            intent.putExtra(ReportNoDayStatisticActivity.DATE, entity.getDate());
            mContext.startActivity(intent);
        });
        holder.getView(R.id.tv_report_fact).setOnClickListener(v -> {

            Intent intent = new Intent(mContext, ReportDayStatisticActivity.class);
            intent.putExtra(ReportDayStatisticActivity.PROJECT_ID, projectId);
            intent.putExtra(ReportDayStatisticActivity.DATE, entity.getDate());
            mContext.startActivity(intent);
        });

        CircularProgressBar bar = holder.getView(R.id.pro_rate);

        if (entity.getRate() > 0 && entity.getRate() < 1) {
            bar.setProgress(1);

        } else {
            bar.setProgress((int) (entity.getRate()));
        }


    }
}
