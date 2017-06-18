package org.eenie.wgj.ui.project.projectpersonal;

import android.content.Context;
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
 * Created by Eenie on 2017/6/16 at 18:05
 * Email: 472279981@qq.com
 * Des:
 */

public class AddProjectPersonalActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String PROJECT_ID = "id";
    public static final String COMPANY_ID = "company";
    @BindView(R.id.swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_add_personal)
    RecyclerView mRecyclerView;
    private String projectId;
    private String companyId;
    private String token;
    private ProjectPersonalAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_add_personal_project;
    }

    @Override
    protected void updateUI() {
        token=mPrefsHelper.getPrefs().getString(Constants.TOKEN,"");
        projectId = getIntent().getStringExtra(PROJECT_ID);
        companyId = getIntent().getStringExtra(COMPANY_ID);
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

        mSubscription = mRemoteService.getCompanyPersonalListAll(token,companyId,projectId)
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
                            cancelRefresh();
                            if (apiResponse.getCode()==0){
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<CompanyPersonalList> data = gson.fromJson(jsonArray,
                                        new TypeToken<ArrayList<CompanyPersonalList>>() {
                                        }.getType());
                                if (data != null && !data.isEmpty()) {

                                    if (mAdapter != null) {
                                        ArrayList<CompanyPersonalList> mData=new ArrayList<>();
                                        for (int i=0;i<data.size();i++){
                                            if (data.get(i).getProject_id()==0){
                                                mData.add(data.get(i));
                                            }
                                        }
                                        mAdapter.addAll(mData);
                                    }
                                }

                            }



                        }else {
                            Toast.makeText(AddProjectPersonalActivity.this,apiResponse.getMessage(),
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
            View itemView = inflater.inflate(R.layout.item_project_personal_add, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (companyPersonalList != null && !companyPersonalList.isEmpty()) {
                CompanyPersonalList data = companyPersonalList.get(position);
                holder.setItem(data);
                if (data != null) {
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
            private RelativeLayout rlAddPersonal;
            private TextView itemName;

            private CompanyPersonalList mCompanyList;


            public ProjectViewHolder(View itemView) {
                super(itemView);
                rlAddPersonal = ButterKnife.findById(itemView, R.id.rl_add_personal);
                itemName = ButterKnife.findById(itemView, R.id.item_name);
                rlAddPersonal.setOnClickListener(this);

            }

            public void setItem(CompanyPersonalList projectList) {
                mCompanyList = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_add_personal:
                        addPersonalItem(mCompanyList);

                        break;

                }


            }
        }
    }

    private void addPersonalItem(CompanyPersonalList companyList) {

        CompanyPersonalRequest request = new CompanyPersonalRequest(companyId, projectId,
                String.valueOf(companyList.getUserid()));
        mSubscription = mRemoteService.addProjectPersonal(token, request)
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
                            Toast.makeText(AddProjectPersonalActivity.this,"添加成功",Toast.LENGTH_SHORT).show();
                            finish();

                        }else {
                            Toast.makeText(AddProjectPersonalActivity.this,apiResponse.getMessage(),Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


}
