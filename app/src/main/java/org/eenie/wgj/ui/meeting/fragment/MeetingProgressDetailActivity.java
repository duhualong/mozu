package org.eenie.wgj.ui.meeting.fragment;

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
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yalantis.ucrop.UCrop;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.response.meeting.MeetingEndDetail;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.ImageUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.BindViews;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
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
 * Created by Eenie on 2017/7/12 at 14:59
 * Email: 472279981@qq.com
 * Des:
 */

public class MeetingProgressDetailActivity extends BaseActivity {
    private static final int REQUEST_CAMERA_FIRST = 0x101;
    private static final int REQUEST_CAMERA_SECOND = 0x102;
    private static final int REQUEST_CAMERA_THIRD = 0x103;
    private static final int REQUEST_PHOTO_FIRST = 0x104;
    private static final int REQUEST_PHOTO_SECOND = 0x105;
    private static final int REQUEST_PHOTO_THIRD = 0x106;
    private static final int RESPONSE_CODE_FIRST = 0x107;
    private static final int RESPONSE_CODE_SECOND = 0x108;
    private static final int RESPONSE_CODE_THIRD = 0x109;
    private Uri mImageUri;

    public static final String APPLY_ID = "id";
    @BindView(R.id.img_avatar_meeting)
    CircleImageView imgAvatar;
    @BindView(R.id.tv_username)
    TextView tvUserName;
    @BindView(R.id.tv_status_meeting)
    TextView tvMeetingStatus;
    @BindView(R.id.tv_meeting_name)
    TextView tvMeetingName;
    @BindView(R.id.tv_meeting_time)
    TextView tvMeetingTime;
    @BindView(R.id.tv_meeting_address)
    TextView tvMeetingAddress;
    @BindView(R.id.tv_meeting_host)
    TextView tvMeetingHost;
    @BindView(R.id.tv_meeting_record)
    TextView tvMeetingRecord;
    @BindView(R.id.tv_meeting_purpose)
    TextView tvMeetingPurpose;
    @BindView(R.id.tv_meeting_plan)
    TextView tvMeetingPlan;
    @BindView(R.id.tv_meeting_join)
    TextView tvMeetingJoin;
    @BindView(R.id.edit_meeting_content)
    EditText editMeetingRecord;

    private String applyId;
    @BindViews({R.id.img_first, R.id.img_second, R.id.img_third})
    List<ImageView> imgList;
    private String firstPath;
    private String secondPath;
    private String thirdPath;
    private File firstFile;
    private File secondFile;
    private File thirdFile;

    @Override
    protected int getContentView() {
        return R.layout.activity_meeting_progress_detail;
    }

    @Override
    protected void updateUI() {
        applyId = getIntent().getStringExtra(APPLY_ID);
        if (!TextUtils.isEmpty(applyId)) {
            getData(applyId);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!TextUtils.isEmpty(applyId)) {
            getData(applyId);
        }
    }

    private void getData(String applyId) {

        mSubscription = mRemoteService.getMeetingArrangeDetail(mPrefsHelper.getPrefs().
                        getString(Constants.TOKEN, ""),
                applyId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {

                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            MeetingEndDetail mData =
                                    gson.fromJson(jsonArray,
                                            new TypeToken<MeetingEndDetail>() {
                                            }.getType());

                            if (mData != null) {
                                if (!TextUtils.isEmpty(mData.getId_card_head_image())) {
                                    Glide.with(context).load(Constant.DOMIN + mData.getId_card_head_image())
                                            .centerCrop().into(imgAvatar);

                                }
                                if (!TextUtils.isEmpty(mData.getUsername())) {
                                    tvUserName.setText(mData.getUsername());

                                }
                                if (!TextUtils.isEmpty(mData.getState())) {
                                    tvMeetingStatus.setText(mData.getState());
                                }
                                if (!TextUtils.isEmpty(mData.getName())) {
                                    tvMeetingName.setText(mData.getName());
                                }
                                if (!TextUtils.isEmpty(mData.getStart()) && !TextUtils.isEmpty(mData.getEnd())) {
                                    tvMeetingTime.setText(mData.getStart() + "至" + mData.getEnd());
                                }
                                if (mData.getHost().equals("null")) {
                                    tvMeetingHost.setText("无");
                                } else {
                                    tvMeetingHost.setText(mData.getHost());
                                }
                                if (mData.getRecorder().equals("null")) {

                                } else {
                                    if (!TextUtils.isEmpty(mData.getRecorder())) {
                                        tvMeetingRecord.setText(mData.getRecorder());
                                    }
                                }
                                if (!mData.getNumber().isEmpty() && mData.getNumber() != null) {

                                    if (mData.getNumber().size() > 0 && !TextUtils.isEmpty(
                                            mData.getNumber().get(0).getUsername())) {
                                        String joiner = "";
                                        for (int i = 0; i < mData.getNumber().size(); i++) {
                                            if (i == (mData.getNumber().size() - 1)) {
                                                joiner = joiner + mData.getNumber().get(i).getUsername();
                                            } else {
                                                joiner = joiner + mData.getNumber().
                                                        get(i).getUsername() + "/";
                                            }


                                        }
                                        tvMeetingJoin.setText(joiner);
                                    }
                                }
                                if (!TextUtils.isEmpty(mData.getAddress())) {
                                    tvMeetingAddress.setText(mData.getAddress());
                                }

                                if (!TextUtils.isEmpty(mData.getMeeting_purpose())) {
                                    tvMeetingPurpose.setText(mData.getMeeting_purpose());
                                }
                                if (!TextUtils.isEmpty(mData.getMeeting_agenda())) {
                                    tvMeetingPlan.setText(mData.getMeeting_agenda());
                                }


                            }

                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(),
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });
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
    private static Uri createImageUri(Context context) {
        String name = "takePhoto" + System.currentTimeMillis();
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, name);
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, name + ".jpg");
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpg");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        return uri;
    }

    private void showPhotoSelectDialog(int camera) {
        mImageUri = createImageUri(context);
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
        startActivityForResult(intent, camera);
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
                        .withAspectRatio(1,1)
                        .withMaxResultSize(500, 500)
                        .start(MeetingProgressDetailActivity.this, requestCode);
                break;
            case RESPONSE_CODE_SECOND:
                File cropFiles = new File(context.getCacheDir(), "b.jpg");
                UCrop.of(resUri, Uri.fromFile(cropFiles))
                        .withAspectRatio(1,1)
                        .withMaxResultSize(500, 500)
                        .start(MeetingProgressDetailActivity.this, requestCode);
                break;
            case RESPONSE_CODE_THIRD:
                File cropFiless = new File(context.getCacheDir(), "c.jpg");
                UCrop.of(resUri, Uri.fromFile(cropFiless))
                        .withAspectRatio(1,1)
                        .withMaxResultSize(500, 500)
                        .start(MeetingProgressDetailActivity.this, requestCode);
                break;
        }


    }

    @OnClick({R.id.rl_sign_scan, R.id.img_back, R.id.img_first, R.id.img_second, R.id.img_third,
    R.id.tv_apply_ok})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_apply_ok:
                if (!TextUtils.isEmpty(editMeetingRecord.getText().toString())){
                    List<File> files = new ArrayList<>();


                    if (firstFile != null) {
                        files.add(firstFile);
                    }
                    if (secondFile != null) {
                        files.add(secondFile);
                    }
                    if (thirdFile != null) {
                        files.add(thirdFile);
                    }
                    if (files.size()>0) {
                        new Thread() {
                            public void run() {
                                addData(getMultipartBody(files,editMeetingRecord.getText().toString()),
                                        mPrefsHelper.getPrefs().getString(Constants.TOKEN,"")) ;



                            }
                        }.start();
                    } else {
                        Toast.makeText(context, "请至少选择一张会议照片", Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(context,"请输入会议记录",Toast.LENGTH_SHORT).show();
                }


                break;
            case R.id.rl_sign_scan:
                startActivity(new Intent(context, SignInMeetingActivity.class)
                        .putExtra(SignInMeetingActivity.APPLY_ID, applyId));

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


    private void addData(RequestBody body, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.DOMIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);

        Call<ApiResponse> call = userBiz.addMeetingRecord(token, body);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                Log.d("tag:", "onResponse: " + response.code());
                if (response.body().getResultCode() == 200) {
                    Toast.makeText(context,response.body().getResultMessage(),
                            Toast.LENGTH_SHORT).show();


                }else {
                    Toast.makeText(context,response.body().getResultMessage(),
                            Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context,"网络错误", Toast.LENGTH_SHORT).show();

            }
        });
    }
    public  MultipartBody getMultipartBody(List<File> files, String meetingContent) {


        MultipartBody.Builder builder = new MultipartBody.Builder();
        for (int i = 0; i < files.size(); i++) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"),
                    files.get(i));
            builder.addFormDataPart("image[]", files.get(i).getName(), requestBody);
        }
        builder.addFormDataPart("record", meetingContent);
        builder.addFormDataPart("meetingid",applyId);
        builder.setType(MultipartBody.FORM);
        return builder.build();

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
                                imgList.get(2).setScaleType(ImageView.ScaleType.CENTER);
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

}
