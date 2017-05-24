package org.eenie.wgj.ui.project.workpost;

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
import org.eenie.wgj.model.response.PostWorkList;
import org.eenie.wgj.ui.project.keypersonal.AddKeyPersonalInformationActivity;
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

/**
 * Created by Eenie on 2017/5/24 at 10:15
 * Email: 472279981@qq.com
 * Des:
 */

public class WorkPostSettingActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {
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
        return R.layout.activity_post_work_setting;
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
                        AddKeyPersonalInformationActivity.class);
                intent.putExtra(AddKeyPersonalInformationActivity.PROJECT_ID, projectId);

                startActivity(intent);

                break;
            case R.id.ly_add_keyman:
                Intent intents = new Intent(context,
                        AddKeyPersonalInformationActivity.class);
                intents.putExtra(AddKeyPersonalInformationActivity.PROJECT_ID, projectId);

                startActivity(intents);
                break;
        }

    }

    @Override
    public void onRefresh() {
        mKeyContactAdapter.clear();
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        mSubscription = mRemoteService.getPostList(token, projectId)
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
                                List<PostWorkList> postWorkLists = gson.fromJson(jsonArray,
                                        new TypeToken<List<PostWorkList>>() {
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
        private List<PostWorkList> contactsList;

        public KeyContactAdapter(Context context, List<PostWorkList> contactsList) {
            this.context = context;
            this.contactsList = contactsList;
        }

        @Override
        public KeyContactViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_key_contacts_setting, parent, false);
            return new KeyContactViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(KeyContactViewHolder holder, int position) {
            if (contactsList != null && !contactsList.isEmpty()) {
                PostWorkList data = contactsList.get(position);
                holder.setItem(data);
                if (data != null) {

                    if (data.getPost() != null && !TextUtils.isEmpty(data.getPost())) {
                        holder.contactsName.setText(data.getPost());
                    }
                    holder.avatarImg.setVisibility(View.GONE);


                }


            }

        }

        @Override
        public int getItemCount() {
            return contactsList.size();
        }

        public void addAll(List<PostWorkList> contactsList) {
            this.contactsList.addAll(contactsList);
            KeyContactAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.contactsList.clear();
            KeyContactAdapter.this.notifyDataSetChanged();
        }

        class KeyContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView contactsName;
            private CircleImageView avatarImg;
            private RelativeLayout rlPersonal;
            private PostWorkList mContactList;

            public KeyContactViewHolder(View itemView) {

                super(itemView);

                contactsName = ButterKnife.findById(itemView, R.id.item_contacts_name);
                avatarImg = ButterKnife.findById(itemView, R.id.img_avatar);
                rlPersonal = ButterKnife.findById(itemView, R.id.rl_key_personal);
                rlPersonal.setOnClickListener(this);

            }

            public void setItem(PostWorkList data) {
                mContactList = data;
            }

            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WorkPostSettingActivity.this,
                        WorkPostDetailActivity.class);
                if (mContactList!=null){
                    intent.putExtra(WorkPostDetailActivity.INFO,mContactList);

                }
                intent.putExtra(WorkPostDetailActivity.PROJECT_ID,projectId);
                startActivity(intent);




            }
        }
    }
}