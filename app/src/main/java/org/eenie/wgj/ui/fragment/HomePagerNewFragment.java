package org.eenie.wgj.ui.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.yyydjk.library.BannerLayout;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseSupportFragment;
import org.eenie.wgj.data.local.HomeModule;
import org.eenie.wgj.model.ApiResponse;
import org.eenie.wgj.model.requset.UserId;
import org.eenie.wgj.model.response.AttendanceListResponse;
import org.eenie.wgj.model.response.UserInforById;
import org.eenie.wgj.ui.routinginspection.api.ProgressSubscriber;
import org.eenie.wgj.ui.scan.ScanActivity;
import org.eenie.wgj.util.Constants;
import org.eenie.wgj.util.PermissionManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Eenie on 2017/8/1 at 10:41
 * Email: 472279981@qq.com
 * Des:
 */

public class HomePagerNewFragment extends BaseSupportFragment {
    private static final int REQUEST_CAMERA_PERMISSION = 0x104;
    @BindView(R.id.banner_top)
    BannerLayout bannerTop;
    @BindView(R.id.banner_bottom)
    BannerLayout bannerBottom;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.home_root_view)
    View rootView;
    @BindView(R.id.rv_module)
    RecyclerView rvModule;

    ModuleAdapter mAdapter;
    List<HomeModule> modules = new ArrayList<>();

    boolean isModuleChange = false;

    boolean permission = false;
    private Integer[] ids = {R.mipmap.ic_home_banner_one, R.mipmap.ic_home_banner_two,
            R.mipmap.ic_home_banner_three, R.mipmap.home_banner_default_bg3};
    private Integer[] idBottom = {R.mipmap.home_banner_default_bg4, R.mipmap.home_banner_default_bg5};

    @Override
    protected int getContentView() {
        return R.layout.fragment_new_home_pager;
    }

    @Override
    protected void updateUI() {

        initPersonalInfo();
        initData();
        initDatas();
        checkPermission();
        //禁止滑动
//        MyGridLayoutManager gridLayoutManager = new
//                MyGridLayoutManager(getContext(), 4);//两列
//        gridLayoutManager.setScrollEnabled(false);

        rvModule.setLayoutManager(new GridLayoutManager(getContext(), 4));
        mAdapter = new ModuleAdapter(modules);
        rvModule.setAdapter(mAdapter);
        getModule();


    }

    private void getModule() {
        modules.clear();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<HomeModule> results = realm.where(HomeModule.class).findAllSorted("index");

        for (HomeModule m : results) {
            if (m.isShow()) {
                modules.add(m);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void getBundle(Bundle bundle) {

//
////        if (modules.isEmpty() || isModuleChange) {
////            reloadModule();
////        }
//        reloadModule();

    }


    private void reloadModule() {
        modules.clear();
        Realm realm = Realm.getDefaultInstance();
        RealmResults<HomeModule> results = realm.where(HomeModule.class)
                .equalTo("show", true)
                .findAllSorted("index");
        for (HomeModule m : results) {
            if (m.isShow()) {
                modules.add(m);
            }
        }
        isModuleChange = false;
    }

    public void needRefresh() {
        isModuleChange = true;
    }


    @OnClick({R.id.img_scan, R.id.img_search})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_scan:
                if (permission) {
                    IntentIntegrator integrator = IntentIntegrator.forSupportFragment(HomePagerNewFragment.this);
                    integrator.setCaptureActivity(ScanActivity.class);
                    integrator.initiateScan();
                } else {
                    checkPermission();
                }
                break;
            case R.id.img_search:
                  //startActivity(new Intent(context, RoutingPointUploadActivity.class));
                break;
        }
    }

    /**
     * 针对高版本系统检查权限
     */
    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            boolean hasPermission = PermissionManager.checkCameraPermission(context)
                    && PermissionManager.checkWriteExternalStoragePermission(context);
            if (hasPermission) {
                permission = true;

            } else {
                showRequestPermissionDialog();
            }
        } else {
            permission = true;
        }
    }

    /**
     * 请求权限Snackbar
     */
    private void showRequestPermissionDialog() {
        if (this.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            Snackbar.make(rootView, "请提供摄像头及文件权限，以拍摄和预览相机图片!", Snackbar.LENGTH_INDEFINITE)
                    .setAction("OK", v -> {
                        PermissionManager.invokeCameras(HomePagerNewFragment.this, REQUEST_CAMERA_PERMISSION);
                    })
                    .show();
        } else {
            PermissionManager.invokeCameras(HomePagerNewFragment.this, REQUEST_CAMERA_PERMISSION);
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
                    permission = true;

                } else {
                    Snackbar.make(rootView, "请完整的权限，以预览裁剪图片!", Snackbar.LENGTH_SHORT).show();
                }
                break;

        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (scanResult != null) {
            String contents = scanResult.getContents();
            System.out.println("扫码内容" + contents);
            if (!TextUtils.isEmpty(contents)) {
                if (contents.startsWith("meet")) {
                    String[] temp = null;
                    temp = contents.split("=");
                    if (temp[1] != null) {
                        scanMeeting(temp[1]);
                    }

                }
            }
        }

    }

    private void scanMeeting(String id) {
        mSubscription = mRemoteService.checkInMeeting(mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""), id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new ProgressSubscriber<ApiResponse>(context) {
                    @Override
                    public void onNext(ApiResponse apiResponse) {
                        if (apiResponse.getResultCode() == 200 || apiResponse.getResultCode() == 0) {
                            Toast.makeText(context, apiResponse.getResultMessage(), Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(context, apiResponse.getResultMessage(), Toast.LENGTH_SHORT).show();
                        }

                    }
                });

    }

    private void initPersonalInfo() {

        UserId mUser = new UserId(mPrefsHelper.getPrefs().getString(Constants.UID, ""));
        mSubscription = mRemoteService.getUserInfoById(mPrefsHelper.getPrefs().
                getString(Constants.TOKEN, ""), mUser)
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
                        Gson gson = new Gson();
                        String jsonArray = gson.toJson(apiResponse.getData());
                        UserInforById mData = gson.fromJson(jsonArray,
                                new TypeToken<UserInforById>() {
                                }.getType());
                        if (mData != null) {
                            if (!mData.getCompany_name().isEmpty() && mData.getCompany_name() != null) {
                                tvTitle.setText(mData.getCompany_name());
                                mPrefsHelper.getPrefs().edit().putString(Constants.COMPANY_NAME,
                                        mData.getCompany_name()).apply();

                            } else {
                                mPrefsHelper.getPrefs().edit().putString(Constants.COMPANY_NAME,
                                        "").apply();
                            }
                            if (!mData.getProject_name().isEmpty() && mData.getProject_name() != null) {
                                mPrefsHelper.getPrefs().edit().putString(Constants.PROJECT_NAME,
                                        mData.getProject_name()).apply();
                            } else {
                                mPrefsHelper.getPrefs().edit().putString(Constants.PROJECT_NAME,
                                        "").apply();
                            }

                        }
                    }
                });

    }


    //初始化数据
    private void initData() {
        bannerTop.setViewRes(Arrays.asList(ids));
        bannerBottom.setViewRes(Arrays.asList(idBottom));

    }

    private void initDatas() {

        mSubscription = mRemoteService.getAttendanceList(
                mPrefsHelper.getPrefs().getString(Constants.TOKEN, ""),
                new SimpleDateFormat("yyyy-MM").format(Calendar.getInstance().getTime()))
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
                        Gson gson = new Gson();
                        if (apiResponse.getResultCode() == 200 ||
                                apiResponse.getResultCode() == 0) {
                            if (apiResponse.getData() != null) {

                                String jsonArray = gson.toJson(apiResponse.getData());
                                ArrayList<AttendanceListResponse> attendanceResponse =
                                        gson.fromJson(jsonArray,
                                                new TypeToken<ArrayList<AttendanceListResponse>>() {
                                                }.getType());

                                if (attendanceResponse != null) {
                                    ArrayList<String> mList = new ArrayList<>();
                                    ArrayList<String> mLists = new ArrayList<>();
                                    for (int i = 0; i < attendanceResponse.size(); i++) {
                                        mList.add(attendanceResponse.get(i).getDay());
                                        mLists.add(attendanceResponse.get(i).getService().
                                                getServicesname());
                                    }
                                    mPrefsHelper.getPrefs().edit().
                                            putString(Constants.DATE_LIST, gson.toJson(mList))
                                            .putString(Constants.DATE_THING_LIST, gson.toJson(mLists))
                                            .apply();

                                } else {
                                    mPrefsHelper.getPrefs().edit().
                                            putString(Constants.DATE_LIST, "")
                                            .putString(Constants.DATE_THING_LIST, "")
                                            .apply();
                                }
                            }
                        } else {
                            mPrefsHelper.getPrefs().edit().
                                    putString(Constants.DATE_LIST, "")
                                    .putString(Constants.DATE_THING_LIST, "")
                                    .apply();

                        }

                    }
                });
    }

}
