package org.eenie.wgj.ui.personal.information;

import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.ModifyInfo;
import org.eenie.wgj.util.Constants;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/2 at 10:58
 * Email: 472279981@qq.com
 * Des:
 */

public class PersonalSecurityActivity extends BaseActivity {
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.et_bank_card)
    EditText etSecurityCard;
    private String mBank;
    private String mAvatarUrl;

    @Override
    protected int getContentView() {
        return R.layout.activity_personal_security_code;
    }

    @Override
    protected void updateUI() {
        mBank = mPrefsHelper.getPrefs().getString(Constants.BANK_CARD, "");

        mAvatarUrl = mPrefsHelper.getPrefs().getString(Constants.PERSONAL_AVATAR, "");
        String security = mPrefsHelper.getPrefs().getString(Constants.SECURITY_CARD, "");
        if (!TextUtils.isEmpty(security)){
            etSecurityCard.setText(security);
        }

    }

    @OnClick({R.id.img_back, R.id.btn_login})
    public void onClick(View view) {
        String securityCard = etSecurityCard.getText().toString();
        switch (view.getId()) {
            case R.id.btn_login:
                if (!TextUtils.isEmpty(securityCard)){
                    modifyInformation(mAvatarUrl,mBank,securityCard);


                }else {
                    Snackbar.make(rootView,"请输入保安证号",Snackbar.LENGTH_SHORT).show();
                }

                break;
            case R.id.img_back:

                onBackPressed();
                break;
        }
    }
    private void  modifyInformation(String avatarUrl,String bankCard,String securityCard){
        ModifyInfo info=new ModifyInfo(avatarUrl,bankCard,securityCard);
        String token=mPrefsHelper.getPrefs().getString(Constants.TOKEN,"");
        mSubscription = mRemoteService.modifyInforById(token, info)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode() == 200) {
                            Snackbar.make(rootView,"修改成功！",Snackbar.LENGTH_LONG).show();

                            if (!TextUtils.isEmpty(securityCard)){
                                mPrefsHelper.getPrefs().edit().putString(
                                        Constants.SECURITY_CARD,securityCard)
                                        .apply();
                            }
                            finish();


                        } else {
                            Snackbar.make(rootView, apiResponse.getResultMessage(),
                                    Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
