package org.eenie.wgj.ui.reportpoststatistics;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.MapMarkerResponse;
import org.eenie.wgj.model.response.reportpost.ReportActualPostResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

import static com.amap.api.maps.AMap.MAP_TYPE_SATELLITE;

/**
 * Created by Eenie on 2017/7/6 at 16:00
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportPostStatusMapViewActivity extends BaseActivity implements AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener, AMap.OnMapTouchListener {
    public static final String INFO = "info";
    @BindView(R.id.map_view)
    MapView mMapView;
    private AMap mAMap;
    ReportActualPostResponse.PostsBean mData;
    private Marker curShowWindowMarker;

    @Override
    protected int getContentView() {
        return R.layout.activity_report_map_view;
    }

    @Override
    protected void updateUI() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mMapView.onCreate(savedInstanceState);
        if (mAMap == null) {

            initMapView();
        } else {
            initMapView();
        }
    }

    private void initMapView() {
        mAMap = mMapView.getMap();
        mAMap.setMapType(MAP_TYPE_SATELLITE);
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        mAMap.setInfoWindowAdapter(new CustomInfoReportWindowAdapter(context));
        mData = getIntent().getParcelableExtra(INFO);
        if (mData != null) {
            initData(mData);
        }
    }

    private void initData(ReportActualPostResponse.PostsBean mInfoBean) {
        if (mInfoBean != null) {

            ArrayList<MapMarkerResponse> markerResponseArrayList = new ArrayList<>();

            MapMarkerResponse mapMarkerResponse = new MapMarkerResponse(mInfoBean.getPostsetting_name(),
                    mInfoBean.getLatitude(), mInfoBean.getLocation_name(), mInfoBean.getLongitude(),
                    mInfoBean.getCompletetime(), "正常", mInfoBean.getUser_name());
            markerResponseArrayList.add(mapMarkerResponse);
            moveCamera(new LatLng(mInfoBean.getLatitude(),
                    mInfoBean.getLongitude()));
            onSignCircleChange(markerResponseArrayList);
        }


    }


    public void onSignCircleChange(ArrayList<MapMarkerResponse> markerResponseArrayList) {
        ArrayList<MarkerOptions> markerOptionsArrayList = new ArrayList<>();
        for (int i = 0; i < markerResponseArrayList.size(); i++) {


            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(markerResponseArrayList.get(i).getLatitude(),
                    markerResponseArrayList.get(i).getLongitude())).icon(
                    BitmapDescriptorFactory.fromResource(R.mipmap.ic_routing_point));
            markerOptions.title("");
            markerOptionsArrayList.add(markerOptions);

        }
        if (!markerOptionsArrayList.isEmpty() && markerOptionsArrayList.size() > 0) {
            for (int j = 0; j < markerOptionsArrayList.size(); j++) {
                Marker marker = mAMap.addMarker(markerOptionsArrayList.get(j));
                marker.setObject(markerResponseArrayList.get(j));
                mAMap.setOnMarkerClickListener(this);


            }
        }


        mAMap.setOnMapTouchListener(this);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {

        curShowWindowMarker = marker;//保存当前点击的Marker，以便点击地图其他地方设置InfoWindow消失
        marker.showInfoWindow();
        //返回:true 表示点击marker 后marker 不会移动到地图中心；返回false 表示点击marker 后marker 会自动移动到地图中心

        return false;//返回true，消费此事件。
    }


    @Override
    public void onInfoWindowClick(Marker marker) {
        // TODO: 2016/11/3
        if (marker.isInfoWindowShown()) {
            marker.hideInfoWindow();
        }
    }


    @Override
    public void onTouch(MotionEvent motionEvent) {
        if (mAMap != null && curShowWindowMarker != null) {
            if (curShowWindowMarker.isInfoWindowShown()) {
                curShowWindowMarker.hideInfoWindow();
            }
        }
    }

    private void moveCamera(LatLng latLng) {

        if (mAMap != null) {
            mAMap.moveCamera(CameraUpdateFactory.zoomTo(15));
            LatLngBounds bounds = new LatLngBounds.Builder().include(latLng).build();
            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 15));

        }
    }


    @OnClick(R.id.img_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }
}
