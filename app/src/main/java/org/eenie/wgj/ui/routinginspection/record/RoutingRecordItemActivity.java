package org.eenie.wgj.ui.routinginspection.record;

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

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.UserId;
import org.eenie.wgj.model.response.RecordRoutingResponse;
import org.eenie.wgj.model.response.UserInforById;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/29 at 12:22
 * Email: 472279981@qq.com
 * Des:
 */

public class RoutingRecordItemActivity extends BaseActivity {
    @BindView(R.id.recycler_view_item)
    RecyclerView mRecyclerView;
    public static final String ROUTING_INFO = "info";
    public static final String PROJECT_ID = "project_id";
    public static final String USER_ID = "user_id";
    @BindView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    private RecordRoutingResponse data;
    private RoutingAdapter mAdapter;
    private String mDate;
    private String userName;
    private String avatarUrl;
    private String projectId;
    private String userId;

    @Override
    protected int getContentView() {
        return R.layout.activity_item_routing_record;
    }

    @Override
    protected void updateUI() {

        data = getIntent().getParcelableExtra(ROUTING_INFO);
        mDate = data.getDate();

        if (data != null) {
            if (data.getUser() != null) {
                userName = data.getUser().getName();
                avatarUrl = data.getUser().getId_card_head_image();
                tvName.setText(data.getUser().getName());
                if (data.getUser().getId_card_head_image() != null) {
                    Glide.with(context).load(Constant.DOMIN + data.getUser().getId_card_head_image()).
                            centerCrop().into(imgAvatar);
                }
                if (data.getInfo() != null) {
                    mAdapter = new RoutingAdapter(context, data.getInfo());
                    LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                    mRecyclerView.setLayoutManager(layoutManager);
                    mRecyclerView.setAdapter(mAdapter);
                }
            }
            projectId = getIntent().getStringExtra(PROJECT_ID);
            userId = getIntent().getStringExtra(USER_ID);
            if (TextUtils.isEmpty(userId)) {
                userId = mPrefsHelper.getPrefs().getString(Constants.UID, "");
            }
            if (TextUtils.isEmpty(projectId)) {
                getUserInfo();
            }
        }


    }

    private void getUserInfo() {

        UserId mUser = new UserId(mPrefsHelper.getPrefs().getString(Constants.UID, ""));
        mSubscription = mRemoteService.getUserInfoById(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), mUser)
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
                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(apiResponse.getData());
                        UserInforById mData = gson.fromJson(jsonArray,
                                new TypeToken<UserInforById>() {
                                }.getType());
                        if (mData != null) {
                            if (TextUtils.isEmpty(userName)) {
                                userName = mData.getName();
                            }
                            if (TextUtils.isEmpty(avatarUrl)) {
                                avatarUrl = mData.getAvatar();
                            }
                            projectId = String.valueOf(mData.getProject_id());

                        }
                    }
                });

    }

    @OnClick(R.id.img_back)
    public void onClick() {
        onBackPressed();
    }


    class RoutingAdapter extends RecyclerView.Adapter<RoutingAdapter.ProjectViewHolder> {
        private Context context;
        private List<RecordRoutingResponse.InfoBean> mInfoBeanList;

        public RoutingAdapter(Context context, List<RecordRoutingResponse.InfoBean> mInfoBeanList) {
            this.context = context;
            this.mInfoBeanList = mInfoBeanList;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_routing_detail, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (mInfoBeanList != null && !mInfoBeanList.isEmpty()) {
                RecordRoutingResponse.InfoBean data = mInfoBeanList.get(position);
                holder.setItem(data, position);
                if (data != null) {
                    int mPosition = position + 1;
                    if (mPosition <= 9) {
                        holder.itemCircleNumber.setText("圈数0" + mPosition);
                    } else {
                        holder.itemCircleNumber.setText("圈数" + mPosition);
                    }
                    if (data.getInspection() != null) {
                        holder.itemPointNumber.setText("共" + data.getInspection().size() + "个巡检点位");
                    }


                }
            }

        }

        @Override
        public int getItemCount() {
            return mInfoBeanList.size();
        }

        public void addAll(List<RecordRoutingResponse.InfoBean> projectList) {
            this.mInfoBeanList.addAll(projectList);
            RoutingAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mInfoBeanList.clear();
            RoutingAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private RecordRoutingResponse.InfoBean mRoutingReponse;
            private RelativeLayout mRelativeLayout;
            private TextView itemCircleNumber;
            private TextView itemPointNumber;
            private int mPosition;


            public ProjectViewHolder(View itemView) {
                super(itemView);
                itemCircleNumber = ButterKnife.findById(itemView, R.id.item_number_circle);
                itemPointNumber = ButterKnife.findById(itemView, R.id.item_point_number);
                mRelativeLayout = ButterKnife.findById(itemView, R.id.rl_item);
                mRelativeLayout.setOnClickListener(this);
            }

            public void setItem(RecordRoutingResponse.InfoBean projectList, int position) {
                mRoutingReponse = projectList;
                mPosition = position;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_item:
                        startActivity(new Intent(RoutingRecordItemActivity.this, RoutingRecordItemDetailActivity.class)
                                .putExtra(RoutingRecordItemDetailActivity.INFO_ROUTING, mRoutingReponse)
                                .putExtra(RoutingRecordItemDetailActivity.DATE, mDate)
                                .putExtra(RoutingRecordItemDetailActivity.USERNAME, userName)
                                .putExtra(RoutingRecordItemDetailActivity.AVATAR_URL, avatarUrl)
                                .putExtra(RoutingRecordItemDetailActivity.PROJECT_ID, projectId)
                                .putExtra(RoutingRecordItemDetailActivity.USER_ID, userId));
                        break;
                }

            }
        }
    }

}
