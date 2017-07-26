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
import org.eenie.wgj.model.response.attendancestatistic.NewAddPeopleResponse;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/23 at 9:49
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceLeaveOutActivity extends BaseActivity implements
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
        tvTitle.setText("离职人员");
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
        mSubscription = mRemoteService.getLeaveOutPeopleList(mPrefsHelper.
                getPrefs().getString(Constants.TOKEN, ""), projectId,date)
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
                        if (apiResponse.getCode()== 0) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            ArrayList<NewAddPeopleResponse> data = gson.fromJson(jsonArray,
                                    new TypeToken<ArrayList<NewAddPeopleResponse>>() {
                                    }.getType());
                            if (data != null) {
                                if (mAdapter != null) {
                                    mAdapter.addAll(data);
                                }
                            }
                        } else {
                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();
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
        private ArrayList<NewAddPeopleResponse> projectMonth;

        public ProjectAdapter(Context context, ArrayList<NewAddPeopleResponse> projectMonth) {
            this.context = context;
            this.projectMonth = projectMonth;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_attendance_leave_out, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (projectMonth != null && !projectMonth.isEmpty()) {
                NewAddPeopleResponse data = projectMonth.get(position);
                if (data != null) {
                    if (!data.getName().isEmpty() && data.getName() != null) {
                        holder.itemTitle.setText("离职人员  "+data.getName());
                    } else {
                        holder.itemTitle.setText("离职人员  " + "无");
                    }
                    holder.itemDate.setText("离职时间  "+data.getLeave_at());
                    holder.itemPost.setText("离职岗位  "+data.getTypename());
                }
            }
        }

        @Override
        public int getItemCount() {
            return projectMonth.size();
        }

        public void addAll(ArrayList<NewAddPeopleResponse> projectMonth) {
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
            private TextView itemPost;



            public ProjectViewHolder(View itemView) {
                super(itemView);
                itemTitle = ButterKnife.findById(itemView, R.id.item_title);
                itemDate = ButterKnife.findById(itemView, R.id.item_date);
                itemPost = ButterKnife.findById(itemView, R.id.item_post);



            }


        }
    }
}
