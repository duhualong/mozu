package org.eenie.wgj.ui.takephoto;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.yalantis.ucrop.UCrop;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.util.ImageUtils;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import rx.Single;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/6/11 at 15:59
 * Email: 472279981@qq.com
 * Des:
 */

public class AddTakePhotoActivity extends BaseActivity {
    @BindView(R.id.et_input_work_title)EditText etInputTitle;
    @BindView(R.id.et_input_work_content)EditText etInputContent;
    private static final int REQUEST_CAMERA_FIRST=0x101;
    private static final int REQUEST_CAMERA_SECOND=0x102;
    private static final int REQUEST_CAMERA_THIRD=0x103;
    private static final int REQUEST_PHOTO_FIRST=0x104;
    private static final int REQUEST_PHOTO_SECOND=0x105;
    private static final int REQUEST_PHOTO_THIRD=0x106;
    private static final int RESPONSE_CODE_FIRST=0x107;
    private static final int RESPONSE_CODE_SECOND=0x108;
    private static final int RESPONSE_CODE_THIRD=0x109;

    @BindViews({R.id.img_first, R.id.img_second, R.id.img_third})
    List<ImageView> imgList;
    private String mTitleName;
    private String mContent;
    private Uri mImageUri;
    private String firstPath;
    private String secondPath;
    private String thirdPath;
    @Override
    protected int getContentView() {
        return R.layout.activity_add_take_photo;
    }

    @Override
    protected void updateUI() {



    }

    @OnClick({R.id.img_back, R.id.tv_save, R.id.img_first, R.id.img_second, R.id.img_third})
    public void onClick(View view) {
        mContent = etInputContent.getText().toString();
        mTitleName=etInputTitle.getText().toString();

        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_save:
                if (!TextUtils.isEmpty(mTitleName)){
                    if (!TextUtils.isEmpty(mContent)){
                        if (!TextUtils.isEmpty(firstPath)){
                            Intent intent =new Intent(context,SelectReportPeopleActivity.class);
                            intent.putExtra(SelectReportPeopleActivity.TITLE,mTitleName);
                            intent.putExtra(SelectReportPeopleActivity.CONTENT,mContent);
                            intent.putExtra(SelectReportPeopleActivity.FIRST_URL,firstPath);
                            intent.putExtra(SelectReportPeopleActivity.SECOND_URL,secondPath);
                            intent.putExtra(SelectReportPeopleActivity.THIRD_URL,thirdPath);
                            startActivityForResult(intent,1);

                        }else {
                            Toast.makeText(context,"请至少上传一张图片",Toast.LENGTH_SHORT).show();

                        }
                    }else {
                        Toast.makeText(context,"请输入说明",Toast.LENGTH_SHORT).show();
                    }



                }else {
                    Toast.makeText(context,"请输入标题",Toast.LENGTH_SHORT).show();
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
                .withMaxResultSize(300, 300)
                .start(AddTakePhotoActivity.this, requestCode);
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
                    firstPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));

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
                    secondPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));
                    //secondFile=new File(secondPath);


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
                    thirdPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));
                   //thirdFile=new File(thirdPath);



//                    }

                    break;
            }
        }
        //回调结果
        if (requestCode==1&&resultCode==4){
            finish();


        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
