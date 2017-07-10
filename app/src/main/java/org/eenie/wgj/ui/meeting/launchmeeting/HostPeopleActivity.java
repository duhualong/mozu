package org.eenie.wgj.ui.meeting.launchmeeting;

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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.meeting.MeetingPeople;
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
 * Created by Eenie on 2017/7/10 at 16:09
 * Email: 472279981@qq.com
 * Des:
 */

public class HostPeopleActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final String TYPE="type";
    @BindView(R.id.swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    private ProjectAdapter mProjectAdapter;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
String mTitle;

    @Override
    protected int getContentView() {
        return R.layout.activity_host;
    }

    @Override
    protected void updateUI() {
        mTitle=getIntent().getStringExtra(TYPE);
        switch (mTitle){
            case "host":
                tvTitle.setText("主持人");


                break;
            case "record":
                tvTitle.setText("记录人");
                break;
        }
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

    @OnClick(R.id.img_back)
    public void onClick() {
        onBackPressed();

    }


    @Override
    public void onRefresh() {
        mProjectAdapter.clear();
        initData();

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


    private void initData() {

        mSubscription = mRemoteService.getMeetingPeopleList(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        cancelRefresh();

                        if (apiResponse.getResultCode() == 200 ||
                                apiResponse.getResultCode() == 0) {
                            if (apiResponse.getData() != null) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                List<MeetingPeople> mData =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<List<MeetingPeople>>() {
                                                }.getType());
                                if (mData!=null){
                                    if (mProjectAdapter!=null){
                                        mProjectAdapter.clear();
                                    }
                                    mProjectAdapter.addAll(mData);
                                }


                            }
                        }
                    }
                });

    }

    class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
        private Context context;
        private List<MeetingPeople> projectList;

        public ProjectAdapter(Context context, List<MeetingPeople> projectList) {
            this.context = context;
            this.projectList = projectList;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_behavior, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (projectList != null && !projectList.isEmpty()) {
                MeetingPeople data = projectList.get(position);
                holder.setItem(data);
                if (data != null) {
                    holder.itemText.setText(data.getName());

                }
//设置显示内容

            }

        }

        @Override
        public int getItemCount() {
            return projectList.size();
        }

        public void addAll(List<MeetingPeople> projectList) {
            this.projectList.addAll(projectList);
            ProjectAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.projectList.clear();
            ProjectAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView itemText;
            private MeetingPeople mMeetingPeople;
            private LinearLayout mLinearLayout;


            public ProjectViewHolder(View itemView) {

                super(itemView);
                itemText = ButterKnife.findById(itemView, R.id.text_item);
                mLinearLayout = ButterKnife.findById(itemView, R.id.line_item);
                mLinearLayout.setOnClickListener(this);

            }

            public void setItem(MeetingPeople projectList) {
                mMeetingPeople = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.line_item:
                        Intent mIntent = new Intent();
                        mIntent.putExtra("mData", mMeetingPeople);
                        // 设置结果，并进行传送
                        setResult(RESULT_OK, mIntent);
                        finish();
                        break;

                }


            }
        }
    }
}