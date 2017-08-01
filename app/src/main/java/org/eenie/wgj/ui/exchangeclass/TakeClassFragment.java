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

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.model.response.exchangework.ExchangeWorkHistoryResponse;
import org.eenie.wgj.util.Constant;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

        for (int i = 0; i < 3; i++) {
            List<ExchangeWorkHistoryResponse.peopleBean> listpeople = new ArrayList<>();
            List<ExchangeWorkHistoryResponse.IamgeBean> imgList = new ArrayList<>();
            imgList.add(new ExchangeWorkHistoryResponse.IamgeBean(Constant.DOMIN +
                    "/images/workshow/20170731/20170731085449YC647015801.jpg"));

            if (i == 2) {
                listpeople.add(new ExchangeWorkHistoryResponse.peopleBean(0, "张三"));
                listpeople.add(new ExchangeWorkHistoryResponse.peopleBean(1, "李四"));
            } else {
                listpeople.add(new ExchangeWorkHistoryResponse.peopleBean(i, "李四"));
            }


            ExchangeWorkHistoryResponse mdata = new ExchangeWorkHistoryResponse(i, 1, "班次" + i,
                    "2017年07月31日 08:20", listpeople, imgList, "1.交接班注意事项内容1；\n2.交接班注意事项2",
                    "1.交接班说明事项内容1；\n2.交接班说明事项2");
            mExchangeHistory.add(mdata);

        }
        mAdapter = new HandWorkAdapter(context, mExchangeHistory);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(mAdapter);


    }


    class HandWorkAdapter extends RecyclerView.Adapter<HandWorkAdapter.ProjectViewHolder> {
        private Context context;
        private List<ExchangeWorkHistoryResponse> mInspectionBeanList;


        public HandWorkAdapter(Context context, List<ExchangeWorkHistoryResponse>
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

                ExchangeWorkHistoryResponse data = mInspectionBeanList.get(position);
                holder.setItem(data);
                if (data != null) {
                    holder.itemClassDate.setText(data.getDate());

                    if (data.getType() == 0) {
                        holder.itemClassName.setText("交接班次：" + data.getClassname());
                        holder.itemClassTypePeople.setText("交班人员：");
                    } else {
                        holder.itemClassName.setText("接接班次：" + data.getClassname());
                        holder.itemClassTypePeople.setText("接班人员：");

                    }
                    if (data.getPeople().size() > 0) {
                        String name = "";
                        for (int j = 0; j < data.getPeople().size(); j++) {
                            name = name + data.getPeople().get(j).getName() + " ";

                        }

                        holder.itemClassPeopleName.setText(name);

                    }
                }


            }

        }

        @Override
        public int getItemCount() {
            return mInspectionBeanList.size();
        }

        public void addAll(List<ExchangeWorkHistoryResponse> projectList) {
            this.mInspectionBeanList.addAll(projectList);
            HandWorkAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mInspectionBeanList.clear();
            HandWorkAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ExchangeWorkHistoryResponse mRoutingReponse;
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

            public void setItem(ExchangeWorkHistoryResponse projectList) {
                mRoutingReponse = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.item_work:
                        startActivity(new Intent(context, ExchangeHistoryItemDetailActivity.class)
                                .putExtra(ExchangeHistoryItemDetailActivity.INFO, mRoutingReponse));


                        break;


                }

            }


        }
    }
}
