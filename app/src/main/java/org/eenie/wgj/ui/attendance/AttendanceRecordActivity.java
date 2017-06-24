package org.eenie.wgj.ui.attendance;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
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
 * Created by Eenie on 2017/6/13 at 14:24
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceRecordActivity extends BaseActivity {
    public static final String USER_NAME="user_name";

    @BindView(R.id.tv_date)
    TextView titleDate;
    @BindView(R.id.tv_position_name)
    TextView tvPositionName;
    @BindView(R.id.calendar_view)
    SignCalendar mSignCalendar;
    SignCalendarCellDecorator mDecorator;
    private String userName;
    @Override
    protected int getContentView() {
        return R.layout.activity_attendance_record;
    }
    @Override
    protected void updateUI() {
       userName=getIntent().getStringExtra(USER_NAME);
        if (!TextUtils.isEmpty(userName)){
            tvPositionName.setText(userName);
        }

        titleDate.setText(new SimpleDateFormat("yyyy年MM月").format(Calendar.getInstance().getTime()));

        getAttendanceList(new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime()));


    }

    private void getAttendanceList(String date) {
        mSubscription = mRemoteService.getAttendanceDayOfMonth(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""), date,
                mPrefsHelper.getPrefs().getString(Constants.UID,""))
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
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {

                            if (apiResponse.getData() != null) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<StatisticsInfoEntity.ResultMessageBean> results =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<ArrayList<
                                                        StatisticsInfoEntity.ResultMessageBean>>() {
                                                }.getType());
                                if (results != null) {
                                    mDecorator = new SignCalendarCellDecorator();
                                    mDecorator.setData(results);
                                    mSignCalendar.setSignCalendarCellDecorator(mDecorator);
                                    mSignCalendar.initMonthView();
                                }
                            }

                        }

                    }
                });
    }

    @OnClick({R.id.img_back, R.id.rl_attendance_error})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.rl_attendance_error:
                startActivity(new Intent(context, AttendanceAbnormalActivity.class)
                .putExtra(AttendanceAbnormalActivity.DATE,"")
                .putExtra(AttendanceAbnormalActivity.UID,""));
                break;
        }
    }

}
