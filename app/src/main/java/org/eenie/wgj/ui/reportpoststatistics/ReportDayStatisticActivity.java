package org.eenie.wgj.ui.reportpoststatistics;


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
import org.eenie.wgj.model.response.reportpost.QueryReportPostMonth;
import org.eenie.wgj.model.response.reportpost.ReportActualPostResponse;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ReportDayStatisticActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener{
    public static final String DATE = "date";
    public static final String PROJECT_ID = "id";
    @BindView(R.id.notice_swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_project)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_new_rebuild)
    TextView tvNewBuild;
    String date;
    String projectId;
    private ProjectAdapter mAdapter;
    private   String startTime="2017-07-01";
    private   String endTime="2017-07-31";

    @Override
    protected int getContentView() {
        return R.layout.activity_project_setting;
    }

    @Override
    protected void updateUI() {
        tvNewBuild.setVisibility(View.GONE);
        tvTitle.setText("实际报岗");
        date=getIntent().getStringExtra(DATE);
        projectId=getIntent().getStringExtra(PROJECT_ID);
        if (!TextUtils.isEmpty(date)) {
            String[] date = this.date.split("-");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(date[0]));
            calendar.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
             startTime = dateFormat.format(calendar.getTime());
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
             endTime = dateFormat.format(calendar.getTime());
        }


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
    public void onClick() {
        onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }
    private void cancelRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
    @Override
    public void onRefresh() {
        mAdapter.clear();
        QueryReportPostMonth request=new QueryReportPostMonth(endTime,projectId,startTime);
        mSubscription=mRemoteService.queryMonthReportPostList(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN,""),request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        cancelRefresh();
                        if (apiResponse.getCode()==0){
                            if (apiResponse.getData()!=null){
                                Gson gson=new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<ReportActualPostResponse> mData = gson.fromJson(jsonArray,
                                        new TypeToken<ArrayList<ReportActualPostResponse>>() {
                                        }.getType());

                                if (mData!=null&&!mData.isEmpty()){
                                    if (mAdapter!=null){
                                        mAdapter.clear();
                                    }
                                    mAdapter.addAll(mData);


                                }else {
                                    Toast.makeText(context,apiResponse.getMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }

                            }else {
                                Toast.makeText(context,apiResponse.getMessage(),
                                        Toast.LENGTH_SHORT).show();
                            }

                        }else {
                            Toast.makeText(context,apiResponse.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });



    }


    class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
        private Context context;
        private ArrayList<ReportActualPostResponse> mReportActualPostResponses;

        public ProjectAdapter(Context context, ArrayList<ReportActualPostResponse>
                mReportActualPostResponses) {
            this.context = context;
            this.mReportActualPostResponses = mReportActualPostResponses;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_no_month_report_post_statistic, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (mReportActualPostResponses != null &&
                    !mReportActualPostResponses.isEmpty()) {
                ReportActualPostResponse data = mReportActualPostResponses.get(position);
                holder.setItem(data);
                if (data != null) {

                    if (!TextUtils.isEmpty(data.getDate())) {
                        holder.itemDate.setText((data.getDate().replaceFirst("-", "年"))
                                .replaceFirst("-", "月") + "日");
                    } else {
                        holder.itemDate.setText("无");
                    }

                }
//设置显示内容

            }

        }

        @Override
        public int getItemCount() {
            return mReportActualPostResponses.size();
        }

        public void addAll(ArrayList<ReportActualPostResponse> projectList) {
            this.mReportActualPostResponses.addAll(projectList);
            ProjectAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mReportActualPostResponses.clear();
            ProjectAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView itemDate;
            private ReportActualPostResponse mMonthStatisticResponse;
            private RelativeLayout mRelativeLayout;


            public ProjectViewHolder(View itemView) {

                super(itemView);
                itemDate = ButterKnife.findById(itemView, R.id.item_date);
                mRelativeLayout = ButterKnife.findById(itemView, R.id.rl_item);

                mRelativeLayout.setOnClickListener(this);

            }

            public void setItem(ReportActualPostResponse projectList) {
                mMonthStatisticResponse = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_item:

                        startActivity(new Intent(context,
                                ReportActualPointItemActivity.class).putExtra(
                                ReportActualPointItemActivity.INFO, mMonthStatisticResponse));

                        break;
                }


            }
        }
    }
}
