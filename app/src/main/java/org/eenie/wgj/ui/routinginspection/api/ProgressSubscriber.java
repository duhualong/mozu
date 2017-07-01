package org.eenie.wgj.ui.routinginspection.api;

import android.content.Context;
import android.widget.Toast;

import java.net.SocketTimeoutException;


import rx.Subscriber;


public abstract class ProgressSubscriber<T> extends Subscriber<T> implements ProgressCancelListener {

    private static final String TAG = "ProgressSubscriber";
    private ProgressDialogHandler mProgressDialogHandler;
    private Context context;
    public ProgressSubscriber(Context context) {

        this.context = context;
        mProgressDialogHandler = new ProgressDialogHandler(context, this, true);

    }
    private void showProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.SHOW_PROGRESS_DIALOG).sendToTarget();
        }
    }

    private void dismissProgressDialog(){
        if (mProgressDialogHandler != null) {
            mProgressDialogHandler.obtainMessage(ProgressDialogHandler.DISMISS_PROGRESS_DIALOG).sendToTarget();
            mProgressDialogHandler = null;
        }
    }

    @Override
    public void onStart() {
        showProgressDialog();
    }

    @Override
    public void onCompleted() {
        dismissProgressDialog();
    }

    @Override
    public void onError(Throwable e) {
        e.printStackTrace();
        dismissProgressDialog();
        if (e instanceof SocketTimeoutException) {
            Toast.makeText(context,"请求超时", Toast.LENGTH_LONG).show();
            return;
        }

        Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
    }

    @Override
    public abstract void onNext(T t);

    @Override
    public void onCancelProgress() {
        if (!this.isUnsubscribed()) {
            this.unsubscribe();
        }
    }
}
