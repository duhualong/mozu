package org.eenie.wgj.ui.project.exchangework;

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

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private static final int REQUEST_CAMERA_FIRST=0x101;
    private static final int REQUEST_CAMERA_SECOND=0x102;
    private static final int REQUEST_CAMERA_THIRD=0x103;
    private static final int REQUEST_PHOTO_FIRST=0x104;
    private static final int REQUEST_PHOTO_SECOND=0x105;
    private static final int REQUEST_PHOTO_THIRD=0x106;
    private static final int RESPONSE_CODE_FIRST=0x107;
    private static final int RESPONSE_CODE_SECOND=0x108;
    private static final int RESPONSE_CODE_THIRD=0x109;
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
    private List<String>imgUrlPath;


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
        String ss = mInputContent.getText().toString();

        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_save:
                if (!TextUtils.isEmpty(firstPath)&&!TextUtils.isEmpty(secondPath)&&!TextUtils.isEmpty(thirdPath)){
                    new Thread() {
                        public void run() {
                            addData(mPrefsHelper.getPrefs().getString(Constants.TOKEN,""),firstPath,secondPath,thirdPath);
                        }
                    }.start();

                }




//                ExchangeWorkList list=new ExchangeWorkList(1,"s","s",lists);
//                Intent mIntent = new Intent();
//                mIntent.putExtra("exchange_work", list);
//                // 设置结果，并进行传送
//                setResult(4,mIntent);

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
                .start(ExchangeWorkEditActivity.this, requestCode);
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
                                imgList.get(0).setImageBitmap(bitmap);
                                imgList.get(1).setVisibility(View.VISIBLE);

                            });
                    firstPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));
//                    if (!TextUtils.isEmpty(ImageUtils.getRealPath(context, UCrop.getOutput(data)))){
//                        imgUrlPath.add(0,ImageUtils.getRealPath(context, UCrop.getOutput(data)));
//
//                    }

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
                                imgList.get(1).setImageBitmap(bitmap);
                                imgList.get(2).setVisibility(View.VISIBLE);

                            });
                    secondPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));

//                    if (!TextUtils.isEmpty(ImageUtils.getRealPath(context, UCrop.getOutput(data)))){
//                        imgUrlPath.add(1,ImageUtils.getRealPath(context, UCrop.getOutput(data)));
//
//                    }

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
                                imgList.get(2).setImageBitmap(bitmap);


                            });
                    thirdPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));

//                    if (!TextUtils.isEmpty(ImageUtils.getRealPath(context, UCrop.getOutput(data)))){
//                        imgUrlPath.add(2,ImageUtils.getRealPath(context, UCrop.getOutput(data)));
//
//                    }

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    private void addData(String token, String pathOne,String pathTwo,String pathThree) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.DOMIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);
        final Map<String, RequestBody> requestMap = new HashMap<>();
        requestMap.put("projectid", RequestBody.create(MediaType.parse("multipart/form-data"),
                "3"));
        requestMap.put("mattername", RequestBody.create(MediaType.parse("multipart/form-data"),
                "test"));
        requestMap.put("matter", RequestBody.create(MediaType.parse("multipart/form-data"),
                "test_content"));

        final List<MultipartBody.Part> partHashMap = new ArrayList<>();

            RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"),
                            new File(pathOne));
            MultipartBody.Part body = MultipartBody.Part.createFormData("image[]",
                 "aa", requestFile);
        RequestBody requestFile1 =
                RequestBody.create(MediaType.parse("multipart/form-data"),
                        new File(pathTwo));
        MultipartBody.Part body1 = MultipartBody.Part.createFormData("image[]",
                "bb", requestFile1);
            partHashMap.add(1, body1);
        RequestBody requestFile2 =
                RequestBody.create(MediaType.parse("multipart/form-data"),
                        new File(pathThree));
        MultipartBody.Part body2 = MultipartBody.Part.createFormData("image[]",
                "cc", requestFile2);
        partHashMap.add(2, body2);


        Call<ApiResponse> call = userBiz.addExchangeWorkList(token, requestMap, partHashMap);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("tag:", "onResponse: "+response.code());
                if (response.body().getResultCode()==200){
                    Toast.makeText(context,"测试成功",Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });


    }

}
