package org.eenie.wgj.ui.login;

import android.support.design.widget.Snackbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

import static android.content.ContentValues.TAG;

/**
 * Created by Eenie on 2017/5/3 at 11:43
 * Email: 472279981@qq.com
 * Des:
 */

public class RegisterCompanyFragment extends BaseFragment {
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.et_company_name)
    EditText etCompany;
    @BindView(R.id.et_contacts_name)
    EditText etContacts;
    @BindView(R.id.et_contacts_phone)
    EditText etPhone;
    @BindView(R.id.et_contacts_email)
    EditText etEmail;

    @Override
    protected int getContentView() {
        return R.layout.fragment_register_company;
    }

    @Override
    protected void updateUI() {

    }

    @OnClick({R.id.security_register_button, R.id.img_back, R.id.btn_next})
    public void onClick(View view) {
        String company = etCompany.getText().toString();
        String contacts = etContacts.getText().toString();
        String phone = etPhone.getText().toString();
        String email = etEmail.getText().toString();
        switch (view.getId()) {


            case R.id.img_back:
                fragmentMgr.beginTransaction()
                        .addToBackStack(TAG)
                        .replace(R.id.fragment_login_container, new LoginFragment())
                        .commit();

                break;
            case R.id.security_register_button:
                fragmentMgr.beginTransaction()
                        .addToBackStack(TAG)
                        .replace(R.id.fragment_login_container, new RegisterFirstFragment())
                        .commit();

                break;
            case R.id.btn_next:
                if (checkInput(company, contacts, phone, email)) {
                    fragmentMgr.beginTransaction()
                            .addToBackStack(TAG)
                            .replace(R.id.fragment_login_container,
                                    new RegisterCompanyFirstFragment())
                            .commit();

                }

                break;
        }
    }

    //检查输入内容
    private boolean checkInput(String company, String contacts, String phone, String email) {
        boolean result = true;
        if (TextUtils.isEmpty(company)) {
            Snackbar.make(rootView, "请输入公司名称", Snackbar.LENGTH_LONG).show();
            result = false;
        }
        if (result && TextUtils.isEmpty(contacts)) {
            Snackbar.make(rootView, "请输入联系人姓名", Snackbar.LENGTH_LONG).show();
            result = false;
        }
        if (result && TextUtils.isEmpty(phone)) {
            Snackbar.make(rootView, "请输入联系人电话", Snackbar.LENGTH_LONG).show();
            result = false;
        }
        if (result && TextUtils.isEmpty(email)) {
            Snackbar.make(rootView, "请输入联系人邮箱", Snackbar.LENGTH_LONG).show();
            result = false;
        }
        return result;
    }
}
