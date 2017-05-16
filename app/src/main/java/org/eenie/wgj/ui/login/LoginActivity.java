package org.eenie.wgj.ui.login;

import android.app.Fragment;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import cn.jpush.android.api.JPushInterface;


/**
 * 登录注册的容器
 */

public class LoginActivity extends BaseActivity {
    @Override
    protected int getContentView() {
        return R.layout.activity_login_container;
    }

    @Override
    protected void updateUI() {
        JPushInterface.resumePush(getApplicationContext());
        Fragment fragment = fragmentMgr.findFragmentById(R.id.fragment_login_container);
        if (fragment == null) {
            fragment = new LoginFragment();
            fragmentMgr.beginTransaction().add(R.id.fragment_login_container, fragment).commit();
        }

    }
}
