package org.eenie.wgj.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.ui.personal.PersonalBaseInfoActivity;
import org.eenie.wgj.util.PermissionManager;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Eenie on 2017/4/10 at 11:13
 * Email: 472279981@qq.com
 * Des:
 */

public class PersonalCenterFragment extends BaseSupportFragment {
    private static final int REQUEST_CAMERA_PERMISSION =1 ;
    @BindView(R.id.img_avatar)CircleImageView avatar;
    @BindView(R.id.personal_name)TextView name;
    @BindView(R.id.personal_phone)TextView phone;
    @BindView(R.id.mroot_view)View rootView;


    @Override
    protected int getContentView() {
        return R.layout.fragment_personal_center;
    }

    @Override
    protected void updateUI() {

    }
    @OnClick({R.id.img_scan,R.id.rl_base_information,R.id.rl_alert_setting,R.id.rl_version})
    public void onClick(View view){
        switch (view.getId()){
            case R.id.img_scan:
            case R.id.rl_base_information:
                checkPermission();


                break;
            case R.id.rl_alert_setting:



                break;
            case R.id.rl_version:


                break;

        }
    }



    /**
     * 针对高版本系统检查权限
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPermission = PermissionManager.checkCameraPermission(context)
                    && PermissionManager.checkWriteExternalStoragePermission(context);
            if (hasPermission) {
                startActivity(new Intent(context, PersonalBaseInfoActivity.class));

            } else {
                showRequestPermissionDialog();
            }
        } else {
            startActivity(new Intent(context, PersonalBaseInfoActivity.class));

        }
    }



    /**
     * 请求权限Snackbar
     */
    private void showRequestPermissionDialog() {
        if (this.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Snackbar.make(rootView, "请提供摄像头及文件权限，以拍摄和预览相机图片!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", v -> {
                        PermissionManager.invokeCameras(PersonalCenterFragment.this, REQUEST_CAMERA_PERMISSION);
                    })
                    .show();
        } else {
            PermissionManager.invokeCameras(PersonalCenterFragment.this, REQUEST_CAMERA_PERMISSION);
        }
    }


    /**
     * 权限申请回调
     *
     * @param requestCode  请求码
     * @param permissions  请求权限
     * @param grantResults 请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:

                boolean isCanCapturePhoto = true;

                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        isCanCapturePhoto = false;
                    }
                }
                if (isCanCapturePhoto) {
                    startActivity(new Intent(context, PersonalBaseInfoActivity.class));
                } else {
                    Snackbar.make(rootView, "请完整的权限，以预览裁剪图片!", Snackbar.LENGTH_SHORT).show();
                }

                break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
