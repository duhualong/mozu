package org.eenie.wgj.ui.project.projecttime;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.timessquare.CalendarView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.ClassListResponse;
import org.eenie.wgj.model.response.DayMonthTime;
import org.eenie.wgj.model.response.ProjectTimeTotal;
import org.eenie.wgj.util.Constants;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/31 at 13:25
 * Email: 472279981@qq.com
 * Des:
 */

public class ProjectTimeSettingActivity extends BaseActivity implements CalendarView.OnCalendarChangeListener,
        CalendarView.HeightDateClickListener {
    @BindView(R.id.calendar_view)
    CalendarView mCalendarView;
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.tv_total_time)
    TextView tvTotalTime;
    public static final String PROJECT_ID = "project_id";
    private String projectId = "project_id";
    private ArrayList<DayMonthTime> mDayMonthTimes;
    private ExchangeWorkAdapter mAdapter;


    @Override
    protected int getContentView() {
        return R.layout.activity_total_project_time;
    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);
        mCalendarView.setOnCalendarChangeListener(this);
        mCalendarView.setOnHeightDateClickListener(this);
        tvDate.setText(new SimpleDateFormat("yyyy年MM月").format(mCalendarView.getDate()));
        if (!TextUtils.isEmpty(projectId)) {
            getProjectTime(projectId, new SimpleDateFormat("yyyy-MM").
                    format(mCalendarView.getDate()) + "");
            getProjectDayTime(projectId, new SimpleDateFormat("yyyy-MM").
                    format(mCalendarView.getDate()) + "");
        }


    }

    private void getProjectDayTime(String projectId, String date) {
        mSubscription = mRemoteService.getMonthDayTime(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""), "2017-06", projectId)
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
                            mDayMonthTimes = gson.fromJson(jsonArray,
                                    new TypeToken<ArrayList<DayMonthTime>>() {
                                    }.getType());
                            onDataRefresh(mDayMonthTimes);


                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    private void getProjectTime(String projectId, String date) {
        mSubscription = mRemoteService.getProjectTime(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""), date, projectId)
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
                            ProjectTimeTotal data = gson.fromJson(jsonArray,
                                    new TypeToken<ProjectTimeTotal>() {
                                    }.getType());
                            tvTotalTime.setText(String.valueOf(data.getHours().
                                    getWorkinghours()));

                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();

                        }

                    }
                });
    }

    @OnClick({R.id.img_back, R.id.setting_tv, R.id.btnPri, R.id.btnNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:

                onBackPressed();
                break;
            case R.id.setting_tv:

                break;
            case R.id.btnPri:
                mCalendarView.prevMonth();
                onDateChange();
                break;
            case R.id.btnNext:
                mCalendarView.nextMonth();
                onDateChange();

                break;
        }
    }

    @Override
    public void onHeightDateClick(Date date) {

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

                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            if (apiResponse.getData() != null) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<ClassListResponse> exchangeWorkLists = gson.fromJson(jsonArray,
                                        new TypeToken<ArrayList<ClassListResponse>>() {
                                        }.getType());

                                if (exchangeWorkLists != null && !exchangeWorkLists.isEmpty()) {
                                    for (int i = 0; i < mDayMonthTimes.size(); i++) {
                                        if (mDayMonthTimes.get(i).getDate().endsWith(
                                                (new SimpleDateFormat("yyyy-MM-dd").
                                                format(date) + "").trim())) {
                                            for (int m=0;m<exchangeWorkLists.size();m++){
                                                for (int j = 0; j < mDayMonthTimes.get(i).
                                                        getService().size(); j++) {
                                                    if (mDayMonthTimes.get(i).
                                                            getService().get(j).getService().getId()==
                                                            exchangeWorkLists.get(m).getService().
                                                                    getId()){
                                                        exchangeWorkLists.get(m).setChecked(true);
                                                        exchangeWorkLists.get(m).setService_people
                                                                (Integer.parseInt(mDayMonthTimes.get(i).
                                                                getService().get(j).getService_people()));
                                                    }

                                                }
                                            }


                                        }

                                    }
                                    showDateDialog(date,exchangeWorkLists);
                                }
                            }
                        }
                    }
                });



    }

    private void showDateDialog(Date date,ArrayList<ClassListResponse>data) {
        View view = View.inflate(context, R.layout.dialog_show_arrange_time, null);
        TextView dialogTitle = (TextView) view.findViewById(R.id.tv_title);
        RecyclerView recycler = (RecyclerView) view.findViewById(
                R.id.recycler_add_range);
        dialogTitle.setText(new SimpleDateFormat("yyyy-MM-dd").format(date));
        mAdapter = new ExchangeWorkAdapter(context,data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(mAdapter);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();



    }

    class ExchangeWorkAdapter extends RecyclerView.Adapter<ExchangeWorkAdapter.ExchangeWorkViewHolder> {
        private Context context;
        private List<ClassListResponse> mClassMeetingLists;

        public ExchangeWorkAdapter(Context context, List<ClassListResponse> mClassMeetingLists) {
            this.context = context;
            this.mClassMeetingLists = mClassMeetingLists;
        }

        @Override
        public ExchangeWorkViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_project_time_show, parent, false);
            return new ExchangeWorkViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ExchangeWorkViewHolder holder, int position) {
            if (mClassMeetingLists != null && !mClassMeetingLists.isEmpty()) {
                ClassListResponse data = mClassMeetingLists.get(position);
                holder.setItem(data);
                if (data != null) {
                    if (!TextUtils.isEmpty(data.getService().getServicesname())) {
                        holder.itemName.setText(data.getService().getServicesname());
                    }
                    if (data.isChecked()) {
                        holder.mCheckBox.setChecked(true);
                    } else {
                        holder.mCheckBox.setChecked(true);
                    }
                    if (data.getService_people() > 0) {
                        holder.etNumber.setText(data.getService_people());
                    }
                }


            }

        }

        @Override
        public int getItemCount() {
            return mClassMeetingLists.size();
        }

        public void addAll(List<ClassListResponse> classMeetingLists) {
            this.mClassMeetingLists.addAll(classMeetingLists);
            ExchangeWorkAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mClassMeetingLists.clear();
            ExchangeWorkAdapter.this.notifyDataSetChanged();
        }

        class ExchangeWorkViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            private CheckBox mCheckBox;
            private TextView itemName;
            private EditText etNumber;
            private ClassListResponse mClassMeetingList;


            public ExchangeWorkViewHolder(View itemView) {

                super(itemView);

                mCheckBox = ButterKnife.findById(itemView, R.id.checkbox_password_remember);
                itemName = ButterKnife.findById(itemView, R.id.item_name);
                etNumber = ButterKnife.findById(itemView, R.id.btn_number);
                mCheckBox.setOnClickListener(this);

            }

            public void setItem(ClassListResponse data) {
                mClassMeetingList = data;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.checkbox_password_remember:
                        if (mCheckBox.isChecked()) {
                            if (Integer.parseInt(etNumber.getText().toString()) > 0) {
                                mClassMeetingList.setChecked(true);
                                mClassMeetingList.setService_people(Integer.parseInt(
                                        etNumber.getText().toString()));
                            } else {
                                Toast.makeText(ProjectTimeSettingActivity.this, "人员数量必须大于零",
                                        Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            mClassMeetingList.setChecked(false);
                        }


                        break;
                }


            }
        }
    }


    @Override
    public void onChange(Date date) {
        tvDate.setText(new SimpleDateFormat("yyyy年MM月").format(date));
        getProjectTime(projectId, new SimpleDateFormat("yyyy-MM").format(date) + "");
    }

    public void onDateChange() {
        mCalendarView.getSelectedCells().clear();
        mCalendarView.upCalendarView();
//        mLaborPresenter.getRankList(projectId);
//        mLaborPresenter.getLaborList(projectId, new SimpleDateFormat("yyyy-MM-dd").format(mCalendarView.getDate()));
    }

    public Calendar covertTime(String time) {
        Calendar dayc1 = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date daystart = null;
        try {
            daystart = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dayc1.setTime(daystart);
        return dayc1;
    }


    public void onDataRefresh(ArrayList<DayMonthTime> mData) {
        mCalendarView.clearHeightCells();

        for (DayMonthTime bean : mData) {
            mCalendarView.addHeightCells(covertTime(bean.getDate()));
        }
        mCalendarView.upCalendarView();


    }
}
