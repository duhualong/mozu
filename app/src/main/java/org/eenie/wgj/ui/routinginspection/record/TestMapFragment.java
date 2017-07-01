package org.eenie.wgj.ui.routinginspection.record;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.SupportMapFragment;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Polyline;
import com.amap.api.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.MapMarkerResponse;
import org.eenie.wgj.model.response.RecordRoutingResponse;
import org.eenie.wgj.model.response.RoutingPointLatLngResponse;
import org.eenie.wgj.util.Constant;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.amap.api.maps.AMap.MAP_TYPE_SATELLITE;

/**
 * Created by Eenie on 2017/6/30 at 14:39
 * Email: 472279981@qq.com
 * Des:
 */

public class TestMapFragment extends SupportMapFragment implements AMap.OnMarkerClickListener,
        AMap.OnInfoWindowClickListener, AMap.OnMapTouchListener {
    public static final String USER_NAME = "inspection_id";
    public static final String AVATAR_URL = "url";
    public static final String DATE = "date";
    public static final String INFO = "info";
    public static final String USER_ID = "uid";
    public static final String TOKEN = "token";
    public static final String PROJECT_ID = "project_id";
    private RecordRoutingResponse.InfoBean mInfoBean;
    MapView mMapView;
    private AMap mAMap;

    private String username;
    private String avatarUrl;
    private String token;
    private String userID;
    private String projectId;
    private String date;
    private View mRoot;
    private Marker curShowWindowMarker;
    Polyline mPolyline;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mRoot = inflater.inflate(R.layout.fragment_routing_line_map_view, container, false);
        mMapView = (MapView) mRoot.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        if (mAMap == null) {

            initMapView();
        } else {
            initMapView();
        }


        return mRoot;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mMapView.onSaveInstanceState(outState);
    }


    private void initMapView() {

        mAMap = mMapView.getMap();

        mAMap.setMapType(MAP_TYPE_SATELLITE);
        mAMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        mAMap.setInfoWindowAdapter(new CustomInfoWindowAdapter(getActivity()));
        if (getArguments() != null) {

            username = getArguments().getString(USER_NAME);
            avatarUrl = getArguments().getString(AVATAR_URL);
            userID = getArguments().getString(USER_ID);
            date = getArguments().getString(DATE);
            token = getArguments().getString(TOKEN);
            projectId = getArguments().getString(PROJECT_ID);
            mInfoBean = getArguments().getParcelable(INFO);
            initData(mInfoBean);
            initDataLine();
        }


    }

    private void initDataLine() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.DOMIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService fileUploadService = retrofit.create(FileUploadService.class);
        Call<ApiResponse> call = fileUploadService.getRoutingLines(token, date, String.valueOf(
                mInfoBean.getInspectionday_id()), projectId, userID);

        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body().getMessage().equals("获取轨迹记录信息成功") && response.body().getCode() == 0) {
                    if (response.body().getData() != null) {
                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(response.body().getData());
                        ArrayList<RoutingPointLatLngResponse> mData =
                                gson.fromJson(jsonArray,
                                        new TypeToken<ArrayList<RoutingPointLatLngResponse>>() {
                                        }.getType());
                        if (mData != null && mData.size() >= 1) {

                                    List<RoutingPointLatLngResponse.InfoBean> data = mData.get(0).getInfo();
                                    if (data != null) {
                                        List<LatLng> latLngs = new ArrayList<>();
                                        for (int i = 0; i < data.size(); i++) {
                                            if (data.get(i).getLongitude() > 0 && data.get(i).getLatitude() > 0) {
                                                latLngs.add(new LatLng(data.get(i).getLatitude(),
                                                        data.get(i).getLongitude()));
                                            }
                                        }
                                        Log.d("mline:", "onResponse: " + new Gson().toJson(latLngs));
                                        mPolyline = mAMap.addPolyline(new PolylineOptions().
                                                addAll(latLngs).width(10).color(Color.BLUE));
                                    }
                        }
                    } else {
                        Toast.makeText(getContext(),
                                "没有路径", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(),
                            response.body().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMapView.onDestroy();


    }

    private void initData(RecordRoutingResponse.InfoBean mInfoBean) {
        Log.d("logd", "initData: " + new Gson().toJson(mInfoBean));
        if (mInfoBean.getInspection() != null) {
            ArrayList<MapMarkerResponse> markerResponseArrayList = new ArrayList<>();
            List<RecordRoutingResponse.InspectionBean> mData = new ArrayList<>();
            mData = mInfoBean.getInspection();
            for (int i = 0; i < mData.size(); i++) {
                if (mData.get(i).getSituation() != 2 && mData.get(i).getLongitude() > 0) {
                    MapMarkerResponse mapMarkerResponse = new MapMarkerResponse(
                            mData.get(i).getInspectionname(), mData.get(i).getLatitude(),
                            mData.get(i).getLocation_name(), mData.get(i).getLongitude(),
                            mData.get(i).getSituation(), mData.get(i).getTime(),
                            mData.get(i).getHappening(), avatarUrl, i, username);
                    markerResponseArrayList.add(mapMarkerResponse);

                }

            }
            Log.d("marker", "initData: " + new Gson().toJson(markerResponseArrayList));
            if (!markerResponseArrayList.isEmpty() && markerResponseArrayList.size() > 0) {
                System.out.println("mLat:" + markerResponseArrayList.get(0).getLatitude() + "\n" +
                        "mLong:" + markerResponseArrayList.get(0).getLongitude());

                moveCamera(new LatLng(markerResponseArrayList.get(0).getLatitude(),
                        markerResponseArrayList.get(0).getLongitude()));
                onSignCircleChange(markerResponseArrayList);
            }


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


    public static TestMapFragment newInstance(String date, String userName, String avatarUrl,
                                              String userId, String token, String projectId,
                                              RecordRoutingResponse.InfoBean infoBean) {


        TestMapFragment fragment = new TestMapFragment();
        if (!TextUtils.isEmpty(date) && !TextUtils.isEmpty(userName)) {
            Bundle args = new Bundle();
            args.putString(USER_NAME, userName);
            args.putString(AVATAR_URL, avatarUrl);
            args.putString(DATE, date);
            args.putString(USER_ID, userId);
            args.putString(TOKEN, token);
            args.putString(PROJECT_ID, projectId);
            args.putParcelable(INFO, infoBean);
            fragment.setArguments(args);
        }

        return fragment;

    }

//    /**
//     * 自定义弹框
//     * @param marker
//     * @return
//     */
//    @Override
//    public View getInfoWindow(Marker marker) {
//        View infoContent = LayoutInflater.from(getContext()).inflate(R.layout.activity_marker, null);
//        render(infoContent,marker);
//        return infoContent;
//    }
//
//    private void render(View view, Marker marker) {
//       MapMarkerResponse storeInfo= (MapMarkerResponse) marker.getObject();
//        CircleImageView ivPic = (CircleImageView) view.findViewById(R.id.img_avatar);
//        Glide.with(getContext()).load(Constant.DOMIN+storeInfo.getAvatarUrl()).centerCrop().into(ivPic);
//        TextView tvPoint = (TextView) view.findViewById(R.id.marker_point);
//        int mPosition=storeInfo.getPosition()+1;
//        if (mPosition<=9){
//            tvPoint.setText("巡检点位：0"+mPosition);
//        }else {
//            tvPoint.setText("巡检点位："+mPosition);
//        }
//        TextView tvPersonal= (TextView) view.findViewById(R.id.marker_personal);
//        tvPersonal.setText("巡检人员：test");
//        TextView tvTime= (TextView) view.findViewById(R.id.marker_time);
//        tvTime.setText("巡检时间："+storeInfo.getTime());
//        TextView tvStatus=(TextView) view.findViewById(R.id.marker_status);
//        tvStatus.setText("巡检状况："+storeInfo.getHappening());
//        TextView tvLocation=(TextView) view.findViewById(R.id.marker_location);
//        tvLocation.setText("巡检地点："+storeInfo.getInspectionname());
//        TextView tvAddress = (TextView) view.findViewById(R.id.marker_address);
//        tvAddress.setText(storeInfo.getLocation_name());
//
//    }
//
//    @Override
//    public View getInfoContents(Marker marker) {
//        return null;
//    }
}
