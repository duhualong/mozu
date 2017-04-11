package org.eenie.wgj.data.remote;

import com.facebook.stetho.okhttp3.StethoInterceptor;

import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.RxUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Single;

/**
 * Custom OkHttp Client

 */
@Singleton public class HttpClient {

  private final OkHttpClient client;

  @Inject public HttpClient() {
    client = new OkHttpClient.Builder().connectTimeout(10, TimeUnit.SECONDS)
        .writeTimeout(10, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .addNetworkInterceptor(new StethoInterceptor())
        .build();
  }

  public void getData(String url, Callback callback) {

    Request request = new Request.Builder()
            .url(url).get().build();

    if (client != null) {
      client.newCall(request).enqueue(callback);
    }
  }
  public void getDatas(String url, Callback callback,String token) {

    Request request = new Request.Builder()
            .addHeader("token",token)
            .url(url).get().build();

    if (client != null) {
      client.newCall(request).enqueue(callback);
    }
  }



  public Single<Response> getData(String url) {
    return Single.create((Single.OnSubscribe<Response>) subscriber -> getData(url, new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        subscriber.onError(e);
      }
      @Override
      public void onResponse(Call call, Response response) throws IOException {
        subscriber.onSuccess(response);
      }
    })).compose(RxUtils.applySchedulers());
  }
  public Single<Response> getDatas(String url) {
    return Single.create((Single.OnSubscribe<Response>) subscriber -> getDatas(url, new Callback() {
      @Override
      public void onFailure(Call call, IOException e) {
        subscriber.onError(e);
      }
      @Override
      public void onResponse(Call call, Response response) throws IOException {
        subscriber.onSuccess(response);
      }
    }, Constant.TOKEN)).compose(RxUtils.applySchedulers());
  }
}
