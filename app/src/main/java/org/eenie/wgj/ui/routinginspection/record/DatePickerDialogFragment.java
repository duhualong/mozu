package org.eenie.wgj.ui.routinginspection.record;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;

import org.eenie.wgj.R;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/6/28 at 17:00
 * Email: 472279981@qq.com
 * Des:
 */

public class DatePickerDialogFragment extends DialogFragment {

    private String Tag = "TimePickerDialogFragment";
    @BindView(R.id.time_picker)
    DatePicker mTimePicker;
    @BindView(R.id.tv_time_picker_title)
    TextView mTitle;
    @BindView(R.id.btn_cancel)
    Button mBtnCancel;
    @BindView(R.id.btn_enter)
    Button mBtnEnter;


    String title;

    FragmentManager manager;
    String tag;

    onTimePickedListener mPickedListener;


    boolean hideDay;


    NumberPicker mDayPicker;
    NumberPicker mMonthPicker;
    NumberPicker mYearPicker;


    EditText mDayInputText;
    EditText mMonthInputText;
    EditText mYearInputText;

    public interface onTimePickedListener {
        void onPicked(String time);
    }


    public void setOnDateDissListener(onTimePickedListener mPickedListener) {
        this.mPickedListener = mPickedListener;
    }

    public DatePickerDialogFragment(FragmentManager manager, String title, String tag) {
        super();
        this.title = title;
        this.manager = manager;
        this.tag = tag;
    }

    public DatePickerDialogFragment(FragmentManager manager, String title, String tag, boolean hideDay) {
        super();
        this.title = title;
        this.manager = manager;
        this.tag = tag;
        this.hideDay = hideDay;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View v = inflater.inflate(R.layout.dialog_fargment_datepicker_layout, container, false);
        ButterKnife.bind(this, v);
//        mTimePicker.setIs24HourView(true);
        mTitle.setText(title);


        hideDay(mTimePicker);


//        mMonthPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
//            @Override
//            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
//                LogUtil.e("change = " + newVal);
//            }
//        });


        if (hideDay) {
            mDayPicker.setVisibility(View.GONE);

        }


        return v;
    }


    public void showDialog() {
        super.show(manager, tag);
    }


    @OnClick(R.id.btn_cancel)
    public void onCancelClick() {
        dismiss();
    }

    @OnClick(R.id.btn_enter)
    public void onEnterClick() {


//        LogUtil.e(mMonthInputText.getText().toString());
















        if (mPickedListener != null) {
//            int hour = mTimePicker.getCurrentHour();
//            int minute = mTimePicker.getCurrentMinute();

            long date;

            if (!TextUtils.isEmpty(mYearInputText.getText().toString()) &&!TextUtils.isEmpty(mMonthInputText.getText().toString()) && !TextUtils.isEmpty(mDayInputText.getText().toString())) {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.YEAR, Integer.parseInt(mYearInputText.getText().toString()));
                calendar.set(Calendar.MONTH, Integer.parseInt(mMonthInputText.getText().toString()) - 1);
                calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(mDayInputText.getText().toString()));
                date = calendar.getTime().getTime();

            } else {
                date = mTimePicker.getCalendarView().getDate();
            }

//            LogUtil.e("date = " );

            mPickedListener.onPicked(new SimpleDateFormat("yyyy-MM-dd").format(date));
        }
        dismiss();
    }


    public String getDateStr() {
        return new SimpleDateFormat("yyyy-MM-dd").format(mTimePicker.getCalendarView().getDate());
    }


    private void onValueChange(DatePicker mDatePicker) {
        try {
            Field field = mDatePicker.getClass().getSuperclass().getField("mDaySpinner");
            NumberPicker numberPicker = (NumberPicker) field.get(mDatePicker);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }


    }


    private void hideDay(DatePicker mDatePicker) {
        try {
            /* 处理android5.0以上的特殊情况 */
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                int daySpinnerId = Resources.getSystem().getIdentifier("day", "id", "android");
                int monthSpinnerId = Resources.getSystem().getIdentifier("month", "id", "android");
                int yearSpinnerId = Resources.getSystem().getIdentifier("year", "id", "android");


                if (daySpinnerId != 0) {
                    mDayPicker = (NumberPicker) mDatePicker.findViewById(daySpinnerId);
                    mMonthPicker = (NumberPicker) mDatePicker.findViewById(monthSpinnerId);
                    mYearPicker = (NumberPicker) mDatePicker.findViewById(yearSpinnerId);






                }
            } else {
                Field[] datePickerfFields = mDatePicker.getClass().getDeclaredFields();
                for (Field datePickerField : datePickerfFields) {
                    if ("mDaySpinner".equals(datePickerField.getName()) || ("mDayPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        try {
                            Object dayPicker = datePickerField.get(mDatePicker);
                            mDayPicker = (NumberPicker) dayPicker;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                    if ("mMonthSpinner".equals(datePickerField.getName()) || ("mMonthPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        try {
                            Object dayPicker = datePickerField.get(mDatePicker);
                            mMonthPicker = (NumberPicker) dayPicker;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                    if ("mYearSpinner".equals(datePickerField.getName()) || ("mYearPicker").equals(datePickerField.getName())) {
                        datePickerField.setAccessible(true);
                        try {
                            Object dayPicker = datePickerField.get(mDatePicker);
                            mYearPicker = (NumberPicker) dayPicker;
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        } catch (IllegalArgumentException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }




            Field dayfield = mDayPicker.getClass().getDeclaredField("mInputText");
            dayfield.setAccessible(true);
            mDayInputText = (EditText) dayfield.get(mDayPicker);

            Field monthfield = mMonthPicker.getClass().getDeclaredField("mInputText");
            monthfield.setAccessible(true);
            mMonthInputText = (EditText) monthfield.get(mMonthPicker);

            Field yearfield = mYearPicker.getClass().getDeclaredField("mInputText");
            yearfield.setAccessible(true);
            mYearInputText = (EditText) yearfield.get(mYearPicker);







        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}