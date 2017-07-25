package org.eenie.wgj.ui.attendance.takephoto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.LocationSource;
import com.amap.api.maps.MapView;
import com.amap.api.maps.UiSettings;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.AttendanceAddressInfo;
import org.eenie.wgj.model.response.AttendanceListResponse;
import org.eenie.wgj.ui.attendance.sign.AttendanceResDialog;
import org.eenie.wgj.ui.attendance.sign.AttendanceTokePhotoActivity;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.PermissionManager;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/24 at 15:40
 * Email: 472279981@qq.com
 * Des:
 */

public class NewAttendancenSignInActivity extends BaseActivity implements LocationSource,
        AMapLocationListener,
        AMap.OnMarkerClickListener {
    public static final String INFO = "info";
    private static final int REQUEST_LOCATION_CODE = 0x101;
    @BindView(R.id.tv_location)
    TextView tvMyLocation;
    @BindView(R.id.map_view)
    MapView mMapView;
    @BindView(R.id.btn_take_photo)
    Button mButton;
    @BindView(R.id.tv_sel_rank)
    TextView tvSelectRank;
    @BindView(R.id.activity_map)
    View rootView;
    AMap mAMap;
    //签到点的圈圈
    protected Circle mSignCircle;
    protected Marker mSignMarker;
    //定位点
    protected Marker mLocationMarker;
    //定位Client
    public AMapLocationClient mLocationClient = null;
    //定位Option
    AMapLocationClientOption mLocationClientOption;
    boolean locationSuc = false;


    private PopupMenu menu;
    private int type = 0;
    private LatLng mLatLng;
    public static final int REQUEST_CODE = 0x101;
    private String path;
    private String extraMsg;
    private String address;
    private String mLong;
    private String mLat;
    private int serviceId;


    //地图的UI
    UiSettings mMapUiSetting;

    @Override
    protected int getContentView() {
        return R.layout.activity_sign_in_attandance;
    }

    @Override
    protected void updateUI() {
        AttendanceListResponse data = getIntent().getParcelableExtra(INFO);
        if (data != null) {
            serviceId = data.getService().getId();
            menu = new PopupMenu(context, tvSelectRank);
            initPopRank(data);
            tvSelectRank.setText(data.getService().getServicesname());
            type = 1;
        } else {
            menu = new PopupMenu(context, tvSelectRank);
            initPopRank(data);
            tvSelectRank.setText("日班");
            type = 1;
        }

    }

    @OnClick({R.id.img_back, R.id.btn_take_photo, R.id.tv_sel_rank})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_take_photo:
                if (type == 0) {
                    Toast.makeText(context, "请选择班次", Toast.LENGTH_LONG).show();
                } else {
                    if (!TextUtils.isEmpty(address)) {

                        startActivityForResult(new Intent(context, AttendanceTokePhotoActivity.class),
                                REQUEST_CODE);

                    } else {
                        Toast.makeText(context, "请打开定位权限，允许定位", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_sel_rank:
                showSelRankPop();
                break;


        }
    }

    private void showSelRankPop() {
        menu.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == REQUEST_CODE && resultCode == RESULT_CODE) {
//            path = data.getStringExtra("path");
//
//            if (type == 2) {
//                startActivityForResult(new Intent(context, SignExtraMsgActivity.class),
//                        REQUEST_CODE_NEED_EXTRA);
//            } else {
//                // signIn(path);
//                signIn(getMultipartBody(path, mLong, mLat, type, serviceId, address, ""));
//            }
//        } else if (requestCode == REQUEST_CODE_NEED_EXTRA && resultCode == RESULT_CODE) {
//            extraMsg = data.getStringExtra("extra_msg");
//            signIn(getMultipartBody(path, mLong, mLat, type, serviceId, address, extraMsg));
//
//        }
    }

    public MultipartBody getMultipartBody(String path, String mLong, String mLat, int type, int serviceId,
                                          String address, String content) {
        File file = new File(path);
        MultipartBody.Builder builder = new MultipartBody.Builder();

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                file);
        builder.addFormDataPart("image", file.getName(), requestBody);
        builder.addFormDataPart("longitude", mLong)
                .addFormDataPart("latitude", mLat)
                .addFormDataPart("serviceid", String.valueOf(serviceId))
                .addFormDataPart("type", String.valueOf(type))
                .addFormDataPart("address", address)
                .addFormDataPart("description", content);
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }


    private void signIn(RequestBody requestBody) {
        mButton.setClickable(false);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.DOMIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService fileUploadService = retrofit.create(FileUploadService.class);
        Call<ApiResponse> call = fileUploadService.
                signInAttendance(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""), requestBody);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body() != null) {

                    if (response.body().getCode() == 0) {
                        if (response.body().getMessage().equals("恭喜你签到第1名")) {
                            AttendanceResDialog.newInstance("签到结果",
                                    response.body().getMessage(), String.valueOf(2)).show(getFragmentManager(), "signin");
                        } else {
                            AttendanceResDialog.newInstance("签到结果",
                                    response.body().getMessage(), String.valueOf(0)).show(getFragmentManager(), "signin");
                        }
                        mButton.setClickable(false);
                        mButton.setText("已签到");

                    } else {

                        mButton.setClickable(true);
                        AttendanceResDialog.newInstance("签到结果",
                                response.body().getMessage(), String.valueOf(1)).show(getFragmentManager(), "signin");
                    }
                } else {
                    Toast.makeText(context, "网路请求错误", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                mButton.setClickable(true);
                AttendanceResDialog.newInstance("签到结果", "签到失败！",
                        String.valueOf(1)).show(getFragmentManager(), "signin");
            }
        });

    }

    private void initPopRank(AttendanceListResponse data) {
        String name = data.getService().getServicesname();
        if (TextUtils.isEmpty(name)) {
            name = "日班";
        }
        menu.getMenu().add(name);
        menu.getMenu().add("外出");
        menu.getMenu().add("借调");
        menu.getMenu().add("实习");
        menu.setOnMenuItemClickListener(item -> {
            tvSelectRank.setText(item.getTitle());
            switch (item.getTitle().toString()) {
                case "外出":
                    type = 2;
                    break;
                case "借调":
                    type = 3;
                    break;
                case "实习":
                    type = 4;
                    break;
                default:
                    type = 1;
                    break;
            }
            return true;
        });
        menu.setGravity(Gravity.TOP);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
        if (mAMap == null) {
            mAMap = mMapView.getMap();

        }
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClientOption = new AMapLocationClientOption();
        checkLocationPermission();

    }


    /**
     * 检查是否有定位权限
     */
    private void checkLocationPermission() {
        if (PermissionManager.checkLocation(this)) {
            //定位
            startLocation();
            getAttendanceAddress();
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

    private void startLocation() {
        // 初始化定位Client
        mLocationClient = new AMapLocationClient(context);
        mLocationClient.setLocationListener(this);
        initMapSettingUI();
        // 初始化定位参数
        AMapLocationClientOption option = new AMapLocationClientOption();
        setupLocationOption(option);

        //给定位Client设置定位参数
        mLocationClient.setLocationOption(option);
        //启动定位
        mLocationClient.startLocation();
    }

    private void initMapSettingUI() {
        mMapUiSetting = mAMap.getUiSettings();
        mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
        mAMap.setLocationSource(this);// 设置定位监听
        mMapUiSetting.setMyLocationButtonEnabled(true); // 显示默认的定位按钮
        mAMap.setMyLocationEnabled(true);// 可触发定位并显示定位层

    }


    /**
     * Init the location options
     *
     * @param option option to init
     */
    private void setupLocationOption(AMapLocationClientOption option) {
        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置是否返回地址信息（默认返回地址信息）
        option.setNeedAddress(true);
        //设置是否只定位一次,默认为false
        option.setOnceLocation(false);
        //设置是否强制刷新WIFI，默认为强制刷新
        option.setWifiActiveScan(true);
        //设置是否允许模拟位置,默认为false，不允许模拟位置
        option.setMockEnable(false);
        //设置定位间隔,单位毫秒,默认为2000ms
        option.setInterval(10 * 1000L);
    }


    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) {
            LatLng latLng = new LatLng(aMapLocation.getLatitude(), aMapLocation.getLongitude());
            mLatLng = latLng;
            address = aMapLocation.getAddress();
            mLong = String.valueOf(aMapLocation.getLongitude());
            mLat = String.valueOf(aMapLocation.getLatitude());
            if (mLocationMarker == null) {
                mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("我的位置：" + aMapLocation.getAddress());
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_safe_picture));
                mLocationMarker = mAMap.addMarker(markerOptions);
                tvMyLocation.setText(aMapLocation.getAddress());
            } else {
                mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
                mLocationMarker.setPosition(latLng);
            }

        }

    }

    //获取当天个人的考勤信息
    private void getAttendanceAddress() {

        mSubscription = mRemoteService.getAttendanceAddress(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            Gson gson = new Gson();
                            if (apiResponse.getData() != null) {
                                String jsonArray = gson.toJson(apiResponse.getData());
                                AttendanceAddressInfo data =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<AttendanceAddressInfo>() {
                                                }.getType());
                                if (data != null) {
                                    onSignCircleChange(new LatLng(Double.parseDouble(data.getLatitude()),
                                                    Double.parseDouble(data.getLongitude())),
                                            Double.parseDouble(data.getAttendancescope()), data.getAddress());
                                }
                            }
                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    public void onSignCircleChange(LatLng latLng, double radius, String info) {
        if (mSignCircle == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng)
                    .radius(radius)
                    .fillColor(Color.argb(25, 0, 0, 255))
                    .strokeWidth(0);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("考勤点");
            markerOptions.snippet(info);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_attendance_point));
            mSignMarker = mAMap.addMarker(markerOptions);
            mAMap.setOnMarkerClickListener(this);
            mSignCircle = mAMap.addCircle(circleOptions);
            mSignMarker.showInfoWindow();

        } else {
            mSignMarker.setPosition(latLng);
            mSignCircle.setCenter(latLng);
            mSignCircle.setRadius(radius);
        }
    }


    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {

    }

    @Override
    public void deactivate() {
        // AttendanceTestSignInActivityPermissionsDispatcher.startLocationWithCheck(this);

    }


    /**
     * MapView 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * MapView 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * MapView 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mLocationClient != null) {
            mLocationClient.stopLocation(); // 停止定位
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mAMap != null) {
            mAMap = null;
        }
        if (mLocationClient != null) {
            mLocationClient.onDestroy(); // 销毁定位客户端
        }
    }

    // 请求权限后的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[0])
                    && PackageManager.PERMISSION_GRANTED == grantResults[0]) { // 请求定位权限被允许
                startLocation();
                getAttendanceAddress();
            } else { // 请求定位权限被拒绝
                Snackbar.make(rootView, "请提供完整权限，以定位您当前位置!", Snackbar.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
