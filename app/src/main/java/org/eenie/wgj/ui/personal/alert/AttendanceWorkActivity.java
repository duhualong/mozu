package org.eenie.wgj.ui.personal.alert;

import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

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

    @Override
    protected int getContentView() {
        return R.layout.activity_attendance_alert;
    }

    @Override
    protected void updateUI() {

    }

    @OnClick({R.id.img_back, R.id.checkbox_attendance, R.id.tv_save,
            R.id.rl_set_start_time, R.id.rl_set_end_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.checkbox_attendance:
                if (checkBoxAttendance.isChecked()){
                    //保存设置的考勤时间
                }else {

                }



                break;
            case R.id.rl_set_start_time:
                showTimeStartDialog();

                break;
            case R.id.rl_set_end_time:
                showTimeEndDialog();

                break;
            case R.id.tv_save:
                showSuccessDialog();

                break;


        }
    }

    private void showSuccessDialog() {
        View view = View.inflate(context, R.layout.dialog_set_finished, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.btn_ok).setOnClickListener(v -> {
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


        addHour.setOnClickListener(v -> {
            String hour = editHour.getText().toString();

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
            if (Integer.parseInt(hour) >= 24 || Integer.parseInt(hour) < 0) {
                Toast.makeText(context, "请设置正确的小时！", Toast.LENGTH_LONG).show();
            } else {
                if (Integer.parseInt(minute) >= 60 || Integer.parseInt(minute) < 0) {
                    Toast.makeText(context, "请设置正确的分钟！", Toast.LENGTH_LONG).show();
                } else {
                    dialog.dismiss();
                    if (Integer.parseInt(hour) <= 9) {
                        hour = "0" + hour;

                    }
                    if (Integer.parseInt(minute) <= 9) {
                        minute = "0" + minute;
                    }
                    endtime.setText(hour + ":" + minute);
                    endtime.setTextColor(ContextCompat.getColor
                            (context, R.color.titleColor));
                }
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


        addHour.setOnClickListener(v -> {
            String hour = editHour.getText().toString();

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
            if (Integer.parseInt(hour) >= 24 || Integer.parseInt(hour) < 0) {
                Toast.makeText(context, "请设置正确的小时！", Toast.LENGTH_LONG).show();
            } else {
                if (Integer.parseInt(minute) >= 60 || Integer.parseInt(minute) < 0) {
                    Toast.makeText(context, "请设置正确的分钟！", Toast.LENGTH_LONG).show();
                } else {
                    dialog.dismiss();
                    if (Integer.parseInt(hour) <= 9) {
                        hour = "0" + hour;

                    }
                    if (Integer.parseInt(minute) <= 9) {
                        minute = "0" + minute;
                    }
                    starttime.setText(hour + ":" + minute);
                    starttime.setTextColor(ContextCompat.getColor
                            (context, R.color.titleColor));
                }
            }

        });
    }

}
