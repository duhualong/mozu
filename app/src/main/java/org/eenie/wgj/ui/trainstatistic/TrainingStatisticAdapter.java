package org.eenie.wgj.ui.trainstatistic;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.eenie.wgj.R;
import org.eenie.wgj.model.response.training.TrainingStatisticListResponse;
import org.eenie.wgj.ui.reportpoststatistics.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eenie on 2017/7/7 at 13:07
 * Email: 472279981@qq.com
 * Des:
 */

public class TrainingStatisticAdapter extends BaseQuickAdapter<TrainingStatisticListResponse, BaseViewHolder> {
    public TrainingStatisticAdapter(ArrayList<TrainingStatisticListResponse> data) {
        super(R.layout.item_training_project_statistic, data);
    }

    public TrainingStatisticAdapter(List<TrainingStatisticListResponse> data) {
        super(data);
    }

    @Override
    protected void convert(BaseViewHolder holder, TrainingStatisticListResponse entity) {


        holder.setText(R.id.tv_person_name, entity.getName());

        for (TrainingStatisticListResponse.InfoBean infoBean : entity.getInfo()) {

            if (infoBean.getType() == 1) {
                fillMaster(holder, infoBean);
            } else if (infoBean.getType() == 2) {
                fillPost(holder, infoBean);
            }
        }

    }


    private void fillMaster(BaseViewHolder holder, TrainingStatisticListResponse.InfoBean infoBean) {
        CircularProgressBar bar = holder.getView(R.id.pro_master_rate);


        if (infoBean.getSchedule() > 0 && infoBean.getSchedule() < 1) {
            bar.setProgress(1);
        } else {
            bar.setProgress((int) infoBean.getSchedule());

        }

        holder.setText(R.id.tv_master_rate, String.format("%s%%", infoBean.getSchedule()));


    }

    private void fillPost(BaseViewHolder holder, TrainingStatisticListResponse.InfoBean infoBean) {
        CircularProgressBar bar = holder.getView(R.id.pro_post_rate);

        if (infoBean.getSchedule() > 0 && infoBean.getSchedule() < 1) {
            bar.setProgress(1);
        } else {
            bar.setProgress((int) infoBean.getSchedule());

        }

        holder.setText(R.id.tv_post_rate, String.format("%s%%", infoBean.getSchedule()));
    }


}