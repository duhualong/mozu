package org.eenie.wgj;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import com.facebook.drawee.backends.pipeline.Fresco;

import org.eenie.wgj.data.local.HomeModule;
import org.eenie.wgj.di.component.ApplicationComponent;
import org.eenie.wgj.di.component.DaggerApplicationComponent;
import org.eenie.wgj.di.module.ApplicationModule;
import org.eenie.wgj.realm.RealmController;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;

/**
 * App
 */
public class App extends Application {

  private ApplicationComponent mApplicationComponent;
  private static Application sApplicationContext;
  private static Stack<Activity> sActivityStack;

  private static RealmController realmController;


  RealmConfiguration.Builder builder;


  public static App get(Context context) {
    return (App)context.getApplicationContext();
  }

  @Override
  public void onCreate() {
    super.onCreate();
    mApplicationComponent = prepareApplicationComponent().build();
    mApplicationComponent.inject(this);
    sApplicationContext = this;
    realmController=new RealmController(getApplicationContext());
    sActivityStack = new Stack<>();
    Fresco.initialize(getApplicationContext());
    //sRefWatcher = LeakCanary.install(this);

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
//
//    Realm.init(this);
//    RealmConfiguration config = new  RealmConfiguration.Builder()
//            .name("myRealm.realm")
//            .deleteRealmIfMigrationNeeded()
//            .build();
//    Realm.setDefaultConfiguration(config);
  }

  private void saveModule(Realm realm) {
    List<HomeModule> mModules = new ArrayList<>();
    mModules.add(new HomeModule("ic_home_module_sign", "考勤", false, 0, true));
    mModules.add(new HomeModule( "ic_home_module_stati", "考勤统计", false, 1, true));
    mModules.add(new HomeModule("ic_home_module_polling", "巡检", false, 2, true));
    mModules.add(new HomeModule( "ic_home_module_polling_stati", "巡检统计", false, 3, true));
    mModules.add(new HomeModule("ic_home_module__form_up", "表单上传", false, 4, true));
    mModules.add(new HomeModule("ic_home_module_visitor", "访客通行", false, 5, true));
    mModules.add(new HomeModule( "ic_home_module_meeting", "会议", false, 6, true));
    mModules.add(new HomeModule( "ic_home_module_event_info", "项目设置", false, 7, true));
    mModules.add(new HomeModule("ic_home_module_fix", "维修", false, 10, false));
    mModules.add(new HomeModule( "ic_home_module_train", "培训", false, 11, false));
    mModules.add(new HomeModule("ic_home_module_workshow", "工作秀", false, 12, false));
    mModules.add(new HomeModule( "ic_home_module_report", "报岗", false, 13, false));
    mModules.add(new HomeModule("ic_home_module_reportstatistic", "报岗统计", false, 14, false));
    mModules.add(new HomeModule("ic_home_module_casual", "随手拍", false, 15, false));
    mModules.add(new HomeModule("ic_training_statistics", "培训统计", false, 16, false));
    realm.insertOrUpdate(mModules);
  }

  protected DaggerApplicationComponent.Builder prepareApplicationComponent() {
    return DaggerApplicationComponent.builder()
        .applicationModule(new ApplicationModule(this));
  }

  public ApplicationComponent getApplicationComponent() {
    return mApplicationComponent;
  }

  public static void addActivity(Activity activity) {
    if (activity != null) {
      sActivityStack.add(activity);
    }
  }

  public static RealmController getRealmController() {
    return realmController;
  }


  public static void clearStack() {
    if (sActivityStack != null && !sActivityStack.isEmpty()) {
      for (Activity activity : sActivityStack) {
        activity.finish();
      }
    }
  }
}
