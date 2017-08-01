package org.eenie.wgj.ui.fragment;

import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import org.eenie.wgj.R;
import org.eenie.wgj.data.local.HomeModule;

import java.util.List;

/**
 * Created by Eenie on 2017/8/1 at 10:59
 * Email: 472279981@qq.com
 * Des:
 */

public class ModuleAdapter  extends BaseQuickAdapter<HomeModule, BaseViewHolder> {
    public ModuleAdapter(List<HomeModule> data) {
        super(R.layout.item_module_layout, data);
    }

    @Override
    protected void convert(BaseViewHolder holder, final HomeModule module) {

        int iconRes = mContext.getResources().getIdentifier(module.getIconRes(), "mipmap", mContext.getPackageName());
        holder.setText(R.id.tv_module_name, module.getModuleName())
                .setImageResource(R.id.iv_module_ic, iconRes);
        if (module.isVIP()) {
            holder.getView(R.id.iv_vip_flag).setVisibility(View.VISIBLE);
        } else {
            holder.getView(R.id.iv_vip_flag).setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (module.getTarget() != null) {
                    mContext.startActivity(new Intent(mContext, module.getTarget()));
                } else {
                    Toast.makeText(mContext,"模块开发中",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }



}
