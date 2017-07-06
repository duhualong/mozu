package org.eenie.wgj.ui.reportpoststatistics;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.response.reportpost.ReportActualPostResponse;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/7/6 at 14:14
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportActualPointItemActivity extends BaseActivity {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    public static final String INFO = "info";
    private ReportActualPostResponse mData;
    String date;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private ProjectAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_actual_report_post_item;
    }

    @Override
    protected void updateUI() {
        mAdapter = new ProjectAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mData = getIntent().getParcelableExtra(INFO);
        if (mData != null) {
            date = mData.getDate();
            if (!TextUtils.isEmpty(date)) {
                tvTitle.setText((date.replaceFirst("-", "年"))
                        .replaceFirst("-", "月") + "日");

                if (mData.getPosts() != null && !mData.getPosts().isEmpty()) {
                    if (mAdapter != null) {
                        mAdapter.clear();
                    }
                    mAdapter.addAll(mData.getPosts());


                }
            }


        }


    }


    @OnClick(R.id.img_back)
    public void onClick() {
        onBackPressed();


    }

    class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
        private Context context;
        private ArrayList<ReportActualPostResponse.PostsBean> mNoReportMonthStatisticResponses;

        public ProjectAdapter(Context context, ArrayList<ReportActualPostResponse.PostsBean>
                mNoReportMonthStatisticResponses) {
            this.context = context;
            this.mNoReportMonthStatisticResponses = mNoReportMonthStatisticResponses;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_actual_month_report_post_statistic, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (mNoReportMonthStatisticResponses != null &&
                    !mNoReportMonthStatisticResponses.isEmpty()) {
                ReportActualPostResponse.PostsBean data = mNoReportMonthStatisticResponses.get(position);
                holder.setItem(data);
                if (data != null) {
                    if (!TextUtils.isEmpty(data.getCompletetime())) {
                        holder.itemTime.setText(data.getCompletetime());
                    } else {
                        holder.itemTime.setText("无");

                    }
                    if (data.getUser_name() != null && !data.getUser_name().isEmpty()) {
                        holder.itemName.setText(data.getUser_name());
                    } else {
                        holder.itemName.setText("无");
                    }
                }
            }

        }

        @Override
        public int getItemCount() {
            return mNoReportMonthStatisticResponses.size();
        }

        public void addAll(ArrayList<ReportActualPostResponse.PostsBean> projectList) {
            this.mNoReportMonthStatisticResponses.addAll(projectList);
            ProjectAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mNoReportMonthStatisticResponses.clear();
            ProjectAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView itemTime;
            private TextView itemName;
            private RelativeLayout mRelativeLayout;
            ReportActualPostResponse.PostsBean mPostsBean;


            public ProjectViewHolder(View itemView) {

                super(itemView);
                itemTime = ButterKnife.findById(itemView, R.id.item_time);
                itemName = ButterKnife.findById(itemView, R.id.item_name);
                mRelativeLayout = ButterKnife.findById(itemView, R.id.rl_item);
                mRelativeLayout.setOnClickListener(this);


            }

            public void setItem(ReportActualPostResponse.PostsBean projectList) {
                mPostsBean = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_item:
                        if (mPostsBean.getUser_name()!=null&&!mPostsBean.getUser_name().isEmpty()
                                &&mPostsBean.getLocation_name()!=null&&
                                !mPostsBean.getLocation_name().isEmpty()){
                            startActivity(new Intent(context,ReportPostStatusMapViewActivity.class)
                                    .putExtra(ReportPostStatusMapViewActivity.INFO,mPostsBean));
                        }else {
                            Toast.makeText(context,"该岗位没有上报位置信息！",Toast.LENGTH_SHORT).show();
                        }



                        break;
                }


            }
        }
    }


}
