package org.eenie.wgj.ui.fragment;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.eenie.wgj.R;
import org.eenie.wgj.model.response.message.MessageRequestData;

import java.util.List;

import static org.eenie.wgj.R.mipmap.ic_gift;

/**
 * Created by Eenie on 2017/7/21 at 21:05
 * Email: 472279981@qq.com
 * Des:
 */

public class MyAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {


    //定义一个集合，接收从Activity中传递过来的数据和上下文
    private List<MessageRequestData.DataBean> mList;
    private Context mContext;

    MyAdapter(Context context, List<MessageRequestData.DataBean> list) {
        this.mContext = context;
        this.mList = list;
    }

    private OnItemClickListener mOnItemClickListener = null;

    //define interface
    public static interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    @Override
    public int getItemCount() {
        return mList.size();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(R.layout.item_notice_new_pager, parent, false);
        MyHolder vh = new MyHolder(layout);
        //将创建的View注册点击事件
        layout.setOnClickListener(this);
        return new MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MyHolder) {
            MessageRequestData.DataBean itemData = mList.get(position);
            ((MyHolder) holder).tvTitle.setText(itemData.getTitle());
            ((MyHolder) holder).tvContent.setText(itemData.getAlert());
            ((MyHolder) holder).tvDate.setText(itemData.getCreated_at());
            switch (itemData.getKey()) {

                case 1101:

                    Glide.with(mContext)
                            .load(R.mipmap.ic_remind)
                            .into(((MyHolder) holder).imgIcon);


                    break;
                case 2101:
                    //考勤

                    Glide.with(mContext)
                            .load(R.mipmap.ic_notice)
                            .into(((MyHolder) holder).imgIcon);

                   // ((MyHolder) holder).imgIcon.setImageResource(R.mipmap.ic_notice);

                    break;
                case 1002:
                    //报岗
                    Glide.with(mContext)
                            .load(R.mipmap.ic_notice)
                            .into(((MyHolder) holder).imgIcon);
                    break;
                case 1003:
                    Glide.with(mContext)
                            .load(R.mipmap.ic_notice)
                            .into(((MyHolder) holder).imgIcon);
                    //巡检
                    break;
                case 2201:
                    //会议室审核后
                    Glide.with(mContext)
                            .load(R.mipmap.ic_notice)
                            .into(((MyHolder) holder).imgIcon);


                    break;
                case 2202:
                    //会议
                    Glide.with(mContext)
                            .load(R.mipmap.ic_notice)
                            .into(((MyHolder) holder).imgIcon);

                    break;
                case 2203:
                    //异常
                    Glide.with(mContext)
                            .load(R.mipmap.ic_notice)
                            .into(((MyHolder) holder).imgIcon);

                    break;
                case 2301:
                    //新进员工
                    Glide.with(mContext)
                            .load(R.mipmap.ic_notice)
                            .into(((MyHolder) holder).imgIcon);

                    break;

                case 2302:
                    //六天没考勤
                    Glide.with(mContext)
                            .load(R.mipmap.ic_notice)
                            .into(((MyHolder) holder).imgIcon);

                    break;
                case 2303:
                    //提醒签合同录指纹
                    Glide.with(mContext)
                            .load(R.mipmap.ic_notice)
                            .into(((MyHolder) holder).imgIcon);

                    break;
                case 2401:
                    //交接班注意事项
                    Glide.with(mContext)
                            .load(R.mipmap.ic_notice)
                            .into(((MyHolder) holder).imgIcon);

                    break;
                case 3101:
                    //生日提醒
                    Glide.with(mContext)
                            .load(ic_gift)
                            .into(((MyHolder) holder).imgIcon);


                    break;
                case 4101:
                    //随手拍
                    Glide.with(mContext)
                            .load(R.mipmap.ic_notice)
                            .into(((MyHolder) holder).imgIcon);
                    break;


            }
            //将position保存在itemView的Tag中，以便点击时进行获取
            ((MyHolder) holder).itemView.setTag(position);
        }
    }

    @Override
    public void onClick(View v) {
        if (mOnItemClickListener != null) {
            //注意这里使用getTag方法获取position
            mOnItemClickListener.onItemClick(v, (int) v.getTag());
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.mOnItemClickListener = listener;
    }

    public static class MyHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvContent;
        TextView tvDate;
        ImageView imgIcon;

        public MyHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.item_to_do_title);
            tvContent = (TextView) itemView.findViewById(R.id.tv_content);
            tvDate = (TextView) itemView.findViewById(R.id.item_apply_date);
            imgIcon = (ImageView) itemView.findViewById(R.id.img_setting);


        }
    }
}