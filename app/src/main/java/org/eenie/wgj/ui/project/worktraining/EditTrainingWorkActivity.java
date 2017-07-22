package org.eenie.wgj.ui.project.worktraining;

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
import org.eenie.wgj.model.response.WorkTrainingList;
import org.eenie.wgj.ui.message.GalleryActivity;
import org.eenie.wgj.ui.reportpost.GallerysActivity;
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

import static android.R.attr.maxWidth;

/**
 * Created by Eenie on 2017/5/21 at 15:10
 * Email: 472279981@qq.com
 * Des:
 */

public class EditTrainingWorkActivity extends BaseActivity {
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
    private ArrayList<WorkTrainingList.ImageBean> lists;
    private WorkTrainingList data;
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
    @BindView(R.id.img_delete_first)
    ImageView imgDeletaFirst;
    @BindView(R.id.img_delete_second)
    ImageView imgDeleteSecond;
    @BindView(R.id.img_delete_third)
    ImageView imgDeleteThird;

    @Override
    protected int getContentView() {
        return R.layout.activity_edit_training_work;
    }

    @Override
    protected void updateUI() {
        data = getIntent().getParcelableExtra(INFO);
        mProjectId = getIntent().getStringExtra(PROJECT_ID);
        if (data != null) {
            mId = data.getId();
            mTitleName = data.getTrainingname();
            mContent = data.getTrainingcontent();
            lists = data.getImage();
            if (!TextUtils.isEmpty(mTitleName)) {
                mInputTitle.setText(mTitleName);
            }
            if (!TextUtils.isEmpty(mContent)) {
                mInputContent.setText(mContent);
            }
            if (lists != null) {
                if (lists.size() == 1) {
                    downloadImg(lists.get(0).getImage(), 0);
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(0).getImage())
                            .centerCrop().into(imgList.get(0));
                    imgDeletaFirst.setVisibility(View.VISIBLE);
                    imgList.get(1).setBackgroundResource(R.color.white);
                    imgList.get(1).setScaleType(ImageView.ScaleType.CENTER);
                    imgList.get(1).setImageResource(R.mipmap.ic_carmer_first);

                } else if (lists.size() == 2) {
                    downloadImg(lists.get(0).getImage(), 0);
                    downloadImg(lists.get(1).getImage(), 1);
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(0).getImage())
                            .centerCrop().into(imgList.get(0));
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(1).getImage())
                            .centerCrop().into(imgList.get(1));
                    imgDeletaFirst.setVisibility(View.VISIBLE);
                    imgDeleteSecond.setVisibility(View.VISIBLE);
                    imgList.get(2).setBackgroundResource(R.color.white);
                    imgList.get(2).setScaleType(ImageView.ScaleType.CENTER);
                    imgList.get(2).setImageResource(R.mipmap.ic_carmer_first);

                } else if (lists.size() >= 3) {
                    downloadImg(lists.get(0).getImage(), 0);
                    downloadImg(lists.get(1).getImage(), 1);
                    downloadImg(lists.get(2).getImage(), 2);
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(0).getImage())
                            .centerCrop().into(imgList.get(0));
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(1).getImage())
                            .centerCrop().into(imgList.get(1));
                    Glide.with(context).load(Constant.DOMIN + data.getImage().get(2).getImage())
                            .centerCrop().into(imgList.get(2));
                    imgDeletaFirst.setVisibility(View.VISIBLE);
                    imgDeleteSecond.setVisibility(View.VISIBLE);
                    imgDeleteThird.setVisibility(View.VISIBLE);

                }

            }


        }

    }

    @OnClick({R.id.img_back, R.id.tv_save, R.id.img_first, R.id.img_second,
            R.id.img_third, R.id.img_delete_first,
            R.id.img_delete_second, R.id.img_delete_third})
    public void onClick(View view) {
        mContent = mInputContent.getText().toString();
        mTitleName = mInputTitle.getText().toString();

        switch (view.getId()) {
            case R.id.img_delete_first:
                if (firstFile != null) {
                    firstFile = null;
                    firstPath = "";

                    imgDeletaFirst.setVisibility(View.GONE);
                    imgList.get(0).setScaleType(ImageView.ScaleType.CENTER);
                    imgList.get(0).setImageResource(R.mipmap.ic_carmer_first);

                }

                break;
            case R.id.img_delete_second:
                if (secondFile != null) {
                    secondFile = null;
                    secondPath = "";
                    imgDeleteSecond.setVisibility(View.GONE);
                    imgList.get(1).setScaleType(ImageView.ScaleType.CENTER);
                    imgList.get(1).setImageResource(R.mipmap.ic_carmer_first);

                }

                break;
            case R.id.img_delete_third:
                if (thirdFile != null) {
                    thirdFile = null;
                    thirdPath = "";
                    imgDeleteThird.setVisibility(View.GONE);
                    imgList.get(2).setScaleType(ImageView.ScaleType.CENTER);
                    imgList.get(2).setImageResource(R.mipmap.ic_carmer_first);

                }

                break;
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_save:
                if (!TextUtils.isEmpty(mContent) && !TextUtils.isEmpty(mTitleName))


                    if (firstFile != null) {
                        files.add(0, firstFile);
                    }
                if (secondFile != null) {
                    files.add(1, secondFile);
                }
                if (thirdFile != null) {
                    files.add(2, thirdFile);
                }
                if (files != null) {
                    new Thread() {
                        public void run() {
                            editData(getMultipartBody(files, mProjectId, mTitleName, mContent, mId + ""),
                                    mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""));
                        }
                    }.start();

                }


//                ExchangeWorkListResponse list=new ExchangeWorkListResponse(1,"s","s",lists);
//                Intent mIntent = new Intent();
//                mIntent.putExtra("exchange_work", list);
//                // 设置结果，并进行传送
//                setResult(4,mIntent);

                break;
            case R.id.img_first:
                if (firstFile == null) {
                    showUploadDialog(REQUEST_CAMERA_FIRST, REQUEST_PHOTO_FIRST);
                } else {
                    if (!TextUtils.isEmpty(firstPath)) {
                        startActivity(new Intent(context, GallerysActivity.class)
                                .putExtra(GallerysActivity.EXTRA_IMAGE_URI, firstPath));
                    } else {
                        startActivity(new Intent(context, GalleryActivity.class)
                                .putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                        Constant.DOMIN + data.getImage().get(0).getImage()));
                    }
                }


                break;
            case R.id.img_second:
                if (secondFile == null) {
                    showUploadDialog(REQUEST_CAMERA_SECOND, REQUEST_PHOTO_SECOND);

                } else {
                    if (!TextUtils.isEmpty(secondPath)) {
                        startActivity(new Intent(context, GallerysActivity.class)
                                .putExtra(GallerysActivity.EXTRA_IMAGE_URI, secondPath));
                    } else {
                        startActivity(new Intent(context, GalleryActivity.class)
                                .putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                        Constant.DOMIN + data.getImage().get(1).getImage()));
                    }
                }


                break;
            case R.id.img_third:
                if (thirdFile == null) {
                    showUploadDialog(REQUEST_CAMERA_THIRD, REQUEST_PHOTO_THIRD);

                } else {
                    if (!TextUtils.isEmpty(thirdPath)) {
                        startActivity(new Intent(context, GallerysActivity.class)
                                .putExtra(GallerysActivity.EXTRA_IMAGE_URI, thirdPath));
                    } else {
                        startActivity(new Intent(context, GalleryActivity.class)
                                .putExtra(GalleryActivity.EXTRA_IMAGE_URI,
                                        Constant.DOMIN + data.getImage().get(2).getImage()));
                    }
                }


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
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                contentValues);
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

                File cropFile = new File(context.getCacheDir(), System.currentTimeMillis() + ".jpg");
                UCrop.of(resUri, Uri.fromFile(cropFile))
                        .withAspectRatio(4,3)
                        .withMaxResultSize(maxWidth, 300)
                        .start(EditTrainingWorkActivity.this, requestCode);
                break;

            case RESPONSE_CODE_SECOND:
                File cropFiles = new File(context.getCacheDir(), System.currentTimeMillis() + ".jpg");
                UCrop.of(resUri, Uri.fromFile(cropFiles))
                        .withAspectRatio(4,3)
                        .withMaxResultSize(maxWidth, 300)
                        .start(EditTrainingWorkActivity.this, requestCode);

                break;
            case RESPONSE_CODE_THIRD:
                File mCropFiles = new File(context.getCacheDir(), System.currentTimeMillis() + ".jpg");
                UCrop.of(resUri, Uri.fromFile(mCropFiles))
                        .withAspectRatio(4,3)
                        .withMaxResultSize(maxWidth, 300)
                        .start(EditTrainingWorkActivity.this, requestCode);
                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUEST_CAMERA_FIRST:
                    if (mImageUri != null) {
                        startCropImage(mImageUri, RESPONSE_CODE_FIRST);
                    }


                    break;
                case REQUEST_PHOTO_FIRST:
                    if (data.getData() != null) {
                        startCropImage(data.getData(), RESPONSE_CODE_FIRST);
                    }
                    break;
                case RESPONSE_CODE_FIRST:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgList.get(0)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgList.get(0).setScaleType(ImageView.ScaleType.FIT_XY);
                                imgList.get(0).setImageBitmap(bitmap);
                                imgDeletaFirst.setVisibility(View.VISIBLE);
                                if (secondFile == null) {
                                    imgList.get(1).setScaleType(ImageView.ScaleType.CENTER);
                                    imgList.get(1).setBackgroundResource(R.color.white);
                                    imgList.get(1).setImageResource(R.mipmap.ic_carmer_first);
                                }


                            });
                    firstPath = ImageUtils.getRealPath(context, UCrop.getOutput(data));
                    firstFile = new File(firstPath);


                    break;
                case REQUEST_CAMERA_SECOND:
                    if (mImageUri != null) {
                        startCropImage(mImageUri, RESPONSE_CODE_SECOND);
                    }
                    break;

                case REQUEST_PHOTO_SECOND:
                    if (data.getData() != null) {
                        startCropImage(data.getData(), RESPONSE_CODE_SECOND);
                    }

                    break;

                case RESPONSE_CODE_SECOND:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgList.get(1)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgList.get(1).setScaleType(ImageView.ScaleType.FIT_XY);
                                imgList.get(1).setImageBitmap(bitmap);
                                imgDeleteSecond.setVisibility(View.VISIBLE);
                                if (thirdFile == null) {
                                    imgList.get(2).setScaleType(ImageView.ScaleType.CENTER);
                                    imgList.get(2).setBackgroundResource(R.color.white);
                                    imgList.get(2).setImageResource(R.mipmap.ic_carmer_first);
                                }
                            });
                    secondPath = ImageUtils.getRealPath(context, UCrop.getOutput(data));
                    secondFile = new File(secondPath);
                    break;

                case REQUEST_CAMERA_THIRD:
                    if (mImageUri != null) {
                        startCropImage(mImageUri, RESPONSE_CODE_THIRD);
                    }
                    break;

                case REQUEST_PHOTO_THIRD:
                    if (data.getData() != null) {
                        startCropImage(data.getData(), RESPONSE_CODE_THIRD);
                    }

                    break;


                case RESPONSE_CODE_THIRD:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgList.get(2)))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgList.get(2).setScaleType(ImageView.ScaleType.FIT_XY);
                                imgList.get(2).setImageBitmap(bitmap);
                                imgDeleteThird.setVisibility(View.VISIBLE);
                            });
                    thirdPath = ImageUtils.getRealPath(context, UCrop.getOutput(data));
                    thirdFile = new File(thirdPath);

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void editData(RequestBody body, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.DOMIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);


        Call<ApiResponse> call = userBiz.editTrainingWork(token, body);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("tag:", "onResponse: " + response.code());
                if (response.body().getResultCode() == 0 || response.body().getResultCode() == 200) {
                    //会调数据

                    Toast.makeText(context, "编辑成功", Toast.LENGTH_SHORT).show();

                    Single.just("").delay(1, TimeUnit.SECONDS).
                            compose(RxUtils.applySchedulers()).
                            subscribe(s ->
                                    startActivity(new Intent(context,
                                            WorkTrainingDetailActivity.class))
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
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                    files.get(i));
            builder.addFormDataPart("image[]", files.get(i).getName(), requestBody);

        }
        builder.addFormDataPart("projectid", projectId);
        builder.addFormDataPart("trainingname", title);
        builder.addFormDataPart("trainingcontent", content);
        builder.addFormDataPart("id", id);
        builder.setType(MultipartBody.FORM);
        return builder.build();
    }

    //下载
    private void downloadImg(String imgUrl, int position) {
        OkHttpUtils.get().url(Constant.DOMIN + imgUrl)
                .build()
                .execute(new FileCallBack(Environment.getExternalStorageDirectory().
                        getAbsolutePath(), "a.jpg") {
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

}
