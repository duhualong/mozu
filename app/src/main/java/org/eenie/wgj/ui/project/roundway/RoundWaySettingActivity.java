package org.eenie.wgj.ui.project.roundway;

import android.content.Context;
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
 * Created by Eenie on 2017/5/25 at 18:36
 * Email: 472279981@qq.com
 * Des:
 */

public class RoundWaySettingActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener{
    private boolean checkState;
    public static final String PROJECT_ID = "id";
    @BindView(R.id.tv_edit)
    TextView editState;
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
    @Override
    protected int getContentView() {
        return R.layout.activity_round_way_setting;
    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);

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

    @OnClick({R.id.img_back, R.id.tv_edit, R.id.img_add_contacts, R.id.ly_add_keyman})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:

                onBackPressed();
                break;
            case R.id.tv_edit:
                if (checkState) {
                    checkState = false;
                    editState.setText("编辑");

                } else {
                    checkState = true;
                    editState.setText("完成");
                }
                mAdapter.notifyDataSetChanged();


                break;
            case R.id.img_add_contacts:


                break;
            case R.id.ly_add_keyman:


                break;


        }

    }

    @Override
    public void onRefresh() {
        mAdapter.clear();
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        mSubscription = mRemoteService.getLineList(token, projectId)
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
                        if (apiResponse.getResultCode() == 200) {
                            if (apiResponse.getData() != null) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                List<RoundWayList> postWorkLists = gson.fromJson(jsonArray,
                                        new TypeToken<List<RoundWayList>>() {
                                        }.getType());

                                if (postWorkLists != null && !postWorkLists.isEmpty()) {
                                    if (mAdapter != null) {
                                        lyNoPersonal.setVisibility(View.GONE);
                                        imgContacts.setVisibility(View.VISIBLE);
                                        lyNoData.setVisibility(View.VISIBLE);
                                        mAdapter.addAll(postWorkLists);
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
        private List<RoundWayList> contactsList;

        public RoundWayAdapter(Context context, List<RoundWayList> contactsList) {
            this.context = context;
            this.contactsList = contactsList;
        }

        @Override
        public RoundWayViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_round_way, parent, false);
            return new RoundWayViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(RoundWayViewHolder holder, int position) {
            if (contactsList != null && !contactsList.isEmpty()) {
                RoundWayList data = contactsList.get(position);
                holder.setItem(data);
                if (data != null) {
                    position=position+1;
                    if (position<10){

                        holder.reportPost.setText("0"+position+"");
                    }else {
                        holder.reportPost.setText(position+"");
                    }
                    holder.reportClass.setText(data.getName());

                }
                if (checkState){
                    holder.itemEdit.setVisibility(View.VISIBLE);
                    holder.itemDelete.setVisibility(View.VISIBLE);
                }else {
                    holder.itemEdit.setVisibility(View.GONE);
                    holder.itemDelete.setVisibility(View.GONE);
                }

            }

        }

        @Override
        public int getItemCount() {
            return contactsList.size();
        }

        public void addAll(List<RoundWayList> contactsList) {
            this.contactsList.addAll(contactsList);
            RoundWayAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.contactsList.clear();
            RoundWayAdapter.this.notifyDataSetChanged();
        }

        class RoundWayViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView reportPost;
            private TextView reportClass;
            private RelativeLayout rlReport;
            private TextView itemEdit;
            private TextView itemDelete;
            private RoundWayList mReportPostList;

            public RoundWayViewHolder(View itemView) {

                super(itemView);

                reportPost = ButterKnife.findById(itemView, R.id.item_post);
                reportClass = ButterKnife.findById(itemView, R.id.item_class);
                rlReport = ButterKnife.findById(itemView, R.id.rl_key_personal);
                itemEdit = ButterKnife.findById(itemView, R.id.tv_edit_item);
                itemDelete = ButterKnife.findById(itemView, R.id.tv_delete_item);
                rlReport.setOnClickListener(this);


            }

            public void setItem(RoundWayList data) {
                mReportPostList = data;
            }

            @Override
            public void onClick(View v) {
//                Intent intent = new Intent(RoundPointSettingActivity.this,
//                        RoundPointDetailActivity.class);
//                if (mReportPostList != null) {
//                    intent.putExtra(RoundPointDetailActivity.INFO, mReportPostList);
//                }
//                intent.putExtra(RoundPointDetailActivity.PROJECT_ID, projectId);
//                startActivity(intent);


            }
        }
    }
}
