package org.eenie.wgj.ui.personal.information;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.EmergencyContactMod;
import org.eenie.wgj.model.requset.ModifyInfo;
import org.eenie.wgj.model.requset.UserId;
import org.eenie.wgj.model.response.UserInforById;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.Utils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/2 at 12:04
 * Email: 472279981@qq.com
 * Des:
 */

public class PersonalInformationActivity extends BaseActivity {
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.tv_qualifications)
    TextView tvEducation;
    @BindView(R.id.tv_marry_state)
    TextView tvMarry;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.tv_contacts)
    TextView tvContacts;
    @BindView(R.id.tv_industry)
    TextView tvIndustry;
    @BindView(R.id.tv_skill)
    TextView tvSkill;
    @BindView(R.id.tv_employment)
    TextView tvChannel;
    @BindView(R.id.tv_experience)
    TextView tvWorkExperience;
    @BindView(R.id.tv_work_content)
    TextView tvWorkContent;


    private String height;
    private String qualifications;
    private String qualification;
    private String marryState;
    private String marrayStates;
    private String addressNow;
    private EmergencyContactMod mContactEmergency;
    private String mContactName;
    private String mContactPhone;
    private String mRelation;
    private int mPosition;
    private List<String> industry = new ArrayList<>();
    private List<String> skill = new ArrayList<>();
    private int channel;
    private String channelString;
    private String channelName;
    private String channelPhone;
    private String channelStr;
    private int channelCode;
    private String workExperience;
    private String workContent;

    private String mHeight;
    private String mEducation;
    private String mMarry;
    private String mAddress;
    private String mContacts;
    private String mIndustry;
    private String mSkill;
    private String mChannel;
    private String mWorkExperience;
    private String mWorkContent;

    @Override
    protected int getContentView() {
        return R.layout.activity_personal_information;
    }

    @Override
    protected void updateUI() {
        String userId = mPrefsHelper.getPrefs().getString(Constants.UID, "");
        if (!TextUtils.isEmpty(userId)) {
            getUserInformationById(userId);


        }


    }

    private void getUserInformationById(String userId) {
        UserId mUser = new UserId(userId);
        mSubscription = mRemoteService.getUserInfoById(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), mUser)
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
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            UserInforById mData = gson.fromJson(jsonArray,
                                    new TypeToken<UserInforById>() {
                                    }.getType());
                            if (mData != null) {
                                if (mData.getHeight() != null && !TextUtils.isEmpty(
                                        mData.getHeight())) {
                                    tvHeight.setText(mData.getHeight() + "cm");
                                    tvHeight.setTextColor(ContextCompat.getColor
                                            (context, R.color.titleColor));
                                    mHeight = mData.getHeight();
                                }
                                if (mData.getGraduate() != null && !TextUtils.isEmpty(
                                        mData.getGraduate())) {
                                    tvEducation.setText(mData.getGraduate());
                                    tvEducation.setTextColor(ContextCompat.getColor
                                            (context, R.color.titleColor));
                                    mEducation = mData.getGraduate();

                                }
                                if (mData.getMarry() != null && !TextUtils.isEmpty(mData.getMarry())) {
                                    switch (mData.getMarry()) {
                                        case "1":
                                            tvMarry.setText("未婚");

                                            break;
                                        case "2":

                                            tvMarry.setText("已婚");
                                            break;
                                    }

                                    tvMarry.setTextColor(ContextCompat.getColor
                                            (context, R.color.titleColor));
                                    mMarry = mData.getMarry();
                                }
                                if (mData.getAddress() != null && !TextUtils.isEmpty(
                                        mData.getAddress())) {
                                    tvAddress.setText(mData.getAddress());
                                    tvAddress.setTextColor(ContextCompat.getColor
                                            (context, R.color.titleColor));
                                    mAddress = mData.getAddress();

                                }
                                if (mData.getEmergency_contact() != null) {
                                    tvContacts.setText(mData.getEmergency_contact().getName() + "/" +
                                            mData.getEmergency_contact().getRelation() + "/" +
                                            mData.getEmergency_contact().getPhone());
                                    tvContacts.setTextColor(ContextCompat.getColor
                                            (context, R.color.titleColor));
                                    mContacts = gson.toJson(mData.getEmergency_contact());


                                }
                                if (mData.getIndustry() != null && !TextUtils.isEmpty(
                                        mData.getIndustry())) {
                                    tvIndustry.setTextColor(ContextCompat.getColor
                                            (context, R.color.titleColor));
                                    tvIndustry.setText(mData.getIndustry());
                                    mIndustry = mData.getIndustry();
                                }
                                if (mData.getSkill() != null && !TextUtils.isEmpty(mData.getSkill())) {
                                    tvSkill.setTextColor(ContextCompat.getColor
                                            (context, R.color.titleColor));
                                    tvSkill.setText(mData.getSkill());
                                    mSkill = mData.getSkill();
                                }
                                if (mData.getChannel() != null && !TextUtils.isEmpty(
                                        mData.getChannel())) {
                                    tvChannel.setTextColor(ContextCompat.getColor
                                            (context, R.color.titleColor));
                                    tvChannel.setText(mData.getChannel());
                                    mChannel = mData.getChannel();

                                }
                                if (mData.getWork_experience() != null && !TextUtils.isEmpty(
                                        mData.getWork_experience())) {
                                    tvWorkExperience.setTextColor(ContextCompat.getColor
                                            (context, R.color.titleColor));
                                    tvWorkExperience.setText(mData.getWork_experience());
                                    mWorkExperience = mData.getWork_experience();
                                }
                                if (mData.getWork_content() != null && !TextUtils.isEmpty(
                                        mData.getWork_content())) {
                                    tvWorkContent.setTextColor(ContextCompat.getColor
                                            (context, R.color.titleColor));
                                    tvWorkContent.setText(mData.getWork_content());
                                    mWorkContent = mData.getWork_content();
                                }


                            }
                        } else {
                            Snackbar.make(rootView, apiResponse.getResultMessage(),
                                    Snackbar.LENGTH_SHORT).show();

                        }


                    }
                });

    }

    @OnClick({R.id.img_back, R.id.rl_height, R.id.rl_qualifications, R.id.rl_marry_state,
            R.id.rl_now_address, R.id.rl_contacts, R.id.rl_industry, R.id.rl_skill,
            R.id.rl_work_channel, R.id.rl_work_experience, R.id.rl_work_content})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.rl_height:
                showHeightDialog();


                break;
            case R.id.rl_qualifications:
                showQualifications();


                break;
            case R.id.rl_marry_state:
                showMarryDialog();

                break;
            case R.id.rl_now_address:
                showNowAddressDialog();


                break;
            case R.id.rl_contacts:
                showContactsDialog();


                break;
            case R.id.rl_industry:
                showIndustryDialog();


                break;
            case R.id.rl_skill:
                showSkillDialog();
                break;

            case R.id.rl_work_channel:
                showEmploymentDialog();


                break;
            case R.id.rl_work_experience:
                showExperienceDialog();

                break;
            case R.id.rl_work_content:
                showContentDialog();


                break;

        }
    }

    //可从事的工作内容
    private void showContentDialog() {

        View view = View.inflate(context, R.layout.dialog_work_content, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        EditText editExperience = (EditText) dialog.getWindow().findViewById(R.id.edit_address);
        if (!TextUtils.isEmpty(workContent)) {
            editExperience.setText(workContent);
        }

        dialog.getWindow().findViewById(R.id.btn_next).setOnClickListener(v -> {
            String inputAddress = editExperience.getText().toString();
            if (TextUtils.isEmpty(inputAddress)) {
                editExperience.setError("可从事的工作内容不能为空");
            } else {
                dialog.dismiss();
                workContent = inputAddress;
                modifyInformation(mHeight, mEducation, mMarry, mAddress, mContacts, mIndustry,
                        mSkill, mChannel, mWorkExperience, workContent);
            }


        });
        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {

            dialog.dismiss(); //取消对话框

        });
    }

    //工作经验
    private void showExperienceDialog() {


        View view = View.inflate(context, R.layout.dialog_work_experience, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        EditText editExperience = (EditText) dialog.getWindow().findViewById(R.id.edit_address);
        if (!TextUtils.isEmpty(workExperience)) {
            editExperience.setText(workExperience);
        }

        dialog.getWindow().findViewById(R.id.btn_next).setOnClickListener(v -> {
            String inputAddress = editExperience.getText().toString();
            if (TextUtils.isEmpty(inputAddress)) {
                editExperience.setError("工作经验不能为空！");
            } else {
                dialog.dismiss();
                workExperience = inputAddress;
                modifyInformation(mHeight, mEducation, mMarry, mAddress, mContacts, mIndustry,
                        mSkill, mChannel, workExperience, mWorkContent);
            }


        });
        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {

            dialog.dismiss(); //取消对话框

        });


    }

    //应聘渠道
    private void showEmploymentDialog() {

        View view = View.inflate(context, R.layout.dialog_channel_employment, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        TextView firstChannel = (TextView) dialog.getWindow().findViewById(R.id.tv_first);
        TextView secondChannel = (TextView) dialog.getWindow().findViewById(R.id.tv_second);
        TextView thirdChannel = (TextView) dialog.getWindow().findViewById(R.id.tv_third);
        TextView fourthChannel = (TextView) dialog.getWindow().findViewById(R.id.tv_fourth);
        TextView[] listChannel = new TextView[]{firstChannel, secondChannel, thirdChannel,
                fourthChannel};
        LinearLayout linearOne = (LinearLayout) dialog.getWindow().findViewById(R.id.linear_one);
        LinearLayout linearTwo = (LinearLayout) dialog.getWindow().findViewById(R.id.linear_two);
        TextView title = (TextView) dialog.getWindow().findViewById(R.id.title_dialog);
        EditText etName = (EditText) dialog.getWindow().findViewById(R.id.labour_work);
        EditText etPhone = (EditText) dialog.getWindow().findViewById(R.id.labour_phone);
        if (!TextUtils.isEmpty(channelStr)) {
            switch (channelCode) {
                case 0:
                    setColorSize(0, listChannel);

                    break;
                case 1:
                    setColorSize(1, listChannel);

                    break;

                case 2:
                    setColorSize(2, listChannel);

                    break;
                case 3:
                    setColorSize(3, listChannel);

                    break;

            }
        }
        firstChannel.setOnClickListener(v -> {
            channelString = "赶集网";
            channel = 0;
            setColorSize(0, listChannel);

        });
        secondChannel.setOnClickListener(v -> {
            channelString = "58同城";
            channel = 1;
            setColorSize(1, listChannel);
        });
        thirdChannel.setOnClickListener(v -> {
            linearOne.setVisibility(View.GONE);
            title.setText(R.string.channel_third);
            if (!TextUtils.isEmpty(channelName) && channelCode == 2) {
                etName.setText(channelName);
            }
            if (!TextUtils.isEmpty(channelPhone) && channelCode == 2) {
                etPhone.setText(channelPhone);
            }
            etName.setHint(R.string.friend_name);
            etPhone.setHint(R.string.friend_phone);
            linearTwo.setVisibility(View.VISIBLE);
            channel = 2;
        });
        fourthChannel.setOnClickListener(v -> {
            linearOne.setVisibility(View.GONE);
            linearTwo.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(channelName) && channelCode == 3) {
                etName.setText(channelName);
            }
            if (!TextUtils.isEmpty(channelPhone) && channelCode == 3) {
                etPhone.setText(channelPhone);
            } else {


            }
            etName.setHint(R.string.labour_service);
            etPhone.setHint(R.string.phone_hint);
            linearTwo.setVisibility(View.VISIBLE);
            channel = 3;
        });
        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            dialog.dismiss();
            channelString = null;


        });
        dialog.getWindow().findViewById(R.id.btn_next).setOnClickListener(v -> {
            if (channel == 2 || channel == 3) {
                if (TextUtils.isEmpty(etName.getText().toString())) {
                    etName.setError("输入的姓名不能为空");
                    channelName = null;
                } else {
                    channelName = etName.getText().toString();
                    if (TextUtils.isEmpty(etPhone.getText().toString())) {
                        etPhone.setError("输入的电话不能为空");
                        channelPhone = null;

                    } else {
                        if (Utils.isMobile(etPhone.getText().toString()) ||
                                Utils.IsTelephone(etName.getText().toString())) {
                            channelPhone = etPhone.getText().toString();

                        } else {
                            etPhone.setError("输入的电话格式不正确");
                        }
                    }
                    channelString = channelName + "/" + channelPhone;
                }
            }

            if (TextUtils.isEmpty(channelString)) {
                Toast.makeText(context, "请选择一种应聘渠道", Toast.LENGTH_LONG).show();
            } else {
                channelStr = channelString;
                channelCode = channel;
                dialog.dismiss();
                modifyInformation(mHeight, mEducation, mMarry, mAddress, mContacts,
                        mIndustry, mSkill, channelString, mWorkExperience, mWorkContent);
            }

        });


    }

    //技能
    private void showSkillDialog() {
        View view = View.inflate(context, R.layout.dialog_skill, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        CheckBox first = (CheckBox) dialog.getWindow().findViewById(R.id.checkbox_first);
        CheckBox second = (CheckBox) dialog.getWindow().findViewById(R.id.checkbox_second);
        CheckBox third = (CheckBox) dialog.getWindow().findViewById(R.id.checkbox_third);
        CheckBox fourth = (CheckBox) dialog.getWindow().findViewById(R.id.checkbox_fourth);
        CheckBox fifth = (CheckBox) dialog.getWindow().findViewById(R.id.checkbox_fifth);
        CheckBox sixth = (CheckBox) dialog.getWindow().findViewById(R.id.checkbox_sixth);
        CheckBox seventh = (CheckBox) dialog.getWindow().findViewById(R.id.checkbox_seventh);
        Button btnSave = (Button) dialog.getWindow().findViewById(R.id.btn_save);
        CheckBox[] checkBoxes = new CheckBox[]{first, second, third, fourth, fifth,
                sixth, seventh};
        for (String s : skill) {
            switch (s) {
                case "武术":

                    setChecked(checkBoxes, 0);

                    break;

                case "散打":
                    setChecked(checkBoxes, 1);

                    break;

                case "拳击":
                    setChecked(checkBoxes, 2);

                    break;

                case "退伍":
                    setChecked(checkBoxes, 3);

                    break;

                case "美术":

                    setChecked(checkBoxes, 4);
                    break;
                case "驾照":
                    setChecked(checkBoxes, 5);

                    break;
                case "其它":
                    setChecked(checkBoxes, 6);
                    break;


            }
        }
        onCheckBoxListener(checkBoxes);

        btnSave.setOnClickListener(v -> {
            int count = 0;
            for (CheckBox mChecked : checkBoxes) {
                if (mChecked.isChecked()) {
                    count++;
                }
            }
            if (count == 0) {
                Toast.makeText(context, "至少选择一项技能！", Toast.LENGTH_LONG).show();
            } else {
                List<String> mString = new ArrayList<>();
                for (int i = 0; i < checkBoxes.length; i++) {
                    if (checkBoxes[i].isChecked()) {
                        switch (i) {
                            case 0:
                                mString.add("武术");

                                break;
                            case 1:

                                mString.add("散打");
                                break;

                            case 2:

                                mString.add("拳击");
                                break;

                            case 3:
                                mString.add("退伍");

                                break;

                            case 4:
                                mString.add("美术");

                                break;
                            case 5:
                                mString.add("驾照");

                                break;
                            case 6:
                                mString.add("其它");
                                break;
                        }
                    }
                }
                skill = null;
                skill = mString;
                dialog.dismiss();
                modifyInformation(mHeight, mEducation, mMarry, mAddress, mContacts,
                        mIndustry, Utils.getStr(mString), mChannel, mWorkExperience, mWorkContent);
            }


        });


    }

    private void showIndustryDialog() {
        View view = View.inflate(context, R.layout.dialog_industry, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        CheckBox first = (CheckBox) dialog.getWindow().findViewById(R.id.checkbox_first);
        CheckBox second = (CheckBox) dialog.getWindow().findViewById(R.id.checkbox_second);
        CheckBox third = (CheckBox) dialog.getWindow().findViewById(R.id.checkbox_third);
        CheckBox fourth = (CheckBox) dialog.getWindow().findViewById(R.id.checkbox_fourth);
        CheckBox fifth = (CheckBox) dialog.getWindow().findViewById(R.id.checkbox_fifth);
        CheckBox sixth = (CheckBox) dialog.getWindow().findViewById(R.id.checkbox_sixth);
        Button btnSave = (Button) dialog.getWindow().findViewById(R.id.btn_save);
        TextView tip = (TextView) dialog.getWindow().findViewById(R.id.tip_industrary);
        CheckBox[] checkBoxes = new CheckBox[]{first, second, third, fourth, fifth,
                sixth};
        for (String s : industry) {
            switch (s) {
                case "保安":

                    setChecked(checkBoxes, 0);

                    break;

                case "消防":
                    setChecked(checkBoxes, 1);

                    break;

                case "工程":
                    setChecked(checkBoxes, 2);

                    break;

                case "保洁":
                    setChecked(checkBoxes, 3);

                    break;

                case "客服":

                    setChecked(checkBoxes, 4);
                    break;
                case "其它":
                    setChecked(checkBoxes, 5);

                    break;
            }
        }
        onCheckBoxListener(checkBoxes);

        btnSave.setOnClickListener(v -> {
            int count = 0;
            for (CheckBox mChecked : checkBoxes) {
                if (mChecked.isChecked()) {
                    count++;
                }
            }
            if (count == 0) {
                Toast.makeText(context, "至少选择一项行业！", Toast.LENGTH_LONG).show();
            } else if (count > 3) {
                tip.setVisibility(View.VISIBLE);
            } else {
                tip.setVisibility(View.INVISIBLE);
                List<String> mString = new ArrayList<>();
                for (int i = 0; i < checkBoxes.length; i++) {
                    if (checkBoxes[i].isChecked()) {
                        switch (i) {
                            case 0:
                                mString.add("保安");

                                break;
                            case 1:

                                mString.add("消防");
                                break;

                            case 2:

                                mString.add("工程");
                                break;

                            case 3:
                                mString.add("保洁");

                                break;

                            case 4:
                                mString.add("客服");

                                break;
                            case 5:
                                mString.add("其它");

                                break;
                        }
                    }
                }
                industry = null;
                industry = mString;
                dialog.dismiss();
                modifyInformation(mHeight, mEducation, mMarry, mAddress, mContacts,
                        Utils.getStr(mString), mSkill, mChannel, mWorkExperience, mWorkContent);
            }


        });


    }

    private void setChecked(CheckBox[] checkbox, int position) {
        checkbox[position].setChecked(true);
        checkbox[position].setTextColor(ContextCompat.getColor
                (context, R.color.white));
    }

    private void onCheckBoxListener(CheckBox[] checkBoxes) {
        for (CheckBox checkBox : checkBoxes) {
            checkBox.setOnClickListener(v -> {
                if (checkBox.isChecked()) {
                    checkBox.setTextColor(ContextCompat.getColor
                            (context, R.color.white));
                } else {
                    checkBox.setTextColor(ContextCompat.getColor
                            (context, R.color.titleColor));
                }
            });
        }


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
        if (!TextUtils.isEmpty(mContactName)) {
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
        dialog.getWindow().findViewById(R.id.btn_save).setOnClickListener(v -> {
            if (TextUtils.isEmpty(contactName.getText().toString())) {
                contactName.setError("输入的联系人姓名不能为空！");
            } else {
                mContactName = contactName.getText().toString();
                if (TextUtils.isEmpty(contactPhone.getText().toString())) {
                    contactPhone.setError("输入的手机号不能为空！");
                } else if (!Utils.isMobile(contactPhone.getText().toString())) {
                    contactPhone.setError("输入的手机号格式不正确！");

                } else {
                    mContactPhone = contactPhone.getText().toString();

                    if (TextUtils.isEmpty(mRelation)) {
                        Toast.makeText(context, "请选择关系！", Toast.LENGTH_LONG).show();
                    } else {
                        dialog.dismiss();
                        Gson gson = new Gson();
                        EmergencyContactMod data = new EmergencyContactMod();
                        if (!TextUtils.isEmpty(mContactName) && !TextUtils.isEmpty(mContactPhone) &&
                                !TextUtils.isEmpty(mRelation)) {
                            data.setName(mContactName);
                            data.setPhone(mContactPhone);
                            data.setRelation(mRelation);

                        }
                        modifyInformation(mHeight, mEducation, mMarry, mAddress, gson.toJson(data),
                                mIndustry, mSkill, mChannel, mWorkExperience, mWorkContent);


                    }


                }
            }


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
                editAddress.setError("地址不能为空！");
            } else {
                dialog.dismiss();
                addressNow = inputAddress;
                modifyInformation(mHeight, mEducation, mMarry, addressNow, mContacts, mIndustry,
                        mSkill, mChannel, mWorkExperience, mWorkContent);
            }


        });
        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {

            dialog.dismiss(); //取消对话框

        });

    }

    //婚姻状态
    private void showMarryDialog() {
        View view = View.inflate(context, R.layout.dialog_set_marry_state, null);
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
                marrayStates = "未婚";
                setColorSize(0, listText);

            });
        }
        if (married != null) {
            married.setOnClickListener(v -> {
                marrayStates = "已婚";
                setColorSize(1, listText);

            });
        }
        dialog.getWindow().findViewById(R.id.btn_next).setOnClickListener(v -> {

            if (TextUtils.isEmpty(marrayStates)) {
                Toast.makeText(context, "请选择婚姻状况！", Toast.LENGTH_SHORT).show();
            } else {
                marryState = marrayStates;
                dialog.dismiss();
                modifyInformation(mHeight, mEducation, marryState, mAddress, mContacts, mIndustry,
                        mSkill, mChannel, mWorkExperience, mWorkContent);


            }

        });
        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            dialog.dismiss(); //取消对话框
            marrayStates = "";

        });

    }


    public void setColorSize(int position, TextView[] list) {
        for (int i = 0; i < list.length; i++) {
            list[i].setTextColor(ContextCompat.getColor
                    (context, R.color.titleColor))
            ;
            list[i].setTextSize(14);
        }
        list[position].setTextColor(ContextCompat.getColor
                (context, R.color.colorAccent));
        list[position].setTextSize(18);

    }

    //学历
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
                case "初中":
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
                qualification = "小学";
                setColorSize(0, listText);

            });
        }
        if (middle != null) {
            middle.setOnClickListener(v -> {
                qualification = "初中";
                setColorSize(1, listText);

            });
        }
        if (heightSchool != null) {
            heightSchool.setOnClickListener(v -> {
                qualification = "高中";
                setColorSize(2, listText);

            });
        }
        if (junior != null) {
            junior.setOnClickListener(v -> {
                qualification = "专科";
                setColorSize(3, listText);

            });
        }
        if (university != null) {
            university.setOnClickListener(v -> {
                qualification = "本科";
                setColorSize(4, listText);

            });
        }
        if (graduate != null) {
            graduate.setOnClickListener(v -> {
                qualification = "研究生";
                setColorSize(5, listText);

            });
        }
        if (doctor != null) {
            doctor.setOnClickListener(v -> {
                qualification = "博士";
                setColorSize(6, listText);

            });
        }


        dialog.getWindow().findViewById(R.id.btn_next).setOnClickListener(v -> {

            if (TextUtils.isEmpty(qualification)) {
                Toast.makeText(context, "请选择学历！", Toast.LENGTH_SHORT).show();
            } else {
                qualifications = qualification;
                dialog.dismiss();
                modifyInformation(mHeight, qualifications, mMarry, mAddress, mContacts, mIndustry,
                        mSkill, mChannel, mWorkExperience, mWorkContent);


            }

        });
        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {

            dialog.dismiss(); //取消对话框
            qualification = null;

        });

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
                if (height.endsWith("cm") || height.endsWith("CM")) {
                    height = height.substring(0, height.length() - 2);
                }
                modifyInformation(height, mEducation, mMarry, mAddress, mContacts, mIndustry,
                        mSkill, mChannel, mWorkExperience, mWorkContent);

            }


        });
        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {

            dialog.dismiss(); //取消对话框

        });


    }


    private void modifyInformation(String height, String education, String marry, String address,
                                   String contacts, String industry, String skill, String channel,
                                   String workExperience, String workContent) {
        String marryState = "";

        switch (marry) {
            case "未婚":
                marryState = "1";

                break;
            case "已婚":
                marryState = "2";

                break;
            case "":
                marryState = marry;
                break;
            case "1":

                marryState = "1";
                break;
            case "2":
                marryState = "2";
                break;


        }

        ModifyInfo info = new ModifyInfo(height, education, marryState, address, contacts, industry, skill,
                channel, workExperience, workContent
        );
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        String finalMarryState = marryState;
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
                            Snackbar.make(rootView, "修改成功！", Snackbar.LENGTH_LONG).show();
                            if (!mHeight.equals(height)) {
                                tvHeight.setText(height + "cm");
                                tvHeight.setTextColor(ContextCompat.getColor
                                        (context, R.color.titleColor));
                                mHeight = height;

                            }
                            if (!mEducation.equals(education)) {
                                tvEducation.setText(education);
                                tvEducation.setTextColor(ContextCompat.getColor
                                        (context, R.color.titleColor));
                                qualification = "";
                                mEducation = education;
                            }
                            if (!mMarry.equals(finalMarryState)) {
                                switch (finalMarryState) {
                                    case "1":

                                        tvMarry.setText("未婚");
                                        break;

                                    case "2":
                                        tvMarry.setText("已婚");

                                        break;
                                }

                                tvMarry.setTextColor(ContextCompat.getColor
                                        (context, R.color.titleColor));
                                marrayStates = "";
                                mMarry = finalMarryState;

                            }
                            if (!mAddress.equals(address)) {
                                tvAddress.setText(address);
                                tvAddress.setTextColor(ContextCompat.getColor
                                        (context, R.color.titleColor));
                                mAddress = address;
                            }
                            if (!mContacts.equals(contacts)) {
                                tvContacts.setText(mContactName + "/" + mRelation + "/" +
                                        mContactPhone);
                                tvContacts.setTextColor(ContextCompat.getColor
                                        (context, R.color.titleColor));
                                mContacts = contacts;
                            }
                            if (!mIndustry.equals(industry)) {
                                tvIndustry.setText(industry);
                                tvIndustry.setTextColor(ContextCompat.getColor
                                        (context, R.color.titleColor));
                                mIndustry = industry;
                            }
                            if (!mSkill.equals(skill)) {
                                tvSkill.setText(skill);
                                tvSkill.setTextColor(ContextCompat.getColor
                                        (context, R.color.titleColor));
                                mSkill = skill;
                            }
                            if (!mChannel.equals(channel)) {
                                tvChannel.setText(channel);
                                tvChannel.setTextColor(ContextCompat.getColor
                                        (context, R.color.titleColor));
                                mChannel = channel;
                                channelString = null;
                            }
                            if (!mWorkExperience.equals(workExperience)) {
                                tvWorkExperience.setText(workExperience);
                                tvWorkExperience.setTextColor(ContextCompat.getColor
                                        (context, R.color.titleColor));
                                mWorkExperience = workExperience;
                            }
                            if (!mWorkContent.equals(workContent)){
                                tvWorkContent.setText(workExperience);
                                tvWorkContent.setTextColor(ContextCompat.getColor
                                        (context, R.color.titleColor));
                                mWorkContent = workContent;
                            }


                        } else {
                            Snackbar.make(rootView, apiResponse.getResultMessage(),
                                    Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });

    }
}
