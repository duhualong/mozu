package org.eenie.wgj.ui.meeting;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.ui.meeting.launchmeeting.LaunchMeetingActivity;
import org.eenie.wgj.util.PermissionManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/7/7 at 16:45
 * Email: 472279981@qq.com
 * Des:
 */

public class MeetingSettingActivity extends BaseActivity {
    private static final int REQUEST_CAMERA_PERMISSION = 0x101;
    @BindView(R.id.root_view)View rootView;
    private boolean permission;
    @Override
    protected int getContentView() {
        return R.layout.activity_meeting_setting;
    }

    @Override
    protected void updateUI() {
        checkPermission();

    }
    @OnClick({R.id.img_back,R.id.rl_launch_meeting,R.id.rl_apply_meeting_class,R.id.rl_meeting_arrange,
    R.id.rl_meeting_feedback})public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();


                break;
            case R.id.rl_launch_meeting:
                startActivity(new Intent(context, LaunchMeetingActivity.class));


                break;
            case R.id.rl_apply_meeting_class:


                break;
            case R.id.rl_meeting_arrange:

                break;
            case R.id.rl_meeting_feedback:


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
                permission=true;


            } else {
                showRequestPermissionDialog();
            }
        } else {
            permission=true;

        }
    }


    /**
     * 请求权限Snackbar
     */
    private void showRequestPermissionDialog() {
        if (this.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Snackbar.make(rootView, "请提供摄像头及文件权限，以拍摄和预览相机图片!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", v -> {
                        PermissionManager.requestCamera(MeetingSettingActivity.this, REQUEST_CAMERA_PERMISSION);
                    }).show();

        } else {
            PermissionManager.requestCamera(MeetingSettingActivity.this, REQUEST_CAMERA_PERMISSION);
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
                    permission=true;
                } else {
                    Snackbar.make(rootView, "请完整的权限，以预览裁剪图片!", Snackbar.LENGTH_SHORT).show();
                }

                break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
