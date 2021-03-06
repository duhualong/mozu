package org.eenie.wgj.ui.project.reportpost;

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
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.ReportPostList;
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
 * Created by Eenie on 2017/5/24 at 13:59
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportPostSettingActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
    public static final String PROJECT_ID = "project_id";
    private String projectId;
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.ly_add_keyman)
    LinearLayout lyNoPersonal;
    @BindView(R.id.lv_data_contacts)
    LinearLayout lyNoData;
    @BindView(R.id.img_add_contacts)
    ImageView imgContacts;
    @BindView(R.id.swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_project_contacts)
    RecyclerView mRecyclerView;
    private KeyContactAdapter mKeyContactAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_report_post;
    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mKeyContactAdapter = new KeyContactAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(mKeyContactAdapter);


    }

    @OnClick({R.id.img_back, R.id.img_add_contacts, R.id.ly_add_keyman})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.img_add_contacts:

                Intent intent = new Intent(context,
                        ReportPostAddActivity.class);
                intent.putExtra(ReportPostAddActivity.PROJECT_ID, projectId);

                startActivity(intent);

                break;
            case R.id.ly_add_keyman:
                Intent intents = new Intent(context,
                        ReportPostAddActivity.class);
                intents.putExtra(ReportPostAddActivity.PROJECT_ID, projectId);

                startActivity(intents);
                break;
        }

    }

    @Override
    public void onRefresh() {
        mKeyContactAdapter.clear();
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        mSubscription = mRemoteService.getReportPostList(token, projectId)
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
                                List<ReportPostList> postWorkLists = gson.fromJson(jsonArray,
                                        new TypeToken<List<ReportPostList>>() {
                                        }.getType());

                                if (postWorkLists != null && !postWorkLists.isEmpty()) {
                                    if (mKeyContactAdapter != null) {
                                        lyNoPersonal.setVisibility(View.GONE);
                                        imgContacts.setVisibility(View.VISIBLE);
                                        lyNoData.setVisibility(View.VISIBLE);
                                        mKeyContactAdapter.addAll(postWorkLists);
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

    class KeyContactAdapter extends RecyclerView.Adapter<KeyContactAdapter.KeyContactViewHolder> {
        private Context context;
        private List<ReportPostList> contactsList;

        public KeyContactAdapter(Context context, List<ReportPostList> contactsList) {
            this.context = context;
            this.contactsList = contactsList;
        }

        @Override
        public KeyContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_report_post, parent, false);
            return new KeyContactViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(KeyContactViewHolder holder, int position) {
            if (contactsList != null && !contactsList.isEmpty()) {
                ReportPostList data = contactsList.get(position);
                holder.setItem(data);
                if (data != null) {
                    if (!TextUtils.isEmpty(data.getPostsetting().getPost())) {
                        holder.reportPost.setText(data.getPostsetting().getPost());
                    }
                    if (!TextUtils.isEmpty(data.getService().getServicesname())) {
                        holder.reportClass.setText(data.getService().getServicesname());
                    }

                }


            }

        }

        @Override
        public int getItemCount() {
            return contactsList.size();
        }

        public void addAll(List<ReportPostList> contactsList) {
            this.contactsList.addAll(contactsList);
            KeyContactAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.contactsList.clear();
            KeyContactAdapter.this.notifyDataSetChanged();
        }

        class KeyContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private TextView reportPost;
            private TextView reportClass;
            private RelativeLayout rlReport;
            private ReportPostList mReportPostList;

            public KeyContactViewHolder(View itemView) {

                super(itemView);

                reportPost = ButterKnife.findById(itemView, R.id.item_post);
                reportClass = ButterKnife.findById(itemView, R.id.item_class);
                rlReport = ButterKnife.findById(itemView, R.id.rl_key_personal);
                rlReport.setOnClickListener(this);


            }

            public void setItem(ReportPostList data) {
                mReportPostList = data;
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportPostSettingActivity.this,
                        ReportPostDetailActivity.class);
                if (mReportPostList != null) {
                    intent.putExtra(ReportPostDetailActivity.INFO, mReportPostList);
                }
                intent.putExtra(ReportPostDetailActivity.PROJECT_ID, projectId);
                startActivity(intent);


            }
        }
    }

}
