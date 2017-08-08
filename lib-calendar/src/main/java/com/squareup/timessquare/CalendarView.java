package com.squareup.timessquare;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;


import static com.squareup.timessquare.CalendarPickerView.setMidnight;

import static java.util.Calendar.DATE;
import static java.util.Calendar.DAY_OF_MONTH;
import static java.util.Calendar.DAY_OF_WEEK;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;

/**
 * Created by Eenie on 2016/11/1 at 9:06
 * Email: 472279981@qq.com
 * Des:
 */

public class CalendarView extends FrameLayout implements MonthView.Listener {

    /**
     * 日历表选择模式：单选、多选
     */
    public enum SelectionMode {
        SINGLE,
        MULTIPLE
    }

    Context mContext;
    MonthView mMonthView;
    LayoutInflater mInflater;
    Calendar today;

    private final List<Calendar> selectedCals = new ArrayList<>();

    private OnDateSelectedListener dateListener;
    final List<MonthCellDescriptor> selectedCells = new ArrayList<>();
    final List<MonthCellDescriptor> highlightedCells = new ArrayList<>();
    final List<Calendar> highlightedCals = new ArrayList<>();
    private Calendar minCal;
    private Calendar maxCal;
    private Calendar monthCounter;

    private DateFormat monthNameFormat;
    private DateFormat weekdayNameFormat;
    private DateFormat fullDateFormat;

    private Locale locale;
    private TimeZone timeZone;
    private int dividerColor;
    private int dayBackgroundResId;
    private int dayTextColorResId;
    private int titleTextColor;
    private boolean displayHeader;
    private int headerTextColor;
    private Typeface titleTypeface;
    private Typeface dateTypeface;

    SelectionMode selectionMode = SelectionMode.MULTIPLE;
    private DateSelectableFilter dateConfiguredListener;
    MonthDescriptor mMonthDescriptor;

    private HeightDateClickListener mHeightDateClickListener;

    private OnCalendarChangeListener mOnCalendarChangeListener;

    private DayViewAdapter dayViewAdapter = new DefaultDayViewAdapter();

    public CalendarView(Context context) {
        this(context, null);
    }


    public CalendarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        mInflater = LayoutInflater.from(mContext);
        init(context, attrs);
        initCalendar(mContext);
    }


    public interface HeightDateClickListener {

        void onHeightDateClick(Date date);
    }


    public void setOnCalendarChangeListener(OnCalendarChangeListener mOnCalendarChangeListener) {
        this.mOnCalendarChangeListener = mOnCalendarChangeListener;
    }

    public void setOnHeightDateClickListener(HeightDateClickListener heightDateClickListener) {
        this.mHeightDateClickListener = heightDateClickListener;
    }

    private void init(Context context, AttributeSet attrs) {
        Resources res = context.getResources();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CalendarView);
        final int bg = a.getColor(R.styleable.CalendarView_ysquare_dayBackground, res.getColor(R.color.calendar_bg));
        dividerColor = a.getColor(R.styleable.CalendarView_ysquare_dividerColor, res.getColor(R.color.calendar_divider));
        dayBackgroundResId = a.getResourceId(R.styleable.CalendarView_ysquare_dayBackground, R.drawable.calendar_bg_selector);
        dayTextColorResId = a.getResourceId(R.styleable.CalendarView_ysquare_dayTextColor, R.color.calendar_text_selector);
        titleTextColor = a.getColor(R.styleable.CalendarView_ysquare_titleTextColor, res.getColor(R.color.calendar_text_active));
        displayHeader = a.getBoolean(R.styleable.CalendarView_ysquare_displayHeader, true);
        headerTextColor = a.getColor(R.styleable.CalendarView_ysquare_headerTextColor, res.getColor(R.color.calendar_text_active));
        a.recycle();
        setBackgroundColor(bg);
    }


    private void initCalendar(Context context) {
        timeZone = TimeZone.getDefault();
        locale = Locale.getDefault();
        today = Calendar.getInstance(timeZone, locale);
        minCal = Calendar.getInstance(timeZone, locale);
        maxCal = Calendar.getInstance(timeZone, locale);
        maxCal.set(Calendar.DAY_OF_MONTH, maxCal.getMaximum(Calendar.DAY_OF_MONTH));

        monthCounter = Calendar.getInstance(timeZone, locale);
        monthNameFormat = new SimpleDateFormat(context.getString(R.string.month_name_format), locale);
        monthNameFormat.setTimeZone(timeZone);
        weekdayNameFormat = new SimpleDateFormat(context.getString(R.string.day_name_format), locale);
        weekdayNameFormat.setTimeZone(timeZone);

        mMonthView = MonthView.create(this,
                mInflater,
                weekdayNameFormat,
                this,
                today,
                dividerColor,
                dayBackgroundResId,
                dayTextColorResId,
                titleTextColor,
                true,
                headerTextColor,
                locale,
                dayViewAdapter);

        mMonthView.title.setVisibility(View.GONE);

        upCalendarView();

        addView(mMonthView);
    }


    public void setDayViewAdapter(DayViewAdapter dayViewAdapter) {
        this.dayViewAdapter = dayViewAdapter;
    }


    public void setHeightLight(Date date) {


    }

    public void upCalendarView() {
        Date date = monthCounter.getTime();
        mMonthDescriptor = new MonthDescriptor(
                monthCounter.get(MONTH),
                monthCounter.get(YEAR),
                date,
                monthNameFormat.format(date));

        List<List<MonthCellDescriptor>> cells = getMonthCells(mMonthDescriptor,
                monthCounter);
        mMonthView.init(mMonthDescriptor, cells, false, titleTypeface, dateTypeface);
        Logr.d("date = %s", mMonthDescriptor.getDate());
        if (mOnCalendarChangeListener != null) {
            mOnCalendarChangeListener.onChange(mMonthDescriptor.getDate());
        }
    }

    public void prevMonth() {
        monthCounter.add(MONTH, -1);
        upCalendarView();
    }

    public List<MonthCellDescriptor> getHighlightedCells() {
        return highlightedCells;
    }

    public List<Calendar> getSelectedCells() {
        return selectedCals;
    }

    public void setHeightCells(List<MonthCellDescriptor> monthCellDescriptors) {
        this.highlightedCells.clear();
        this.highlightedCells.addAll(monthCellDescriptors);

    }

    public void setSelectedCells(List<MonthCellDescriptor> monthCellDescriptors) {
        this.selectedCells.clear();
        this.selectedCells.addAll(monthCellDescriptors);

    }


    public void clearSelectedCells() {
        this.selectedCals.clear();
    }

    public void clearHeightCells() {
        this.highlightedCals.clear();
    }

    public void addHeightCells(Calendar calendar) {
        this.highlightedCals.add(calendar);
    }


    public void nextMonth() {

        monthCounter.add(MONTH, 1);

        upCalendarView();
    }


    public Date getDate() {
        return mMonthDescriptor.getDate();
    }


    private static Calendar minDate(List<Calendar> selectedCals) {
        if (selectedCals == null || selectedCals.size() == 0) {
            return null;
        }
        Collections.sort(selectedCals);
        return selectedCals.get(0);
    }

    private static Calendar maxDate(List<Calendar> selectedCals) {
        if (selectedCals == null || selectedCals.size() == 0) {
            return null;
        }
        Collections.sort(selectedCals);
        return selectedCals.get(selectedCals.size() - 1);
    }

    private static boolean sameDate(Calendar cal, Calendar selectedDate) {
        return cal.get(MONTH) == selectedDate.get(MONTH)
                && cal.get(YEAR) == selectedDate.get(YEAR)
                && cal.get(DAY_OF_MONTH) == selectedDate.get(DAY_OF_MONTH);
    }

    private boolean isDateSelectable(Date date) {
        return dateConfiguredListener == null || dateConfiguredListener.isDateSelectable(date);
    }


    private static boolean containsDate(List<Calendar> selectedCals, Calendar cal) {
        for (Calendar selectedCal : selectedCals) {
            if (sameDate(cal, selectedCal)) {
                return true;
            }
        }
        return false;
    }

    private static boolean betweenDates(Calendar cal, Calendar minCal, Calendar maxCal) {
        final Date date = cal.getTime();
        return betweenDates(date, minCal, maxCal);
    }

    static boolean betweenDates(Date date, Calendar minCal, Calendar maxCal) {
        final Date min = minCal.getTime();
        return (date.equals(min) || date.after(min)) // >= minCal
                && date.before(maxCal.getTime()); // && < maxCal
    }


    private boolean isPast(Calendar calendar) {
        return today.before(calendar);
    }

    private boolean isPast(Date calendar) {

        Calendar cal = Calendar.getInstance();
        cal.setTime(calendar);
        return today.before(cal) || today.equals(cal);
    }


    /**
     * 获取一个月中，需要标记的一天：是否被选中、是否是今天...
     *
     * @param descriptor
     * @param startCal
     * @return
     */
    List<List<MonthCellDescriptor>> getMonthCells(MonthDescriptor descriptor, Calendar startCal) {
        Calendar cal = Calendar.getInstance(timeZone, locale);
        cal.setTime(startCal.getTime());

        List<List<MonthCellDescriptor>> cells = new ArrayList<>();
        cal.set(DAY_OF_MONTH, 1);

        int firstDayOfWeek = cal.get(DAY_OF_WEEK);
        int offset = cal.getFirstDayOfWeek() - firstDayOfWeek;
        if (offset > 0) {
            offset -= 7;
        }
        cal.add(Calendar.DATE, offset);
        while ((cal.get(MONTH) < descriptor.getMonth() + 1 || cal.get(YEAR) < descriptor.getYear())
                && cal.get(YEAR) <= descriptor.getYear()) {
            List<MonthCellDescriptor> weekCells = new ArrayList<>();
            for (int c = 0; c < 7; c++) {
                Date date = cal.getTime();
                boolean isCurrentMonth = cal.get(MONTH) == descriptor.getMonth();
                boolean isSelected = isCurrentMonth && containsDate(selectedCals, cal);
                boolean isSelectable = isPast(cal) && isDateSelectable(date);
                boolean isHoliday = (c == 0 && isCurrentMonth) || (c == 6 && isCurrentMonth);
                boolean isToday = sameDate(cal, today);
                boolean isHighlighted = containsDate(highlightedCals, cal);
                int value = cal.get(DAY_OF_MONTH);
                weekCells.add(new MonthCellDescriptor(
                        date,
                        isCurrentMonth,
                        isSelectable,//isSelectable
                        isSelected,//isSelected
                        isToday,
                        isHighlighted,//isHighlighted
                        false,
                        value,
                        null));

                cal.add(DATE, 1);
            }
            cells.add(weekCells);
        }
        return cells;
    }


    public interface DateSelectableFilter {
        boolean isDateSelectable(Date date);
    }


    private boolean doSelectDate(Date date, MonthCellDescriptor cell) {
        Calendar newlySelectedCal = Calendar.getInstance(timeZone, locale);
        newlySelectedCal.setTime(date);
        // Sanitize input: clear out the hours/minutes/seconds/millis.
        setMidnight(newlySelectedCal);
        switch (selectionMode) {
            case MULTIPLE:
                date = applyMultiSelect(date, newlySelectedCal);
                break;
            case SINGLE:
                clearOldSelections();
                break;
            default:
                throw new IllegalStateException("Unknown selectionMode " + selectionMode);
        }

        if (date != null) {
            // Select a new cell.
            if (selectedCells.size() == 0 || !selectedCells.get(0).equals(cell)) {
                selectedCells.add(cell);
                cell.setSelected(true);
            }
            selectedCals.add(newlySelectedCal);
        }
        upCalendarView();
        return date != null;
    }


    private Date applyMultiSelect(Date date, Calendar selectedCal) {
        for (MonthCellDescriptor selectedCell : selectedCells) {
            if (selectedCell.getDate().equals(date)) {
                selectedCell.setSelected(false);
                selectedCells.remove(selectedCell);
                date = null;
                break;
            }
        }
        for (Calendar cal : selectedCals) {
            if (sameDate(cal, selectedCal)) {
                selectedCals.remove(cal);
                break;
            }
        }
        return date;
    }

    private void clearOldSelections() {
        for (MonthCellDescriptor selectedCell : selectedCells) {
            selectedCell.setSelected(false);
            if (dateListener != null) {
                Date selectedDate = selectedCell.getDate();
                dateListener.onDateUnselected(selectedDate);
            }
        }
        selectedCells.clear();
        selectedCals.clear();
    }


    public interface OnDateSelectedListener {
        void onDateSelected(Date date);

        void onDateUnselected(Date date);
    }


    @Override
    public void handleClick(MonthCellDescriptor cell) {

        Date clickedDate = cell.getDate();
        if (!isPast(clickedDate) && !cell.isHighlighted() || !isDateSelectable(clickedDate) /*|| cell.isToday()*/) {

        } else if (cell.isHighlighted()) {
            if (mHeightDateClickListener != null) {
                mHeightDateClickListener.onHeightDateClick(cell.getDate());
            }

        } else {
            boolean wasSelected = doSelectDate(clickedDate, cell);
            if (dateListener != null) {
                if (wasSelected) {
                    dateListener.onDateSelected(clickedDate);
                } else {
                    dateListener.onDateUnselected(clickedDate);
                }
            }
        }
    }


    public interface OnCalendarChangeListener {
        void onChange(Date date);
    }


}
