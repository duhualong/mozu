package org.eenie.wgj.ui.exchangeclass;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.exchangework.ExchangeDataHistory;
import org.eenie.wgj.model.response.exchangework.ExchangeWorkHistoryResponse;
import org.eenie.wgj.model.response.exchangework.ExchangeWorkHistoryTakeResponse;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/31 at 18:32
 * Email: 472279981@qq.com
 * Des:
 */

public class TakeClassFragment extends BaseSupportFragment {
    @BindView(R.id.recycler_view_hand)
    RecyclerView mRecyclerView;
    private ArrayList<ExchangeWorkHistoryResponse> mExchangeHistory = new ArrayList<>();
    private HandWorkAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_hand_class;
    }

    @Override
    protected void updateUI() {
        initData();

    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    private void initData() {



        mSubscription = mRemoteService.getReceiveClass(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getCode() == 0) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            ExchangeDataHistory mData =
                                    gson.fromJson(jsonArray,
                                            new TypeToken<ExchangeDataHistory>() {
                                            }.getType());

                            if (mData.getData() != null) {
                                mAdapter = new HandWorkAdapter(context, mData.getData());
                                mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
                                mRecyclerView.setAdapter(mAdapter);
                            }

                        }

                    }
                });




    }


    class HandWorkAdapter extends RecyclerView.Adapter<HandWorkAdapter.ProjectViewHolder> {
        private Context context;
        private List<ExchangeWorkHistoryTakeResponse> mInspectionBeanList;


        public HandWorkAdapter(Context context, List<ExchangeWorkHistoryTakeResponse>
                mInspectionBeanList) {
            this.context = context;
            this.mInspectionBeanList = mInspectionBeanList;

        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_exchange_work_history, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (mInspectionBeanList != null && !mInspectionBeanList.isEmpty()) {

                ExchangeWorkHistoryTakeResponse data = mInspectionBeanList.get(position);
                holder.setItem(data);
                if (data != null) {
                    holder.itemClassDate.setText(data.getCreated_at());

                    holder.itemClassName.setText("接接班次：" + data.getMattername());
                    holder.itemClassTypePeople.setText("交班人员：");

                    if (data.getFrom()!=null){

                            holder.itemClassPeopleName.setText(data.getFrom().getName());

                        }


                }

            }

        }

        @Override
        public int getItemCount() {
            return mInspectionBeanList.size();
        }

        public void addAll(List<ExchangeWorkHistoryTakeResponse> projectList) {
            this.mInspectionBeanList.addAll(projectList);
            HandWorkAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mInspectionBeanList.clear();
            HandWorkAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ExchangeWorkHistoryTakeResponse mRoutingReponse;
            private LinearLayout mLinearLayout;


            private TextView itemClassName;
            private TextView itemClassDate;
            private TextView itemClassTypePeople;
            private TextView itemClassPeopleName;

            public ProjectViewHolder(View itemView) {

                super(itemView);
                mLinearLayout = ButterKnife.findById(itemView, R.id.item_work);
                itemClassName = ButterKnife.findById(itemView, R.id.item_class_name);
                itemClassDate = ButterKnife.findById(itemView, R.id.item_date);
                itemClassTypePeople = ButterKnife.findById(itemView, R.id.item_class_people_type);
                itemClassPeopleName = ButterKnife.findById(itemView, R.id.item_class_people_name);
                mLinearLayout.setOnClickListener(this);

            }

            public void setItem(ExchangeWorkHistoryTakeResponse projectList) {
                mRoutingReponse = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.item_work:
                        startActivity(new Intent(context, ExchangeHistoryItemDetailActivity.class)
                                .putExtra(ExchangeHistoryItemDetailActivity.INFO, mRoutingReponse)
                                .putExtra(ExchangeHistoryItemDetailActivity.TYPE, "2"));

                        break;

                }

            }


        }
    }
}
