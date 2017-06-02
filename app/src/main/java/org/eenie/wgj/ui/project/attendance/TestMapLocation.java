//package org.eenie.wgj.ui.project.attendance;
//
//import android.Manifest;
//import android.content.Intent;
//import android.content.SharedPreferences;
//import android.content.pm.PackageManager;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.support.annotation.NonNull;
//import android.support.design.widget.CoordinatorLayout;
//import android.support.design.widget.Snackbar;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.view.MenuItemCompat;
//import android.support.v7.app.ActionBar;
//import android.support.v7.widget.SearchView;
//import android.support.v7.widget.Toolbar;
//import android.text.TextUtils;
//import android.view.Menu;
//import android.view.MenuInflater;
//import android.view.MenuItem;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.AdapterView;
//import android.widget.ListView;
//import android.widget.PopupWindow;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.amap.api.location.AMapLocation;
//import com.amap.api.location.AMapLocationClient;
//import com.amap.api.location.AMapLocationClientOption;
//import com.amap.api.location.AMapLocationListener;
//import com.amap.api.maps.AMap;
//import com.amap.api.maps.CameraUpdateFactory;
//import com.amap.api.maps.MapView;
//import com.amap.api.maps.model.BitmapDescriptorFactory;
//import com.amap.api.maps.model.LatLng;
//import com.amap.api.maps.model.LatLngBounds;
//import com.amap.api.maps.model.Marker;
//import com.amap.api.maps.model.MarkerOptions;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//import org.eenie.wgj.App;
//import org.eenie.wgj.R;
//import org.eenie.wgj.base.BaseActivity;
//import org.eenie.wgj.realm.AreaTable;
//
//import java.lang.reflect.Type;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.BindView;
//import io.realm.Realm;
//
///**
// * Created by Eenie on 2017/6/2 at 11:13
// * Email: 472279981@qq.com
// * Des:
// */
//
//public class TestMapLocation  extends BaseActivity
//        implements IStaticHandler, AMapLocationListener{
//    private Handler handler = StaticHandlerFactory.create(this);
//    private static final int SEARCH_ADDRESS_SUCCESS = 0x101;
//    private static final int SEARCH_ADDRESS_FAILURE = 0x102;
//    private static final int REQUEST_SUCCESS_AREAID = 0x104;
//    private static final int REQUEST_FAILURE_AREA_ID = 0x105;
//    private static final int REQUEST_FAILURE = 0x202;
//    private static final int REQUEST_LOCATION_CODE = 0x201;
//
//    @BindView(R.id.root_view)
//    CoordinatorLayout rootView;
//    @BindView(R.id.toolbar)
//    Toolbar mToolbar;
//    @BindView(R.id.locationTitle)
//    TextView mTitleView;
//    @BindView(R.id.map)
//    MapView mMapView;
//    private Realm realm;
//
//    private View mPopupView;
//    private ListView mAddressListView;
//    private PopupWindow mPopupAddress;
//    private AMap mAMap;
//
//    private String lat;
//    private String lng;
//    private Handler mHandler = StaticHandlerFactory.create(this);
//
//    // Location parts
//    private AMapLocationClient mLocationClient;
//    private Location mLocation;
//    private String region;
//
//    private SearchAddressAdapter mAdapter;
//    private List<SearchAddress> mAddressList;
//    private SearchAddress address;
//    private SearchView mSearchView;
//    private String oldRecoder;
//    private ArrayList<SearchAddress>data;
//
//    @Override protected int getContentView() {
//        return R.layout.test_map;
//    }
//
//    @Override public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater = getMenuInflater();
//        menuInflater.inflate(R.menu.menu_search_location, menu);
//
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
//
//        // Set search view search actions
//        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override public boolean onQueryTextSubmit(String query) {
//                if (!TextUtils.isEmpty(query) && ValidUtils.isChineseInput(query)) {
//                    searchAddress(query);
//                } else {
//                    if (TextUtils.isEmpty(oldRecoder)) {
//                        if (mPopupAddress != null && mPopupAddress.isShowing()) {
//                            mPopupAddress.dismiss();
//                        }
//                    }else {
//
//                    }
//                }
//                return true;
//            }
//
//            @Override public boolean onQueryTextChange(String newText) {
//                if (!TextUtils.isEmpty(newText) && ValidUtils.isChineseInput(newText)) {
//                    searchAddress(newText);
//                } else {
//                    if (mPopupAddress != null && mPopupAddress.isShowing()) {
//                        mPopupAddress.dismiss();
//                    }
//                }
//                return true;
//            }
//        });
//        return true;
//    }
//
//    @Override public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                onBackPressed();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    @Override protected void updateUI() {
//        oldRecoder = App.getPrefsHelper().getString(Constants.SEARCH_OLD_ADDRESS,"");
//        realm = App.getRealmController().getAreaRealm();
//        if (TextUtils.isEmpty(App.getPrefsHelper().getString(Constants.SEARCH_ADDRESS, ""))) {
//            mTitleView.setText("当前地理位置");
//            mTitleView.setTextSize(14);
//        } else {
//            mTitleView.setText(App.getPrefsHelper().getString(Constants.SEARCH_ADDRESS, ""));
//            mTitleView.setTextSize(14);
//        }
//
//        setSupportActionBar(mToolbar);
//        ActionBar actionBar = getSupportActionBar();
//        if (actionBar != null) {
//            actionBar.setDisplayHomeAsUpEnabled(true);
//            actionBar.setDisplayShowTitleEnabled(false);
//        }
//
//        mPopupView = View.inflate(SearchLocationActivity.this, R.layout.popup_address_list, null);
//        mAddressListView = (ListView) mPopupView.findViewById(R.id.addressList);
//
//        checkLocationPermission();
//    }
//
//    @Override protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        mMapView.onCreate(savedInstanceState);
//        if (mAMap == null) {
//            mAMap = mMapView.getMap();
//        }
//    }
//
//    /**
//     * MapView 方法必须重写
//     */
//    @Override protected void onResume() {
//        super.onResume();
//        mMapView.onResume();
//    }
//
//    /**
//     * MapView 方法必须重写
//     */
//    @Override protected void onPause() {
//        super.onPause();
//        mMapView.onPause();
//    }
//
//    /**
//     * MapView 方法必须重写
//     */
//    @Override protected void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        mMapView.onSaveInstanceState(outState);
//    }
//
//    @Override protected void onStop() {
//        super.onStop();
//        if (mLocationClient != null) {
//            mLocationClient.stopLocation(); // 停止定位
//        }
//    }
//
//    @Override protected void onDestroy() {
//        super.onDestroy();
//        if (mMapView != null) {
//            mMapView.onDestroy();
//        }
//        if (mLocationClient != null) {
//            mLocationClient.onDestroy(); // 销毁定位客户端
//        }
//    }
//
//    /**
//     * Query proper address
//     *
//     * @param address address to search
//     */
//    private void searchAddress(String address) {
//        App.getAppAction()
//                .searchAddress(region, address,
//                        new ActionCallbackListener<ApiResponse<List<SearchAddress>>>() {
//                            @Override public void onActionSuccess(ApiResponse<List<SearchAddress>> data) {
//                                if (data != null && data.getResult() != null && !data.getResult().isEmpty()) {
//                                    List<SearchAddress> searchList = data.getResult();
//                                    Message msg = Message.obtain();
//                                    msg.what = SEARCH_ADDRESS_SUCCESS;
//                                    msg.obj = searchList;
//                                    mHandler.sendMessage(msg);
//                                }
//                            }
//
//                            @Override public void onActionFailure(int errorCode, String message) {
//                                mHandler.sendEmptyMessage(SEARCH_ADDRESS_FAILURE);
//                            }
//                        });
//
//        System.out.println("address: " + address);
//    }
//
//    @SuppressWarnings("unchecked") @Override public void handleMessage(Message msg) {
//        switch (msg.what) {
//            case SEARCH_ADDRESS_SUCCESS:
//                List<SearchAddress> searchList = (List<SearchAddress>) msg.obj;
//                if (searchList != null && !searchList.isEmpty()) {
//                    mAddressList = new ArrayList<>();
//                    for (SearchAddress address : searchList) {
//                        if (!TextUtils.isEmpty(address.getUid())) {
//                            mAddressList.add(address);
//                        }
//                    }
//
//                    mAdapter = new SearchAddressAdapter(context, mAddressList);
//                    mAddressListView.setAdapter(mAdapter);
//                    mAddressListView.setOnItemClickListener(this);
//
//                    if (mPopupAddress == null) {
//                        mPopupAddress = new PopupWindow(mPopupView, ViewGroup.LayoutParams.WRAP_CONTENT,
//                                ViewGroup.LayoutParams.WRAP_CONTENT);
//                    }
//
//                    int xOff = ImageUtil.dpToPx(48);
//                    int yOff = ImageUtil.dpToPx(12) * (-1);
//
//                    mPopupAddress.showAsDropDown(mToolbar, xOff, yOff);
//                }
//                break;
//            case SEARCH_ADDRESS_FAILURE:
//                if (mAdapter != null) {
//                    mAdapter.clear();
//                    mAddressListView.setVisibility(View.GONE);
//                }
//                break;
//            case REQUEST_SUCCESS_AREAID:
//                LocationToAddress.AddressComponentBean addressComponentBean =
//                        (LocationToAddress.AddressComponentBean) msg.obj;
//                if (addressComponentBean != null) {
//                    String province = addressComponentBean.getProvince().substring(0, 2);
//                    String district = addressComponentBean.getDistrict().substring(0, 2);
//                    int areaId = realm.where(AreaTable.class)
//                            .beginsWith("province", province)
//                            .beginsWith("area", district)
//                            .findFirst()
//                            .getId();
//                    System.out.println("我所在区域：" + areaId);
//                    App.getPrefsHelper().edit().putString(Constants.POINT_AREA_ID, areaId + "").apply();
//                }
//                break;
//
//            case REQUEST_FAILURE_AREA_ID:
//                String messages = (String) msg.obj;
//                Toast.makeText(context, messages, Toast.LENGTH_SHORT).show();
//                break;
//            case REQUEST_FAILURE:
//
//                break;
//        }
//    }
//
//    /**
//     * 检查是否有定位权限
//     */
//    private void checkLocationPermission() {
//        if (PermissionUtils.checkLocation(this)) {
//            startLocation();
//        } else {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//                Snackbar.make(rootView, "请提供完整权限，以定位您当前位置!", Snackbar.LENGTH_INDEFINITE)
//                        .setAction("OK", v -> {
//                            PermissionUtils.requestLocation(this, REQUEST_LOCATION_CODE);
//                        });
//            } else {
//                PermissionUtils.requestLocation(this, REQUEST_LOCATION_CODE);
//            }
//        }
//    }
//
//    // 请求权限后的回调
//    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
//                                                     @NonNull int[] grantResults) {
//        if (requestCode == REQUEST_LOCATION_CODE) {
//            if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[0])
//                    && PackageManager.PERMISSION_GRANTED == grantResults[0]) { // 请求定位权限被允许
//                startLocation();
//            } else { // 请求定位权限被拒绝
//                Snackbar.make(rootView, "请提供完整权限，以定位您当前位置!", Snackbar.LENGTH_SHORT).show();
//            }
//        }
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }
//
//    /**
//     * 定位成功后的回调
//     *
//     * @param aMapLocation AMap location instance
//     */
//    @Override public void onLocationChanged(AMapLocation aMapLocation) {
//        if (aMapLocation != null && aMapLocation.getErrorCode() == 0) { // Location is ok
//            // 定位成功获取相关信息
//            double latitude = aMapLocation.getLatitude();
//            double longitude = aMapLocation.getLongitude();
//
//            int originalHash = mLocation.hashCode();
//
//            mLocation.setLat(String.valueOf(latitude));
//            mLocation.setLng(String.valueOf(longitude));
//
//            region = aMapLocation.getCity();
//
//            if (originalHash != mLocation.hashCode()) { // 有新的地理位置
//                LatLng latLng = new LatLng(latitude, longitude);
//                moveCamera(latLng);
//                setMapInfoView(mAMap); // 添加地理位置覆盖物
//                addMarkersToMap(latLng, aMapLocation.getAddress());
//            }
//
//            System.out.println(mLocation + "region: " + region);
//        } else {
//            Toast.makeText(SearchLocationActivity.this, "定位失败呢，请检查网络权限..", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * Init the location options
//     *
//     * @param option option to init
//     */
//    private void setupLocationOption(AMapLocationClientOption option) {
//        //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
//        option.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//        //设置是否返回地址信息（默认返回地址信息）
//        option.setNeedAddress(true);
//        //设置是否只定位一次,默认为false
//        option.setOnceLocation(false);
//        //设置是否强制刷新WIFI，默认为强制刷新
//        option.setWifiActiveScan(true);
//        //设置是否允许模拟位置,默认为false，不允许模拟位置
//        option.setMockEnable(false);
//        //设置定位间隔,单位毫秒,默认为2000ms
//        option.setInterval(10 * 1000L);
//    }
//
//    /**
//     * Start Location
//     */
//    private void startLocation() {
//        // 初始化定位Client
//        mLocationClient = new AMapLocationClient(getApplicationContext());
//        mLocationClient.setLocationListener(this);
//
//        // 初始化定位参数
//        AMapLocationClientOption option = new AMapLocationClientOption();
//        setupLocationOption(option);
//
//        mLocation = new Location(); // 初始化记录经纬度信息
//
//        //给定位Client设置定位参数
//        mLocationClient.setLocationOption(option);
//        //启动定位
//        mLocationClient.startLocation();
//    }
//
//    /**
//     * 移动地图焦点至相应经纬度
//     *
//     * @param latLng 经纬度对象
//     */
//    private void moveCamera(LatLng latLng) {
//        if (mAMap != null) {
//            mAMap.moveCamera(CameraUpdateFactory.zoomTo(16));
//            LatLngBounds bounds = new LatLngBounds.Builder().include(latLng).build();
//            mAMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 10));
//        }
//    }
//
//    /**
//     * 搜索地址信息列表点击事件
//     */
//    @Override public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//        if (mAddressList != null && !mAddressList.isEmpty()) {
//            address=mAddressList.get(position);
//
//            mSearchView.clearFocus();
//
//            if (mPopupAddress.isShowing()) {
//                mPopupAddress.dismiss();
//            }
//
//            Location location = mAddressList.get(position).getLocation();
//
//            String address = mAddressList.get(position).getName();
//            if (location != null) {
//                LatLng latLng = transLatLng(location);
//                lat = location.getLat();
//                lng = location.getLng();
//                moveCamera(latLng);
//                setMapInfoView(mAMap); // 添加地理位置覆盖物
//                addMarkersToMap(latLng, address); // 添加地理位置标识物
//            }
//        }
//    }
//
//    /**
//     * 向地图添加Marker
//     */
//    private void addMarkersToMap(LatLng latLng, String markerTitle) {
//        Marker marker = mAMap.addMarker(new MarkerOptions().position(latLng)
//                .title(markerTitle)
//                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker))
//                .draggable(true));
//        marker.showInfoWindow();
//    }
//
//    /**
//     * Transform Location instance to LatLng
//     *
//     * @param location location instance
//     * @return LatLng instance.
//     */
//    private LatLng transLatLng(Location location) {
//        double latitude;
//        double longitude;
//        if (location != null) {
//            latitude = Double.parseDouble(location.getLat());
//            longitude = Double.parseDouble(location.getLng());
//            return new LatLng(latitude, longitude);
//        }
//        return null;
//    }
//
//    /**
//     * 设置地图出现的Info Window样式
//     *
//     * @param aMap 地图对象
//     */
//    private void setMapInfoView(AMap aMap) {
//        aMap.setInfoWindowAdapter(new AMap.InfoWindowAdapter() {
//            @Override public View getInfoWindow(Marker marker) {
//                View infoWindow = View.inflate(context, R.layout.custom_info_window, null);
//                TextView windowTitle = (TextView) infoWindow.findViewById(R.id.infoWindowTitle);
//
//                windowTitle.setText(marker.getTitle());
//
//                infoWindow.findViewById(R.id.infoWindowSubmit).setOnClickListener(v -> {
//                    Intent intent = new Intent();
//                    intent.putExtra("location", marker.getTitle());
//                    setResult(RESULT_OK, intent);
//                    if (!TextUtils.isEmpty(lat) && !TextUtils.isEmpty(lng)) {
//                        getAreaId(Double.parseDouble(lat), Double.parseDouble(lng));
//                    }
//                    App.getPrefsHelper()
//                            .edit()
//                            .putString(Constants.SEARCH_ADDRESS, marker.getTitle())
//                            .apply();
//                    String s1= marker.getTitle();
//                    // Gson gson=new Gson();
//                    String recoder=App.getPrefsHelper().getString(Constants.SEARCH_OLD_ADDRESS,"");
//
//                    //List<SearchAddress> data= gson.fromJson(ss,SearchAddress.class);
//                    if (!TextUtils.isEmpty(recoder)) {
//                        Type type=new TypeToken<List<String>>(){}.getType();
//                        List<SearchAddress> listData=new Gson().fromJson(recoder,type);
//                        System.out.println("listData"+listData);
//                        for (SearchAddress dates:listData){
//                            if (address==dates){
//
//                            }else {
//                                listData.add(address);
//                                System.out.println("打印listData："+listData);
//                                SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
//                                SharedPreferences.Editor prefsEditor = mPrefs.edit();
//                                Gson gson = new Gson();
//                                String json = gson.toJson(listData);
//                                prefsEditor.putString(Constants.SEARCH_OLD_ADDRESS, json);
//                                prefsEditor.apply();
//                            }
//                        }
//
//                    }else {
//                        SharedPreferences mPrefs = getPreferences(MODE_PRIVATE);
//                        SharedPreferences.Editor prefsEditor = mPrefs.edit();
//                        Gson gson = new Gson();
//                        String json = gson.toJson(address);
//                        prefsEditor.putString(Constants.SEARCH_OLD_ADDRESS, json);
//                        prefsEditor.apply();
//
//                        //recoder= marker.getTitle();
//                        //System.out.println("打印："+recoder);
//                        //App.getPrefsHelper().edit().putString(Constants.SEARCH_OLD_ADDRESS, recoder);
//                    }
//
//
//                    //
//
//                    //
//                    SearchLocationActivity.this.finish();
//                });
//                return infoWindow;
//            }
//
//            @Override public View getInfoContents(Marker marker) {
//                return null;
//            }
//        });
//    }
//
//    private void getAreaId(double lats, double lngs) {
//        App.getAppAction().getAreaId(lats, lngs, new ActionCallbackListener<LocationToAddress>() {
//            @Override public void onActionSuccess(LocationToAddress data) {
//                if (data != null) {
//                    Message msg = Message.obtain();
//                    msg.what = REQUEST_SUCCESS_AREAID;
//                    msg.obj = data.getAddressComponent();
//                    handler.sendMessage(msg);
//                } else {
//                    Message msg = Message.obtain();
//                    msg.what = REQUEST_FAILURE_AREA_ID;
//                    msg.obj = "参数错误";
//                    handler.sendMessage(msg);
//                }
//            }
//
//            @Override public void onActionFailure(int errorCode, String message) {
//                Message msg = Message.obtain();
//                msg.what = REQUEST_FAILURE;
//                msg.obj = message;
//                handler.sendMessage(msg);
//            }
//        });
//    }
//}
