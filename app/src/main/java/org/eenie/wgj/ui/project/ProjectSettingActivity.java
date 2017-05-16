package org.eenie.wgj.ui.project;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.ProjectList;
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
 * Created by Eenie on 2017/5/16 at 16:07
 * Email: 472279981@qq.com
 * Des:
 */

public class ProjectSettingActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.notice_swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ProjectAdapter mProjectAdapter;
    @BindView(R.id.recycler_project)
    RecyclerView mRecyclerView;


    @Override
    protected int getContentView() {
        return R.layout.activity_project_setting;
    }

    @Override
    protected void updateUI() {
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

    @OnClick({R.id.img_back, R.id.tv_new_rebuild})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_new_rebuild:

                break;
        }
    }

    @Override
    public void onRefresh() {
        mProjectAdapter.clear();
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        mSubscription = mRemoteService.getProjectList(token)
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
                        if (apiResponse.getResultCode() == 200) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            List<ProjectList> projectLists = gson.fromJson(jsonArray,
                                    new TypeToken<List<ProjectList>>() {
                                    }.getType());
                            cancelRefresh();
                            if (projectLists != null && !projectLists.isEmpty()) {
                                if (mProjectAdapter != null) {
                                    mProjectAdapter.addAll(projectLists);
                                }
                            }
                        }else {
                            Snackbar.make(rootView,apiResponse.getResultMessage(),
                                    Snackbar.LENGTH_SHORT).show();
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
        private List<ProjectList> projectList;

        public ProjectAdapter(Context context, List<ProjectList> projectList) {
            this.context = context;
            this.projectList = projectList;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_project, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (projectList != null && !projectList.isEmpty()) {
                ProjectList data = projectList.get(position);
                if (data != null) {
                    holder.projectName.setText(data.getProjectname());
                    // holder.imgProject.setImageURI(data.get);
                }
//设置显示内容

            }

        }

        @Override
        public int getItemCount() {
            return projectList.size();
        }

        public void addAll(List<ProjectList> projectList) {
            this.projectList.addAll(projectList);
            ProjectAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.projectList.clear();
            ProjectAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView imgProject;
            private TextView projectName;
            private ImageView projectEdit;
            private ProjectList mProjectList;


            public ProjectViewHolder(View itemView) {

                super(itemView);
                imgProject = ButterKnife.findById(itemView, R.id.item_project_img);
                projectName = ButterKnife.findById(itemView, R.id.item_project_name);
                projectEdit = ButterKnife.findById(itemView, R.id.item_project_edit);
                projectEdit.setOnClickListener(this);

            }

            public void setItem(ProjectList projectList) {
                mProjectList = projectList;
            }

            @Override
            public void onClick(View v) {


            }
        }
    }

}
