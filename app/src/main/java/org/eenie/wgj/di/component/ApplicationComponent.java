package org.eenie.wgj.di.component;

import android.content.Context;
import android.webkit.WebView;

import org.eenie.wgj.App;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.base.BaseFragment;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.data.local.PreferencesHelper;
import org.eenie.wgj.data.local.UserDao;
import org.eenie.wgj.data.remote.HttpClient;
import org.eenie.wgj.data.remote.RemoteService;
import org.eenie.wgj.di.ApplicationContext;
import org.eenie.wgj.di.module.ApplicationModule;
import org.eenie.wgj.realm.RealmController;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Application component
 */
@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {
  void inject(App app);
  void inject(BaseActivity baseActivity);
  void inject(BaseFragment baseFragment);
  void inject(BaseSupportFragment supportFragment);

  @ApplicationContext
  Context context();
  RemoteService webService();
  PreferencesHelper prefsHelper();
  HttpClient httpClient();
  UserDao userDao();
  WebView webView();
  RealmController realmController();
}
