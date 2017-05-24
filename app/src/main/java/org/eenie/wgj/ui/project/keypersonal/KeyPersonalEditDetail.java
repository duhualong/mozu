package org.eenie.wgj.ui.project.keypersonal;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yalantis.ucrop.UCrop;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.AddKeyPersonalInformation;
import org.eenie.wgj.model.response.KeyContactList;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.ImageUtils;
import org.eenie.wgj.util.RxUtils;
import org.eenie.wgj.util.Utils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;
import okhttp3.MediaType;
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

/**
 * Created by Eenie on 2017/5/18 at 9:41
 * Email: 472279981@qq.com
 * Des:
 */

public class KeyPersonalEditDetail extends BaseActivity {
    public static final String INFO = "info";
    public static final String PROJECT_ID = "id";
    private static final String TAG = "KeyPersonalEditDetail";
    private static final int TAKE_PHOTO_REQUEST = 0x101;
    private static final int REQUEST_GALLERY_PHOTO = 0x102;
    private static final int RESPONSE_CODE_POSITIVE = 0x103;
    private static final int RESPONSE_CODE_NEGIVITE = 0x104;
    private String avatarUrl;
    private Uri imageUri;
    @BindView(R.id.avatar_tv)
    TextView tvAvatar;
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.checkbox_avatar_show_state)
    CheckBox mCheckBox;
    @BindView(R.id.img_avatar)
    CircleImageView avatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_gender)
    TextView tvGender;
    @BindView(R.id.tv_age)
    TextView tvAge;
    @BindView(R.id.tv_height)
    TextView tvHeight;
    @BindView(R.id.tv_post)
    TextView tvPost;
    @BindView(R.id.tv_wok_time)
    TextView tvWorkTime;
    @BindView(R.id.tv_car_number)
    TextView tvCarNumber;
    @BindView(R.id.tv_phone_number)
    TextView tvPhone;
    @BindView(R.id.tv_other)
    TextView tvOther;
    private File mAvatarFile;
    private String mName;
    private String mGender;
    private String mHeight;
    private String mAge;
    private String mPost;
    private String mCarNumber;
    private String mPhone;
    private String mOther;
    private String mWorkTime;
    private String startTime;
    private String endTime;
    private String mAvatarUrl;
    private KeyContactList data;
    private int mId;
    private String projectId;


    @Override
    protected int getContentView() {
        return R.layout.activity_edit_key_contact_information;
    }

    @Override
    protected void updateUI() {
        projectId = getIntent().getStringExtra(PROJECT_ID);

        data = getIntent().getParcelableExtra(INFO);
        if (data != null) {
            mId = data.getId();
            mName = data.getName();
            mAvatarUrl = data.getImages();
            mHeight = data.getInfo().getHeight() + "";
            switch (data.getInfo().getSex()) {
                case 1:
                    mGender = "男";
                    break;
                case 2:
                    mGender = "女";
                    break;
            }
            mAge = data.getInfo().getAge() + "";
            mPost = data.getInfo().getJob();
            mWorkTime = data.getInfo().getWorkinghours();
            mCarNumber = data.getInfo().getNumberplates();
            mPhone = data.getInfo().getPhone();
            mOther = data.getInfo().getRemarks();
            if (!TextUtils.isEmpty(mName)) {
                tvName.setText(mName);
                tvName.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            }
            if (!TextUtils.isEmpty(mAvatarUrl)) {
                if (mAvatarUrl.startsWith("/images")) {
                    Glide.with(context).load(Constant.DOMIN + mAvatarUrl)
                            .centerCrop().into(avatar);
                }
            }
            if (!TextUtils.isEmpty(mGender)) {
                tvGender.setText(mGender);
                tvGender.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            }
            if (!TextUtils.isEmpty(mAge)) {
                tvAge.setText(mAge + "岁");
                tvAge.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            }
            if (!TextUtils.isEmpty(mHeight)) {
                tvHeight.setText(mHeight + "cm");
                tvHeight.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            }
            if (!TextUtils.isEmpty(mPost)) {
                tvPost.setText(mPost);
                tvPost.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            }
            if (!TextUtils.isEmpty(mWorkTime)) {
                tvWorkTime.setText(mWorkTime);
                tvWorkTime.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));

                String[] time = mWorkTime.split("-");
                if (time.length > 1) {
                    startTime = time[0];
                    endTime = time[1];
                }


            }
            if (!TextUtils.isEmpty(mCarNumber)) {
                tvCarNumber.setText(mCarNumber);
                tvCarNumber.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            }
            if (!TextUtils.isEmpty(mOther)) {
                tvOther.setText(mOther);
                tvOther.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            }


        }

    }

    @OnClick({R.id.img_back, R.id.checkbox_avatar_show_state, R.id.rl_avatar_img, R.id.rl_name,
            R.id.rl_gender, R.id.rl_height, R.id.rl_age, R.id.rl_post, R.id.rl_work_time, R.id.rl_car_number,
            R.id.rl_phone, R.id.rl_other, R.id.tv_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:

                onBackPressed();
                break;
            case R.id.checkbox_avatar_show_state:
                if (mCheckBox.isChecked()) {
                    tvAvatar.setText("显示头像");
                    avatar.setVisibility(View.VISIBLE);

                } else {
                    avatar.setVisibility(View.INVISIBLE);

                }


                break;
            case R.id.rl_avatar_img:
                showUploadDialog();

                break;
            case R.id.rl_name:
                showInputNameDialog();


                break;
            case R.id.rl_gender:
                showSetGenderDialog();

                break;
            case R.id.rl_height:
                showSetHeightDialog();

                break;
            case R.id.rl_age:

                showAgeDialog();
                break;
            case R.id.rl_post:
                showSetPostDialog();


                break;
            case R.id.rl_work_time:
                showSetWorkTimeDialog();


                break;
            case R.id.rl_car_number:
                showSetCarNumber();

                break;
            case R.id.rl_phone:
                showSetPhoneDialog();


                break;
            case R.id.rl_other:
                showSetOtherDialog();

                break;
            case R.id.tv_save:
                String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
                System.out.println("路径：" + mAvatarUrl + "\nToken:" + token);
//                System.out.println("id："+mId);
//                File mFile=new File(mAvatarUrl);
//
//                System.out.println("file:"+mFile.length());
                String mSex = null;
                switch (mGender) {
                    case "男":
                        mSex = "1";

                        break;
                    case "女":
                        mSex = "2";

                        break;
                }

                updatePersonalInformation(mAvatarUrl, token, mAge, mHeight, mId, mPost, mName, mCarNumber,
                        mPhone, projectId, mSex, mOther, mWorkTime);


                break;
        }
    }

    private void showSetOtherDialog() {
        View view = View.inflate(context, R.layout.dialog_location_now, null);
        TextView dialogTitle = (TextView) view.findViewById(R.id.title_dialog);
        EditText etDialog = (EditText) view.findViewById(R.id.edit_address);
        etDialog.setHint("请补充其他特征");
        dialogTitle.setText("其他描述");
        if (!TextUtils.isEmpty(mOther)) {
            etDialog.setText(mOther);

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.getWindow().findViewById(R.id.btn_next).setOnClickListener(v -> {
            if (!TextUtils.isEmpty(etDialog.getText().toString())) {
                dialog.dismiss();
                Snackbar.make(rootView, "设置成功！", Snackbar.LENGTH_SHORT).show();
                mOther = etDialog.getText().toString();
                tvOther.setText(mOther);
                tvOther.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            } else {
                Toast.makeText(context, "请输入其他描述", Toast.LENGTH_SHORT).show();
            }

        });

    }

    //设置手机
    private void showSetPhoneDialog() {
        View view = View.inflate(context, R.layout.dialog_set_input_content, null);
        TextView dialogTitle = (TextView) view.findViewById(R.id.title_dialog);
        EditText etDialog = (EditText) view.findViewById(R.id.et_input_content);
        etDialog.setHint("请输入手机号");
        dialogTitle.setText("设置手机号");
        if (!TextUtils.isEmpty(mPhone)) {
            etDialog.setText(mPhone);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.button_project_cancel).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.getWindow().findViewById(R.id.button_project_ok).setOnClickListener(v -> {
            if (Utils.isMobile(etDialog.getText().toString())) {
                dialog.dismiss();
                Snackbar.make(rootView, "手机号设置成功！", Snackbar.LENGTH_SHORT).show();
                mPhone = etDialog.getText().toString();
                tvPhone.setText(mPhone);
                tvPhone.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            } else {
                Toast.makeText(context, "请输入正确手机号", Toast.LENGTH_SHORT).show();
            }

        });
    }

    //设置车牌
    private void showSetCarNumber() {

        View view = View.inflate(context, R.layout.dialog_set_input_content, null);
        TextView dialogTitle = (TextView) view.findViewById(R.id.title_dialog);
        EditText etDialog = (EditText) view.findViewById(R.id.et_input_content);
        etDialog.setHint("请输入车牌号");
        dialogTitle.setText("设置车牌号");
        if (!TextUtils.isEmpty(mCarNumber)) {
            etDialog.setText(mCarNumber);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.button_project_cancel).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.getWindow().findViewById(R.id.button_project_ok).setOnClickListener(v -> {
            if (!TextUtils.isEmpty(etDialog.getText().toString())) {
                dialog.dismiss();
                Snackbar.make(rootView, "车牌号设置成功！", Snackbar.LENGTH_SHORT).show();
                mCarNumber = etDialog.getText().toString();
                tvCarNumber.setText(mCarNumber);
                tvCarNumber.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            } else {
                Toast.makeText(context, "请输入车牌号", Toast.LENGTH_SHORT).show();
            }

        });

    }

    //设置工作时间
    private void showSetWorkTimeDialog() {
        View view = View.inflate(context, R.layout.dialog_set_work_time, null);
        TextView tvStartTime = (TextView) view.findViewById(R.id.tv_start_time);
        TextView tvEndTime = (TextView) view.findViewById(R.id.tv_end_time);

        if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
            tvStartTime.setText(startTime);
            tvEndTime.setText(endTime);

        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();

        dialog.show();
        dialog.getWindow().findViewById(R.id.ly_start_time).setOnClickListener(v ->
                showTimeStartDialog(tvStartTime, 0));
        dialog.getWindow().findViewById(R.id.ly_end_time).setOnClickListener(v -> {
            showTimeStartDialog(tvEndTime, 1);

        });
        dialog.getWindow().findViewById(R.id.button_project_cancel).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.getWindow().findViewById(R.id.button_project_ok).setOnClickListener(v -> {
            if (!TextUtils.isEmpty(startTime) && !TextUtils.isEmpty(endTime)) {
                dialog.dismiss();
                mWorkTime=startTime+"-"+endTime;
                tvWorkTime.setText(startTime + "-" + endTime);
                tvWorkTime.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            } else {
                Toast.makeText(context, "请设置工作的开始和结束时间", Toast.LENGTH_SHORT).show();

            }

        });
    }

    //选择时间
    private void showTimeStartDialog(TextView textView, int type) {
        View view = View.inflate(context, R.layout.dialog_alert_start_time, null);
        TextView dialogTitle = (TextView) view.findViewById(R.id.tv_dialog_title);
        dialogTitle.setText("设置工作时间");
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();
        Button addHour = (Button) dialog.getWindow().findViewById(R.id.btn_add_hour);
        Button subHour = (Button) dialog.getWindow().findViewById(R.id.btn_subtract_hour);
        EditText editHour = (EditText) dialog.getWindow().findViewById(R.id.et_hour);
        Button addMinute = (Button) dialog.getWindow().findViewById(R.id.btn_add_minute);
        Button subMinute = (Button) dialog.getWindow().findViewById(R.id.btn_subtract_minute);
        EditText editMinute = (EditText) dialog.getWindow().findViewById(R.id.edit_minute);
        Button btnOk = (Button) dialog.getWindow().findViewById(R.id.btn_ok);


        addHour.setOnClickListener(v -> {
            String hour = editHour.getText().toString();
            if (TextUtils.isEmpty(hour)) {
                hour = "0";
            }

            if (Integer.parseInt(hour) >= 23) {
                Toast.makeText(context, "不能超过24小时", Toast.LENGTH_LONG).show();

            } else {
                if (Integer.parseInt(hour) >= 9) {
                    editHour.setText(String.valueOf(Integer.parseInt(hour) + 1));
                } else {
                    editHour.setText("0" + String.valueOf(Integer.parseInt(hour) + 1));
                }
            }
        });


        subHour.setOnClickListener(v -> {
            String hour = editHour.getText().toString();
            if (TextUtils.isEmpty(hour)) {
                hour = "0";
            }
            if (Integer.parseInt(hour) <= 0) {
                Toast.makeText(context, "不能小于0时", Toast.LENGTH_LONG).show();
            } else {
                if (Integer.parseInt(hour) >= 11) {
                    editHour.setText(String.valueOf(Integer.parseInt(hour) - 1));
                } else {
                    editHour.setText("0" + String.valueOf(Integer.parseInt(hour) - 1));
                }
            }

        });


        addMinute.setOnClickListener(v -> {
            String minute = editMinute.getText().toString();

            if (TextUtils.isEmpty(minute)) {
                minute = "0";
            }
            if (Integer.parseInt(minute) >= 59) {
                Toast.makeText(context, "不能超过60分钟", Toast.LENGTH_LONG).show();

            } else {
                if (Integer.parseInt(minute) >= 9) {
                    editMinute.setText(String.valueOf(Integer.parseInt(minute) + 1));
                } else {
                    editMinute.setText("0" + String.valueOf(Integer.parseInt(minute) + 1));
                }
            }
        });


        subMinute.setOnClickListener(v -> {
            String minute = editMinute.getText().toString();

            if (TextUtils.isEmpty(minute)) {
                minute = "0";
            }

            if (Integer.parseInt(minute) <= 0) {
                Toast.makeText(context, "不能少于0分钟", Toast.LENGTH_LONG).show();

            } else {
                if (Integer.parseInt(minute) >= 11) {
                    editMinute.setText(String.valueOf(Integer.parseInt(minute) - 1));
                } else {
                    editMinute.setText("0" + String.valueOf(Integer.parseInt(minute) - 1));
                }

            }

        });

        btnOk.setOnClickListener(v -> {
            String hour = editHour.getText().toString();
            String minute = editMinute.getText().toString();
            if (!TextUtils.isEmpty(hour) && !TextUtils.isEmpty(minute)) {
                if (Integer.parseInt(hour) >= 24 || Integer.parseInt(hour) < 0) {
                    Toast.makeText(context, "请设置正确的小时！", Toast.LENGTH_LONG).show();
                } else {
                    if (Integer.parseInt(minute) >= 60 || Integer.parseInt(minute) < 0) {
                        Toast.makeText(context, "请设置正确的分钟！", Toast.LENGTH_LONG).show();
                    } else {
                        dialog.dismiss();
                        switch (type) {
                            case 0:
                                startTime = hour + ":" + minute;
                                break;
                            case 1:
                                endTime = hour + ":" + minute;

                                break;
                        }

                        textView.setText(hour + ":" + minute);
                        textView.setTextColor(ContextCompat.getColor
                                (context, R.color.titleColor));
                    }
                }
            } else {
                Toast.makeText(context, "请设置工作的时间", Toast.LENGTH_LONG).show();
            }

        });
    }

    //设置职务
    private void showSetPostDialog() {
        View view = View.inflate(context, R.layout.dialog_set_input_content, null);
        TextView dialogTitle = (TextView) view.findViewById(R.id.title_dialog);
        EditText etDialog = (EditText) view.findViewById(R.id.et_input_content);
        etDialog.setHint("请输入职务 例如：总经理");
        dialogTitle.setText("设置职务");
        if (!TextUtils.isEmpty(mPost)) {
            etDialog.setText(mPost);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.button_project_cancel).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.getWindow().findViewById(R.id.button_project_ok).setOnClickListener(v -> {
            if (!TextUtils.isEmpty(etDialog.getText().toString())) {
                dialog.dismiss();
                Snackbar.make(rootView, "职务设置成功！", Snackbar.LENGTH_SHORT).show();
                mPost = etDialog.getText().toString();
                tvPost.setText(mPost);
                tvPost.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            } else {
                Toast.makeText(context, "请输入职务", Toast.LENGTH_SHORT).show();
            }

        });
    }

    //设置年龄
    private void showAgeDialog() {
        View view = View.inflate(context, R.layout.dialog_set_input_content, null);
        TextView dialogTitle = (TextView) view.findViewById(R.id.title_dialog);
        EditText etDialog = (EditText) view.findViewById(R.id.et_input_content);
        etDialog.setHint("请输入年龄 例如：40");
        dialogTitle.setText("设置年龄");
        if (!TextUtils.isEmpty(mAge)) {
            etDialog.setText(mAge);
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.button_project_cancel).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.getWindow().findViewById(R.id.button_project_ok).setOnClickListener(v -> {
            if (!TextUtils.isEmpty(etDialog.getText().toString())) {
                dialog.dismiss();
                Snackbar.make(rootView, "年龄设置成功！", Snackbar.LENGTH_SHORT).show();
                mAge = etDialog.getText().toString();
                tvAge.setText(mAge + "岁");
                tvAge.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            } else {
                Toast.makeText(context, "请输入年龄", Toast.LENGTH_SHORT).show();
            }

        });
    }

    //设置身高
    private void showSetHeightDialog() {
        View view = View.inflate(context, R.layout.dialog_set_input_content, null);
        TextView dialogTitle = (TextView) view.findViewById(R.id.title_dialog);
        EditText etDialog = (EditText) view.findViewById(R.id.et_input_content);
        etDialog.setHint("请输入身高 例如：170");
        dialogTitle.setText("设置身高");
        if (!TextUtils.isEmpty(mHeight)) {
            etDialog.setText(mHeight);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.button_project_cancel).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.getWindow().findViewById(R.id.button_project_ok).setOnClickListener(v -> {
            if (!TextUtils.isEmpty(etDialog.getText().toString())) {
                dialog.dismiss();
                Snackbar.make(rootView, "身高设置成功！", Snackbar.LENGTH_SHORT).show();
                mHeight = etDialog.getText().toString();
                if (mHeight.endsWith("cm") || mHeight.endsWith("CM")) {
                    mHeight = mHeight.substring(0, mHeight.length() - 2);
                }
                tvHeight.setText(mHeight + "cm");
                tvHeight.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            } else {
                Toast.makeText(context, "请输入身高", Toast.LENGTH_SHORT).show();
            }

        });
    }

    //设置性别
    private void showSetGenderDialog() {
        View view = View.inflate(context, R.layout.dialog_select_gender, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();

        dialog.getWindow().findViewById(R.id.tv_man).setOnClickListener(v -> {
            dialog.dismiss();
            Snackbar.make(rootView, "性别设置成功", Snackbar.LENGTH_SHORT).show();
            mGender = "男";
            tvGender.setText(mGender);
            tvGender.setTextColor(ContextCompat.getColor
                    (context, R.color.titleColor));

        });

        dialog.getWindow().findViewById(R.id.tv_woman).setOnClickListener(v -> {
            dialog.dismiss();
            Snackbar.make(rootView, "性别设置成功", Snackbar.LENGTH_SHORT).show();
            mGender = "女";
            tvGender.setText(mGender);
            tvGender.setTextColor(ContextCompat.getColor
                    (context, R.color.titleColor));
        });

    }

    //修改姓名
    private void showInputNameDialog() {
        View view = View.inflate(context, R.layout.dialog_set_input_content, null);
        TextView dialogTitle = (TextView) view.findViewById(R.id.title_dialog);
        EditText etDialog = (EditText) view.findViewById(R.id.et_input_content);
        etDialog.setHint("请输入姓名");
        dialogTitle.setText("设置姓名");
        if (!TextUtils.isEmpty(mName)) {
            etDialog.setText(mName);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .setCancelable(true)
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.button_project_cancel).setOnClickListener(v -> {
            dialog.dismiss();
        });
        dialog.getWindow().findViewById(R.id.button_project_ok).setOnClickListener(v -> {
            if (!TextUtils.isEmpty(etDialog.getText().toString())) {
                dialog.dismiss();
                Snackbar.make(rootView, "姓名设置成功！", Snackbar.LENGTH_SHORT).show();
                mName = etDialog.getText().toString();
                tvName.setText(mName);
                tvName.setTextColor(ContextCompat.getColor
                        (context, R.color.titleColor));
            } else {
                Toast.makeText(context, "请输入姓名", Toast.LENGTH_SHORT).show();
            }

        });

    }

    private void showUploadDialog() {
        View view = View.inflate(context, R.layout.dialog_personal_avatar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.tv_camera_personal).setOnClickListener(v -> {
            dialog.dismiss();
            showPhotoSelectDialog();


        });
        dialog.getWindow().findViewById(R.id.tv_photo_personal).setOnClickListener(v -> {
            dialog.dismiss();
            startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"),
                    REQUEST_GALLERY_PHOTO);
        });


    }

    private void showPhotoSelectDialog() {
        imageUri = createImageUri(context);
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
        startActivityForResult(intent, TAKE_PHOTO_REQUEST);
    }

    private static Uri createImageUri(Context context) {
        String name = "takePhoto" + System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, name);
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        return uri;
    }

    /**
     * 裁剪图片
     *
     * @param resUri      图片uri
     * @param requestCode 请求码
     */
    private void startCropImage(Uri resUri, int requestCode) {


        File cropFile = new File(context.getCacheDir(), "a.jpg");


        UCrop.of(resUri, Uri.fromFile(cropFile))
                .withAspectRatio(1,1)
                .withMaxResultSize(100, 100)
                .start(KeyPersonalEditDetail.this, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case TAKE_PHOTO_REQUEST:
                    startCropImage(imageUri, RESPONSE_CODE_POSITIVE);

                    break;
                case RESPONSE_CODE_POSITIVE:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), avatar))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                avatar.setImageBitmap(bitmap);

                            });

                    avatarUrl = ImageUtils.getRealPath(context, UCrop.getOutput(data));

                    mAvatarFile = new File(avatarUrl);
                    uploadFile(mAvatarFile);


                    break;
                case REQUEST_GALLERY_PHOTO:

                    startCropImage(data.getData(), RESPONSE_CODE_NEGIVITE);
                    break;

                case RESPONSE_CODE_NEGIVITE:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), avatar))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                avatar.setImageBitmap(bitmap);

                            });
                    avatarUrl = ImageUtils.getRealPath(context, UCrop.getOutput(data));
                    mAvatarFile = new File(avatarUrl);
                    uploadFile(mAvatarFile);

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updatePersonalInformation(String img, String token, String age, String height, int id, String job,
                                           String name, String carNumber, String phone, String projectsId,
                                           String sex, String remarks, String workTime) {


        AddKeyPersonalInformation addKeyPersonalInformation = new AddKeyPersonalInformation(age, height,
                img, job, name, carNumber, phone, projectsId, sex, workTime, remarks, id);


        mSubscription = mRemoteService.updateKeyPersonal(token, addKeyPersonalInformation)
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
                            KeyContactList.InfoBean info = new KeyContactList.InfoBean(Integer.parseInt(sex),
                                    Integer.parseInt(age), Integer.parseInt(height),
                                    job, workTime, carNumber, remarks, phone);
                            KeyContactList list = new KeyContactList(id, name, img, info);
                            Intent mIntent = new Intent();
                            mIntent.putExtra("info", list);
                            // 设置结果，并进行传送
                            setResult(4,mIntent);
                            Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();

                            Single.just("").delay(1, TimeUnit.SECONDS).
                                    compose(RxUtils.applySchedulers()).
                                    subscribe(s -> finish()
                                    );


                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
    }

    //下载
    private void getData() {
        OkHttpUtils.get().url("http://118.178.88.132:8000/images/user/20170518/20170518171413YC1898026850.jpg")
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "a.jpg") {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(File response, int id) {
                        uploadFile(response);
                        System.out.println("fileName:" + response.getName());
                        System.out.println("fileSize:" + response.length());

                    }
                });
    }


    private void uploadFile(File file) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://118.178.88.132:8000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("filename", file.getName(),
                        RequestBody.create(MediaType.parse("image/jpg"), file))
                .build();
        Call<ApiResponse> call = userBiz.uploadFile(requestBody);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().getResultCode() == 200) {

                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(response.body().getData());
                        String fileUrl = gson.fromJson(jsonArray,
                                new TypeToken<String>() {
                                }.getType());
                        System.out.println("file:" + fileUrl);
                        mAvatarUrl = fileUrl;

                    } else {
                        Toast.makeText(context, response.body().getResultMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "请检查网络！",
                            Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {


            }
        });


    }


    public File compressior(File file) {
        return new Compressor.Builder(context)
                .setMaxWidth(300)
                .setMaxHeight(300)
                .setQuality(150)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build()
                .compressToFile(file);
    }
}
