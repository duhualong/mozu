package org.eenie.wgj.ui.attendance;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

    @BindView(R.id.swipe_refresh_list)SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_attendance_abnormal)RecyclerView mRecyclerView;
    private AbnormalAdapter mAdapter;
    @Override
    protected int getContentView() {
        return R.layout.activity_attendance_abnormal;
    }

    @Override
    protected void updateUI() {
       // getIntent().getStringExtra()

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

        mSubscription = mRemoteService.getAttendanceDayOfMonth(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                date,mPrefsHelper.getPrefs().getString(Constants.UID,""))
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
                                                    mData=new ArrayList<>();
                                            for (int i=0;i<data.size();i++){
                                                if (data.get(i).getStateCode()==1){
                                                    mData.add(data.get(i));
                                                }
                                            }
                                            Log.d("test", "data: "+gson.toJson(mData));

                                                mAdapter.addAll(mData);

                                        }

                                }


                            }

                        }else {
                            Toast.makeText(context,
                                    apiResponse.getResultMessage(),Toast.LENGTH_SHORT).show();
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

    @OnClick(R.id.img_back)public void onClick(){
        onBackPressed();
    }

    @Override
    public void onRefresh() {
        mAdapter.clear();
        getAttendanceList(new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime()));


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
                    holder.attendanceTimeScope.setText(data.getDate()+" "+
                            data.getService().getStarttime()+"-"+data.getService().getEndtime());
                    if (data.getCheckin().getLate()==0&&data.getSignback().getLate()==0){
                        holder.attendanceStartCause.setText(data.getStateDes());
                        holder.attendanceStartAddress.setText("无");
                        holder.attendanceEndCause.setVisibility(View.GONE);
                        holder.attendanceEndCauseContent.setVisibility(View.GONE);
                        holder.attendanceEndAddress.setVisibility(View.GONE);
                        holder.attendanceEndAddressContent.setVisibility(View.GONE);

                    }else if (data.getCheckin().getLate()==2&&data.getSignback().getLate()==2){
                        holder.attendanceStartCause.setText(data.getCheckin().getStatus());
                        if (data.getCheckin().getAddress()!=null){
                            holder.attendanceStartAddress.setText(data.getCheckin().getAddress());
                        }else {
                            holder.attendanceStartAddress.setText("无");

                        }

                        holder.attendanceEndCause.setVisibility(View.VISIBLE);
                        holder.attendanceEndCauseContent.setVisibility(View.VISIBLE);
                        holder.attendanceEndCauseContent.setText(data.getSignback().getStatus());
                        holder.attendanceEndAddress.setVisibility(View.VISIBLE);
                        holder.attendanceEndAddressContent.setVisibility(View.VISIBLE);
                        if (data.getSignback().getAddress()!=null){
                            holder.attendanceEndAddressContent.setText(data.getSignback().getAddress());
                        }else {
                            holder.attendanceEndAddressContent.setText("无");

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


            public ProjectViewHolder(View itemView) {

                super(itemView);
                attendanceTimeScope = ButterKnife.findById(itemView, R.id.attendance_time_scope);
                attendanceStartCause = ButterKnife.findById(itemView, R.id.abnormal_cause_content);
                attendanceStartAddress = ButterKnife.findById(itemView, R.id.attendance_address);
                attendanceEndCause=ButterKnife.findById(itemView,R.id.attendance_end_cause);
                attendanceEndCauseContent= ButterKnife.findById(itemView,
                        R.id.attendance_end_cause_content);
                attendanceEndAddress=ButterKnife.findById(itemView,R.id.attendance_end_address);
                attendanceEndAddressContent=ButterKnife.findById(itemView,
                        R.id.attendance_end_address_content);

            }


        }
    }
}
