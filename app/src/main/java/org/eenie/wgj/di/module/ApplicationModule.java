package org.eenie.wgj.di.module;

import android.app.Application;
import android.content.Context;
import android.webkit.WebView;

import org.eenie.wgj.data.local.PreferencesHelper;
import org.eenie.wgj.data.remote.HttpClient;
import org.eenie.wgj.data.remote.RemoteService;
import org.eenie.wgj.di.ApplicationContext;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 */
@Module
public class ApplicationModule {

  protected  Application application;

  public ApplicationModule(@ApplicationContext Application application) {
    this.application = application;
  }

  @Provides
  @ApplicationContext
  Context provideContext() {
    return application;
  }


  @Provides @Singleton
  RemoteService provideWebService() {
    return new RemoteService.Creator().createService();
  }
//  @Provides @Singleton
//  UserDao provideUserDao() {
//    return new UserDao(application);
//  }


  @Provides @Singleton
  HttpClient provideHttpClient() {
    return new HttpClient();
  }

  @Provides @Singleton
  PreferencesHelper providePrefsHelper() {
    return new PreferencesHelper(application);
  }

//  @Provides @Singleton
//  RealmController provideRealmController() {
//    return new RealmController(application);
//  }
  @Provides @Singleton
  WebView provideWebView(@ApplicationContext Context context) {
    return new WebView(context);
  }

}







