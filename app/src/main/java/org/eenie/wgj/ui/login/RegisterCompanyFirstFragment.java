package org.eenie.wgj.ui.login;

import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;

/**
 * Created by Eenie on 2017/5/3 at 14:48
 * Email: 472279981@qq.com
 * Des:
 */

public class RegisterCompanyFirstFragment extends BaseFragment {
    @BindView(R.id.root_view)View rootView;
    @BindView(R.id.edit_company_address)EditText etCompanyAddress;

    @Override
    protected int getContentView() {
        return R.layout.register_company_information_first;
    }

    @Override
    protected void updateUI() {

    }
    @OnClick({R.id.img_back,R.id.btn_next})public void onClick(View view){
        String companyAddress=etCompanyAddress.getText().toString();
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();

                break;
            case R.id.btn_next:
                if (TextUtils.isEmpty(companyAddress)){
                    Snackbar.make(rootView,"请填写公司的详细地址",Snackbar.LENGTH_LONG).show();

                }else {
                    //跳转到下一个页面
                    fragmentMgr.beginTransaction()
                            .addToBackStack(TAG)
                            .replace(R.id.fragment_login_container,
                                    new RegisterCompanySecondFragment())
                            .commit();

                }
                break;
        }
    }
}
