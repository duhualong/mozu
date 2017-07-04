package org.eenie.wgj.ui.routingstatistics;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.routing.RoutingInfoListResponse;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.ui.routinginspection.base.HintProgressBar;
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
 * Created by Eenie on 2017/7/1 at 19:49
 * Email: 472279981@qq.com
 * Des:
 */

public class RoutingStaticsItemDetailActivity extends BaseActivity implements
        SwipeRefreshLayout.OnRefreshListener {
    public static final String PROJECT_ID = "id";
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.swipe_refresh_list)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @BindView(R.id.tv_select_time)
    TextView tvSelectTime;
    private String projectId;
    private String selectTime="1990-01";
    private RoutingRecordAdapter mAdapter;


    @Override
    protected int getContentView() {
        return R.layout.activity_routing_statics_item_detail;
    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);
//
//        selectTime=new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime());
//        tvSelectTime.setText(selectTime);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light, android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
        mAdapter = new RoutingRecordAdapter(context, new ArrayList<>());
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(
                new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
    }
    @Override
    public void onRefresh() {
        mAdapter.clear();
        mSubscription=mRemoteService.getRoutingInfoList(mPrefsHelper.getPrefs().
                        getString(Constants.TOKEN,""),
                selectTime,projectId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        cancelRefresh();
                        if (apiResponse.getResultCode()==0){
                            if (apiResponse.getData()!=null){
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                List<RoutingInfoListResponse> data =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<List<RoutingInfoListResponse>>() {
                                                }.getType());
                                if (data!=null){
                                    if (mAdapter!=null){
                                        mAdapter.clear();
                                    }
                                    mAdapter.addAll(data);
                                }

                            }
                        }

                    }
                });

    }

    @OnClick({R.id.img_back, R.id.tv_select_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:

                onBackPressed();
                break;
            case R.id.tv_select_time:
                showSelectTimeDialog();


                break;
        }
    }

    private void showSelectTimeDialog() {
        View view = View.inflate(context, R.layout.dialog_routing_time, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();
        EditText etYear = (EditText) dialog.getWindow().findViewById(R.id.et_year);
        EditText etMonth = (EditText) dialog.getWindow().findViewById(R.id.edit_month);
        etYear.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        etMonth.setKeyListener(DigitsKeyListener.getInstance("0123456789"));

        String mYear = new SimpleDateFormat("yyyy").format(Calendar.getInstance().getTime());
        String mMonth = new SimpleDateFormat("MM").format(Calendar.getInstance().getTime());
        final int[] year = new int[1];
        final int[] month = new int[1];
        year[0] = Integer.valueOf(mYear);
        month[0] = Integer.valueOf(mMonth);
        etYear.setText(mYear);
        etMonth.setText(mMonth);
        etYear.setSelection(mYear.length());
        etMonth.setSelection(mMonth.length());

        dialog.getWindow().findViewById(R.id.btn_add_year).setOnClickListener(v -> {
            String inputYear = etYear.getText().toString();
            if (!TextUtils.isEmpty(inputYear)) {
                year[0] = Integer.valueOf(inputYear);
            }

            year[0] = year[0] + 1;
            etYear.setText(String.valueOf(year[0]));


        });
        dialog.getWindow().findViewById(R.id.btn_subtract_year).setOnClickListener(v -> {
            String inputYear = etYear.getText().toString();
            if (!TextUtils.isEmpty(inputYear)) {
                year[0] = Integer.valueOf(inputYear);
            }
            year[0] = year[0] - 1;
            etYear.setText(String.valueOf(year[0]));
        });
        dialog.getWindow().findViewById(R.id.btn_add_month).setOnClickListener(v -> {
            String inputMonth = etMonth.getText().toString();
            if (!TextUtils.isEmpty(inputMonth)) {
                month[0] = Integer.valueOf(inputMonth);
            }
            if (month[0] >= 12) {
                month[0] = 0;
            }
            month[0] = month[0] + 1;
            if (month[0]<=9){
                etMonth.setText("0"+String.valueOf(month[0]));
            }else {
                etMonth.setText(String.valueOf(month[0]));
            }




        });
        dialog.getWindow().findViewById(R.id.btn_subtract_month).setOnClickListener(v -> {
            String inputMonth = etMonth.getText().toString();
            if (!TextUtils.isEmpty(inputMonth)) {
                month[0] = Integer.valueOf(inputMonth);
            }
            if (month[0] <= 1) {
                month[0] = 13;
            }
            month[0] = month[0] - 1;
            etMonth.setText(String.valueOf(month[0]));
            if (month[0]<=9){
                etMonth.setText("0"+String.valueOf(month[0]));
            }else {
                etMonth.setText(String.valueOf(month[0]));
            }

        });

        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.getWindow().findViewById(R.id.btn_ok).setOnClickListener(v -> {
            String selectYear=etYear.getText().toString().trim();
            String selectMonth=etMonth.getText().toString().trim();
            if (TextUtils.isEmpty(selectYear)){
                selectYear=String.valueOf(year[0]);
            }else {
                if (selectYear.length()<4){
                    Toast.makeText(context,"请输入正确的年份",Toast.LENGTH_SHORT).show();
                    selectYear=String.valueOf(year[0]);
                }
            }
            if (TextUtils.isEmpty(String.valueOf(selectMonth))){
                selectMonth=String.valueOf(month[0]);
                if (month[0]<=9&&selectMonth.length()==1){
                    selectMonth="0"+selectMonth;
                }

            }else {
                if (Integer.valueOf(selectMonth)>12||Integer.valueOf(selectMonth)==0){
                    Toast.makeText(context,"请输入正确的月份",Toast.LENGTH_SHORT).show();
                    selectMonth=String.valueOf(month[0]);
                    if (month[0]<=9&&selectMonth.length()==1){
                        selectMonth="0"+selectMonth;
                    }
                }else {
                    if (selectMonth.length()==1){
                        selectMonth="0"+selectMonth;
                    }
                }

            }
            if (selectMonth.length()==1){
                selectMonth="0"+selectMonth;
            }
            selectTime=selectYear+"-"+selectMonth;
            tvSelectTime.setText(selectTime);
            onRefresh();
            onRefresh();
            dialog.dismiss();
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
        onRefresh();
    }
    private void cancelRefresh() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }


    class RoutingRecordAdapter extends RecyclerView.Adapter<RoutingRecordAdapter.ProjectViewHolder> {
        private Context context;
        private List<RoutingInfoListResponse> mRecordArrayList;

        public RoutingRecordAdapter(Context context, List<RoutingInfoListResponse> mRecordArrayList) {
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
                RoutingInfoListResponse data = mRecordArrayList.get(position);
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

        public void addAll(List<RoutingInfoListResponse> projectList) {
            this.mRecordArrayList.addAll(projectList);
            RoutingRecordAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mRecordArrayList.clear();
            RoutingRecordAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

            private RoutingInfoListResponse mRecordRoutingResponse;


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

            public void setItem(RoutingInfoListResponse mData) {
                mRecordRoutingResponse = mData;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.line_routing_record:

                        startActivity(new Intent(context, RoutingStaticsDetailMonthActivity.class)
                                .putExtra(RoutingStaticsDetailMonthActivity.DATA, selectTime)
                        .putExtra(RoutingStaticsDetailMonthActivity.USER_ID,
                                String.valueOf(mRecordRoutingResponse.getUser().getId()))
                        .putExtra(RoutingStaticsDetailMonthActivity.PROJECT_ID,projectId));
                        break;

                }


            }
        }
    }


}
