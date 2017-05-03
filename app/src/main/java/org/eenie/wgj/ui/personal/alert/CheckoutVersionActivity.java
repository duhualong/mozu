package org.eenie.wgj.ui.personal.alert;

import android.view.View;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by Eenie on 2017/5/3 at 17:40
 * Email: 472279981@qq.com
 * Des:
 */

public class CheckoutVersionActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_version_setting;
    }

    @Override
    protected void updateUI() {

    }
    @OnClick({R.id.img_back})public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
}
