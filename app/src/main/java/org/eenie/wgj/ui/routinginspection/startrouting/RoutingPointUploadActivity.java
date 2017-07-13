package org.eenie.wgj.ui.routinginspection.startrouting;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.yalantis.ucrop.UCrop;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;
import org.eenie.wgj.data.remote.FileUploadService;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.AddRoutingContent;
import org.eenie.wgj.model.requset.StartRoutingRecord;
import org.eenie.wgj.model.response.ManagerPeopleResponse;
import org.eenie.wgj.model.response.NewCircleLineId;
import org.eenie.wgj.model.response.PointNeedResponse;
import org.eenie.wgj.model.response.UploadPointPatrol;
import org.eenie.wgj.model.response.routing.RoutingContentResponse;
import org.eenie.wgj.model.response.routing.StartRoutingResponse;
import org.eenie.wgj.util.Constant;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.ImageUtils;
import org.eenie.wgj.util.PermissionManager;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
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

import static org.eenie.wgj.R.id.tv_camera_personal;

/**
 * Created by Eenie on 2017/6/26 at 16:13
 * Email: 472279981@qq.com
 * Des:
 */

public class RoutingPointUploadActivity extends BaseActivity implements AMapLocationListener {

    private static final int REQUEST_LOCATION_CODE = 0x101;
    private static final int REQUEST_CAMERA_FIRST = 0x102;
    private static final int REQUEST_CAMERA_SECOND = 0x103;
    private static final int REQUEST_CAMERA_THIRD = 0x104;
    private static final int RESPONSE_CODE_FIRST = 0x105;
    private static final int REQUEST_PHOTO_FIRST = 0x106;
    private static final int RESPONSE_CODE_SECOND = 0x107;
    private static final int REQUEST_PHOTO_SECOND = 0x108;
    private static final int RESPONSE_CODE_THIRD = 0x109;
    private static final int REQUEST_PHOTO_THIRD = 0x110;

    private static final int REQUEST_CAMERA_ONE = 0x200;
    private static final int REQUEST_CAMERA_TWO = 0x201;
    private static final int REQUEST_CAMERA_THREE = 0x202;
    private static final int RESPONSE_CODE_ONE = 0x203;
    private static final int RESPONSE_CODE_TWO = 0x204;
    private static final int RESPONSE_CODE_THREE = 0x205;
    private static final int REQUEST_PHOTO_ONE = 0x206;
    private static final int REQUEST_PHOTO_TWO = 0x207;
    private static final int REQUEST_PHOTO_THREE = 0x208;
    private static final int REQUEST_PEOPLE = 0x301;
    private AddPersonalAdapter mAdapter;
    private AddContentAdapter mAddContentAdapter;

    @BindView(R.id.root_view)
    View rootView;
    private ArrayList<ManagerPeopleResponse> mData = new ArrayList<>();
    //声明mLocationOption对象
    public AMapLocationClientOption mLocationOption = null;
    public AMapLocationClient mlocationClient = null;
    public static final String INFO = "info";
    public static final String LINE_ID = "id";
    public static final String TYPE = "type";
    public static final String PROJECT_ID = "project_id";
    public static final String INSPECTIONDAY_ID = "inspection_id";
    private int type;
    @BindView(R.id.routing_time)
    TextView tvRoutingTime;
    @BindView(R.id.routing_address)
    TextView tvRoutingAddress;

    @BindView(R.id.img_first)
    ImageView imgFirst;
    @BindView(R.id.img_second)
    ImageView imgSecond;
    @BindView(R.id.img_third)
    ImageView imgThird;
    @BindView(R.id.checkbox_select_normal)
    CheckBox checkBoxNormal;
    @BindView(R.id.checkbox_select_abnormal)
    CheckBox checkBoxAbnormal;
    @BindView(R.id.line_abnormal)
    LinearLayout mLinearLayout;
    @BindView(R.id.edit_abnormal_content)
    EditText editAbnormalContent;
    @BindView(R.id.img_one)
    ImageView imgOne;
    @BindView(R.id.img_two)
    ImageView imgTwo;
    @BindView(R.id.img_three)
    ImageView imgThree;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private StartRoutingResponse.InfoBean data;
    private int situation = 2;
    private Uri mFirstUri;
    private Uri mSecondUri;
    private Uri mThirdUri;
    private Uri mOneUri;
    private Uri mTwoUri;
    private Uri mThreeUri;
    private File mFirstFile;
    private File mSecondFile;
    private File mThirdFile;
    private File mOneFile;
    private File mTwoFile;
    private File mThreeFile;
    private double mLong;
    private double mLat;
    private String mAddress;
    private String mLineId;
    private String mType;
    private String projectId;
    private String inspectiondayId;
    private String circleId;
    private String newCircleId;
    @BindView(R.id.recycler_view_content)
    RecyclerView mRecyclerViewContent;

    ArrayList<RoutingContentResponse> b = new ArrayList<>();

    @Override
    protected int getContentView() {
        return R.layout.activity_routing_point_upload;
    }

    @Override
    protected void updateUI() {

        checkLocationPermission();
        data = getIntent().getParcelableExtra(INFO);
        mLineId = getIntent().getStringExtra(LINE_ID);
        mType = getIntent().getStringExtra(TYPE);
        projectId = getIntent().getStringExtra(PROJECT_ID);
        circleId = getIntent().getStringExtra(INSPECTIONDAY_ID);
        if (data != null) {
            tvRoutingTime.setText("巡检时间：" + data.getInspectiontime());
            tvRoutingAddress.setText("巡检地点：" + data.getInspectionname());
            String content = data.getInspectioncontent();
            if (!TextUtils.isEmpty(content)) {
                String[] strings = content.split("\n");

                for (int i = 0; i < strings.length; i++) {
                    b.add(new RoutingContentResponse(strings[i], false));
                }
                mAddContentAdapter = new AddContentAdapter(context, b);
                LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                mRecyclerViewContent.setLayoutManager(layoutManager);
                mRecyclerViewContent.setAdapter(mAddContentAdapter);

            }
            inspectiondayId = String.valueOf(data.getId());

        }

    }


    private void initMapView() {
        mlocationClient = new AMapLocationClient(this);
//初始化定位参数
        mLocationOption = new AMapLocationClientOption();
//设置定位监听
        mlocationClient.setLocationListener(this);
//设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
//设置定位间隔,单位毫秒,默认为2000ms
        mLocationOption.setInterval(5000);
//设置定位参数
        mlocationClient.setLocationOption(mLocationOption);
// 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
// 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
// 在定位结束后，在合适的生命周期调用onDestroy()方法
// 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
//启动定位
        mlocationClient.startLocation();
    }

    @OnClick({R.id.img_back, R.id.tv_apply_ok, R.id.rl_select_notice, R.id.img_one, R.id.img_two,
            R.id.img_three, R.id.img_first, R.id.img_second, R.id.img_third,
            R.id.checkbox_select_normal, R.id.checkbox_select_abnormal})
    public void onClick(View view) {
        ArrayList<File> fileNortmal = new ArrayList<>();
        ArrayList<File> fileAbnormal = new ArrayList<>();
        List<Integer> userId = new ArrayList<>();
        String abnormalContent = editAbnormalContent.getText().toString();

        switch (view.getId()) {
            case R.id.img_back:
                onBackPressed();
                break;
            case R.id.tv_apply_ok:


                if (situation == 1) {
                    if (mFirstFile != null) {
                        fileNortmal.add(mFirstFile);
                    }
                    if (mSecondFile != null) {
                        fileNortmal.add(mSecondFile);
                    }
                    if (mThirdFile != null) {
                        fileNortmal.add(mThirdFile);
                    }

                    if (fileNortmal.isEmpty()) {
                        Toast.makeText(context, "请至少上传一张巡检图片", Toast.LENGTH_SHORT).show();
                    } else {
                        if (TextUtils.isEmpty(mAddress) || mLat == 0) {
                            Toast.makeText(context, "正在获取定位信息", Toast.LENGTH_SHORT).show();
                            mlocationClient.startLocation();
                        } else {
                            boolean checked = true;
                            if (!b.isEmpty() && b.size() > 0) {
                                for (int g = 0; g < b.size(); g++) {
                                    if (!b.get(g).isSelect()) {
                                        checked = false;
                                    }
                                }
                            }
                            if (checked) {
                                AddRoutingContent request = new AddRoutingContent(1,
                                        Integer.valueOf(inspectiondayId), mAddress, mLong, mLat, 1);


                                addData(getMultipartBody(fileNortmal, fileAbnormal,
                                        new Gson().toJson(request), 1), mPrefsHelper.getPrefs().
                                        getString(Constants.TOKEN, ""));
                            } else {
                                Toast.makeText(context, "请勾选所有的巡检内容", Toast.LENGTH_SHORT).show();
                            }


                        }

                    }
                } else if (situation == 0) {
                    if (mFirstFile != null) {
                        fileNortmal.add(mFirstFile);
                    }
                    if (mSecondFile != null) {
                        fileNortmal.add(mSecondFile);
                    }
                    if (mThirdFile != null) {
                        fileNortmal.add(mThirdFile);
                    }
                    if (mOneFile != null) {
                        fileAbnormal.add(mOneFile);
                    }
                    if (mTwoFile != null) {
                        fileAbnormal.add(mTwoFile);
                    }
                    if (mThreeFile != null) {
                        fileAbnormal.add(mThreeFile);

                    }
                    if (fileNortmal.isEmpty()) {
                        Toast.makeText(context, "请至少上传一张巡检图片", Toast.LENGTH_SHORT).show();
                    } else {
                        if (TextUtils.isEmpty(mAddress) || mLat == 0) {
                            Toast.makeText(context, "正在获取定位信息"
                                    , Toast.LENGTH_SHORT).show();
                            mlocationClient.startLocation();
                        } else {

                            if (fileAbnormal.isEmpty()) {
                                Toast.makeText(context, "请至少上传一张异常情况图片", Toast.LENGTH_SHORT).show();
                            } else {

                                if (!TextUtils.isEmpty(abnormalContent)) {
                                    if (mData != null) {
                                        for (int i = 0; i < mData.size(); i++) {
                                            userId.add(mData.get(i).getId());
                                        }
                                    }
                                    AddRoutingContent.ErrorBean
                                            erroBean = new AddRoutingContent.ErrorBean(abnormalContent, userId);

                                    boolean mchecked = true;
                                    if (!b.isEmpty() && b.size() > 0) {
                                        for (int g = 0; g < b.size(); g++) {
                                            if (!b.get(g).isSelect()) {
                                                mchecked = false;
                                            }
                                        }
                                    }
                                    if (mchecked) {
                                        AddRoutingContent request = new AddRoutingContent(0,
                                                Integer.valueOf(inspectiondayId), mAddress, mLong, mLat, erroBean, 1);

                                        addData(getMultipartBody(fileNortmal, fileAbnormal,
                                                new Gson().toJson(request), 0), mPrefsHelper.getPrefs().
                                                getString(Constants.TOKEN, ""));

                                    } else {
                                        Toast.makeText(context, "请勾选所以的巡检内容", Toast.LENGTH_SHORT).show();
                                    }


                                } else {
                                    Toast.makeText(context, "请填写异常内容", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    }

                } else {
                    Toast.makeText(context, "请选择巡检状况", Toast.LENGTH_SHORT).show();

                }


                break;
            case R.id.checkbox_select_normal:
                if (checkBoxNormal.isChecked()) {
                    checkBoxAbnormal.setChecked(false);
                    mLinearLayout.setVisibility(View.GONE);
                    situation = 1;

                } else {
                    checkBoxAbnormal.setChecked(true);
                    mLinearLayout.setVisibility(View.VISIBLE);
                    situation = 0;
                }

                break;
            case R.id.checkbox_select_abnormal:
                if (checkBoxAbnormal.isChecked()) {
                    mLinearLayout.setVisibility(View.VISIBLE);
                    situation = 0;
                    checkBoxNormal.setChecked(false);
                } else {
                    checkBoxNormal.setChecked(true);
                    mLinearLayout.setVisibility(View.GONE);
                    situation = 1;
                }

                break;
            case R.id.rl_select_notice:
                Intent intent = new Intent(context, AddManagerPeopleActivity.class);
                startActivityForResult(intent, REQUEST_PEOPLE);

                break;
            case R.id.img_one:
                showUploadDialog(REQUEST_CAMERA_ONE, REQUEST_PHOTO_ONE);

                break;
            case R.id.img_two:
                showUploadDialog(REQUEST_CAMERA_TWO, REQUEST_PHOTO_TWO);

                break;
            case R.id.img_three:
                showUploadDialog(REQUEST_CAMERA_THREE, REQUEST_PHOTO_THREE);
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


    private void uploadData(ArrayList<PointNeedResponse> mList, String lineId) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.DOMIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);

        UploadPointPatrol uploadPointPatrol = new UploadPointPatrol(lineId, new Gson().toJson(mList));
        Call<ApiResponse> call = userBiz.uploadPoiotByService(mPrefsHelper.getPrefs().
                        getString(Constants.TOKEN, ""),
                uploadPointPatrol);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body().getCode() == 0) {

                    System.out.println("上传成功");

                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }


    private void addData(RequestBody body, String token) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constant.DOMIN_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        FileUploadService userBiz = retrofit.create(FileUploadService.class);

        Call<ApiResponse> call = userBiz.uploadRoutingPointInfo(token, body);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.body().getResultCode() == 200) {
                    // showRegisterDialog();
                    Toast.makeText(context, response.body().getResultMessage(), Toast.LENGTH_SHORT).show();
                    System.out.println("上传成功");
                    switch (mType) {
                        case "one_point":
                            getStartRecord();
                            break;
                        case "first":
                            getStartRecord();
                            break;
                        case "last":
                            stopService(new Intent(context, MLocationService.class));
                            break;
                    }
                    finish();

                } else {
                    Toast.makeText(context, response.body().getResultMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }

    //开始记录
    private void getStartRecord() {
        StartRoutingRecord request = new StartRoutingRecord(circleId, projectId);
        mSubscription = mRemoteService.startRoutingRecord(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""), request)
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
                        if (apiResponse.getCode() == 0) {
                            Gson gson = new Gson();
                            String jsonArray = gson.toJson(apiResponse.getData());
                            NewCircleLineId mLineData = gson.fromJson(jsonArray,
                                    new TypeToken<NewCircleLineId>() {
                                    }.getType());
                            if (mLineId != null) {
                                newCircleId = String.valueOf(mLineData.getId());
                                ArrayList<PointNeedResponse> mdat = new ArrayList<>();
                                mdat.add(new PointNeedResponse(mLong,
                                        mLat,
                                        new SimpleDateFormat("yyyy-MM-dd hh:mm").format(Calendar.getInstance().getTime())));
                                uploadData(mdat, newCircleId);

                                Intent intet1 = new Intent(context, MLocationService.class);
                                intet1.putExtra("lineId", newCircleId);
                                intet1.putExtra("mToken", mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""));
                                startService(intet1);
                            }


                            Log.d("开始记录", "onNext: " + "start");
                        }

                    }
                });


    }

    public static MultipartBody getMultipartBody(ArrayList<File> files, ArrayList<File> mFiles,
                                                 String jsonData, int situation) {

        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (situation == 1) {
            for (int i = 0; i < files.size(); i++) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), files.get(i));
                builder.addFormDataPart("image[]", files.get(i).getName(), requestBody);

            }
        } else if (situation == 0) {
            for (int i = 0; i < files.size(); i++) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), files.get(i));
                builder.addFormDataPart("image[]", files.get(i).getName(), requestBody);

            }
            for (int i = 0; i < mFiles.size(); i++) {
                RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), mFiles.get(i));
                builder.addFormDataPart("warranty_image[]", mFiles.get(i).getName(), requestBody);
            }

        }

        builder.addFormDataPart("data", jsonData);

        builder.setType(MultipartBody.FORM);
        return builder.build();

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == RESULT_OK) {

            switch (requestCode) {
                case REQUEST_PEOPLE:
                    ArrayList<ManagerPeopleResponse> mList =
                            data.getParcelableArrayListExtra("mData");
                    Gson gson = new Gson();
                    Log.d("arraylist", "onActivityResult: " + gson.toJson(mList));
                    if (mData.isEmpty() || mData == null) {
                        mData = mList;
                        System.out.println("ssss");

                    } else {
                        if (mList != null) {
                            for (int j = 0; j < mList.size(); j++) {
                                boolean checked = false;
                                for (int i = 0; i < mData.size(); i++) {
                                    if (mData.get(i).getId() == mList.get(j).getId()) {
                                        checked = true;
                                    }
                                }
                                if (!checked) {
                                    mData.add(mList.get(j));
                                }
                            }
                        }
                        System.out.println("ssss");
                    }

                    Log.d("test", "onActivityResult: " + gson.toJson(mData));
                    if (mData != null) {
                        mAdapter = new AddPersonalAdapter(context, mData);
                        LinearLayoutManager layoutManager = new LinearLayoutManager(context);
                        mRecyclerView.setLayoutManager(layoutManager);
                        mRecyclerView.setAdapter(mAdapter);
                    }

                    break;
                case REQUEST_CAMERA_ONE:
                    startCropImage(mOneUri, RESPONSE_CODE_ONE);

                    break;
                case REQUEST_PHOTO_ONE:
                    startCropImage(data.getData(), RESPONSE_CODE_ONE);

                    break;
                case RESPONSE_CODE_ONE:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgOne))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgOne.setScaleType(ImageView.ScaleType.FIT_XY);
                                imgOne.setImageBitmap(bitmap);
                                imgTwo.setVisibility(View.VISIBLE);

                            });
                    mOneFile = new File(ImageUtils.getRealPath(context, UCrop.getOutput(data)));
//                    firstPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));
//                    firstFile=new File(firstPath);

                    break;
                case REQUEST_CAMERA_TWO:
                    startCropImage(mTwoUri, RESPONSE_CODE_TWO);

                    break;
                case REQUEST_PHOTO_TWO:
                    startCropImage(data.getData(), RESPONSE_CODE_TWO);

                    break;
                case RESPONSE_CODE_TWO:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgTwo))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgTwo.setScaleType(ImageView.ScaleType.FIT_XY);
                                imgTwo.setImageBitmap(bitmap);
                                imgThree.setVisibility(View.VISIBLE);

                            });
                    mTwoFile = new File(ImageUtils.getRealPath(context, UCrop.getOutput(data)));

//                    firstPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));
//                    firstFile=new File(firstPath);

                    break;
                case REQUEST_CAMERA_THREE:
                    startCropImage(mThreeUri, RESPONSE_CODE_THREE);

                    break;
                case REQUEST_PHOTO_THREE:
                    startCropImage(data.getData(), RESPONSE_CODE_THREE);

                    break;
                case RESPONSE_CODE_THREE:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgThree))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgThree.setScaleType(ImageView.ScaleType.FIT_XY);
                                imgThree.setImageBitmap(bitmap);

                            });
                    mThreeFile = new File(ImageUtils.getRealPath(context, UCrop.getOutput(data)));

//                      firstPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));
//                    firstFile=new File(firstPath);

                    break;
                case REQUEST_CAMERA_FIRST:
                    startCropImage(mFirstUri, RESPONSE_CODE_FIRST);

                    break;
                case REQUEST_PHOTO_FIRST:
                    startCropImage(data.getData(), RESPONSE_CODE_FIRST);
                    break;
                case RESPONSE_CODE_FIRST:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgFirst))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgFirst.setScaleType(ImageView.ScaleType.FIT_XY);
                                imgFirst.setImageBitmap(bitmap);
                                imgSecond.setVisibility(View.VISIBLE);

                            });
                    mFirstFile = new File(ImageUtils.getRealPath(context, UCrop.getOutput(data)));

//                    firstPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));
//                    firstFile=new File(firstPath);

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
                    mSecondFile = new File(ImageUtils.getRealPath(context, UCrop.getOutput(data)));

//                    secondPath= ImageUtils.getRealPath(context, UCrop.getOutput(data));
//                    secondFile=new File(secondPath);


                    break;

                case REQUEST_CAMERA_THIRD:
                    startCropImage(mThirdUri, RESPONSE_CODE_THIRD);
                    break;

                case REQUEST_PHOTO_THIRD:
                    startCropImage(data.getData(), RESPONSE_CODE_THIRD);
                    break;
                case RESPONSE_CODE_THIRD:
                    Single.just(ImageUtils.getScaledBitmap(context, UCrop.getOutput(data), imgThird))
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(bitmap -> {
                                imgThird.setScaleType(ImageView.ScaleType.FIT_XY);
                                imgThird.setImageBitmap(bitmap);

                            });
                    mThirdFile = new File(ImageUtils.getRealPath(context, UCrop.getOutput(data)));

//                    thirdPath=ImageUtils.getRealPath(context, UCrop.getOutput(data));
//                    thirdFile=new File(thirdPath);


//                    }

                    break;
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showUploadDialog(int camera, int photo) {
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
        switch (camera) {
            case REQUEST_CAMERA_ONE:
                mOneUri = createImageUri(context);
                Intent mintent = new Intent();
                mintent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                mintent.putExtra(MediaStore.EXTRA_OUTPUT, mOneUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
                startActivityForResult(mintent, camera);

                break;
            case REQUEST_CAMERA_TWO:
                mTwoUri = createImageUri(context);
                Intent mintents = new Intent();
                mintents.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                mintents.putExtra(MediaStore.EXTRA_OUTPUT, mTwoUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
                startActivityForResult(mintents, camera);
                break;
            case REQUEST_CAMERA_THREE:
                mThreeUri = createImageUri(context);
                Intent mIntentes = new Intent();
                mIntentes.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                mIntentes.putExtra(MediaStore.EXTRA_OUTPUT, mThreeUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
                startActivityForResult(mIntentes, camera);
                break;
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
        switch (requestCode) {
            case RESPONSE_CODE_ONE:
                File cropFile = new File(context.getCacheDir(), "a.jpg");
                UCrop.of(resUri, Uri.fromFile(cropFile))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(500, 500)
                        .start(RoutingPointUploadActivity.this, requestCode);
                break;
            case RESPONSE_CODE_TWO:

                File cropFiles = new File(context.getCacheDir(), "b.jpg");
                UCrop.of(resUri, Uri.fromFile(cropFiles))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(500, 500)
                        .start(RoutingPointUploadActivity.this, requestCode);
                break;
            case RESPONSE_CODE_THREE:
                File mcropFiles = new File(context.getCacheDir(), "c.jpg");
                UCrop.of(resUri, Uri.fromFile(mcropFiles))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(500, 500)
                        .start(RoutingPointUploadActivity.this, requestCode);

                break;

            case RESPONSE_CODE_FIRST:
                File mCropFiles = new File(context.getCacheDir(), "d.jpg");
                UCrop.of(resUri, Uri.fromFile(mCropFiles))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(500, 500)
                        .start(RoutingPointUploadActivity.this, requestCode);


                break;
            case RESPONSE_CODE_SECOND:
                File mCropFile = new File(context.getCacheDir(), "e.jpg");
                UCrop.of(resUri, Uri.fromFile(mCropFile))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(500, 500)
                        .start(RoutingPointUploadActivity.this, requestCode);


                break;
            case RESPONSE_CODE_THIRD:
                File CropFile = new File(context.getCacheDir(), "f.jpg");
                UCrop.of(resUri, Uri.fromFile(CropFile))
                        .withAspectRatio(1, 1)
                        .withMaxResultSize(300, 300)
                        .start(RoutingPointUploadActivity.this, requestCode);

                break;


        }


    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {
        if (aMapLocation.getErrorCode() == 0) {
            //定位成功回调信息，设置相关消息
            mAddress = aMapLocation.getAddress();
            mLat = aMapLocation.getLatitude();
            mLong = aMapLocation.getLongitude();

            System.out.println("address" + mAddress + "\n" + "经度" + mLong + "\n纬度：" + mLat);


        } else {
            //显示错误信息ErrCode是错误码，errInfo是错误信息，详见错误码表。
            Log.e("AmapError", "location Error, ErrCode:"
                    + aMapLocation.getErrorCode() + ", errInfo:"
                    + aMapLocation.getErrorInfo());
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mlocationClient != null) {
            mlocationClient.onDestroy(); // 销毁定位客户端
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mlocationClient != null) {
            mlocationClient.stopLocation(); // 停止定位
        }
    }


    /**
     * 检查是否有定位权限
     */
    private void checkLocationPermission() {
        if (PermissionManager.checkLocation(this)) {
            //定位
            initMapView();

        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                Snackbar.make(rootView, "请提供完整权限，以定位您当前位置!", Snackbar.LENGTH_INDEFINITE)
                        .setAction("OK", v -> {
                            PermissionManager.requestLocation(this, REQUEST_LOCATION_CODE);
                        });
            } else {
                PermissionManager.requestLocation(this, REQUEST_LOCATION_CODE);
            }
        }
    }

    // 请求权限后的回调
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_CODE) {
            if (Manifest.permission.ACCESS_FINE_LOCATION.equals(permissions[0])
                    && PackageManager.PERMISSION_GRANTED == grantResults[0]) { // 请求定位权限被允许
                initMapView();

            } else { // 请求定位权限被拒绝
                Snackbar.make(rootView, "请提供完整权限，以定位您当前位置!", Snackbar.LENGTH_SHORT).show();
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    class AddContentAdapter extends RecyclerView.Adapter<AddContentAdapter.ProjectViewHolder> {
        private Context context;
        private ArrayList<RoutingContentResponse> mList;

        public AddContentAdapter(Context context, ArrayList<RoutingContentResponse> mList) {
            this.context = context;
            this.mList = mList;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_recycler_upload_routing_content, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (mList != null && !mList.isEmpty()) {
                RoutingContentResponse data = mList.get(position);
                holder.setItem(data);

                if (data != null) {
                    if (data.getContent() != null) {
                        holder.itemName.setText(data.getContent());
                    }
                    if (data.isSelect()) {
                        holder.rlItem.setChecked(true);
                    } else {
                        holder.rlItem.setChecked(false);
                    }
                }
            }

        }

        @Override
        public int getItemCount() {
            return mList.size();
        }

        public void addAll(ArrayList<RoutingContentResponse> projectList) {
            this.mList.addAll(projectList);
            AddContentAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.mList.clear();
            AddContentAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


            private RoutingContentResponse mRoutingContentResponses;
            private TextView itemName;
            private CheckBox rlItem;
            private RelativeLayout mRelativeLayout;

            public ProjectViewHolder(View itemView) {

                super(itemView);
                itemName = ButterKnife.findById(itemView, R.id.item_content_routing);
                rlItem = ButterKnife.findById(itemView, R.id.checkbox_select_content);
                mRelativeLayout=ButterKnife.findById(itemView,R.id.rl_select_item);
                mRelativeLayout.setOnClickListener(this);


            }

            public void setItem(RoutingContentResponse mRoutingContentResponse) {
                mRoutingContentResponses = mRoutingContentResponse;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.rl_select_item:
                        if (rlItem.isChecked()) {
                            rlItem.setChecked(false);
                            mRoutingContentResponses.setSelect(false);
                        } else {
                            rlItem.setChecked(true);
                            mRoutingContentResponses.setSelect(true);
                        }
                        notifyDataSetChanged();

                        break;

                }


            }
        }
    }

    class AddPersonalAdapter extends RecyclerView.Adapter<AddPersonalAdapter.ProjectViewHolder> {
        private Context context;
        private ArrayList<ManagerPeopleResponse> addPersonalClass;

        public AddPersonalAdapter(Context context, ArrayList<ManagerPeopleResponse> addPersonalClass) {
            this.context = context;
            this.addPersonalClass = addPersonalClass;
        }

        @Override
        public ProjectViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(context);
            View itemView = inflater.inflate(R.layout.item_notice_people, parent, false);
            return new ProjectViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(ProjectViewHolder holder, int position) {
            if (addPersonalClass != null && !addPersonalClass.isEmpty()) {
                ManagerPeopleResponse data = addPersonalClass.get(position);
                holder.setItem(data);

                if (data != null) {
                    if (data.getName() != null) {
                        holder.itemName.setText(data.getName());
                    }
                }
            }

        }

        @Override
        public int getItemCount() {
            return addPersonalClass.size();
        }

        public void addAll(ArrayList<ManagerPeopleResponse> projectList) {
            this.addPersonalClass.addAll(projectList);
            AddPersonalAdapter.this.notifyDataSetChanged();
        }

        public void clear() {
            this.addPersonalClass.clear();
            AddPersonalAdapter.this.notifyDataSetChanged();
        }

        class ProjectViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


            private ManagerPeopleResponse mProjectList;
            private TextView itemName;
            private RelativeLayout rlItem;

            public ProjectViewHolder(View itemView) {

                super(itemView);
                itemName = ButterKnife.findById(itemView, R.id.item_name);
                rlItem = ButterKnife.findById(itemView, R.id.rl_item);
                rlItem.setOnClickListener(this);


            }

            public void setItem(ManagerPeopleResponse projectList) {
                mProjectList = projectList;
            }

            @Override
            public void onClick(View v) {
                switch (v.getId()) {

                    case R.id.rl_item:
                        addPersonalClass.remove(mProjectList);
                        notifyDataSetChanged();
                        break;

                }


            }
        }
    }


}
