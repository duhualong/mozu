package org.eenie.wgj.ui.exchangeclass;

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
import org.eenie.wgj.model.response.exchangework.ExchangeWorkListResponse;
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
 * Created by Eenie on 2017/7/13 at 11:51
 * Email: 472279981@qq.com
 * Des:
 */

public class ExchangeWorkClassSetting extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener {
    public static final String PROJECT_ID = "id";
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_project_contacts)
    RecyclerView mRecyclerView;
    private ExchangeWorkAdapter mExchangeWorkAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_exchange_work_module;
    }

    @Override
    protected void updateUI() {

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

    @OnClick(R.id.img_back)
    public void onClick() {

        onBackPressed();


    }

    @Override
    public void onRefresh() {
        mExchangeWorkAdapter.clear();
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        mSubscription = mRemoteService.getExchangeList(token)
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
                                List<ExchangeWorkListResponse> exchangeWorkLists = gson.fromJson(jsonArray,
                                        new TypeToken<List<ExchangeWorkListResponse>>() {
                                        }.getType());

                                if (exchangeWorkLists != null && !exchangeWorkLists.isEmpty()) {
                                    if (mExchangeWorkAdapter != null) {
                                        mExchangeWorkAdapter.clear();
                                    }
                                    mExchangeWorkAdapter.addAll(exchangeWorkLists);
                                }
                            }

                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                            ;
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
        private List<ExchangeWorkListResponse> mExchangeWorkLists;

        public ExchangeWorkAdapter(Context context, List<ExchangeWorkListResponse> mExchangeWorkLists) {
            this.context = context;
            this.mExchangeWorkLists = mExchangeWorkLists;
        }

        @Override
        public ExchangeWorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_key_contacts_setting, parent, false);
            return new ExchangeWorkViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ExchangeWorkViewHolder holder, int position) {
            if (mExchangeWorkLists != null && !mExchangeWorkLists.isEmpty()) {
                ExchangeWorkListResponse data = mExchangeWorkLists.get(position);
                if (data != null) {
                    holder.setItem(data);
                    holder.avatarImg.setVisibility(View.GONE);
                    if (!TextUtils.isEmpty(data.getName())) {
                        holder.contactsName.setText(data.getName());
                    }

                }


            }

        }

        @Override
        public int getItemCount() {
            return mExchangeWorkLists.size();
        }

        public void addAll(List<ExchangeWorkListResponse> mExchangeWorkLists) {
            this.mExchangeWorkLists.addAll(mExchangeWorkLists);
            ExchangeWorkAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mExchangeWorkLists.clear();
            ExchangeWorkAdapter.this.notifyDataSetChanged();
        }

        class ExchangeWorkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private TextView contactsName;
            private CircleImageView avatarImg;
            private RelativeLayout rlPersonal;
            private ExchangeWorkListResponse mExchangeWorkList;

            public ExchangeWorkViewHolder(View itemView) {

                super(itemView);

                contactsName = ButterKnife.findById(itemView, R.id.item_contacts_name);
                avatarImg = ButterKnife.findById(itemView, R.id.img_avatar);
                rlPersonal = ButterKnife.findById(itemView, R.id.rl_key_personal);
                rlPersonal.setOnClickListener(this);

            }

            public void setItem(ExchangeWorkListResponse data) {
                mExchangeWorkList = data;
            }

            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, ExchangeWorkClassDetailActivity.class)
                        .putExtra(ExchangeWorkClassDetailActivity.INFO, mExchangeWorkList));


            }
        }
    }
}
