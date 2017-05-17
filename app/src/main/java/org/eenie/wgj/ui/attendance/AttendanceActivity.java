package org.eenie.wgj.ui.attendance;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.loonggg.weekcalendar.view.WeekCalendar;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/5/15 at 18:01
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceActivity extends BaseActivity {
    @BindView(R.id.week_calendar)WeekCalendar mWeekCalendar;
    @BindView(R.id.title_attendance)TextView mTitle;
    @Override
    protected int getContentView() {
        return R.layout.activity_attendance_work;
    }

    @Override
    protected void updateUI() {

        if (mWeekCalendar.showToday()){

            System.out.println("sss");

        }
//点击当前日期time：2017-05-18
        mWeekCalendar.setOnDateClickListener(time ->
                Toast.makeText(context,time,Toast.LENGTH_SHORT).show());

        mWeekCalendar.setOnCurrentMonthDateListener((year, month) -> {
           mTitle.setText(year+"年"+month+"月");
        });


    }
    @OnClick({R.id.img_back,R.id.rl_sign_in,R.id.rl_sign_off,R.id.rl_work_overtime,
    R.id.rl_work_recoder})public void onClick(View view){
        switch (view.getId()){
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


                break;
        }
    }
}
