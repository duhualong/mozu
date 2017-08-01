package org.eenie.wgj.ui.routinginspection.report;

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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.ReportRoutingReponse;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/23 at 16:40
 * Email: 472279981@qq.com
 * Des:
 */

public class PatrolErrorFragment extends BaseSupportFragment {
    @BindView(R.id.rv_patrol_error)
    RecyclerView mRvPatrolError;
    private ProjectAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.fragment_patrol_error_history;
    }

    @Override
    protected void updateUI() {
        mAdapter = new ProjectAdapter(context, new ArrayList<>());
        mRvPatrolError.setLayoutManager(new LinearLayoutManager(context));
        mRvPatrolError.setAdapter(mAdapter);
        initData();
    }

    @Override
    public void getBundle(Bundle bundle) {

    }

    private void initData() {
        mSubscription = mRemoteService.getReportRoutingList
                (mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""), 1)
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
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            List<ReportRoutingReponse> data =
                                    gson.fromJson(jsonArray,
                                            new TypeToken<List<ReportRoutingReponse>>() {
                                            }.getType());
                            if (data != null) {
                                if (mAdapter != null) {
                                    mAdapter.addAll(data);
                                }


                            } else {
                                Toast.makeText(context, "没有数据",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.ProjectViewHolder> {
        private Context context;
        private List<ReportRoutingReponse> mReportRoutingReponses;

        public ProjectAdapter(Context context, List<ReportRoutingReponse> mReportRoutingReponses) {
            this.context = context;
            this.mReportRoutingReponses = mReportRoutingReponses;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_patrol_error_layout, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (mReportRoutingReponses != null && !mReportRoutingReponses.isEmpty()) {
                ReportRoutingReponse data = mReportRoutingReponses.get(position);
                holder.setItem(data);
                if (data != null) {
                    holder.itemAddress.setText(data.getInspectionpoint().getInspectionname());
                    holder.itemState.setText("异常");
                    holder.itemReportNumber.setText(data.getUniqueid());
                    if (data.getTime() != null && data.getTime().length() >= 11) {
                        holder.itemDate.setText(data.getTime().substring(0, 10));
                        holder.itemTime.setText(data.getTime().substring(11, data.getTime().length()));

                    }


                    // holder.imgProject.setImageURI(data.get);
                }
//设置显示内容

            }

        }

        @Override
        public int getItemCount() {
            return mReportRoutingReponses.size();
        }

        public void addAll(List<ReportRoutingReponse> projectList) {
            this.mReportRoutingReponses.addAll(projectList);
            ProjectAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mReportRoutingReponses.clear();
            ProjectAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private ReportRoutingReponse mRoutingReponse;
            private LinearLayout mLinearLayout;
            private TextView itemReportNumber;
            private TextView itemDate;
            private TextView itemTime;
            private TextView itemState;
            private TextView itemAddress;


            public ProjectViewHolder(View itemView) {

                super(itemView);
                itemDate = ButterKnife.findById(itemView, R.id.tv_patrol_submit_time);
                itemReportNumber = ButterKnife.findById(itemView, R.id.tv_patrol_person_name);
                itemTime = ButterKnife.findById(itemView, R.id.tv_patrol_time);
                mLinearLayout = ButterKnife.findById(itemView, R.id.line_detail);
                itemState = ButterKnife.findById(itemView, R.id.tv_patrol_state);
                itemAddress = ButterKnife.findById(itemView, R.id.tv_patrol_addr);
                mLinearLayout.setOnClickListener(this);

            }

            public void setItem(ReportRoutingReponse projectList) {
                mRoutingReponse = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.line_detail:
                        startActivity(new Intent(context,DetailShowActivity.class).
                                putExtra(DetailShowActivity.INFO,mRoutingReponse));

                        break;
                }

            }


        }
    }
}
