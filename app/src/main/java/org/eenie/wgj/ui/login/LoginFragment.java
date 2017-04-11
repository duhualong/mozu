package org.eenie.wgj.ui.login;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.google.gson.Gson;

import org.eenie.wgj.MainActivity;
import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseFragment;
import org.eenie.wgj.data.remote.HttpClient;
import org.eenie.wgj.data.remote.RemoteService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.Mdata;
import org.eenie.wgj.model.response.Login;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.RxUtils;
import org.eenie.wgj.util.Utils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import rx.Single;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;

/**
 * 登录页面
 */

public class LoginFragment extends BaseFragment {
    private boolean isLogin=false;
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

    @Override
    protected int getContentView() {
        return R.layout.fragment_login;
    }

    @Override
    protected void updateUI() {
        if (!TextUtils.isEmpty(mPrefsHelper.getPrefs().getString(Constants.PHONE,""))){
            inputPhone.setText(mPrefsHelper.getPrefs().getString(Constants.PHONE,""));

        }

        if (mPrefsHelper.getPrefs().getBoolean(Constants.IS_LOGIN, false)) {
            //自动登录（已保存账号密码）
            startActivity(new Intent(context, MainActivity.class));
            getActivity().finish();
        }


    }

    @OnClick({R.id.tv_forget_password, R.id.checkbox_password_remember,
            R.id.checkbox_password_show_state, R.id.btn_login, R.id.btn_register})
    public void onClick(View view) {
        String mPhone = inputPhone.getText().toString().trim();
        String mPassword = inputPassword.getText().toString().trim();

        switch (view.getId()) {
            case R.id.btn_login:
               // testOther();
               // startActivity(new Intent(context, MainActivity.class));
                boolean isChecked = checkInputContent(mPhone, mPassword);
                if (isChecked) {
                    checkLogin(mPhone, mPassword);
                }
                break;
            case R.id.btn_register:



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


                break;
        }

    }

    private void testOther() {
        String tokenUrl = RemoteService.DOMAIN + "readilyShootList";
        HttpClient client = new HttpClient();
        client.getDatas(tokenUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                System.out.println("打印数据：" + "111111");
                String result = response.body().string();
                if (response.isSuccessful() && !TextUtils.isEmpty(result)) {

                    Gson gson = new Gson();
                    Mdata mdata = gson.fromJson(result, Mdata.class);
                    String code = mdata.getResultCode();
                    Log.d(TAG, "onResponse: " + code);
                }
//                if (response.isSuccessful()&&!TextUtils.isEmpty(result)){
//                    try {
//                        JSONObject jsonObject=new JSONObject(result);
//                      int code=  jsonObject.optInt("resultCode");
//                        String message=jsonObject.optString("resultMessage");
//                        System.out.println("result:"+code+",message"+message);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//
//
//                }

            }
        }, Constant.TOKEN);


        client.getData(tokenUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

            }
        });


//        mSubscription=mRemoteService.getList().subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread()).subscribe(new SingleSubscriber<ApiResponse<List<ShootList>>>() {
//                    @Override
//                    public void onSuccess(ApiResponse<List<ShootList>> data) {
//                        if (data.getResultCode()==0){
//                            System.out.println("测试数据："+data.getResultMessage().length());
//                        }
//
//                    }
//
//                    @Override
//                    public void onError(Throwable error) {
//
//                    }
//                });
    }

    private void checkLogin(String phone, String password) {

        mSubscription = mRemoteService.login(phone, password)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiResponse<Login>>() {
                    @Override
                    public void onSuccess(ApiResponse<Login> data) {
                        if (data.getResultCode() == 0&&data.getData()!= null) {
                            Login login = data.getData();
//                            mUserDao.initUserData(login);
                            Realm mRealm=Realm.getDefaultInstance();
                            mRealm.executeTransaction(realm -> realm.copyToRealm(login));

                            String token = login.getToken();
                            if (!TextUtils.isEmpty(token)) {
                                mPrefsHelper.getPrefs().edit().
                                        putString(Constants.TOKEN, token)
                                        .putBoolean(Constants.IS_LOGIN, isLogin)
                                        .putString(Constants.PHONE,phone)
                                        .apply();
                            }
                            Snackbar.make(rootView, "登陆成功，即将进入首页！", Snackbar.LENGTH_SHORT).show();
                            Single.just("").delay(2, TimeUnit.SECONDS).compose(RxUtils.applySchedulers()).subscribe(s -> {
                                startActivity(new Intent(context, MainActivity.class));
                               getActivity().finish();
                            });

                            System.out.println("token:" + token);
                            System.out.println("data数据：" + login);

                        }

                        System.out.println("打印code：" + data.getResultCode());

                    }

                    @Override
                    public void onError(Throwable error) {

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
