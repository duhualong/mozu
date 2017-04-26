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

import org.eenie.wgj.MainActivity;
import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseFragment;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.MLogin;
import org.eenie.wgj.model.response.TestLogin;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.Utils;

import butterknife.BindView;
import butterknife.OnClick;
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
                    checkedLogined(mPhone, mPassword);
                    //checkLogin(mPhone,mPassword);
                }

//                fragmentMgr.beginTransaction()
//                        .addToBackStack(TAG)
//                        .replace(R.id.fragment_login_container, new RegisterThirdFragment())
//                        .commit();
                break;
            case R.id.btn_register:

                fragmentMgr.beginTransaction()
                        .addToBackStack(TAG)
                        .replace(R.id.fragment_login_container, new RegisterFirstFragment())
                        .commit();

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

    public void checkedLogined(String phone, String password) {
        MLogin login=new MLogin(phone,password);
        mSubscription = mRemoteService.logined(login)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse<TestLogin>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ApiResponse<TestLogin> testLoginApiResponse) {
                        if (testLoginApiResponse.getResultCode()==200){
                            TestLogin data=testLoginApiResponse.getData();
                            System.out.println(data.getToken());
                            Snackbar.make(rootView, "登陆成功，即将进入首页！", Snackbar.LENGTH_SHORT).show();
                            startActivity(new Intent(context,MainActivity.class));
                        }

                    }
                });
    }

    private void checkLogin(String phone, String password) {


        mSubscription = mRemoteService.postLogin(phone, password)
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
                            TestLogin login = (TestLogin) apiResponse.getData();
                            login.getToken();
                            Snackbar.make(rootView, "登陆成功，即将进入首页！" + login.getToken(), Snackbar.LENGTH_SHORT).show();
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
