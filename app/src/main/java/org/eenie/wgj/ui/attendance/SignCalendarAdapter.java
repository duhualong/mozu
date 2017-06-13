package org.eenie.wgj.ui.attendance;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.squareup.timessquare.CalendarCellView;
import com.squareup.timessquare.DayViewAdapter;

import org.eenie.wgj.R;

/**
 * Created by Eenie on 2017/6/13 at 15:15
 * Email: 472279981@qq.com
 * Des:
 */

public class SignCalendarAdapter implements DayViewAdapter {
    @Override
    public void makeCellView(CalendarCellView parent) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.statistics_day_view_layout, null);
        parent.addView(layout);
        parent.setDayOfMonthTextView((TextView) layout.findViewById(R.id.day_view));
    }
}
