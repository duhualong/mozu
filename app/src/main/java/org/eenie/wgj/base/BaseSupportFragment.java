package org.eenie.wgj.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.eenie.wgj.App;
import org.eenie.wgj.data.local.PreferencesHelper;
import org.eenie.wgj.data.local.UserDao;
import org.eenie.wgj.data.remote.RemoteService;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;

/**
 * Created by Eenie on 2017/4/5 at 10:30
 * Email: 472279981@qq.com
 * Des:
 */
public abstract class BaseSupportFragment extends Fragment {

    protected View mainView;
    protected FragmentManager fragmentMgr;
    protected Context context;

    private Unbinder unbinder;
    protected Subscription mSubscription;

    @Inject
    protected RemoteService mRemoteService;

    @Inject
    protected PreferencesHelper mPrefsHelper;

    @Inject
    protected UserDao mUserDao;

    protected ProgressDialog mProgressDialog;


    // 获取子类布局
    protected abstract int getContentView();

    // 更新页面ui
    protected abstract void updateUI();

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        if (getContentView() != 0) {
            mainView = inflater.inflate(getContentView(), container, false);
            unbinder = ButterKnife.bind(this, mainView);

            context = getActivity();
            fragmentMgr = getFragmentManager();

            App.get(getActivity()).getApplicationComponent().inject(this);

            updateUI();

            return mainView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        if (unbinder != null) {
            unbinder.unbind();
        }
        if (mProgressDialog != null) {
            mProgressDialog = null;
        }
    }

    @Override public void onDestroy() {
        super.onDestroy();
        //App.getRefWatcher().watch(this);
    }

    @Override public void onStop() {
        super.onStop();
        if (mProgressDialog != null) {
            hideProgress();
        }
        unSubscribe();
    }

    /**
     * Back press action in fragment
     */
    protected void onBackPressed() {
        if (fragmentMgr.getBackStackEntryCount() > 0) {
            fragmentMgr.popBackStack();
        } else {
            getActivity().onBackPressed();
        }
    }

    protected void showProgress(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
        }
        if (!TextUtils.isEmpty(message)) {
            mProgressDialog.setMessage(message);
        }
        mProgressDialog.show();
    }

    protected void showProgress(int msgResource) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(context);
        }
        if (msgResource != 0) {
            mProgressDialog.setMessage(getText(msgResource));
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