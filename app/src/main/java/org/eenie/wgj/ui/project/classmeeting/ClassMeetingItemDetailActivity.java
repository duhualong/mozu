package org.eenie.wgj.ui.project.classmeeting;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.ClassMeetingRequest;
import org.eenie.wgj.model.response.ClassMeetingList;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.RxUtils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/22 at 10:10
 * Email: 472279981@qq.com
 * Des:
 */

public class ClassMeetingItemDetailActivity extends BaseActivity {
    public static final String INFO = "info";
    public static final String PROJECT_ID = "project_id";
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.et_input_work_title)
    EditText etTitle;
    @BindView(R.id.tv_start_time)
    TextView tvStartTime;
    @BindView(R.id.tv_end_time)
    TextView tvEndTime;
    private String mTitle;
    private String startTime;
    private String endTime;
    private ClassMeetingList data;
    private String projectId;
    @BindView(R.id.ly_show_button)
    LinearLayout lyButton;
    @BindView(R.id.tv_save)
    TextView tvSave;
    private String mId;


    @Override
    protected int getContentView() {
        return R.layout.activity_class_meeting_item_detail;
    }

    @Override
    protected void updateUI() {
        data = getIntent().getParcelableExtra(INFO);
        if (data != null) {
            lyButton.setVisibility(View.VISIBLE);
            tvSave.setVisibility(View.GONE);

            if (!TextUtils.isEmpty(data.getEndtime())) {
                endTime = data.getEndtime();
                tvEndTime.setText(data.getEndtime());
                tvEndTime.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            }
            if (!TextUtils.isEmpty(data.getStarttime())) {
                startTime = data.getStarttime();
                tvStartTime.setText(data.getStarttime());
                tvStartTime.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            }
            if (!TextUtils.isEmpty(data.getServicesname())) {
                etTitle.setText(data.getServicesname());
            }
            mId = data.getId() + "";
        } else {
            lyButton.setVisibility(View.GONE);
            tvSave.setVisibility(View.VISIBLE);
        }
        projectId = getIntent().getStringExtra(PROJECT_ID);


    }

    @OnClick({R.id.img_back, R.id.tv_save, R.id.button_save, R.id.button_delete, R.id.ly_start_time,
            R.id.ly_end_time})
    public void onClick(View view) {
        mTitle = etTitle.getText().toString();
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_save:
                if (!TextUtils.isEmpty(mTitle)) {
                    if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
                        ClassMeetingRequest request = new ClassMeetingRequest(projectId, mTitle,
                                startTime, endTime);
                        //添加班次
                        addClassMeetingItem(token, request);


                    } else {
                        Snackbar.make(rootView, "请选择班次的时间", Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    Snackbar.make(rootView, "输入的班次名称不能为空", Snackbar.LENGTH_SHORT).show();
                }


                break;
            case R.id.button_delete:
                if (!TextUtils.isEmpty(projectId)) {
                    //删除班次
                    deleteClassMeetingItem(token, projectId);

                }

                break;
            case R.id.button_save:
                if (!TextUtils.isEmpty(mTitle)) {
                    if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
                        ClassMeetingRequest request = new ClassMeetingRequest(projectId, mTitle,
                                startTime, endTime,mId);
                        //编辑班次
                        editClassMeetingItem(token, request);


                    } else {
                        Snackbar.make(rootView, "请选择班次的时间", Snackbar.LENGTH_SHORT).show();
                    }

                } else {
                    Snackbar.make(rootView, "输入的班次名称不能为空", Snackbar.LENGTH_SHORT).show();
                }




                break;
            case R.id.ly_start_time:
                showTimeStartDialog(tvStartTime, 0);


                break;
            case R.id.ly_end_time:
                showTimeStartDialog(tvEndTime, 1);

                break;

        }
    }

    private void editClassMeetingItem(String token, ClassMeetingRequest request) {
        mSubscription=mRemoteService.editClassItem(token,request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode()==200||apiResponse.getResultCode()==0){
                            Toast.makeText(context,"编辑成功",Toast.LENGTH_SHORT).show();
                            Single.just("").delay(1, TimeUnit.SECONDS).
                                    compose(RxUtils.applySchedulers()).
                                    subscribe(s -> finish()
                                    );
                        }

                    }
                });
    }

    private void addClassMeetingItem(String token, ClassMeetingRequest request) {
        mSubscription=mRemoteService.addClassItem(token,request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode()==200||apiResponse.getResultCode()==0){
                            Toast.makeText(context,"添加成功",Toast.LENGTH_SHORT).show();
                            Single.just("").delay(1, TimeUnit.SECONDS).
                                    compose(RxUtils.applySchedulers()).
                                    subscribe(s -> finish()
                                    );
                        }

                    }
                });
    }

    private void deleteClassMeetingItem(String token, String projectId) {
        mSubscription = mRemoteService.deleteClassItem(token, Integer.parseInt(projectId))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                            Single.just("").delay(1, TimeUnit.SECONDS).
                                    compose(RxUtils.applySchedulers()).
                                    subscribe(s -> finish()
                                    );
                        }

                    }
                });
    }


    //选择时间
    private void showTimeStartDialog(TextView textView, int type) {
        View view = View.inflate(context, R.layout.dialog_alert_start_time, null);
        TextView dialogTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        dialogTitle.setText("设置班次时间");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
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
            if (TextUtils.isEmpty(hour)) {
                hour = "0";
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
            if (TextUtils.isEmpty(hour)) {
                hour = "0";
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

            if (TextUtils.isEmpty(minute)) {
                minute = "0";
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

            if (TextUtils.isEmpty(minute)) {
                minute = "0";
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
            if (!TextUtils.isEmpty(hour) && !TextUtils.isEmpty(minute)) {
                if (Integer.parseInt(hour) >= 24 || Integer.parseInt(hour) < 0) {
                    Toast.makeText(context, "请设置正确的小时！", Toast.LENGTH_LONG).show();
                } else {
                    if (Integer.parseInt(minute) >= 60 || Integer.parseInt(minute) < 0) {
                        Toast.makeText(context, "请设置正确的分钟！", Toast.LENGTH_LONG).show();
                    } else {
                        dialog.dismiss();
                        switch (type) {
                            case 0:
                                startTime = hour + ":" + minute;
                                break;
                            case 1:
                                endTime = hour + ":" + minute;

                                break;
                        }
                        if (Integer.parseInt(hour)<=9&&hour.length()<=1){
                            hour="0"+hour;
                        }
                        if (Integer.parseInt(minute)<=9&&minute.length()<=1){
                            minute="0"+minute;
                        }
                        textView.setText(hour + ":" + minute);
                        textView.setTextColor(ContextCompat.getColor
                                (context, R.color.titleColor));
                    }
                }
            } else {
                Toast.makeText(context, "请设置工作的时间", Toast.LENGTH_LONG).show();
            }

        });
    }
}
