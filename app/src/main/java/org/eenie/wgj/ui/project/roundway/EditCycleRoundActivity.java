package org.eenie.wgj.ui.project.roundway;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import org.eenie.wgj.model.requset.CycleNumberEdit;
import org.eenie.wgj.model.requset.RoundPointRequest;
import org.eenie.wgj.model.response.CycleNumber;
import org.eenie.wgj.model.response.RoundPoint;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.RxUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/26 at 15:18
 * Email: 472279981@qq.com
 * Des:
 */

public class EditCycleRoundActivity extends BaseActivity {
    public static final String PROJECT_ID = "id";
    public static final String INFO = "info";
    public static final String LINE_ID = "line_id";
    @BindView(R.id.recycler_round_point)
    RecyclerView mRecyclerView;
    private String projectId;
    private CycleNumber data;
    private ArrayList<CycleNumber.Info> mInfoBeen = new ArrayList<>();
    private RoundPointAdapter mAdapter;
    private ArrayList<RoundPoint> mPostWorkLists = new ArrayList<>();
    private String lineId;
    private CycleNumberEdit addData;
    private ArrayList<CycleNumberEdit> mData = new ArrayList<>();
    private int inspectiondayId;
    private RoundPointRequest mRoundPointRequest;


    @Override
    protected int getContentView() {
        return R.layout.activity_round_cycle_setting;
    }

    @Override
    protected void updateUI() {
        data = getIntent().getParcelableExtra(INFO);
        lineId = getIntent().getStringExtra(LINE_ID);
        if (data != null) {
            mInfoBeen = data.getInfo();
            inspectiondayId = data.getInspectiondayid();

        }
        projectId = getIntent().getStringExtra(PROJECT_ID);
        getData(projectId, mInfoBeen);


    }

    @OnClick({R.id.img_back, R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_save:


                for (int i = 0; i < mPostWorkLists.size(); i++) {
                    if (mPostWorkLists.get(i).isChecked()) {
                        addData = new
                                CycleNumberEdit(mPostWorkLists.get(i).getId(),
                                mPostWorkLists.get(i).getTime());
                        mData.add(addData);

                    }
                }
                mRoundPointRequest = new RoundPointRequest(Integer.parseInt(projectId), inspectiondayId,
                        Integer.parseInt(lineId), mData);
                updatePoint(mRoundPointRequest);


                break;


        }
    }

    //更新点位
    private void updatePoint(RoundPointRequest mRequest) {
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        mSubscription = mRemoteService.updateRoundPoint(token, mRequest)
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
                            Toast.makeText(context, "更新成功！", Toast.LENGTH_LONG).show();
                            Intent mIntent = new Intent();
                            mIntent.putExtra("info", data);
                            // 设置结果，并进行传送
                            setResult(6, mIntent);

                            Single.just("").delay(1, TimeUnit.SECONDS).
                                    compose(RxUtils.applySchedulers()).
                                    subscribe(s -> finish()
                                    );


                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_LONG).show();
                        }

                    }
                });
    }

    private void getData(String projectId, ArrayList<CycleNumber.Info> mData) {
        //获取巡检点
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        mSubscription = mRemoteService.getRoundPointList(token, projectId)
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
                            if (apiResponse.getData() != null) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                mPostWorkLists = gson.fromJson(jsonArray,
                                        new TypeToken<ArrayList<RoundPoint>>() {
                                        }.getType());
                                if (mData.size() > 0 && mPostWorkLists.size() > 0) {
                                    for (int i = 0; i < mData.size(); i++) {
                                        for (int j = 0; j < mPostWorkLists.size(); j++) {
                                            if (mPostWorkLists.get(j).getId() ==
                                                    mData.get(i).getInspectionpoint()) {

                                                mPostWorkLists.get(j).setChecked(true);
                                                mPostWorkLists.get(j).setTime(mData.get(i).getInspectiontime());
                                            }
                                        }

                                    }
                                    mAdapter = new RoundPointAdapter(context, mPostWorkLists);
                                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                    mRecyclerView.setLayoutManager(layoutManager);

                                    mRecyclerView.setAdapter(mAdapter);


                                }


                            }


                        }
                    }

                });
    }

    class RoundPointAdapter extends RecyclerView.Adapter<RoundPointAdapter.RoundPointViewHolder> {
        private Context context;
        private ArrayList<RoundPoint> contactsList;

        public RoundPointAdapter(Context context, ArrayList<RoundPoint> contactsList) {
            this.context = context;
            this.contactsList = contactsList;
        }

        @Override
        public RoundPointViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_round_point_show, parent, false);
            return new RoundPointViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RoundPointViewHolder holder, int position) {
            if (contactsList != null && !contactsList.isEmpty()) {
                RoundPoint data = contactsList.get(position);
                holder.setItem(data);
                if (data != null) {
                    position = position + 1;
                    if (position < 10) {
                        holder.tvPost.setText("0" + position + "");
                    } else {
                        holder.tvPost.setText(position + "");
                    }
                    holder.tvName.setText(data.getInspectionname());
                    if (!TextUtils.isEmpty(data.getTime())) {
                        holder.tvTime.setText(data.getTime());
                        holder.mCheckBox.setChecked(true);

                    } else {
                        holder.tvTime.setText("巡检时间");
                        holder.mCheckBox.setChecked(false);
                    }

                }
            }

        }

        @Override
        public int getItemCount() {
            return contactsList.size();
        }

        public void addAll(List<RoundPoint> contactsList) {
            this.contactsList.addAll(contactsList);
            RoundPointAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.contactsList.clear();
            RoundPointAdapter.this.notifyDataSetChanged();
        }

        class RoundPointViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private CheckBox mCheckBox;
            private TextView tvPost;
            private TextView tvName;
            private TextView tvTime;
            private RoundPoint infoBean;

            public RoundPointViewHolder(View itemView) {

                super(itemView);

                mCheckBox = ButterKnife.findById(itemView, R.id.checkbox_password_remember);
                tvPost = ButterKnife.findById(itemView, R.id.tv_post);
                tvName = ButterKnife.findById(itemView, R.id.item_name);
                tvTime = ButterKnife.findById(itemView, R.id.btn_time);
                tvTime.setOnClickListener(this);
                mCheckBox.setOnClickListener(this);


            }

            public void setItem(RoundPoint data) {
                infoBean = data;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.checkbox_password_remember:
                        if (mCheckBox.isChecked()) {
                            infoBean.setChecked(true);
                        } else {
                            infoBean.setChecked(false);
                        }
                        break;
                    case R.id.btn_time:
                        showTimeDialog(tvTime, infoBean);
                        break;
                }
            }
        }
    }

    private void showTimeDialog(TextView textView, RoundPoint data) {
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
        editMinute.requestFocus();
        editHour.requestFocus();

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
                        data.setTime(hour + ":" + minute);
                        if (Integer.parseInt(hour) <= 9 && hour.length() == 1) {
                            hour = "0" + hour;
                        }
                        if (Integer.parseInt(minute) <= 9 && minute.length() == 1) {
                            minute = "0" + minute;
                        }
                        textView.setText(hour + ":" + minute);


                    }
                }
            } else {
                Toast.makeText(context, "请设置班次时间", Toast.LENGTH_LONG).show();
            }

        });
    }
}
