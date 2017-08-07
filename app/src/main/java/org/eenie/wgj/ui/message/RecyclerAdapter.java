package org.eenie.wgj.ui.message;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;

import com.bumptech.glide.Glide;
import com.gxz.library.SwapWrapperUtils;
import com.gxz.library.SwipeMenuBuilder;
import com.gxz.library.view.SwipeMenuLayout;
import com.gxz.library.view.SwipeMenuView;

import org.eenie.wgj.R;
import org.eenie.wgj.model.response.message.MessageRequestData;

import java.util.List;

import static org.eenie.wgj.R.mipmap.ic_gift;


/**
 * ================================================
 * 作    者：顾修忠-guxiuzhong@youku.com/gfj19900401@163.com
 * 版    本：
 * 创建日期：16/7/8-下午6:11
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class RecyclerAdapter extends RecyclerView.Adapter {
    private List<MessageRequestData.DataBean> mData;
    private Context mContext;
    private SwipeMenuBuilder swipeMenuBuilder;

    public RecyclerAdapter(List<MessageRequestData.DataBean> mData, Context context) {
        this.mData = mData;
        this.mContext = context;
        swipeMenuBuilder = (SwipeMenuBuilder)mContext;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //根据数据创建右边的操作view
        SwipeMenuView menuView = swipeMenuBuilder.create();
        //包装用户的item布局
        SwipeMenuLayout swipeMenuLayout = SwapWrapperUtils.wrap(parent, R.layout.item_notice_new_pager,
                menuView, new BounceInterpolator(), new LinearInterpolator());
        MyViewHolder holder = new MyViewHolder(swipeMenuLayout);
        setListener(parent, holder, viewType);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        MyViewHolder viewHolder = (MyViewHolder) holder;
        viewHolder.tvDate.setText(mData.get(position).getCreated_at());
        viewHolder.tvTitle.setText(mData.get(position).getTitle());
        viewHolder.tvContent.setText(mData.get(position).getAlert());

        switch (mData.get(position).getKey()) {

            case 1101:
                Glide.with(mContext)
                        .load(R.mipmap.ic_remind)
                        .into(((MyViewHolder) holder).imgSetting);

                break;
            case 2101:
                //考勤

                Glide.with(mContext)
                        .load(R.mipmap.ic_notice)
                        .into(((MyViewHolder) holder).imgSetting);

                // ((MyHolder) holder).imgIcon.setImageResource(R.mipmap.ic_notice);

                break;
            case 1002:
                //报岗
                Glide.with(mContext)
                        .load(R.mipmap.ic_notice)
                        .into(((MyViewHolder) holder).imgSetting);

                break;
            case 1003:
                Glide.with(mContext)
                        .load(R.mipmap.ic_notice)
                        .into(((MyViewHolder) holder).imgSetting);

                //巡检
                break;
            case 2201:
                //会议室审核后
                Glide.with(mContext)
                        .load(R.mipmap.ic_notice)
                        .into(((MyViewHolder) holder).imgSetting);

                break;
            case 2202:
                //会议
                Glide.with(mContext)
                        .load(R.mipmap.ic_notice)
                        .into(((MyViewHolder) holder).imgSetting);

                break;
            case 2203:
                //异常
                Glide.with(mContext)
                        .load(R.mipmap.ic_notice)
                        .into(((MyViewHolder) holder).imgSetting);

                break;
            case 2301:
                //新进员工
                Glide.with(mContext)
                        .load(R.mipmap.ic_notice)
                        .into(((MyViewHolder) holder).imgSetting);

                break;

            case 2302:
                //六天没考勤
                Glide.with(mContext)
                        .load(R.mipmap.ic_notice)
                        .into(((MyViewHolder) holder).imgSetting);
                break;
            case 2303:
                //提醒签合同录指纹
                Glide.with(mContext)
                        .load(R.mipmap.ic_notice)
                        .into(((MyViewHolder) holder).imgSetting);

                break;
            case 2401:
                //交接班注意事项
                Glide.with(mContext)
                        .load(R.mipmap.ic_notice)
                        .into(((MyViewHolder) holder).imgSetting);
                break;
            case 3101:
                //生日提醒
                Glide.with(mContext)
                        .load(ic_gift)
                        .into(((MyViewHolder) holder).imgSetting);


                break;
            case 4101:
                //随手拍
                Glide.with(mContext)
                        .load(R.mipmap.ic_notice)
                        .into(((MyViewHolder) holder).imgSetting);
                break;


        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void remove(int pos) {
        this.notifyItemRemoved(pos);
    }

    protected void setListener(final ViewGroup parent, final MyViewHolder viewHolder, int viewType) {
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    mOnItemClickListener.onItemClick(v, viewHolder, mData.get(position), position);
                }
            }
        });

        viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mOnItemClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    return mOnItemClickListener.onItemLongClick(v, viewHolder, mData.get(position), position);
                }
                return false;
            }
        });
    }


    public OnItemClickListener<MessageRequestData.DataBean> mOnItemClickListener;

    public interface OnItemClickListener<T> {
        void onItemClick(View view, RecyclerView.ViewHolder holder, T o, int position);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, T o, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

}
