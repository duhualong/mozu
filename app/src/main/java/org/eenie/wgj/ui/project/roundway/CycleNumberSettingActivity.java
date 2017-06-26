package org.eenie.wgj.ui.project.roundway;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.CycleNumber;
import org.eenie.wgj.model.response.RoundWayList;
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
 * Created by Eenie on 2017/5/26 at 11:06
 * Email: 472279981@qq.com
 * Des:
 */

public class CycleNumberSettingActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener {
    public static final String PROJECT_ID = "id";
    public static final String INFO = "info";

    @BindView(R.id.swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_project_contacts)
    RecyclerView mRecyclerView;
    private String projectId;
    private RoundWayAdapter mAdapter;
    @BindView(R.id.ly_add_keyman)
    LinearLayout lyNoPersonal;
    @BindView(R.id.lv_data_contacts)
    LinearLayout lyNoData;
    @BindView(R.id.img_add_contacts)
    ImageView imgContacts;
    private int mLineId;
    private RoundWayList data;
    @Override
    protected int getContentView() {
        return R.layout.activity_cycle_number;
    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);
        data=getIntent().getParcelableExtra(INFO);
        if (data!=null){
            mLineId=data.getId();
        }


        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mAdapter = new RoundWayAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(mAdapter);


    }

    @OnClick({R.id.img_back, R.id.img_add_contacts, R.id.ly_add_keyman})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:

                onBackPressed();
                break;

            case R.id.img_add_contacts:
                Intent intent = new Intent(context, AddCycleRoundActivity.class);
                intent.putExtra(AddCycleRoundActivity.PROJECT_ID, projectId);
                intent.putExtra(AddCycleRoundActivity.LINE_ID,mLineId+"");
                startActivity(intent);


                break;
            case R.id.ly_add_keyman:
                Intent intents = new Intent(context, AddCycleRoundActivity.class);
                intents.putExtra(AddCycleRoundActivity.PROJECT_ID, projectId);
                intents.putExtra(AddCycleRoundActivity.LINE_ID,mLineId+"");
                startActivity(intents);
                break;


        }

    }

    @Override
    public void onRefresh() {
        mAdapter.clear();
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        mSubscription = mRemoteService.getCycleNumber(token, projectId, mLineId)
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
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            if (apiResponse.getData() != null) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                List<CycleNumber> postWorkLists = gson.fromJson(jsonArray,
                                        new TypeToken<List<CycleNumber>>() {
                                        }.getType());
                                if (postWorkLists.size()>0) {
                                    for (int i = 0; i < postWorkLists.size(); i++) {

                                        if (postWorkLists.get(i).getInfo() == null ||
                                                postWorkLists.get(i).getInfo().isEmpty()) {
                                            postWorkLists.remove(i);

                                        }
                                    }

                                }
                                    if (mAdapter != null) {
                                        lyNoPersonal.setVisibility(View.GONE);
                                        imgContacts.setVisibility(View.VISIBLE);
                                        lyNoData.setVisibility(View.VISIBLE);
                                        mAdapter.addAll(postWorkLists);
                                    } else {
                                    lyNoPersonal.setVisibility(View.VISIBLE);
                                    imgContacts.setVisibility(View.GONE);
                                    lyNoData.setVisibility(View.GONE);
                                }
                            } else {
                                lyNoPersonal.setVisibility(View.VISIBLE);
                                imgContacts.setVisibility(View.GONE);
                                lyNoData.setVisibility(View.GONE);
                            }


                        } else {
                            lyNoPersonal.setVisibility(View.VISIBLE);
                            imgContacts.setVisibility(View.GONE);
                            lyNoData.setVisibility(View.GONE);
//                            Snackbar.make(rootView, apiResponse.getResultMessage(),
//                                    Snackbar.LENGTH_SHORT).show();
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


    class RoundWayAdapter extends RecyclerView.Adapter<RoundWayAdapter.RoundWayViewHolder> {
        private Context context;
        private List<CycleNumber> contactsList;

        public RoundWayAdapter(Context context, List<CycleNumber> contactsList) {
            this.context = context;
            this.contactsList = contactsList;
        }

        @Override
        public RoundWayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_cycle, parent, false);
            return new RoundWayViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RoundWayViewHolder holder, int position) {
            if (contactsList != null && !contactsList.isEmpty()) {
                CycleNumber data = contactsList.get(position);
                holder.setItem(data);

                if (data != null) {

                    position = position + 1;
                    if (position < 10) {
                        holder.reportPost.setText("圈数0" + position);
                    } else {
                        holder.reportPost.setText("圈数" + position);
                    }


                }
                if (data.getInfo() != null && !data.getInfo().isEmpty()) {
                    if (data.getInfo().size() > 0) {
                        holder.itemPoint.setText("共" + data.getInfo().size() + "个巡检点");
                    } else {
                        holder.itemPoint.setText("共0个巡检点");
                    }

                }
            }

        }

        @Override
        public int getItemCount() {
            return contactsList.size();
        }

        public void addAll(List<CycleNumber> contactsList) {
            this.contactsList.addAll(contactsList);
            RoundWayAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.contactsList.clear();
            RoundWayAdapter.this.notifyDataSetChanged();
        }

        class RoundWayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView reportPost;
            private RelativeLayout rlReport;
            private TextView itemPoint;

            private CycleNumber mCycleNumber;

            public RoundWayViewHolder(View itemView) {

                super(itemView);

                reportPost = ButterKnife.findById(itemView, R.id.item_post);

                rlReport = ButterKnife.findById(itemView, R.id.rl_key_personal);
                itemPoint = ButterKnife.findById(itemView, R.id.tv_round_point);
                rlReport.setOnClickListener(this);


            }

            public void setItem(CycleNumber data) {
                mCycleNumber = data;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.rl_key_personal:
                        Intent intent=new Intent(CycleNumberSettingActivity.this,
                                CycleNumberDetailActivity.class);
                        intent.putExtra(CycleNumberDetailActivity.INFO,mCycleNumber);
                        intent.putExtra(CycleNumberDetailActivity.PROJECT_ID,projectId);
                        intent.putExtra(CycleNumberDetailActivity.LINE_ID,String.valueOf(mLineId));
                        startActivity(intent);


                        break;


                }


            }
        }
    }
}
