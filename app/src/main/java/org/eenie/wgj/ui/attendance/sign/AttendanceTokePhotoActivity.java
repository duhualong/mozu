package org.eenie.wgj.ui.attendance.sign;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;

import com.cymaybe.foucsurfaceview.FocusSurfaceView;
import com.facebook.stetho.common.LogUtil;

import org.eenie.wgj.R;
import org.eenie.wgj.base.BaseActivity;

import org.eenie.wgj.util.ToastUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static android.Manifest.permission.CAMERA;
import static org.eenie.wgj.ui.mytest.PictureFragment.CROP_PICTURE;

/**
 * Created by Eenie on 2017/6/18 at 15:42
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceTokePhotoActivity extends BaseActivity implements  View.OnClickListener,
        SurfaceHolder.Callback {

    private static final String TAG = "moubiao";

    public static final int REQUEST_CODE = 1010;
    public static final int REQUEST_CODE_NEED_EXTRA = 1013;
    public static final int RESULT_CODE = 1011;

    private int camera_flag = 1;

    private FocusSurfaceView previewSFV;
    private Button mTakeBT, mThreeFourBT, mFourThreeBT, mNineSixteenBT, mSixteenNineBT, mFitImgBT, mCircleBT, mFreeBT, mSquareBT,
            mCircleSquareBT, mCustomBT;

    private Camera mCamera;
    private SurfaceHolder mHolder;
    private boolean focus = false;

//    @Override
//    public int initContentView() {
//        return R.layout.activity_attendance_toke_photo;
//    }
//
//    @Override
//    public void initInjector() {
//        camera_flag = getIntent().getIntExtra("camera_flag", 1);
//    }
//
//    @Override
//    public void initUiAndListener() {
//
//
//    }
//
//    @Override
//    protected boolean isApplyStatusBarTranslucency() {
//        return false;
//    }
//
//    @Override
//    protected boolean isApplyStatusBarColor() {
//        return false;
//    }


    @Override
    protected int getContentView() {
        return R.layout.activity_attendance_take_photo;
    }

    @Override
    protected void updateUI() {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initData();
        initView();
        setListener();
    }

    private void initData() {
        DetectScreenOrientation detectScreenOrientation = new DetectScreenOrientation(this);
        detectScreenOrientation.enable();
    }

    private void initView() {
        previewSFV = (FocusSurfaceView) findViewById(R.id.preview_sv);
        mHolder = previewSFV.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mTakeBT = (Button) findViewById(R.id.take_bt);
        mThreeFourBT = (Button) findViewById(R.id.three_four_bt);
        mFourThreeBT = (Button) findViewById(R.id.four_three_bt);
        mNineSixteenBT = (Button) findViewById(R.id.nine_sixteen_bt);
        mSixteenNineBT = (Button) findViewById(R.id.sixteen_nine_bt);
        mFitImgBT = (Button) findViewById(R.id.fit_image_bt);
        mCircleBT = (Button) findViewById(R.id.circle_bt);
        mFreeBT = (Button) findViewById(R.id.free_bt);
        mSquareBT = (Button) findViewById(R.id.square_bt);
        mCircleSquareBT = (Button) findViewById(R.id.circle_square_bt);
        mCustomBT = (Button) findViewById(R.id.custom_bt);
    }

    private void setListener() {
        mTakeBT.setOnClickListener(this);
        mThreeFourBT.setOnClickListener(this);
        mFourThreeBT.setOnClickListener(this);
        mNineSixteenBT.setOnClickListener(this);
        mSixteenNineBT.setOnClickListener(this);
        mFitImgBT.setOnClickListener(this);
        mCircleBT.setOnClickListener(this);
        mFreeBT.setOnClickListener(this);
        mSquareBT.setOnClickListener(this);
        mCircleSquareBT.setOnClickListener(this);
        mCustomBT.setOnClickListener(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        initCamera();
        setCameraParams();
        previewSFV.setCropMode(FocusSurfaceView.CropMode.RATIO_3_4);
    }

    private void initCamera() {
        if (checkPermission()) {
            try {
                mCamera = android.hardware.Camera.open(camera_flag);//1:采集指纹的摄像头. 0:拍照的摄像头.
                mCamera.setPreviewDisplay(mHolder);
            } catch (Exception e) {
                Snackbar.make(mTakeBT, "camera open failed!", Snackbar.LENGTH_SHORT).show();
                finish();
                e.printStackTrace();
            }
        } else {
            requestPermission();
        }
    }

    private boolean checkPermission() {
        return ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{CAMERA}, 10000);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 10000:
                if (grantResults.length > 0) {
                    if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        initCamera();
                        setCameraParams();
                    }
                }

                break;
        }
    }

    private void setCameraParams() {
        if (mCamera == null) {
            return;
        }
        try {
            Camera.Parameters parameters = mCamera.getParameters();


            int w = 0;
            int h = 0;


            for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
                if (size.width > w) {
                    w = size.width;
                    h = size.height;
                }
//                LogUtil.e(String.format("PreviewSize w = %s h = %s", size.width, size.height));
            }


            int pw = 0;
            int ph = 0;

            for (Camera.Size size : parameters.getSupportedPictureSizes()) {
                if (size.width > w) {
                    pw = size.width;
                    ph = size.height;
                }
            }


//            Camera.Size size = parameters.getSupportedPreviewSizes().get(0);

            int orientation = judgeScreenOrientation();

//            System.out.println("orientation " + orientation);


            if (Surface.ROTATION_0 == orientation) {
                mCamera.setDisplayOrientation(90);
                parameters.setRotation(270);
            } else if (Surface.ROTATION_90 == orientation) {
                mCamera.setDisplayOrientation(0);
                parameters.setRotation(0);
            } else if (Surface.ROTATION_180 == orientation) {
                mCamera.setDisplayOrientation(180);
                parameters.setRotation(180);
            } else if (Surface.ROTATION_270 == orientation) {
                mCamera.setDisplayOrientation(180);
                parameters.setRotation(180);
            }

            LogUtil.e(String.format("PreviewSize w = %s h = %s", w, h));
            parameters.setPictureSize(pw, ph);
            parameters.setPreviewSize(w, h);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    }

    /**
     * 判断屏幕方向
     *
     * @return 0：竖屏 1：左横屏 2：反向竖屏 3：右横屏
     */
    private int judgeScreenOrientation() {
        return getWindowManager().getDefaultDisplay().getRotation();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();
    }

    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.take_bt:
                if (!focus) {
                    takePicture();
                }
                break;
            case R.id.three_four_bt:
                previewSFV.setCropMode(FocusSurfaceView.CropMode.RATIO_3_4);
                break;
            case R.id.four_three_bt:
                previewSFV.setCropMode(FocusSurfaceView.CropMode.RATIO_4_3);
                break;
            case R.id.nine_sixteen_bt:
                previewSFV.setCropMode(FocusSurfaceView.CropMode.RATIO_9_16);
                break;
            case R.id.sixteen_nine_bt:
                previewSFV.setCropMode(FocusSurfaceView.CropMode.RATIO_16_9);
                break;
            case R.id.fit_image_bt:
                previewSFV.setCropMode(FocusSurfaceView.CropMode.FIT_IMAGE);
                break;
            case R.id.circle_bt:
                previewSFV.setCropMode(FocusSurfaceView.CropMode.CIRCLE);
                break;
            case R.id.free_bt:
                previewSFV.setCropMode(FocusSurfaceView.CropMode.FREE);
                break;
            case R.id.square_bt:
                previewSFV.setCropMode(FocusSurfaceView.CropMode.SQUARE);
                break;
            case R.id.circle_square_bt:
                previewSFV.setCropMode(FocusSurfaceView.CropMode.CIRCLE_SQUARE);
                break;
            case R.id.custom_bt:
                previewSFV.setCropMode(FocusSurfaceView.CropMode.CUSTOM);
                break;
            default:
                break;
        }
    }

    /**
     * 拍照
     */
    private void takePicture() {
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                focus = success;
                if (success) {
                    mCamera.cancelAutoFocus();
                    mCamera.takePicture(new Camera.ShutterCallback() {
                        @Override
                        public void onShutter() {
                        }
                    }, null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] data, Camera camera) {
                            Bitmap cropBitmap = previewSFV.getPicture(data);
                            PictureFragment pictureFragment = new PictureFragment();
                            Bundle bundle = new Bundle();
//                            bundle.putParcelable(ORIGIN_PICTURE, originBitmap);
                            bundle.putParcelable(CROP_PICTURE, cropBitmap);
                            pictureFragment.setArguments(bundle);
                            pictureFragment.show(getFragmentManager(), null);
                            focus = false;
                            mCamera.startPreview();
                        }
                    });
                }
            }
        });
    }

    /**
     * 用来监测左横屏和右横屏切换时旋转摄像头的角度
     */
    private class DetectScreenOrientation extends OrientationEventListener {
        DetectScreenOrientation(Context context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (260 < orientation && orientation < 290) {
                setCameraParams();
            } else if (80 < orientation && orientation < 100) {
                setCameraParams();
            }
        }
    }


    public void onTakePhoto(Bitmap bitmap) {
        String path = savePic(bitmap);
        if (!TextUtils.isEmpty(path)) {
            setResult(AttendanceTokePhotoActivity.RESULT_CODE, getIntent().putExtra("path", path));
            finish();
        } else {
            ToastUtil.showToast("照片保存失败，请重拍");
        }
    }


    public String savePic(Bitmap bm) {
        String path = getExternalCacheDir().getAbsolutePath() + "/sign_" + System.currentTimeMillis() + "_attendance.jpeg";
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i(TAG, "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return path;
    }
}
