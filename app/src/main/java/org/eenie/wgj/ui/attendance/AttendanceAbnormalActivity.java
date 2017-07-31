package org.eenie.wgj.ui.attendance;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.newattendancestatistic.UserAttendanceStatisticData;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/14 at 9:13
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceAbnormalActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @BindView(R.id.swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_attendance_abnormal)
    RecyclerView mRecyclerView;
    private AbnormalAdapter mAdapter;
    public static final String DATE = "date";
    public static final String UID = "uid";
    public static final String PROJECT_ID = "id";
    private String date;
    private String uid;
    private String projectId;

    @Override
    protected int getContentView() {
        return R.layout.activity_attendance_abnormal;
    }

    @Override
    protected void updateUI() {
        date = getIntent().getStringExtra(DATE);
        uid = getIntent().getStringExtra(UID);
        projectId = getIntent().getStringExtra(PROJECT_ID);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mAdapter = new AbnormalAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

    }


    private void getAttendanceList(String date) {

        mSubscription = mRemoteService.getUserAttendanceStatisticData(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                uid, projectId, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        cancelRefresh();
                        if (apiResponse.getCode() == 0) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            UserAttendanceStatisticData data =
                                    gson.fromJson(jsonArray,
                                            new TypeToken<UserAttendanceStatisticData>() {
                                            }.getType());

                            if (data != null) {
                                if (data.getException_list() != null) {
                                    if (mAdapter != null) {
                                        mAdapter.clear();
                                    }
                                    mAdapter.addAll(data.getException_list());
                                }

                            }
//
                        } else {
                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }


                    }
                });

//        mSubscription = mRemoteService.getAttendanceDayOfMonth(
//                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
//                date, uid)
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Subscriber<ApiResponse>() {
//                    @Override
//                    public void onCompleted() {
//
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                    }
//
//                    @Override
//                    public void onNext(ApiResponse apiResponse) {
//                        cancelRefresh();
//                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
//
//                            if (apiResponse.getData() != null) {
//                                Gson gson = new Gson();
//                                String jsonArray = gson.toJson(apiResponse.getData());
//                                ArrayList<AttendanceAbnormal> data =
//                                        gson.fromJson(jsonArray,
//                                                new TypeToken<ArrayList<AttendanceAbnormal>>() {
//                                                }.getType());
//                                if (data != null) {
//
//                                    if (mAdapter != null) {
//                                        ArrayList<AttendanceAbnormal>
//                                                mData = new ArrayList<>();
//                                        for (int i = 0; i < data.size(); i++) {
//                                            if (data.get(i).getStateCode() == 1) {
//                                                mData.add(data.get(i));
//                                            }
//                                        }
//                                        Log.d("test", "data: " + gson.toJson(mData));
//
//                                        mAdapter.addAll(mData);
//
//                                    }
//
//                                }
//
//
//                            }
//
//                        } else {
//                            Toast.makeText(context,
//                                    apiResponse.getResultMessage(), Toast.LENGTH_SHORT).show();
//                        }
//
//                    }
//                });


    }

    private void cancelRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    @OnClick(R.id.img_back)
    public void onClick() {
        onBackPressed();
    }

    @Override
    public void onRefresh() {
        mAdapter.clear();
        if (TextUtils.isEmpty(date)) {
            getAttendanceList(new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime()));

        } else {
            getAttendanceList(date);
        }


    }

    class AbnormalAdapter extends RecyclerView.Adapter<AbnormalAdapter.ProjectViewHolder> {
        private Context context;
        private List<UserAttendanceStatisticData.ExceptionlistBean> abnormalList;

        public AbnormalAdapter(Context context, List<UserAttendanceStatisticData.ExceptionlistBean>
                projectList) {
            this.context = context;
            this.abnormalList = projectList;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_new_attendance_abnormal_detail, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (abnormalList != null && !abnormalList.isEmpty()) {
                UserAttendanceStatisticData.ExceptionlistBean data = abnormalList.get(position);


                if (data != null) {
                    holder.attendanceTimeScope.setText(data.getDay());
                    holder.attendanceAbnormalCause.setText(data.getType());
                    if (data.getType().equals("旷工")) {
                        holder.rlAddress.setVisibility(View.GONE);
                        holder.rlSignTime.setVisibility(View.GONE);


                    } else {
                        if (!TextUtils.isEmpty(data.getAddress())) {
                            holder.attendanceAbnormalAddress.setText(data.getAddress());

                        } else {
                            holder.attendanceAbnormalAddress.setText("无");

                        }
                        if (data.getType().equals("迟到")) {

                            holder.tvSignTime.setText("签到时间");

                        } else {
                            holder.tvSignTime.setText("签退时间");

                        }

                        if (!TextUtils.isEmpty(data.getTime())) {
                            holder.signTime.setText(data.getTime());
                        } else {
                            holder.signTime.setText("无");

                        }

                    }


                }

            }

        }

        @Override
        public int getItemCount() {
            return abnormalList.size();
        }

        public void addAll(List<UserAttendanceStatisticData.ExceptionlistBean> projectList) {
            this.abnormalList.addAll(projectList);
            AbnormalAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.abnormalList.clear();
            AbnormalAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder {
            private TextView attendanceTimeScope;
            private TextView attendanceAbnormalCause;
            private RelativeLayout rlAddress;
            private TextView attendanceAbnormalAddress;
            private RelativeLayout rlSignTime;
            private TextView tvSignTime;
            private TextView signTime;


            public ProjectViewHolder(View itemView) {

                super(itemView);
                attendanceTimeScope = ButterKnife.findById(itemView, R.id.item_attendance_time);
                attendanceAbnormalCause = ButterKnife.findById(itemView,
                        R.id.item_attendance_abnormal_cause);
                rlAddress = ButterKnife.findById(itemView, R.id.rl_attendance_address);
                attendanceAbnormalAddress = ButterKnife.findById(itemView,
                        R.id.item_attendance_abnormal_address);
                rlSignTime = ButterKnife.findById(itemView,
                        R.id.rl_sign_time);
                tvSignTime = ButterKnife.findById(itemView,
                        R.id.tv_attendance_sign_time);
                signTime = ButterKnife.findById(itemView,
                        R.id.item_attendance_abnormal_sign_time);


            }


        }
    }
}
