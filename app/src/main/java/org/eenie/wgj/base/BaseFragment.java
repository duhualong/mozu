package org.eenie.wgj.base;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.eenie.wgj.App;
import org.eenie.wgj.data.local.PreferencesHelper;
import org.eenie.wgj.data.remote.RemoteService;

import javax.inject.Inject;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import rx.Subscription;

/**
 *
 */
public abstract class BaseFragment extends Fragment {



    protected View rootView;
    protected FragmentManager fragmentMgr;
    protected Context context;
    @Inject
    protected RemoteService mRemoteService;
    @Inject
    protected PreferencesHelper mPrefsHelper;
//    @Inject
//    protected UserDao mUserDao;
    protected Subscription mSubscription;

    // 获取子类布局
    protected abstract int getContentView();
    private Unbinder unbinder;
    private ProgressDialog mProgressDialog;

    // 更新页面ui
    protected abstract void updateUI();

    @Override public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                       Bundle savedInstanceState) {
        if (getContentView() != 0) {
            rootView = inflater.inflate(getContentView(), container, false);
            unbinder = ButterKnife.bind(this, rootView);

            context = getActivity();

            fragmentMgr = getFragmentManager();

            App.get(getActivity()).getApplicationComponent().inject(this);

            updateUI();

            return rootView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override public void onStop() {
        super.onStop();
        hideProgress();
        unSubscribe();
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (mProgressDialog != null) {
            mProgressDialog = null;
        }
    }

    @Override public void onDestroy() {
        super.onDestroy();
        //App.getRefWatcher().watch(this);
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

    protected void showProgress(CharSequence message ) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
        }
        mProgressDialog.setMessage(message);
        mProgressDialog.show();
    }

    protected void showProgress(int messageRes ) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
        }
        mProgressDialog.setMessage(getString(messageRes));
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

    protected void addFragment(int containerId, Fragment fragment) {
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        if (getFragmentManager().findFragmentById(containerId) == null) {
            transaction.add(containerId, fragment).commit();
        } else {
            transaction.replace(containerId, fragment).commit();
        }
    }






}
