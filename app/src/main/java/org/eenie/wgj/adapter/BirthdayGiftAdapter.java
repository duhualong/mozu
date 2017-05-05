package org.eenie.wgj.adapter;

import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.eenie.wgj.R;
import org.eenie.wgj.model.requset.BirthdayGift;

import java.util.List;



/**
 * Created by Eenie on 2017/5/5 at 13:48
 * Email: 472279981@qq.com
 * Des:
 */

public class BirthdayGiftAdapter  extends BaseQuickAdapter<BirthdayGift, BaseViewHolder> {


    private int selected = 0;


    public BirthdayGiftAdapter(List<BirthdayGift> data) {
        super(R.layout.item_gift_birthday, data);
    }

    @Override
    protected void convert(final BaseViewHolder holder, final BirthdayGift birthdayGift) {


        holder.setText(R.id.tv_gift_name, birthdayGift.getName());
        holder.setImageResource(R.id.img_gift_icon, birthdayGift.getIcon());


        if (holder.getPosition() == selected) {
            holder.itemView.setSelected(true);
            holder.getView(R.id.tv_gift_name).setVisibility(View.VISIBLE);
        } else {
            holder.itemView.setSelected(false);
            holder.getView(R.id.tv_gift_name).setVisibility(View.INVISIBLE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selected = holder.getPosition();
//                notifyDataSetChanged();
                onItemClick(holder, birthdayGift);
            }
        });
    }





    public void onItemClick(BaseViewHolder holder, final BirthdayGift birthdayGift) {

    }

    public void setSelected(int selected) {
        this.selected = selected;
    }





    public BirthdayGift getSelectedEntity() {
        return getItem(selected);
    }



}