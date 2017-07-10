package org.eenie.wgj.ui.routinginspection.startrouting;

import android.annotation.TargetApi;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;

import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.PointNeedResponse;
import org.eenie.wgj.model.response.UploadPointPatrol;
import org.eenie.wgj.util.Constant;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Eenie on 2017/6/27 at 17:43
 * Email: 472279981@qq.com
 * Des:
 */

public class MLocationService extends Service {
    //声明mLocationOption对象，定位参数
    ArrayList<PointNeedResponse>mAMapLocationList=new ArrayList<>();

    public AMapLocationClientOption mLocationOption = new AMapLocationClientOption();
    //标识，用于判断是否只显示一次定位信息和用户重新定位
    private boolean isFirstLoc = true;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    private AMapLocation amapLocation;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    private boolean isStop = false;
    public int intTimer = 20;
    public String strIsLogin = "1";
    private String lineId;
    private String token;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    private void init() {
        //初始化定位
        mLocationClient = new AMapLocationClient(this);
        mLocationOption.setInterval(10000);
        //设置精度模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //关闭缓存模式
        mLocationOption.setLocationCacheEnable(false);
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);

//        //启动定位
        mLocationClient.startLocation();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
      lineId=  intent.getStringExtra("lineId");
         token=intent.getStringExtra("mToken");
        System.out.println("lineId:"+lineId);
        // 触发定时器
        if (!isStop) {
            startTimer();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mLocationClient != null) {
            mLocationClient.stopLocation();
        }
        super.onDestroy();
        // 停止定时器
        if (isStop) {
            stopTimer();
        }
    }

    private void startTimer() {
        isStop = true;//定时器启动后，修改标识，关闭定时器的开关
        if (mTimer == null) {
            mTimer = new Timer();
        }
        if (mTimerTask == null) {
            mTimerTask = new TimerTask() {
                @Override
                public void run() {
                    do {
                        try {
//                            Log.d("location地址：", "run: "+new Gson().toJson(mAMapLocationList)+"\n"+
//                            mAMapLocationList.size());
                            if (!mAMapLocationList.isEmpty()&&mAMapLocationList!=null){
                                uploadData(mAMapLocationList);

                            }

                            mAMapLocationList=new ArrayList<>();

                            Thread.sleep(1000 * intTimer);//3秒后再次执行
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            return;
                        }
                    } while (isStop);
                }
            };
        }
        if (mTimer != null && mTimerTask != null) {
            mTimer.schedule(mTimerTask, 0);//执行定时器中的任务
        }
    }

    private void uploadData(ArrayList<PointNeedResponse> mList){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.DOMIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);

        UploadPointPatrol uploadPointPatrol=new UploadPointPatrol(lineId,new Gson().toJson(mList));
        Call<ApiResponse> call = userBiz.uploadPoiotByService(token,uploadPointPatrol);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body().getCode()==0){
                    // showRegisterDialog();
                    System.out.println("上传成功");

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    /**
     * 停止定时器，初始化定时器开关
     */
    private void stopTimer() {
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if (mTimerTask != null) {
            mTimerTask.cancel();
            mTimerTask = null;
        }
        isStop = false;//重新打开定时器开关
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public void onLocationChanged(AMapLocation aMapLocation) {
            if (aMapLocation != null) {
                if (aMapLocation.getErrorCode() == 0) {
                    System.out.println("sss");
                    String mDate = new SimpleDateFormat("yyyy-MM-dd hh:mm").format(Calendar.getInstance().getTime());
                    if (aMapLocation.getAccuracy()<=50){
                        mAMapLocationList.add(new PointNeedResponse(aMapLocation.getLongitude(),
                                aMapLocation.getLatitude(),
                                mDate));
                    }

                    //可在其中解析amapLocation获取相应内容。
//                    aMapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
//                    aMapLocation.getLatitude();//获取纬度
//                    aMapLocation.getLongitude();//获取经度
//                    aMapLocation.getAccuracy();//获取精度信息
//                    aMapLocation.getAddress();//地址，如果option中设置isNeedAddress为false，则没有此结果，网络定位结果中会有地址信息，GPS定位不返回地址信息。
//                    aMapLocation.getCountry();//国家信息
//                    aMapLocation.getProvince();//省信息
//                    aMapLocation.getCity();//城市信息
//                    aMapLocation.getDistrict();//城区信息
//                    aMapLocation.getStreet();//街道信息
//                    aMapLocation.getStreetNum();//街道门牌号信息
//                    aMapLocation.getCityCode();//城市编码
//                    aMapLocation.getAdCode();//地区编码
//                    aMapLocation.getAoiName();//获取当前定位点的AOI信息
//                    //这个是请求数据的接口：
//                    double lat = aMapLocation.getLatitude();
//                    double lng = aMapLocation.getLongitude();
////                    String gps_url = MyURL.loginUrl + String.format("gps.ashx?userid=%s&x=%s&y=%s&position=%s&company=%s&address=%s&state=%s&remark=%s",
//                            MyURL.no, lng, lat, "5", MyURL.company, aMapLocation.getStreet(), "0", "0");
//                    String m = MyHttpUtils.getTextFromUrl(gps_url);

                }
                // 应该停止客户端再发送定位请求
//                if (mLocationClient.isStarted()) {
//                    mLocationClient.onDestroy();
//                }
            }
        }
    };
}