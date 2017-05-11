package org.eenie.wgj.ui.login;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseFragment;
import org.eenie.wgj.util.Constants;

import butterknife.OnClick;

import static android.content.ContentValues.TAG;

/**
 * Created by Eenie on 2017/5/8 at 14:58
 * Email: 472279981@qq.com
 * Des:
 */

public class SelectCompanyWayFragment extends BaseFragment {
    public static final String PHONE = "phone";
    public static final String PWD = "pwd";
    public static final String UID = "uid";
    public static final String TOKEN="token";
    private String mPhone;
    private String mPassword;
    private int userId;
    private String token;

    @Override
    protected int getContentView() {
        return R.layout.fragment_select_company;
    }

    @Override
    protected void updateUI() {

    }

    public static SelectCompanyWayFragment newInstance(String username, String password, int userId,
                                                       String token) {
        SelectCompanyWayFragment fragment = new SelectCompanyWayFragment();
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)&&!TextUtils.isEmpty(token)) {
            Bundle args = new Bundle();
            args.putString(PHONE, username);
            args.putString(PWD, password);
            args.putInt(UID, userId);
            args.putString(TOKEN,token);
            fragment.setArguments(args);
            fragment.setArguments(args);
        }
        return fragment;


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getArguments() != null) {
            mPhone = getArguments().getString(PHONE);
            mPassword = getArguments().getString(PWD);
            userId = getArguments().getInt(UID);
            token=getArguments().getString(TOKEN);

        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @OnClick({R.id.img_back, R.id.img_join_company, R.id.img_create_company})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                mPrefsHelper.getPrefs().edit().putString(Constants.REGISTER_PHONE, mPhone)
                        .putString(Constants.REGISTER_PASSWORD, mPassword)
                        .apply();
                fragmentMgr.beginTransaction()
                        .addToBackStack(TAG)
                        .replace(R.id.fragment_login_container,
                                new LoginFragment()).commit();


                break;
            case R.id.img_join_company:

                fragmentMgr.beginTransaction()
                        .addToBackStack(TAG)
                        .replace(R.id.fragment_login_container,
                                RegisterPersonalFirstFragment.newInstance(userId,token)).commit();

                break;
            case R.id.img_create_company:

                fragmentMgr.beginTransaction()
                        .addToBackStack(TAG)
                        .replace(R.id.fragment_login_container,
                                 CreateCompanyFragment.newInstance(userId,token)).commit();

                break;
        }
    }
}
