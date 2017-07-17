package org.eenie.wgj.ui.workshow;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.yalantis.ucrop.UCrop;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.AddWorkShow;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.ImageUtils;
import org.eenie.wgj.util.RxUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
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
import top.zibin.luban.Luban;
import top.zibin.luban.OnCompressListener;

import static android.R.attr.maxWidth;

/**
 * Created by Eenie on 2017/6/11 at 13:08
 * Email: 472279981@qq.com
 * Des:
 */

public class AddWorkShowActivity extends BaseActivity {
    private static final int REQUEST_CAMERA_FIRST = 0x101;
    private static final int REQUEST_CAMERA_SECOND = 0x102;
    private static final int REQUEST_CAMERA_THIRD = 0x103;
    private static final int REQUEST_PHOTO_FIRST = 0x104;
    private static final int REQUEST_PHOTO_SECOND = 0x105;
    private static final int REQUEST_PHOTO_THIRD = 0x106;
    private static final int RESPONSE_CODE_FIRST = 0x107;
    private static final int RESPONSE_CODE_SECOND = 0x108;
    private static final int RESPONSE_CODE_THIRD = 0x109;

    @BindView(R.id.et_input_work_title)
    EditText mInputTitle;
    @BindViews({R.id.img_first, R.id.img_second, R.id.img_third})
    List<ImageView> imgList;
    private String mTitleName;

    private Uri mImageUri;
    private String firstPath;
    private String secondPath;
    private String thirdPath;
    private File firstFile;
    private File secondFile;
    private File thirdFile;
    List<File> files = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_add_work_show;
    }

    @Override
    protected void updateUI() {


    }

    @OnClick({R.id.img_back, R.id.tv_send, R.id.img_first, R.id.img_second, R.id.img_third})
    public void onClick(View view) {
        mTitleName = mInputTitle.getText().toString();

        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_send:
                if (!TextUtils.isEmpty(mTitleName)) {
                    if (firstFile != null) {
                        files.add(firstFile);
                    }
                    if (secondFile != null) {
                        files.add(secondFile);
                    }
                    if (thirdFile != null) {
                        files.add(thirdFile);
                    }
                    if (files != null) {
                        new Thread() {
                            public void run() {
                                addData(getMultipartBody(files, mTitleName),
                                        mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""));
                            }
                        }.start();
                    } else {
                        Toast.makeText(context, "请至少选择一张图片", Toast.LENGTH_LONG).show();
                    }


                } else {
                    Toast.makeText(context, "请输入工作秀主题", Toast.LENGTH_LONG).show();
                }


                break;
            case R.id.img_first:
                showUploadDialog(REQUEST_CAMERA_FIRST, REQUEST_PHOTO_FIRST);


                break;
            case R.id.img_second:
                showUploadDialog(REQUEST_CAMERA_SECOND, REQUEST_PHOTO_SECOND);


                break;
            case R.id.img_third:
                showUploadDialog(REQUEST_CAMERA_THIRD, REQUEST_PHOTO_THIRD);

                break;
        }
    }

    private void showUploadDialog(int camera, int photo) {
        View view = View.inflate(context, R.layout.dialog_personal_avatar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.tv_camera_personal).setOnClickListener(v -> {
            dialog.dismiss();
            showPhotoSelectDialog(camera);


        });
        dialog.getWindow().findViewById(R.id.tv_photo_personal).setOnClickListener(v -> {
            dialog.dismiss();
            startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"),
                    photo);
        });


    }

    private void showPhotoSelectDialog(int camera) {
        mImageUri = createImageUri(context);
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
        startActivityForResult(intent, camera);
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
        switch (requestCode) {
            case RESPONSE_CODE_FIRST:
                File cropFile = new File(context.getCacheDir(), "a.jpg");
                UCrop.of(resUri, Uri.fromFile(cropFile))
                        .withAspectRatio(4,3)
                        .withMaxResultSize(maxWidth, 720)
                        .start(AddWorkShowActivity.this, requestCode);
                break;
            case RESPONSE_CODE_SECOND:
                File cropFiles = new File(context.getCacheDir(), "b.jpg");
                UCrop.of(resUri, Uri.fromFile(cropFiles))
                        .withAspectRatio(4,3)
                        .withMaxResultSize(maxWidth, 720)
                        .start(AddWorkShowActivity.this, requestCode);
                break;
            case RESPONSE_CODE_THIRD:
                File cropFiless = new File(context.getCacheDir(), "c.jpg");
                UCrop.of(resUri, Uri.fromFile(cropFiless))
                        .withAspectRatio(4,3)
                        .withMaxResultSize(maxWidth, 720)
                        .start(AddWorkShowActivity.this, requestCode);
                break;
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUEST_CAMERA_FIRST:
                    startCropImage(mImageUri, RESPONSE_CODE_FIRST);

                    break;
                case REQUEST_PHOTO_FIRST:
                    startCropImage(data.getData(), RESPONSE_CODE_FIRST);
                    break;
                case RESPONSE_CODE_FIRST:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgList.get(0)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgList.get(0).setScaleType(ImageView.ScaleType.FIT_XY);
                                imgList.get(0).setImageBitmap(bitmap);
                                imgList.get(1).setVisibility(View.VISIBLE);
                                imgList.get(1).setScaleType(ImageView.ScaleType.FIT_XY);

                            });
                    firstPath = ImageUtils.getRealPath(context, UCrop.getOutput(data));
                    firstFile = new File(firstPath);

                    break;
                case REQUEST_CAMERA_SECOND:
                    startCropImage(mImageUri, RESPONSE_CODE_SECOND);
                    break;

                case REQUEST_PHOTO_SECOND:
                    startCropImage(data.getData(), RESPONSE_CODE_SECOND);
                    break;

                case RESPONSE_CODE_SECOND:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data),
                            imgList.get(1)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgList.get(1).setScaleType(ImageView.ScaleType.FIT_XY);
                                imgList.get(1).setImageBitmap(bitmap);
                                imgList.get(2).setVisibility(View.VISIBLE);
                                imgList.get(2).setScaleType(ImageView.ScaleType.FIT_XY);
                            });
                    secondPath = ImageUtils.getRealPath(context, UCrop.getOutput(data));
                    secondFile = new File(secondPath);


                    break;

                case REQUEST_CAMERA_THIRD:
                    startCropImage(mImageUri, RESPONSE_CODE_THIRD);
                    break;

                case REQUEST_PHOTO_THIRD:
                    startCropImage(data.getData(), RESPONSE_CODE_THIRD);
                    break;

                case RESPONSE_CODE_THIRD:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data),
                            imgList.get(2)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgList.get(2).setScaleType(ImageView.ScaleType.FIT_XY);
                                imgList.get(2).setImageBitmap(bitmap);

                            });
                    thirdPath = ImageUtils.getRealPath(context, UCrop.getOutput(data));
                    thirdFile = new File(thirdPath);


                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addData(RequestBody body, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.DOMIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);

        Call<ApiResponse> call = userBiz.addWorkShowItem(token, body);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("tag:", "onResponse: " + response.code());
                if (response.body().getResultCode() == 200) {
                    Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();
                    Single.just("").delay(1, TimeUnit.SECONDS).
                            compose(RxUtils.applySchedulers()).
                            subscribe(s ->
                                    finish()
                            );
                }else {
                    Toast.makeText(context,response.body().getResultMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    public static MultipartBody getMultipartBody(List<File> files, String title) {
        AddWorkShow addWorkShow = new AddWorkShow(1, title);
        Gson gson = new Gson();
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < files.size(); i++) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                    files.get(i));
            builder.addFormDataPart("image[]", files.get(i).getName(), requestBody);
        }
        builder.addFormDataPart("data", gson.toJson(addWorkShow));
        builder.setType(MultipartBody.FORM);
        return builder.build();

    }

    private File CompressFile(File mFile) {
        final File[] files = new File[1];
        Luban.with(this)
                .load(mFile)                     //传人要压缩的图片
                .setCompressListener(new OnCompressListener() { //设置回调
                    @Override
                    public void onStart() {
                        // TODO 压缩开始前调用，可以在方法内启动 loading UI

                    }

                    @Override
                    public void onSuccess(File file) {
                        // TODO 压缩成功后调用，返回压缩后的图片文件
                        files[0] = file;
                        Log.d("鲁班压缩", "onSuccess: "+file.length()/1024+"kb");

                    }

                    @Override
                    public void onError(Throwable e) {
                        // TODO 当压缩过程出现问题时调用
                        files[0] = mFile;
                    }
                }).launch();    //启动压缩
        return files[0];


    }


}
