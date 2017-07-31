package org.eenie.wgj.ui.attendancestatistics;

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
import org.eenie.wgj.ui.attendance.AttendanceAbnormalActivity;
import org.eenie.wgj.ui.attendance.SignCalendar;
import org.eenie.wgj.ui.attendance.SignCalendarCellDecorator;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/20 at 15:08
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceRecorderMonthActivity extends BaseActivity {
    public static final String USER_NAME = "user_name";
    public static final String POST = "post";
    public static final String DATE = "date";
    public static final String UID="uid";
    public static final String PROJECT_ID="id";

    @BindView(R.id.tv_date)
    TextView titleDate;
    @BindView(R.id.tv_position_name)
    TextView tvPositionName;
    @BindView(R.id.calendar_view)
    SignCalendar mSignCalendar;
    SignCalendarCellDecorator mDecorator;
    private String userName;
    private String date;
    private String post;
    private String uid;
    private String projectId;

    @Override
    protected int getContentView() {
        return R.layout.activity_attendance_record;
    }

    @Override
    protected void updateUI() {
        date = getIntent().getStringExtra(DATE);
        userName = getIntent().getStringExtra(USER_NAME);
        uid=getIntent().getStringExtra(UID);
        post=getIntent().getStringExtra(POST);
        projectId=getIntent().getStringExtra(PROJECT_ID);
        if (!TextUtils.isEmpty(userName)&&!TextUtils.isEmpty(post)) {
            tvPositionName.setText(userName+"|"+post);
        }
        if (date != null) {

            titleDate.setText(date.replaceAll("-", "年") + "月");
            getAttendanceList(date);

        }

    }
    private void getAttendanceList(String date) {

        mSubscription = mRemoteService.getUserAttendanceStatisticData(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                uid, projectId, date)
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
                                    mDecorator = new SignCalendarCellDecorator();
                                    mDecorator.setData(data.getSchedul());
                                    mSignCalendar.setSignCalendarCellDecorator(mDecorator);
                                   // mSignCalendar.initMonthView();


                                    String[] ca = date.split("-");
                                    Calendar calendar = Calendar.getInstance();
                                    calendar.set(Calendar.YEAR, Integer.parseInt(ca[0]));
                                    calendar.set(Calendar.MONTH, Integer.parseInt(ca[1]) - 1);
                                    mSignCalendar.initMonthView(calendar);
                                }

                            }
//
                        } else {
                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

//    private void getAttendanceList(String date) {
//        mSubscription = mRemoteService.getAttendanceDayOfMonth(
//                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""), date,uid)
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
//
//                                    String[] ca = date.split("-");
//                                    Calendar calendar = Calendar.getInstance();
//                                    calendar.set(Calendar.YEAR, Integer.parseInt(ca[0]));
//                                    calendar.set(Calendar.MONTH, Integer.parseInt(ca[1]) - 1);
//                                    mSignCalendar.initMonthView(calendar);
//                                }
//
//                            }
//
//                        }
//
//                    }
//                });
//
//    }



    @OnClick({R.id.img_back, R.id.rl_attendance_error})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.rl_attendance_error:
                startActivity(new Intent(context, AttendanceAbnormalActivity.class)
                .putExtra(AttendanceAbnormalActivity.DATE,date)
                .putExtra(AttendanceAbnormalActivity.UID,uid)
                .putExtra(AttendanceAbnormalActivity.PROJECT_ID,projectId));
                break;
        }
    }
}
