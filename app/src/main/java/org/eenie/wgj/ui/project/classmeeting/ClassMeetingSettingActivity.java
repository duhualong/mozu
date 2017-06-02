package org.eenie.wgj.ui.project.classmeeting;

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
import org.eenie.wgj.model.response.ClassMeetingList;
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
 * Created by Eenie on 2017/5/22 at 8:51
 * Email: 472279981@qq.com
 * Des:
 */

public class ClassMeetingSettingActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener {
    public static final String PROJECT_ID = "id";

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
    private ExchangeWorkAdapter mExchangeWorkAdapter;
    private String projectId;

    @Override
    protected int getContentView() {
        return R.layout.activity_classwide_meeting_setting;
    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);

        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mExchangeWorkAdapter = new ExchangeWorkAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.setAdapter(mExchangeWorkAdapter);


    }

    @OnClick({R.id.img_back, R.id.img_add_contacts, R.id.ly_add_keyman})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.img_add_contacts:
                Intent intent = new Intent(context,
                        ClassMeetingItemDetailActivity.class);

                    if (!TextUtils.isEmpty(projectId)) {
                        intent.putExtra(ClassMeetingItemDetailActivity.PROJECT_ID, projectId);
                    }

                startActivity(intent);
                break;
            case R.id.ly_add_keyman:
                Intent intents = new Intent(context,
                        ClassMeetingItemDetailActivity.class);

                if (!TextUtils.isEmpty(projectId)) {
                    intents.putExtra(ClassMeetingItemDetailActivity.PROJECT_ID, projectId);
                }
                startActivity(intents);
                break;
        }

    }

    @Override
    public void onRefresh() {
        mExchangeWorkAdapter.clear();
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        mSubscription = mRemoteService.getClassWideList(token, projectId)
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
                        if (apiResponse.getResultCode() == 200||apiResponse.getResultCode()==0) {
                            if (apiResponse.getData() != null) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<ClassMeetingList> exchangeWorkLists = gson.fromJson(jsonArray,
                                        new TypeToken<ArrayList<ClassMeetingList>>() {
                                        }.getType());

                                if (exchangeWorkLists != null && !exchangeWorkLists.isEmpty()) {
                                    if (mExchangeWorkAdapter != null) {
                                        lyNoPersonal.setVisibility(View.GONE);
                                        imgContacts.setVisibility(View.VISIBLE);
                                        lyNoData.setVisibility(View.VISIBLE);
                                        mExchangeWorkAdapter.addAll(exchangeWorkLists);
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

    class ExchangeWorkAdapter extends RecyclerView.Adapter<ExchangeWorkAdapter.ExchangeWorkViewHolder> {
        private Context context;
        private ArrayList<ClassMeetingList> mClassMeetingLists;

        public ExchangeWorkAdapter(Context context, ArrayList<ClassMeetingList> mClassMeetingLists) {
            this.context = context;
            this.mClassMeetingLists = mClassMeetingLists;
        }

        @Override
        public ExchangeWorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_classwide_setting, parent, false);
            return new ExchangeWorkViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ExchangeWorkViewHolder holder, int position) {
            if (mClassMeetingLists != null && !mClassMeetingLists.isEmpty()) {
                ClassMeetingList data = mClassMeetingLists.get(position);
                holder.setItem(data);
                if (data != null) {
                    if (!TextUtils.isEmpty(data.getServicesname())) {
                        holder.contactsName.setText(data.getServicesname());
                    }
                    if (!TextUtils.isEmpty(data.getStarttime())&&!TextUtils.isEmpty(data.getEndtime())){
                        holder.timeClass.setText(data.getStarttime()+"-"+data.getEndtime());
                    }

                }


            }

        }

        @Override
        public int getItemCount() {
            return mClassMeetingLists.size();
        }

        public void addAll(List<ClassMeetingList> classMeetingLists) {
            this.mClassMeetingLists.addAll(classMeetingLists);
            ExchangeWorkAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mClassMeetingLists.clear();
            ExchangeWorkAdapter.this.notifyDataSetChanged();
        }

        class ExchangeWorkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView contactsName;
            private RelativeLayout rlPersonal;
            private ClassMeetingList mClassMeetingList;
            private ImageView editImg;
            private TextView timeClass;

            public ExchangeWorkViewHolder(View itemView) {

                super(itemView);

                contactsName = ButterKnife.findById(itemView, R.id.item_contacts_name);
                rlPersonal = ButterKnife.findById(itemView, R.id.rl_key_personal);
                editImg=ButterKnife.findById(itemView,R.id.img_edit_classwide);
                timeClass=ButterKnife.findById(itemView,R.id.item_time);
                rlPersonal.setOnClickListener(this);
                editImg.setOnClickListener(this);

            }

            public void setItem(ClassMeetingList data) {
                mClassMeetingList = data;
            }

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,
                        ClassMeetingItemDetailActivity.class);
                if (mClassMeetingList != null) {
                    intent.putExtra(ClassMeetingItemDetailActivity.INFO, mClassMeetingList);
                    if (!TextUtils.isEmpty(projectId)) {
                        intent.putExtra(ClassMeetingItemDetailActivity.PROJECT_ID, projectId);
                    }
                }

                startActivity(intent);


            }
        }
    }
}
