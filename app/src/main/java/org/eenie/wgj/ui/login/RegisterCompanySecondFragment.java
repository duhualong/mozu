package org.eenie.wgj.ui.login;

import android.view.View;
import android.widget.ImageView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;

/**
 * Created by Eenie on 2017/5/3 at 15:20
 * Email: 472279981@qq.com
 * Des:
 */

public class RegisterCompanySecondFragment extends BaseFragment {
    @BindView(R.id.root_view)View rootView;
    @BindView(R.id.img_business_licence)ImageView imgBusinessLicence;
    @Override
    protected int getContentView() {
        return R.layout.register_company_information_second;
    }

    @Override
    protected void updateUI() {

    }
    @OnClick({R.id.img_back,R.id.btn_next,R.id.btn_upload_licence_business})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();

                break;
            case R.id.btn_next:
                //跳转下一个页面
                fragmentMgr.beginTransaction()
                        .addToBackStack(TAG)
                        .replace(R.id.fragment_login_container,
                                new RegisterCompanyThirdFragment())
                        .commit();

                break;
            case R.id.btn_upload_licence_business:
            //上传营业执照

                break;
        }
    }
}
