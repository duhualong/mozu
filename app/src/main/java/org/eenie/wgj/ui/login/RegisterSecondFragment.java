package org.eenie.wgj.ui.login;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yalantis.ucrop.UCrop;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseFragment;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.Api;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.Infomation;
import org.eenie.wgj.model.response.RegisterSuccessData;
import org.eenie.wgj.ui.mytest.MyTestMain;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.ImageUtils;
import org.eenie.wgj.util.PermissionManager;
import org.eenie.wgj.util.RxUtils;
import org.eenie.wgj.util.Utils;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;
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
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static android.R.attr.maxWidth;
import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * Created by Eenie on 2017/4/14 at 13:21
 * Des:
 */

public class RegisterSecondFragment extends BaseFragment {
    private boolean checkFront;
    private boolean checkBack;
    private File fileCardFront;
    private File fileCardBack;
    private String frontUrl;
    private String backUrl;
    private File fileAvatarCard;
    private static final String PHONE = "phone";
    private static final String PWD = "pwd";
    private String mPhone;
    private String mPassword;
    private static final int REQUEST_CAMERA_PERMISSION = 2;
    private static final int REQUEST_CAMERA_PERMISSIONS = 4;

    private static final int TAKE_PHOTO_REQUEST_ONE = 1;
    private static final int TAKE_PHOTO_REQUEST_ONES = 3;

    private static final int RESPONSE_CODE_POSITIVE = 10;
    private static final int RESPONSE_CODE_NEGIVITE = 9;
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.img_positive)
    ImageView positiveImg;
    @BindView(R.id.img_negative)
    ImageView negativeImg;
    @BindView(R.id.btn_submitbtn)
    Button btnSubmibtn;

    private Uri imageUri;
    private AlertDialog.Builder mBuilder;
    private File mFileNews;

    @Override
    protected int getContentView() {
        return R.layout.fragment_register_second;
    }

    @Override
    protected void updateUI() {
    }

    public static RegisterSecondFragment newInstance(String username, String password) {
        RegisterSecondFragment fragment = new RegisterSecondFragment();
        if (!TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)) {
            Bundle args = new Bundle();
            args.putString(PHONE, username);
            args.putString(PWD, password);
            fragment.setArguments(args);
            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            mPhone = getArguments().getString(PHONE);
            mPassword = getArguments().getString(PWD);
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @RequiresApi(api = Build.VERSION_CODES.CUPCAKE)
    @OnClick({R.id.btn_positive, R.id.img_back,
            R.id.btn_negative, R.id.btn_submitbtn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;

            case R.id.btn_positive:
                //startActivityForResult(new Intent(context,MyTestMain.class),111);

               checkPermission(TAKE_PHOTO_REQUEST_ONE, REQUEST_CAMERA_PERMISSION);
                break;

            case R.id.btn_negative:
                checkPermission(TAKE_PHOTO_REQUEST_ONES, REQUEST_CAMERA_PERMISSIONS);

                break;
            case R.id.btn_submitbtn:
                if (checkBack && checkFront) {
                    showDialog();
                } else {
                    Snackbar.make(rootView, "信息上传中,请稍后....", Snackbar.LENGTH_SHORT).show();
                }
                break;

        }
    }

    //dialog弹窗
    private void showDialog() {

        View view = View.inflate(context, R.layout.dialog_new_identity_card_information, null);


        CircleImageView avatar = (CircleImageView) view.findViewById(R.id.personal_sdv_avatar);
        TextView mName = (TextView) view.findViewById(R.id.tv_name);
        TextView mSex = (TextView) view.findViewById(R.id.tv_sex);
        TextView mNation = (TextView) view.findViewById(R.id.tv_nation);
        TextView mBirthday = (TextView) view.findViewById(R.id.tv_birthday);
        TextView mAddress = (TextView) view.findViewById(R.id.tv_address);
        TextView mIdentity = (TextView) view.findViewById(R.id.tv_identity_card);
        TextView mSignOffice = (TextView) view.findViewById(R.id.tv_sign_office);
        TextView mDeadline = (TextView) view.findViewById(R.id.tv_date_deadline);


        String base64 = mPrefsHelper.getPrefs().getString(Constants.AVATAR, "");
        try {
            File file = Utils.base64ToFile(base64, context);
            uploadFile(file, 2);

            Glide.with(context)
                    .load(file)
                    .asBitmap()
                    .centerCrop()
                    .into(avatar);
        } catch (IOException e) {
            e.printStackTrace();
        }
        mName.setText(getValues(Constants.NAME));
        mSex.setText(getValues(Constants.SEX));
        mNation.setText(getValues(Constants.NATION));
        mBirthday.setText(getValues(Constants.BIRTHDAY));
        mAddress.setText(getValues(Constants.ADDRESS));
        mIdentity.setText(getValues(Constants.CARD_IDENTITY));
        mSignOffice.setText(getValues(Constants.SIGN_OFFICE));
        mDeadline.setText(getValues(Constants.START_DATE).replaceAll("-", ":") + "—" +
                getValues(Constants.END_DATE).replaceAll("-", ":"));
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view)                //自定义的布局文件
                .setCancelable(false)
                .create();
        dialog.show();

        dialog.getWindow().findViewById(R.id.btn_next).setOnClickListener(v -> {

            registerInformationApply(mPhone, mPassword, getValues(Constants.NAME)
                    , getValues(Constants.SEX), getValues(Constants.BIRTHDAY),
                    getValues(Constants.ADDRESS), getValues(Constants.CARD_IDENTITY),
                    getValues(Constants.SIGN_OFFICE), getValues(Constants.START_DATE).
                            replaceAll("-", ":") + "—" + getValues(Constants.END_DATE).
                            replaceAll("-", ":"), getValues(Constants.FRONT_URL),
                    getValues(Constants.BACK_URL), getValues(Constants.AVATAR_URL));


            dialog.dismiss();


        });
        dialog.getWindow().findViewById(R.id.btn_cancel).setOnClickListener(v -> {

            dialog.dismiss(); //取消对话框

        });

    }
    private void registerInformationApply(String username, String password, String name,
                                          String gender, String birthday, String address,
                                          String number, String publisher, String validate,
                                          String id_card_positive, String id_card_negative,
                                          String id_card_head_image) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://118.178.88.132:8000/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("username", username)
                .addFormDataPart("password", Utils.md5(password))
                .addFormDataPart("name", name)
                .addFormDataPart("gender", gender)
                .addFormDataPart("birthday", birthday)
                .addFormDataPart("address", address)
                .addFormDataPart("number", number)
                .addFormDataPart("publisher", publisher)
                .addFormDataPart("validate", validate)
                .addFormDataPart("id_card_positive", id_card_positive)
                .addFormDataPart("id_card_negative", id_card_negative)
                .addFormDataPart("id_card_head_image", id_card_head_image)
                .build();
        Call<ApiResponse> call = userBiz.registerInforation(requestBody);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful()) {

                    if (response.body().getResultCode() == 200) {

                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(response.body().getData());
                        Log.d(TAG, "json: " + jsonArray);
                        RegisterSuccessData data = gson.fromJson(jsonArray,
                                new TypeToken<RegisterSuccessData>() {
                                }.getType());

                        Toast.makeText(context, "注册成功，请选择公司！", Toast.LENGTH_LONG).show();
//

                        Set<String> tags = new HashSet<>();
                        tags.add(Utils.md5(Utils.md5(String.valueOf(data.getUser_id()))));
                        JPushInterface.setTags(context, tags, (i, s, set) -> {
                            System.out.println("dddd:" + i);
                            if (i == 0) {

                                System.out.println("打印code" + i + "set:" + set);
                            }else {
                            }
                        });

                        Single.just("").delay(2, TimeUnit.SECONDS).compose(RxUtils.applySchedulers()).
                                subscribe(s ->
                                        fragmentMgr.beginTransaction()
                                                .addToBackStack(TAG)
                                                .replace(R.id.fragment_login_container,
                                                        SelectCompanyWayFragment.newInstance
                                                                (username, password,
                                                                        data.getUser_id(),
                                                                        data.getToken())).commit()
                                );

                    } else {
                        Toast.makeText(context, response.body().getResultMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(context, "注册失败", Toast.LENGTH_LONG).show();

                }


            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });


    }


    public String getValues(String key) {

        return mPrefsHelper.getPrefs().getString(key, "");
    }


    /**
     * 针对高版本系统检查权限
     */
    private void checkPermission(int code, int permission) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPermission = PermissionManager.checkCameraPermission(context)
                    && PermissionManager.checkWriteExternalStoragePermission(context);
            if (hasPermission) {
                switch (code){
                    case TAKE_PHOTO_REQUEST_ONE:
                        startActivityForResult(new Intent(context,MyTestMain.class),111);
                        break;
                    case TAKE_PHOTO_REQUEST_ONES:
                        startActivityForResult(new Intent(context,MyTestMain.class),222);

                        break;

                }

               // startCapturePhoto(code);

            } else {
                showRequestPermissionDialog(permission);
            }
        } else {
            switch (code){
                case TAKE_PHOTO_REQUEST_ONE:
                    startActivityForResult(new Intent(context,MyTestMain.class),111);
                    break;
                case TAKE_PHOTO_REQUEST_ONES:
                    startActivityForResult(new Intent(context,MyTestMain.class),222);

                    break;

            }
        }
    }

    private void startCapturePhoto(int code) {
        imageUri = createImageUri(context);
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
        startActivityForResult(intent, code);
    }


    /**
     * 请求权限Snackbar
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void showRequestPermissionDialog(int permission) {
        if (this.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Snackbar.make(rootView, "请提供摄像头及文件权限，以拍摄和预览相机图片!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", v -> {
                        PermissionManager.invokeCamera(RegisterSecondFragment.this, permission);
                    })
                    .show();
        } else {
            PermissionManager.invokeCamera(RegisterSecondFragment.this, permission);
        }
    }


    /**
     * 权限申请回调
     *
     * @param requestCode  请求码
     * @param permissions  请求权限
     * @param grantResults 请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:

                boolean isCanCapturePhoto = true;

                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        isCanCapturePhoto = false;
                    }
                }
                if (isCanCapturePhoto) {
                    startActivityForResult(new Intent(context,MyTestMain.class),111);

                   // startCapturePhoto(TAKE_PHOTO_REQUEST_ONE);
                } else {
                    Snackbar.make(rootView, "请完整的权限，以预览裁剪图片!", Snackbar.LENGTH_SHORT).show();
                }

                break;
            case REQUEST_CAMERA_PERMISSIONS:

                boolean isCanCapturePhotos = true;

                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        isCanCapturePhotos = false;
                    }
                }
                if (isCanCapturePhotos) {
                   // startCapturePhoto(TAKE_PHOTO_REQUEST_ONES);
                    startActivityForResult(new Intent(context,MyTestMain.class),222);

                } else {
                    Snackbar.make(rootView, "请完整的权限，以预览裁剪图片!", Snackbar.LENGTH_SHORT).show();
                }


                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
                .withMaxResultSize(maxWidth, 300)
                .start(context, this, requestCode);
    }

    public File compressior(File file) {
        return new Compressor.Builder(context)
                .setMaxWidth(150)
                .setMaxHeight(120)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build()
                .compressToFile(file);
    }
    public static Uri getImageContentUri(Context context, String filePath) {
        Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[] { MediaStore.Images.Media._ID }, MediaStore.Images.Media.DATA + "=? ",
                new String[] { filePath }, null);
        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {

                return null;

        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 111:

                    String url= data.getStringExtra("uri");

                   startCropImage(Uri.parse(url),RESPONSE_CODE_POSITIVE);
                    break;
                case 222:
                    String urls= data.getStringExtra("uri");
                    startCropImage(Uri.parse(urls),RESPONSE_CODE_NEGIVITE);
                    break;
                case TAKE_PHOTO_REQUEST_ONE:
                    startCropImage(imageUri, RESPONSE_CODE_POSITIVE);

                    break;
                case RESPONSE_CODE_POSITIVE:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), positiveImg))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                positiveImg.setImageBitmap(bitmap);
                            });
                    frontUrl = ImageUtils.getRealPath(context, UCrop.getOutput(data));

                    Snackbar.make(rootView, "身份证正面上传成功！", Snackbar.LENGTH_LONG).show();
                    fileCardFront = new File(frontUrl);
                    uploadFile(fileCardFront, 0);

                    new Thread() {
                        public void run() {
                            initData(Compressor.getDefault(context).compressToFile(fileCardFront), "2");

                        }
                    }.start();

                    break;
                case TAKE_PHOTO_REQUEST_ONES:
                    startCropImage(imageUri, RESPONSE_CODE_NEGIVITE);

                    break;
                case RESPONSE_CODE_NEGIVITE:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), negativeImg))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                negativeImg.setImageBitmap(bitmap);
                            });
                    backUrl = ImageUtils.getRealPath(context, UCrop.getOutput(data));
                    // String imgUrls = ImageUtils.getRealPath(context, imageUri);
                    // negativeImg.setImageURI(imageUri);
                    Snackbar.make(rootView, "身份证背面上传成功！", Snackbar.LENGTH_LONG).show();
                    fileCardBack = new File(backUrl);
                    uploadFile(fileCardFront, 1);
                    new Thread() {
                        public void run() {
                            initData(Compressor.getDefault(context).compressToFile(fileCardBack), "3");

                        }
                    }.start();

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void uploadFile(File file, int type) {
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
                Log.d(TAG, "onResponseCode: " + response.code());
                if (response.isSuccessful()) {
                    if (response.body().getResultCode() == 200) {

                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(response.body().getData());
                        String fileUrl = gson.fromJson(jsonArray,
                                new TypeToken<String>() {
                                }.getType());

                        System.out.println("上传成功" + response.body().getData());
                        switch (type) {
                            case 0:
                                mPrefsHelper.getPrefs().edit().
                                        putString(Constants.FRONT_URL, fileUrl).apply();


                                break;
                            case 1:
                                mPrefsHelper.getPrefs().edit().
                                        putString(Constants.BACK_URL, fileUrl).apply();
                                break;
                            case 2:
                                mPrefsHelper.getPrefs().edit().
                                        putString(Constants.AVATAR_URL, fileUrl).apply();

                                break;
                        }
                    } else {
                        Toast.makeText(context, response.body().getResultMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "请检查网络！",
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);

            }
        });


    }

    private void initData(File files, String typeId) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://netocr.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                .addFormDataPart("key", Constants.OCRKey)
                .addFormDataPart("secret", Constants.OCRSecret)
                .addFormDataPart("typeId", typeId)
                .addFormDataPart("format", "json")
                .addFormDataPart("file", files.getName(), RequestBody.create(MediaType.parse("image/jpg"), files))
                .build();
        Call<Api> call = userBiz.upLoad(requestBody);
        call.enqueue(new Callback<Api>() {
            @Override
            public void onResponse(Call<Api> call, Response<Api> response) {
                int status = response.body().getMessage().getStatus();
                System.out.println("status:" + status + "错误信息：" +
                        response.body().getMessage().getValue());
                if (status == 2) {

                    checkFront = true;
                    List<Infomation> data = response.body().getCardsinfo();
                    if (data != null) {
                        Infomation infomation = data.get(0);
                        List<Infomation.Item> items = infomation.getItems();
                        for (Infomation.Item item : items) {
                            switch (item.getDesc()) {
                                case "姓名":
                                    String name = item.getContent();
                                    if (!TextUtils.isEmpty(name)) {
                                        mPrefsHelper.getPrefs().edit().
                                                putString(Constants.NAME, name).apply();
                                    }
                                    System.out.println("name:" + name);
                                    break;

                                case "性别":
                                    String sex = item.getContent();
                                    if (!TextUtils.isEmpty(sex)) {
                                        mPrefsHelper.getPrefs().edit().
                                                putString(Constants.SEX, sex).apply();
                                    }
                                    System.out.println("性别:" + sex);
                                    break;

                                case "公民身份号码":
                                    String number = item.getContent();
                                    if (!TextUtils.isEmpty(number)) {
                                        mPrefsHelper.getPrefs().edit().
                                                putString(Constants.CARD_IDENTITY, number).apply();
                                    }
                                    System.out.println("号码:" + number);
                                    break;

                                case "头像":
                                    String avatar = item.getContent();
                                    if (!TextUtils.isEmpty(avatar)) {

                                        mPrefsHelper.getPrefs().edit().
                                                putString(Constants.AVATAR, avatar).apply();

                                    }
                                    System.out.println("头像:" + avatar);
                                    break;
                                case "出生":
                                    String birthday = item.getContent();
                                    if (!TextUtils.isEmpty(birthday)) {
                                        mPrefsHelper.getPrefs().edit().
                                                putString(Constants.BIRTHDAY, birthday).apply();
                                    }
                                    System.out.println("出生:" + birthday);
                                    break;
                                case "住址":
                                    String address = item.getContent();
                                    if (!TextUtils.isEmpty(address)) {

                                        mPrefsHelper.getPrefs().edit().
                                                putString(Constants.ADDRESS, address).apply();
                                    }

                                    System.out.println("地址:" + address);
                                    break;
                                case "民族":
                                    String nation = item.getContent();
                                    if (!TextUtils.isEmpty(nation)) {

                                        mPrefsHelper.getPrefs().edit().
                                                putString(Constants.NATION, nation).apply();
                                    }

                                    System.out.println("民族:" + nation);

                                    break;


                            }
                        }

                    }
                    if (checkFront && checkBack) {
                        btnSubmibtn.setBackgroundResource(R.mipmap.bg_submit_button);
                    } else {
                        btnSubmibtn.setBackgroundResource(R.mipmap.bg_btnbukedianji);
                    }
                } else if (status == 3) {
                    checkBack = true;

                    List<Infomation> data = response.body().getCardsinfo();
                    if (data != null) {
                        Infomation infomation = data.get(0);
                        List<Infomation.Item> items = infomation.getItems();
                        for (Infomation.Item item : items) {

                            switch (item.getDesc()) {
                                case "签发机关":
                                    String signOffice = item.getContent();
                                    if (!TextUtils.isEmpty(signOffice)) {
                                        mPrefsHelper.getPrefs().edit().
                                                putString(Constants.SIGN_OFFICE, signOffice).apply();
                                    }
                                    System.out.println("机关:" + signOffice);
                                    break;

                                case "签发日期":
                                    String startDate = item.getContent();
                                    if (!TextUtils.isEmpty(startDate)) {
                                        mPrefsHelper.getPrefs().edit().
                                                putString(Constants.START_DATE, startDate).apply();
                                    }
                                    System.out.println("startTime:" + startDate);
                                    break;

                                case "有效期至":
                                    String endTime = item.getContent();
                                    if (!TextUtils.isEmpty(endTime)) {
                                        mPrefsHelper.getPrefs().edit().
                                                putString(Constants.END_DATE, endTime).apply();
                                    }
                                    System.out.println("endTime:" + endTime);
                                    break;

                            }
                        }

                    }
                    if (checkFront && checkBack) {
                        btnSubmibtn.setBackgroundResource(R.mipmap.bg_submit_button);
                    } else {
                        btnSubmibtn.setBackgroundResource(R.mipmap.bg_btnbukedianji);
                    }
                } else {
                    if (typeId.equals("2")) {
                        Snackbar.make(rootView, "身份证正面识别失败，请重新上传！", Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(rootView, "身份证背面识别失败，请重新上传！", Snackbar.LENGTH_LONG).show();
                    }
                }


            }

            @Override
            public void onFailure(Call<Api> call, Throwable t) {
                Snackbar.make(rootView, "请检查网络是否可用！", Snackbar.LENGTH_LONG).show();

            }
        });


    }


    private static Uri createImageUri(Context context) {
        String name = "takePhoto" + System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, name);
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".jpeg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        return uri;
    }

}
