package org.eenie.wgj.ui.attendancestatistics;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.SortTeamAttendance;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/20 at 14:00
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceSortTeamMonthActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    public static final String DATE = "date";
    public static final String PROJECT_ID = "id";
    @BindView(R.id.swipe_refresh_list)SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_sort)RecyclerView mRecyclerView;
    private String date;
    private String projectId;
    private ProjectAdapter mAdapter;
    @BindView(R.id.tv_title)TextView tvTitle;
    @Override
    protected int getContentView() {
        return R.layout.activity_sort_team_detail;
    }

    @Override
    protected void updateUI() {
        tvTitle.setText("月度考勤排名");
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
    @OnClick({R.id.img_back})public void onClick(View view){
        switch (view.getId()){
            case R.id.img_back:
                onBackPressed();
                break;
        }
    }



    private void getData(String projectId, String date) {
        mSubscription = mRemoteService.getMonthTeamSort(mPrefsHelper.
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
                            ArrayList<SortTeamAttendance> data = gson.fromJson(jsonArray,
                                    new TypeToken<ArrayList<SortTeamAttendance>>() {
                                    }.getType());
                            if (data != null) {
                                if (mAdapter != null) {
                                    mAdapter.addAll(data);
                                }
                            }
                        }else {
                            Toast.makeText(context,apiResponse.getResultMessage(),Toast.LENGTH_SHORT).show();
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
        private ArrayList<SortTeamAttendance> projectMonth;

        public ProjectAdapter(Context context, ArrayList<SortTeamAttendance> projectMonth) {
            this.context = context;
            this.projectMonth = projectMonth;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_sort_team, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (projectMonth != null && !projectMonth.isEmpty()) {

                SortTeamAttendance data = projectMonth.get(position);
                if (data!=null) {
                    holder.setItem(data);

                    if (position <= 2) {
                        holder.itemNumber.setVisibility(View.INVISIBLE);
                        holder.imgSort.setVisibility(View.VISIBLE);
                        switch (position) {
                            case 0:
                                holder.imgSort.setImageResource(R.mipmap.ic_gold);

                                break;
                            case 1:
                                holder.imgSort.setImageResource(R.mipmap.ic_silver);
                                break;
                            case 2:
                                holder.imgSort.setImageResource(R.mipmap.ic_copper);

                                break;
                        }
                    } else {
                        holder.itemNumber.setVisibility(View.VISIBLE);
                        holder.imgSort.setVisibility(View.INVISIBLE);
                        holder.itemNumber.setText(String.valueOf((position + 1)));
                    }

                    holder.itemName.setText(data.getName());
                    holder.itemPost.setText(data.getPermissions());
                    holder.attendanceDay.setText("出勤：" + data.getWork_number() + "天");
                    holder.itemDate.setText(date);
                    holder.attendanceFirst.setText("第一名\n" + "共" + data.getRank() + "次");
                    if (data.getService() != null) {
                        if (data.getService().size() == 1) {
                            holder.restDay.setVisibility(View.VISIBLE);
                            holder.restTwo.setVisibility(View.GONE);
                            holder.restThree.setVisibility(View.GONE);
                            holder.restFour.setVisibility(View.GONE);
                            holder.restDay.setText(data.getService().get(0).getServicesname()
                                    + "\n" + data.getService().get(0).getWork_day());

                        } else if (data.getService().size() == 2) {
                            holder.restDay.setVisibility(View.VISIBLE);
                            holder.restTwo.setVisibility(View.VISIBLE);
                            holder.restThree.setVisibility(View.GONE);
                            holder.restFour.setVisibility(View.GONE);
                            holder.restDay.setText(data.getService().get(0).getServicesname()
                                    + "\n" + data.getService().get(0).getWork_day());
                            holder.restTwo.setText(data.getService().get(1).getServicesname()
                                    + "\n" + data.getService().get(1).getWork_day());

                        } else if (data.getService().size() == 3) {
                            holder.restDay.setVisibility(View.VISIBLE);
                            holder.restTwo.setVisibility(View.VISIBLE);
                            holder.restThree.setVisibility(View.VISIBLE);
                            holder.restFour.setVisibility(View.GONE);
                            holder.restDay.setText(data.getService().get(0).getServicesname()
                                    + "\n" + data.getService().get(0).getWork_day());
                            holder.restTwo.setText(data.getService().get(1).getServicesname()
                                    + "\n" + data.getService().get(1).getWork_day());

                            holder.restThree.setText(data.getService().get(2).getServicesname()
                                    + "\n" + data.getService().get(2).getWork_day());
                        } else if (data.getService().size() >= 4) {
                            holder.restDay.setVisibility(View.VISIBLE);
                            holder.restTwo.setVisibility(View.VISIBLE);
                            holder.restThree.setVisibility(View.VISIBLE);
                            holder.restFour.setVisibility(View.VISIBLE);
                            holder.restDay.setText(data.getService().get(0).getServicesname()
                                    + "\n" + data.getService().get(0).getWork_day());
                            holder.restTwo.setText(data.getService().get(1).getServicesname()
                                    + "\n" + data.getService().get(1).getWork_day());

                            holder.restThree.setText(data.getService().get(2).getServicesname()
                                    + "\n" + data.getService().get(2).getWork_day());
                            holder.restFour.setText(data.getService().get(3).getServicesname()
                                    + "\n" + data.getService().get(3).getWork_day());
                        }

                    } else {
                        holder.restDay.setVisibility(View.GONE);
                        holder.restTwo.setVisibility(View.GONE);
                        holder.restThree.setVisibility(View.GONE);
                        holder.restFour.setVisibility(View.GONE);
                    }
                }

//设置显示内容

            }

        }

        @Override
        public int getItemCount() {
            return projectMonth.size();
        }

        public void addAll(ArrayList<SortTeamAttendance> projectMonth) {
            this.projectMonth.addAll(projectMonth);
            ProjectAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.projectMonth.clear();
            ProjectAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

            private TextView itemNumber;
            private ImageView imgSort;
            private TextView itemName;
            private TextView itemPost;
            private TextView attendanceDay;
            private TextView itemDate;
            private TextView attendanceFirst;
            private TextView restDay;
            private TextView restTwo;
            private TextView restThree;
            private TextView restFour;
            private RelativeLayout rlSortTeam;
            private SortTeamAttendance mProjectList;

            public void setItem(SortTeamAttendance projectList) {
                mProjectList = projectList;
            }


            public ProjectViewHolder(View itemView) {
                super(itemView);
                itemNumber = ButterKnife.findById(itemView, R.id.tv_gold_number);
                imgSort = ButterKnife.findById(itemView, R.id.img_gold_number);
                itemName = ButterKnife.findById(itemView, R.id.item_name);
                itemPost = ButterKnife.findById(itemView, R.id.item_post);
                attendanceDay=ButterKnife.findById(itemView,R.id.attendance_day);
                itemDate=ButterKnife.findById(itemView,R.id.item_date);
                attendanceFirst=ButterKnife.findById(itemView,R.id.attendance_first);
                restDay=ButterKnife.findById(itemView,R.id.rest_day);
                restTwo=ButterKnife.findById(itemView,R.id.rest_day_two);
                restThree=ButterKnife.findById(itemView,R.id.rest_day_three);
                restFour=ButterKnife.findById(itemView,R.id.rest_day_four);
                rlSortTeam=ButterKnife.findById(itemView,R.id.rl_sort_team);
                rlSortTeam.setOnClickListener(this);



            }


            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.rl_sort_team:
                        startActivity(new Intent(AttendanceSortTeamMonthActivity.this,
                                AttendanceRecorderMonthActivity.class)
                        .putExtra(AttendanceRecorderMonthActivity.DATE,date)
                        .putExtra(AttendanceRecorderMonthActivity.POST,mProjectList.getPermissions())
                        .putExtra(AttendanceRecorderMonthActivity.USER_NAME,mProjectList.getName())
                        .putExtra(AttendanceRecorderMonthActivity.UID,mProjectList.getId()+""));


                        break;
                }
            }
        }
    }

}
