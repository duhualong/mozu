package org.eenie.wgj.ui.attendance;

import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loonggg.weekcalendar.view.WeekCalendar;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.UserId;
import org.eenie.wgj.model.response.AttendanceListResponse;
import org.eenie.wgj.model.response.SignOutInfor;
import org.eenie.wgj.model.response.UserInforById;
import org.eenie.wgj.ui.attendance.attendancesort.AttendanceSortMonthItemActivity;
import org.eenie.wgj.ui.attendance.sign.AttendanceTestSignInActivity;
import org.eenie.wgj.ui.attendance.signout.AttendanceSignOutActivity;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
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
 * Created by Eenie on 2017/5/15 at 18:01
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceActivity extends BaseActivity {

    @BindView(R.id.title_attendance)
    TextView mTitle;
    @BindView(R.id.attendance_name)
    TextView attendanceName;
    private Gson gson = new Gson();
    private String username;
    private ArrayList<AttendanceListResponse> attendanceResponse = new ArrayList<>();
    @BindView(R.id.rl_attendance_info)
    RelativeLayout rlAttendanceInfo;
    @BindView(R.id.date_title)
    TextView tvDateTitle;
    @BindView(R.id.start_time)
    TextView tvStartTime;
    @BindView(R.id.end_time)
    TextView tvEndTime;
    @BindView(R.id.start_time_result)
    TextView tvStartTimeAttendance;
    @BindView(R.id.end_time_result)
    TextView tvEndTimeAttendance;
    @BindView(R.id.start_attendance_address)
    TextView tvStartAttendanceAddress;
    @BindView(R.id.end_attendance_address)
    TextView tvEndAttendanceAddress;

    @BindView(R.id.week_calendar)
    WeekCalendar mWeekCalendar;

    ArrayList<String> mList = new ArrayList<>();
    ArrayList<String> mLists = new ArrayList<>();
    private String projectId;

    @Override
    protected int getContentView() {

        return R.layout.activity_attendance_work;
    }


    @Override
    protected void updateUI() {
        String mDate = mPrefsHelper.getPrefs().getString(Constants.DATE_LIST, "");
        String mDateThing = mPrefsHelper.getPrefs().getString(Constants.DATE_THING_LIST, "");
        getData();
        if (!TextUtils.isEmpty(mDate) && !TextUtils.isEmpty(mDateThing)) {
            mList = gson.fromJson(mDate, new TypeToken<ArrayList<String>>() {
            }.getType());
            mLists = gson.fromJson(mDateThing, new TypeToken<ArrayList<String>>() {
            }.getType());
        }
        mWeekCalendar.setSelectDates(mList, mLists);
        mWeekCalendar.showToday();
        mWeekCalendar.setOnDateClickListener(time ->
                showAttendanceInfo(time));
        mWeekCalendar.setOnCurrentMonthDateListener((year, month) -> {
            mTitle.setText(year + "年" + month + "月");
        });

        mTitle.setText(new SimpleDateFormat("yyyy年MM月").format(Calendar.getInstance().getTime()));


    }

    private void getData() {

        mSubscription = mRemoteService.getAttendanceList(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode() == 200 ||
                                apiResponse.getResultCode() == 0) {
                            if (apiResponse.getData() != null) {
                                String jsonArray = gson.toJson(apiResponse.getData());
                                attendanceResponse =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<ArrayList<AttendanceListResponse>>() {
                                                }.getType());
                                if (attendanceResponse != null) {

                                    for (int i = 0; i < attendanceResponse.size(); i++) {
                                        mList.add(attendanceResponse.get(i).getDay());
                                        mLists.add(attendanceResponse.get(i).getService().
                                                getServicesname());
                                    }

                                }


                            }

                        }

                    }
                });


    }


    private void showAttendanceInfo(String date) {
        System.out.println("data" + date);
        Log.d("list", "showAttendanceInfo: " + gson.toJson(attendanceResponse));
        if (attendanceResponse != null) {
            boolean result = false;
            int position = 0;
            for (int j = 0; j < attendanceResponse.size(); j++) {
                if (date.equals(attendanceResponse.get(j).getDay())) {
                    result = true;
                    position = j;
                }
            }

            if (result) {
                AttendanceListResponse attendanceData = attendanceResponse.get(position);
                Log.d("TAG", "showAttendanceInfo: " + gson.toJson(attendanceData));
                rlAttendanceInfo.setVisibility(View.VISIBLE);
                if (attendanceData != null) {
                    String startTime = "08:30";
                    String endTime = "18:30";
                    if (attendanceData.getService().getStarttime() != null) {

                        if (attendanceData.getService().getStarttime().length() > 5) {
                            startTime = attendanceData.getService().getStarttime().substring(0, 5);

                        } else {
                            startTime = attendanceData.getService().getStarttime();

                        }
                    }
                    if (attendanceData.getService().getEndtime() != null) {
                        if (attendanceData.getService().getEndtime().length() > 5) {
                            endTime = attendanceData.getService().getEndtime().substring(0, 5);
                        } else {
                            endTime = attendanceData.getService().getEndtime();
                        }
                    }
                    tvDateTitle.setText(date + " " + "考勤详情   " +
                            startTime +
                            "-" + endTime);
                    if (attendanceData.getCheckin().getTime() != null) {
                        tvStartTime.setText("上班时间：" +
                                attendanceData.getCheckin().getTime());
                    } else {
                        tvStartTime.setText("上班时间：");
                    }
                    if (attendanceData.getCheckin().getAttendance() != null) {
                        tvStartTimeAttendance.setText("考勤情况：" +
                                attendanceData.getCheckin().getAttendance());
                    } else {
                        tvStartTimeAttendance.setText("考勤情况：");
                    }

                    if (attendanceData.getCheckin().getAddress() != null) {
                        tvStartAttendanceAddress.setText("地址：" +
                                attendanceData.getCheckin().getAddress());
                    } else {
                        tvStartAttendanceAddress.setText("地址：");

                    }


                    if (attendanceData.getSignback().getTime() != null) {
                        tvEndTime.setText("下班时间：" +
                                attendanceData.getSignback().getTime());
                    } else {
                        tvEndTime.setText("下班时间：");
                    }

                    if (attendanceData.getSignback().getAttendance() != null) {
                        tvEndTimeAttendance.setText("考勤情况：" +
                                attendanceData.getSignback().getAttendance());
                    } else {
                        tvEndTimeAttendance.setText("考勤情况：");
                    }
                    if (attendanceData.getSignback().getAddress() != null) {
                        tvEndAttendanceAddress.setText("地址：" +
                                attendanceData.getSignback().getAddress());

                    } else {
                        tvEndAttendanceAddress.setText("地址：");

                    }

                } else {
                    rlAttendanceInfo.setVisibility(View.GONE);
                    Toast.makeText(context, "今天暂无考勤信息", Toast.LENGTH_LONG).show();
                }

            } else {
                rlAttendanceInfo.setVisibility(View.GONE);
                Toast.makeText(context, "今天暂无考勤信息", Toast.LENGTH_LONG).show();

            }
        } else {
            rlAttendanceInfo.setVisibility(View.GONE);
            Toast.makeText(context, "没有考勤信息", Toast.LENGTH_SHORT).show();
        }


    }


    @OnClick({R.id.img_back, R.id.rl_sign_in, R.id.rl_sign_off, R.id.rl_work_recoder,
            R.id.line_attendance_other, R.id.rl_attendance_info, R.id.rl_attendance_sort})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_attendance_info:
                if (rlAttendanceInfo.getVisibility() == View.VISIBLE) {
                    rlAttendanceInfo.setVisibility(View.GONE);
                }

                break;
            case R.id.line_attendance_other:
                if (rlAttendanceInfo.getVisibility() == View.VISIBLE) {
                    rlAttendanceInfo.setVisibility(View.GONE);
                }


                break;
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.rl_sign_in:
                // startActivity(new Intent(context, AttendanceTestSignInActivity.class));
                String todayDate =
                        new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
                boolean select = false;
                int position = 0;
                if (attendanceResponse != null) {
                    for (int i = 0; i < attendanceResponse.size(); i++) {
                        if (todayDate.equals(attendanceResponse.get(i).getDay())) {
                            select = true;
                            position = i;
                            Log.d("tick", "onClick: " + gson.toJson(attendanceResponse.get(i)));
                        }
                    }
                    if (select) {
                        if (attendanceResponse.get(position).getCheckin().getTime() != null) {
                            Toast.makeText(context, "今日已过签到", Toast.LENGTH_SHORT).show();

                        } else {
                            if (openGPSSettings()) {
                                startActivity(new Intent(context, AttendanceTestSignInActivity.class)
                                        .putExtra(AttendanceTestSignInActivity.INFO,
                                                attendanceResponse.get(position)));
                            } else {
                                Toast.makeText(context, "请打开GPS", Toast.LENGTH_SHORT).show();
                            }

                        }

                    } else {
                        Toast.makeText(context, "今日无考勤,无需签到", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(context, "本月无考勤", Toast.LENGTH_SHORT).show();
                }


                break;

            case R.id.rl_sign_off:
                if (openGPSSettings()) {
                    checkSignOff();
                } else {
                    Toast.makeText(context, "请打开GPS", Toast.LENGTH_SHORT).show();
                }

                break;

            case R.id.rl_attendance_sort:
                //考勤排名
                startActivity(new Intent(context, AttendanceSortMonthItemActivity.class)
                        .putExtra(AttendanceSortMonthItemActivity.PROJECT_ID, projectId));

                break;
            case R.id.rl_work_recoder:
                startActivity(new Intent(context, AttendanceRecordActivity.class).putExtra(
                        AttendanceRecordActivity.USER_NAME, username)
                        .putExtra(AttendanceRecordActivity.PROJECT_ID, projectId));

                break;
        }
    }

    private boolean openGPSSettings() {
        boolean openGPSSettings;
        LocationManager locationManager = (LocationManager) this
                .getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
            openGPSSettings = true;
        } else {
            openGPSSettings = false;

        }
        return openGPSSettings;
    }

    private void checkSignOff() {

        mSubscription = mRemoteService.getSignOutInfor(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""))
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
                        if (apiResponse.getCode() == 200 || apiResponse.getCode() == 0) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            SignOutInfor data = gson.fromJson(jsonArray,
                                    new TypeToken<SignOutInfor>() {
                                    }.getType());
                            if (data != null) {
                                startActivity(new Intent(context, AttendanceSignOutActivity.class)
                                        .putExtra(AttendanceSignOutActivity.INFO, data));

                            } else {
                                Toast.makeText(context, "获取签退信息失败", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(context, apiResponse.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


    private void getUserInfo() {

        UserId mUser = new UserId(mPrefsHelper.getPrefs().getString(Constants.UID, ""));
        mSubscription = mRemoteService.getUserInfoById(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), mUser)
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
                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(apiResponse.getData());
                        UserInforById mData = gson.fromJson(jsonArray,
                                new TypeToken<UserInforById>() {
                                }.getType());
                        if (mData != null) {
                            username = mData.getName();
                            attendanceName.setText(username);
                            projectId = String.valueOf(mData.getProject_id());
                        }
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo();
        getData();

        //  getAttendanceList(new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime()));

    }
}
