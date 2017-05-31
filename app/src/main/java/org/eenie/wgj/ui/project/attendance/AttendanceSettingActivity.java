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
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.MyLocationStyle;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.util.PermissionManager;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/5/22 at 16:14
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceSettingActivity extends BaseActivity implements AMapLocationListener {
    @BindView(R.id.root_view)View rootView;
    private AMapLocationClient mLocationClient;
    private static final int REQUEST_LOCATION_CODE = 0xff;

    public static final String PROJECT_ID="project_id";
    private AMap mAMap;
    private Marker locationMarker;
    private View mPopupView;
    private ListView mAddressListView;
    private PopupWindow mPopupAddress;

    @BindView(R.id.map_view)MapView mapView;
    @BindView(R.id.tv_sign_in_city)TextView tvSignCity;
    @Override
    protected int getContentView() {
        return R.layout.activity_attendance_address_setting;
    }

    @Override
    protected void updateUI() {
//        sHA1(context);
        checkLocationPermission();



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mapView.onCreate(savedInstanceState);
        if (mAMap == null) {
            mAMap = mapView.getMap();

            setUpMap();
        }
    }
    /**
     * 设置一些amap的属性
     */
    private void setUpMap() {
        // 自定义系统定位小蓝点
        MyLocationStyle myLocationStyle = new MyLocationStyle();
        myLocationStyle.myLocationIcon(BitmapDescriptorFactory.fromBitmap(
                BitmapFactory.decodeResource(getResources(),
                        R.mipmap.ic_sign_point)));

        myLocationStyle.strokeColor(Color.BLACK);// 设置圆形的边框颜色
        myLocationStyle.radiusFillColor(R.color.text_blue);// 设置圆形的填充颜色
        // myLocationStyle.anchor(int,int)//设置小蓝点的锚点
        myLocationStyle.strokeWidth(1.0f);// 设置圆形的边框粗细
        mAMap.setMyLocationStyle(myLocationStyle);
        mAMap.getUiSettings().setMyLocationButtonEnabled(false);// 设置默认定位按钮是否显示
        mAMap.setMyLocationEnabled(true);// 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        // aMap.setMyLocationType()
    }


//    public static String sHA1(Context context) {
//        try {
//            PackageInfo info = context.getPackageManager().getPackageInfo(
//                    context.getPackageName(), PackageManager.GET_SIGNATURES);
//            byte[] cert = info.signatures[0].toByteArray();
//            MessageDigest md = MessageDigest.getInstance("SHA1");
//            byte[] publicKey = md.digest(cert);
//            StringBuffer hexString = new StringBuffer();
//            for (int i = 0; i < publicKey.length; i++) {
//                String appendString = Integer.toHexString(0xFF & publicKey[i])
//                        .toUpperCase(Locale.US);
//                if (appendString.length() == 1)
//                    hexString.append("0");
//                hexString.append(appendString);
//                hexString.append(":");
//            }
//            String result=hexString.toString();
//            System.out.println("SHA1："+result.substring(0, result.length()-1));
//            return result.substring(0, result.length()-1);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        } catch (NoSuchAlgorithmException e) {
//            e.printStackTrace();
//        }
//        return null;
//    }
    @OnClick({R.id.img_back,R.id.tv_save,R.id.rl_sign_in_city,R.id.rl_sign_in_address,
    R.id.rl_sign_in_scope,R.id.rl_sign_in_work_day,R.id.rl_sign_in_fingerprint,
            R.id.rl_sign_in_contract})public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
            onBackPressed();
                break;
            case R.id.tv_save:



                break;

            case R.id.rl_sign_in_city:

                startActivity(new Intent(context,SelectCityActivity.class));


                break;
            case R.id.rl_sign_in_address:
                startActivity(new Intent(context,SearchAddressDetailActivity.class));



                break;

            case R.id.rl_sign_in_scope:
                startActivity(new Intent(context,SearchAddressActivity.class));

                break;
            case R.id.rl_sign_in_work_day:


                break;
            case R.id.rl_sign_in_fingerprint:


                break;
            case R.id.rl_sign_in_contract:


                break;

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
    @Override public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation != null) {
            if (aMapLocation.getErrorCode() == 0) {
                //可在其中解析amapLocation获取相应内容。
                LatLng latLng = new LatLng(aMapLocation.getLatitude(),
                        aMapLocation.getLongitude());//取出经纬度
                tvSignCity.setText(aMapLocation.getCity());
                tvSignCity.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
                //添加Marker显示定位位置
                if (locationMarker == null) {
//                    //如果是空的添加一个新的,icon方法就是设置定位图标，可以自定义
//                    MarkerOptions markerOptions = new MarkerOptions();
//        markerOptions.setFlat(true);
//        markerOptions.anchor(0.5f, 0.5f);
//
//
//                    markerOptions.position(latLng);
//      markerOptions.snippet(aMapLocation.getAddress()).draggable(true).setFlat(true);
//        markerOptions.icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory.decodeResource(getResources(),
//                R.mipmap.ic_sign_point)));


                    //locationMarker=mAMap.addMarker(markerOptions);
                    locationMarker = mAMap.addMarker(new MarkerOptions().position(latLng).snippet(
                                    aMapLocation.getAddress()).draggable(true).setFlat(true));
                    locationMarker.showInfoWindow();//主动显示indowindow
                    //mAMap.addText(new TextOptions().position(latLng).text(aMapLocation.getAddress()));

                    //固定标签在屏幕中央
                    locationMarker.setPositionByPixels(mapView.getWidth() / 2,mapView.getHeight() / 2);

                } else {
                    //已经添加过了，修改位置即可
                    locationMarker.setPosition(latLng);
                }
                //然后可以移动到定位点,使用animateCamera就有动画效果
                mAMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 30));//参数提示:1.经纬度 2.缩放级别
            }else {
                //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                Log.e("AmapError","location Error, ErrCode:" +aMapLocation.getErrorCode() + ", " +
                        "errInfo:"+ aMapLocation.getErrorInfo());
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


    /**
     * MapView 方法必须重写
     */
    @Override protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * MapView 方法必须重写
     */
    @Override protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * MapView 方法必须重写
     */
    @Override protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override protected void onStop() {
        super.onStop();
        if (mLocationClient != null) {
            mLocationClient.stopLocation(); // 停止定位
        }
    }

    @Override protected void onDestroy() {
        super.onDestroy();
        if(mAMap!=null){
            mAMap=null;
        }
        if (mLocationClient != null) {
            mLocationClient.onDestroy(); // 销毁定位客户端
        }
    }
    // 请求权限后的回调
    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
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
