package org.eenie.wgj.ui.mytest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.cymaybe.foucsurfaceview.FocusSurfaceView;
import com.facebook.stetho.common.LogUtil;

import org.eenie.wgj.R;

import static android.Manifest.permission.CAMERA;

/**
 * Created by Eenie on 2017/6/15 at 9:17
 * Email: 472279981@qq.com
 * Des:
 */

public class MyTestMain extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {
    private static final String TAG = "moubiao";
    private int screenWidth, screenHeight;

    private FocusSurfaceView previewSFV;
    private Button mTakeBT, mThreeFourBT, mFourThreeBT, mNineSixteenBT, mSixteenNineBT, mFitImgBT, mCircleBT, mFreeBT, mSquareBT,
            mCircleSquareBT, mCustomBT;

    private Camera mCamera;
    private SurfaceHolder mHolder;
    private boolean focus = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_test_main);
        //initDefult();
        initData();
        initView();
        setListener();
    }

    private void initData() {
        DetectScreenOrientation detectScreenOrientation = new DetectScreenOrientation(this);
        detectScreenOrientation.enable();
    }



    private void initDefult() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
//        btn_capture.setY(screenHeight * 0.8f);
//        String surfaceViewSize = getSurfaceViewSize(screenWidth, screenHeight);
//        setSurfaceViewSize(surfaceViewSize);
    }

    public String getSurfaceViewSize(int width, int height) {
        if (equalRate(width, height, 1.33f)) {
            return "4:3";
        } else {
            return "16:9";
        }
    }
    public boolean equalRate(int width, int height, float rate) {
        float r = (float) width / (float) height;
        if (Math.abs(r - rate) <= 0.2) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 根据分辨率设置预览SurfaceView的大小以防止变形
     *
     * @param surfaceSize
     */
    private void setSurfaceViewSize(String surfaceSize) {
        ViewGroup.LayoutParams params = previewSFV.getLayoutParams();
        if (surfaceSize.equals("16:9")) {
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else if (surfaceSize.equals("4:3")) {
            params.height = 4 * screenWidth / 3;
        }
        previewSFV.setLayoutParams(params);
    }

    private void initView() {
        previewSFV = (FocusSurfaceView) findViewById(R.id.preview_sv);
        mHolder = previewSFV.getHolder();
        mHolder.addCallback(MyTestMain.this);
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
        setCameraParames();
    }

    private void initCamera() {
        if (checkPermission()) {
            try {
                mCamera = android.hardware.Camera.open(0);//1:采集指纹的摄像头. 0:拍照的摄像头.
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
                        setCameraParames();
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

            int orientation = judgeScreenOrientation();
            if (Surface.ROTATION_0 == orientation) {
                mCamera.setDisplayOrientation(90);
                parameters.setRotation(90);
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

            parameters.setPictureSize(1280, 720);
            parameters.setPreviewSize(1280, 720);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void setCameraParames() {
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
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
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
            if (mCamera == null) return;
            mCamera.autoFocus(null);
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
                            Bitmap originBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                            Bitmap cropBitmap = previewSFV.getPicture(data);
//                            rotateBitmap(cropBitmap,90);
                            Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),rotateBitmap(cropBitmap,90), null,null));
                            Intent intent=new Intent();
                            intent.putExtra("uri",uri.toString());
                            setResult(RESULT_OK,intent);

                            finish();
//                            PictureFragment pictureFragment = new PictureFragment();
//                            Bundle bundle = new Bundle();
//                            bundle.putParcelable(ORIGIN_PICTURE, originBitmap);
//                            bundle.putParcelable(CROP_PICTURE, cropBitmap);
//                            pictureFragment.setArguments(bundle);
                          //  pictureFragment.show(getFragmentManager(), null);

//                            mCamera.startPreview();
                        }
                    });
                }
            }
        });
    }

    private Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    /**
     * 用来监测左横屏和右横屏切换时旋转摄像头的角度
     */
    private class DetectScreenOrientation extends OrientationEventListener {
        DetectScreenOrientation(MyTestMain context) {
            super(context);
        }

        @Override
        public void onOrientationChanged(int orientation) {
            if (260 < orientation && orientation < 290) {
                setCameraParames();
            } else if (80 < orientation && orientation < 100) {
                setCameraParames();
            }
        }
    }
}
