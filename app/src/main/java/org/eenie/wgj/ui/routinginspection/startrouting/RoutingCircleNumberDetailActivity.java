package org.eenie.wgj.ui.routinginspection.startrouting;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.UserId;
import org.eenie.wgj.model.response.UserInforById;
import org.eenie.wgj.model.response.routing.StartRoutingResponse;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.eenie.wgj.R.id.rl_new_circle_detail;

/**
 * Created by Eenie on 2017/6/26 at 8:57
 * Email: 472279981@qq.com
 * Des:
 */

public class RoutingCircleNumberDetailActivity extends BaseActivity
        implements SwipeRefreshLayout.OnRefreshListener {
    public static final String POSITION = "position";
    public static final String ROUTING_ID = "id";
    public static final String LINE_ID = "line_id";
    private String position;
    private int routingId;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.img_avatar)
    CircleImageView imgAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_post)
    TextView tvPost;
    @BindView(R.id.swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private RoutingCircleDetailAdapter mAdapter;
    private String lineId;
    private   String projectId;


    @Override
    protected int getContentView() {
        return R.layout.activity_routing_start_detail_item;
    }

    @Override
    protected void updateUI() {
        position = getIntent().getStringExtra(POSITION);
        routingId = Integer.valueOf(getIntent().getStringExtra(ROUTING_ID));
        lineId = getIntent().getStringExtra(LINE_ID);
        if (!TextUtils.isEmpty(position)) {
            int mPosition = Integer.valueOf(position) + 1;
            if (mPosition <= 9) {
                tvTitle.setText("圈数0" + mPosition);
            } else {
                tvTitle.setText("圈数" + mPosition);
            }

        }
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mAdapter = new RoutingCircleDetailAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        getUserInfo();

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
                                    for (int i=0;i<data.size();i++){
                                        if (data.get(i).getId()==routingId){
                                            mAdapter.addAll(data.get(i).getInfo());
                                        }
                                    }

                                }
                            }
                        } else {
                            Toast.makeText(context,
                                    apiResponse.getResultMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

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
                            String username = mData.getName();
                            String avatarUrl = mData.getAvatar();
                             projectId=String.valueOf(mData.getProject_id());
                            if (!TextUtils.isEmpty(avatarUrl)) {
                                Glide.with(context).load(Constant.DOMIN + avatarUrl).
                                        centerCrop().into(imgAvatar);
                            }
                            tvPost.setText("测试数据");
                            tvName.setText(username + "/");

                        }
                    }
                });

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
        getRoutingData(lineId);
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


    class RoutingCircleDetailAdapter extends RecyclerView.Adapter<RoutingCircleDetailAdapter.ProjectViewHolder> {
        private Context context;
        private List<StartRoutingResponse.InfoBean> mReportRoutingReponses;

        public RoutingCircleDetailAdapter(Context context, List<StartRoutingResponse.InfoBean>
                mReportRoutingReponses) {
            this.context = context;
            this.mReportRoutingReponses = mReportRoutingReponses;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_circle_detail_routing, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (mReportRoutingReponses != null && !mReportRoutingReponses.isEmpty()) {
                StartRoutingResponse.InfoBean data = mReportRoutingReponses.get(position);

                if (data != null) {
                    String type="progress";
                    if (mReportRoutingReponses.size()==1){
                        type="one_point";

                    }else if (mReportRoutingReponses.size()>=2){
                        if (position==0){
                            type="first";
                        }else if (position==(mReportRoutingReponses.size()-1)){
                            type="last";
                        }
                    }

                    holder.setItem(data, position,type);

                    int mPosition=position+1;
                    if (mPosition<=9){
                        holder.itemPosition.setText("0"+mPosition);
                    }else {
                        holder.itemPosition.setText(mPosition);
                    }

                    if (data.getImage()!=null){
                        if (data.getImage().size()>=1){
                            Glide.with(context).load(Constant.DOMIN+data.getImage().
                                    get(0).getImage()).centerCrop()
                                    .into(holder.imgOldItem);
                        }

                    }
                    if (data.getInspectiontime()!=null&&!TextUtils.isEmpty(data.getInspectiontime())){
                        holder.itemOldTime.setText("时间："+data.getInspectiontime());
                    }
                    if (data.getInspectionname()!=null&&!TextUtils.isEmpty(data.getInspectionname())){
                        holder.itemOldAddress.setText("地点："+data.getInspectionname());
                    }
                    if (data.getInspection()==null||data.getInspection().getTime()==null){
                        holder.imgNothing.setVisibility(View.VISIBLE);
                        holder.imgNothing.setClickable(true);


                    }else {
                        holder.imgNothing.setVisibility(View.GONE);
                        if (data.getInspection().getTime()!=null&&
                                !TextUtils.isEmpty(data.getInspection().getTime())){
                            holder.itemNewTime.setText("时间："+data.getInspection().getTime());
                        }
                        holder.itemNewAddress.setText("地址："+data.getInspectionname());
                        if (data.getInspection().getImage()!=null){
                            if (data.getInspection().getImage().size()>=1){
                                Glide.with(context).load(Constant.DOMIN+data.getInspection().
                                        getImage().get(0).getImage()).centerCrop().
                                        into(holder.imgNewItem);
                            }
                        }
                    }

                }
            }

        }

        @Override
        public int getItemCount() {
            return mReportRoutingReponses.size();
        }

        public void addAll(List<StartRoutingResponse.InfoBean> projectList) {
            this.mReportRoutingReponses.addAll(projectList);
            RoutingCircleDetailAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mReportRoutingReponses.clear();
            RoutingCircleDetailAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private RelativeLayout rlOldDetail;
            private ImageView imgOldItem;
            private TextView itemOldTime;
            private TextView itemOldAddress;
            private TextView itemPosition;
            private RelativeLayout rlNewDetail;
            private ImageView imgNewItem;
            private ImageView imgNothing;
            private TextView itemNewTime;
            private TextView itemNewAddress;
            private StartRoutingResponse.InfoBean mRoutingReponse;
            private int positions;
            private String mPosition;
            private String mType;


            public ProjectViewHolder(View itemView) {
                super(itemView);
                rlOldDetail= ButterKnife.findById(itemView,R.id.rl_old_circle_detail);
                imgOldItem=ButterKnife.findById(itemView,R.id.item_img_old);
                itemOldTime=ButterKnife.findById(itemView,R.id.item_old_time);
                itemOldAddress=ButterKnife.findById(itemView,R.id.item_old_address);
                itemPosition=ButterKnife.findById(itemView,R.id.tv_position);
                rlNewDetail=ButterKnife.findById(itemView, rl_new_circle_detail);
                imgNewItem=ButterKnife.findById(itemView,R.id.item_img_new);
                itemNewTime=ButterKnife.findById(itemView,R.id.item_new_time);
                itemNewAddress=ButterKnife.findById(itemView,R.id.item_new_address);
                imgNothing=ButterKnife.findById(itemView,R.id.img_nothing);
                imgNothing.setOnClickListener(this);
                rlOldDetail.setOnClickListener(this);
                rlNewDetail.setOnClickListener(this);


            }

            public void setItem(StartRoutingResponse.InfoBean projectList, int position,String type) {
                mRoutingReponse = projectList;
                positions = position;
                mType=type;
            }

            @Override
            public void onClick(View v) {
                positions=positions+1;
                if (positions<=9){
                    mPosition="0"+positions;

                }else {
                    mPosition=positions+"";
                }
                switch (v.getId()) {
                    case R.id.rl_old_circle_detail:
                        startActivity(new Intent(context,RoutingPointDetailActivity.class)
                        .putExtra(RoutingPointDetailActivity.INFO,mRoutingReponse)
                        .putExtra(RoutingPointDetailActivity.POSITION,mPosition)
                        .putExtra(RoutingPointDetailActivity.TYPE,"1"));


                        break;
                    case  rl_new_circle_detail:
                        Log.d("test", "onClick: "+new Gson().toJson(mRoutingReponse));

                        startActivity(new Intent(context,RoutingPointDetailActivity.class)
                                .putExtra(RoutingPointDetailActivity.INFO,mRoutingReponse)
                                .putExtra(RoutingPointDetailActivity.POSITION,mPosition)
                                .putExtra(RoutingPointDetailActivity.TYPE,"2"));

                        break;
                    case R.id.img_nothing:
                        startActivity(new Intent(context,RoutingPointUploadActivity.class)
                        .putExtra(RoutingPointUploadActivity.INFO,mRoutingReponse)
                        .putExtra(RoutingPointUploadActivity.LINE_ID,lineId)
                        .putExtra(RoutingPointUploadActivity.TYPE,mType)
                        .putExtra(RoutingPointUploadActivity.PROJECT_ID,projectId)
                        .putExtra(RoutingPointUploadActivity.INSPECTIONDAY_ID,String.valueOf(routingId)));

                        break;

                }

            }
        }
    }
}
