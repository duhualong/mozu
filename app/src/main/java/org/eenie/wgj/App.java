package org.eenie.wgj;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

import org.eenie.wgj.data.local.HomeModule;
import org.eenie.wgj.di.component.ApplicationComponent;
import org.eenie.wgj.di.component.DaggerApplicationComponent;
import org.eenie.wgj.di.module.ApplicationModule;
import org.eenie.wgj.ui.attendance.AttendanceActivity;
import org.eenie.wgj.ui.attendancestatistics.AttendanceStatisticsActivity;
import org.eenie.wgj.ui.exchangeclass.ExchangeWorkClassSetting;
import org.eenie.wgj.ui.meeting.MeetingSettingActivity;
import org.eenie.wgj.ui.noticemessage.NoticeMessageActivity;
import org.eenie.wgj.ui.project.ProjectSettingActivity;
import org.eenie.wgj.ui.reportpost.ReportPostSettingUploadActivity;
import org.eenie.wgj.ui.reportpoststatistics.ReportPostStatisticsSettingActivity;
import org.eenie.wgj.ui.routinginspection.RoutingInspectionActivity;
import org.eenie.wgj.ui.routingstatistics.RoutingStatisticsSettingActivity;
import org.eenie.wgj.ui.takephoto.TakePhotoSettingActivity;
import org.eenie.wgj.ui.train.TrainStudySettingActivity;
import org.eenie.wgj.ui.trainstatistic.TrainingStatisticSettingActivity;
import org.eenie.wgj.ui.workshow.WorkShowListActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import cn.jpush.android.api.JPushInterface;
import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;


/**
 * App
 */
public class App extends MultiDexApplication {


    private ApplicationComponent mApplicationComponent;
    private static Stack<Activity> sActivityStack;
    public static Application sApplicationContext;

    private RealmConfiguration.Builder builder;


    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mApplicationComponent = prepareApplicationComponent().build();
        mApplicationComponent.inject(this);
        sApplicationContext = this;
        JPushInterface.init(this);
        sActivityStack = new Stack<>();
//        Fresco.initialize(getApplicationContext());
        initApplicationConfig();



    }
    @Override
    protected void attachBaseContext(Context base) {
        super .attachBaseContext(base);
        MultiDex.install(this);

    }




    private void initApplicationConfig() {
        builder = new RealmConfiguration.Builder(this);
        builder.name("youchi.realm")
                .migration(new RealmMigration() {
                    @Override
                    public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
                        realm.delete(HomeModule.class.getSimpleName());
                    }
                })
                .initialData(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        saveModule(realm);
                    }
                }).deleteRealmIfMigrationNeeded();
        Realm.setDefaultConfiguration(builder.build());
    }

    protected DaggerApplicationComponent.Builder prepareApplicationComponent() {
        return DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this));
    }
    private void saveModule(Realm realm) {
        List<HomeModule> mModules = new ArrayList<>();
        mModules.add(new HomeModule(AttendanceActivity.class, "ic_home_attendance", "考勤",
                false, 0, true));
        mModules.add(new HomeModule(AttendanceStatisticsActivity.class,
                "ic_home_attendance_statistics", "考勤统计", false, 1, true));
        mModules.add(new HomeModule(RoutingInspectionActivity.class, "ic_inspection", "巡检", false, 2, true));
        mModules.add(new HomeModule(RoutingStatisticsSettingActivity.class,
                "ic_inspection_statistics", "巡检统计", false, 3, true));
        mModules.add(new HomeModule(ExchangeWorkClassSetting.class, "ic_exchange_work_module", "交接班", false, 4, true));

        //mModules.add(new HomeModule("ic_home_module_form_up", "表单上传", false, 4, true));
        //mModules.add(new HomeModule("ic_visitor_pass", "访客通行", false, 5, true));
        mModules.add(new HomeModule(MeetingSettingActivity.class, "ic_meeting", "会议", false, 5, true));
        mModules.add(new HomeModule(NoticeMessageActivity.class,"ic_notice_module", "通告", false, 6, true));
        mModules.add(new HomeModule(ProjectSettingActivity.class, "ic_project_setting",
                "项目设置", false, 7, true));
        //mModules.add(new HomeModule("ic_repair", "维修", false, 10, false));
        mModules.add(new HomeModule(TrainStudySettingActivity.class, "ic_training", "培训", false, 10, false));
        mModules.add(new HomeModule(WorkShowListActivity.class, "ic_work_show", "工作秀", false, 11, false));
        mModules.add(new HomeModule(ReportPostSettingUploadActivity.class, "ic_submitted_post", "报岗", false, 12, false));
        mModules.add(new HomeModule(ReportPostStatisticsSettingActivity.class,
                "ic_submitted_post_statistics", "报岗统计", false, 13, false));
        mModules.add(new HomeModule(TakePhotoSettingActivity.class, "ic_snapshot", "随手拍", false, 14, false));
        mModules.add(new HomeModule(TrainingStatisticSettingActivity.class,
                "ic_training_statistics", "培训统计", false, 15, false));
        realm.insertOrUpdate(mModules);
    }



    public ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }


    public static void addActivity(Activity activity) {
        if (activity != null) {
            sActivityStack.add(activity);
        }
    }

    public static void clearStack() {
        if (sActivityStack != null && !sActivityStack.isEmpty()) {
            for (Activity activity : sActivityStack) {
                activity.finish();
            }
        }
    }
}
