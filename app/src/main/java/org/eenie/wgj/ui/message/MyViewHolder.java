package org.eenie.wgj.ui.message;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.eenie.wgj.R;

/**
 * Created by Eenie on 2017/8/4 at 14:05
 * Email: 472279981@qq.com
 * Des:
 */

public class MyViewHolder extends RecyclerView.ViewHolder {
    public ImageView imgSetting;
    public TextView tvTitle;
    public TextView tvContent;
    public TextView tvDate;

    public MyViewHolder(View itemView) {
        super(itemView);
        imgSetting = (ImageView) itemView.findViewById(R.id.img_setting);
        tvTitle = (TextView) itemView.findViewById(R.id.item_to_do_title);
        tvContent = (TextView) itemView.findViewById(R.id.tv_content);
        tvDate = (TextView) itemView.findViewById(R.id.item_apply_date);
    }
}
