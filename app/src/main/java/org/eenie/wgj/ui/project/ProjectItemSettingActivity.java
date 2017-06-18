package org.eenie.wgj.ui.project;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.UserId;
import org.eenie.wgj.model.response.UserInforById;
import org.eenie.wgj.ui.project.attendance.AttendanceSettingActivity;
import org.eenie.wgj.ui.project.classmeeting.ClassMeetingSettingActivity;
import org.eenie.wgj.ui.project.exchangework.ExchangeWorkSettingActivity;
import org.eenie.wgj.ui.project.keypersonal.KeyPersonalSettingActivity;
import org.eenie.wgj.ui.project.projectpersonal.ProjectPersonalSetting;
import org.eenie.wgj.ui.project.projecttime.ProjectTotalTimeSettingActivity;
import org.eenie.wgj.ui.project.reportpost.ReportPostSettingActivity;
import org.eenie.wgj.ui.project.roundpoint.RoundPointSettingActivity;
import org.eenie.wgj.ui.project.roundway.RoundWaySettingActivity;
import org.eenie.wgj.ui.project.sortclass.SortClassSettingTestActivity;
import org.eenie.wgj.ui.project.workpost.WorkPostSettingActivity;
import org.eenie.wgj.ui.project.worktraining.WorkTrainingSettingActivity;
import org.eenie.wgj.util.Constants;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/17 at 11:29
 * Email: 472279981@qq.com
 * Des:
 */

public class ProjectItemSettingActivity extends BaseActivity {
    public static final String PROJECT_ID = "project_id";
    public static final String PROJECT_NAME = "project_name";
    private String projectId;
    private String projectName;
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private Intent mIntent;
    private String companyId;

    @Override
    protected int getContentView() {
        return R.layout.activity_project_item_setting;
    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);
        projectName = getIntent().getStringExtra(PROJECT_NAME);
        if (!TextUtils.isEmpty(projectName)) {
            tvTitle.setText(projectName);
        }
        getUserInfo();
    }


    private void getUserInfo() {
        UserId mUser = new UserId(mPrefsHelper.getPrefs().getString(Constants.UID, ""));

        mSubscription = mRemoteService.getUserInfoById(mPrefsHelper.getPrefs().getString(
                Constants.TOKEN, ""), mUser)
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
                        UserInforById mData = gson.fromJson(jsonArray,
                                new TypeToken<UserInforById>() {
                                }.getType());
                        if (mData != null) {
                            companyId = String.valueOf(mData.getCompanyid());

                        }
                    }
                });

    }

    @OnClick({R.id.img_back, R.id.rl_set_key_personal, R.id.rl_set_exchange_work, R.id.rl_set_job_training,
            R.id.rl_set_attendance, R.id.rl_set_security, R.id.rl_set_work_flights, R.id.rl_set_project_time,
            R.id.rl_set_arrange_schedual, R.id.rl_set_post, R.id.rl_set_report_post, R.id.rl_set_patrol_point,
            R.id.rl_set_patrol_road, R.id.rl_set_class_meeting, R.id.rl_set_project_personal})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:

                onBackPressed();
                break;
            case R.id.rl_set_project_personal:

                mIntent = new Intent(context, ProjectPersonalSetting.class);
                if (!TextUtils.isEmpty(projectId)) {
                    mIntent.putExtra(ProjectPersonalSetting.PROJECT_ID, projectId)
                    .putExtra(ProjectPersonalSetting.COMPANY_ID,companyId);
                }
                startActivity(mIntent);

                break;
            case R.id.rl_set_key_personal:
                mIntent = new Intent(context, KeyPersonalSettingActivity.class);
                if (!TextUtils.isEmpty(projectId)) {
                    mIntent.putExtra(KeyPersonalSettingActivity.PROJECT_ID, projectId);
                }
                startActivity(mIntent);

                break;
            case R.id.rl_set_exchange_work:

                mIntent = new Intent(context, ExchangeWorkSettingActivity.class);
                if (!TextUtils.isEmpty(projectId)) {
                    mIntent.putExtra(ExchangeWorkSettingActivity.PROJECT_ID, projectId);
                }
                startActivity(mIntent);

                break;
            case R.id.rl_set_job_training:

                mIntent = new Intent(context, WorkTrainingSettingActivity.class);
                if (!TextUtils.isEmpty(projectId)) {
                    mIntent.putExtra(ExchangeWorkSettingActivity.PROJECT_ID, projectId);
                }
                startActivity(mIntent);

                break;
            case R.id.rl_set_security:
                //临保

                break;
            case R.id.rl_set_work_flights:
                mIntent = new Intent(context, ClassMeetingSettingActivity.class);
                if (!TextUtils.isEmpty(projectId)) {
                    mIntent.putExtra(ClassMeetingSettingActivity.PROJECT_ID, projectId);
                }
                startActivity(mIntent);
                break;
            case R.id.rl_set_attendance:
                //考勤
                mIntent = new Intent(context, AttendanceSettingActivity.class);
                if (!TextUtils.isEmpty(projectId)) {
                    mIntent.putExtra(AttendanceSettingActivity.PROJECT_ID, projectId);
                }
                startActivity(mIntent);

                break;
            case R.id.rl_set_project_time:
                //项目工时
                mIntent = new Intent(context, ProjectTotalTimeSettingActivity.class);
                if (!TextUtils.isEmpty(projectId)) {
                    mIntent.putExtra(ProjectTotalTimeSettingActivity.PROJECT_ID, projectId);
                }
                startActivity(mIntent);

                break;
            case R.id.rl_set_arrange_schedual:
                //排班设置
                mIntent = new Intent(context, SortClassSettingTestActivity.class);
                if (!TextUtils.isEmpty(projectId)) {
                    mIntent.putExtra(SortClassSettingTestActivity.PROJECT_ID, projectId);
                }
                startActivity(mIntent);


                break;
            case R.id.rl_set_post:
                //岗位设置
                mIntent = new Intent(context, WorkPostSettingActivity.class);
                if (!TextUtils.isEmpty(projectId)) {
                    mIntent.putExtra(WorkPostSettingActivity.PROJECT_ID, projectId);
                }
                startActivity(mIntent);


                break;
            case R.id.rl_set_report_post:
                //报岗设置

                mIntent = new Intent(context, ReportPostSettingActivity.class);
                if (!TextUtils.isEmpty(projectId)) {
                    mIntent.putExtra(ReportPostSettingActivity.PROJECT_ID, projectId);
                }
                startActivity(mIntent);

                break;
            case R.id.rl_set_patrol_point:
                //巡检点设置
                mIntent = new Intent(context, RoundPointSettingActivity.class);
                if (!TextUtils.isEmpty(projectId)) {
                    mIntent.putExtra(RoundPointSettingActivity.PROJECT_ID, projectId);
                }
                startActivity(mIntent);


                break;
            case R.id.rl_set_patrol_road:
                //巡检线路设置
                mIntent = new Intent(context, RoundWaySettingActivity.class);
                if (!TextUtils.isEmpty(projectId)) {
                    mIntent.putExtra(RoundWaySettingActivity.PROJECT_ID, projectId);
                }
                startActivity(mIntent);
                break;
            case R.id.rl_set_class_meeting:

                break;

        }

    }
}
