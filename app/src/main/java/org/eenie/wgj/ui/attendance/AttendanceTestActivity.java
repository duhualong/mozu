package org.eenie.wgj.ui.attendance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.loonggg.weekcalendar.view.WeekCalendar;

import org.eenie.wgj.R;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/7/4 at 10:46
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceTestActivity extends AppCompatActivity {
    private WeekCalendar mWeekCalendar;
    private Gson gson = new Gson();
    ArrayList<String> mList = new ArrayList<>();
    ArrayList<String> mLists = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_work);
        ArrayList<String> test=getIntent().getStringArrayListExtra("list");
        ArrayList<String> tests=getIntent().getStringArrayListExtra("lists");
        updateUI(test,tests);
        //initUI();
    }


    private void updateUI(ArrayList<String>list,ArrayList<String>list1) {
        mWeekCalendar = (WeekCalendar) findViewById(R.id.week_calendar);
        mWeekCalendar.setSelectDates(list, list1);
        mWeekCalendar.showToday();


    }




}
