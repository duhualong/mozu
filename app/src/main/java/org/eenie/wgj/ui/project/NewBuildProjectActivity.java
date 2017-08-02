package org.eenie.wgj.ui.project;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

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
import org.eenie.wgj.model.requset.BuildNewProject;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.ImageUtils;
import org.eenie.wgj.util.RxUtils;

import java.io.File;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.OnClick;
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
 * Created by Eenie on 2017/5/16 at 16:45
 * Email: 472279981@qq.com
 * Des:
 */

public class NewBuildProjectActivity extends BaseActivity {
    private static final int TAKE_PHOTO_REQUEST = 0x101;
    private static final int RESPONSE_CODE_POSITIVE = 0x102;
    private static final int REQUEST_GALLERY_PHOTO = 0x103;
    private static final int RESPONSE_CODE_NEGIVITE = 0x104;
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.et_project_name)
    EditText etProjectName;
    @BindView(R.id.img_project)
    ImageView imgProject;
    @BindView(R.id.img_delete)
    ImageView imgDelete;
    @BindView(R.id.img_project_background)
    ImageView imgBackground;

    private String avatarUrl;
    private String fileUrl;
    private Uri imageUri;
    ImagePicker imagePicker = new ImagePicker();

    @Override
    protected int getContentView() {
        return R.layout.activity_new_build_project;
    }

    @Override
    protected void updateUI() {
        imagePicker.setTitle("添加图片");
// 设置是否裁剪图片
        imagePicker.setCropImage(true);

    }

    @OnClick({R.id.img_back, R.id.rl_upload, R.id.button_project_cancel, R.id.button_project_ok, R.id.img_delete})
    public void onClick(View view) {
        String projectName = etProjectName.getText().toString();

        switch (view.getId()) {
            case R.id.img_delete:
                if (!TextUtils.isEmpty(fileUrl)) {
                    imgProject.setScaleType(ImageView.ScaleType.CENTER);
                    imgProject.setImageResource(R.mipmap.ic_carmer_first);
                    imgDelete.setVisibility(View.GONE);
                    avatarUrl = "";
                    fileUrl = "";

                }

                break;
            case R.id.rl_upload:
                // showUploadDialogDialog();
                if (TextUtils.isEmpty(fileUrl)){
                    startChooser();
                }

                break;
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.button_project_cancel:
                onBackPressed();
                break;
            case R.id.button_project_ok:
                if (!TextUtils.isEmpty(projectName)) {
                    if (!TextUtils.isEmpty(fileUrl)) {
                        rebulidNewProject(projectName);
                    } else {
                        Toast.makeText(context, "请上传一张图片", Toast.LENGTH_SHORT).show();
                    }


                } else {
                    Toast.makeText(context, "请填写项目名称", Toast.LENGTH_SHORT).show();
                }

                break;
        }


    }


    private void startChooser() {
        // 启动图片选择器
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override
            public void onPickImage(Uri imageUri) {

            }

            // 裁剪图片回调
            @Override
            public void onCropImage(Uri imageUri) {
                imgProject.setScaleType(ImageView.ScaleType.CENTER_CROP);

                imgProject.setImageURI(imageUri);
                imgDelete.setVisibility(View.VISIBLE);
                avatarUrl = ImageUtils.getRealPath(context, imageUri);
                File fileCardFronts = new File(avatarUrl);
                uploadFile(fileCardFronts);


            }

            // 自定义裁剪配置
            @Override
            public void cropConfig(CropImage.ActivityBuilder builder) {
                builder
                        // 是否启动多点触摸
                        .setMultiTouchEnabled(false)
                        // 设置网格显示模式
                        .setGuidelines(CropImageView.Guidelines.OFF)
                        .setRequestedSize(320, 240);
                // 圆形/矩形
//            .setCropShape(CropImageView.CropShape.RECTANGLE);
                // 调整裁剪后的图片最终大小
//            .setRequestedSize(960, 540);
                // 宽高比
                //  .setAspectRatio(16, 9);
            }

            // 用户拒绝授权回调
            @Override
            public void onPermissionDenied(int requestCode, String[] permissions,
                                           int[] grantResults) {
            }
        });
    }


    private void rebulidNewProject(String projectName) {
        String token = mPrefsHelper.getPrefs().getString(Constants.TOKEN, "");
        BuildNewProject mProject = new BuildNewProject(projectName, fileUrl);
        if (!TextUtils.isEmpty(token)) {
            mSubscription = mRemoteService.newProject(token, mProject)
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
                                Snackbar.make(rootView, apiResponse.getResultMessage(),
                                        Snackbar.LENGTH_SHORT).show();
                                Single.just("").delay(1, TimeUnit.SECONDS).compose(RxUtils.applySchedulers()).
                                        subscribe(s -> finish()
                                        );

                            } else {
                                Snackbar.make(rootView, apiResponse.getResultMessage(),
                                        Snackbar.LENGTH_SHORT).show();
                            }

                        }
                    });

        }


    }

    private void showUploadDialogDialog() {
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
        File cropFile = new File(context.getCacheDir(), System.currentTimeMillis() + ".jpg");
        UCrop.of(resUri, Uri.fromFile(cropFile))
                .withAspectRatio(1, 1)
                .withMaxResultSize(200, 200)
                .start(NewBuildProjectActivity.this, requestCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == RESULT_OK) {
//
//            switch (requestCode) {
//                case TAKE_PHOTO_REQUEST:
//                    startCropImage(imageUri, RESPONSE_CODE_POSITIVE);
//
//                    break;
//                case RESPONSE_CODE_POSITIVE:
//                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgProject))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(bitmap -> {
//                                imgBackground.setVisibility(View.GONE);
//                                imgProject.setImageBitmap(bitmap);
//                                imgDelete.setVisibility(View.VISIBLE);
//                            });
//
//                    avatarUrl = ImageUtils.getRealPath(context, UCrop.getOutput(data));
//
//                    File fileCardFront = new File(avatarUrl);
//                    uploadFile(fileCardFront);
//
//                    break;
//                case REQUEST_GALLERY_PHOTO:
//
//
//                    startCropImage(data.getData(), RESPONSE_CODE_NEGIVITE);
//                    break;
//
//                case RESPONSE_CODE_NEGIVITE:
//                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgProject))
//                            .subscribeOn(Schedulers.io())
//                            .observeOn(AndroidSchedulers.mainThread())
//                            .subscribe(bitmap -> {
//                                imgBackground.setVisibility(View.GONE);
//                                imgProject.setImageBitmap(bitmap);
//                                imgDelete.setVisibility(View.VISIBLE);
//                            });
//                    avatarUrl = ImageUtils.getRealPath(context, UCrop.getOutput(data));
//                    File fileCardFronts = new File(avatarUrl);
//                    uploadFile(fileCardFronts);
//
//
//                    break;
//            }
//        }
        super.onActivityResult(requestCode, resultCode, data);
        imagePicker.onActivityResult(this, requestCode, resultCode, data);

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
                        fileUrl = gson.fromJson(jsonArray,
                                new TypeToken<String>() {
                                }.getType());

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
                .setMaxWidth(100)
                .setMaxHeight(100)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build()
                .compressToFile(file);
    }

}
