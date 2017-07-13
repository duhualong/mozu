package org.eenie.wgj.ui.personal.alert;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.AttendanceAlertRequest;
import org.eenie.wgj.model.response.alert.AttendanceAlert;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

import butterknife.BindView;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/2 at 14:34
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceWorkActivity extends BaseActivity {
    @BindView(R.id.tv_set_start_time)
    TextView starttime;
    @BindView(R.id.tv_set_end_time)
    TextView endtime;
    @BindView(R.id.checkbox_attendance)
    CheckBox checkBoxAttendance;
    String mStartTime;
    String mEndTime;


    @Override
    protected int getContentView() {
        return R.layout.activity_attendance_alert;
    }

    @Override
    protected void updateUI() {
        getAttendanceData();


    }

    private void getAttendanceData() {

        mSubscription=mRemoteService.getAttendanceAlert(mPrefsHelper.getPrefs().getString(Constants.TOKEN,""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode()==200||apiResponse.getResultCode()==0){
                            if (apiResponse.getData()!=null){
                                Gson gson=new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                AttendanceAlert mData = gson.fromJson(jsonArray,
                                        new TypeToken<AttendanceAlert>() {
                                        }.getType());
                                if (mData!=null){
                                    if (mData.getStart()!=null&&!mData.getStart().isEmpty()){
                                        if (mData.getStart().length()>=6){
                                            mStartTime=mData.getStart().substring(0,5);
                                        }else {
                                            mStartTime=mData.getStart().substring(0,5);
                                        }
                                        starttime.setText(mStartTime);
                                        starttime.setTextColor(ContextCompat.getColor
                                                (context, R.color.titleColor));

                                    }

                                    if (mData.getEnd()!=null&&!mData.getEnd().isEmpty()){
                                        if (mData.getEnd().length()>=6){
                                            mEndTime=mData.getEnd().substring(0,5);
                                        }else {
                                            mEndTime=mData.getEnd();
                                        }
                                        endtime.setText(mEndTime);
                                        endtime.setTextColor(ContextCompat.getColor
                                                (context, R.color.titleColor));
                                    }

                                    if (mData.getOpen()==0){
                                        checkBoxAttendance.setChecked(false);
                                    }else {
                                        checkBoxAttendance.setChecked(true);
                                    }

                                }

                            }

                        }else {
                            Toast.makeText(context,apiResponse.getResultMessage(),Toast.LENGTH_SHORT).show();
                        }


                    }
                });
    }

    @OnClick({R.id.img_back, R.id.checkbox_attendance, R.id.tv_save,
            R.id.rl_set_start_time, R.id.rl_set_end_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.checkbox_attendance:
              openAttendance();


                break;
            case R.id.rl_set_start_time:
                showTimeStartDialog();

                break;
            case R.id.rl_set_end_time:
                showTimeEndDialog();

                break;
            case R.id.tv_save:
                if (!TextUtils.isEmpty(mStartTime)&&!TextUtils.isEmpty(mEndTime)){
                    addAttendanceTime(mStartTime,mEndTime);


                }else {
                    Toast.makeText(context,"请添加考勤提醒的签到或签退时间",Toast.LENGTH_SHORT).show();
                }

                break;


        }
    }

    private void addAttendanceTime(String startTime, String endTime) {
        AttendanceAlertRequest request=new AttendanceAlertRequest(startTime,endTime);
        mSubscription=mRemoteService.addAttendanceTime(mPrefsHelper.getPrefs().getString(Constants.TOKEN,""),request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
               .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                   @Override
                   public void onNext(ApiResponse apiResponse) {
                       if (apiResponse.getResultCode()==200||apiResponse.getResultCode()==0){
                           showSuccessDialog();
                           if (checkBoxAttendance.isChecked()){
                               openAttendance();
                           }

                       }else {
                           Toast.makeText(context,apiResponse.getResultMessage(),
                                   Toast.LENGTH_SHORT).show();
                       }

                   }
               });
    }

    private void openAttendance() {
        mSubscription=mRemoteService.openCloseAttendanceAlert(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN,""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode()==200||apiResponse.getResultCode()==0){
                            Toast.makeText(context,apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(context,apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void showSuccessDialog() {
        View view = View.inflate(context, R.layout.dialog_set_finished, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.btn_ok).setOnClickListener(v -> {
            Toast.makeText(context,"考勤提醒时间设置成功",Toast.LENGTH_SHORT).show();
            dialog.dismiss();

        });
    }

    //考勤结束时间
    private void showTimeEndDialog() {
        View view = View.inflate(context, R.layout.dialog_alert_start_time, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        Button addHour = (Button) dialog.getWindow().findViewById(R.id.btn_add_hour);
        Button subHour = (Button) dialog.getWindow().findViewById(R.id.btn_subtract_hour);
        EditText editHour = (EditText) dialog.getWindow().findViewById(R.id.et_hour);
        Button addMinute = (Button) dialog.getWindow().findViewById(R.id.btn_add_minute);
        Button subMinute = (Button) dialog.getWindow().findViewById(R.id.btn_subtract_minute);
        EditText editMinute = (EditText) dialog.getWindow().findViewById(R.id.edit_minute);
        Button btnOk = (Button) dialog.getWindow().findViewById(R.id.btn_ok);
        if (!TextUtils.isEmpty(mEndTime)){
            editHour.setText(mEndTime.substring(0,2));
            editMinute.setText(mEndTime.substring(3,5));

        }


        addHour.setOnClickListener(v -> {
            String hour = editHour.getText().toString();
            if (TextUtils.isEmpty(hour)){
                hour="0";
            }

            if (Integer.parseInt(hour) >= 23) {
                Toast.makeText(context, "不能超过24小时", Toast.LENGTH_LONG).show();

            } else {
                if (Integer.parseInt(hour) >= 9) {
                    editHour.setText(String.valueOf(Integer.parseInt(hour) + 1));
                } else {
                    editHour.setText("0" + String.valueOf(Integer.parseInt(hour) + 1));
                }
            }
        });


        subHour.setOnClickListener(v -> {
            String hour = editHour.getText().toString();
            if (TextUtils.isEmpty(hour)){
                hour="0";
            }
            if (Integer.parseInt(hour) <= 0) {
                Toast.makeText(context, "不能小于0时", Toast.LENGTH_LONG).show();
            } else {
                if (Integer.parseInt(hour) >= 11) {
                    editHour.setText(String.valueOf(Integer.parseInt(hour) - 1));
                } else {
                    editHour.setText("0" + String.valueOf(Integer.parseInt(hour) - 1));
                }
            }

        });


        addMinute.setOnClickListener(v -> {
            String minute = editMinute.getText().toString();
            if (TextUtils.isEmpty(minute)){
                minute="0";
            }

            if (Integer.parseInt(minute) >= 59) {
                Toast.makeText(context, "不能超过60分钟", Toast.LENGTH_LONG).show();

            } else {
                if (Integer.parseInt(minute) >= 9) {
                    editMinute.setText(String.valueOf(Integer.parseInt(minute) + 1));
                } else {
                    editMinute.setText("0" + String.valueOf(Integer.parseInt(minute) + 1));
                }
            }
        });


        subMinute.setOnClickListener(v -> {
            String minute = editMinute.getText().toString();
            if (TextUtils.isEmpty(minute)){
                minute="0";
            }

            if (Integer.parseInt(minute) <= 0) {
                Toast.makeText(context, "不能少于0分钟", Toast.LENGTH_LONG).show();

            } else {
                if (Integer.parseInt(minute) >= 11) {
                    editMinute.setText(String.valueOf(Integer.parseInt(minute) - 1));
                } else {
                    editMinute.setText("0" + String.valueOf(Integer.parseInt(minute) - 1));
                }

            }

        });

        btnOk.setOnClickListener(v -> {
            String hour = editHour.getText().toString();
            String minute = editMinute.getText().toString();
            if (!TextUtils.isEmpty(hour)&&!TextUtils.isEmpty(minute)) {
                if (Integer.parseInt(hour) >= 24 || Integer.parseInt(hour) < 0) {
                    Toast.makeText(context, "请设置正确的小时！", Toast.LENGTH_LONG).show();
                } else {
                    if (Integer.parseInt(minute) >= 60 || Integer.parseInt(minute) < 0) {
                        Toast.makeText(context, "请设置正确的分钟！", Toast.LENGTH_LONG).show();
                    } else {
                        dialog.dismiss();
                        if (hour.length()<=1&&Integer.parseInt(hour) <= 9) {
                            hour = "0" + hour;

                        }
                        if (minute.length()<=1&&Integer.parseInt(minute) <= 9) {
                            minute = "0" + minute;
                        }

                        mEndTime=hour+":"+minute;
                        endtime.setText(hour + ":" + minute);
                        endtime.setTextColor(ContextCompat.getColor
                                (context, R.color.titleColor));
                    }
                }
            }else {
                Toast.makeText(context,"请设置考勤结束的时间",Toast.LENGTH_LONG).show();
            }

        });


    }

    //选择考勤的开始时间
    private void showTimeStartDialog() {
        View view = View.inflate(context, R.layout.dialog_alert_start_time, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        Button addHour = (Button) dialog.getWindow().findViewById(R.id.btn_add_hour);
        Button subHour = (Button) dialog.getWindow().findViewById(R.id.btn_subtract_hour);
        EditText editHour = (EditText) dialog.getWindow().findViewById(R.id.et_hour);
        Button addMinute = (Button) dialog.getWindow().findViewById(R.id.btn_add_minute);
        Button subMinute = (Button) dialog.getWindow().findViewById(R.id.btn_subtract_minute);
        EditText editMinute = (EditText) dialog.getWindow().findViewById(R.id.edit_minute);
        Button btnOk = (Button) dialog.getWindow().findViewById(R.id.btn_ok);
        if (!TextUtils.isEmpty(mStartTime)){
            editHour.setText(mStartTime.substring(0,2));
            editMinute.setText(mStartTime.substring(3,5));

        }


        addHour.setOnClickListener(v -> {
            String hour = editHour.getText().toString();
            if (TextUtils.isEmpty(hour)){
                hour="0";
            }

            if (Integer.parseInt(hour) >= 23) {
                Toast.makeText(context, "不能超过24小时", Toast.LENGTH_LONG).show();

            } else {
                if (Integer.parseInt(hour) >= 9) {
                    editHour.setText(String.valueOf(Integer.parseInt(hour) + 1));
                } else {
                    editHour.setText("0" + String.valueOf(Integer.parseInt(hour) + 1));
                }
            }
        });


        subHour.setOnClickListener(v -> {
            String hour = editHour.getText().toString();
            if (TextUtils.isEmpty(hour)){
                hour="0";
            }
            if (Integer.parseInt(hour) <= 0) {
                Toast.makeText(context, "不能小于0时", Toast.LENGTH_LONG).show();
            } else {
                if (Integer.parseInt(hour) >= 11) {
                    editHour.setText(String.valueOf(Integer.parseInt(hour) - 1));
                } else {
                    editHour.setText("0" + String.valueOf(Integer.parseInt(hour) - 1));
                }
            }

        });


        addMinute.setOnClickListener(v -> {
            String minute = editMinute.getText().toString();

            if (TextUtils.isEmpty(minute)){
                minute="0";
            }
            if (Integer.parseInt(minute) >= 59) {
                Toast.makeText(context, "不能超过60分钟", Toast.LENGTH_LONG).show();

            } else {
                if (Integer.parseInt(minute) >= 9) {
                    editMinute.setText(String.valueOf(Integer.parseInt(minute) + 1));
                } else {
                    editMinute.setText("0" + String.valueOf(Integer.parseInt(minute) + 1));
                }
            }
        });


        subMinute.setOnClickListener(v -> {
            String minute = editMinute.getText().toString();

            if (TextUtils.isEmpty(minute)){
                minute="0";
            }

            if (Integer.parseInt(minute) <= 0) {
                Toast.makeText(context, "不能少于0分钟", Toast.LENGTH_LONG).show();

            } else {
                if (Integer.parseInt(minute) >= 11) {
                    editMinute.setText(String.valueOf(Integer.parseInt(minute) - 1));
                } else {
                    editMinute.setText("0" + String.valueOf(Integer.parseInt(minute) - 1));
                }

            }

        });

        btnOk.setOnClickListener(v -> {
            String hour = editHour.getText().toString();
            String minute = editMinute.getText().toString();
            if (!TextUtils.isEmpty(hour)&&!TextUtils.isEmpty(minute)) {
                if (Integer.parseInt(hour) >= 24 || Integer.parseInt(hour) < 0) {
                    Toast.makeText(context, "请设置正确的小时！", Toast.LENGTH_LONG).show();
                } else {
                    if (Integer.parseInt(minute) >= 60 || Integer.parseInt(minute) < 0) {
                        Toast.makeText(context, "请设置正确的分钟！", Toast.LENGTH_LONG).show();
                    } else {
                        dialog.dismiss();
                        if (hour.length()<=1&&Integer.parseInt(hour) <= 9) {
                            hour = "0" + hour;

                        }
                        if (minute.length()<=1&&Integer.parseInt(minute) <= 9) {
                            minute = "0" + minute;
                        }

                        mStartTime=hour+":"+minute;
                        starttime.setText(hour + ":" + minute);
                        starttime.setTextColor(ContextCompat.getColor
                                (context, R.color.titleColor));
                    }
                }
            }else {
                Toast.makeText(context,"请设置考勤开始的时间",Toast.LENGTH_LONG).show();
            }

        });
    }


}
