//package org.eenie.wgj.ui.meeting.launchmeeting;
//
//import android.content.res.Resources;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.DialogFragment;
//import android.support.v4.app.FragmentManager;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.NumberPicker;
//import android.widget.TextView;
//import android.widget.TimePicker;
//
//import org.eenie.wgj.R;
//import org.eenie.wgj.ui.routinginspection.record.DatePickerDialogFragment;
//
//import java.lang.reflect.Field;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
//
///**
// * Created by Eenie on 2016/10/28 at 9:20
// * Email: 472279981@qq.com
// * Des:
// */
//
//public class TimePickerDialogFragment extends DialogFragment {
//
//    private String Tag = "TimePickerDialogFragment";
//    @BindView(R.id.time_picker)
//    TimePicker mTimePicker;
//    @BindView(R.id.tv_time_picker_title)
//    TextView mTitle;
//    @BindView(R.id.btn_cancel)
//    Button mBtnCancel;
//    @BindView(R.id.btn_enter)
//    Button mBtnEnter;
//
//
//    String title;
//    String date = "";
//
//
//    FragmentManager manager;
//    String tag;
//
//    onTimePickedListener mPickedListener;
//
//
//    EditText mHourInput;
//    EditText mMinuteInput;
//
//
//    public interface onTimePickedListener {
//        void onPicked(String time);
//    }
//
//
//    public void setOnTimePickedListener(onTimePickedListener mPickedListener) {
//        this.mPickedListener = mPickedListener;
//    }
//
//    public TimePickerDialogFragment(FragmentManager manager, String title, String tag) {
//        super();
//        this.title = title;
//        this.manager = manager;
//        this.tag = tag;
//    }
//
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        View v = inflater.inflate(R.layout.dialog_fragment_timepicker_layout, container, false);
//        ButterKnife.bind(this, v);
//
////        mTimePicker = new TimePicker();
//
//        mTimePicker.setIs24HourView(true);
//
//
//        mTitle.setText(title);
//
//
//        handleTime(mTimePicker);
//        return v;
//    }
//
//
//    public void showDialog() {
//        super.show(manager, tag);
//    }
//
//
//    public void showWhitDate() {
//        final DatePickerDialogFragment datePickerDialogFragment = new DatePickerDialogFragment(manager, "选择日期", "date");
//        datePickerDialogFragment.setOnDateDissListener(new DatePickerDialogFragment.onTimePickedListener() {
//            @Override
//            public void onPicked(String date) {
//                TimePickerDialogFragment.this.date = date;
//                showDialog();
//            }
//        });
//
//        datePickerDialogFragment.show(manager, "date");
//    }
//
//
//    @OnClick(R.id.btn_cancel)
//    public void onCancelClick() {
//        dismiss();
//    }
//
//    @OnClick(R.id.btn_enter)
//    public void onEnterClick() {
//
//
//        if (mPickedListener != null) {
//
//            if (!TextUtils.isEmpty(mHourInput.getText().toString()) && !TextUtils.isEmpty(mMinuteInput.getText().toString())) {
//
//                String h = mHourInput.getText().toString();
//                String m = mMinuteInput.getText().toString();
//                if (m.length() < 2) {
//                    m = "0" + m;
//                }
//
//                if (h.length() < 2) {
//                    h = "0" + h;
//                }
//
//                String time = String.format("%s:%s", h, m);
//                if (TextUtils.isEmpty(date)) {
//                    mPickedListener.onPicked(time);
//                } else {
//                    mPickedListener.onPicked(date + " " + time);
//                }
//            } else {
//                int hour = mTimePicker.getCurrentHour();
//                int minute = mTimePicker.getCurrentMinute();
//                if (TextUtils.isEmpty(date)) {
//                    mPickedListener.onPicked(String.format("%1s:%2s", hour >= 10 ? hour : "0" + hour, minute >= 10 ? minute : "0" + minute));
//                } else {
//                    mPickedListener.onPicked(date + String.format(" %1s:%2s", hour >= 10 ? hour : "0" + hour, minute >= 10 ? minute : "0" + minute));
//                }
//            }
//
//        }
//        dismiss();
//    }
//
//
//    private void handleTime(TimePicker timePicker) {
//
//
//        NumberPicker mHourPicker = null;
//        NumberPicker mMonthPicker = null;
//
//
//        try {
//            /* 处理android5.0以上的特殊情况 */
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                int hourSpinnerId = Resources.getSystem().getIdentifier("hour", "id", "android");
//                int minuteSpinnerId = Resources.getSystem().getIdentifier("minute", "id", "android");
//
//                if (hourSpinnerId != 0) {
//                    mHourPicker = (NumberPicker) timePicker.findViewById(hourSpinnerId);
//                    mMonthPicker = (NumberPicker) timePicker.findViewById(minuteSpinnerId);
//
//
//                }
//            } else {
//                Field[] datePickerfFields = timePicker.getClass().getDeclaredFields();
//                for (Field datePickerField : datePickerfFields) {
//                    if ("mHourSpinner".equals(datePickerField.getName()) || ("mHourPicker").equals(datePickerField.getName())) {
//                        datePickerField.setAccessible(true);
//                        try {
//                            Object dayPicker = datePickerField.get(timePicker);
//                            mHourPicker = (NumberPicker) dayPicker;
//                        } catch (IllegalAccessException e) {
//                            e.printStackTrace();
//                        } catch (IllegalArgumentException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                    if ("mMinuteSpinner".equals(datePickerField.getName()) || ("mMinutePicker").equals(datePickerField.getName())) {
//                        datePickerField.setAccessible(true);
//                        try {
//                            Object dayPicker = datePickerField.get(timePicker);
//                            mMonthPicker = (NumberPicker) dayPicker;
//                        } catch (IllegalAccessException e) {
//                            e.printStackTrace();
//                        } catch (IllegalArgumentException e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//            }
//
//
//            Field hourfield = mHourPicker.getClass().getDeclaredField("mInputText");
//            hourfield.setAccessible(true);
//            mHourInput = (EditText) hourfield.get(mHourPicker);
//
//            Field monthfield = mMonthPicker.getClass().getDeclaredField("mInputText");
//            monthfield.setAccessible(true);
//            mMinuteInput = (EditText) monthfield.get(mMonthPicker);
//
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
//
//}
