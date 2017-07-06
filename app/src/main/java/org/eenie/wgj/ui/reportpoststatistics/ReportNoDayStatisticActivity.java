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
import org.eenie.wgj.model.response.reportpost.NoReportMonthStatisticResponse;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/6 at 10:36
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportNoDayStatisticActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener {
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


    @Override
    protected int getContentView() {
        return R.layout.activity_project_setting;
    }

    @Override
    protected void updateUI() {
        date = getIntent().getStringExtra(DATE);
        projectId = getIntent().getStringExtra(PROJECT_ID);
        tvNewBuild.setVisibility(View.GONE);
        tvTitle.setText("未报岗");
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

    @OnClick({R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;

        }
    }

    @Override
    public void onRefresh() {
        mAdapter.clear();
        mSubscription = mRemoteService.getReportMonthNoStatisticsList(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""), projectId, date)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        cancelRefresh();
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            if (apiResponse.getData() != null) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<NoReportMonthStatisticResponse> mData = gson.fromJson(jsonArray,
                                        new TypeToken<ArrayList<NoReportMonthStatisticResponse>>() {
                                        }.getType());

                                if (mData != null && !mData.isEmpty()) {
                                    if (mAdapter != null) {
                                        mAdapter.clear();
                                    }
                                    mAdapter.addAll(mData);
                                }

                            }
                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
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

    class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
        private Context context;
        private ArrayList<NoReportMonthStatisticResponse> mNoReportMonthStatisticResponses;

        public ProjectAdapter(Context context, ArrayList<NoReportMonthStatisticResponse>
                mNoReportMonthStatisticResponses) {
            this.context = context;
            this.mNoReportMonthStatisticResponses = mNoReportMonthStatisticResponses;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_no_month_report_post_statistic, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (mNoReportMonthStatisticResponses != null &&
                    !mNoReportMonthStatisticResponses.isEmpty()) {
                NoReportMonthStatisticResponse data = mNoReportMonthStatisticResponses.get(position);
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
            return mNoReportMonthStatisticResponses.size();
        }

        public void addAll(ArrayList<NoReportMonthStatisticResponse> projectList) {
            this.mNoReportMonthStatisticResponses.addAll(projectList);
            ProjectAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mNoReportMonthStatisticResponses.clear();
            ProjectAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView itemDate;
            private NoReportMonthStatisticResponse mMonthStatisticResponse;
            private RelativeLayout mRelativeLayout;


            public ProjectViewHolder(View itemView) {

                super(itemView);
                itemDate = ButterKnife.findById(itemView, R.id.item_date);
                mRelativeLayout = ButterKnife.findById(itemView, R.id.rl_item);

                mRelativeLayout.setOnClickListener(this);

            }

            public void setItem(NoReportMonthStatisticResponse projectList) {
                mMonthStatisticResponse = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_item:

                        startActivity(new Intent(context,
                                ReportPostNoDayStatisticActivity.class).putExtra(
                                ReportPostNoDayStatisticActivity.INFO, mMonthStatisticResponse));

                        break;
                }


            }
        }
    }
}
