package org.eenie.wgj.ui.attendancestatistics;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import org.eenie.wgj.model.response.AttendanceLeaveResponse;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/23 at 9:26
 * Email: 472279981@qq.com
 * 请假
 * Des:
 */

public class AttendanceLeaveActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener {
    public static final String PROJECT_ID = "id";
    public static final String DATE = "date";
    private ProjectAdapter mAdapter;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private String projectId;
    private String date;

    @Override
    protected int getContentView() {
        return R.layout.activity_attendance_cause;
    }


    @Override
    protected void updateUI() {
        tvTitle.setText("请假详情");
        projectId = getIntent().getStringExtra(PROJECT_ID);
        date = getIntent().getStringExtra(DATE);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mAdapter = new ProjectAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(mAdapter);


    }

    @OnClick(R.id.img_back)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
        }

    }

    private void getData(String projectId, String date) {
        mSubscription = mRemoteService.getLeaveInformation(mPrefsHelper.
                getPrefs().getString(Constants.TOKEN, ""), date, projectId)
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
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            ArrayList<AttendanceLeaveResponse> data = gson.fromJson(jsonArray,
                                    new TypeToken<ArrayList<AttendanceLeaveResponse>>() {
                                    }.getType());
                            if (data != null) {
                                if (mAdapter != null) {
                                    mAdapter.addAll(data);
                                }
                            }
                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(), Toast.LENGTH_SHORT).show();
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
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    @Override
    public void onRefresh() {
        mAdapter.clear();
        getData(projectId, date);
    }

    class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
        private Context context;
        private ArrayList<AttendanceLeaveResponse> projectMonth;

        public ProjectAdapter(Context context, ArrayList<AttendanceLeaveResponse> projectMonth) {
            this.context = context;
            this.projectMonth = projectMonth;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_attendance_status, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (projectMonth != null && !projectMonth.isEmpty()) {
                AttendanceLeaveResponse data = projectMonth.get(position);
                if (data != null) {
                    holder.itemTitle.setText("请假人员  " + data.getName());
                    holder.itemDate.setText("请假时间  " + data.getCreated_at());
                    holder.itemCause.setText("请假时长  " + "未知");

                }


            }

        }

        @Override
        public int getItemCount() {
            return projectMonth.size();
        }

        public void addAll(ArrayList<AttendanceLeaveResponse> projectMonth) {
            this.projectMonth.addAll(projectMonth);
            ProjectAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.projectMonth.clear();
            ProjectAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder {

            private TextView itemTitle;
            private TextView itemDate;
            private TextView itemCause;
            private TextView itemPost;


            public ProjectViewHolder(View itemView) {
                super(itemView);
                itemTitle = ButterKnife.findById(itemView, R.id.item_title);
                itemDate = ButterKnife.findById(itemView, R.id.item_date);
                itemCause = ButterKnife.findById(itemView, R.id.item_cause);
                itemPost = ButterKnife.findById(itemView, R.id.item_post);


            }


        }
    }
}
