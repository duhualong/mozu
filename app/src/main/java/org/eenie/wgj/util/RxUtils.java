package org.eenie.wgj.util;

import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Utilities for RxJava
 * Created by Zac on 2016/7/1.
 */
public class RxUtils {

  /**
   * Working with .compose(Transformer t) to apply schedulers.
   */
  public static <T> Single.Transformer<T, T> applySchedulers() {
    return tSingle -> tSingle.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread());
  }

}
