package org.eenie.wgj.ui.takephoto;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.yalantis.ucrop.UCrop;

import org.eenie.wgj.MainTestPictureActivity;
import org.eenie.wgj.MainTestPictureOneActivity;
import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.ui.reportpost.GallerysActivity;
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
//    private static final int CAMERA_WITH_DATA =0x301 ;
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
    @BindView(R.id.img_delete_first)ImageView imgDeleteFirst;
    @BindView(R.id.img_delete_second)ImageView imgDeleteSecond;
    @BindView(R.id.img_delete_third)ImageView imgDeleteThird;
    //图片保存路径
    public static final String filePath = Environment.getExternalStorageDirectory() + "/PictureTest/";

    private String tempPhotoPath;
    private File mCurrentPhotoFile;
    @Override
    protected int getContentView() {
        return R.layout.activity_add_take_photo;
    }

    @Override
    protected void updateUI() {



    }

    @OnClick({R.id.img_back, R.id.tv_save, R.id.img_first, R.id.img_second, R.id.img_third,R.id.img_delete_first,
    R.id.img_delete_second,R.id.img_delete_third})
    public void onClick(View view) {
        mContent = etInputContent.getText().toString();
        mTitleName=etInputTitle.getText().toString();

        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.img_delete_first:
                if (!TextUtils.isEmpty(firstPath)){
                    firstPath="";
                    imgDeleteFirst.setVisibility(View.GONE);
                    imgList.get(0).setScaleType(ImageView.ScaleType.CENTER);
                    imgList.get(0).setImageResource(R.mipmap.ic_carmer_first);
                    imgList.get(0).setBackgroundResource(R.color.white);
                }


                break;
            case R.id.img_delete_second:
                if (!TextUtils.isEmpty(secondPath)){
                    secondPath="";
                    imgDeleteSecond.setVisibility(View.GONE);
                    imgList.get(1).setScaleType(ImageView.ScaleType.CENTER);
                    imgList.get(1).setImageResource(R.mipmap.ic_carmer_first);
                    imgList.get(1).setBackgroundResource(R.color.white);
                }

                break;
            case R.id.img_delete_third:

                if (!TextUtils.isEmpty(thirdPath)){
                    thirdPath="";
                    imgDeleteThird.setVisibility(View.GONE);
                    imgList.get(2).setScaleType(ImageView.ScaleType.CENTER);
                    imgList.get(2).setImageResource(R.mipmap.ic_carmer_first);
                    imgList.get(2).setBackgroundResource(R.color.white);
                }
                break;
            case R.id.tv_save:
                if (!TextUtils.isEmpty(mTitleName)){
                    if (!TextUtils.isEmpty(mContent)){
                        if (!TextUtils.isEmpty(firstPath)){
                            Intent intent =new Intent(context,SelectProjectTakephotoActivity.class);
                            intent.putExtra(SelectProjectTakephotoActivity.TITLE,mTitleName);
                            intent.putExtra(SelectProjectTakephotoActivity.CONTENT,mContent);
                            intent.putExtra(SelectProjectTakephotoActivity.FIRST_URL,firstPath);
                            intent.putExtra(SelectProjectTakephotoActivity.SECOND_URL,secondPath);
                            intent.putExtra(SelectProjectTakephotoActivity.THIRD_URL,thirdPath);
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
                if (TextUtils.isEmpty(firstPath)){
                    startActivityForResult(new Intent(context, MainTestPictureOneActivity.class),111);
                }else {
                    startActivity(new Intent(context, GallerysActivity.class)
                            .putExtra(GallerysActivity.EXTRA_IMAGE_URI, firstPath));
                }


//                if (TextUtils.isEmpty(firstPath)){
//                    showUploadDialog(REQUEST_CAMERA_FIRST,REQUEST_PHOTO_FIRST);
//                }else {
//                    startActivity(new Intent(context, GallerysActivity.class)
//                            .putExtra(GallerysActivity.EXTRA_IMAGE_URI, firstPath));
//                }



                break;
            case R.id.img_second:
                if (TextUtils.isEmpty(secondPath)){
                    startActivityForResult(new Intent(context, MainTestPictureOneActivity.class),222);
                }else {
                    startActivity(new Intent(context, GallerysActivity.class)
                            .putExtra(GallerysActivity.EXTRA_IMAGE_URI, secondPath));
                }
//                if (TextUtils.isEmpty(secondPath)){
//                    showUploadDialog(REQUEST_CAMERA_SECOND,REQUEST_PHOTO_SECOND);
//                }else {
//                    startActivity(new Intent(context, GallerysActivity.class)
//                            .putExtra(GallerysActivity.EXTRA_IMAGE_URI, secondPath));
//                }


                break;
            case R.id.img_third:
                if (TextUtils.isEmpty(thirdPath)){
                    startActivityForResult(new Intent(context, MainTestPictureOneActivity.class),333);
                }else {
                    startActivity(new Intent(context, GallerysActivity.class)
                            .putExtra(GallerysActivity.EXTRA_IMAGE_URI, thirdPath));
                }
//                if (TextUtils.isEmpty(secondPath)){
//                    showUploadDialog(REQUEST_CAMERA_THIRD,REQUEST_PHOTO_THIRD);
//                }else {
//                    startActivity(new Intent(context, GallerysActivity.class)
//                            .putExtra(GallerysActivity.EXTRA_IMAGE_URI, thirdPath));
//                }


                break;
        }
    }

//
//    /* 从相机中获取照片 */
//    private void getPictureFromCamera() {
//
//
//
//        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//        tempPhotoPath = filePath + getNewFileName()
//                + ".png";
//        mCurrentPhotoFile = new File(tempPhotoPath);
//
//        if (!mCurrentPhotoFile.exists()) {
//            try {
//                mCurrentPhotoFile.createNewFile();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        intent.putExtra(MediaStore.EXTRA_OUTPUT,
//                Uri.fromFile(mCurrentPhotoFile));
//        startActivityForResult(intent, CAMERA_WITH_DATA);
//
//
////        mImageUri = createImageUri(this);
////        Intent intent = new Intent();
////        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
////        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
////        startActivityForResult(intent, CAMERA_WITH_DATA);
//    }
//
//
//    /**
//     * 根据时间戳生成文件名
//     *
//     * @return
//     */
//    public static String getNewFileName() {
//        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
//        Date curDate = new Date(System.currentTimeMillis());
//        return formatter.format(curDate);
//    }
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
        switch (requestCode){
            case RESPONSE_CODE_FIRST:
                File cropFile = new File(context.getCacheDir(), System.currentTimeMillis()+ ".jpg");
                UCrop.of(resUri, Uri.fromFile(cropFile))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(500, 500)
                        .start(AddTakePhotoActivity.this, requestCode);

                break;
            case RESPONSE_CODE_SECOND:
                File cropFiles = new File(context.getCacheDir(), System.currentTimeMillis()+ ".jpg");
                UCrop.of(resUri, Uri.fromFile(cropFiles))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(500, 500)
                        .start(AddTakePhotoActivity.this, requestCode);
                break;
            case RESPONSE_CODE_THIRD:
                File mCropFiles = new File(context.getCacheDir(), System.currentTimeMillis()+ ".jpg");
                UCrop.of(resUri, Uri.fromFile(mCropFiles))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(500, 500)
                        .start(AddTakePhotoActivity.this, requestCode);

                break;
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode==4){
            switch (requestCode){
                case 111:
                    firstPath= data.getStringExtra("path");
                    if (!TextUtils.isEmpty(firstPath)){
                        Glide.with(context).load(new File(firstPath)).centerCrop().into( imgList.get(0));
                        imgDeleteFirst.setVisibility(View.VISIBLE);

                        if (TextUtils.isEmpty(secondPath)){
                            imgList.get(1).setScaleType(ImageView.ScaleType.CENTER);
                            imgList.get(1).setImageResource(R.mipmap.ic_carmer_first);
                            imgList.get(1).setBackgroundResource(R.color.white);

                        }
                    }




                    break;
                case 222:
                    secondPath= data.getStringExtra("path");
                    if (!TextUtils.isEmpty(secondPath)){
                        Glide.with(context).load(new File(secondPath)).centerCrop().into( imgList.get(1));
                        imgDeleteSecond.setVisibility(View.VISIBLE);

                        if (TextUtils.isEmpty(thirdPath)){
                            imgList.get(2).setScaleType(ImageView.ScaleType.CENTER);
                            imgList.get(2).setImageResource(R.mipmap.ic_carmer_first);
                            imgList.get(2).setBackgroundResource(R.color.white);
                        }
                    }

                    break;
                case 333:
                    thirdPath= data.getStringExtra("path");
                    if (!TextUtils.isEmpty(thirdPath)){
                        Glide.with(context).load(new File(thirdPath)).centerCrop().into( imgList.get(2));
                        imgDeleteThird.setVisibility(View.VISIBLE);


                    }

                    break;
            }
        }
        if (resultCode == RESULT_OK) {

            switch (requestCode) {

                case REQUEST_CAMERA_FIRST:
                    startActivity(new Intent(context, MainTestPictureActivity.class).
                            putExtra("uri",String.valueOf(mImageUri)));



                  //  startCropImage(mImageUri, RESPONSE_CODE_FIRST);

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
                                imgDeleteFirst.setVisibility(View.VISIBLE);
                                if (TextUtils.isEmpty(secondPath)){
                                    imgList.get(1).setScaleType(ImageView.ScaleType.CENTER);
                                    imgList.get(1).setImageResource(R.mipmap.ic_carmer_first);
                                    imgList.get(1).setBackgroundResource(R.color.white);

                                }


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
                                imgDeleteSecond.setVisibility(View.VISIBLE);
                                if (TextUtils.isEmpty(secondPath)){
                                    imgList.get(2).setScaleType(ImageView.ScaleType.CENTER);
                                    imgList.get(2).setImageResource(R.mipmap.ic_carmer_first);
                                    imgList.get(2).setBackgroundResource(R.color.white);

                                }

                            });
                    secondPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));
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
                                imgDeleteThird.setVisibility(View.VISIBLE);


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
