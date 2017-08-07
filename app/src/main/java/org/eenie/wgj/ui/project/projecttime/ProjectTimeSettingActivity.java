package org.eenie.wgj.ui.project.projecttime;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.timessquare.CalendarView;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.ProjectTimeRequest;
import org.eenie.wgj.model.response.ClassListResponse;
import org.eenie.wgj.model.response.DayMonthTime;
import org.eenie.wgj.model.response.TotalTimeProject;
import org.eenie.wgj.model.response.project.QueryService;
import org.eenie.wgj.model.response.project.ServiceClassPeople;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
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
    private int serviceId;
    private int count;
    private  int servicePeople;

    @Override
    protected int getContentView() {
        return R.layout.activity_total_project_time;
    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);
        getServiceClassList();
        mCalendarView.setOnCalendarChangeListener(this);
        mCalendarView.setOnHeightDateClickListener(this);
        tvDate.setText(new SimpleDateFormat("yyyy年MM月").format(mCalendarView.getDate()));
        if (!TextUtils.isEmpty(projectId)) {
            getProjectTime(projectId, new SimpleDateFormat("yyyy-MM-dd").
                    format(mCalendarView.getDate()) + "");
            getProjectDayTime(projectId, new SimpleDateFormat("yyyy-MM").
                    format(mCalendarView.getDate()) + "");
        }

    }
    private void getServiceClassList() {

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
                                if (exchangeWorkLists.size()>0){
                                    for (int i=0;i<exchangeWorkLists.size();i++){
                                        if (exchangeWorkLists.get(i).getServicesname().equals("常日班")){
                                            queryServiceDay();
                                        }

                                    }

                                }



                            }
                        }
                    }
                });

    }

    private void queryServiceDay() {
        QueryService request=new QueryService(projectId,"常日班");
        mSubscription = mRemoteService.queryServicePeople(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN,""),request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getCode()==0){
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            ServiceClassPeople serviceData = gson.fromJson(jsonArray,
                                    new TypeToken<ServiceClassPeople>() {
                                    }.getType());
                            if (serviceData!=null){
                                servicePeople=serviceData.getPersons();
                            }


                        }else {


                        }

                    }
                });
    }

    private void getProjectDayTime(String projectId, String date) {
        mSubscription = mRemoteService.getMonthDayTime(
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

        mSubscription = mRemoteService.getProjectTotalTime(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""), projectId, date)
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
                        if (apiResponse.getCode() == 0) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            TotalTimeProject data = gson.fromJson(jsonArray,
                                    new TypeToken<TotalTimeProject>() {
                                    }.getType());
                            tvTotalTime.setText(data.getTotal());

                        } else {
                            Toast.makeText(context, apiResponse.getMessage(),
                                    Toast.LENGTH_SHORT).show();
                            tvTotalTime.setText("0");


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
                boolean checkDate = true;
                ArrayList<String> dates = new ArrayList<>();
                if (mCalendarView.getSelectedCells() != null) {

                    for (int i = 0; i < mCalendarView.getSelectedCells().size(); i++) {

                        Log.d("日历", "选择: " + new SimpleDateFormat("yyyy-MM-dd").
                                format(mCalendarView.getSelectedCells().get(i).getTime()));


                        dates.add(new SimpleDateFormat("yyyy-MM-dd").
                                format(mCalendarView.getSelectedCells().get(i).getTime()));
                    }


                    if (dates.size() > 0 && dates.size() == mCalendarView.getSelectedCells().size()) {
                        getData(dates);
                    }
                } else {
                    Toast.makeText(context, "选择的日期不能为空", Toast.LENGTH_SHORT).show();
                }

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

    public static boolean isDateOneBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date dt1 = null;
        Date dt2 = null;
        try {
            dt1 = sdf.parse(str1);
            dt2 = sdf.parse(str2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (dt1.getTime() >= dt2.getTime()) {
            isBigger = true;
        } else if (dt1.getTime() < dt2.getTime()) {
            isBigger = false;
        }
        return isBigger;
    }


    private void getData(ArrayList<String> date) {
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

                                showDateDialogs(date, exchangeWorkLists);

                            }
                        }
                    }
                });
    }

    private void showDateDialogs(ArrayList<String> date, ArrayList<ClassListResponse> data) {
        View view = View.inflate(context, R.layout.dialog_show_arrange_time, null);
        TextView dialogTitle = (TextView) view.findViewById(R.id.tv_title);
        if (date.size() > 0) {
            dialogTitle.setText(date.get(0));
        }


        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.btn_delete).setVisibility(View.INVISIBLE);
        RecyclerView recycler = (RecyclerView) dialog.getWindow().findViewById(
                R.id.recycler_add_range);
        mAdapter = new ExchangeWorkAdapter(context, data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(mAdapter);

//只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//加上下面这一行弹出对话框时软键盘随之弹出
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());

        dialog.getWindow().findViewById(R.id.btn_save).setOnClickListener(v -> {
            for (int i = 0; i < date.size(); i++) {

                boolean checkDate = isDateOneBigger(date.get(i), new SimpleDateFormat("yyyy-MM-dd").
                        format(Calendar.getInstance().getTime()));
                if (checkDate) {
                    addProjectTime(data, date.get(i));
                }

            }

            dialog.dismiss();
        });


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
                                Log.d("ArrayList", "onNext: " + exchangeWorkLists.size() + "\n" +
                                        exchangeWorkLists.toString());
                                String mDate = new SimpleDateFormat("yyyy-MM-dd").
                                        format(date) + "";
                                if (!exchangeWorkLists.isEmpty()) {

                                    for (int i = 0; i < mDayMonthTimes.size(); i++) {
                                        if (mDayMonthTimes.get(i).getDate().equals(mDate.trim())) {
                                            serviceId = mDayMonthTimes.get(i).getId();

                                            for (int m = 0; m < mDayMonthTimes.get(i).getService().size(); m++) {
                                                for (int j = 0; j < exchangeWorkLists.size(); j++) {
                                                    if (mDayMonthTimes.get(i).getService().
                                                            get(m).getService_id() ==
                                                            exchangeWorkLists.get(j).getId()) {
                                                        exchangeWorkLists.get(j).setChecked(true);
                                                        exchangeWorkLists.get(j).setService_people(
                                                                Integer.parseInt(mDayMonthTimes.
                                                                        get(i).getService().
                                                                        get(m).getService_people()));
                                                        exchangeWorkLists.get(j).setTime(
                                                                mDayMonthTimes.get(i).getService().
                                                                        get(m).getTime());

                                                    }
                                                }
                                            }

                                        }

                                    }
                                    showDateDialog(mDate, exchangeWorkLists, serviceId);
                                }
                            }
                        }
                    }
                });


    }


    private void showDateDialog(String date, ArrayList<ClassListResponse> data, int serviceId) {
        View view = View.inflate(context, R.layout.dialog_show_arrange_time, null);
        TextView dialogTitle = (TextView) view.findViewById(R.id.tv_title);

        dialogTitle.setText(date);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();
        RecyclerView recycler = (RecyclerView) dialog.getWindow().findViewById(
                R.id.recycler_add_range);
        mAdapter = new ExchangeWorkAdapter(context, data);
        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
        recycler.setLayoutManager(layoutManager);
        recycler.setAdapter(mAdapter);

//只用下面这一行弹出对话框时需要点击输入框才能弹出软键盘
        dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
//加上下面这一行弹出对话框时软键盘随之弹出
        dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> dialog.dismiss());
        dialog.getWindow().findViewById(R.id.btn_delete).setOnClickListener(v -> {
            boolean checkDate = isDateOneBigger(date, new SimpleDateFormat("yyyy-MM-dd").
                    format(Calendar.getInstance().getTime()));

            if (checkDate) {
                deleteProjectTime(serviceId);
            } else {
                Toast.makeText(context, "过去的日期不能删除工时", Toast.LENGTH_SHORT).show();

            }

            dialog.dismiss();
        });
        dialog.getWindow().findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean checkDate = isDateOneBigger(date, new SimpleDateFormat("yyyy-MM-dd").
                        format(Calendar.getInstance().getTime()));
                if (checkDate) {
                    addProjectTime(data, date);

                } else {
                    Toast.makeText(context, "过去的日期不能修改工时", Toast.LENGTH_SHORT).show();
                }
                dialog.dismiss();
            }
        });


    }

    private void addProjectTime(ArrayList<ClassListResponse> data, String date) {
        ArrayList<Integer> ids = new ArrayList<>();
        ArrayList<Integer> people = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).isChecked() && data.get(i).getService_people() > 0) {
                ids.add(data.get(i).getId());

                people.add(data.get(i).getService_people());


            }
        }
        ProjectTimeRequest request = new ProjectTimeRequest(date, ids, people, projectId);
        mSubscription = mRemoteService.addMonthDay(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), request)
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
//                            Toast.makeText(ProjectTimeSettingActivity.this, "编辑成功", Toast.LENGTH_SHORT).show();
                            mCalendarView.getSelectedCells().clear();
                            mCalendarView.upCalendarView();
                            getProjectDayTime(projectId, new SimpleDateFormat("yyyy-MM").
                                    format(mCalendarView.getDate()) + "");
                            getProjectTime(projectId, new SimpleDateFormat("yyyy-MM").
                                    format(mCalendarView.getDate()) + "");
                        }

                    }
                });

    }

    private void deleteProjectTime(int serviceId) {
        mSubscription = mRemoteService.deleteMonthDay(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), serviceId)
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
                            Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                            getProjectTime(projectId, new SimpleDateFormat("yyyy-MM").
                                    format(mCalendarView.getDate()) + "");
                            getProjectDayTime(projectId, new SimpleDateFormat("yyyy-MM").
                                    format(mCalendarView.getDate()) + "");
                        }

                    }
                });


    }

    class ExchangeWorkAdapter extends RecyclerView.Adapter<ExchangeWorkAdapter.ExchangeWorkViewHolder> {
        private Context context;
        private ArrayList<ClassListResponse> mClassMeetingLists;

        public ExchangeWorkAdapter(Context context, ArrayList<ClassListResponse> mClassMeetingLists) {
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
                boolean checked;

                if (data != null) {
                    if (data.getServicesname().equals("常日班")){
                        checked=true;
                        if (data.getService_people()==0){
                            data.setService_people(servicePeople);
                        }
//                        data.setService_people(data.get);
                        holder.etNumber.setClickable(false);
                        holder.etNumber.setEnabled(false);

                    }else {
                        holder.etNumber.setClickable(true);
                        holder.etNumber.setEnabled(true);
                    }
                    holder.setItem(data);
                    Gson gson = new Gson();
                    Log.d("Gson", "onBindViewHolder数据: " + gson.toJson(data));
                    if (!TextUtils.isEmpty(data.getServicesname())) {
                        holder.itemName.setText(data.getServicesname());
                    }

                    if (data.isChecked()) {
                        holder.mCheckBox.setChecked(true);
                    } else {
                        holder.mCheckBox.setChecked(false);
                    }
                    if (data.getService_people() > 0) {
                        holder.etNumber.setText(String.valueOf(data.getService_people()));
                    }
                    holder.etNumber.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            if (!TextUtils.isEmpty(s.toString())) {
                                if (Integer.parseInt(
                                        holder.etNumber.getText().toString()) > 0) {
                                    data.setService_people(Integer.parseInt(
                                            holder.etNumber.getText().toString()));
                                }
                            } else {
                                data.setService_people(0);

                            }

                        }
                    });

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
            private RelativeLayout mRelativeLayout;
            private ClassListResponse mClassMeetingList;


            public ExchangeWorkViewHolder(View itemView) {

                super(itemView);

                mCheckBox = ButterKnife.findById(itemView, R.id.checkbox_password_remember);
                itemName = ButterKnife.findById(itemView, R.id.item_name);
                etNumber = ButterKnife.findById(itemView, R.id.btn_number);
                mRelativeLayout = ButterKnife.findById(itemView, R.id.rl_select_class_date);
                mCheckBox.setOnClickListener(this);
                mRelativeLayout.setOnClickListener(this);
                etNumber.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (!TextUtils.isEmpty(s.toString())) {
                            if (Integer.parseInt(
                                    etNumber.getText().toString()) > 0) {
                                mClassMeetingList.setService_people(Integer.parseInt(
                                        etNumber.getText().toString()));
                            }
                        } else {
                            mClassMeetingList.setService_people(0);

                        }

                    }
                });

            }

            public void setItem(ClassListResponse data) {
                mClassMeetingList = data;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.checkbox_password_remember:
                        if (mCheckBox.isChecked()) {
                            mClassMeetingList.setChecked(true);
                        } else {
                            mClassMeetingList.setChecked(false);
                        }
                        etNumber.requestFocus();
                        break;
                    case R.id.rl_select_class_date:
                        etNumber.requestFocus();

                        break;

                }


            }
        }
    }


    @Override
    public void onChange(Date date) {

    }

    public void onDateChange() {
        mCalendarView.getSelectedCells().clear();
        mCalendarView.upCalendarView();

        tvDate.setText(new SimpleDateFormat("yyyy年MM月").format(mCalendarView.getDate()));
        getProjectTime(projectId, new SimpleDateFormat("yyyy-MM").format(mCalendarView.getDate()));

        getProjectDayTime(projectId, new SimpleDateFormat("yyyy-MM").
                format(mCalendarView.getDate()));
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
