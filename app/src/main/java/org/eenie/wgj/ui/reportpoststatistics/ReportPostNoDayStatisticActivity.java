package org.eenie.wgj.ui.reportpoststatistics;

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
import android.widget.TextView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.response.reportpost.NoReportMonthStatisticResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/7/6 at 11:13
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportPostNoDayStatisticActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener {
    public static final String INFO = "info";
    private NoReportMonthStatisticResponse mData;
    @BindView(R.id.notice_swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_project)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_new_rebuild)
    TextView tvNewBuild;
    String date;
    private ProjectAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_project_setting;
    }

    @Override
    protected void updateUI() {
        mData = getIntent().getParcelableExtra(INFO);
        date = mData.getDate();


        tvNewBuild.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(date)) {
            tvTitle.setText((date.replaceFirst("-", "年"))
                    .replaceFirst("-", "月") + "日");

        } else {
            tvTitle.setText("无");
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

    private void cancelRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRefresh() {
        if (mAdapter != null) {
            mAdapter.clear();
        }
        cancelRefresh();
        mAdapter.addAll(mData.getInfo());

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
    protected void onResume() {
        super.onResume();
        onRefresh();
    }


    class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
        private Context context;
        private ArrayList<NoReportMonthStatisticResponse.InfoBean> mNoReportMonthStatisticResponses;

        public ProjectAdapter(Context context, ArrayList<NoReportMonthStatisticResponse.InfoBean>
                mNoReportMonthStatisticResponses) {
            this.context = context;
            this.mNoReportMonthStatisticResponses = mNoReportMonthStatisticResponses;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_report_post_day_no, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (mNoReportMonthStatisticResponses != null &&
                    !mNoReportMonthStatisticResponses.isEmpty()) {
                NoReportMonthStatisticResponse.InfoBean data = mNoReportMonthStatisticResponses.get(position);
                if (data != null) {
                    if (!TextUtils.isEmpty(data.getTime())) {
                        holder.itemTime.setText(data.getTime());
                    } else {
                        holder.itemTime.setText("无");

                    }
                    if (!TextUtils.isEmpty(data.getPost())) {
                        holder.itemPostName.setText(data.getPost());

                    } else {
                        holder.itemPostName.setText("无");

                    }
                }
//设置显示内容

            }

        }

        @Override
        public int getItemCount() {
            return mNoReportMonthStatisticResponses.size();
        }

        public void addAll(ArrayList<NoReportMonthStatisticResponse.InfoBean> projectList) {
            this.mNoReportMonthStatisticResponses.addAll(projectList);
            ProjectAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mNoReportMonthStatisticResponses.clear();
            ProjectAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder {

            private TextView itemTime;
            private TextView itemPostName;


            public ProjectViewHolder(View itemView) {

                super(itemView);
                itemTime = ButterKnife.findById(itemView, R.id.item_time);
                itemPostName = ButterKnife.findById(itemView, R.id.item_post_name);


            }


        }
    }
}

