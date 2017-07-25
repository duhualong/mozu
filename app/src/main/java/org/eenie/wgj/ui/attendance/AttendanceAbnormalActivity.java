package org.eenie.wgj.ui.attendance;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.AttendanceAbnormal;
import org.eenie.wgj.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
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
    private String date;
    private String uid;

    @Override
    protected int getContentView() {
        return R.layout.activity_attendance_abnormal;
    }

    @Override
    protected void updateUI() {
        date = getIntent().getStringExtra(DATE);
        uid = getIntent().getStringExtra(UID);

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
        if (TextUtils.isEmpty(uid)) {
            uid = mPrefsHelper.getPrefs().getString(Constants.UID, "");
        }

        mSubscription = mRemoteService.getAttendanceDayOfMonth(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                date, uid)
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
                        cancelRefresh();
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {

                            if (apiResponse.getData() != null) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<AttendanceAbnormal> data =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<ArrayList<AttendanceAbnormal>>() {
                                                }.getType());
                                if (data != null) {

                                    if (mAdapter != null) {
                                        ArrayList<AttendanceAbnormal>
                                                mData = new ArrayList<>();
                                        for (int i = 0; i < data.size(); i++) {
                                            if (data.get(i).getStateCode() == 1) {
                                                mData.add(data.get(i));
                                            }
                                        }
                                        Log.d("test", "data: " + gson.toJson(mData));

                                        mAdapter.addAll(mData);

                                    }

                                }


                            }

                        } else {
                            Toast.makeText(context,
                                    apiResponse.getResultMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


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
        private ArrayList<AttendanceAbnormal> abnormalList;

        public AbnormalAdapter(Context context, ArrayList<AttendanceAbnormal>
                projectList) {
            this.context = context;
            this.abnormalList = projectList;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_attendance_abnormal, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (abnormalList != null && !abnormalList.isEmpty()) {
                AttendanceAbnormal data = abnormalList.get(position);


                if (data != null) {
                    if (data.getService().getStarttime() != null) {
                        holder.attendanceTimeScope.setText(data.getDate() + " " +
                                data.getService().getStarttime() + "-" + data.getService().getEndtime());
                    } else {
                        holder.attendanceTimeScope.setText(data.getDate());
                    }

                    if (data.getStateDes().equals("旷工")) {
                        holder.attendanceCheckInStatus.setVisibility(View.VISIBLE);
                        holder.attendanceStartCause.setVisibility(View.VISIBLE);
                        holder.attendanceCheckinAddress.setVisibility(View.VISIBLE);
                        holder.attendanceStartAddress.setVisibility(View.VISIBLE);
                        holder.attendanceCheckinAddress.setText("考勤地点");
                        holder.attendanceCheckInStatus.setText("异常原因");
                        holder.attendanceStartCause.setText(data.getStateDes());
                        holder.attendanceStartAddress.setText("无");

                        holder.attendanceEndCause.setVisibility(View.GONE);
                        holder.attendanceEndCauseContent.setVisibility(View.GONE);
                        holder.attendanceEndAddress.setVisibility(View.GONE);
                        holder.attendanceEndAddressContent.setVisibility(View.GONE);
                        holder.signBackTime.setVisibility(View.GONE);
                        holder.tvSignBackTime.setVisibility(View.GONE);
                        holder.tvSigninTime.setVisibility(View.GONE);
                        holder.signInTime.setVisibility(View.GONE);

                    } else {
                        if (data.getCheckin() != null) {
//                            if (data.getCheckin().getStatus().equals("正常")){
//                                holder.attendanceCheckInStatus.setVisibility(View.GONE);
//                                holder.attendanceStartCause.setVisibility(View.GONE);
//                                holder.attendanceCheckinAddress.setVisibility(View.GONE);
//                                holder.attendanceStartAddress.setVisibility(View.GONE);
//
//                            }else {
                            holder.attendanceCheckInStatus.setVisibility(View.VISIBLE);
                            holder.attendanceStartCause.setVisibility(View.VISIBLE);
                            holder.attendanceCheckinAddress.setVisibility(View.VISIBLE);
                            holder.attendanceStartAddress.setVisibility(View.VISIBLE);
                            holder.tvSigninTime.setVisibility(View.VISIBLE);
                            holder.signInTime.setVisibility(View.VISIBLE);
                            if (TextUtils.isEmpty(data.getCheckin().getComplete_time())){

                                if (data.getCheckin().getStatus().equals("正常")){
                                    holder.signInTime.setText("正常考勤");
                                }else {
                                    holder.signInTime.setText("无");
                                }

                            }else {
                                holder.signInTime.setText(data.getCheckin().getComplete_time());

                            }

                            if (TextUtils.isEmpty(data.getCheckin().getDescription())){
                                holder.attendanceStartCause.setText(data.getCheckin().getStatus());
                            }else {
                                holder.attendanceStartCause.setText(data.getCheckin().getStatus()+"(外出)");

                            }
                            if (!TextUtils.isEmpty(data.getCheckin().getAddress())) {
                                holder.attendanceStartAddress.setText(data.getCheckin().getAddress());
                            }
//                            }
                        } else {
                            holder.attendanceCheckInStatus.setVisibility(View.GONE);
                            holder.attendanceStartCause.setVisibility(View.GONE);
                            holder.attendanceCheckinAddress.setVisibility(View.GONE);
                            holder.attendanceStartAddress.setVisibility(View.GONE);
                            holder.tvSigninTime.setVisibility(View.GONE);
                            holder.signInTime.setVisibility(View.GONE);

                        }

                        if (data.getSignback() != null) {
//                            if (!data.getSignback().getStatus().equals("正常")){
//                                holder.attendanceEndCause.setVisibility(View.GONE);
//                                holder.attendanceEndCauseContent.setVisibility(View.GONE);
//                                holder.attendanceEndAddress.setVisibility(View.GONE);
//                                holder.attendanceEndAddressContent.setVisibility(View.GONE);
//                            }else {
                            holder.attendanceEndCause.setVisibility(View.VISIBLE);
                            holder.attendanceEndCauseContent.setVisibility(View.VISIBLE);
                            holder.attendanceEndAddress.setVisibility(View.VISIBLE);
                            holder.attendanceEndAddressContent.setVisibility(View.VISIBLE);
                            if (TextUtils.isEmpty(data.getSignback().getDescription())){
                                holder.attendanceEndCauseContent.setText(data.getSignback().getStatus());
                            }else {
                                holder.attendanceEndCauseContent.setText(data.getCheckin().getStatus()+"(外出)");

                            }

                            holder.tvSignBackTime.setVisibility(View.VISIBLE);
                            holder.signBackTime.setVisibility(View.VISIBLE);
                            if (TextUtils.isEmpty(data.getSignback().getComplete_time())){
                                if (data.getSignback().getStatus().equals("正常")){
                                    holder.signBackTime.setText("正常考勤");
                                }else {
                                    holder.signBackTime.setText("无");
                                }

                            }else {
                                holder.signBackTime.setText(data.getSignback().getComplete_time());

                            }


                            if (!TextUtils.isEmpty(data.getSignback().getAddress())) {
                                holder.attendanceEndAddressContent.setText(data.getSignback().getAddress());
                            }
                            // }

                        } else {
                            holder.attendanceEndCause.setVisibility(View.GONE);
                            holder.attendanceEndCauseContent.setVisibility(View.GONE);
                            holder.attendanceEndAddress.setVisibility(View.GONE);
                            holder.attendanceEndAddressContent.setVisibility(View.GONE);
                        }


                    }


                }

            }

        }

        @Override
        public int getItemCount() {
            return abnormalList.size();
        }

        public void addAll(ArrayList<AttendanceAbnormal> projectList) {
            this.abnormalList.addAll(projectList);
            AbnormalAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.abnormalList.clear();
            AbnormalAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder {
            private TextView attendanceTimeScope;
            private TextView attendanceStartCause;
            private TextView attendanceStartAddress;
            private TextView attendanceEndCause;
            private TextView attendanceEndCauseContent;
            private TextView attendanceEndAddress;
            private TextView attendanceEndAddressContent;
            private TextView attendanceCheckInStatus;
            private TextView attendanceCheckinAddress;

            private TextView tvSigninTime;
            private TextView signInTime;
            private TextView tvSignBackTime;
            private TextView signBackTime;


            public ProjectViewHolder(View itemView) {

                super(itemView);
                attendanceCheckInStatus = ButterKnife.findById(itemView, R.id.abnormal_cause);
                attendanceTimeScope = ButterKnife.findById(itemView, R.id.attendance_time_scope);

                attendanceStartCause = ButterKnife.findById(itemView, R.id.abnormal_cause_content);

                attendanceCheckinAddress = ButterKnife.findById(itemView, R.id.attendance_start_address);

                attendanceStartAddress = ButterKnife.findById(itemView, R.id.attendance_address);

                attendanceEndCause = ButterKnife.findById(itemView, R.id.attendance_end_cause);
                attendanceEndCauseContent = ButterKnife.findById(itemView,
                        R.id.attendance_end_cause_content);
                attendanceEndAddress = ButterKnife.findById(itemView, R.id.attendance_end_address);
                attendanceEndAddressContent = ButterKnife.findById(itemView,
                        R.id.attendance_end_address_content);
                tvSigninTime=ButterKnife.findById(itemView,
                        R.id.sign_in);
                signInTime=ButterKnife.findById(itemView,
                        R.id.sign_in_time);
                tvSignBackTime=ButterKnife.findById(itemView,
                        R.id.tv_sign_back_time);
                signBackTime=ButterKnife.findById(itemView,
                        R.id.sign_back_time);

            }


        }
    }
}
