package org.eenie.wgj.ui.project.sortclass;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.model.response.sortclass.ArrangeProjectDateTotal;
import org.eenie.wgj.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/5 at 14:46
 * Email: 472279981@qq.com
 * Des:
 */

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> implements View.OnClickListener{
    private Context context;
    private ArrayList<ArrangeProjectDateTotal> mDateList;

    public MyAdapter(Context context, ArrayList<ArrangeProjectDateTotal> dateList) {
        this.context = context;
        mDateList = dateList;
    }

    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    interface OnItemClickListener {
        void onItemClick(View view , int position);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_arrange_date,
                viewGroup, false);
        ViewHolder vh = new ViewHolder(view);
        //将创建的View注册点击事件
        view.setOnClickListener(this);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder,  int position) {
        if (mDateList.get(position).isChecked()){
            viewHolder.mRelativeLayout.setBackgroundResource(R.color.text_blue);
            viewHolder.itemDate.setTextColor(Color.WHITE);
            viewHolder.itemWeek.setTextColor(Color.WHITE);
        }else {
            viewHolder.mRelativeLayout.setBackgroundResource(R.color.white);

            viewHolder.itemDate.setTextColor(ContextCompat.getColor
                    (context, R.color.black_light));
            viewHolder.itemWeek.setTextColor(ContextCompat.getColor
                    (context, R.color.titleColor));
        }

        if (!TextUtils.isEmpty(mDateList.get(position).getDate())){
            viewHolder.itemDate.setText(new SimpleDateFormat("MM-dd").
                    format(Utils.covertTime(mDateList.get(position).getDate()).getTime()));
            viewHolder.itemWeek.setText(Utils.covertWeek(mDateList.get(position).getDate()));
        }

        //将position保存在itemView的Tag中，以便点击时进行获取
        viewHolder.itemView.setTag(position);

    }

    public void clear() {
        this.mDateList.clear();
        MyAdapter.this.notifyDataSetChanged();
    }
    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v,(int)v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }


    //获取数据的数量
    @Override
    public int getItemCount() {
        return mDateList.size();
    }
    //自定义的ViewHolder，持有每个Item的的所有界面元素
    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView itemDate;
        private TextView itemWeek;
        private RelativeLayout mRelativeLayout;

        public ViewHolder(View view){
            super(view);
            itemDate = (TextView) view.findViewById(R.id.item_date);
            itemWeek= (TextView) view.findViewById(R.id.item_week);
            mRelativeLayout= (RelativeLayout) view.findViewById(R.id.rl_arrange_date);
        }

    }
}