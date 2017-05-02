package org.eenie.wgj.ui.personal;

import android.view.View;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import butterknife.OnClick;

/**
 * Created by Eenie on 2017/5/2 at 12:04
 * Email: 472279981@qq.com
 * Des:
 */

public class PersonalInformationActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_personal_information;
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
