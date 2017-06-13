package org.eenie.wgj.ui.attendance;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;

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

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.RuntimePermissions;

/**
 * Created by Eenie on 2017/6/12 at 18:53
 * Email: 472279981@qq.com
 * Des:
 */
@RuntimePermissions
public abstract class BaseAttendanceActivity extends BaseActivity implements LocationSource,
        AMapLocationListener,
        AMap.OnMarkerClickListener {


    AMap mMap;
    //地图的UI
    UiSettings mMapUiSetting;
    MapView mMapView;
    //签到点的圈圈
    protected Circle mSignCircle;
    protected Marker mSignMarker;
    //定位点
    protected Marker mLocationMarker;
    //定位Client
    public AMapLocationClient mLocationClient = null;
    //定位Option
    AMapLocationClientOption mLocationClientOption;


    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_TIME_TICK)) {
                onTimeChange(System.currentTimeMillis());
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        mLocationClientOption = new AMapLocationClientOption();
        initLocation(mLocationClient, mLocationClientOption);
        mMapView = fetchMapView();
        mMapView.onCreate(savedInstanceState);
        mMap = mMapView.getMap();
        initMapSettingUI();
        if (fetchLastLocation() != null) {
            LatLng latLng = new LatLng(fetchLastLocation().getLatitude(), fetchLastLocation().getLongitude());
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("我的位置");
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sign_point));
            mLocationMarker = mMap.addMarker(markerOptions);
        }
        IntentFilter filter = new IntentFilter();
        filter.addAction(Intent.ACTION_TIME_TICK);
        registerReceiver(receiver, filter);
        onTimeChange(System.currentTimeMillis());
    }



    public void initMapSettingUI() {

        mMapUiSetting = mMap.getUiSettings();
        mMap.setMapType(AMap.MAP_TYPE_NORMAL);
        mMap.setLocationSource(this);// 设置定位监听
        mMapUiSetting.setMyLocationButtonEnabled(true); // 显示默认的定位按钮
        mMap.setMyLocationEnabled(true);// 可触发定位并显示定位层
//        mMapUiSetting.setScaleControlsEnabled(true);//显示比例尺控件
//        mMapUiSetting.setCompassEnabled(true);//罗盘


    }


    //当定位发生变化的时调用
    @Override
    public void onLocationChanged(AMapLocation location) {
        onLocationStop(location != null && location.getErrorCode() == 0);
        if (location != null && location.getErrorCode() == 0) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (mLocationMarker == null) {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_sign_point));
                mLocationMarker = mMap.addMarker(markerOptions);
            } else {
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 16));
                mLocationMarker.setPosition(latLng);
            }

        }


    }


    public void onSignCircleChange(LatLng latLng, double radius, String info) {
        if (mSignCircle == null) {
            CircleOptions circleOptions = new CircleOptions();
            circleOptions.center(latLng)
                    .radius(radius)
                    .fillColor(Color.parseColor("#503B3231"))
                    .strokeColor(Color.parseColor("#504996FF"))
                    .strokeWidth(5);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng);
            markerOptions.title("考勤点");
            markerOptions.snippet(info);
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_check_point_mark));


            mSignMarker = mMap.addMarker(markerOptions);
            mMap.setOnMarkerClickListener(this);
            mSignCircle = mMap.addCircle(circleOptions);
            mSignMarker.showInfoWindow();
        } else {
            mSignMarker.setPosition(latLng);
            mSignCircle.setCenter(latLng);
            mSignCircle.setRadius(radius);
        }
    }


    @Override
    public void activate(OnLocationChangedListener onLocationChangedListener) {
        BaseAttendanceActivityPermissionsDispatcher.startLocationWithCheck(this);
    }

    @Override
    public void deactivate() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mMapView.onDestroy();
        unregisterReceiver(receiver);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，实现地图生命周期管理
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，实现地图生命周期管理
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，实现地图生命周期管理
        mMapView.onSaveInstanceState(outState);
    }


    protected MapView fetchMapView() {
        return (MapView) findViewById(initMapView());
    }


    /**
     * 初始化定位参数
     *
     * @param client
     * @param option
     */
    private void initLocation(AMapLocationClient client, AMapLocationClientOption option) {
        //设置定位回调监听
        client.setLocationListener(this);
        //设置定位一次、该方法默认为false。
        option.setOnceLocation(true);
        //设置定位模式：GPS WIFI 基站
//        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        option.setOnceLocationLatest(true);
        client.setLocationOption(option);
    }


    /**
     * 启动定位
     */
    @NeedsPermission({Manifest.permission.ACCESS_COARSE_LOCATION})
    public void startLocation() {
        onLocationStart();
        mLocationClient.startLocation();
    }


    /**
     * 停止定位
     */
    public void stopLocation() {

        mLocationClient.stopLocation();
    }

    public abstract int initMapView();

    /**
     * 获取上次定位点
     *
     * @return
     */
    public AMapLocation fetchLastLocation() {
        return mLocationClient.getLastKnownLocation();
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
        } else {
            marker.showInfoWindow();
        }
        return true;
    }


    public void onLocationStart() {

    }


    public void onLocationStop(boolean success) {

    }


    public abstract void onTimeChange(long date);


}
