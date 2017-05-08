package org.eenie.wgj.ui.login;

import android.view.View;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;

import butterknife.OnClick;

/**
 * Created by Eenie on 2017/5/8 at 14:58
 * Email: 472279981@qq.com
 * Des:
 */

public class SelectCompanyWayFragment extends BaseSupportFragment {
    @Override
    protected int getContentView() {
        return R.layout.fragment_select_company;
    }

    @Override
    protected void updateUI() {

    }
    @OnClick({R.id.img_back,R.id.img_join_company,R.id.img_create_company})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:


                break;
            case R.id.img_join_company:


                break;
            case R.id.img_create_company:


                break;
        }
    }
}
