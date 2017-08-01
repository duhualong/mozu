package org.eenie.wgj.ui.routinginspection.record;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.RecordRoutingResponse;
import org.eenie.wgj.model.response.RoutingPointLatLngResponse;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/29 at 17:32
 * Email: 472279981@qq.com
 * Des:
 */

public class RoutingLineFragment extends BaseSupportFragment {
    public static final String INSPECTION_ID = "inspection_id";
    public static final String DATE = "date";
    public static final String INFO = "info";
    private RecordRoutingResponse.InfoBean mInfoBean;
    MapView mMapView;
    private AMap mAMap;

    private String inspectionId;
    private String date;
    private AggregationUtils aggregationutils;
    List<AggregationUtils.ClusterItem> mClusterItems = new ArrayList<>();
    private View mRoot;

    @Override
    protected int getContentView() {
        return R.layout.fragment_routing_line_map_view;
    }


    private void setUpMap() {
        mAMap.setMapType(AMap.MAP_TYPE_SATELLITE);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mMapView!=null){
            mMapView.onDestroy();
        }

    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    /**
     * 方法必须重写
     */
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void updateUI() {
        for (int i = 0; i < mInfoBean.getInspection().size(); i++) {
            mClusterItems.add(new AggregationUtils.ClusterItem(new LatLng(mInfoBean.getInspection().get(i).getLatitude(),
                    mInfoBean.getInspection().get(i).getLongitude()),
                    mInfoBean.getInspection().get(i).getLocation_name(), String.valueOf(i)));

        }
        aggregationutils.setAllPoints(mClusterItems);


        mSubscription = mRemoteService.getRoutingLine(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                "2017-06-30", "99", "3", "19")
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
                        if (apiResponse.getCode() == 0) {
                            if (apiResponse.getData() != null) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                List<RoutingPointLatLngResponse> data =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<List<RoutingPointLatLngResponse>>() {
                                                }.getType());

                            }
                        }

                    }
                });


    }

    public static RoutingLineFragment newInstance(String date, String inspectionId,
                                                  RecordRoutingResponse.InfoBean infoBean) {


        RoutingLineFragment fragment = new RoutingLineFragment();
        if (!TextUtils.isEmpty(date) && !TextUtils.isEmpty(inspectionId)) {
            Bundle args = new Bundle();
            args.putString(INSPECTION_ID, inspectionId);
            args.putString(DATE, date);
            args.putParcelable(INFO, infoBean);
            fragment.setArguments(args);
        }

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

       mRoot =inflater.inflate(R.layout.fragment_routing_line_map_view,container,false);
        mMapView =(MapView) mRoot.findViewById(R.id.map_view);
        mMapView.onCreate(savedInstanceState);
        if (mAMap == null) {
            mAMap = mMapView.getMap();
            setUpMap();
            aggregationutils = new AggregationUtils();
            aggregationutils.setaMap(mAMap);
            aggregationutils.setContext(context);


        }
        if (getArguments() != null) {
            inspectionId = getArguments().getString(INSPECTION_ID);
            date = getArguments().getString(DATE);
            mInfoBean = getArguments().getParcelable(INFO);

        }

        //初始化定位
        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
