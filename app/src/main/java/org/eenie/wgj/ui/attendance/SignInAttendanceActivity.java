package org.eenie.wgj.ui.attendance;

import android.support.v7.widget.PopupMenu;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.AttendanceAddressInfo;
import org.eenie.wgj.model.response.AttendanceListResponse;
import org.eenie.wgj.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/13 at 9:16
 * Email: 472279981@qq.com
 * Des:
 */

public class SignInAttendanceActivity extends BaseAttendanceActivity {
    public static final String INFO="info";
    @BindView(R.id.tv_location)TextView tvLocation;
    private  String address;
    boolean locationSuc = false;
    @BindView(R.id.tv_sel_rank)TextView tvSelectRank;
    private PopupMenu menu;
    private int type=1;



    @Override
    public int initMapView() {
        return R.id.map_view;
    }

    @Override
    public void onTimeChange(long date) {

    }


    @Override
    public void startLocation() {
        super.startLocation();
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_sign_in_attandance;
    }

    @Override
    protected void updateUI() {
        this.registerForContextMenu(tvSelectRank);
        getAttendanceList(new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime()));
        getAttendanceAddress();

    }

    private void getAttendanceList(String time) {
        mSubscription = mRemoteService.getAttendanceList(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),time)
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
                        if (apiResponse.getResultCode() == 200 ||
                                apiResponse.getResultCode() == 0) {
                            if (apiResponse.getData() != null) {
                                Gson gson=new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<AttendanceListResponse> attendanceResponse =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<ArrayList<AttendanceListResponse>>() {
                                                }.getType());
                                if (attendanceResponse!=null){
                                    for (int i=0;i<attendanceResponse.size();i++){
                                        if (attendanceResponse.get(i).getDay().equals(time)){
                                            if (attendanceResponse.get(i).getCheckin().getType()!=0){
                                            }
                                            menu = new PopupMenu(context, tvSelectRank);
                                            initPopRank(attendanceResponse.get(i));
                                        }
                                    }
                                }

                            }


//传入已经预约或者曾经要展示选中的时间列表


                        }

                    }
                });
    }

    private void initPopRank(AttendanceListResponse data){
        final String name = data.getService().getServicesname();
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


//获取当天个人的考勤信息
    private void getAttendanceAddress() {

        mSubscription=mRemoteService.getAttendanceAddress(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN,""))
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
                            Gson gson=new Gson();
                            if (apiResponse.getData()!=null){
                                String jsonArray = gson.toJson(apiResponse.getData());
                              AttendanceAddressInfo data =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<AttendanceAddressInfo>() {
                                                }.getType());
                                if (data!=null){
                                    onSignCircleChange(new LatLng(Double.parseDouble(data.getLatitude()),
                                                    Double.parseDouble(data.getLongitude())),
                                            Double.parseDouble(data.getAttendancescope()),data.getAddress());
                                }
                            }
                        }else {
                            Toast.makeText(context,apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    @OnClick({R.id.img_back,R.id.tv_sel_rank})public void onClick(View view){
        switch (view.getId()){
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
    public void onLocationChanged(AMapLocation location) {
        super.onLocationChanged(location);
        if (location!=null){
            address = location.getAddress();
            tvLocation.setText(address);
        }else {
            tvLocation.setText("定位失败");
        }

    }

    @Override
    public void onLocationStart() {
        super.onLocationStart();
        tvLocation.setText("正在定位...");
    }

    @Override
    public void onLocationStop(boolean success) {
        super.onLocationStop(success);
        locationSuc = success;
        if (!success) {
            tvLocation.setText("定位失败");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
