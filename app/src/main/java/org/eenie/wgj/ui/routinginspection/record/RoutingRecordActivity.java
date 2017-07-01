package org.eenie.wgj.ui.routinginspection.record;

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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.RecordRoutingResponse;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.ui.routinginspection.base.HintProgressBar;
import org.eenie.wgj.util.Constants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.eenie.wgj.R.id.tv_end_time;
import static org.eenie.wgj.R.id.tv_start_time;

/**
 * Created by Eenie on 2017/6/28 at 14:48
 * Email: 472279981@qq.com
 * Des:
 */

public class RoutingRecordActivity extends BaseActivity  {
//    @BindView(R.id.swipe_refresh_list)
//    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(tv_start_time)
    TextView mTvStartTime;
    @BindView(R.id.tv_end_time)
    TextView mTvEndTime;
    private String startTime;
    private String endTime;
    private RoutingRecordAdapter mAdapter;

    @Override
    protected int getContentView() {
        return R.layout.activity_routing_record_setting;
    }

    @Override
    protected void updateUI() {
        mAdapter=new RoutingRecordAdapter(context,new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

    }


    @OnClick({tv_start_time, R.id.tv_end_time,R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_back:

                onBackPressed();
                break;
            case tv_start_time:
                showWhitDate(tv_start_time);

                break;
            case R.id.tv_end_time:
                showWhitDate(tv_end_time);

                break;
        }

    }

    public void showWhitDate(final int id) {
        final DatePickerDialogFragment datePickerDialogFragment =
                new DatePickerDialogFragment(getSupportFragmentManager(), "选择日期", "date");
        datePickerDialogFragment.setOnDateDissListener(new DatePickerDialogFragment.onTimePickedListener() {
            @Override
            public void onPicked(String date) {
                switch (id) {
                    case tv_start_time:
                        mTvStartTime.setText(date);
                        break;
                    case R.id.tv_end_time:
                        mTvEndTime.setText(date);
                        break;
                }
                fetchData();
            }
        });
        datePickerDialogFragment.show(getSupportFragmentManager(), "date");
    }

    private void fetchData() {
        if (TextUtils.isEmpty(mTvStartTime.getText().toString())) {
            Toast.makeText(context,"请选择开始日期",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(mTvEndTime.getText().toString())) {
           Toast.makeText(context,"请选择结束日期",Toast.LENGTH_SHORT).show();
            return;
        }
        startTime=mTvStartTime.getText().toString();
        endTime=mTvEndTime.getText().toString();

        onRefreshes(mTvStartTime.getText().toString(),mTvEndTime.getText().toString());

    }


    public void onRefreshes(String startTime,String endTime) {
        mAdapter.clear();
        mSubscription=mRemoteService.getTimeRoutingRecordList(mPrefsHelper.
                        getPrefs().getString(Constants.TOKEN, ""),
                startTime, endTime)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(apiResponse.getData());
                        List<RecordRoutingResponse> data =
                                gson.fromJson(jsonArray,
                                        new TypeToken<List<RecordRoutingResponse>>() {
                                        }.getType());
                        if (data != null && !data.isEmpty()) {

                            mAdapter.addAll(data);

                        }
                    }
                });


    }


    @Override
    public void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(startTime)&&!TextUtils.isEmpty(endTime)){
            onRefreshes(startTime,endTime);
        }

    }

    class RoutingRecordAdapter extends RecyclerView.Adapter<RoutingRecordAdapter.ProjectViewHolder> {
        private Context context;
        private  List<RecordRoutingResponse> mRecordArrayList;

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
                    if (data.getUser()!=null){
                        holder.itemReportName.setText(data.getUser().getName());
                    }
                    if (data.getDate()!=null){
                        holder.itemReportTime.setText(data.getDate());
                    }
                    if (data.getRate()!=null){

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
                itemPointCorrect= ButterKnife.findById(itemView, R.id.tv_point_complete);
                lineRoutingRecord=ButterKnife.findById(itemView,R.id.line_routing_record);
                lineRoutingRecord.setOnClickListener(this);
            }

            public void setItem(RecordRoutingResponse mData) {
                mRecordRoutingResponse = mData;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()){
                    case R.id.line_routing_record:
                        startActivity(new Intent(context,RoutingRecordItemActivity.class)
                        .putExtra(RoutingRecordItemActivity.ROUTING_INFO,mRecordRoutingResponse));
                        break;

                }


            }
        }
    }
}
