package org.eenie.wgj.ui.project.attendance;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.Circle;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.AttendanceDay;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.PermissionManager;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/22 at 16:14
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceSettingActivity extends BaseActivity implements AMapLocationListener {
    @BindView(R.id.root_view)
    View rootView;
    private AMapLocationClient mLocationClient;
    private static final int REQUEST_LOCATION_CODE = 0xff;

    public static final String PROJECT_ID = "project_id";
    private AMap mAMap;
    private Marker locationMarker;
    private String mLatLog;
    double radius = 0d;
    @BindView(R.id.map_view)
    MapView mapView;
    @BindView(R.id.tv_sign_in_city)
    TextView tvCity;
    private String mCity;
    @BindView(R.id.tv_sign_in_address)
    TextView tvAddress;
    @BindView(R.id.tv_sign_in_scope)
    TextView tvScope;
    @BindView(R.id.tv_sign_in_work_day)
    TextView tvSingin;
    @BindView(R.id.tv_sign_in_fingerprint)
    TextView tvFinger;
    @BindView(R.id.tv_sign_in_contract)
    TextView tvContractDay;
    private AlertDialog mDialogs;
    private String mScope;
    private String mAttendanceDay;
    private String mFingerTime;
    private String mContractDay;
    private String projectId;
    private String mAddress;
    private LatLng mLatLng;
    //考勤范围
    Circle mLocationCircle;


    @Override
    protected int getContentView() {
        return R.layout.activity_attendance_address_setting;
    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);
        getAttendaceDay();

    }

    private void getAttendaceDay() {
        mSubscription = mRemoteService.getAttendanceInformation(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), projectId)
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
                            String jsonArray = gson.toJson(apiResponse.getData());
                            AttendanceDay attendance = gson.fromJson(jsonArray,
                                    new TypeToken<AttendanceDay>() {
                                    }.getType());
                            if (attendance != null) {
                                mCity=attendance.getArea();
                                mAddress=attendance.getAddress();
                                mContractDay=attendance.getContracttime();
                                mFingerTime=attendance.getFingerprinttime();
                                mAttendanceDay=attendance.getAttendance();
                                mScope=attendance.getAttendancescope();

                                tvCity.setText(attendance.getArea());
                                tvAddress.setText(attendance.getAddress());
                                tvScope.setText(Integer.parseInt(
                                        attendance.getAttendancescope()) + "米");
                                tvSingin.setText(attendance.getAttendance() + "天");
                                tvFinger.setText(attendance.getFingerprinttime() + "天");
                                tvContractDay.setText(attendance.getContracttime() + "天");
                                tvCity.setTextColor(ContextCompat.getColor
                                        (context, R.color.titleColor));
                                tvAddress.setTextColor(ContextCompat.getColor
                                        (context, R.color.titleColor));
                                tvScope.setTextColor(ContextCompat.getColor
                                        (context, R.color.titleColor));
                                tvSingin.setTextColor(ContextCompat.getColor
                                        (context, R.color.titleColor));
                                tvFinger.setTextColor(ContextCompat.getColor
                                        (context, R.color.titleColor));
                                tvContractDay.setTextColor(ContextCompat.getColor
                                        (context, R.color.titleColor));
                                if (!TextUtils.isEmpty(attendance.getLatitude()) && !
                                        TextUtils.isEmpty(attendance.getLongitude())) {
                                     mLatLng = new LatLng(Double.parseDouble(attendance.getLatitude()),
                                            Double.parseDouble(attendance.getLongitude()));//取出经纬度
                                    // mLocationCircle.setCenter(latLng);
                                    moveCamera(mLatLng);
                                    addMarkersToMap(mLatLng, attendance.getAddress());
                                }

                            }else {
                                checkLocationPermission();
                            }


                        }else {
                            checkLocationPermission();
                        }

                    }
                });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapView.onCreate(savedInstanceState);
        if (mAMap == null) {
            mAMap = mapView.getMap();

        }
    }

    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();

//

        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(R.color.text_blue);// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        mAMap.setMyLocationStyle(myLocationStyle);
        mAMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
    }


    @OnClick({R.id.img_back, R.id.tv_save, R.id.rl_sign_in_city, R.id.rl_sign_in_address,
            R.id.rl_sign_in_scope, R.id.rl_sign_in_work_day, R.id.rl_sign_in_fingerprint,
            R.id.rl_sign_in_contract})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_save:
                if (!TextUtils.isEmpty(mScope)&&!TextUtils.isEmpty(mAddress)){
                    AttendanceDay request=new AttendanceDay(mAddress,mAttendanceDay,mScope,
                            mFingerTime,mContractDay, mLatLng.longitude+"",
                            mLatLng.latitude+"",mCity,projectId);
                    addAttendanceDay(request);
                }

                break;

            case R.id.rl_sign_in_city:
                Intent intent = new Intent(context, SelectCityActivity.class);
                intent.putExtra(SelectCityActivity.CITY, mCity);
                startActivityForResult(intent, 1);




                break;
            case R.id.rl_sign_in_address:
                Intent intents = new Intent(context, SearchAddressDetailActivity.class);
                if (!TextUtils.isEmpty(mCity)) {
                    intents.putExtra(SearchAddressDetailActivity.CITY, mCity);
                }
                startActivityForResult(intents, 2);

                break;

            case R.id.rl_sign_in_scope:
                showDialog();


                break;
            case R.id.rl_sign_in_work_day:
                showSignWorkDay();


                break;
            case R.id.rl_sign_in_fingerprint:
                showFingerprintDialog();


                break;
            case R.id.rl_sign_in_contract:
                showSignContractDialog();


                break;

        }

    }

    private void addAttendanceDay(AttendanceDay data) {
        mRemoteService.addAttendanceDaySetting(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), data)
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
                       if (apiResponse.getResultCode()==200||apiResponse.getResultCode()==0){
                           Toast.makeText(AttendanceSettingActivity.this,"编辑成功！",
                                   Toast.LENGTH_SHORT).show();
                           finish();

                       }

                    }
                });

    }

    private void showSignContractDialog() {
        View view = View.inflate(context, R.layout.dialog_set_input_content, null);
        TextView title = (TextView) view.findViewById(R.id.title_dialog);
        title.setText("提示签合同时间");
        EditText inputText = (EditText) view.findViewById(R.id.et_input_content);
        inputText.setHint("例如：20");
        if (!TextUtils.isEmpty(mContractDay)) {
            inputText.setText(mContractDay);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //自定义的布局文件
        mDialogs = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        mDialogs.show();
        mDialogs.getWindow().findViewById(R.id.button_project_cancel).setOnClickListener(v -> {
            mDialogs.dismiss();

        });
        mDialogs.getWindow().findViewById(R.id.button_project_ok).setOnClickListener(v -> {
            String content = inputText.getText().toString().trim();
            if (!TextUtils.isEmpty(content)) {
                mContractDay = content;
                tvContractDay.setText(content + "天");
                tvContractDay.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
                mDialogs.dismiss();
            } else {
                Toast.makeText(AttendanceSettingActivity.this, "请设置签合同天数",
                        Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void showFingerprintDialog() {
        View view = View.inflate(context, R.layout.dialog_set_input_content, null);
        TextView title = (TextView) view.findViewById(R.id.title_dialog);
        title.setText("提示录入指纹时间");
        EditText inputText = (EditText) view.findViewById(R.id.et_input_content);
        inputText.setHint("例如：5");
        if (!TextUtils.isEmpty(mFingerTime)) {
            inputText.setText(mFingerTime);
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //自定义的布局文件
        mDialogs = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        mDialogs.show();
        mDialogs.getWindow().findViewById(R.id.button_project_cancel).setOnClickListener(v -> {
            mDialogs.dismiss();

        });
        mDialogs.getWindow().findViewById(R.id.button_project_ok).setOnClickListener(v -> {
            String content = inputText.getText().toString().trim();
            if (!TextUtils.isEmpty(content)) {
                mFingerTime = content;
                tvFinger.setText(content + "天");
                tvFinger.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
                mDialogs.dismiss();
            } else {
                Toast.makeText(AttendanceSettingActivity.this, "请设置录入指纹天数",
                        Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void showSignWorkDay() {
        View view = View.inflate(context, R.layout.dialog_set_input_content, null);
        TextView title = (TextView) view.findViewById(R.id.title_dialog);

        title.setText("月考勤天数");
        EditText inputText = (EditText) view.findViewById(R.id.et_input_content);
        inputText.setHint("例如：20");
        if (!TextUtils.isEmpty(mAttendanceDay)) {
            inputText.setText(mAttendanceDay);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //自定义的布局文件
        mDialogs = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        mDialogs.show();
        mDialogs.getWindow().findViewById(R.id.button_project_cancel).setOnClickListener(v -> {
            mDialogs.dismiss();

        });
        mDialogs.getWindow().findViewById(R.id.button_project_ok).setOnClickListener(v -> {
            String content = inputText.getText().toString().trim();
            if (!TextUtils.isEmpty(content)) {
                mAttendanceDay = content;
                tvSingin.setText(content + "天");
                tvSingin.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
                mDialogs.dismiss();
                mDialogs.dismiss();
            } else {
                Toast.makeText(AttendanceSettingActivity.this, "请设置考勤天数",
                        Toast.LENGTH_SHORT).show();
            }


        });

    }

    private void showDialog() {

        View view = View.inflate(context, R.layout.dialog_set_input_content, null);
        TextView title = (TextView) view.findViewById(R.id.title_dialog);
        title.setText("考勤范围");
        EditText inputText = (EditText) view.findViewById(R.id.et_input_content);
        inputText.setHint("例如：200");
        if (!TextUtils.isEmpty(mScope)) {
            inputText.setText(mScope);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        //自定义的布局文件
        mDialogs = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        mDialogs.show();
        mDialogs.getWindow().findViewById(R.id.button_project_cancel).setOnClickListener(v -> {
            mDialogs.dismiss();

        });
        mDialogs.getWindow().findViewById(R.id.button_project_ok).setOnClickListener(v -> {
            String content = inputText.getText().toString().trim();
            if (!TextUtils.isEmpty(content)) {
                mScope = content;
                tvScope.setText(content + "米");
                tvScope.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
                if (Double.parseDouble(mScope)>0){
                    mLocationCircle.setRadius(Double.parseDouble(mScope));

                }
                mDialogs.dismiss();
            } else {
                Toast.makeText(AttendanceSettingActivity.this, "请设置考勤范围",
                        Toast.LENGTH_SHORT).show();
            }

        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == 4) {
            String city = data.getStringExtra("city");
            if (!TextUtils.isEmpty(city)) {
                mCity = city;
            }
            tvCity.setText(mCity);
            tvCity.setTextColor(ContextCompat.getColor
                    (context, R.color.titleColor));


        } else if (requestCode == 2 && resultCode == 6) {
            mAddress = data.getStringExtra("location_name");
            mLatLog=data.getStringExtra("latlng");

            //可在其中解析amapLocation获取相应内容。
            tvAddress.setText(mAddress);
            tvAddress.setTextColor(ContextCompat.getColor
                    (context, R.color.titleColor));
            if (!TextUtils.isEmpty(mLatLog)){
                String[] sourceStrArray = mLatLog.split(",");
                mLatLng = new LatLng(Double.parseDouble(sourceStrArray[1]),
                        Double.parseDouble(sourceStrArray[0]));//取出经纬度
            }

            // mLocationCircle.setCenter(latLng);
            moveCamera(mLatLng);
            addMarkersToMap(mLatLng,mAddress);

        }

    }

    /**
     * 向地图添加Marker
     */
    private void addMarkersToMap(LatLng latLng, String markerTitle) {
        Marker marker = mAMap.addMarker(new MarkerOptions().position(latLng)
                .title(markerTitle)
                .icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sign_point))
                .draggable(true));
        marker.showInfoWindow();
    }

    /**
     * 移动地图焦点至相应经纬度
     *
     * @param latLng 经纬度对象
     */
    private void moveCamera(LatLng latLng) {
        if (mAMap != null) {
//            mAMap.moveCamera(CameraUpdateFactory.zoomTo(16));

            LatLngBounds bounds = new LatLngBounds.Builder().include(latLng).build();
            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 16));
            if (Double.parseDouble(mScope)>0){
                mLocationCircle= mAMap.addCircle(new CircleOptions().
                        center(latLng).
                        radius(Double.parseDouble(mScope)).
                        fillColor(Color.argb(25, 0, 0, 255))
                        .strokeWidth(0));
            }else {
                mLocationCircle= mAMap.addCircle(new CircleOptions().
                        center(latLng).
                        radius(100).
                        fillColor(Color.argb(25, 0, 0, 255))
                        .strokeWidth(0));
            }
            mLocationCircle.setCenter(latLng);

        }
    }

    /**
     * 检查是否有定位权限
     */
    private void checkLocationPermission() {
        if (PermissionManager.checkLocation(this)) {
            //定位
            startLocation();
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

        // 初始化定位参数
        AMapLocationClientOption option = new AMapLocationClientOption();
        setupLocationOption(option);

        //给定位Client设置定位参数
        mLocationClient.setLocationOption(option);
        //启动定位
        mLocationClient.startLocation();
    }

    /**
     * 定位成功后的回调
     *
     * @param aMapLocation AMap location instance
     */
    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                 mLatLng = new LatLng(aMapLocation.getLatitude(),
                        aMapLocation.getLongitude());//取出经纬度
                mCity = aMapLocation.getCity();
                tvCity.setText(aMapLocation.getCity());
                tvCity.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
                //添加Marker显示定位位置
                mLocationCircle= mAMap.addCircle(new CircleOptions().
                        center(mLatLng).
                        radius(100).
                        fillColor(Color.argb(25, 0, 0, 255))
                        .strokeWidth(0));

                if (locationMarker == null) {
                    locationMarker = mAMap.addMarker(new MarkerOptions().icon(
                            BitmapDescriptorFactory.fromBitmap(
                                    BitmapFactory.decodeResource(getResources(),
                                            R.mipmap.ic_sign_point))).position(mLatLng).snippet(
                            aMapLocation.getAddress()).draggable(true).setFlat(true));
                    locationMarker.showInfoWindow();//主动显示indowindow

                } else {
                    //已经添加过了，修改位置即可
                    locationMarker.setPosition(mLatLng);
                }
                //然后可以移动到定位点,使用animateCamera就有动画效果
                mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(mLatLng, 16));//参数提示:1.经纬度 2.缩放级别
            } else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError", "location Error, ErrCode:" + aMapLocation.getErrorCode() + ", " +
                        "errInfo:" + aMapLocation.getErrorInfo());
            }
        }
//        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) { // Location is ok
//            // 定位成功获取相关信息
//            double latitude = aMapLocation.getLatitude();
//            double longitude = aMapLocation.getLongitude();
//            String city = aMapLocation.getCity();
//
//                System.out.println("latitude:"+latitude+"\nlongitude:"+longitude+"\ncity:"+city);
//
//                // Stop 定位
//                if (mLocationClient != null) {
//                    mLocationClient.stopLocation(); // 停止定位
//
//            }
//        }
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
    public void onRangeChange(double radius) {
        this.radius = radius;
        mLocationCircle.setRadius(radius);
    }

    /**
     * MapView 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * MapView 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * MapView 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
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
            } else { // 请求定位权限被拒绝
                Snackbar.make(rootView, "请提供完整权限，以定位您当前位置!", Snackbar.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

}
