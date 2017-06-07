package org.eenie.wgj.ui.project.roundpoint;

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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yalantis.ucrop.UCrop;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.UpdateRoundPoint;
import org.eenie.wgj.model.response.RoundPoint;
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
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/5/25 at 17:51
 * Email: 472279981@qq.com
 * Des:
 */

public class RoundPointEditActivity extends BaseActivity {
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
    private ArrayList<RoundPoint.ImageBean> lists;
    private RoundPoint data;
    @BindView(R.id.et_input_work_title)
    EditText mInputTitle;
    @BindView(R.id.et_input_exchange_work_content)
    EditText mInputContent;
    @BindViews({R.id.img_first, R.id.img_second, R.id.img_third})
    List<ImageView> imgList;
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
    List<File> files = new ArrayList<>();
    List<String> imgPath = new ArrayList<>();
    ArrayList<RoundPoint.ImageBean> mImageBeen = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_round_point_edit;
    }

    @Override
    protected void updateUI() {
        data = getIntent().getParcelableExtra(INFO);
        mProjectId = getIntent().getStringExtra(PROJECT_ID);
        if (data != null) {
            mId = data.getId();
            mTitleName = data.getInspectionname();
            mContent = data.getInspectioncontent();
            lists = data.getImage();
            if (!TextUtils.isEmpty(mTitleName)) {
                mInputTitle.setText(mTitleName);
            }
            if (!TextUtils.isEmpty(mContent)) {
                mInputContent.setText(mContent);
            }
            if (lists.size() > 0) {
                switch (lists.size()) {
                    case 1:
                        firstPath = lists.get(0).getImage();

                        break;
                    case 2:
                        firstPath = lists.get(0).getImage();
                        secondPath = lists.get(1).getImage();

                        break;
                    case 3:
                        firstPath = lists.get(0).getImage();
                        secondPath = lists.get(1).getImage();
                        thirdPath = lists.get(2).getImage();


                        break;
                }
                for (int i = 0; i < lists.size(); i++) {


                    if (i < 2) {
                        imgList.get(i + 1).setVisibility(View.VISIBLE);
                    }
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(i).getImage())
                            .centerCrop().into(imgList.get(i));

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
                if (!TextUtils.isEmpty(mContent) && !TextUtils.isEmpty(mTitleName))


                    if (!TextUtils.isEmpty(firstPath)) {
                        imgPath.add(0, firstPath);
                        RoundPoint.ImageBean imageBean = new RoundPoint.ImageBean(firstPath);
                        mImageBeen.add(0, imageBean);

                    }
                if (!TextUtils.isEmpty(secondPath)) {
                    imgPath.add(1, secondPath);
                    RoundPoint.ImageBean imageBean = new RoundPoint.ImageBean(firstPath);
                    mImageBeen.add(1, imageBean);
                }
                if (!TextUtils.isEmpty(thirdPath)) {
                    imgPath.add(2, thirdPath);
                    RoundPoint.ImageBean imageBean = new RoundPoint.ImageBean(firstPath);
                    mImageBeen.add(2, imageBean);

                }
                new Thread() {
                    public void run() {
                        if (!TextUtils.isEmpty(mTitleName) && !TextUtils.isEmpty(mContent)) {

                            UpdateRoundPoint roundPoint = new
                                    UpdateRoundPoint(mProjectId, mTitleName, mContent, imgPath, mId);
                            updateItem(roundPoint, mImageBeen);
                        } else {
                            Toast.makeText(context, "请补全内容", Toast.LENGTH_SHORT).show();
                        }

//                            editData(getMultipartBody(files,mProjectId,mTitleName,mContent,mId+""),
//                                    mPrefsHelper.getPrefs().getString(Constants.TOKEN,""));
                    }
                }.start();


//                ExchangeWorkList list=new ExchangeWorkList(1,"s","s",lists);
//                Intent mIntent = new Intent();
//                mIntent.putExtra("exchange_work", list);
//                // 设置结果，并进行传送
//                setResult(4,mIntent);

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
        File cropFile = new File(context.getCacheDir(), "a.jpg");
        UCrop.of(resUri, Uri.fromFile(cropFile))
                .withAspectRatio(1, 1)
                .withMaxResultSize(100, 100)
                .start(RoundPointEditActivity.this, requestCode);
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
                                imgList.get(1).setScaleType(ImageView.ScaleType.CENTER);


                            });
                    firstFile = new File(ImageUtils.getRealPath(context, UCrop.getOutput(data)));
                    uploadFile(firstFile, 0);


                    break;
                case REQUEST_CAMERA_SECOND:
                    startCropImage(mImageUri, RESPONSE_CODE_SECOND);

                case REQUEST_PHOTO_SECOND:
                    startCropImage(data.getData(), RESPONSE_CODE_SECOND);
                    break;

                case RESPONSE_CODE_SECOND:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgList.get(0)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgList.get(1).setScaleType(ImageView.ScaleType.FIT_XY);
                                imgList.get(1).setImageBitmap(bitmap);
                                imgList.get(2).setVisibility(View.VISIBLE);
                                imgList.get(2).setScaleType(ImageView.ScaleType.CENTER);

                            });
                    secondFile = new File(ImageUtils.getRealPath(context, UCrop.getOutput(data)));
                    uploadFile(secondFile, 1);


                    break;

                case REQUEST_CAMERA_THIRD:
                    startCropImage(mImageUri, RESPONSE_CODE_THIRD);

                case REQUEST_PHOTO_THIRD:
                    startCropImage(data.getData(), RESPONSE_CODE_THIRD);
                    break;


                case RESPONSE_CODE_THIRD:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgList.get(0)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgList.get(2).setScaleType(ImageView.ScaleType.FIT_XY);
                                imgList.get(2).setImageBitmap(bitmap);


                            });
                    thirdFile = new File(ImageUtils.getRealPath(context, UCrop.getOutput(data)));
                    uploadFile(thirdFile, 2);
                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateItem(UpdateRoundPoint updateRoundPoint, ArrayList<RoundPoint.ImageBean> imageBeanArrayList) {
        mSubscription = mRemoteService.updateRoundItem(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""), updateRoundPoint)
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
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            RoundPoint roundPoint = new RoundPoint(updateRoundPoint.getId(),
                                    updateRoundPoint.getInspectionname(),
                                    updateRoundPoint.getInspectioncontent(),imageBeanArrayList);
                            Intent mIntent = new Intent();
                            mIntent.putExtra("info", roundPoint);
                            // 设置结果，并进行传送
                            setResult(4, mIntent);
                            Toast.makeText(context, "修改成功", Toast.LENGTH_SHORT).show();

                            Single.just("").delay(1, TimeUnit.SECONDS).
                                    compose(RxUtils.applySchedulers()).
                                    subscribe(s -> finish()
                                    );

                        }

                    }
                });
    }

    private void editData(RequestBody body, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.DOMIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);


        Call<ApiResponse> call = userBiz.editInspectionItem(token, body);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("tag:", "onResponse: " + response.code());
                if (response.body().getResultCode() == 200) {
                    //会调数据

//                ExchangeWorkList list=new ExchangeWorkList(mId,mContent,mTitleName,lists);
//                Intent mIntent = new Intent();
//                mIntent.putExtra("exchange_work", list);
//                // 设置结果，并进行传送
//                setResult(4,mIntent);
                    Toast.makeText(context, "编辑成功", Toast.LENGTH_SHORT).show();

                    Single.just("").delay(1, TimeUnit.SECONDS).
                            compose(RxUtils.applySchedulers()).
                            subscribe(s ->
                                    startActivity(new Intent(context,
                                            RoundPointSettingActivity.class).putExtra(
                                            RoundPointSettingActivity.PROJECT_ID,
                                            PROJECT_ID))
                            );

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
        builder.addFormDataPart("inspectionname", title);
        builder.addFormDataPart("inspectioncontent", content);
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
                        System.out.println("response" + response.length());
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
                        System.out.println("fileName:" + response.getName());


                    }
                });
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
                if (response.isSuccessful()) {
                    if (response.body().getResultCode() == 200) {

                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(response.body().getData());
                        String fileUrl = gson.fromJson(jsonArray,
                                new TypeToken<String>() {
                                }.getType());
                        System.out.println("file:" + fileUrl);
                        switch (type) {
                            case 0:
                                firstPath = fileUrl;

                                break;

                            case 1:
                                secondPath = fileUrl;

                                break;

                            case 2:
                                thirdPath = fileUrl;

                                break;
                        }


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
}
