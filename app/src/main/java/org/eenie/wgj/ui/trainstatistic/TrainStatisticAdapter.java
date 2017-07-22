package org.eenie.wgj.ui.trainstatistic;

import android.widget.ProgressBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.eenie.wgj.R;
import org.eenie.wgj.model.response.training.TrainingStatisticListResponse;

import java.util.List;



/**

 * Des:
 */

public class TrainStatisticAdapter extends BaseQuickAdapter<TrainingStatisticListResponse, BaseViewHolder> {
    public TrainStatisticAdapter(List<TrainingStatisticListResponse> data) {
        super(R.layout.item_training_statistic_new_item, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, TrainingStatisticListResponse entity) {


        holder.setText(R.id.tv_name, entity.getName());

        for (TrainingStatisticListResponse.InfoBean infoBean : entity.getInfo()) {

            if (infoBean.getType() == 1) {
                fillMaster(holder, infoBean);
            } else if (infoBean.getType() == 2) {
                fillPost(holder, infoBean);
            }


        }

    }


    private void fillMaster(BaseViewHolder holder, TrainingStatisticListResponse.InfoBean infoBean) {
        ProgressBar bar = holder.getView(R.id.progress_bar_key_personal);

        if (infoBean.getSchedule() > 0 && infoBean.getSchedule() < 1) {
            bar.setProgress(1);
        } else {
            bar.setProgress((int) infoBean.getSchedule());

        }
        if (infoBean.getSchedule()>100){
            holder.setText(R.id.tv_key_personal, String.format("%s%%", 100));
        }else {
            if (String.valueOf(infoBean.getSchedule()).length()>6){
                holder.setText(R.id.tv_key_personal, String.format("%s%%",String.valueOf(infoBean.getSchedule()).substring(0,5)));
            }else {
                holder.setText(R.id.tv_key_personal, String.format("%s%%", infoBean.getSchedule()));
            }

        }



    }

    private void fillPost(BaseViewHolder holder, TrainingStatisticListResponse.InfoBean infoBean) {
        ProgressBar bar = holder.getView(R.id.progress_bar_post);

        if (infoBean.getSchedule() > 0 && infoBean.getSchedule() < 1) {
            bar.setProgress(1);
        } else {
            bar.setProgress((int) infoBean.getSchedule());

        }
        if (infoBean.getSchedule()>100){
            holder.setText(R.id.tv_post, String.format("%s%%", 100));
        }else {
            if (String.valueOf(infoBean.getSchedule()).length()>6){
                holder.setText(R.id.tv_post, String.format("%s%%",String.valueOf(infoBean.getSchedule()).substring(0,5)));
            }else {
                holder.setText(R.id.tv_post, String.format("%s%%", infoBean.getSchedule()));
            }
        }

    }


}
