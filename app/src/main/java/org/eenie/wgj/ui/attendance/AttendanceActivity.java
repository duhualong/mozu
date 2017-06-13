package org.eenie.wgj.ui.attendance;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.loonggg.weekcalendar.view.WeekCalendar;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.AttendanceListResponse;
import org.eenie.wgj.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    @BindView(R.id.week_calendar)
    WeekCalendar mWeekCalendar;
    @BindView(R.id.title_attendance)
    TextView mTitle;
    private Gson gson = new Gson();

    @Override
    protected int getContentView() {
        return R.layout.activity_attendance_work;
    }

    @Override
    protected void updateUI() {
        mTitle.setText(new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime()));
        getAttendanceList(new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime()));

//        if (mWeekCalendar.showToday()) {
//
//            System.out.println("sss");
//
//        }
//点击当前日期time：2017-05-18
        mWeekCalendar.setOnDateClickListener(time ->
                Toast.makeText(context, time, Toast.LENGTH_SHORT).show());

        mWeekCalendar.setOnCurrentMonthDateListener((year, month) -> {
            mTitle.setText(year + "年" + month + "月");
        });


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
                                String jsonArray = gson.toJson(apiResponse.getData());
                               ArrayList<AttendanceListResponse> attendanceResponse =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<ArrayList<AttendanceListResponse>>() {
                                                }.getType());

                                List<String> list = new ArrayList<>();
                                if (attendanceResponse!=null){
                                    for (int i=0;i<attendanceResponse.size();i++){
                                        list.add(attendanceResponse.get(i).getDay());
                                    }
                                    Log.d("list", "onNext: "+gson.toJson(list));

                                    mWeekCalendar.setSelectDates(list);

                                }

                            }


//传入已经预约或者曾经要展示选中的时间列表


                        }

                    }
                });
    }

    @OnClick({R.id.img_back, R.id.rl_sign_in, R.id.rl_sign_off, R.id.rl_work_overtime,
            R.id.rl_work_recoder})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.rl_sign_in:


                break;

            case R.id.rl_sign_off:


                break;

            case R.id.rl_work_overtime:


                break;
            case R.id.rl_work_recoder:
                startActivity(new Intent(context,AttendanceRecordActivity.class));


                break;
        }
    }
}
