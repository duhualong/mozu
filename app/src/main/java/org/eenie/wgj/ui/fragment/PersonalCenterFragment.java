package org.eenie.wgj.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.UserId;
import org.eenie.wgj.model.response.UserInforById;
import org.eenie.wgj.ui.personal.alert.CheckoutVersionActivity;
import org.eenie.wgj.ui.personal.alert.PersonalMessageAlertActivity;
import org.eenie.wgj.ui.personal.information.PersonalBaseInfoActivity;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.PermissionManager;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/4/10 at 11:13
 * Email: 472279981@qq.com
 * Des:
 */

public class PersonalCenterFragment extends BaseSupportFragment {
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    @BindView(R.id.img_avatar)
    CircleImageView avatar;
    @BindView(R.id.personal_name)
    TextView name;
    @BindView(R.id.personal_phone)
    TextView phone;
    @BindView(R.id.mroot_view)
    View rootView;
    private UserInforById mData;


    @Override
    protected int getContentView() {
        return R.layout.fragment_personal_center;
    }

    @Override
    protected void updateUI() {
        String userId = mPrefsHelper.getPrefs().getString(Constants.UID, "");
        if (!TextUtils.isEmpty(userId)) {
            getUerInformationById(userId);


        }


    }

    @OnClick({R.id.img_scan, R.id.rl_base_information, R.id.rl_alert_setting, R.id.rl_version})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_scan:
            case R.id.rl_base_information:
                checkPermission();
                break;
            case R.id.rl_alert_setting:
                startActivity(new Intent(context, PersonalMessageAlertActivity.class));


                break;
            case R.id.rl_version:
                startActivity(new Intent(context, CheckoutVersionActivity.class));


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
//            Intent intent = new Intent(context, PersonalBaseInfoActivity.class);
//            if (TextUtils.isEmpty(mData.getAvatar())){
//                intent.putExtra(PersonalBaseInfoActivity.AVATAR,
//                        Constant.DOMIN+mData.getId_card_head_image());
//
//            }else {
//                intent.putExtra(PersonalBaseInfoActivity.AVATAR,
//                        Constant.DOMIN+mData.getAvatar());
//            }
            startActivity(new Intent(context,PersonalBaseInfoActivity.class));

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


    private void getUerInformationById(String userId) {
        UserId mUser = new UserId(userId);
        mSubscription = mRemoteService.getUserInfoById(mUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiResponse<UserInforById>>() {
                    @Override
                    public void onSuccess(ApiResponse<UserInforById> value) {
                        if (value.getResultCode() == 200) {
                            mData = value.getData();
                            if (mData != null) {
                                if (!TextUtils.isEmpty(mData.getSecurity_card())){
                                    mPrefsHelper.getPrefs().edit().
                                            putString(Constants.SECURITY_CARD,
                                                    mData.getSecurity_card())
                                            .apply();
                                }
                                if (!TextUtils.isEmpty(mData.getBank_card())){
                                    mPrefsHelper.getPrefs().edit().
                                            putString(Constants.BANK_CARD,
                                                    mData.getBank_card())
                                            .apply();
                                }
                                if (TextUtils.isEmpty(mData.getAvatar())) {

                                    Glide.with(context)
                                            .load(Constant.DOMIN + mData.getId_card_head_image())
                                            .asBitmap()
                                            .centerCrop()
                                            .into(avatar);
                                    mPrefsHelper.getPrefs().edit().
                                            putString(Constants.PERSONAL_AVATAR,
                                                    Constant.DOMIN + mData.getId_card_head_image())
                                            .apply();

                                } else {
                                    Glide.with(context)
                                            .load(Constant.DOMIN + mData.getAvatar())
                                            .asBitmap()
                                            .centerCrop()
                                            .into(avatar);
                                    mPrefsHelper.getPrefs().edit().
                                            putString(Constants.PERSONAL_AVATAR,
                                                    Constant.DOMIN + mData.getAvatar())
                                            .apply();
                                }
                                if (!TextUtils.isEmpty(mData.getName())) {
                                    name.setText(mData.getName());
                                }
                                if (!TextUtils.isEmpty(mData.getUsername())) {
                                    phone.setText(mData.getUsername());
                                }


                            }

                        } else {
                            Snackbar.make(rootView, value.getResultMessage(),
                                    Snackbar.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(context, "参数错误", Toast.LENGTH_LONG).show();

                    }
                });

    }

    @Override
    public void onResume() {
        super.onResume();
//        String userId = mPrefsHelper.getPrefs().getString(Constants.UID, "");
//        getUerInformationById(userId);
    }
}
