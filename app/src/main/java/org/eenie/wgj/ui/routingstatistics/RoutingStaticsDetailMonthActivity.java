package org.eenie.wgj.ui.routingstatistics;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.RecordRoutingResponse;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.ui.routinginspection.base.HintProgressBar;
import org.eenie.wgj.ui.routinginspection.record.RoutingRecordItemActivity;
import org.eenie.wgj.util.Constants;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/3 at 13:54
 * Email: 472279981@qq.com
 * Des:
 */

public class RoutingStaticsDetailMonthActivity extends BaseActivity {
    public static final String DATA = "date";
    public static final String USER_ID = "user_id";
    public static final String PROJECT_ID = "project_id";
    private String projectId;
    private String date;
    private String userId;
    private RoutingRecordAdapter mAdapter;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected int getContentView() {
        return R.layout.activity_routing_static_list_month;

    }

    @OnClick(R.id.img_back)
    public void onClick() {
        onBackPressed();
    }

    @Override
    protected void updateUI() {
        mAdapter = new RoutingRecordAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);
        projectId = getIntent().getStringExtra(PROJECT_ID);
        userId = getIntent().getStringExtra(USER_ID);
        date = getIntent().getStringExtra(DATA);
        if (!TextUtils.isEmpty(date)) {
            String[] date = this.date.split("-");
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.YEAR, Integer.parseInt(date[0]));
            calendar.set(Calendar.MONTH, Integer.parseInt(date[1]) - 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String startTime = dateFormat.format(calendar.getTime());
            calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
            String endTime = dateFormat.format(calendar.getTime());
            initData(userId, startTime, endTime);
            initData(userId, startTime, endTime);


        }

    }

    private void initData(String userId, String startTime, String endTime) {
        mAdapter.clear();
        mSubscription = mRemoteService.getTimeRoutingRecordList(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                startTime, endTime, userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode() == 0) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            List<RecordRoutingResponse> data =
                                    gson.fromJson(jsonArray,
                                            new TypeToken<List<RecordRoutingResponse>>() {
                                            }.getType());
                            if (data != null && !data.isEmpty()) {
                                if (mAdapter != null) {
                                    mAdapter.clear();
                                }
                                mAdapter.addAll(data);

                            }
                        }
                    }
                });

    }

    class RoutingRecordAdapter extends RecyclerView.Adapter<RoutingRecordAdapter.ProjectViewHolder> {
        private Context context;
        private List<RecordRoutingResponse> mRecordArrayList;

        public RoutingRecordAdapter(Context context, List<RecordRoutingResponse> mRecordArrayList) {
            this.context = context;
            this.mRecordArrayList = mRecordArrayList;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_patrol_statistic, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (mRecordArrayList != null && !mRecordArrayList.isEmpty()) {
                RecordRoutingResponse data = mRecordArrayList.get(position);
                holder.setItem(data);
                if (data != null) {
                    if (data.getUser() != null) {
                        holder.itemReportName.setText(data.getUser().getName());
                    }
                    if (data.getDate() != null) {
                        holder.itemReportTime.setText(data.getDate());
                    }
                    if (data.getRate() != null) {

                        holder.mHintProgressBarFinish.setProgress((int) data.getRate().getTurn());
                        holder.itemTurnFinish.setText(String.format("圈数：%s/%s",
                                data.getRate().getTurn_ring(),
                                data.getRate().getRing()));
                        holder.mHintProgressBarCorrect.setProgress(
                                (int) data.getRate().getNumber());
                        holder.itemPointCorrect.setText(String.format("点数：%s/%s",
                                data.getRate().getTurn_actual(), data.getRate().getTurn_total()));
                    }

                }

            }

        }

        @Override
        public int getItemCount() {
            return mRecordArrayList.size();
        }

        public void addAll(List<RecordRoutingResponse> projectList) {
            this.mRecordArrayList.addAll(projectList);
            RoutingRecordAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mRecordArrayList.clear();
            RoutingRecordAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private RecordRoutingResponse mRecordRoutingResponse;


            private TextView itemReportName;
            private TextView itemReportTime;
            private HintProgressBar mHintProgressBarFinish;
            private TextView itemTurnFinish;
            private HintProgressBar mHintProgressBarCorrect;
            private TextView itemPointCorrect;
            private LinearLayout lineRoutingRecord;


            public ProjectViewHolder(View itemView) {
                super(itemView);
                itemReportName = ButterKnife.findById(itemView, R.id.tv_report_name);
                itemReportTime = ButterKnife.findById(itemView, R.id.tv_report_time);
                mHintProgressBarFinish = ButterKnife.findById(itemView, R.id.turn_progress);
                itemTurnFinish = ButterKnife.findById(itemView, R.id.tv_turn_complete);
                mHintProgressBarCorrect = ButterKnife.findById(itemView, R.id.point_progress);
                itemPointCorrect = ButterKnife.findById(itemView, R.id.tv_point_complete);
                lineRoutingRecord = ButterKnife.findById(itemView, R.id.line_routing_record);
                lineRoutingRecord.setOnClickListener(this);
            }

            public void setItem(RecordRoutingResponse mData) {
                mRecordRoutingResponse = mData;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.line_routing_record:
                        startActivity(new Intent(context, RoutingRecordItemActivity.class)
                                .putExtra(RoutingRecordItemActivity.ROUTING_INFO, mRecordRoutingResponse)
                                .putExtra(RoutingRecordItemActivity.PROJECT_ID,projectId)
                                .putExtra(RoutingRecordItemActivity.USER_ID,userId));
                        break;

                }


            }
        }
    }


}
