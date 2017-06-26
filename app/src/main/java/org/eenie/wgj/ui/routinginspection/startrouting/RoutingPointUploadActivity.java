package org.eenie.wgj.ui.routinginspection.startrouting;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.yalantis.ucrop.UCrop;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.response.routing.StartRoutingResponse;
import org.eenie.wgj.util.ImageUtils;
import org.eenie.wgj.util.PermissionManager;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.eenie.wgj.R.id.tv_camera_personal;

/**
 * Created by Eenie on 2017/6/26 at 16:13
 * Email: 472279981@qq.com
 * Des:
 */

public class RoutingPointUploadActivity extends BaseActivity implements AMapLocationListener {
    private static final int REQUEST_LOCATION_CODE = 0x101;
    private static final int REQUEST_CAMERA_FIRST = 0x102;
    private static final int REQUEST_CAMERA_SECOND =0x103 ;
    private static final int REQUEST_CAMERA_THIRD =0x104 ;
    private static final int RESPONSE_CODE_FIRST = 0x105;
    private static final int REQUEST_PHOTO_FIRST =0x106 ;
    private static final int RESPONSE_CODE_SECOND =0x107 ;
    private static final int REQUEST_PHOTO_SECOND = 0x108;
    private static final int RESPONSE_CODE_THIRD = 0x109;
    private static final int REQUEST_PHOTO_THIRD =0x110 ;


    private static final int REQUEST_CAMERA_ONE = 0x200;
    private static final int REQUEST_CAMERA_TWO =0x201 ;
    private static final int REQUEST_CAMERA_THREE =0x202 ;
    private static final int RESPONSE_CODE_ONE = 0x203;
    private static final int RESPONSE_CODE_TWO =0x204 ;
    private static final int RESPONSE_CODE_THREE = 0x205;
    private static final int REQUEST_PHOTO_ONE =0x206 ;
    private static final int REQUEST_PHOTO_TWO = 0x207;
    private static final int REQUEST_PHOTO_THREE =0x208 ;

    @BindView(R.id.root_view)
    View rootView;
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mlocationClient = null;
    public static final String INFO = "info";
    private int type;
    @BindView(R.id.routing_time)
    TextView tvRoutingTime;
    @BindView(R.id.routing_address)
    TextView tvRoutingAddress;
    @BindView(R.id.routing_content)
    TextView tvRoutingContent;
    @BindView(R.id.img_first)
    ImageView imgFirst;
    @BindView(R.id.img_second)
    ImageView imgSecond;
    @BindView(R.id.img_third)
    ImageView imgThird;
    @BindView(R.id.checkbox_select_normal)
    CheckBox checkBoxNormal;
    @BindView(R.id.checkbox_select_abnormal)
    CheckBox checkBoxAbnormal;
    @BindView(R.id.line_abnormal)
    LinearLayout mLinearLayout;
    @BindView(R.id.edit_abnormal_content)
    EditText editAbnormalContent;
    @BindView(R.id.img_one)
    ImageView imgOne;
    @BindView(R.id.img_two)
    ImageView imgTwo;
    @BindView(R.id.img_three)
    ImageView imgThree;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private StartRoutingResponse.InfoBean data;
    private  int situation=2;
    private Uri mFirstUri;
    private Uri mSecondUri;
    private Uri mThirdUri;
    private Uri mOneUri;
    private Uri mTwoUri;
    private Uri mThreeUri;

    @Override
    protected int getContentView() {
        return R.layout.activity_routing_point_upload;
    }

    @Override
    protected void updateUI() {
        checkLocationPermission();
        data = getIntent().getParcelableExtra(INFO);
        if (data != null) {
            tvRoutingTime.setText("巡检时间：" + data.getInspectiontime());
            tvRoutingAddress.setText("巡检地点：" + data.getInspectionname());
            tvRoutingContent.setText("巡检内容：" + data.getInspectioncontent());
        }


    }

    private void initMapView() {
        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(60000);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
        mlocationClient.startLocation();
    }

    @OnClick({R.id.img_back, R.id.tv_apply_ok, R.id.rl_select_notice, R.id.img_one, R.id.img_two,
            R.id.img_three, R.id.img_first, R.id.img_second, R.id.img_third,
            R.id.checkbox_select_normal, R.id.checkbox_select_abnormal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_apply_ok:

                break;
            case R.id.checkbox_select_normal:
                if (checkBoxNormal.isChecked()){
                    checkBoxAbnormal.setChecked(false);

                }else {
                    checkBoxAbnormal.setChecked(true);

                }




                break;
            case R.id.checkbox_select_abnormal:
                if (checkBoxAbnormal.isChecked()){
                    checkBoxNormal.setChecked(false);
                }else {
                    checkBoxNormal.setChecked(true);

                }


                break;
            case R.id.rl_select_notice:


                break;
            case R.id.img_one:
                showUploadDialog(REQUEST_CAMERA_ONE,REQUEST_PHOTO_ONE);

                break;
            case R.id.img_two:
                showUploadDialog(REQUEST_CAMERA_TWO,REQUEST_PHOTO_TWO);

                break;
            case R.id.img_three:
                showUploadDialog(REQUEST_CAMERA_THREE,REQUEST_PHOTO_THREE);
                break;
            case R.id.img_first:
                showUploadDialog(REQUEST_CAMERA_FIRST,REQUEST_PHOTO_FIRST);


                break;
            case R.id.img_second:
                showUploadDialog(REQUEST_CAMERA_SECOND,REQUEST_PHOTO_SECOND);


                break;
            case R.id.img_third:
                showUploadDialog(REQUEST_CAMERA_THIRD,REQUEST_PHOTO_THIRD);

                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK ) {

            switch (requestCode) {
                case REQUEST_CAMERA_ONE:
                    startCropImage(mOneUri, RESPONSE_CODE_ONE);

                    break;
                case REQUEST_PHOTO_ONE:
                    startCropImage(data.getData(), RESPONSE_CODE_ONE);

                    break;
                case  RESPONSE_CODE_ONE:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgOne))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgOne.setScaleType(ImageView.ScaleType.FIT_XY);
                                imgOne.setImageBitmap(bitmap);
                                imgTwo.setVisibility(View.VISIBLE);

                            });
//                    firstPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));
//                    firstFile=new File(firstPath);

                    break;
                case REQUEST_CAMERA_TWO:
                    startCropImage(mTwoUri, RESPONSE_CODE_TWO);

                    break;
                case REQUEST_PHOTO_TWO:
                    startCropImage(data.getData(), RESPONSE_CODE_TWO);

                    break;
                case  RESPONSE_CODE_TWO:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgTwo))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgTwo.setScaleType(ImageView.ScaleType.FIT_XY);
                                imgTwo.setImageBitmap(bitmap);
                                imgThree.setVisibility(View.VISIBLE);

                            });
//                    firstPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));
//                    firstFile=new File(firstPath);

                    break;
                case REQUEST_CAMERA_THREE:
                    startCropImage(mThreeUri, RESPONSE_CODE_THREE);

                    break;
                case REQUEST_PHOTO_THREE:
                    startCropImage(data.getData(), RESPONSE_CODE_THREE);

                    break;
                case  RESPONSE_CODE_THREE:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgThree))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgThree.setScaleType(ImageView.ScaleType.FIT_XY);
                                imgThree.setImageBitmap(bitmap);

                            });
//                    firstPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));
//                    firstFile=new File(firstPath);

                    break;
                case REQUEST_CAMERA_FIRST:
                    startCropImage(mFirstUri, RESPONSE_CODE_FIRST);

                    break;
                case REQUEST_PHOTO_FIRST:
                    startCropImage(data.getData(), RESPONSE_CODE_FIRST);
                    break;
                case RESPONSE_CODE_FIRST:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgFirst))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgFirst.setScaleType(ImageView.ScaleType.FIT_XY);
                                imgFirst.setImageBitmap(bitmap);
                                imgSecond.setVisibility(View.VISIBLE);

                            });
//                    firstPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));
//                    firstFile=new File(firstPath);

                    break;
                case REQUEST_CAMERA_SECOND:
                    startCropImage(mSecondUri, RESPONSE_CODE_SECOND);
                    break;

                case REQUEST_PHOTO_SECOND:
                    startCropImage(data.getData(), RESPONSE_CODE_SECOND);
                    break;

                case RESPONSE_CODE_SECOND:

                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgSecond))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgSecond.setScaleType(ImageView.ScaleType.FIT_XY);
                                imgSecond.setImageBitmap(bitmap);
                                imgThird.setVisibility(View.VISIBLE);

                            });

//                    secondPath= ImageUtils.getRealPath(context, UCrop.getOutput(data));
//                    secondFile=new File(secondPath);


                    break;

                case REQUEST_CAMERA_THIRD:
                    startCropImage(mThirdUri, RESPONSE_CODE_THIRD);
                    break;

                case REQUEST_PHOTO_THIRD:
                    startCropImage(data.getData(), RESPONSE_CODE_THIRD);
                    break;
                case RESPONSE_CODE_THIRD:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data),imgThird))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgThird.setScaleType(ImageView.ScaleType.FIT_XY);
                                imgThird.setImageBitmap(bitmap);

                            });
//                    thirdPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));
//                    thirdFile=new File(thirdPath);



//                    }

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showUploadDialog(int camera,int photo) {
        View view = View.inflate(context, R.layout.dialog_personal_avatar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        dialog.getWindow().findViewById(tv_camera_personal).setOnClickListener(v -> {
            dialog.dismiss();
            showPhotoSelectDialog(camera);
            // startCapturePhoto(camera);


        });

        dialog.getWindow().findViewById(R.id.tv_photo_personal).setOnClickListener(v -> {
            dialog.dismiss();
            startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"),
                    photo);
        });


    }
    private void showPhotoSelectDialog(int camera) {
        switch (camera){
            case REQUEST_CAMERA_ONE:
                mOneUri = createImageUri(context);
                Intent mintent = new Intent();
                mintent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                mintent.putExtra(MediaStore.EXTRA_OUTPUT, mOneUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
                startActivityForResult(mintent, camera);

                break;
            case REQUEST_CAMERA_TWO:
                mTwoUri = createImageUri(context);
                Intent mintents = new Intent();
                mintents.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                mintents.putExtra(MediaStore.EXTRA_OUTPUT, mTwoUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
                startActivityForResult(mintents, camera);
                break;
            case REQUEST_CAMERA_THREE:
                mThreeUri = createImageUri(context);
                Intent mIntentes = new Intent();
                mIntentes.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                mIntentes.putExtra(MediaStore.EXTRA_OUTPUT, mThreeUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
                startActivityForResult(mIntentes, camera);
                break;
            case REQUEST_CAMERA_FIRST:
                mFirstUri = createImageUri(context);
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mFirstUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
                startActivityForResult(intent, camera);

                break;
            case REQUEST_CAMERA_SECOND:
                mSecondUri = createImageUri(context);
                Intent intents = new Intent();
                intents.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intents.putExtra(MediaStore.EXTRA_OUTPUT, mSecondUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
                startActivityForResult(intents, camera);


                break;

            case REQUEST_CAMERA_THIRD:
                mThirdUri = createImageUri(context);
                Intent intentes = new Intent();
                intentes.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intentes.putExtra(MediaStore.EXTRA_OUTPUT, mThirdUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
                startActivityForResult(intentes, camera);
                break;

        }



    }
    private static Uri createImageUri(Context context) {
        String name = "takePhoto" + System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, name);
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        return uri;
    }

    /**
     * 裁剪图片
     *
     * @param resUri      图片uri
     * @param requestCode 请求码
     */
    private void startCropImage(Uri resUri, int requestCode) {
        File cropFile = new File(context.getCacheDir(), "a.jpg");
        UCrop.of(resUri, Uri.fromFile(cropFile))
                .withAspectRatio(1, 1)
                .withMaxResultSize(100, 100)
                .start(RoutingPointUploadActivity.this, requestCode);



    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation.getErrorCode() == 0) {
            //定位成功回调信息，设置相关消息
            String address = aMapLocation.getAddress();
            double mLat = aMapLocation.getLatitude();
            double mLong = aMapLocation.getLongitude();
            System.out.println("address" + address + "\n" + "经度" + mLong + "\n纬度：" + mLat);

        } else {
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
            Log.e("AmapError", "location Error, ErrCode:"
                    + aMapLocation.getErrorCode() + ", errInfo:"
                    + aMapLocation.getErrorInfo());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mlocationClient != null) {
            mlocationClient.onDestroy(); // 销毁定位客户端
        }
    }


    /**
     * 检查是否有定位权限
     */
    private void checkLocationPermission() {
        if (PermissionManager.checkLocation(this)) {
            //定位
            initMapView();

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(rootView, "请提供完整权限，以定位您当前位置!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", v -> {
                            PermissionManager.requestLocation(this, REQUEST_LOCATION_CODE);
                        });
            } else {
                PermissionManager.requestLocation(this, REQUEST_LOCATION_CODE);
            }
        }
    }

    // 请求权限后的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[0])
                    && PackageManager.PERMISSION_GRANTED == grantResults[0]) { // 请求定位权限被允许
                initMapView();

            } else { // 请求定位权限被拒绝
                Snackbar.make(rootView, "请提供完整权限，以定位您当前位置!", Snackbar.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
