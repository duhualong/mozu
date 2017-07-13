package org.eenie.wgj.ui.project.exchangework;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.ExchangeWorkList;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.ImageUtils;
import org.eenie.wgj.util.PhotoUtil;
import org.eenie.wgj.util.RxUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
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

import static org.eenie.wgj.R.id.tv_camera_personal;

/**
 * Created by Eenie on 2017/5/21 at 14:23
 * Email: 472279981@qq.com
 * Des:
 */

public class AddExchangeWorkActivity extends BaseActivity {
    private static final int REQUEST_CAMERA_FIRST=11;
    private static final int REQUEST_CAMERA_SECOND=12;
    private static final int REQUEST_CAMERA_THIRD=13;
    private static final int REQUEST_PHOTO_FIRST=14;
    private static final int REQUEST_PHOTO_SECOND=15;
    private static final int REQUEST_PHOTO_THIRD=16;
    private static final int RESPONSE_CODE_FIRST=17;
    private static final int RESPONSE_CODE_SECOND=18;
    private static final int RESPONSE_CODE_THIRD=19;
    public static final String INFO = "info";
    public static final String PROJECT_ID = "id";
    @BindView(R.id.root_view)
    View rootView;
    private ArrayList<ExchangeWorkList.ImageBean> lists;
    private ExchangeWorkList data;
    @BindView(R.id.et_input_work_title)
    EditText mInputTitle;
    @BindView(R.id.et_input_exchange_work_content)
    EditText mInputContent;

    @BindView(R.id.img_first)ImageView imgFist;
    @BindView(R.id.img_second)ImageView imgSecond;
    @BindView(R.id.img_third)ImageView imgThird;

    private int mId;
    private String mProjectId;
    private String mTitleName;
    private String mContent;
    private Uri mImageUri;
    private Uri mSecondUri;
    private Uri mThirdUri;
    private Uri mFirstUri;
    private String firstPath;
    private String secondPath;
    private String thirdPath;
    private File firstFile;
    private File secondFile;
    private File thirdFile;

    @Override
    protected int getContentView() {
        return R.layout.activity_edit_exchange_work;
    }

    @Override
    protected void updateUI() {

        mProjectId = getIntent().getStringExtra(PROJECT_ID);

    }

    @OnClick({R.id.img_back, R.id.tv_save, R.id.img_first, R.id.img_second, R.id.img_third})
    public void onClick(View view) {
        mContent = mInputContent.getText().toString();
        mTitleName=mInputTitle.getText().toString();

        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_save:
                List<File> files = new ArrayList<>();
                if (!TextUtils.isEmpty(mContent)&&!TextUtils.isEmpty(mTitleName)) {


                    if (firstFile != null) {
                        files.add(firstFile);
                    }
                    if (secondFile != null) {
                        files.add(secondFile);
                    }
                    if (thirdFile != null) {
                        files.add(thirdFile);
                    }
                    if (files.size() > 0) {
                        new Thread() {
                            public void run() {
                                addData(getMultipartBody(files, mProjectId, mTitleName, mContent),
                                        mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""));
                            }
                        }.start();

                    } else {
                        Toast.makeText(context, "请至少添加一张照片", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(context, "请输入交接班的名称或注意事项", Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.img_first:
                showUploadDialog(REQUEST_CAMERA_FIRST,REQUEST_PHOTO_FIRST);


                break;
            case R.id.img_second:
                showUploadDialog(REQUEST_CAMERA_SECOND,REQUEST_PHOTO_SECOND);


                break;
            case R.id.img_third:
                showUploadDialog(REQUEST_CAMERA_THIRD,REQUEST_PHOTO_THIRD);

                break;
        }
    }

    private void showUploadDialog(int camera,int photo) {
        View view = View.inflate(context, R.layout.dialog_personal_avatar, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        dialog.getWindow().findViewById(tv_camera_personal).setOnClickListener(v -> {
            dialog.dismiss();
            showPhotoSelectDialog(camera);
           // startCapturePhoto(camera);


        });

        dialog.getWindow().findViewById(R.id.tv_photo_personal).setOnClickListener(v -> {
            dialog.dismiss();
            startActivityForResult(new Intent(Intent.ACTION_PICK).setType("image/*"),
                    photo);
        });


    }

    private void showPhotoSelectDialog(int camera) {
        switch (camera){
            case REQUEST_CAMERA_FIRST:
                mFirstUri = createImageUri(context);
                Intent intent = new Intent();
                intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, mFirstUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
                startActivityForResult(intent, camera);

                break;
            case REQUEST_CAMERA_SECOND:
                mSecondUri = createImageUri(context);
                Intent intents = new Intent();
                intents.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intents.putExtra(MediaStore.EXTRA_OUTPUT, mSecondUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
                startActivityForResult(intents, camera);


                break;

            case REQUEST_CAMERA_THIRD:
                mThirdUri = createImageUri(context);
                Intent intentes = new Intent();
                intentes.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                intentes.putExtra(MediaStore.EXTRA_OUTPUT, mThirdUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
                startActivityForResult(intentes, camera);
                break;



        }



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
        switch (requestCode){
            case RESPONSE_CODE_FIRST:
                File cropFile = new File(context.getCacheDir(), "a.jpg");
                UCrop.of(resUri, Uri.fromFile(cropFile))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(500, 500)
                        .start(AddExchangeWorkActivity.this, requestCode);
                break;
            case RESPONSE_CODE_SECOND:
                File cropFiles = new File(context.getCacheDir(), "b.jpg");
                UCrop.of(resUri, Uri.fromFile(cropFiles))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(500, 500)
                        .start(AddExchangeWorkActivity.this, requestCode);
                break;
            case RESPONSE_CODE_THIRD:

                File mCropFiles = new File(context.getCacheDir(), "c.jpg");
                UCrop.of(resUri, Uri.fromFile(mCropFiles))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(500, 500)
                        .start(AddExchangeWorkActivity.this, requestCode);
                break;
        }



    }



    /**
     * 调用系统相机
     */
    private void startCapturePhoto(int requestCode) {
        File photoFile = PhotoUtil.createImageFile();
        boolean isCanCapturePhoto = PhotoUtil.isCanCapturePhoto(context, photoFile);
        if (isCanCapturePhoto) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
           Uri mPhotoUri = Uri.fromFile(photoFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, mPhotoUri);
            startActivityForResult(intent, requestCode);
        } else {
            Snackbar.make(rootView, "您的手机暂不支持相机拍摄，请选择相册图片！", Snackbar.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK ) {

            switch (requestCode) {
                case REQUEST_CAMERA_FIRST:
                    startCropImage(mFirstUri, RESPONSE_CODE_FIRST);

                    break;
                case REQUEST_PHOTO_FIRST:
                    startCropImage(data.getData(), RESPONSE_CODE_FIRST);
                    break;
                case RESPONSE_CODE_FIRST:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgFist))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgFist.setScaleType(ImageView.ScaleType.FIT_XY);
                                imgFist.setImageBitmap(bitmap);
                                imgSecond.setVisibility(View.VISIBLE);

                            });
                    firstPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));
                    firstFile=new File(firstPath);

                    break;
                case REQUEST_CAMERA_SECOND:
                    startCropImage(mSecondUri, RESPONSE_CODE_SECOND);
                    break;

                case REQUEST_PHOTO_SECOND:
                    startCropImage(data.getData(), RESPONSE_CODE_SECOND);
                    break;

                case RESPONSE_CODE_SECOND:

                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgSecond))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgSecond.setScaleType(ImageView.ScaleType.FIT_XY);
                                imgSecond.setImageBitmap(bitmap);
                               imgThird.setVisibility(View.VISIBLE);

                            });

                    secondPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));
                    secondFile=new File(secondPath);


                    break;

                case REQUEST_CAMERA_THIRD:
                    startCropImage(mThirdUri, RESPONSE_CODE_THIRD);
                    break;

                case REQUEST_PHOTO_THIRD:
                    startCropImage(data.getData(), RESPONSE_CODE_THIRD);
                    break;
                case RESPONSE_CODE_THIRD:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data),imgThird))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgThird.setScaleType(ImageView.ScaleType.FIT_XY);
                                imgThird.setImageBitmap(bitmap);

                            });
                    thirdPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));
                    thirdFile=new File(thirdPath);



//                    }

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void fitImageHolder(Intent intent, ImageView imageHolder) {
        Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(intent), imageHolder))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(imageHolder::setImageBitmap);
    }



    private void addData(RequestBody body, String token){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.DOMIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);

        Call<ApiResponse> call = userBiz.addExchangeWorkList(token,body);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("tag:", "onResponse: "+response.code());
                if (response.body().getResultCode()==200){
                    //会调数据

//                ExchangeWorkListResponse list=new ExchangeWorkListResponse(mId,mContent,mTitleName,lists);
//                Intent mIntent = new Intent();
//                mIntent.putExtra("exchange_work", list);
//                // 设置结果，并进行传送
//                setResult(4,mIntent);
                    Toast.makeText(context, "添加成功", Toast.LENGTH_SHORT).show();

                    Single.just("").delay(1, TimeUnit.SECONDS).
                            compose(RxUtils.applySchedulers()).
                            subscribe(s ->
                                  finish()
                            );

                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }
    public static MultipartBody getMultipartBody(List<File> files,String projectId,String title,
                                                 String content){
        MultipartBody.Builder builder=new MultipartBody.Builder();
        for (int i=0;i<files.size();i++){
            RequestBody requestBody=RequestBody.create(MediaType.parse("multipart/form-data"),files.get(i));
            builder.addFormDataPart("image[]",files.get(i).getName(),requestBody);

        }
        builder.addFormDataPart("projectid",projectId);
        builder.addFormDataPart("mattername",title);
        builder.addFormDataPart("matter",content);
        builder.setType(MultipartBody.FORM);
        return builder.build();

    }

}
