package org.eenie.wgj.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.MainActivity;
import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseFragment;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.MLogin;
import org.eenie.wgj.model.response.LoginData;
import org.eenie.wgj.model.response.TestLogin;
import org.eenie.wgj.search.TestMyActivity;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.RxUtils;
import org.eenie.wgj.util.Utils;

import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Single;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * 登录页面
 */

public class LoginFragment extends BaseFragment {
    public static final String PHONE = "phone";
    private String mUsername;
    private boolean isLogin = false;
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.login_edit_input_phone)
    EditText inputPhone;
    @BindView(R.id.login_edit_input_password)
    EditText inputPassword;
    @BindView(R.id.checkbox_password_show_state)
    CheckBox checkboxPassword;
    @BindView(R.id.checkbox_password_remember)
    CheckBox passwordRemember;


    public static LoginFragment newInstance(String username) {
        LoginFragment fragment = new LoginFragment();
        if (!TextUtils.isEmpty(username)) {
            Bundle args = new Bundle();
            args.putString(PHONE, username);
            fragment.setArguments(args);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            mUsername = getArguments().getString(PHONE);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    protected int getContentView() {
        return R.layout.fragment_login;
    }

    @Override
    protected void updateUI() {
        if (!TextUtils.isEmpty(mPrefsHelper.getPrefs().getString(Constants.REGISTER_PHONE, ""))) {
            inputPhone.setText(mPrefsHelper.getPrefs().getString(Constants.REGISTER_PHONE, ""));
        }
        if (!TextUtils.isEmpty(mPrefsHelper.getPrefs().getString(Constants.REGISTER_PASSWORD, ""))) {
            inputPassword.setText(mPrefsHelper.getPrefs().getString(Constants.REGISTER_PASSWORD, ""));
        }
        if (!TextUtils.isEmpty(mUsername)) {
            inputPhone.setText(mUsername);
        } else {
            if (!TextUtils.isEmpty(mPrefsHelper.getPrefs().getString(Constants.PHONE, ""))) {
                inputPhone.setText(mPrefsHelper.getPrefs().getString(Constants.PHONE, ""));

            }

            if (mPrefsHelper.getPrefs().getBoolean(Constants.IS_LOGIN, false)) {
                //自动登录（已保存账号密码）
                startActivity(new Intent(context, MainActivity.class));
                getActivity().finish();
            }
        }

    }

    @OnClick({R.id.tv_forget_password, R.id.checkbox_password_remember,
            R.id.checkbox_password_show_state, R.id.btn_login, R.id.btn_register})
    public void onClick(View view) {
        String mPhone = inputPhone.getText().toString().trim();
        String mPassword = inputPassword.getText().toString().trim();

        switch (view.getId()) {
            case R.id.btn_login:


                boolean isChecked = checkInputContent(mPhone, mPassword);
                if (isChecked) {
                    //Checked("18817772486","111111");
                    checkedLogined(mPhone, mPassword);
                    //checkLogin(mPhone, mPassword);
                }


                break;
            case R.id.btn_register:

//                fragmentMgr.beginTransaction()
//                        .addToBackStack(TAG)
//                        .replace(R.id.fragment_login_container, new RegisterPersonalFirstFragment())
//                        .commit();
                startActivity(new Intent(context, TestMyActivity.class));

                break;
            case R.id.checkbox_password_show_state:
                Utils.setShowHide(checkboxPassword, inputPassword);


                break;
            case R.id.checkbox_password_remember:
                if (passwordRemember.isChecked()) {
                    isLogin = true;


                    //保存密码和账号
                } else {
                    //只保存手机号
                    isLogin = false;

                }

                break;
            case R.id.tv_forget_password:
                fragmentMgr.beginTransaction()
                        .addToBackStack(TAG)
                        .replace(R.id.fragment_login_container, new ForgetPasswordFragment())
                        .commit();

                break;
        }

    }

    private void Checked(String phone, String password) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://118.178.88.132:8000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("username", phone)
                .addFormDataPart("password", password)
                .build();
        Call<ApiResponse> call = userBiz.login(requestBody);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    Gson gson = new Gson();
                    Type objectType = new TypeToken<TestLogin>() {
                    }.getType();
                    TestLogin bean = gson.fromJson(response.body().getData().toString(), objectType);
                    System.out.println(bean.getToken());

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    public void checkedLogined(String phone, String password) {

        MLogin login = new MLogin(phone, Utils.md5(password));
        mSubscription = mRemoteService.logined(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("打印：dddd");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("打印eeee：" + e);

                    }

                    @Override
                    public void onNext(ApiResponse testLoginApiResponse) {
                        if (testLoginApiResponse.getResultCode() == 200) {

                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(testLoginApiResponse.getData());
                            LoginData data = gson.fromJson(jsonArray,
                                    new TypeToken<LoginData>() {
                                    }.getType());

                            mPrefsHelper.getPrefs().edit().putString(Constants.TOKEN, data.getToken())
                                    .putString(Constants.UID, data.getUser_id() + "")
                                    .putString(Constants.PHONE, phone)
                                    .putBoolean(Constants.IS_LOGIN, true).apply()
                            ;
                            System.out.println("uid" + data.getToken() + "\n" + data.getUser_id());
                            if (isLogin) {
                                mPrefsHelper.getPrefs().edit().putString(Constants.PASSWORD, password)
                                        .apply();
                            } else {
                                mPrefsHelper.getPrefs().edit().putString(Constants.PASSWORD, "")
                                        .apply();
                            }
                            Snackbar.make(rootView, "登陆成功，即将进入首页！", Snackbar.LENGTH_SHORT).show();

                            Single.just("").delay(2, TimeUnit.SECONDS).compose(RxUtils.applySchedulers()).
                                    subscribe(s ->
                                            startActivity(new Intent(context, MainActivity.class))
                                    );


                        } else {
                            Snackbar.make(rootView, testLoginApiResponse.getResultMessage(),
                                    Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    private void checkLogin(String phone, String password) {
        MLogin login = new MLogin(phone, password);
        mSubscription = mRemoteService.postLogin(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("ERROR:" + e);
                        Snackbar.make(rootView, "错误请求！", Snackbar.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode() == 200) {


                            Gson gson = new Gson();
                            String ss = gson.toJson(apiResponse.getData());
                            Type objectType = new TypeToken<TestLogin>() {
                            }.getType();
                            final TestLogin bean = gson.fromJson(ss, objectType);


                            Snackbar.make(rootView, "登陆成功，即将进入首页！", Snackbar.LENGTH_SHORT).show();
                        } else {
                            Snackbar.make(rootView, apiResponse.getResultMessage(), Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });
    }


    //检查输入内容（手机号码，密码）

    private boolean checkInputContent(String phone, String password) {
        boolean result = true;
        if (TextUtils.isEmpty(phone)) {

            Snackbar.make(rootView, "请输入手机号码！", Snackbar.LENGTH_LONG).show();
            result = false;

        }
        if (result && !Utils.isMobile(phone)) {
            Snackbar.make(rootView, "请输入正确的的手机号！", Snackbar.LENGTH_LONG).show();
            result = false;
        }
        if (result && TextUtils.isEmpty(password)) {
            Snackbar.make(rootView, "请输入密码！", Snackbar.LENGTH_SHORT).show();
            result = false;

        }
        if (result && (password.length() > 12) || ((password.length() > 0 && password.length() < 6))) {
            Snackbar.make(rootView, "输入密码长度必须是6~12位！", Snackbar.LENGTH_LONG).show();
            result = false;
        }

        return result;
    }


}
