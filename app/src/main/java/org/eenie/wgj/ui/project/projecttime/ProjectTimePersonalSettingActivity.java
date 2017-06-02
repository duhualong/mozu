package org.eenie.wgj.ui.project.projecttime;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.ProjectTimeTotal;
import org.eenie.wgj.util.Constants;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    @BindView(R.id.expand_list)ExpandableListView mExpandableListView;
    private String projectId;
    private Calendar mCalendar;
    @BindView(R.id.tv_total_time)TextView tvTotalTime;
    @BindView(R.id.tv_surplus_time)TextView tvSuplusTime;
    @BindView(R.id.tv_total_people)TextView tvTotalPeople;
    @BindView(R.id.tv_people_now)TextView tvNowPeople;
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
        tvDate.setText(new SimpleDateFormat("yyyy年MM月").format(time));
        mSubscription = mRemoteService.getProjectTime(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                new SimpleDateFormat("yyyy-MM").format(time), projectId)
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
                        if (data!=null){
                            tvTotalTime.setText(String.valueOf(data.getHours().getWorkinghours()));
                            tvSuplusTime.setText(String.valueOf(data.getHours().getRemaininghours()));
                            tvTotalPeople.setText(String.valueOf(data.getHours().getApproved()));
                            tvNowPeople.setText(String.valueOf(data.getHours().getActual()));


                        }

                    }
                });


    }

    @OnClick({R.id.tv_save, R.id.img_back, R.id.btnPri, R.id.btnNext})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_save:

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
    class ProjectExpandableListView extends BaseExpandableListAdapter {



        @Override
        public int getGroupCount() {
            return 0;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return 0;
        }

        @Override
        public Object getGroup(int groupPosition) {
            return null;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return null;
        }

        @Override
        public long getGroupId(int groupPosition) {
            return 0;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return 0;
        }

        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            return null;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return false;
        }
    }

}
