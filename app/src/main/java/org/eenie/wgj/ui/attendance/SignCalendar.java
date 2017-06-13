package org.eenie.wgj.ui.attendance;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.squareup.timessquare.MonthCellDescriptor;
import com.squareup.timessquare.MonthDescriptor;
import com.squareup.timessquare.MonthView;

import org.eenie.wgj.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


/**
 * Created by Eenie on 2017/6/13 at 15:14
 * Email: 472279981@qq.com
 * Des:
 * RankItemColorAgent
 */

public class SignCalendar  extends FrameLayout implements MonthView.Listener {
    Context mContext;
    Calendar todayCal;
    Calendar minCal;//日历显示最小的那天
    Calendar maxCal;//日历显示最大的一天
    Calendar monthCounter;

    MonthView mMonthView;
    MonthDescriptor mMonthDescriptor;
    LayoutInflater mInflater;

    TimeZone timeZone;
    Locale locale;

    SignCalendarAdapter mDayViewAdapter;

    SignCalendarCellDecorator mSignCalendarCellDecorator;


    private int dividerColor;
    private int dayBackgroundResId;
    private int dayTextColorResId;
    private int titleTextColor;
    private boolean displayHeader;
    private int headerTextColor;

    private DateFormat monthNameFormat;
    private DateFormat weekdayNameFormat;


    public SignCalendar(Context context) {
        this(context, null);
    }

    public SignCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        init(attrs);
        initCalendar();
    }

    private void init(AttributeSet attrs) {
        Resources res = mContext.getResources();
        TypedArray a = mContext.obtainStyledAttributes(attrs, R.styleable.SignCalendar);
        dividerColor = a.getColor(R.styleable.SignCalendar_sign_dividerColor, res.getColor(com.squareup.timessquare.R.color.calendar_divider));
        dayBackgroundResId = a.getResourceId(R.styleable.SignCalendar_sign_dayBackground, com.squareup.timessquare.R.drawable.calendar_bg_selector);
        dayTextColorResId = a.getResourceId(R.styleable.SignCalendar_sign_dayTextColor, com.squareup.timessquare.R.color.calendar_text_selector);
        titleTextColor = a.getColor(R.styleable.SignCalendar_sign_titleTextColor, res.getColor(com.squareup.timessquare.R.color.calendar_text_active));
        displayHeader = a.getBoolean(R.styleable.SignCalendar_sign_displayHeader, true);
        headerTextColor = a.getColor(R.styleable.SignCalendar_sign_headerTextColor, res.getColor(com.squareup.timessquare.R.color.calendar_text_active));
        a.recycle();
    }

    private void initCalendar() {
        timeZone = TimeZone.getDefault();
        locale = Locale.getDefault();
        todayCal = Calendar.getInstance(timeZone, locale);
        minCal = Calendar.getInstance(timeZone, locale);
        maxCal = Calendar.getInstance(timeZone, locale);
        monthCounter = Calendar.getInstance(timeZone, locale);

        maxCal.set(Calendar.DAY_OF_MONTH, maxCal.getMaximum(Calendar.DAY_OF_MONTH));

        mDayViewAdapter = new SignCalendarAdapter();
        weekdayNameFormat = new SimpleDateFormat(mContext.getString(com.squareup.timessquare.R.string.day_name_format), locale);
        monthNameFormat = new SimpleDateFormat(mContext.getString(com.squareup.timessquare.R.string.month_name_format), locale);

        mMonthView = MonthView.create(this,
                mInflater,
                weekdayNameFormat,
                this,
                todayCal,
                dividerColor,
                dayBackgroundResId,
                dayTextColorResId,
                Color.BLACK,
                true,
                Color.BLACK,
                locale,
                mDayViewAdapter);



        mMonthView.title.setVisibility(View.GONE);
        initMonthView();
        addView(mMonthView);
    }


    public SignCalendarCellDecorator getSignCalendarCellDecorator() {
        return mSignCalendarCellDecorator;
    }

    public void setSignCalendarCellDecorator(SignCalendarCellDecorator signCalendarCellDecorator) {
        mMonthView.setGlobalDecorator(signCalendarCellDecorator);
    }




    /**
     * 填充日历数据
     */
    public void initMonthView() {
        Date date = monthCounter.getTime();
        mMonthDescriptor = new MonthDescriptor(
                monthCounter.get(Calendar.MONTH),
                monthCounter.get(Calendar.YEAR),
                date,
                monthNameFormat.format(date));
        List<List<MonthCellDescriptor>> cells = getMonthCells(mMonthDescriptor, monthCounter);
        mMonthView.init(mMonthDescriptor, cells, false, null, null);
    }
    public void initMonthView(Calendar calendar) {
        monthCounter = calendar;
        Date date = monthCounter.getTime();
        mMonthDescriptor = new MonthDescriptor(
                monthCounter.get(Calendar.MONTH),
                monthCounter.get(Calendar.YEAR),
                date,
                monthNameFormat.format(date));
        List<List<MonthCellDescriptor>> cells = getMonthCells(mMonthDescriptor, monthCounter);
        mMonthView.init(mMonthDescriptor, cells, false, null, null);
    }


    List<List<MonthCellDescriptor>> getMonthCells(MonthDescriptor descriptor, Calendar startCal) {
        Calendar cal = Calendar.getInstance(timeZone, locale);
        cal.setTime(startCal.getTime());
        List<List<MonthCellDescriptor>> cells = new ArrayList<>();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        int offset = cal.getFirstDayOfWeek() - firstDayOfWeek;
        if (offset > 0) {
            offset -= 7;
        }
        cal.add(Calendar.DATE, offset);
        while ((cal.get(Calendar.MONTH) < descriptor.getMonth() + 1 || cal.get(Calendar.YEAR) < descriptor.getYear())
                && cal.get(Calendar.YEAR) <= descriptor.getYear()) {
            List<MonthCellDescriptor> weekCells = new ArrayList<>();
            for (int c = 0; c < 7; c++) {
                Date date = cal.getTime();
                boolean isCurrentMonth = cal.get(Calendar.MONTH) == descriptor.getMonth();
                int value = cal.get(Calendar.DAY_OF_MONTH);
                System.out.println("isCurrentMonth = " + isCurrentMonth);

                weekCells.add(new MonthCellDescriptor(
                        date,
                        isCurrentMonth,
                        true,
                        false,
                        false,
                        false,
                        false,
                        value,
                        null));
                cal.add(Calendar.DATE, 1);
            }
            cells.add(weekCells);
        }
        return cells;
    }


    @Override
    public void handleClick(MonthCellDescriptor cell) {
//        LogUtil.json(cell);
    }

    private static boolean sameDate(Calendar cal, Calendar selectedDate) {
        return cal.get(Calendar.MONTH) == selectedDate.get(Calendar.MONTH)
                && cal.get(Calendar.YEAR) == selectedDate.get(Calendar.YEAR)
                && cal.get(Calendar.DAY_OF_MONTH) == selectedDate.get(Calendar.DAY_OF_MONTH);
    }
}
