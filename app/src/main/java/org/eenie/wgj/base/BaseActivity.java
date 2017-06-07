package org.eenie.wgj.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import org.eenie.wgj.App;
import org.eenie.wgj.data.local.PreferencesHelper;
import org.eenie.wgj.data.remote.HttpClient;
import org.eenie.wgj.data.remote.RemoteService;

import javax.inject.Inject;

import butterknife.ButterKnife;
import rx.Subscription;

/**
 * Base Activity
 */
public abstract class BaseActivity extends AppCompatActivity {

  private static final String ACTION_NETWORK_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
  private static final String ACTION_NEW_VERSION = "apk.update.action";

  protected abstract int getContentView();

  protected abstract void updateUI();

  private InputMethodManager inputMgr;
  protected FragmentManager fragmentMgr;
  protected android.support.v4.app.FragmentManager supportFragmentMgr;
  protected Context context;

  protected Subscription mSubscription;
//  @Inject
//  protected UserDao mUserDao;
  private ProgressDialog mProgressDialog;

  @Inject protected RemoteService mRemoteService;
  @Inject protected HttpClient mHttpClient;
  @Inject protected PreferencesHelper mPrefsHelper;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (getContentView() != 0) {
      setContentView(getContentView());
    }
    ButterKnife.bind(this);

    App.get(this).getApplicationComponent().inject(this);

    setBaseFeatures();

    App.addActivity(this);

    updateUI();
  }

  @Override
  protected void onStop() {
    super.onStop();
    hideProgress();
    unSubscribe();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    if (mProgressDialog != null) {
      mProgressDialog = null;
    }
    //App.getRefWatcher().watch(this);
  }

  /**
   * 点击非EditText区域自动隐藏软键盘
   */
  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (inputMgr.isActive() && getCurrentFocus() != null) {
      if (getCurrentFocus().getWindowToken() != null) {
        inputMgr.hideSoftInputFromWindow(getCurrentFocus().getApplicationWindowToken(),
            InputMethodManager.HIDE_NOT_ALWAYS);
      }
    }
    return super.onTouchEvent(event);
  }

  protected void setBaseFeatures() {
    inputMgr = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

    fragmentMgr = getFragmentManager();
    supportFragmentMgr = getSupportFragmentManager();

    context = this;
  }

  /**
   * Add fragment to activity container
   *
   * @param containerId container id
   * @param fragment fragment to add to activity.
   */
  protected void addFragment(int containerId, Fragment fragment) {
    fragmentMgr.beginTransaction().add(containerId, fragment).commit();
  }

  /**
   * Fix fragment addToBackStack() not working
   */
  @Override
  public void onBackPressed() {
    if (fragmentMgr.getBackStackEntryCount() > 0) {
      fragmentMgr.popBackStack();
    } else {
      super.onBackPressed();
    }
  }

  /**
   * Show Toast
   *
   * @param msg show message in the toast
   */
  protected void showToast(String msg) {
    Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
  }

  protected void showProgress(CharSequence message) {
    if (mProgressDialog == null) {
      mProgressDialog = new ProgressDialog(context);
    }
    mProgressDialog.setMessage(message);
    mProgressDialog.show();
  }

  protected void showProgress(int messageRes) {
    if (mProgressDialog == null) {
      mProgressDialog = new ProgressDialog(context);
    }
    if (messageRes != 0) {
      mProgressDialog.setMessage(getString(messageRes));
    }
    mProgressDialog.show();
  }

  protected void hideProgress() {
    if (mProgressDialog != null) {
      mProgressDialog.cancel();
    }
  }

  protected void unSubscribe() {
    if (mSubscription != null && !mSubscription.isUnsubscribed()) {
      mSubscription.unsubscribe();
    }
  }
}
