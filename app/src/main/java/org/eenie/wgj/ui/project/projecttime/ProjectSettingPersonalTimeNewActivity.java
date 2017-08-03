package org.eenie.wgj.ui.project.projecttime;

import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.AddProjectDay;
import org.eenie.wgj.model.response.ClassListWorkTime;
import org.eenie.wgj.model.response.PersonalWorkDayMonthList;
import org.eenie.wgj.model.response.ProjectTimeTotal;
import org.eenie.wgj.model.response.TotalTimeProject;
import org.eenie.wgj.model.response.sortclass.ArrangeProjectDate;
import org.eenie.wgj.util.Constants;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/7/13 at 18:48
 * Email: 472279981@qq.com
 * Des:
 */

public class ProjectSettingPersonalTimeNewActivity extends BaseActivity {
    public static final String PROJECT_ID = "id";


    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.expand_list)
    ExpandableListView mExpandableListView;

    @BindView(R.id.tv_total_time)
    TextView tvTotalTime;
    @BindView(R.id.tv_surplus_time)
    TextView tvSuplusTime;
    @BindView(R.id.tv_total_people)
    TextView tvTotalPeople;
    @BindView(R.id.tv_people_now)
    TextView tvNowPeople;
    public ExpandAdapter adapter;

    private String projectId;
    private Calendar mCalendar;
    //该项目下当月的员工
    private ArrayList<ProjectTimeTotal.PersonBean> personBeanArrayList = new ArrayList<>();
    //该项目下的班次
    ArrayList<ClassListWorkTime.ServiceBean> classServiceList = new ArrayList<>();

    //已经排班的员工
    private ArrayList<PersonalWorkDayMonthList> arrangePersonalTime = new ArrayList<>();

    ArrayList<PersonalWorkDayMonthList> mData = new ArrayList<>();


    String attendanceDay;

    @Override
    protected int getContentView() {
        return R.layout.activity_personal_project_time;
    }

    @Override
    protected void updateUI() {

        projectId = getIntent().getStringExtra(PROJECT_ID);
        mCalendar = Calendar.getInstance();
        getData(mCalendar.getTime());


    }

    private void getData(Date time) {
        onMonthChange(time);
        mData = arrangePersonalTime;


    }

    private void onMonthChange(Date time) {
        String mDate = new SimpleDateFormat("yyyy-MM-dd").format(time);
        String date = new SimpleDateFormat("yyyy年MM月").format(time);
        tvDate.setText(date);
        getTotalTime(mDate);
        getSortClassList(mDate);


    }

    private void getArrangeTimePersonal(String projectId, String mDate) {
        mSubscription = mRemoteService.getMonthDay(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), mDate, projectId)
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
                            arrangePersonalTime = gson.fromJson(jsonArray,
                                    new TypeToken<ArrayList<PersonalWorkDayMonthList>>() {
                                    }.getType());
                            if (arrangePersonalTime != null && !arrangePersonalTime.isEmpty()) {
                                fillData();
                            } else {
                                fillDatas();
                            }


                        } else {
                            fillDatas();

                        }

                    }
                });

    }

    private void fillDatas() {

        if (personBeanArrayList.size() > 0) {
            ArrayList<PersonalWorkDayMonthList> mDataList = new ArrayList<>();


            for (int i = 0; i < personBeanArrayList.size(); i++) {
                if (classServiceList.size() > 0) {
                    ArrayList<ClassListWorkTime> serviceList = new ArrayList<>();
                    for (int j = 0; j < classServiceList.size(); j++) {
                        ClassListWorkTime.ServiceBean serviceBean
                                = new ClassListWorkTime.ServiceBean(classServiceList.get(j).getId(),
                                classServiceList.get(j).getServicesname(),
                                classServiceList.get(j).getStarttime(), classServiceList.get(j).getEndtime(),
                                classServiceList.get(j).getTime());
                        ClassListWorkTime service = new ClassListWorkTime(serviceBean, "0");
                        serviceList.add(service);
                    }
                    PersonalWorkDayMonthList mDatas = new PersonalWorkDayMonthList(
                            String.valueOf(personBeanArrayList.get(i).getId()),
                            personBeanArrayList.get(i).getName(), serviceList);
                    mDataList.add(mDatas);

                }
            }
            if (mDataList.size() > 0) {
                arrangePersonalTime = mDataList;
                Log.d("测试", "fillDatas: " + new Gson().toJson(mDataList));

            } else {
                mDataList = new ArrayList<>();
                System.out.println("这是假数据");
            }
            Log.d("测试", "fillDatas: " + new Gson().toJson(arrangePersonalTime));

            adapter = new ExpandAdapter(context, arrangePersonalTime);
            mExpandableListView.setAdapter(adapter);
        }


    }

    private void fillData() {
        if (arrangePersonalTime.size() > 0) {
            for (int m = 0; m < arrangePersonalTime.size(); m++) {
                for (int g = 0; g < classServiceList.size(); g++) {
                    boolean check = false;
                    for (int n = 0; n < arrangePersonalTime.get(m).getInfo().size(); n++) {
                        if (classServiceList.get(g).getId() == arrangePersonalTime.
                                get(m).getInfo().get(n).getService().getId()) {
                            check = true;
                        }


                    }
                    if (!check) {
                        arrangePersonalTime.get(m).getInfo().add(new ClassListWorkTime(new
                                ClassListWorkTime.ServiceBean(classServiceList.get(g).getId(),
                                classServiceList.get(g).getServicesname(),
                                classServiceList.get(g).getStarttime(), classServiceList.get(g).getEndtime(),
                                classServiceList.get(g).getTime()), "0"));

                    }
                }


            }
            if (personBeanArrayList.size() > 0) {
                for (int p = 0; p < personBeanArrayList.size(); p++) {
                    boolean checked = false;
                    for (int q = 0; q < arrangePersonalTime.size(); q++) {
                        if (String.valueOf(personBeanArrayList.get(p).getId()).
                                equals(arrangePersonalTime.get(q).getUser_id())) {
                            checked = true;
                        }

                    }
                    if (!checked) {
                        if (classServiceList.size() > 0) {
                            ArrayList<ClassListWorkTime> serviceList = new ArrayList<>();
                            for (int j = 0; j < classServiceList.size(); j++) {
                                ClassListWorkTime.ServiceBean serviceBean
                                        = new ClassListWorkTime.ServiceBean(classServiceList.get(j).getId(),
                                        classServiceList.get(j).getServicesname(),
                                        classServiceList.get(j).getStarttime(), classServiceList.get(j).getEndtime(),
                                        classServiceList.get(j).getTime());
                                ClassListWorkTime service = new ClassListWorkTime(serviceBean, "0");
                                serviceList.add(service);

                            }
                            PersonalWorkDayMonthList mDatas = new PersonalWorkDayMonthList(
                                    String.valueOf(personBeanArrayList.get(p).getId()),
                                    personBeanArrayList.get(p).getName(), serviceList);
                            arrangePersonalTime.add(mDatas);
                        }
                    }

                }
            }

            adapter = new ExpandAdapter(context, arrangePersonalTime);
            mExpandableListView.setAdapter(adapter);


        }


    }

    private void getSortPersonalList(String mDate, String projectId) {

        mSubscription = mRemoteService.getProjectTime(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                mDate, projectId)
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
                            if (data != null) {
                                attendanceDay = data.getHours().getAttendance();
                                tvTotalPeople.setText(String.valueOf(data.getHours().getApproved()));
                                tvNowPeople.setText(String.valueOf(data.getHours().getActual()));

                                personBeanArrayList = data.getPerson();
                                getArrangeTimePersonal(projectId, mDate);
                                Log.d("mytest", "onNext: " + gson.toJson(personBeanArrayList));

                            }
                        } else {
                            Toast.makeText(context, apiResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    private void getTotalTime(String time) {

        mSubscription = mRemoteService.getProjectTotalTime(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                projectId, time)
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
                            if (data != null) {
                                tvTotalTime.setText(data.getTotal());
                                tvSuplusTime.setText(data.getRemain());
                            }
                        } else {
//                            Toast.makeText(context, apiResponse.getMessage(),
//                                    Toast.LENGTH_SHORT).show();
                            tvTotalTime.setText("0");
                            tvSuplusTime.setText("0");
                        }


                    }
                });
    }

    private void getSortClassList(String date) {


        mSubscription = mRemoteService.getClassWideList(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), projectId)
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
                                classServiceList = gson.fromJson(jsonArray,
                                        new TypeToken<ArrayList<ClassListWorkTime.ServiceBean>>() {
                                        }.getType());
                                getSortPersonalList(date, projectId);
                                Log.d("班次数据", "onNext: " + gson.toJson(classServiceList));


                            }

                        }
                    }

                });
    }

    @OnClick({R.id.tv_save, R.id.img_back, R.id.btnPri, R.id.btnNext, R.id.tv_apply_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                applyData();

                break;
            case R.id.tv_apply_time:

                Toast.makeText(context, "申请成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.btnPri:
                mCalendar.add(Calendar.MONTH, -1);

                String mDate = new SimpleDateFormat("yyyy-MM").format(mCalendar.getTime());
                String mDates = new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime());
                if (isDateOneBigger(mDate, mDates)) {
                    mData = new ArrayList<>();

                    getData(mCalendar.getTime());
                } else {
                    mCalendar.add(Calendar.MONTH, 1);
                    Toast.makeText(context, "不能对过去日期进行工时设置", Toast.LENGTH_SHORT).show();
                }

//                if (Integer.parseInt(mDate.substring(0, 4)) > Integer.parseInt(mDates.substring(0, 4))) {
//                    mData=new ArrayList<>();
//
//                    getData(mCalendar.getTime());
////                    onMonthChange(mCalendar.getTime());
//                } else if (Integer.parseInt(mDate.substring(6, mDate.length())) >=
//                        Integer.parseInt(mDates.substring(6, mDates.length()))) {
//                    mData=new ArrayList<>();
//                    getData(mCalendar.getTime());
//                   // onMonthChange(mCalendar.getTime());
//                } else {
//
//                    mCalendar.add(Calendar.MONTH, 1);
//                }

                break;
            case R.id.btnNext:
                mData = new ArrayList<>();

                mCalendar.add(Calendar.MONTH, 1);

                getData(mCalendar.getTime());
                // onMonthChange(mCalendar.getTime());

                break;
        }
    }

    public static boolean isDateOneBigger(String str1, String str2) {
        boolean isBigger = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
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


    private void applyData() {
        ArrayList<Integer> userId = new ArrayList<>();
        ArrayList<String> serviceId = new ArrayList<>();
        ArrayList<String> dayList = new ArrayList<>();

        if (arrangePersonalTime != null) {
            for (int i = 0; i < arrangePersonalTime.size(); i++) {
                userId.add(Integer.valueOf(arrangePersonalTime.get(i).getUser_id()));
                String splitAdd = "";
                String day = "";
                for (int j = 0; j < arrangePersonalTime.get(i).getInfo().size(); j++) {
                    splitAdd = splitAdd + arrangePersonalTime.get(i).getInfo().get(j).getService().getId() + ",";
                    day = day + arrangePersonalTime.get(i).getInfo().get(j).getDay() + ",";

                }
                serviceId.add(splitAdd.substring(0, splitAdd.length() - 1));
                dayList.add(day.substring(0, day.length() - 1));
            }
            AddProjectDay addProject = new AddProjectDay(projectId, userId, serviceId, dayList,
                    new SimpleDateFormat("yyyy-MM-dd").format(mCalendar.getTime()));
            mSubscription = mRemoteService.addPersonalProjectDay(mPrefsHelper.getPrefs().
                    getString(Constants.TOKEN, ""), addProject)
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
                                Toast.makeText(context, apiResponse.getResultMessage(),
                                        Toast.LENGTH_LONG).show();
                                getTotalTime(new SimpleDateFormat("yyyy-MM-dd").format(mCalendar.getTime()));
                                getSortClassList(new SimpleDateFormat("yyyy-MM-dd").format(mCalendar.getTime()));


                            } else {
                                Toast.makeText(context, apiResponse.getResultMessage(),
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        }


    }


    class ExpandAdapter extends BaseExpandableListAdapter {
        private Context context;

        private ArrayList<PersonalWorkDayMonthList> mData;


        public ExpandAdapter(Context context, ArrayList<PersonalWorkDayMonthList> data) {
            this.context = context;
            mData = data;
        }


        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            GroupViewHolder gvh;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_expandable_listview, null);
                gvh = new GroupViewHolder();
                gvh.groupText = (TextView) convertView.findViewById(R.id.item_name);
                gvh.imgExpend = (ImageView) convertView.findViewById(R.id.img_expend);
                gvh.tvPosition=(TextView)convertView.findViewById(R.id.tv_position) ;
                gvh.tvPosition.setVisibility(View.VISIBLE);
                convertView.setTag(gvh);
            } else {
                gvh = (GroupViewHolder) convertView.getTag();
            }
            int mPosition=groupPosition+1;
            gvh.tvPosition.setText(mPosition+"");
            gvh.groupText.setText(mData.get(groupPosition).getUser_name());
            if (isExpanded) {
                gvh.imgExpend.setImageResource(R.mipmap.ic_expand);
            } else {
                gvh.imgExpend.setImageResource(R.mipmap.ic_collapse);
            }
            return convertView;
        }

        public class GroupViewHolder {
            TextView groupText;
            ImageView imgExpend;
            TextView tvPosition;
        }

        public void clear() {
            this.mData.clear();
            notifyDataSetChanged();
        }

        @Override
        public int getGroupCount() {
            return mData.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return mData.get(groupPosition).getInfo().size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mData.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mData.get(groupPosition).getInfo().get(childPosition);
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                 View convertView, ViewGroup parent) {
            ItemViewHolder ivh;

            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.item_expandable_item_new, null);
                ivh = new ItemViewHolder();
                ivh.itemText = (TextView) convertView.findViewById(R.id.expand_item_name);
                ivh.mEditText = (TextView) convertView.findViewById(R.id.et_input_day);
                ivh.mRelativeLayout = (RelativeLayout) convertView.findViewById(R.id.rl_expend_item);
                convertView.setTag(ivh);
            } else {
                ivh = (ItemViewHolder) convertView.getTag();
            }
            ivh.itemText.setText(mData.get(groupPosition).getInfo().
                    get(childPosition).getService().getServicesname());
            String day = mData.get(groupPosition).getInfo().get(childPosition).getDay();

            if (TextUtils.isEmpty(day)) {
                day = "0天";

            } else {
                day = day + "天";

            }
            ivh.mEditText.setText(day);

//            for (int q=0;q<minDay;q++){
//                stringList.add(String.valueOf(q));
//            }

            ivh.mRelativeLayout.setOnClickListener(v -> {
                mSubscription = mRemoteService.getMonthDayTime(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                        new SimpleDateFormat("yyyy-MM").format(mCalendar.getTime()), projectId)
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
                                if (apiResponse.getResultCode() == 200) {
                                    Gson gson = new Gson();
                                    String jsonArray = gson.toJson(apiResponse.getData());
                                    ArrayList<ArrangeProjectDate> arrangeProjectDates = gson.fromJson(jsonArray,
                                            new TypeToken<ArrayList<ArrangeProjectDate>>() {
                                            }.getType());


                                    if (arrangeProjectDates != null) {
                                        int mCount=0;
                                        for (int i = 0; i < arrangeProjectDates.size(); i++) {
                                            for (int k = 0; k < arrangeProjectDates.get(i).getService().size(); k++) {
                                                if (arrangeProjectDates.get(i).getService().get(k).getService_id() ==
                                                        mData.get(groupPosition).getInfo().get(childPosition).getService().getId()) {
                                                    mCount = mCount + Integer.valueOf(arrangeProjectDates.get(i).
                                                            getService().get(k).getService_people());

                                                }


                                            }
                                        }


                                        System.out.println("新的数据："+mCount);

                                        int otherDay = 0;
                                        for (int p = 0; p < mData.size(); p++) {
                                            for (int r = 0; r < mData.get(p).getInfo().size(); r++) {

                                                if (mData.get(p).getInfo().get(r).getService().getId() == mData.get(groupPosition).getInfo().
                                                        get(childPosition).getService().getId()) {
                                                    if (p != groupPosition) {
                                                        otherDay = otherDay + Integer.valueOf(mData.get(p).getInfo().get(r).getDay());
                                                    }

                                                }
                                            }
                                        }
                                        Log.d("数据", "getChildView: " + otherDay);
                                        int canDay = mCount - otherDay;
                                        if (canDay < 0) {
                                            canDay = 0;
                                        }

                                        int attendanceDayActual = Integer.parseInt(attendanceDay);
                                        int mDay = 0;
                                        for (int n = 0; n < mData.get(groupPosition).getInfo().size(); n++) {
                                            if (n != childPosition) {
                                                mDay = mDay + Integer.valueOf(mData.get(groupPosition).getInfo().get(n).getDay());

                                            }
                                        }
                                        int subAttendanceDay = attendanceDayActual - mDay;
                                        if (subAttendanceDay < 0) {
                                            subAttendanceDay = 0;
                                        }
                                        Log.d("tset", "getChildView: " + "数据：" + subAttendanceDay + "数据：" + canDay);
                                        int minDay = subAttendanceDay < canDay ? subAttendanceDay : canDay;
                                        List<String> stringList = new ArrayList<>();
                                        for (int q = 0; q <= minDay; q++) {
                                            stringList.add(String.valueOf(q));
                                        }

                                        //身高的dialog
                                        View view = View.inflate(context, R.layout.dialog_select_attendance_class_day, null);
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        final AlertDialog dialog = builder
                                                .setView(view) //自定义的布局文件
                                                .create();
                                        dialog.show();

                                        ListView listView = (ListView) dialog.getWindow().findViewById(R.id.dialog_recycler_view);


                                        ArrayAdapter<String> adapter = new ArrayAdapter<>
                                                (context, R.layout.list_item1, stringList);
                                        //获取ListView对象，通过调用setAdapter方法为ListView设置Adapter设置适配器
                                        listView.setAdapter(adapter);
                                        listView.setOnItemClickListener((parent1, view1, position, id) -> {
                                            dialog.dismiss();
                                            ivh.mEditText.setText(stringList.get(position) + "天");
                                            mData.get(groupPosition).getInfo().get(childPosition).setDay(stringList.get(position));


                                        });
                                        notifyDataSetChanged();

                                    }
                                }

                            }
                        });







            });


//            ivh.btnAdd.setOnClickListener(v -> {
//                int addDay = Integer.parseInt(mData.get(groupPosition).getInfo().
//                        get(childPosition).getDay()) + 1;
//                int mDay = 0;
//                for (int n = 0; n < mData.get(groupPosition).getInfo().size(); n++) {
//                    mDay = mDay + Integer.valueOf(mData.get(groupPosition).getInfo().get(n).getDay());
//                }
//                mDay = mDay + 1;
//                if (Integer.parseInt(attendanceDay) >= mDay) {
//                    ivh.mEditText.setText(addDay + "天");
//                    mData.get(groupPosition).getInfo().get(childPosition).setDay(String.valueOf(addDay));
//                } else {
//                    Toast.makeText(context, "设置班次天数，不能超过考勤天数", Toast.LENGTH_SHORT).show();
//                }
//
//
//            });

            return convertView;
        }

//        private void countClassAttendanceTotalDay(int id) {
//            mCount=0;
//            mSubscription = mRemoteService.getMonthDayTime(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
//                    new SimpleDateFormat("yyyy-MM").format(mCalendar.getTime()), projectId)
//                    .subscribeOn(Schedulers.io())
//                    .observeOn(AndroidSchedulers.mainThread())
//                    .subscribe(new Subscriber<ApiResponse>() {
//                        @Override
//                        public void onCompleted() {
//
//                        }
//
//                        @Override
//                        public void onError(Throwable e) {
//
//                        }
//
//                        @Override
//                        public void onNext(ApiResponse apiResponse) {
//                            if (apiResponse.getResultCode() == 200) {
//                                Gson gson = new Gson();
//                                String jsonArray = gson.toJson(apiResponse.getData());
//                                ArrayList<ArrangeProjectDate> arrangeProjectDates = gson.fromJson(jsonArray,
//                                        new TypeToken<ArrayList<ArrangeProjectDate>>() {
//                                        }.getType());
//
//
//                                if (arrangeProjectDates != null) {
//                                    for (int i = 0; i < arrangeProjectDates.size(); i++) {
//                                        for (int k = 0; k < arrangeProjectDates.get(i).getService().size(); k++) {
//                                            if (arrangeProjectDates.get(i).getService().get(k).getService_id() == id) {
//                                                mCount = mCount + Integer.valueOf(arrangeProjectDates.get(i).
//                                                        getService().get(k).getService_people());
//
//                                            }
//
//
//                                        }
//                                    }
//                                    System.out.println("新的数据："+mCount);
//                                }
//                            }
//
//                        }
//                    });
//
//
//        }

        public class ItemViewHolder {
            TextView itemText;

            TextView mEditText;
            RelativeLayout mRelativeLayout;

        }


    }


}
