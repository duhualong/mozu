package org.eenie.wgj.ui.mytest;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.OrientationEventListener;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import com.cymaybe.foucsurfaceview.FocusSurfaceView;

import org.eenie.wgj.R;
import org.eenie.wgj.ui.login.camera.AutoFocusManager;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

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
    private Button mTakeBT;
    private AutoFocusManager autoFocusManager;
   // private SensorControler mSensorControler;

    private Camera mCamera;
    private SurfaceHolder mHolder;
    private boolean focus = false;
    private Camera.AutoFocusCallback mAutoFocusCallback;
    private Camera.Parameters mParameters = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_test_main);
        // initDefult();

        initView();
        initData();
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
        String surfaceViewSize = getSurfaceViewSize(screenWidth, screenHeight);
        setSurfaceViewSize(surfaceViewSize);
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


    }

    private void setListener() {
        mTakeBT.setOnClickListener(this);

    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        //initCamera();
        initCameras();

        setCameraParames();
               mCamera.autoFocus((success, camera) -> {
            if (success) {
              //  autoFocusManager = new AutoFocusManager(camera);
                camera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上

            }
        });


    }


    /**
     * 初始化相机
     */
    private void initCameras() {

        if (checkPermission()) {
            try {
                mCamera = android.hardware.Camera.open(0);//1:采集指纹的摄像头. 0:拍照的摄像头.
                mCamera.setPreviewDisplay(mHolder);
               // mSensorControler = SensorControler.getInstance();
                autoFocusManager = new AutoFocusManager(mCamera);
                mCamera.cancelAutoFocus();

                mParameters = mCamera.getParameters();
                mParameters.setPictureFormat(PixelFormat.JPEG);
                mCamera.setParameters(mParameters);


                List<String> focusModes = mParameters.getSupportedFocusModes();

                //设置对焦模式
                if (focusModes.contains(Camera.Parameters.FOCUS_MODE_AUTO))
                    mParameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);

                try {
                    mCamera.setParameters(mParameters);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                Snackbar.make(mTakeBT, "camera open failed!", Snackbar.LENGTH_SHORT).show();
                finish();
                e.printStackTrace();
            }
        } else {
            requestPermission();
        }


    }


    private void initCamera() {
        if (checkPermission()) {
            try {
                mCamera = android.hardware.Camera.open(0);//1:采集指纹的摄像头. 0:拍照的摄像头.
                mCamera.setPreviewDisplay(mHolder);
              //  mSensorControler = SensorControler.getInstance();
                Camera.Parameters parameters = mCamera.getParameters();
//                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_FIXED);
                mCamera.setParameters(parameters);
                if (!focus) {
                    mCamera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
                    //autoFocusManager = new AutoFocusManager(mCamera);
                }
//                mCamera.startPreview();


//

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
        return ContextCompat.checkSelfPermission(getApplicationContext(), CAMERA) ==
                PackageManager.PERMISSION_GRANTED;
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
                        initCameras();
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

//    private void setCameraParames() {
//        if (mCamera == null) {
//            return;
//        }
//        try {
//            Camera.Parameters parameters = mCamera.getParameters();
//            int w = 0;
//            int h = 0;
//
//            for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
//                if (size.width > w) {
//                    w = size.width;
//                    h = size.height;
//                }
//            }
//
//            int pw = 0;
//            int ph = 0;
//            for (Camera.Size size : parameters.getSupportedPictureSizes()) {
//                if (size.width > w) {
//                    pw = size.width;
//                    ph = size.height;
//                }
//            }
//            int orientation = judgeScreenOrientation();
//            if (Surface.ROTATION_0 == orientation) {
//                mCamera.setDisplayOrientation(90);
//                parameters.setRotation(270);
//            } else if (Surface.ROTATION_90 == orientation) {
//                mCamera.setDisplayOrientation(0);
//                parameters.setRotation(0);
//            } else if (Surface.ROTATION_180 == orientation) {
//                mCamera.setDisplayOrientation(180);
//                parameters.setRotation(180);
//            } else if (Surface.ROTATION_270 == orientation) {
//                mCamera.setDisplayOrientation(180);
//                parameters.setRotation(180);
//            }
//            LogUtil.e(String.format("PreviewSize w = %s h = %s", w, h));
////            parameters.setPictureSize(pw, ph);
////            parameters.setPreviewSize(w, h);
//
//            parameters.setPictureSize(w, h);
//            parameters.setPreviewSize(pw, ph);
//            mCamera.setParameters(parameters);
//            mCamera.startPreview();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }


    private void setCameraParames() {
        if (mCamera == null) {
            return;
        }
        try {
            int PreviewWidth = 0;
            int PreviewHeight = 0;
            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);//获取窗口的管理器
            Display display = wm.getDefaultDisplay();//获得窗口里面的屏幕
            Camera.Parameters parameters = mCamera.getParameters();
            // 选择合适的预览尺寸
            List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();

            // 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
            if (sizeList.size() > 1) {
                Iterator<Camera.Size> itor = sizeList.iterator();
                while (itor.hasNext()) {
                    Camera.Size cur = itor.next();
                    if (cur.width >= PreviewWidth
                            && cur.height >= PreviewHeight) {
                        PreviewWidth = cur.width;
                        PreviewHeight = cur.height;
                        break;
                    }
                }
            }
            int orientation = judgeScreenOrientation();
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
            parameters.setPreviewSize(PreviewWidth, PreviewHeight); //获得摄像区域的大小
            parameters.setPictureFormat(PixelFormat.JPEG);//设置照片输出的格式
            parameters.setPictureSize(PreviewWidth, PreviewHeight);//设置拍出来的屏幕大小
            //
            mCamera.setParameters(parameters);//把上面的设置 赋给摄像头
            mCamera.setPreviewDisplay(mHolder);//把摄像头获得画面显示在SurfaceView控件里面
            mCamera.startPreview();//开始预览
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    //用于根据手机方向获得相机预览画面旋转的角度

    private int getPreviewDegree() {
        // 获得手机的方向
        WindowManager windowManager = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        int rotation = windowManager.getDefaultDisplay()
                .getRotation();
        Log.i(TAG, "rotation:" + rotation);
        int degree = 0;
        // 根据手机的方向计算相机预览画面应该选择的角度
        switch (rotation) {
            case Surface.ROTATION_0:
                degree = 90;
                break;
            case Surface.ROTATION_90:
                degree = 0;
                break;
            case Surface.ROTATION_180:
                degree = 270;
                break;
            case Surface.ROTATION_270:
                degree = 180;
                break;
        }
        return degree;
    }


    /**
     * 按比例缩放图片
     * 作者：_SOLID
     * 链接：http://www.jianshu.com/p/23caee6cad0f
     * 來源：简书
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param origin 原图
     * @param ratio  比例
     * @return 新的bitmap
     */
    private Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
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
        //实现自动对焦
        mCamera.autoFocus((success, camera) -> {
            if (success) {
                // autoFocusManager = new AutoFocusManager(camera);
                camera.cancelAutoFocus();//只有加上了这一句，才会自动对

            }
        });

    }


//    //相机参数的初始化设置
//    private void initCameras()
//    {
//
//        parameters=mCamera.getParameters();
//        // parameters.setPictureFormat(PixelFormat.JPEG);
//        //parameters.setPictureSize(surfaceView.getWidth(), surfaceView.getHeight());  // 部分定制手机，无法正常识别该方法。
//        //parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH)
//        // parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);//1连续对焦
//        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO);
//        mCamera.setParameters(parameters);
//        // mCamera.startPreview();
//        //  mCamera.cancelAutoFocus();// 2如果要实现连续的自动对焦，这一句必须加上
//
//    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        releaseCamera();

    }

    private void releaseCamera() {
        if (mCamera != null) {
            autoFocusManager.stop();
            mCamera.stopPreview();
            mCamera.release();
            autoFocusManager = null;
            mCamera = null;

        }
    }

    @Override
    public void onClick(View view) {
        if (mCamera == null) return;
      //  mSensorControler.lockFocus();
        mCamera.autoFocus(null);
        switch (view.getId()) {
            case R.id.take_bt:
                if (!focus) {
                    System.out.println("测试相机：");
                    takePicture();
                }
                break;
        }
    }

    /**
     * 拍照
     */
    private void takePicture() {
        mCamera.autoFocus((success, camera) -> {
            focus = success;
            if (success) {
                mCamera.cancelAutoFocus();
                mCamera.takePicture(() -> {
                }, null, null, (data, camera1) -> {
//                    Bitmap originBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                    Bitmap cropBitmap = previewSFV.getPicture(data);

                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
                            rotateBitmap(cropBitmap), null, null));
                    Intent intent = new Intent();
                    intent.putExtra("uri", uri.toString());
                    setResult(RESULT_OK, intent);
                    finish();

                });
            }
        });
    }

    private Bitmap scaleBitmap(Bitmap origin, int newWidth, int newHeight) {
        if (origin == null) {
            return null;
        }
        int height = origin.getHeight();
        int width = origin.getWidth();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);// 使用后乘
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (!origin.isRecycled()) {
            origin.recycle();
        }
        return newBM;
    }

    private Bitmap rotateBitmap(Bitmap origin) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();

        Matrix matrix = new Matrix();
        if (width < height) {

           // matrix.setRotate(-90);
            matrix.setRotate(getPreviewDegree());

        }
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width
                , height, matrix, false);
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
