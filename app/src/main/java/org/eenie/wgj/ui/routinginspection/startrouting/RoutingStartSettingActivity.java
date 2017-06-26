package org.eenie.wgj.ui.routinginspection.startrouting;

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
import org.eenie.wgj.model.response.routing.StartRoutingResponse;
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
 * Created by Eenie on 2017/6/24 at 16:24
 * Email: 472279981@qq.com
 * Des:
 */

public class RoutingStartSettingActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String LINE_ID = "id";
    @BindView(R.id.swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private String lineId;
    private RoutingAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_routing_start;
    }

    @Override
    protected void updateUI() {
        lineId = getIntent().getStringExtra(LINE_ID);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mAdapter = new RoutingAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);


    }

    private void getRoutingData(String lineId) {
        mSubscription = mRemoteService.getRoutingByLine(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), lineId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse>() {
                    @Override
                    public void onCompleted() {
                        cancelRefresh();
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
                            List<StartRoutingResponse> data =
                                    gson.fromJson(jsonArray,
                                            new TypeToken<List<StartRoutingResponse>>() {
                                            }.getType());
                            if (data != null) {

                                if (mAdapter != null) {
                                    mAdapter.addAll(data);
                                }
                            }
                        } else {
                            Toast.makeText(context,
                                    apiResponse.getResultMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    @OnClick(R.id.img_back)
    public void onClick() {
        onBackPressed();
    }

    @Override
    public void onRefresh() {
        if (!TextUtils.isEmpty(lineId)) {
            mAdapter.clear();

            getRoutingData(lineId);
        }

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



    class RoutingAdapter extends RecyclerView.Adapter<RoutingAdapter.ProjectViewHolder> {
        private Context context;
        private List<StartRoutingResponse> mReportRoutingReponses;

        public RoutingAdapter(Context context, List<StartRoutingResponse> mReportRoutingReponses) {
            this.context = context;
            this.mReportRoutingReponses = mReportRoutingReponses;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_routing_detail, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (mReportRoutingReponses != null && !mReportRoutingReponses.isEmpty()) {
                StartRoutingResponse data = mReportRoutingReponses.get(position);
                holder.setItem(data, position);
                if (data != null) {
                    int mPosition = position + 1;
                    if (mPosition <= 9) {
                        holder.itemCircleNumber.setText("圈数0" + mPosition);
                    } else {
                        holder.itemCircleNumber.setText("圈数" + mPosition);
                    }
                    if (data.getInfo() != null) {
                        holder.itemPointNumber.setText("共" + data.getInfo().size() + "个巡检点位");
                    }


                }
            }

        }

        @Override
        public int getItemCount() {
            return mReportRoutingReponses.size();
        }

        public void addAll(List<StartRoutingResponse> projectList) {
            this.mReportRoutingReponses.addAll(projectList);
            RoutingAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mReportRoutingReponses.clear();
            RoutingAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private StartRoutingResponse mRoutingReponse;
            private RelativeLayout mRelativeLayout;
            private TextView itemCircleNumber;
            private TextView itemPointNumber;
            private int position;


            public ProjectViewHolder(View itemView) {
                super(itemView);
                itemCircleNumber = ButterKnife.findById(itemView, R.id.item_number_circle);
                itemPointNumber = ButterKnife.findById(itemView, R.id.item_point_number);
                mRelativeLayout = ButterKnife.findById(itemView, R.id.rl_item);
                mRelativeLayout.setOnClickListener(this);
            }

            public void setItem(StartRoutingResponse projectList, int position) {
                mRoutingReponse = projectList;
                position = position;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_item:
                        startActivity(new Intent(context, RoutingCircleNumberDetailActivity.class)
                                .putExtra(RoutingCircleNumberDetailActivity.POSITION, String.valueOf(position))
                        .putExtra(RoutingCircleNumberDetailActivity.ROUTING_ID,
                                String.valueOf(mRoutingReponse.getId()))
                        .putExtra(RoutingCircleNumberDetailActivity.LINE_ID,lineId));

                        break;
                }

            }
        }
    }



}
