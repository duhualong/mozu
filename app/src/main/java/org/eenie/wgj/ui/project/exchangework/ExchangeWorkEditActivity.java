package org.eenie.wgj.ui.project.exchangework;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.ExchangeWorkList;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.ImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

/**
 * Created by Eenie on 2017/5/19 at 15:13
 * Email: 472279981@qq.com
 * Des:
 */

public class ExchangeWorkEditActivity extends BaseActivity {
    private static final int REQUEST_CAMERA_FIRST = 0x101;
    private static final int REQUEST_CAMERA_SECOND = 0x102;
    private static final int REQUEST_CAMERA_THIRD = 0x103;
    private static final int REQUEST_PHOTO_FIRST = 0x104;
    private static final int REQUEST_PHOTO_SECOND = 0x105;
    private static final int REQUEST_PHOTO_THIRD = 0x106;
    private static final int RESPONSE_CODE_FIRST = 0x107;
    private static final int RESPONSE_CODE_SECOND = 0x108;
    private static final int RESPONSE_CODE_THIRD = 0x109;
    public static final String INFO = "info";
    public static final String PROJECT_ID = "id";
    @BindView(R.id.root_view)
    View rootView;
    private ArrayList<ExchangeWorkList.ImageBean> lists = new ArrayList<>();
    private ExchangeWorkList data;
    @BindView(R.id.et_input_work_title)
    EditText mInputTitle;
    @BindView(R.id.et_input_exchange_work_content)
    EditText mInputContent;
    @BindViews({R.id.img_first, R.id.img_second, R.id.img_third})
    List<ImageView> imgList;

    @BindView(R.id.img_first)
    ImageView imgFist;
    @BindView(R.id.img_second)
    ImageView imgSecond;
    @BindView(R.id.img_third)
    ImageView imgThird;
    private int mId;
    private String mProjectId;
    private String mTitleName;
    private String mContent;
    private Uri mImageUri;
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
        data = getIntent().getParcelableExtra(INFO);
        mProjectId = getIntent().getStringExtra(PROJECT_ID);
        if (data != null) {
            mId = data.getId();
            mTitleName = data.getMattername();
            mContent = data.getMatter();
            lists = data.getImage();
            if (!TextUtils.isEmpty(mTitleName)) {
                mInputTitle.setText(mTitleName);
            }
            if (!TextUtils.isEmpty(mContent)) {
                mInputContent.setText(mContent);
            }
            if (lists.size() > 0) {
                if (lists.size() == 1) {
                    imgList.get(0).setVisibility(View.VISIBLE);

                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(0).getImage())
                            .centerCrop().into(imgList.get(0));
                    downloadImg(lists.get(0).getImage(), 0);
                } else if (lists.size() == 2) {
                    imgList.get(0).setVisibility(View.VISIBLE);
                    imgList.get(1).setVisibility(View.VISIBLE);
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(0).getImage())
                            .centerCrop().into(imgList.get(0));
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(1).getImage())
                            .centerCrop().into(imgList.get(1));
                    downloadImg(lists.get(0).getImage(), 0);
                    downloadImg(lists.get(1).getImage(), 1);
                } else if (lists.size() >= 3) {
                    imgList.get(0).setVisibility(View.VISIBLE);
                    imgList.get(1).setVisibility(View.VISIBLE);
                    imgList.get(2).setVisibility(View.VISIBLE);
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(0).getImage())
                            .centerCrop().into(imgList.get(0));
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(1).getImage())
                            .centerCrop().into(imgList.get(1));
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(2).getImage())
                            .centerCrop().into(imgList.get(2));
                    downloadImg(lists.get(0).getImage(), 0);
                    downloadImg(lists.get(1).getImage(), 1);
                    downloadImg(lists.get(2).getImage(), 2);
                }

            }


        }

    }

    @OnClick({R.id.img_back, R.id.tv_save, R.id.img_first, R.id.img_second, R.id.img_third})
    public void onClick(View view) {
        mContent = mInputContent.getText().toString();
        mTitleName = mInputTitle.getText().toString();

        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_save:
//                if (!TextUtils.isEmpty(mContent) && !TextUtils.isEmpty(mTitleName)){
//
//                    if (firstFile != null) {
//                        files.add(0, firstFile);
//                    }
//                    if (secondFile != null) {
//                        files.add(1, secondFile);
//                    }
//                    if (thirdFile != null) {
//                        files.add(2, thirdFile);
//                    }
//                    if (files .size()>0) {
//                        new Thread() {
//                            public void run() {
//                                editData(getMultipartBody(files,
//                                        mProjectId, mTitleName, mContent, mId + ""),
//                                        mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""));
//                            }
//                        }.start();
//
//                    }else {
//
//                    }
//                }else {
//
//                }
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
                                editData(getMultipartBody(files,
                                        mProjectId, mTitleName, mContent, mId + ""),
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
        switch (requestCode){

            case RESPONSE_CODE_FIRST:
                File cropFile = new File(context.getCacheDir(), "m.jpg");
                UCrop.of(resUri, Uri.fromFile(cropFile))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(500, 500)
                        .start(ExchangeWorkEditActivity.this, requestCode);

                break;
            case RESPONSE_CODE_SECOND:
                File cropFiles = new File(context.getCacheDir(), "n.jpg");
                UCrop.of(resUri, Uri.fromFile(cropFiles))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(500, 500)
                        .start(ExchangeWorkEditActivity.this, requestCode);
                break;
            case RESPONSE_CODE_THIRD:

                File mCropFiles = new File(context.getCacheDir(), "k.jpg");
                UCrop.of(resUri, Uri.fromFile(mCropFiles))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(500, 500)
                        .start(ExchangeWorkEditActivity.this, requestCode);
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
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgList.get(1)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgList.get(1).setScaleType(ImageView.ScaleType.FIT_XY);
                                imgList.get(1).setImageBitmap(bitmap);
                                imgList.get(2).setVisibility(View.VISIBLE);

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
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgList.get(2)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgList.get(2).setScaleType(ImageView.ScaleType.FIT_XY);
                                imgList.get(2).setImageBitmap(bitmap);


                            });
                    thirdPath = ImageUtils.getRealPath(context, UCrop.getOutput(data));
                    thirdFile = new File(thirdPath);


//                    }

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addData(String token, String pathOne, String pathTwo, String pathThree) {

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl(Constant.DOMIN_URL)
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//        FileUploadService userBiz = retrofit.create(FileUploadService.class);
//        List<File> files = new ArrayList<>();
//        if (!TextUtils.isEmpty(pathOne)&&!TextUtils.isEmpty(pathTwo)&&!TextUtils.isEmpty(pathThree)){
//
//            files.add(new File(pathOne));
//            files.add(new File(pathTwo));
//            files.add(new File(pathThree));
//
//        }
//
//
//
//        Call<ApiResponse> call = userBiz.addExchangeWorkList(token,getMultipartBody(files)
//        );
//        call.enqueue(new Callback<ApiResponse>() {
//            @Override
//            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
//                Log.d("tag:", "onResponse: "+response.code());
//                if (response.body().getResultCode()==200){
//                    Toast.makeText(context,"测试成功",Toast.LENGTH_LONG).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<ApiResponse> call, Throwable t) {
//
//            }
//        });


    }

    private void editData(RequestBody body, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.DOMIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);


        Call<ApiResponse> call = userBiz.editExchangeWorkList(token, body);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("tag:", "onResponse: " + response.code());
                if (response.body().getResultCode() == 200) {
                    //会调数据

//                ExchangeWorkListResponse list=new ExchangeWorkListResponse(mId,mContent,mTitleName,lists);
//                Intent mIntent = new Intent();
//                mIntent.putExtra("exchange_work", list);
//                // 设置结果，并进行传送
//                setResult(4,mIntent);
                    Toast.makeText(context, "编辑成功", Toast.LENGTH_SHORT).show();
                    finish();


//                    Single.just("").delay(1, TimeUnit.SECONDS).
//                            compose(RxUtils.applySchedulers()).
//                            subscribe(s ->
//                                    startActivity(new Intent(context,
//                                            ExchangeWorkSettingActivity.class).
//                                            putExtra(ExchangeWorkSettingActivity.PROJECT_ID, mProjectId))
//                            );

                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    public static MultipartBody getMultipartBody(List<File> files, String projectId, String title,
                                                 String content, String id) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < files.size(); i++) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), files.get(i));
            builder.addFormDataPart("image[]", files.get(i).getName(), requestBody);

        }
        builder.addFormDataPart("projectid", projectId);
        builder.addFormDataPart("mattername", title);
        builder.addFormDataPart("matter", content);
        builder.addFormDataPart("id", id);
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

    //下载
    private void downloadImg(String imgUrl, int position) {
        OkHttpUtils.get().url(Constant.DOMIN + imgUrl)
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "a.jpg") {
                    @Override
                    public void onError(okhttp3.Call call, Exception e, int id) {

                    }

                    @Override
                    public void onResponse(File response, int id) {

                        switch (position) {
                            case 0:
                                firstFile = response;

                                break;
                            case 1:
                                secondFile = response;

                                break;
                            case 2:
                                thirdFile = response;
                                break;

                        }


                    }
                });
    }


}
