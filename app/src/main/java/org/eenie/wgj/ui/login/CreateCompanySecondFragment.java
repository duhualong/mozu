package org.eenie.wgj.ui.login;

import android.Manifest;
import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yalantis.ucrop.UCrop;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseFragment;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.CreataCompanyRequest;
import org.eenie.wgj.util.ImageUtils;
import org.eenie.wgj.util.PermissionManager;

import java.io.File;

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

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;

/**
 * Created by Eenie on 2017/5/10 at 10:39
 * Email: 472279981@qq.com
 * Des:
 */

public class CreateCompanySecondFragment extends BaseFragment {
    private static final int REQUEST_CAMERA_PERMISSION = 0x101;
    private static final int TAKE_PHOTO_REQUEST =0x102 ;
    private static final int RESPONSE_CODE =0x103 ;
    private Uri imageUri;
    private String licenceUrl;
    public static final String PHONE = "phone";
    public static final String UID = "uid";
    public static final String CITY = "city";
    public static final String COMPANY = "company_name";
    public static final String EMAIL = "email";
    public static final String ADDRESS = "address";
    public static final String NAME = "name";
    public static final String TOKEN="token";

    private int userId;
    private String companyName;
    private String city;
    private String name;
    private String phone;
    private String address;
    private String email;
    @BindView(R.id.img_business_licence)
    ImageView imgLicence;
    @BindView(R.id.root_view)
    View rootView;
    @BindView(R.id.btn_next)
    Button btnNext;
    private File mFileLicence;
    private String mFileUrl;
    private String token;

    @Override
    protected int getContentView() {
        return R.layout.register_company_information_second;
    }

    @Override
    protected void updateUI() {

    }

    public static CreateCompanySecondFragment newInstance(int userId, String token,
                                                          String city, String company,
                                                          String name, String phone, String email,
                                                          String address) {


        CreateCompanySecondFragment fragment = new CreateCompanySecondFragment();
        if (!TextUtils.isEmpty(city) && !TextUtils.isEmpty(company)) {
            Bundle args = new Bundle();
            args.putString(CITY, city);
            args.putString(COMPANY, company);
            args.putString(NAME, name);
            args.putString(PHONE, phone);
            args.putString(EMAIL, email);
            args.putString(ADDRESS, address);
            args.putInt(UID, userId);
            args.putString(TOKEN,token);

            fragment.setArguments(args);
        }
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (getArguments() != null) {
            userId = getArguments().getInt(UID);
            companyName = getArguments().getString(COMPANY);
            name = getArguments().getString(NAME);
            phone = getArguments().getString(PHONE);
            email = getArguments().getString(EMAIL);
            address = getArguments().getString(ADDRESS);
            city = getArguments().getString(CITY);
            token=getArguments().getString(TOKEN);

        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @OnClick({R.id.img_back, R.id.btn_upload_licence_business, R.id.btn_next})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.btn_next:
                if (!TextUtils.isEmpty(mFileUrl)) {
                    createCompany(mFileUrl);

                } else {
                    Snackbar.make(rootView, "请上传营业执照", Snackbar.LENGTH_LONG).show();
                }

                break;
            case R.id.btn_upload_licence_business:
                checkPermission();

                break;
        }
    }

    private void createCompany(String fileUrl) {
        CreataCompanyRequest request =new CreataCompanyRequest(userId, companyName, email, city, address,
                name, "", "", fileUrl);
        mSubscription = mRemoteService.createCompany(token, request)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<ApiResponse>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("error:"+e);

                    }

                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode() == 200||apiResponse.getResultCode()==0) {
                          registerSuccessDialog();


                        }else {
                            Toast.makeText(context,apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                            fragmentMgr.beginTransaction()
                                    .addToBackStack(TAG)
                                    .replace(R.id.fragment_login_container,
                                            new LoginFragment())
                                    .commit();
                        }

                    }
                });
    }

    private void registerSuccessDialog() {
        View view = View.inflate(context, R.layout.dialog_company_success_register, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        final AlertDialog dialog = builder
                .setView(view) //自定义的布局文件
                .create();
        dialog.show();
        dialog.getWindow().findViewById(R.id.btn_ok).setOnClickListener(v -> {
            dialog.dismiss();
            fragmentMgr.beginTransaction()
                    .addToBackStack(TAG)
                    .replace(R.id.fragment_login_container,
                           new LoginFragment())
                    .commit();


        });
    }
    /**
     * 针对高版本系统检查权限
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPermission = PermissionManager.checkCameraPermission(context)
                    && PermissionManager.checkWriteExternalStoragePermission(context);
            if (hasPermission) {
                showPhotoSelectDialog();
            } else {
                showRequestPermissionDialog();
            }
        } else {
            showPhotoSelectDialog();
        }
    }

    /**
     * 请求权限Snackbar
     */
    @TargetApi(Build.VERSION_CODES.M)
    private void showRequestPermissionDialog() {
        if (this.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Snackbar.make(rootView, "请提供摄像头及文件权限，以拍摄和预览相机图片!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", v -> {
                        PermissionManager.invokeCamera(CreateCompanySecondFragment.this,
                                REQUEST_CAMERA_PERMISSION);
                    })
                    .show();
        } else {
            PermissionManager.invokeCamera(CreateCompanySecondFragment.this, REQUEST_CAMERA_PERMISSION);
        }
    }

    /**
     * 权限申请回调
     *
     * @param requestCode  请求码
     * @param permissions  请求权限
     * @param grantResults 请求结果
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:

                boolean isCanCapturePhoto = true;

                for (int result : grantResults) {
                    if (result == PackageManager.PERMISSION_DENIED) {
                        isCanCapturePhoto = false;
                    }
                }
                if (isCanCapturePhoto) {
                    showPhotoSelectDialog();
                } else {
                    Snackbar.make(rootView, "请完整的权限，以预览裁剪图片!", Snackbar.LENGTH_SHORT).show();
                }

                break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
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
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".jpeg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");
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
        File cropFile = new File(context.getCacheDir(), "b.jpg");
        UCrop.of(resUri, Uri.fromFile(cropFile))
                .withAspectRatio(2, 3)
                .withMaxResultSize(imgLicence.getWidth(), imgLicence.getHeight())
                .start(context, this, requestCode);
    }

    public File compressior(File file) {
        return new Compressor.Builder(context)
                .setMaxWidth(120)
                .setMaxHeight(185)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.WEBP)
                .setDestinationDirectoryPath(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES).getAbsolutePath())
                .build()
                .compressToFile(file);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case TAKE_PHOTO_REQUEST:
                    startCropImage(imageUri, RESPONSE_CODE);

                    break;
                case RESPONSE_CODE:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgLicence))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgLicence.setImageBitmap(bitmap);
                            });
                    licenceUrl = ImageUtils.getRealPath(context, UCrop.getOutput(data));
                    Snackbar.make(rootView, "营业执照上传成功！", Snackbar.LENGTH_LONG).show();
                    mFileLicence = new File(licenceUrl);
                    uploadFile(compressior(mFileLicence));

                    break;

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
                Log.d(TAG, "onResponseCode: " + response.code());
                if (response.isSuccessful()) {
                    if (response.body().getResultCode() == 200) {
                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(response.body().getData());
                       String mData = gson.fromJson(jsonArray,
                                new TypeToken<String>() {
                                }.getType());

                        mFileUrl = mData;
                        System.out.println("上传成功" + response.body().getData());
                        btnNext.setBackgroundResource(R.mipmap.bg_submit_button);


                    } else {
                        Toast.makeText(context, response.body().getResultMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(context, "请检查网络！",
                            Toast.LENGTH_LONG).show();
                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t);

            }
        });


    }
}
