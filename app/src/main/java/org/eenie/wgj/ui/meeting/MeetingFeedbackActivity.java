package org.eenie.wgj.ui.meeting;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.meeting.MeetingFeedbackResponseList;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/11 at 14:11
 * Email: 472279981@qq.com
 * Des:
 */

public class MeetingFeedbackActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    @BindView(R.id.swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private FeedbackAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_meeting_feedback;
    }

    @Override
    protected void updateUI() {

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mAdapter = new FeedbackAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(mAdapter);

    }

    @OnClick(R.id.img_back)
    public void onClick() {
        onBackPressed();
    }

    @Override
    public void onRefresh() {
        mAdapter.clear();
        mSubscription = mRemoteService.getMeetingFeedbackList(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        cancelRefresh();
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            Gson gson = new Gson();
                            if (apiResponse.getData() != null) {
                                String jsonArray = gson.toJson(apiResponse.getData());
                                List<MeetingFeedbackResponseList> mData =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<List<MeetingFeedbackResponseList>>() {
                                                }.getType());
                                if (mData != null) {
                                    mAdapter.clear();
                                }
                                mAdapter.addAll(mData);

                            }


                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }

    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }

    private void cancelRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    class FeedbackAdapter extends RecyclerView.Adapter<FeedbackAdapter.ProjectViewHolder> {
        private Context context;
        private List<MeetingFeedbackResponseList> projectList;

        public FeedbackAdapter(Context context, List<MeetingFeedbackResponseList> projectList) {
            this.context = context;
            this.projectList = projectList;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_appl_meeting_feedback, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (projectList != null && !projectList.isEmpty()) {
                MeetingFeedbackResponseList data = projectList.get(position);
                holder.setItem(data);
                if (data != null) {
                    if (data.getId_card_head_image() != null && !data.getId_card_head_image().isEmpty()) {
                        Glide.with(context).load(Constant.DOMIN + data.getId_card_head_image())
                                .centerCrop().into(holder.imgAvatar);
                    }
                    if (!TextUtils.isEmpty(data.getUsername())) {
                        holder.itemName.setText(data.getUsername());

                    } else {
                        holder.itemName.setText("无");

                    }
                    if (!TextUtils.isEmpty(data.getName())) {
                        holder.itemContent.setText(data.getName());
                    } else {
                        holder.itemContent.setText("无");

                    }

                    if (!TextUtils.isEmpty(data.getCreated_at())) {
                        if (data.getCreated_at().length() >= 10) {
                            holder.itemDate.setText((
                                    data.getCreated_at().substring(0, 10).
                                            replaceFirst("-", "年")).replaceFirst("-", "月") + "日");
                        } else {
                            holder.itemDate.setText(data.getCreated_at());
                        }


                    } else {
                        holder.itemDate.setText(data.getCreated_at());
                    }

                    switch (data.getCheckstatus()) {
                        case 1:
                            holder.itemResult.setTextColor(ContextCompat.getColor
                                    (context, R.color.text_green));


                            break;
                        case 2:

                            holder.itemResult.setTextColor(ContextCompat.getColor
                                    (context, R.color.black_light));
                            break;
                        case 3:
                            holder.itemResult.setTextColor(ContextCompat.getColor
                                    (context, R.color.text_red));

                            break;
                    }
                    holder.itemResult.setText(data.getCheckname());

                }
//设置显示内容

            }

        }

        @Override
        public int getItemCount() {
            return projectList.size();
        }

        public void addAll(List<MeetingFeedbackResponseList> projectList) {
            this.projectList.addAll(projectList);
            FeedbackAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.projectList.clear();
            FeedbackAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private CircleImageView imgAvatar;
            private TextView itemName;
            private TextView itemContent;
            private TextView itemDate;
            private TextView itemResult;
            private MeetingFeedbackResponseList mProjectList;
            private RelativeLayout mRelativeLayout;


            public ProjectViewHolder(View itemView) {

                super(itemView);
                imgAvatar = ButterKnife.findById(itemView, R.id.img_avatar);
                itemName = ButterKnife.findById(itemView, R.id.item_name);
                itemContent = ButterKnife.findById(itemView, R.id.item_content);
                itemDate = ButterKnife.findById(itemView, R.id.item_date);
                itemResult = ButterKnife.findById(itemView, R.id.item_feedback_result);
                mRelativeLayout = ButterKnife.findById(itemView, R.id.rl_feedback);
                mRelativeLayout.setOnClickListener(this);

            }

            public void setItem(MeetingFeedbackResponseList projectList) {
                mProjectList = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_feedback:
                        startActivity(new Intent(context,MeetingFeedbackDetailActivity.class)
                        .putExtra(MeetingFeedbackDetailActivity.APPLY_ID,String.valueOf(mProjectList.getId())));

                        break;

                }


            }
        }
    }
}
