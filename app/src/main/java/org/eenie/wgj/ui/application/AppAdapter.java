package org.eenie.wgj.ui.application;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.chad.library.adapter.base.BaseItemDraggableAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.eenie.wgj.R;
import org.eenie.wgj.data.local.HomeModule;

import java.util.List;

import io.realm.Realm;

/**
 * Created by Eenie on 2017/8/1 at 9:18
 * Email: 472279981@qq.com
 * Des:
 */

public class AppAdapter extends BaseItemDraggableAdapter<HomeModule, BaseViewHolder> {


    public interface ItemActionListener {

        void onItemAdd(View itemView, HomeModule module, int position);

        void onItemRemove(View itemView, HomeModule module, int position);

    }


    private boolean isEdit;

    ItemActionListener mItemActionListener;


    public AppAdapter(List<HomeModule> data, ItemActionListener listener) {
        super(R.layout.item_module_layout, data);
        this.mItemActionListener = listener;
    }

    @Override
    protected void convert(final BaseViewHolder holder, final HomeModule module) {


        int iconRes = mContext.getResources().getIdentifier(module.getIconRes(), "mipmap", mContext.getPackageName());


        holder.setText(R.id.tv_module_name, module.getModuleName())
                .setImageResource(R.id.iv_module_ic, iconRes);


//        mContext.getResources().getIdentifier()


        if (module.isVIP()) {
            holder.getView(R.id.iv_vip_flag).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.iv_vip_flag).setVisibility(View.GONE);
        }

        ImageView actionView = holder.getView(R.id.img_action);

        if (module.isShow()) {
            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    module.setIndex(holder.getPosition());
                }
            });

            actionView.setImageResource(R.mipmap.ic_home_module_remove);
        } else {

            Realm.getDefaultInstance().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    module.setIndex(holder.getPosition() + 10);
                }
            });

            actionView.setImageResource(R.mipmap.ic_home_module_add);
        }


        actionView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (module.isShow()) {
                    mItemActionListener.onItemRemove(holder.itemView, module, holder.getPosition());
                } else {
                    mItemActionListener.onItemAdd(holder.itemView, module, holder.getPosition());
                }
            }
        });


        if (isEdit) {
            holder.itemView.setOnClickListener(null);
            actionView.setVisibility(View.VISIBLE);
        } else {
            actionView.setVisibility(View.GONE);
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (module.getTarget() != null) {
                        mContext.startActivity(new Intent(mContext, module.getTarget()));
                    } else {
                       // ToastUtil.showToast(module.getModuleName() + "模块开发中");
                    }
                }
            });
        }
    }
//ApplicationFragment
    public void startEditMode() {
        isEdit = true;
        notifyDataSetChanged();
    }

    public void stopEditMode() {
        isEdit = false;
        notifyDataSetChanged();
    }


}