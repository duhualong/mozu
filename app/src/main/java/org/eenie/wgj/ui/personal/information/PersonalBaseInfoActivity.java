package org.eenie.wgj.ui.personal.information;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.UserId;
import org.eenie.wgj.model.response.UserInforById;
import org.eenie.wgj.ui.login.LoginActivity;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.SingleSubscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/4/25 at 10:53
 * Email: 472279981@qq.com
 * Des:
 */

public class PersonalBaseInfoActivity extends BaseActivity {
    public static final String AVATAR = "avatar";
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.img_avatar)
    CircleImageView avatar;
    @BindView(R.id.bank_card)
    TextView bank;
    @BindView(R.id.security_code)
    TextView security;


    @Override
    protected int getContentView() {
        return R.layout.activity_personal_center;
    }

    @Override
    protected void updateUI() {
        String avatarUrl = mPrefsHelper.getPrefs().getString(Constants.PERSONAL_AVATAR, "");
        System.out.println("打印avatar" + avatarUrl);
        if (!TextUtils.isEmpty(avatarUrl)) {
            Glide.with(context)
                    .load(avatarUrl)
                    .asBitmap()
                    .centerCrop()
                    .into(avatar);
        }
        String bankCard = mPrefsHelper.getPrefs().getString(Constants.BANK_CARD, "");

        String securityCard = mPrefsHelper.getPrefs().getString(Constants.SECURITY_CARD, "");
        if (!TextUtils.isEmpty(bankCard)) {
            bank.setText(bankCard);

        }
        if (!TextUtils.isEmpty(securityCard)) {
            security.setText(securityCard);

        }

    }

    @OnClick({R.id.img_back, R.id.img_scan, R.id.rl_avatar_img, R.id.rl_identity_card, R.id.rl_personal_information,
            R.id.rl_bank_card, R.id.rl_security_certificate, R.id.btn_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:

                onBackPressed();
                break;
            case R.id.img_scan:
                startActivity(new Intent(context, PersonalBarcodeActivity.class));
                break;
            case R.id.rl_avatar_img:
                showAvatarDialog();

                break;

            case R.id.rl_identity_card:
                showCardPersonal();


                break;
            case R.id.rl_personal_information:
                startActivity(new Intent(context, PersonalInformationActivity.class));
                break;
            case R.id.rl_bank_card:
                startActivity(new Intent(context, PersonalBindBankActivity.class));


                break;
            case R.id.rl_security_certificate:
                startActivity(new Intent(context, PersonalSecurityActivity.class));


                break;
            case R.id.btn_logout:
                //退出登录
                mPrefsHelper.getPrefs().edit().putBoolean(Constants.IS_LOGIN, false).apply();

                startActivity(new Intent(context, LoginActivity.class));
                finish();

                break;

        }
    }

    //身份证信息
    private void showCardPersonal() {
        String userId = mPrefsHelper.getPrefs().getString(Constants.UID, "");
        if (!TextUtils.isEmpty(userId)) {
            getCardInfor(userId);

        }


    }


    private void getCardInfor(String userId) {
        UserId mUser = new UserId(userId);
        mSubscription = mRemoteService.getUserInfoById(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), mUser)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new SingleSubscriber<ApiResponse>() {
                    @Override
                    public void onSuccess(ApiResponse value) {
                        if (value.getResultCode() == 200) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(value.getData());
                            UserInforById mData = gson.fromJson(jsonArray,
                                    new TypeToken<UserInforById>() {
                                    }.getType());
                            if (mData != null) {
                                View view = View.inflate(context,
                                        R.layout.dialog_personal_identity_card, null);
                                CircleImageView avatar = (CircleImageView)
                                        view.findViewById(R.id.personal_sdv_avatar);
                                TextView mName = (TextView) view.findViewById(R.id.tv_name);
                                TextView mSex = (TextView) view.findViewById(R.id.tv_sex);
                                TextView mNation = (TextView) view.findViewById(R.id.tv_nation);
                                TextView mBirthday = (TextView) view.findViewById(R.id.tv_birthday);
                                TextView mAddress = (TextView) view.findViewById(R.id.tv_address);
                                TextView mIdentity = (TextView) view.findViewById(R.id.tv_identity_card);
                                TextView mSignOffice = (TextView) view.findViewById(R.id.tv_sign_office);
                                TextView mDeadline = (TextView) view.findViewById(R.id.tv_date_deadline);
                                Glide.with(context)
                                        .load(Constant.DOMIN + mData.getId_card_head_image())
                                        .asBitmap()
                                        .centerCrop()
                                        .into(avatar);
                                mName.setText(mData.getName());
                                mSex.setText(mData.getGender());
                                mNation.setText(mData.getPeople());
                                mBirthday.setText(mData.getBirthday());
                                mAddress.setText(mData.getAddress());
                                mIdentity.setText(mData.getNumber());
                                mSignOffice.setText(mData.getPublisher());
                                mDeadline.setText(mData.getValidate());

                                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                final AlertDialog dialog = builder
                                        .setView(view) //自定义的布局文件
                                        .create();
                                dialog.show();
                                dialog.getWindow().findViewById(R.id.btn_ok).setOnClickListener(v -> {
                                    dialog.dismiss();

                                });
                            }


                        } else {
                            Snackbar.make(rootView, value.getResultMessage(),
                                    Snackbar.LENGTH_LONG).show();
                        }

                    }

                    @Override
                    public void onError(Throwable error) {
                        Toast.makeText(context, "参数错误", Toast.LENGTH_LONG).show();

                    }
                });

    }
    //上传头像

    private void showAvatarDialog() {
        View view = View.inflate(context, R.layout.dialog_personal_avatar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.tv_camera_personal).setOnClickListener(v -> {

        });
        dialog.getWindow().findViewById(R.id.tv_photo_personal).setOnClickListener(v -> {

        });


    }


}
