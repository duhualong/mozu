package org.eenie.wgj.ui.login;

import android.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;

/**
 * Created by Eenie on 2017/5/3 at 11:50
 * Email: 472279981@qq.com
 * Des:
 */

public class RegisterOneFragment extends BaseFragment {

    @BindView(R.id.user_register_tab_bar_container)
    LinearLayout taBbackground;
    @BindView(R.id.property_register_button)
    Button btnManager;
    @BindView(R.id.security_register_button)
    Button btnWork;
    @Override
    protected int getContentView() {
        return R.layout.fragment_register_pager;
    }

    @Override
    protected void updateUI() {
        Fragment fragment = fragmentMgr.findFragmentById(R.id.fragment_login_container);
        if (fragment == null) {
            fragment = new RegisterPersonalFragment();
            fragmentMgr.beginTransaction().add(R.id.fragment_register_container, fragment).commit();
        }
    }
    @OnClick({R.id.security_register_button,R.id.property_register_button})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.security_register_button:
                taBbackground.setBackgroundResource(R.mipmap.bg_user_register_tab_2);
                btnWork.setTextColor(ContextCompat.getColor
                        (context, R.color.white));
                btnManager.setTextColor(ContextCompat.getColor
                        (context, R.color.colorAccent));
                fragmentMgr.beginTransaction()
                        .addToBackStack(TAG)
                        .replace(R.id.fragment_login_container, new RegisterPersonalFragment())
                        .commit();

                break;
            case R.id.property_register_button:
                taBbackground.setBackgroundResource(R.mipmap.bg_user_register_tab_1);
                btnWork.setTextColor(ContextCompat.getColor
                        (context, R.color.colorAccent));
                btnManager.setTextColor(ContextCompat.getColor
                        (context, R.color.white));
                fragmentMgr.beginTransaction()
                        .addToBackStack(TAG)
                        .replace(R.id.fragment_login_container, new RegisterCompanyFragment())
                        .commit();

                break;
        }
    }


}
