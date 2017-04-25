package org.eenie.wgj.ui.contacts;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import org.eenie.wgj.R;

import java.util.List;

import me.zhouzhuo.zzletterssidebar.adapter.BaseSortListViewAdapter;
import me.zhouzhuo.zzletterssidebar.viewholder.BaseViewHolder;

/**
 * Created by Eenie on 2017/4/24 at 14:55
 * Email: 472279981@qq.com
 * Des:
 */

public class PersonRecyclerViewAdapter extends BaseSortListViewAdapter<PersonEntity, PersonRecyclerViewAdapter.ViewHolder> {

    public PersonRecyclerViewAdapter(Context ctx, List<PersonEntity> datas) {
        super(ctx, datas);
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_item;
    }

    @Override
    public ViewHolder getViewHolder(View view) {
        ViewHolder viewHolder = new ViewHolder();
        viewHolder.tvName = (TextView) view.findViewById(R.id.list_item_tv_name);
        return viewHolder;
    }

    @Override
    public void bindValues(ViewHolder viewHolder, int position) {
        viewHolder.tvName.setText(mDatas.get(position).getPersonName());
    }

    public static class ViewHolder extends BaseViewHolder {
        protected TextView tvName;
    }

}