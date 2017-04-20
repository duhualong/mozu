package org.eenie.wgj.ui.login;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseFragment;
import org.eenie.wgj.model.requset.EmergencyContactMod;
import org.eenie.wgj.util.Utils;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/4/18 at 17:26
 * Email: 472279981@qq.com
 * Des:
 */

public class RegisterThirdFragment extends BaseFragment {
    private EmergencyContactMod mContactEmergency;
    private String mContactName;
    private String mContactPhone;
    private String mRelation;
    private int mPosition;
    private String height;
    private String qualifications;
    private String marryState;
    private String addressNow;
    private String workNow;


    @BindView(R.id.tv_height)
    TextView mHeight;
    @BindView(R.id.tv_qualifications)
    TextView mQualifications;
    @BindView(R.id.tv_marry_state)
    TextView mMarryState;
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.tv_address)
    TextView mAddressNow;
    @BindView(R.id.tv_contacts)
    TextView mContacts;
    @BindView(R.id.tv_work_name)
    TextView mWorkName;
    @BindView(R.id.tv_industry)
    TextView mIndustry;
    @BindView(R.id.tv_skill)
    TextView mSkill;
    @BindView(R.id.tv_employment)
    TextView emEmployment;


    @Override
    protected int getContentView() {
        return R.layout.fragment_register_third;
    }

    @Override
    protected void updateUI() {


    }

    public static RegisterThirdFragment newInstance(String phone, String password,
                                                    File positiveImg, File negivateImg,
                                                    File avatarImg) {

        Bundle args = new Bundle();

        RegisterThirdFragment fragment = new RegisterThirdFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @OnClick({R.id.img_back, R.id.btn_apply, R.id.tv_height, R.id.tv_qualifications,
            R.id.tv_marry_state, R.id.tv_address, R.id.tv_contacts, R.id.tv_work_name,
            R.id.tv_industry, R.id.tv_skill, R.id.tv_employment})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();

                break;

            case R.id.btn_apply:
                //getData();

                break;
            case R.id.tv_height:
                showHeightDialog();


                break;
            case R.id.tv_qualifications:
                showQualifications();

                break;
            case R.id.tv_marry_state:
                showMarryDialog();


                break;
            case R.id.tv_address:
                showNowAddressDialog();


                break;
            case R.id.tv_contacts:
                showContactsDialog();


                break;
            case R.id.tv_work_name:
                showWorkDialog();


                break;
            case R.id.tv_industry:


                break;
            case R.id.tv_skill:


                break;
            case R.id.tv_employment:

                break;
        }
    }

    //现单位名称
    private void showWorkDialog() {
        View view = View.inflate(context, R.layout.dialog_work_now, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        EditText work= (EditText) dialog.getWindow().findViewById(R.id.et_work_now);

        dialog.getWindow().findViewById(R.id.btn_next).setOnClickListener(v -> {
            String inputWork = work.getText().toString();
            if (TextUtils.isEmpty(inputWork)) {
                work.setError("现单位名称不能为空！");
            } else {
                dialog.dismiss();
                workNow = inputWork;
                mWorkName.setText(inputWork);
                mWorkName.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            }
        });
        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            dialog.dismiss(); //取消对话框

        });

    }

    //紧急联系人
    private void showContactsDialog() {
        View view = View.inflate(context, R.layout.dialog_contacts, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        //资源转[]
        String[] spinner = getResources().getStringArray(R.array.spingcontacts);

        Spinner mSpinner = (Spinner) dialog.getWindow().findViewById(R.id.spinner_contacts);
        EditText contactName = (EditText) dialog.getWindow().findViewById(R.id.emergency_contact_name);
        EditText contactPhone = (EditText) dialog.getWindow().findViewById(R.id.emergency_contact_telephone);
        if (TextUtils.isEmpty(mContactName)) {
            contactName.setText(mContactName);
        }
        if (!TextUtils.isEmpty(mContactPhone)) {
            contactPhone.setText(mContactPhone);
        }

        //构造ArrayAdapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context,
                R.layout.simple_spinner_item, spinner);
        //设置下拉样式以后显示的样式
        adapter.setDropDownViewResource(R.layout.my_drop_down_item);
        mSpinner.setAdapter(adapter);
        mSpinner.setSelection(mPosition);
        new Thread() {
            public void run() {
                mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        if (position != 0) {
                            mRelation = spinner[position];
                            mPosition = position;

                        } else {

                        }

                        Log.d("qianfeng:", spinner[position]);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        Log.d("qianfeng:", "NothingSelected");

                    }
                });

            }
        }.start();
        dialog.getWindow().findViewById(R.id.btn_save).setOnClickListener(v -> {
            if (TextUtils.isEmpty(contactName.getText().toString())) {
                contactName.setError("输入的联系人姓名不能为空！");
            } else {
                mContactName = contactName.getText().toString();
            }


            if (TextUtils.isEmpty(contactPhone.getText().toString())) {
                contactPhone.setError("输入的手机号不能为空！");
            } else if (!Utils.isMobile(contactPhone.getText().toString())) {
                contactPhone.setError("输入的手机号格式不正确！");

            } else {
                mContactPhone = contactPhone.getText().toString();
            }

            if (TextUtils.isEmpty(mRelation)) {
                Toast.makeText(context, "请选择关系！", Toast.LENGTH_LONG).show();
            }

            mContacts.setText("已填写");
            mContacts.setTextColor(ContextCompat.getColor
                    (context, R.color.titleColor));


        });


    }


    //现居住地址
    private void showNowAddressDialog() {
        View view = View.inflate(context, R.layout.dialog_location_now, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        EditText editAddress = (EditText) dialog.getWindow().findViewById(R.id.edit_address);
        if (!TextUtils.isEmpty(addressNow)) {
            editAddress.setText(addressNow);
        }

        dialog.getWindow().findViewById(R.id.btn_next).setOnClickListener(v -> {
            String inputAddress = editAddress.getText().toString();
            if (TextUtils.isEmpty(inputAddress)) {
                editAddress.setError("身高不能为空！");
            } else {
                dialog.dismiss();
                addressNow = inputAddress;
                mAddressNow.setText(addressNow);
                mAddressNow.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));

            }


        });
        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {

            dialog.dismiss(); //取消对话框

        });

    }

    //婚姻状态
    private void showMarryDialog() {
        View view = View.inflate(context, R.layout.dialog_set_qualifications, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        TextView single = (TextView) dialog.findViewById(R.id.tv_single);
        TextView married = (TextView) dialog.findViewById(R.id.tv_married);
        TextView[] listText = new TextView[]{single, married};
        if (!TextUtils.isEmpty(marryState)) {
            int position = 0;
            switch (marryState) {
                case "未婚":
                    position = 0;
                    break;
                case "已婚":
                    position = 1;
                    break;

            }
            setColorSize(position, listText);

        }

        if (single != null) {
            single.setOnClickListener(v -> {
                marryState = "未婚";
                setColorSize(0, listText);

            });
        }
        if (married != null) {
            married.setOnClickListener(v -> {
                marryState = "中学";
                setColorSize(1, listText);

            });
        }
        dialog.getWindow().findViewById(R.id.btn_next).setOnClickListener(v -> {

            if (TextUtils.isEmpty(marryState)) {
                Toast.makeText(context, "请选择婚姻状况！", Toast.LENGTH_SHORT).show();
            } else {
                dialog.dismiss();
                mMarryState.setText(marryState);
                mMarryState.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            }

        });
        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            dialog.dismiss(); //取消对话框

        });

    }

    private void showQualifications() {
        View view = View.inflate(context, R.layout.dialog_set_qualifications, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        TextView primary = (TextView) dialog.findViewById(R.id.tv_primary);
        TextView middle = (TextView) dialog.findViewById(R.id.tv_middle);
        TextView heightSchool = (TextView) dialog.findViewById(R.id.tv_height_school);
        TextView junior = (TextView) dialog.findViewById(R.id.tv_junior_college);
        TextView university = (TextView) dialog.findViewById(R.id.tv_university);
        TextView graduate = (TextView) dialog.findViewById(R.id.tv_graduate);
        TextView doctor = (TextView) dialog.findViewById(R.id.tv_doctor);
        TextView[] listText = new TextView[]{primary, middle, heightSchool, junior, university,
                graduate, doctor};
        if (!TextUtils.isEmpty(qualifications)) {
            int position = 0;
            switch (qualifications) {
                case "小学":
                    position = 0;
                    break;
                case "中学":
                    position = 1;
                    break;
                case "高中":
                    position = 2;
                    break;
                case "专科":
                    position = 3;
                    break;
                case "本科":
                    position = 4;
                    break;
                case "研究生":
                    position = 5;
                    break;
                case "博士":
                    position = 6;
                    break;

            }
            setColorSize(position, listText);
        }


        if (primary != null) {
            primary.setOnClickListener(v -> {
                qualifications = "小学";
                setColorSize(0, listText);

            });
        }
        if (middle != null) {
            middle.setOnClickListener(v -> {
                qualifications = "中学";
                setColorSize(1, listText);

            });
        }
        if (heightSchool != null) {
            heightSchool.setOnClickListener(v -> {
                qualifications = "高中";
                setColorSize(2, listText);

            });
        }
        if (junior != null) {
            junior.setOnClickListener(v -> {
                qualifications = "专科";
                setColorSize(3, listText);

            });
        }
        if (university != null) {
            university.setOnClickListener(v -> {
                qualifications = "本科";
                setColorSize(4, listText);

            });
        }
        if (graduate != null) {
            graduate.setOnClickListener(v -> {
                qualifications = "研究生";
                setColorSize(5, listText);

            });
        }
        if (doctor != null) {
            doctor.setOnClickListener(v -> {
                qualifications = "博士";
                setColorSize(6, listText);

            });
        }


        dialog.getWindow().findViewById(R.id.btn_next).setOnClickListener(v -> {

            if (TextUtils.isEmpty(qualifications)) {
                Toast.makeText(context, "请选择学历！", Toast.LENGTH_SHORT).show();
            } else {
                dialog.dismiss();
                mQualifications.setText(qualifications);
                mQualifications.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            }

        });
        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {

            dialog.dismiss(); //取消对话框

        });

    }

    public void setColorSize(int position, TextView[] list) {
        for (int i = 0; i < list.length; i++) {
            list[i].setTextColor(ContextCompat.getColor
                    (context, R.color.gray))
            ;
            list[i].setTextSize(14);
        }
        list[position].setTextColor(ContextCompat.getColor
                (context, R.color.colorAccent));
        list[position].setTextSize(18);


    }
    //身高的dialog

    private void showHeightDialog() {
        View view = View.inflate(context, R.layout.dialog_set_height, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        EditText etHeight = (EditText) dialog.getWindow().findViewById(R.id.edit_height);
        if (!TextUtils.isEmpty(height)) {
            etHeight.setText(height);
        }

        dialog.getWindow().findViewById(R.id.btn_next).setOnClickListener(v -> {
            String inputHeight = etHeight.getText().toString();
            if (TextUtils.isEmpty(inputHeight)) {
                etHeight.setError("身高不能为空！");
            } else {
                dialog.dismiss();
                height = inputHeight;
                mHeight.setText(height);
                mHeight.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));

            }


        });
        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {

            dialog.dismiss(); //取消对话框

        });


    }

    public String cutString(String text) {
        Pattern p = Pattern.compile("\\d+");
        Matcher m = p.matcher(text);
        m.find();
        return m.group();
    }
//
}
