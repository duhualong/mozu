package org.eenie.wgj.ui.project.projectpersonal;

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
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.CompanyPersonalRequest;
import org.eenie.wgj.model.response.CompanyPersonalList;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/16 at 17:55
 * Email: 472279981@qq.com
 * Des:
 */

public class ProjectPersonalSetting extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    public static final String PROJECT_ID = "id";
    public static final String COMPANY_ID = "company";
    @BindView(R.id.tv_save)
    TextView tvSave;
    @BindView(R.id.swipe_refresh_list)SwipeRefreshLayout mSwipeRefreshLayout;

    @BindView(R.id.recycler_project_personal)
    RecyclerView mRecyclerView;
    @BindView(R.id.project_personal_total)TextView tvTotalNumber;
    private String projectId;
    private String companyId;
    private String token;
    private boolean isDelete = false;
    private ProjectPersonalAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_project_add_personal_setting;
    }

    @Override
    protected void updateUI() {

        token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        companyId = getIntent().getStringExtra(COMPANY_ID);
        projectId = getIntent().getStringExtra(PROJECT_ID);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mAdapter = new ProjectPersonalAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);

    }



    @OnClick({R.id.rl_add_personal, R.id.img_back, R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.rl_add_personal:
                startActivity(new Intent(context, AddProjectPersonalActivity.class).
                        putExtra(AddProjectPersonalActivity.COMPANY_ID, companyId)
                        .putExtra(AddProjectPersonalActivity.PROJECT_ID, projectId));

                break;
            case R.id.tv_save:
                if (isDelete) {
                    isDelete = false;
                    tvSave.setVisibility(View.VISIBLE);
                    tvSave.setText("编辑");
                } else {
                    isDelete = true;
                    tvSave.setText("保存");
                }
                mAdapter.notifyDataSetChanged();

                break;
        }
    }


    public void onRefresh() {
        mAdapter.clear();
        CompanyPersonalRequest request = new CompanyPersonalRequest(companyId, projectId, "");
        mSubscription = mRemoteService.deleteProjectPersonal(token, request)
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
                        if (apiResponse.getCode() == 0) {
                            if (apiResponse.getData()!=null){
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<CompanyPersonalList> data = gson.fromJson(jsonArray,
                                        new TypeToken<ArrayList<CompanyPersonalList>>() {
                                        }.getType());
                                if (data != null && !data.isEmpty()) {
                                    tvTotalNumber.setText("项目人员("+data.size()+"人)");
                                    if (mAdapter != null) {
                                        mAdapter = new ProjectPersonalAdapter(context, data);
                                        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                                        mRecyclerView.setLayoutManager(layoutManager);
                                        mRecyclerView.addItemDecoration(
                                                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
                                        mRecyclerView.setAdapter(mAdapter);

                                    }else {
                                        tvTotalNumber.setText("项目人员(0人)");
                                    }
                                }
                            }else {
                                tvTotalNumber.setText("项目人员(0人)");

                            }


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


    class ProjectPersonalAdapter extends RecyclerView.Adapter<ProjectPersonalAdapter.
            ProjectViewHolder> {
        private Context context;
        private ArrayList<CompanyPersonalList> companyPersonalList;

        public ProjectPersonalAdapter(Context context, ArrayList<CompanyPersonalList>
                companyPersonalList) {
            this.context = context;
            this.companyPersonalList = companyPersonalList;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_project_personal, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (companyPersonalList != null && !companyPersonalList.isEmpty()) {
                CompanyPersonalList data = companyPersonalList.get(position);
                holder.setItem(data);
                if (data != null) {
                    if (isDelete) {
                        holder.imgDelete.setVisibility(View.VISIBLE);
                    } else {
                        holder.imgDelete.setVisibility(View.GONE);
                    }
                    holder.itemName.setText(data.getName());


                }
//设置显示内容

            }

        }

        @Override
        public int getItemCount() {
            return companyPersonalList.size();
        }

        public void addAll(ArrayList<CompanyPersonalList> companyPersonalList) {
            this.companyPersonalList.addAll(companyPersonalList);
            ProjectPersonalAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.companyPersonalList.clear();
            ProjectPersonalAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private ImageView imgDelete;
            private TextView itemName;

            private CompanyPersonalList mCompanyList;


            public ProjectViewHolder(View itemView) {
                super(itemView);
                imgDelete = ButterKnife.findById(itemView, R.id.img_delete);
                itemName = ButterKnife.findById(itemView, R.id.item_name);
                imgDelete.setOnClickListener(this);

            }

            public void setItem(CompanyPersonalList projectList) {
                mCompanyList = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.img_delete:
                        deletePersonalItem(mCompanyList);
                        notifyDataSetChanged();

                        break;

                }


            }
        }
    }

    private void deletePersonalItem(CompanyPersonalList companyList) {
        mAdapter.clear();
        CompanyPersonalRequest request = new CompanyPersonalRequest(companyId, projectId,
                String.valueOf(companyList.getUserid()));
        mSubscription = mRemoteService.deleteProjectPersonal(token, request)
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
                        if (apiResponse.getCode() == 0) {
                            if (apiResponse.getData()!=null){
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<CompanyPersonalList> data = gson.fromJson(jsonArray,
                                        new TypeToken<ArrayList<CompanyPersonalList>>() {
                                        }.getType());
                                if (data != null && !data.isEmpty()) {
                                    tvTotalNumber.setText("项目人员("+data.size()+"人)");
                                    if (mAdapter != null) {
                                        mAdapter.addAll(data);
                                    }
                                }else {
                                    tvTotalNumber.setText("项目人员(0人)");
                                }
                            }else {
                                tvTotalNumber.setText("项目人员(0人)");
                            }

                        }

                    }
                });
    }
}
