package org.eenie.wgj.ui.attendance;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.newattendancestatistic.UserAttendanceStatisticData;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/13 at 14:24
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceRecordActivity extends BaseActivity {
    public static final String USER_NAME = "user_name";
    public static final String PROJECT_ID = "project_id";

    @BindView(R.id.tv_date)
    TextView titleDate;
    @BindView(R.id.tv_position_name)
    TextView tvPositionName;
    @BindView(R.id.calendar_view)
    SignCalendar mSignCalendar;
    SignCalendarCellDecorator mDecorator;
    private String userName;
    private String projectId;
    private List<String>mDates=new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_attendance_record;
    }

    @Override
    protected void updateUI() {
        userName = getIntent().getStringExtra(USER_NAME);
        projectId = getIntent().getStringExtra(PROJECT_ID);
        if (!TextUtils.isEmpty(userName)) {
            tvPositionName.setText(userName);
        }



        titleDate.setText(new SimpleDateFormat("yyyy年MM月").format(Calendar.getInstance().getTime()));

        getAttendanceList(new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime()));


    }

    private void getAttendanceList(String date) {

        mSubscription = mRemoteService.getUserAttendanceStatisticData(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                mPrefsHelper.getPrefs().getString(Constants.UID,""), projectId, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getCode() == 0) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            UserAttendanceStatisticData data =
                                    gson.fromJson(jsonArray,
                                            new TypeToken<UserAttendanceStatisticData>() {
                                            }.getType());

                            if (data != null) {
                                if (data.getSchedul()!=null){
//                                    List<UserAttendanceStatisticData.SchedualBean>mSchedul=data.getSchedul();
//                                    for (int i=0;i<data.getSchedul().size();i++){
//                                        for (int j=0;j<mDates.size();j++){
//                                            if (data.getSchedul().get(i).getDay().equals(mDates.get(j))){
//                                                mDates.remove(j);
//                                            }
//
//                                        }
//
//                                    }
//
//                                    if (mDates.size()>0){
//                                        for (int m=0;m<mDates.size();m++){
//                                            UserAttendanceStatisticData.SchedualBean schedualBean=
//                                                    new UserAttendanceStatisticData.SchedualBean(mDates.get(m),-1);
//                                            mSchedul.add(schedualBean);
//                                        }
//
//                                    }
                                    mDecorator = new SignCalendarCellDecorator();
                                    mDecorator.setData(data.getSchedul());
                                    mSignCalendar.setSignCalendarCellDecorator(mDecorator);
                                    mSignCalendar.initMonthView();
                                }

                            }
//
                        } else {
                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

//    public  Date stringToDate(String strTime, String formatType)
//            throws ParseException {
//        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
//        Date date = null;
//        date = formatter.parse(strTime);
//        return date;
//    }
//
//
//    public void dayReport(Date month) {
//
//
//
//        Calendar cal = Calendar.getInstance();
//        cal.setTime(month);//month 为指定月份任意日期
//        int year = cal.get(Calendar.YEAR);
//        int m = cal.get(Calendar.MONTH);
//        int dayNumOfMonth =getDaysByYearMonth(year, m);
//        cal.set(Calendar.DAY_OF_MONTH, 1);// 从一号开始
//
//        for (int i = 0; i < dayNumOfMonth; i++, cal.add(Calendar.DATE, 1)) {
//            Date d = cal.getTime();
//            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
//            String df = simpleDateFormat.format(d);
//            mDates.add(df);
//        }
//    }
//    //获取指定月份的天数
//    public static int getDaysByYearMonth(int year, int month) {
//
//        Calendar a = Calendar.getInstance();
//        a.set(Calendar.YEAR, year);
//        a.set(Calendar.MONTH, month - 1);
//        a.set(Calendar.DATE, 1);
//        a.roll(Calendar.DATE, -1);
//        int maxDate = a.get(Calendar.DATE);
//        return maxDate;
//    }


//    private void getAttendanceList(String date) {
//        mSubscription = mRemoteService.getAttendanceDayOfMonth(
//                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""), date,
//                mPrefsHelper.getPrefs().getString(Constants.UID, ""))
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<ApiResponse>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(ApiResponse apiResponse) {
//                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
//
//                            if (apiResponse.getData() != null) {
//                                Gson gson = new Gson();
//                                String jsonArray = gson.toJson(apiResponse.getData());
//                                ArrayList<StatisticsInfoEntity.ResultMessageBean> results =
//                                        gson.fromJson(jsonArray,
//                                                new TypeToken<ArrayList<
//                                                        StatisticsInfoEntity.ResultMessageBean>>() {
//                                                }.getType());
//                                if (results != null) {
//                                    mDecorator = new SignCalendarCellDecorator();
//                                    mDecorator.setData(results);
//                                    mSignCalendar.setSignCalendarCellDecorator(mDecorator);
//                                    mSignCalendar.initMonthView();
//                                }
//                            }
//
//                        }
//
//                    }
//                });
//    }

    @OnClick({R.id.img_back, R.id.rl_attendance_error})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.rl_attendance_error:
                startActivity(new Intent(context, AttendanceAbnormalActivity.class)
                        .putExtra(AttendanceAbnormalActivity.DATE,new SimpleDateFormat("yyyy-MM").
                                format(Calendar.getInstance().getTime()))
                        .putExtra(AttendanceAbnormalActivity.UID,
                                mPrefsHelper.getPrefs().getString(Constants.UID,""))
                        .putExtra(AttendanceAbnormalActivity.PROJECT_ID, projectId));
                break;
        }
    }

}
