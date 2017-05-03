package org.eenie.wgj.ui.login;

import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ImageView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;

/**
 * Created by Eenie on 2017/5/3 at 15:49
 * Email: 472279981@qq.com
 * Des:
 */

public class RegisterCompanyThirdFragment extends BaseFragment {
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.img_negative)
    ImageView imgNegative;
    @BindView(R.id.img_positive)
    ImageView imgPositive;

    @Override
    protected int getContentView() {
        return R.layout.register_company_information_third;
    }

    @Override
    protected void updateUI() {

    }

    @OnClick({R.id.img_back, R.id.btn_next, R.id.btn_upload_card_front, R.id.btn_upload_card_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:

                onBackPressed();
                break;
            case R.id.btn_next:
                showRegisterDialog();
                break;
            case R.id.btn_upload_card_front:
                //上传身份证正面


                break;
            case R.id.btn_upload_card_back:
                //上传身份证反面

                break;
        }
    }

    private void showRegisterDialog() {
        View view = View.inflate(context, R.layout.dialog_company_success_register, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.btn_ok).setOnClickListener(v -> {
            dialog.dismiss();
            fragmentMgr.beginTransaction()
                    .addToBackStack(TAG)
                    .replace(R.id.fragment_login_container,
                            new LoginFragment())
                    .commit();


        });
    }
}
