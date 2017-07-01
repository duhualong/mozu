package org.eenie.wgj.ui.routinginspection;

import android.os.Bundle;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import java.util.ArrayList;

import butterknife.BindView;

/**
 * Created by Eenie on 2017/6/30 at 15:33
 * Email: 472279981@qq.com
 * Des:
 */

public class TestMapViewActivity extends BaseActivity {
    @BindView(R.id.map_view)
    MapView mMapView;
    private AMap mAMap;
    protected Marker mSignMarker;
    @Override
    protected int getContentView() {
        return R.layout.fragment_routing_line_map_view;
    }

    @Override
    protected void updateUI() {


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMapView.onCreate(savedInstanceState);
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            initMapView();

        }
    }

    private void initMapView() {
        mAMap.setMapType(AMap.MAP_TYPE_SATELLITE);
        onSignCircleChange();

    }


    public void onSignCircleChange() {
        ArrayList<MarkerOptions> markerOptionses = new ArrayList<>();
        for (int i = 0; i <= 3; i++) {
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(37.00 + 0.10 * i, 122.00 + 0.10 * i));
            markerOptions.title("考勤点" + i);
            markerOptions.snippet("address" + i);
           // markerOptions.icon(BitmapDescriptorFactory.fromResource(R.mipmap.ic_attendance_point));
            markerOptionses.add(markerOptions);

        }
        if (!markerOptionses.isEmpty()&&markerOptionses.size()>0) {
            for (int j = 0; j < markerOptionses.size(); j++) {
                mAMap.addMarker(markerOptionses.get(j));
            }
        }

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();

    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

}
