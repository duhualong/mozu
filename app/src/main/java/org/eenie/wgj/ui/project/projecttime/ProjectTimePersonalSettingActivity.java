package org.eenie.wgj.ui.project.projecttime;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
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
import org.eenie.wgj.util.Constants;

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
 * Created by Eenie on 2017/6/1 at 18:59
 * Email: 472279981@qq.com
 * Des:
 */

public class ProjectTimePersonalSettingActivity extends BaseActivity {
    public static final String PROJECT_ID = "id";
    @BindView(R.id.tv_date)
    TextView tvDate;
    @BindView(R.id.expand_list)
    ExpandableListView mExpandableListView;
    private String projectId;
    private Calendar mCalendar;
    @BindView(R.id.tv_total_time)
    TextView tvTotalTime;
    @BindView(R.id.tv_surplus_time)
    TextView tvSuplusTime;
    @BindView(R.id.tv_total_people)
    TextView tvTotalPeople;
    @BindView(R.id.tv_people_now)
    TextView tvNowPeople;
    public ExpandAdapter adapter;
    public List<ArrayList<ClassListWorkTime>> itemListDatas = new ArrayList<>();
    private ArrayList<PersonalWorkDayMonthList> personalData = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_personal_project_time;
    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);
        mCalendar = Calendar.getInstance();
        onMonthChange(mCalendar.getTime());
    }

    private void onMonthChange(Date time) {
        String mDate = new SimpleDateFormat("yyyy-MM").format(time);
        String date = new SimpleDateFormat("yyyy年MM月").format(time);
        tvDate.setText(date);
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
                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(apiResponse.getData());
                        ProjectTimeTotal data = gson.fromJson(jsonArray,
                                new TypeToken<ProjectTimeTotal>() {
                                }.getType());
                        if (data != null) {
                            tvTotalTime.setText(String.valueOf(data.getHours().getWorkinghours()));
                            tvSuplusTime.setText(String.valueOf(data.getHours().getRemaininghours()));
                            tvTotalPeople.setText(String.valueOf(data.getHours().getApproved()));
                            tvNowPeople.setText(String.valueOf(data.getHours().getActual()));
                            List<ProjectTimeTotal.PersonBean> personBeanArrayList = data.getPerson();

                            getWorkingHoursList(mDate, personBeanArrayList);


                        }

                    }
                });


    }

    private void getWorkingHoursList(String date, List<ProjectTimeTotal.PersonBean> mData) {
        mSubscription = mRemoteService.getMonthDay(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), date, projectId)
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
                            mExpandableListView.setVisibility(View.VISIBLE);

                            System.out.println("cout" + mData.size());
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            personalData = gson.fromJson(jsonArray,
                                    new TypeToken<ArrayList<PersonalWorkDayMonthList>>() {
                                    }.getType());
                            if (personalData.size() > 0) {
                                adapter = new ExpandAdapter(context, personalData);
                                mExpandableListView.setAdapter(adapter);

                            } else {
                                getClassList(mData);
                            }


                        } else {

                            mExpandableListView.setVisibility(View.INVISIBLE);
                            personalData = null;
                            Toast.makeText(context, apiResponse.getResultMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void getClassList(List<ProjectTimeTotal.PersonBean> mPersonal) {
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
                            ArrayList<ClassListWorkTime> mClass = new ArrayList<>();
                            System.out.println("Mcout" + mPersonal.size());
                            if (apiResponse.getData() != null) {
                                Gson gson = new Gson();
                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<ClassListWorkTime.ServiceBean> exchangeWorkLists =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<ArrayList<ClassListWorkTime.ServiceBean>>() {
                                                }.getType());

                                if (exchangeWorkLists != null && !exchangeWorkLists.isEmpty()) {
                                    if (mPersonal.size() > 0) {
                                        for (int i = 0; i < exchangeWorkLists.size(); i++) {
                                            ClassListWorkTime classListWorkTime =
                                                    new ClassListWorkTime(exchangeWorkLists.get(i), "0");
                                            mClass.add(classListWorkTime);
                                        }

                                        for (int j = 0; j < mPersonal.size(); j++) {

                                            PersonalWorkDayMonthList mPersonalDay = new
                                                    PersonalWorkDayMonthList(String.valueOf(mPersonal.get(j).getId()),
                                                    mPersonal.get(j).getName(), mClass);
                                            personalData.add(mPersonalDay);

                                        }
                                        adapter = new ExpandAdapter(context, personalData);
                                        mExpandableListView.setAdapter(adapter);
                                    } else {


                                    }


                                }

                            }

                        }
                    }

                });
    }

    @OnClick({R.id.tv_save, R.id.img_back, R.id.btnPri, R.id.btnNext,R.id.tv_apply_time})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_save:
                applyData();

                break;
            case R.id.tv_apply_time:

                Toast.makeText(context,"申请成功",Toast.LENGTH_SHORT).show();
                break;
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.btnPri:
                mCalendar.add(Calendar.MONTH, -1);
                onMonthChange(mCalendar.getTime());
                break;
            case R.id.btnNext:
                mCalendar.add(Calendar.MONTH, 1);
                onMonthChange(mCalendar.getTime());

                break;
        }
    }

    private void applyData() {
        ArrayList<Integer> userId = new ArrayList<>();
        ArrayList<String> serviceId = new ArrayList<>();
        ArrayList<String> dayList = new ArrayList<>();
        if (personalData != null) {
            for (int i = 0; i < personalData.size(); i++) {
                userId.add(Integer.valueOf(personalData.get(i).getUser_id()));
                String splitAdd = "";
                String day = "";
                for (int j = 0; j < personalData.get(i).getInfo().size(); j++) {
                    splitAdd = splitAdd + personalData.get(i).getInfo().get(j).getService().getId() + ",";
                    day = day + personalData.get(i).getInfo().get(j).getDay() + ",";

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
                                ProjectTimePersonalSettingActivity.this.finish();
                            } else {
                                Toast.makeText(context, apiResponse.getResultMessage(),
                                        Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        }


        Gson gson = new Gson();

        Log.d("Mdata", "applyData: " + gson.toJson(personalData));

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
                convertView.setTag(gvh);
            } else {
                gvh = (GroupViewHolder) convertView.getTag();
            }
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
                convertView = LayoutInflater.from(context).inflate(R.layout.item_exoandable_item, null);
                ivh = new ItemViewHolder();
                ivh.itemText = (TextView) convertView.findViewById(R.id.expand_item_name);
                ivh.btnAdd = (TextView) convertView.findViewById(R.id.button_add);
                ivh.btnSub = (TextView) convertView.findViewById(R.id.button_sub);
                ivh.mEditText = (TextView) convertView.findViewById(R.id.et_input_day);
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
            ivh.btnAdd.setOnClickListener(v -> {
                int addDay = Integer.parseInt(mData.get(groupPosition).getInfo().
                        get(childPosition).getDay()) + 1;
                ivh.mEditText.setText(addDay + "天");
                mData.get(groupPosition).getInfo().get(childPosition).setDay(String.valueOf(addDay));


            });
            ivh.btnSub.setOnClickListener(v -> {
                int subDay = Integer.parseInt(mData.get(groupPosition).getInfo().
                        get(childPosition).getDay()) - 1;

                if (subDay <= 0) {
                    ivh.mEditText.setText("0天");
                    mData.get(groupPosition).getInfo().get(childPosition).setDay(String.valueOf("0"));
                } else {
                    ivh.mEditText.setText(subDay + "天");
                    mData.get(groupPosition).getInfo().get(childPosition).setDay(String.valueOf(subDay));
                }

            });

            return convertView;
        }

        public class ItemViewHolder {
            TextView itemText;
            TextView btnAdd;
            TextView btnSub;
            TextView mEditText;

        }


    }

}
