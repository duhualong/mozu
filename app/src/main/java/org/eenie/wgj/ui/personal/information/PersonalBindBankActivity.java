package org.eenie.wgj.ui.personal.information;

import android.view.View;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by Eenie on 2017/4/27 at 14:16
 * Email: 472279981@qq.com
 * Des:
 */

public class PersonalBindBankActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_personal_bank_card;
    }

    @Override
    protected void updateUI() {

    }
    @OnClick({R.id.img_back,R.id.btn_login})public void onClick(View view){
        switch (view.getId()){
            case R.id.btn_login:


                break;
            case R.id.img_back:

                onBackPressed();
                break;
        }
    }
}
