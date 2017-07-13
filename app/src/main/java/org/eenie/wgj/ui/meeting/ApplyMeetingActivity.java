package org.eenie.wgj.ui.meeting;

import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.meeting.AddMeetingClassRequest;
import org.eenie.wgj.model.response.meeting.MeetingData;
import org.eenie.wgj.model.response.meeting.MeetingPeople;
import org.eenie.wgj.ui.meeting.launchmeeting.AddMeetingClassActivity;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/11 at 11:11
 * Email: 472279981@qq.com
 * Des:
 */

public class ApplyMeetingActivity extends BaseActivity {
    @BindView(R.id.et_meeting_name)
    EditText etMeetingName;
    @BindView(R.id.meeting_class)
    TextView meetingClass;
    @BindView(R.id.edit_meeting_detail)
    EditText etMeetingDetail;
    private String mStartTime;
    private String mEndTime;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    private final int REQUEST_CODE = 0x101;
    private String meetingId;

    @Override
    protected int getContentView() {
        return R.layout.activity_meeting_apply;
    }

    @Override
    protected void updateUI() {


    }

    @OnClick({R.id.img_back, R.id.tv_start_time, R.id.tv_end_time, R.id.tv_apply_ok, R.id.rl_meeting_address})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_apply_ok:
                if (checkInput()) {
                    addMeetingClass(etMeetingName.getText().toString(), mStartTime, mEndTime, meetingId,
                            etMeetingDetail.getText().toString());
                }

                break;
            case R.id.img_back:

                onBackPressed();
                break;
            case R.id.tv_start_time:
                showTimeSinglePicker("start");

                break;
            case R.id.tv_end_time:
                showTimeSinglePicker("end");

                break;
            case R.id.rl_meeting_address:
                if (!TextUtils.isEmpty(mStartTime) && !TextUtils.isEmpty(mEndTime)) {
                    startActivityForResult(new Intent(context, AddMeetingClassActivity.class)
                            .putExtra(AddMeetingClassActivity.STARTTIME, mStartTime).
                                    putExtra(AddMeetingClassActivity.ENDTIME, mEndTime), REQUEST_CODE);

                } else {
                    Toast.makeText(context, "请选择会议的起始时间", Toast.LENGTH_SHORT).show();
                }


                break;
        }
    }

    private void addMeetingClass(String title, String startTime, String endTime, String meetingId,
                                 String content) {
        AddMeetingClassRequest request = new AddMeetingClassRequest(startTime, endTime, content,title,
                Integer.valueOf(meetingId));
        Gson gson = new Gson();
        mSubscription = mRemoteService.applyMeetingClass(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                new MeetingData(gson.toJson(request)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getCode()== 0) {
                            Toast.makeText(context, apiResponse.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(context, apiResponse.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private boolean checkInput() {
        boolean result = true;
        if (TextUtils.isEmpty(etMeetingName.getText().toString())) {
            result = false;
            Toast.makeText(context, "请输入会议名称", Toast.LENGTH_SHORT).show();
        }
        if (result && TextUtils.isEmpty(mStartTime)) {
            result = false;
            Toast.makeText(context, "请选择会议开始时间", Toast.LENGTH_SHORT).show();
        }
        if (result && TextUtils.isEmpty(mEndTime)) {
            result = false;
            Toast.makeText(context, "请选择会议结束时间", Toast.LENGTH_SHORT).show();
        }
        if (result && TextUtils.isEmpty(meetingId)) {
            result = false;
            Toast.makeText(context, "请选择会议室", Toast.LENGTH_SHORT).show();
        }
        if (result && TextUtils.isEmpty(etMeetingDetail.getText().toString())) {
            result = false;
            Toast.makeText(context, "请输入会议详情", Toast.LENGTH_SHORT).show();
        }

        return result;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                MeetingPeople mData = data.getParcelableExtra("meeting");
                if (mData != null) {
                    meetingId = String.valueOf(mData.getId());
                    meetingClass.setText(mData.getName());
                    meetingClass.setTextColor(ContextCompat.getColor
                            (context, R.color.black_light));
                }


            }
        }
    }

    public void showTimeSinglePicker(String sort) {
        View view = View.inflate(context, R.layout.dialog_meeting_time, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();
        EditText etYear = (EditText) dialog.getWindow().findViewById(R.id.et_year);
        EditText etMonth = (EditText) dialog.getWindow().findViewById(R.id.edit_month);
        EditText etDay = (EditText) dialog.getWindow().findViewById(R.id.edit_day);
        EditText etHour = (EditText) dialog.getWindow().findViewById(R.id.edit_hour);
        EditText etMinute = (EditText) dialog.getWindow().findViewById(R.id.edit_minute);
        String mYear = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
        String mMonth = new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());
        String mDay = new SimpleDateFormat("dd").format(Calendar.getInstance().getTime());
        String mHour = new SimpleDateFormat("HH").format(Calendar.getInstance().getTime());
        String mMinute = new SimpleDateFormat("mm").format(Calendar.getInstance().getTime());
        etYear.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        etMonth.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        etDay.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        etHour.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        etMinute.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        final int[] year = new int[1];
        final int[] month = new int[1];
        final int[] day = new int[1];
        final int[] hour = new int[1];
        final int[] minute = new int[1];
        year[0] = Integer.valueOf(mYear);
        month[0] = Integer.valueOf(mMonth);
        day[0] = Integer.valueOf(mDay);
        hour[0] = Integer.valueOf(mHour);
        minute[0] = Integer.valueOf(mMinute);
        etYear.setText(mYear);
        etMonth.setText(mMonth);
        etDay.setText(mDay);
        etHour.setText(mHour);
        etMinute.setText(mMinute);
        etHour.setSelection(mHour.length());
        etMinute.setSelection(mMinute.length());
        dialog.getWindow().findViewById(R.id.btn_add_year).setOnClickListener(v -> {
            String inputYear = etYear.getText().toString();
            if (!TextUtils.isEmpty(inputYear)) {
                year[0] = Integer.valueOf(inputYear);
                year[0] = year[0] + 1;

            } else {
                year[0] = year[0];

            }
            etYear.setText(String.valueOf(year[0]));

        });
        dialog.getWindow().findViewById(R.id.btn_subtract_year).setOnClickListener(v -> {
            String inputYear = etYear.getText().toString();
            if (!TextUtils.isEmpty(inputYear)) {
                year[0] = Integer.valueOf(inputYear);
                if (year[0] <= Integer.valueOf(new SimpleDateFormat("yyyy").
                        format(Calendar.getInstance().getTime()))) {
                    Toast.makeText(context, "不能选择过去的年份", Toast.LENGTH_SHORT).show();
                    year[0] = year[0];
                } else {
                    year[0] = year[0] - 1;
                }
            } else {
                year[0] = year[0];
            }
            etYear.setText(String.valueOf(year[0]));

        });
        dialog.getWindow().findViewById(R.id.btn_add_month).setOnClickListener(v -> {
            String inputMonth = etMonth.getText().toString();
            if (!TextUtils.isEmpty(inputMonth)) {
                month[0] = Integer.valueOf(inputMonth);
                if (month[0] >= 12) {
                    month[0] = Integer.valueOf(new SimpleDateFormat("MM").
                            format(Calendar.getInstance().getTime()));
                } else {
                    month[0] = month[0] + 1;
                }


            } else {
                month[0] = month[0];
            }
            if (month[0] <= 9 && String.valueOf(month[0]).length() == 1) {
                etMonth.setText("0" + String.valueOf(month[0]));

            } else {
                etMonth.setText(String.valueOf(month[0]));

            }

        });
        dialog.getWindow().findViewById(R.id.btn_subtract_month).setOnClickListener(v -> {
            String inputMonth = etMonth.getText().toString();
            if (!TextUtils.isEmpty(inputMonth)) {
                month[0] = Integer.valueOf(inputMonth);
                if (month[0] <= Integer.valueOf(new SimpleDateFormat("MM").
                        format(Calendar.getInstance().getTime()))) {
                    month[0] = 12;
                } else {
                    month[0] = month[0] - 1;
                }


            } else {
                month[0] = month[0];
            }
            if (month[0] <= 9 && String.valueOf(month[0]).length() == 1) {
                etMonth.setText("0" + String.valueOf(month[0]));

            } else {
                etMonth.setText(String.valueOf(month[0]));

            }

        });
        dialog.getWindow().findViewById(R.id.btn_add_day).setOnClickListener(v -> {
            String inputDay = etDay.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                int maxDay = getDaysOfMonth(sdf.parse(mYear + "-" + mMonth + "-" + mDay));
                if (!TextUtils.isEmpty(inputDay)) {

                    if (day[0] >= maxDay) {
                        day[0] = 1;
                    } else {
                        day[0] = day[0] + 1;
                    }

                } else {
                    day[0] = day[0] + 1;
                }
                if (day[0] <= 9 && String.valueOf(day[0]).length() == 1) {
                    etDay.setText("0" + String.valueOf(day[0]));
                } else {
                    etDay.setText(String.valueOf(day[0]));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }


        });
        dialog.getWindow().findViewById(R.id.btn_subtract_day).setOnClickListener(v -> {
            String inputDay = etDay.getText().toString();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                int maxDay = getDaysOfMonth(sdf.parse(year[0] + "-" + month[0] + "-" + day[0]));
                if (!TextUtils.isEmpty(inputDay)) {

                    if (day[0] <= 1) {
                        day[0] = maxDay;
                    } else {
                        day[0] = day[0] - 1;
                    }

                } else {
                    day[0] = day[0];
                }
                if (day[0] <= 9 && String.valueOf(day[0]).length() == 1) {
                    etDay.setText("0" + String.valueOf(day[0]));
                } else {
                    etDay.setText(String.valueOf(day[0]));
                }

            } catch (ParseException e) {
                e.printStackTrace();
            }


        });

        dialog.getWindow().findViewById(R.id.btn_add_hour).setOnClickListener(v -> {
            String inputHour = etHour.getText().toString();
            if (!TextUtils.isEmpty(inputHour)) {
                hour[0] = Integer.valueOf(inputHour);
            }
            if (hour[0] >= 23) {
                hour[0] = 0;
            } else {
                hour[0] = hour[0] + 1;
            }
            if (String.valueOf(hour[0]).length() == 1) {
                etHour.setText(String.valueOf("0" + hour[0]));
            } else {
                etHour.setText(String.valueOf(hour[0]));
            }


        });
        dialog.getWindow().findViewById(R.id.btn_subtract_hour).setOnClickListener(v -> {
            String inputHour = etHour.getText().toString();
            if (!TextUtils.isEmpty(inputHour)) {
                hour[0] = Integer.valueOf(inputHour);
            }
            if (hour[0] <= 0) {
                hour[0] = 3;
            } else {
                hour[0] = hour[0] - 1;
            }
            if (String.valueOf(hour[0]).length() == 1) {
                etHour.setText("0" + String.valueOf(hour[0]));
            } else {
                etHour.setText(String.valueOf(hour[0]));
            }


        });


        dialog.getWindow().findViewById(R.id.btn_add_minute).setOnClickListener(v -> {
            String inputMonth = etMinute.getText().toString();
            if (!TextUtils.isEmpty(inputMonth)) {
                minute[0] = Integer.valueOf(inputMonth);
            }
            if (minute[0] >= 59) {
                minute[0] = 0;
            } else {
                minute[0] = minute[0] + 1;

            }
            if (minute[0] <= 9 && String.valueOf(minute[0]).length() == 1) {
                etMinute.setText("0" + String.valueOf(minute[0]));
            } else {
                etMinute.setText(String.valueOf(minute[0]));
            }


        });
        dialog.getWindow().findViewById(R.id.btn_subtract_minute).setOnClickListener(v -> {
            String inputMonth = etMinute.getText().toString();
            if (!TextUtils.isEmpty(inputMonth)) {
                minute[0] = Integer.valueOf(inputMonth);
            }
            if (minute[0] <= 0) {
                minute[0] = 60;
            } else {
                minute[0] = minute[0] - 1;
            }


            if (minute[0] <= 9 && String.valueOf(minute[0]).length() == 1) {
                etMinute.setText("0" + String.valueOf(minute[0]));
            } else {
                etMinute.setText(String.valueOf(minute[0]));
            }

        });

        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.getWindow().findViewById(R.id.btn_ok).setOnClickListener(v -> {
            String selectYear = etYear.getText().toString();
            String selectMonth = etMonth.getText().toString();
            String selectDay = etDay.getText().toString();
            String selectHour = etHour.getText().toString();
            String selectMinute = etMinute.getText().toString();
            if (!TextUtils.isEmpty(selectYear)) {
                if (Integer.parseInt(selectYear) < Integer.valueOf(mYear)) {
                    selectYear = mYear;
                }

            } else {
                selectYear = String.valueOf(year[0]);
            }
            if (!TextUtils.isEmpty(selectMonth)) {
                if (Integer.parseInt(selectMonth) < Integer.valueOf(mMonth)) {
                    selectMonth = mMonth;
                }

            } else {
                selectMonth = String.valueOf(month[0]);
            }
            if (!TextUtils.isEmpty(selectDay)) {
                if (Integer.parseInt(selectDay) < Integer.valueOf(mDay) ||
                        Integer.parseInt(selectDay) > 31) {
                    selectDay = mDay;
                }
                if (!TextUtils.isEmpty(selectMonth) && !TextUtils.isEmpty(selectYear)) {
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    if (selectMonth.length() == 1) {
                        selectMonth = "0" + selectMonth;
                    }
                    try {
                        int maxDay = getDaysOfMonth(sdf.parse(selectYear + "-" +
                                selectMonth + "-" + "01"));
                        if (Integer.valueOf(selectDay) > maxDay) {
                            selectDay = mDay;
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }

            } else {
                selectDay = String.valueOf(day[0]);
            }

            if (TextUtils.isEmpty(selectHour)) {
                selectHour = mHour;
            } else {
                if (Integer.valueOf(selectHour) >= 24) {
                    selectHour = mHour;
                }
            }
            if (TextUtils.isEmpty(selectMinute)) {
                selectMinute = mMinute;
            } else {
                if (Integer.valueOf(selectMinute) >= 60) {
                    selectMinute = mMinute;
                }

            }
            if (selectMonth.length() == 1 && Integer.parseInt(selectMonth) <= 9) {
                selectMonth = "0" + selectMonth;
            }

            if (selectDay.length() == 1 && Integer.parseInt(selectDay) <= 9) {
                selectDay = "0" + selectDay;
            }
            if (selectHour.length() == 1 && Integer.valueOf(selectHour) <= 9) {
                selectHour = "0" + selectHour;
            }
            if (selectMinute.length() == 1 && Integer.valueOf(selectMinute) <= 9) {
                selectMinute = "0" + selectMinute;

            }
            System.out.println("hour" + etHour);
            switch (sort) {
                case "start":
                    mStartTime = selectYear + "-" + selectMonth + "-" + selectDay + " " + selectHour + ":" + selectMinute;
                    tvStartTime.setText(selectYear + "-" + selectMonth + "-" + selectDay + " " + selectHour + ":" + selectMinute);
                    tvStartTime.setTextSize(12);
                    break;
                case "end":
                    mEndTime = selectYear + "-" + selectMonth + "-" + selectDay + " " + selectHour + ":" + selectMinute;
                    tvEndTime.setText(selectYear + "-" + selectMonth + "-" + selectDay + " " + selectHour + ":" + selectMinute);
                    tvEndTime.setTextSize(12);
                    break;

            }

            dialog.dismiss();

        });

    }

    public static int getDaysOfMonth(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
    }
}
