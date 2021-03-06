package org.eenie.wgj.ui.personal.information;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;
import com.yalantis.ucrop.UCrop;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.ModifyInfo;
import org.eenie.wgj.model.requset.UserId;
import org.eenie.wgj.model.response.UserInforById;
import org.eenie.wgj.ui.login.LoginActivity;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.ImageUtils;

import java.io.File;

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
import rx.SingleSubscriber;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static org.eenie.wgj.R.id.tv_photo_personal;

/**
 * Created by Eenie on 2017/4/25 at 10:53
 * Email: 472279981@qq.com
 * Des:
 */

public class PersonalBaseInfoActivity extends BaseActivity {
    private static final int TAKE_PHOTO_REQUEST = 0x101;
    private static final int REQUEST_GALLERY_PHOTO = 0x102;
    private static final int RESPONSE_CODE_POSITIVE = 0x103;
    private static final String TAG = "PersonalBaseInfoActivity";
    private Uri imageUri;
    private String avatarUrl;
    private String fileUrl;


    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.img_avatar)
    CircleImageView avatar;
    @BindView(R.id.bank_card)
    TextView bank;
    @BindView(R.id.security_code)
    TextView security;
    private String mBankCard;
    private String mSecurityCard;

    private ImagePicker imagePicker = new ImagePicker();
    private ImageView imageView;


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
                    .centerCrop()
                    .into(avatar);
        }
        mBankCard = mPrefsHelper.getPrefs().getString(Constants.BANK_CARD, "");

        mSecurityCard = mPrefsHelper.getPrefs().getString(Constants.SECURITY_CARD, "");
        if (!TextUtils.isEmpty(mBankCard)) {
            bank.setText(mBankCard);
            bank.setTextColor(ContextCompat.getColor
                    (context, R.color.titleColor));

        }
        if (!TextUtils.isEmpty(mSecurityCard)) {
            security.setText(mSecurityCard);
            security.setTextColor(ContextCompat.getColor
                    (context, R.color.titleColor));

        }
        // 设置是否裁剪图片
        imagePicker.setCropImage(true);

    }

    @OnClick({R.id.img_back, R.id.rl_scan, R.id.rl_avatar_img, R.id.rl_identity_card,
            R.id.rl_personal_information, R.id.rl_bank_card, R.id.rl_security_certificate,
            R.id.btn_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:

                onBackPressed();
                break;
            case R.id.rl_scan:
                startActivity(new Intent(context, PersonalBarcodeActivity.class));
                break;
            case R.id.rl_avatar_img:

                startCameraOrGallery();
                // showAvatarDialog();

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
                mPrefsHelper.clear();
//                mPrefsHelper.getPrefs().edit().putBoolean(Constants.IS_LOGIN, false).apply();
                startActivity(new Intent(context, LoginActivity.class));
                finish();

                break;

        }
    }


    private void startCameraOrGallery() {
        View view = View.inflate(context, R.layout.dialog_personal_avatar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();

        ImagePicker.Callback callback = new ImagePicker.Callback() {
            @Override
            public void onPickImage(Uri imageUri1) {
            }

            @Override
            public void onCropImage(Uri imageUri1) {
                avatar.setImageURI(imageUri1);
                File fileCardFront = new File(ImageUtils.getRealPath(context, imageUri1));
                uploadFile(fileCardFront);
            }
            /**
             * 图片裁剪配置
             */
            public void cropConfig(CropImage.ActivityBuilder builder){
                // 默认配置
                builder.setMultiTouchEnabled(false)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .setRequestedSize(300, 300)
                        .setAspectRatio(1, 1);
            }
        };

        dialog.getWindow().findViewById(R.id.tv_camera_personal).setOnClickListener(v -> {
            dialog.dismiss();
            imagePicker.startCamera(PersonalBaseInfoActivity.this, callback);

        });
        dialog.getWindow().findViewById(tv_photo_personal).setOnClickListener(v -> {
            dialog.dismiss();
            imagePicker.startGallery(PersonalBaseInfoActivity.this, callback);
        });


//        new AlertDialog.Builder(context).setTitle("上传头像")
//                .setItems(new String[] { "从相册中选取图片", "拍照" }, (dialog, which) -> {
//                    // 回调
//                    ImagePicker.Callback callback = new ImagePicker.Callback() {
//                        @Override public void onPickImage(Uri imageUri1) {
//                        }
//
//                        @Override public void onCropImage(Uri imageUri1) {
//                            avatar.setImageURI(imageUri1);
//                            File fileCardFront = new File(ImageUtils.getRealPath(context, imageUri1));
//                            uploadFile(fileCardFront);
//                        }
//                    };
//                    if (which == 0) {
//                        // 从相册中选取图片
//                        imagePicker.startGallery(PersonalBaseInfoActivity.this, callback);
//                    } else {
//                        // 拍照
//                        imagePicker.startCamera(PersonalBaseInfoActivity.this, callback);
//                    }
//                })
//                .show()
//                .getWindow()
//                .setGravity(Gravity.CENTER);
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
                                TextView mIdentity = (TextView) view.findViewById
                                        (R.id.tv_identity_card);
                                TextView mSignOffice = (TextView) view.findViewById
                                        (R.id.tv_sign_office);
                                TextView mDeadline = (TextView) view.findViewById
                                        (R.id.tv_date_deadline);
                                if (!TextUtils.isEmpty(mData.getId_card_head_image()) &&
                                        mData.getId_card_head_image() != null) {
                                    Glide.with(context)
                                            .load(Constant.DOMIN + mData.getId_card_head_image())
                                            .centerCrop()
                                            .into(avatar);
                                }

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
                                dialog.getWindow().findViewById(R.id.btn_ok).setOnClickListener(
                                        v -> dialog.dismiss());
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
            dialog.dismiss();
            showPhotoSelectDialog();


        });
        dialog.getWindow().findViewById(tv_photo_personal).setOnClickListener(v -> {
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
        File cropFile = new File(context.getCacheDir(), System.currentTimeMillis() + "jpg");
        UCrop.of(resUri, Uri.fromFile(cropFile))
                .withAspectRatio(1, 1)
                .withMaxResultSize(500, 500)
                .start(PersonalBaseInfoActivity.this, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

//        if (resultCode == RESULT_OK) {
//
//            switch (requestCode) {
//                case TAKE_PHOTO_REQUEST:
//                    if (imageUri!=null){
//                        startCropImage(imageUri, RESPONSE_CODE_POSITIVE);
//                    }
//
//
//                    break;
//                case REQUEST_GALLERY_PHOTO:
//                    if (data.getData()!=null){
//                        startCropImage(data.getData(), RESPONSE_CODE_POSITIVE);
//                    }
//                    break;
//
//                case RESPONSE_CODE_POSITIVE:
//                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), avatar))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(bitmap -> {
//                                avatar.setImageBitmap(bitmap);
//                            });
//                    avatarUrl = ImageUtils.getRealPath(context, UCrop.getOutput(data));
//
//                    File fileCardFront = new File(avatarUrl);
//                    uploadFile(fileCardFront);
//
//                    break;
//
////                case RESPONSE_CODE_NEGIVITE:
////                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), avatar))
////                            .subscribeOn(Schedulers.io())
////                            .observeOn(AndroidSchedulers.mainThread())
////                            .subscribe(bitmap -> {
////                                avatar.setImageBitmap(bitmap);
////                            });
////                    avatarUrl = ImageUtils.getRealPath(context, UCrop.getOutput(data));
////                    File fileCardFronts = new File(avatarUrl);
////                    uploadFile(fileCardFronts);
//
//
//                  //  break;
//            }
//        }
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(this, requestCode, resultCode, data);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        imagePicker.onRequestPermissionsResult(PersonalBaseInfoActivity.this,
                requestCode, permissions, grantResults);
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
                        System.out.println("图片路径：" + response.body().getData());

                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(response.body().getData());
                        fileUrl = gson.fromJson(jsonArray,
                                new TypeToken<String>() {
                                }.getType());
                        modifyInformation(fileUrl, mBankCard, mSecurityCard);

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

    private void modifyInformation(String avatarUrl, String bankCard, String securityCard) {
        ModifyInfo info = new ModifyInfo(avatarUrl, bankCard, securityCard);
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
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
                            if (!TextUtils.isEmpty(avatarUrl)) {
                                mPrefsHelper.getPrefs().edit().putString
                                        (Constants.PERSONAL_AVATAR, Constant.DOMIN + avatarUrl)
                                        .apply();

                            }


                        } else {
                            Snackbar.make(rootView, apiResponse.getResultMessage(),
                                    Snackbar.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    public File compressior(File file) {
        return new Compressor.Builder(context)
                .setMaxWidth(500)
                .setMaxHeight(500)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build()
                .compressToFile(file);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mBankCard = mPrefsHelper.getPrefs().getString(Constants.BANK_CARD, "");
        mSecurityCard = mPrefsHelper.getPrefs().getString(Constants.SECURITY_CARD, "");
        if (!TextUtils.isEmpty(mBankCard)) {
            bank.setText(mBankCard);
            bank.setTextColor(ContextCompat.getColor
                    (context, R.color.titleColor));

        }
        if (!TextUtils.isEmpty(mSecurityCard)) {
            security.setText(mSecurityCard);
            security.setTextColor(ContextCompat.getColor
                    (context, R.color.titleColor));

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mBankCard != null) {
            mBankCard = null;
        }
        if (mSecurityCard != null) {
            mSecurityCard = null;
        }
    }
}
