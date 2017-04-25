package org.eenie.wgj.ui.login;

import android.annotation.SuppressLint;
import android.os.CountDownTimer;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseFragment;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.Token;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.RxUtils;
import org.eenie.wgj.util.Utils;

import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Single;
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.content.ContentValues.TAG;
import static org.eenie.wgj.util.Constants.COUNT_UNIT;
import static org.eenie.wgj.util.Constants.NUM_COUNTDOWN;

/**
 * Created by Eenie on 2017/4/24 at 10:56
 * Email: 472279981@qq.com
 * Des:
 */

public class ForgetPasswordFragment extends BaseFragment {
    private CountDownTimer timer;
    private boolean isCounting;
    @BindView(R.id.register_fetch_code_button)Button btnSendCaptcha;
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.login_edit_input_phone)
    EditText inputPhone;
    @BindView(R.id.register_code_edit_text)EditText inputCaptcha;
    @BindView(R.id.login_edit_input_password)EditText inputPassword;
    @BindView(R.id.login_edit_input_password_twice)EditText inputRePassword;
    @BindView(R.id.checkbox_password_show_state)CheckBox checkboxFirst;
    @BindView(R.id.checkbox_password_show_state_twice)CheckBox checkboxTwice;

    @Override
    protected int getContentView() {
        return R.layout.fragment_forget_password;
    }

    @Override
    protected void updateUI() {

    }

    @OnClick({R.id.register_fetch_code_button, R.id.register_submit_button,
            R.id.checkbox_password_show_state, R.id.checkbox_password_show_state_twice,R.id.img_back})
    public void onClick(View view) {
        String mPhone=inputPhone.getText().toString();
        String mCaptcha=inputCaptcha.getText().toString();
        String mPassword=inputPassword.getText().toString();
        String mRePassword=inputRePassword.getText().toString();

        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();

                break;
            case R.id.register_fetch_code_button:
                if (checkPhone(mPhone)){
                    getCaptcha(mPhone);
                }
                break;

            case R.id.register_submit_button:
               if (checkboxModifyPassword(mPhone,mCaptcha,mPassword,mRePassword)){

                   //调用修改密码的接口

                   modifyPassword(mPhone,mCaptcha,mPassword);



               }


                break;
            case R.id.checkbox_password_show_state:
                Utils.setShowHide(checkboxFirst, inputPassword);

                break;
            case R.id.checkbox_password_show_state_twice:

                Utils.setShowHide(checkboxTwice, inputRePassword);
                break;
        }

    }

    private void modifyPassword(String mPhone, String mCaptcha, String mPassword) {
        mSubscription=mRemoteService.modifyPassword(mCaptcha,mPassword,mPhone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Snackbar.make(rootView,"请求错误！",Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {
                            if (apiResponse.getResultCode()==200){
                                showModifyDialog(mPhone);

                            }else {
                                Snackbar.make(rootView,apiResponse.getResultMessage(),Snackbar.LENGTH_LONG).show();
                            }

                        }

                });

    }

    private void showModifyDialog(String phone) {
        View view = View.inflate(context, R.layout.dialog_modify_password, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.btn_login).setOnClickListener(v -> {
            Single.just("").delay(1, TimeUnit.SECONDS).compose(RxUtils.applySchedulers()).
                    subscribe(s -> {
                        dialog.dismiss();
                        fragmentMgr.beginTransaction()
                                .addToBackStack(TAG)
                                .replace(R.id.fragment_login_container, LoginFragment.newInstance(phone))
                                .commit();

                    });


        });




    }

    private boolean checkPhone(String mPhone) {
        boolean result=true;
        if (TextUtils.isEmpty(mPhone)){
            result=false;
            inputPhone.setError("输入的手机号码不能为空!");

        }
        if (result&&!Utils.isMobile(mPhone)){
            Snackbar.make(rootView,"请输入正确的手机号码！",Snackbar.LENGTH_LONG).show();
            result=false;

        }
        return result;

    }


    private void getCaptcha(String mPhone) {
        mSubscription=mRemoteService.fetchMessageCode(mPhone)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiResponse<Token>>() {
                    @Override
                    public void onSuccess(ApiResponse<Token> value) {
                        if (value.getResultCode()==200){
                            TimeDown();
                            mPrefsHelper.getPrefs().edit().
                                    putString(Constants.TOKEN, value.getData().getToken()).apply();

                        }else {
                            Snackbar.make(rootView,"获取验证码失败,请检查手机状态！",Snackbar.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(Throwable error) {
                        Snackbar.make(rootView,"获取验证码失败！",Snackbar.LENGTH_LONG).show();

                    }
                });


    }

    //倒计时
    private void TimeDown() {
        //倒计时开始
        timer = new CountDownTimer(NUM_COUNTDOWN,COUNT_UNIT) {
            @Override
            public void onTick(long l) {
                String info = "重新发送" + l / 1000 + "S";
                @SuppressLint("StringFormatMatches") String count =
                        getString(R.string.find_passwd_send_countdown, l / 1000);
                if (btnSendCaptcha != null && !TextUtils.isEmpty(info)) {
                    btnSendCaptcha.setText(count);
                }
            }

            @Override
            public void onFinish() {
                btnSendCaptcha.setText(R.string.btn_code);
                btnSendCaptcha.setEnabled(true);
                isCounting = false;
            }
        };
        timer.start();
        isCounting = true;
        btnSendCaptcha.setEnabled(false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (timer != null) {
            timer.cancel();
        }
        isCounting = false;
    }



    private boolean checkboxModifyPassword(String phone,String captcha,String password,
                                          String rePassword) {
        boolean mResult=true;
        if (TextUtils.isEmpty(phone)){
            Snackbar.make(rootView,"请填写手机号码！",Snackbar.LENGTH_LONG).show();
            mResult=false;
        }
        if (mResult&&!Utils.isMobile(phone)){
            Snackbar.make(rootView,"请填写正确的手机号码！",Snackbar.LENGTH_LONG).show();
            mResult=false;
        }
        if (mResult&&TextUtils.isEmpty(captcha)){
            mResult=false;
            Snackbar.make(rootView,"请填写验证码",Snackbar.LENGTH_LONG).show();
        }
        if (mResult&&(captcha.length()>6||(captcha.length()>0&&captcha.length()<4))){
            mResult=false;
            Snackbar.make(rootView,"请输入4~6位的验证码！",Snackbar.LENGTH_LONG).show();
        }
        if (mResult){

        }
        if (mResult&&TextUtils.isEmpty(password)){
            mResult=false;
            Snackbar.make(rootView,"设置的密码不能为空！",Snackbar.LENGTH_LONG).show();
        }
        if (mResult&&(password.length()>12||(password.length()>0&&password.length()<6))){
            mResult=false;
            Snackbar.make(rootView,"设置的密码长度必须在6~12为之间！",Snackbar.LENGTH_LONG).show();
        }

        if (mResult&&!password.equals(rePassword)){
            mResult=false;
            inputPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            inputRePassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            checkboxFirst.setChecked(true);
            checkboxTwice.setChecked(true);

            Snackbar.make(rootView,"两次输入的密码不一致,请核对密码！",Snackbar.LENGTH_LONG).show();

        }
        return mResult;

    }

}
