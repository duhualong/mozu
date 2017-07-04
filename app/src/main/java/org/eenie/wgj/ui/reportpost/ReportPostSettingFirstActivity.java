package org.eenie.wgj.ui.reportpost;

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
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.reportpost.ReportPostItemDetail;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/4 at 17:02
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportPostSettingFirstActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener {
    public static final String POST_ID = "id";
    public static final String NAME = "name";
    @BindView(R.id.swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private String postId;
    private ReportPostAdapter mAdapter;
    private ReportPostItemDetail mData;
    private String postName;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @Override
    protected int getContentView() {
        return R.layout.activity_report_post_setting_one;
    }

    @Override
    protected void updateUI() {
        postId = getIntent().getStringExtra(POST_ID);
        postName = getIntent().getStringExtra(NAME);
        if (!TextUtils.isEmpty(postName)) {
            tvTitle.setText(postName);

        }
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mAdapter = new ReportPostAdapter(context, new ArrayList<>());
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
    public void onResume() {
        super.onResume();
        onRefresh();


    }

    @Override
    public void onRefresh() {
        mAdapter.clear();
        mSubscription = mRemoteService.getReportInfoByLine(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""), postId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        cancelRefresh();
                        if (apiResponse.getResultCode() == 200) {
                            Gson gson = new Gson();
                            if (apiResponse.getData() != null) {
                                String jsonArray = gson.toJson(apiResponse.getData());
                                mData = gson.fromJson(jsonArray,
                                        new TypeToken<ReportPostItemDetail>() {
                                        }.getType());
                                if (mData.getPlan() != null && mData.getPlan().size() > 0) {

                                    mAdapter.addAll(mData.getPlan());
                                }
                            }

                        }


                    }
                });

    }

    @OnClick({R.id.img_back, R.id.rl_report_detail_first})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.rl_report_detail_first:
                if (mData != null) {
                    startActivity(new Intent(context,ReportPostDetailItemActivity.class)
                            .putExtra(ReportPostDetailItemActivity.INFO,mData.getData())
                    );


                }
                break;
        }
    }

    class ReportPostAdapter extends RecyclerView.Adapter<ReportPostAdapter.ProjectViewHolder> {
        private Context context;
        private ArrayList<ReportPostItemDetail.planBean> mPlanBeanArrayList;

        public ReportPostAdapter(Context context, ArrayList<ReportPostItemDetail.planBean>
                mPlanBeanArrayList) {
            this.context = context;
            this.mPlanBeanArrayList = mPlanBeanArrayList;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_report_post_item, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (mPlanBeanArrayList != null && !mPlanBeanArrayList.isEmpty()) {
                ReportPostItemDetail.planBean data = mPlanBeanArrayList.get(position);
                int mPosition = position + 1;
                String positions = String.valueOf(mPosition);
                holder.setItem(data,positions);
                if (data != null) {
                    if (data.getTime() != null) {
                        holder.tvReportTime.setText(data.getTime());
                    } else {
                        holder.tvReportTime.setText("无");

                    }

                    if (positions.length() <= 1 && mPosition <= 9) {
                        holder.itemPosition.setText("0" + positions);

                    } else {
                        holder.itemPosition.setText(positions);
                    }
                    switch (data.getStatusCode()) {
                        case 1:
                            //超时
                            holder.itemPosition.setBackgroundResource(R.drawable.circle_red);
                            holder.tvOvertime.setText("报岗超时");
                            holder.imgPhoto.setVisibility(View.GONE);
                            holder.rlUploadPost.setVisibility(View.GONE);
                            break;
                        case 2:
                            //时间未到
                            holder.itemPosition.setBackgroundResource(R.drawable.circle_gray);
                            break;
                        case 3:
                            //已报岗
                            holder.itemPosition.setBackgroundResource(R.drawable.circle_blue);
                            holder.rlUploadPost.setVisibility(View.VISIBLE);
                            holder.imgPhoto.setVisibility(View.GONE);
                            holder.tvOvertime.setVisibility(View.GONE);
                            if (data.getActual() != null) {
                                if (data.getActual().getCompletetime() != null) {
                                    holder.tvFinishTime.setText("完成时间：" + data.getActual().getCompletetime());

                                } else {
                                    holder.tvFinishTime.setText("完成时间：无");
                                }
                                if (data.getActual().getLocation_name() != null) {
                                    holder.tvReportPostAddress.setText("位置：" + data.getActual().getLocation_name());
                                } else {
                                    holder.tvReportPostAddress.setText("位置：无");

                                }
                            }

                            break;
                        case 0:
                            //可报岗
                            holder.itemPosition.setBackgroundResource(R.drawable.circle_blue);
                            break;
                    }

                }
//设置显示内容

            }

        }

        @Override
        public int getItemCount() {
            return mPlanBeanArrayList.size();
        }

        public void addAll(ArrayList<ReportPostItemDetail.planBean> projectList) {
            this.mPlanBeanArrayList.addAll(projectList);
            ReportPostAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mPlanBeanArrayList.clear();
            ReportPostAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private RelativeLayout mRelativeLayout;
            private TextView tvReportTime;
            private TextView itemPosition;
            private ImageView imgPhoto;
            private RelativeLayout rlUploadPost;
            private TextView tvOvertime;
            private TextView tvFinishTime;
            private TextView tvReportPostAddress;

            private ReportPostItemDetail.planBean mPlanBean;
            private String positions;

            public ProjectViewHolder(View itemView) {

                super(itemView);


                itemPosition = ButterKnife.findById(itemView, R.id.item_position);
                tvReportTime = ButterKnife.findById(itemView, R.id.item_report_post_time);
                imgPhoto = ButterKnife.findById(itemView, R.id.img_photo);
                rlUploadPost = ButterKnife.findById(itemView, R.id.el_report_post_one);
                tvOvertime = ButterKnife.findById(itemView, R.id.item_overtime_post);
                tvFinishTime = ButterKnife.findById(itemView, R.id.item_tv_time);
                tvReportPostAddress = ButterKnife.findById(itemView, R.id.item_tv_address);


            }

            public void setItem(ReportPostItemDetail.planBean projectList,String position) {
                mPlanBean = projectList;
                positions=position;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.img_photo:
                        if (mPlanBean.getStatusCode() == 2) {
                            Toast.makeText(ReportPostSettingFirstActivity.this,
                                    "报岗时间未到", Toast.LENGTH_SHORT).show();

                        } else if (mPlanBean.getStatusCode() == 0) {
                            Toast.makeText(ReportPostSettingFirstActivity.this,
                                    "可报岗", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case R.id.el_report_post_one:
                        if (mPlanBean.getStatusCode()==3){
                            startActivity(new Intent(ReportPostSettingFirstActivity.this,
                                    ReportPostPointDetailActivity.class)
                            .putExtra(ReportPostPointDetailActivity.INFO,mPlanBean)
                           .putExtra(ReportPostPointDetailActivity.POSITION,positions)
                            .putExtra(ReportPostPointDetailActivity.POST_NAME,postName));
                        }


                        break;

                }


            }
        }
    }


}
