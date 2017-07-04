package org.eenie.wgj.ui.reportpost;

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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.reportpost.ReportPostListResponse;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/3 at 16:41
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportPostSettingUploadActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipe_refresh_list)SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)RecyclerView mRecyclerView;
    private ProjectAdapter mAdapter;
    @Override
    protected int getContentView() {
        return R.layout.activity_report_post_setting;
    }

    @Override
    protected void updateUI() {
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
    @OnClick(R.id.img_back)public void onClick(){
        onBackPressed();
    }

    @Override
    public void onRefresh() {
        mAdapter.clear();
        mSubscription=mRemoteService.getReportLine(mPrefsHelper.getPrefs().getString(Constants.TOKEN,""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        cancelRefresh();
                        if (apiResponse.getResultCode()==200){

                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            List<ReportPostListResponse> data =
                                    gson.fromJson(jsonArray,
                                            new TypeToken<List<ReportPostListResponse>>() {
                                            }.getType());
                            if (data!=null){
                                if (mAdapter!=null){
                                    mAdapter.clear();
                                }
                                mAdapter.addAll(data);
                            }

                        }else {
                            Toast.makeText(context,apiResponse.getResultMessage(),
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
        private List<ReportPostListResponse> projectList;

        public ProjectAdapter(Context context, List<ReportPostListResponse> projectList) {
            this.context = context;
            this.projectList = projectList;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_report_post_detail, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (projectList != null && !projectList.isEmpty()) {
                ReportPostListResponse data = projectList.get(position);
                holder.setItem(data);
                if (data != null) {
                    holder.postName.setText(data.getPost());

                }
//设置显示内容

            }

        }

        @Override
        public int getItemCount() {
            return projectList.size();
        }

        public void addAll(List<ReportPostListResponse> projectList) {
            this.projectList.addAll(projectList);
            ProjectAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.projectList.clear();
            ProjectAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private RelativeLayout mRelativeLayout;
            private TextView postName;

            private ReportPostListResponse mProjectList;

            public ProjectViewHolder(View itemView) {

                super(itemView);

                mRelativeLayout = ButterKnife.findById(itemView, R.id.rl_select_post);
                postName = ButterKnife.findById(itemView, R.id.item_post_name);
                mRelativeLayout.setOnClickListener(this);

            }

            public void setItem(ReportPostListResponse projectList) {
                mProjectList = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.rl_select_post:
                      startActivity(new Intent(context, ReportPostSettingFirstActivity.class)
                      .putExtra(ReportPostSettingFirstActivity.POST_ID,
                              String.valueOf(mProjectList.getId()))
                      .putExtra(ReportPostSettingFirstActivity.NAME,mProjectList.getPost()));

                        break;

                }


            }
        }
    }



}
