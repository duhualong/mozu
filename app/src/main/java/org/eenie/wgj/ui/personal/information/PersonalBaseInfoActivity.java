package org.eenie.wgj.ui.personal.information;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.view.View;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.ui.login.LoginActivity;
import org.eenie.wgj.util.Constants;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/4/25 at 10:53
 * Email: 472279981@qq.com
 * Des:
 */

public class PersonalBaseInfoActivity extends BaseActivity {
    @BindView(R.id.root_view)
    View rootView;

    @Override
    protected int getContentView() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void updateUI() {

    }

    @OnClick({R.id.img_back, R.id.img_scan, R.id.rl_avatar_img, R.id.rl_identity_card, R.id.rl_personal_information,
            R.id.rl_bank_card, R.id.rl_security_certificate,R.id.btn_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:

                onBackPressed();
                break;
            case R.id.img_scan:
                startActivity(new Intent(context, PersonalBarcodeActivity.class));
                break;
            case R.id.rl_avatar_img:
                showAvatarDialog();

                break;

            case R.id.rl_identity_card:
                showCardPersonal();


                break;
            case R.id.rl_personal_information:
                startActivity(new Intent(context, PersonalInformationActivity.class));
                break;
            case R.id.rl_bank_card:
                startActivity(new Intent(context, PersonalBindBankActivity.class));


                break;
            case R.id.rl_security_certificate:
                startActivity(new Intent(context, PersonalSecurityActivity.class));


                break;
            case R.id.btn_logout:
                //退出登录
                mPrefsHelper.getPrefs().edit().putBoolean(Constants.IS_LOGIN,false).apply();

                startActivity(new Intent(context, LoginActivity.class));
                finish();

                break;

        }
    }

    //身份证信息
    private void showCardPersonal() {
        View view = View.inflate(context, R.layout.dialog_personal_identity_card, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();

    }


    //上传头像

    private void showAvatarDialog() {
        View view = View.inflate(context, R.layout.dialog_personal_avatar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.tv_camera_personal).setOnClickListener(v -> {

        });
        dialog.getWindow().findViewById(R.id.tv_photo_personal).setOnClickListener(v -> {

        });


    }


}
