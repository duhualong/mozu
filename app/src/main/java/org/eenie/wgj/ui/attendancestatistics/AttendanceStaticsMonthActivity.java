package org.eenie.wgj.ui.attendancestatistics;

import android.content.Context;
import android.content.Intent;
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
import org.eenie.wgj.model.response.newattendancestatistic.ProjectAttendanceStatisticMonth;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/19 at 13:30
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceStaticsMonthActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    @BindView(R.id.swipe_refresh_list)SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_month)RecyclerView mRecyclerView;
    public static final String PROJECT_ID="id";
    private String projectId;
    private ProjectAdapter mProjectAdapter;

    @Override
    protected int getContentView() {
        return  R.layout.activity_month_attendance_statics;
    }

    @Override
    protected void updateUI() {
        if (!TextUtils.isEmpty(getIntent().getStringExtra(PROJECT_ID))){
            projectId=getIntent().getStringExtra(PROJECT_ID);
        }
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mProjectAdapter = new ProjectAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(mProjectAdapter);


    }
    private void cancelRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @OnClick({R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;

        }
    }
    @Override
    public void onResume() {
        super.onResume();
        onRefresh();
    }

    private void getProjectMonthAttendance(String projectId) {
        mSubscription=mRemoteService.getAttendanceMonthByProjectId(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN,""),projectId)
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
                        if (apiResponse.getCode()==0){
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                           ProjectAttendanceStatisticMonth data = gson.fromJson(jsonArray,
                                    new TypeToken<ProjectAttendanceStatisticMonth>() {
                                    }.getType());
                            if (data!=null){
                                if (mProjectAdapter != null) {
                                    mProjectAdapter.addAll(data.getMonth_list());
                                }
                            }

                        }else {
                            Toast.makeText(context,apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });

    }

    @Override
    public void onRefresh() {
        mProjectAdapter.clear();
        getProjectMonthAttendance(projectId);


    }

    class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
        private Context context;
        private List<String> projectMonth;

        public ProjectAdapter(Context context, List<String> projectMonth) {
            this.context = context;
            this.projectMonth = projectMonth;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_attendance_month, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (projectMonth != null && !projectMonth.isEmpty()) {
                String data = projectMonth.get(position);
                holder.setItem(data);
                if (data != null) {
                    holder.itemDate.setText(data.replaceAll("-","年")+"月");

                }
//设置显示内容

            }

        }

        @Override
        public int getItemCount() {
            return projectMonth.size();
        }

        public void addAll(List<String> projectMonth) {
            this.projectMonth.addAll(projectMonth);
            ProjectAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.projectMonth.clear();
            ProjectAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView itemDate;
            private RelativeLayout rlMonth;
            private String mString;

            public ProjectViewHolder(View itemView) {
                super(itemView);
                itemDate = ButterKnife.findById(itemView, R.id.item_date);
                rlMonth = ButterKnife.findById(itemView, R.id.rl_attendance_statics_month);
                rlMonth.setOnClickListener(this);


            }

            public void setItem(String projectList) {
                mString = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_attendance_statics_month:
                        startActivity(new Intent(AttendanceStaticsMonthActivity.this,
                                AttendanceStaticsMonthDetailActivity.class)
                        .putExtra(AttendanceStaticsMonthDetailActivity.DATE,mString)
                        .putExtra(AttendanceStaticsMonthDetailActivity.PROJECT_ID,projectId));
                        break;
                }

            }
        }
    }

}
