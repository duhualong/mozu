package org.eenie.wgj.ui.login;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Display;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;

import com.tbruyelle.rxpermissions.RxPermissions;

import org.eenie.wgj.R;

import java.io.IOException;
import java.util.List;

import rx.functions.Action1;

/**
 * Created by Eenie on 2017/6/14 at 19:46
 * Email: 472279981@qq.com
 * Des:
 */

public class SurfaceViewActivity extends AppCompatActivity implements SurfaceHolder.Callback, View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private String path = "/sdcard/abc.jpg";
    private FrameLayout frameLayout;
    private ImageView btn_capture;
    private SurfaceView surface_camera;
    private Camera mCamera;
    private SurfaceHolder sHolder;
    private int screenWidth, screenHeight;
    private boolean isCamera = false;       //标识是否获得权限


    private Camera.PictureCallback pictureCallback = new Camera.PictureCallback() {
        //byte[]是拍照后完整的数据
        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            /**
             * 使用RxPermissions请求写入sd权限
             */
            new RxPermissions(SurfaceViewActivity.this).request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Action1<Boolean>() {
                        @Override
                        public void call(Boolean granted) {
                            if (granted) {
                                //保存图片

                                String absolutePath = FileUtil.createIfNotExist(path);
                                FileUtil.writeBytes(path, data);
                                Intent intent =new Intent();

//                                Intent intent = new Intent(SurfaceViewActivity.this, ImageActivity.class);

                                intent.putExtra("path", absolutePath);
                                setResult(RESULT_OK,intent);
                                finish();
                            } else {
                                Toast.makeText(SurfaceViewActivity.this, "保存图片失败！", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_surfaceview);
        frameLayout = (FrameLayout) findViewById(R.id.activity_main);
        btn_capture = (ImageView) findViewById(R.id.btn_capture);
        btn_capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                capture();
            }
        });
        /**
         * 使用系统API请求相机权限
         */
        if (ContextCompat.checkSelfPermission(SurfaceViewActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            isCamera = false;
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, MY_PERMISSIONS_REQUEST_CAMERA);
        } else {
            isCamera = true;
            initCamera();
            initDefult();

        }
    }

    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isCamera = true;
                    initCamera();
                    initDefult();
                } else {
                    isCamera = false;
                    Toast.makeText(SurfaceViewActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
                    finish();
                }
                return;
            }
        }
    }

    /**
     * 获得Camera，开启预览
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (isCamera == false) return;
        if (mCamera == null) {
            mCamera = getCamera();
            if (sHolder != null) {
                setStartPreview(mCamera, sHolder);
            }
        }
    }

    /**
     * 停止预览，销毁Camera
     */
    @Override
    protected void onPause() {
        super.onPause();
        releasePreview();
    }


    /**
     * 初始化Camera相关
     */
    private void initCamera() {
        mCamera = getCamera();
        surface_camera = (SurfaceView) findViewById(R.id.surface_camera);
        frameLayout.bringChildToFront(surface_camera);
        frameLayout.bringChildToFront(btn_capture);
        sHolder = surface_camera.getHolder();
        sHolder.addCallback(this);
        surface_camera.setOnClickListener(this);
        setStartPreview(mCamera, sHolder);    //由于APP在第一次安装时，onResume不会执行，所以重新获得cemera权限以后重新start
    }

    /**
     * 初始化手机屏幕参数
     */
    private void initDefult() {
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        btn_capture.setY(screenHeight * 0.8f);
        String surfaceViewSize = getSurfaceViewSize(screenWidth, screenHeight);
        setSurfaceViewSize(surfaceViewSize);
    }

    /**
     * 根据分辨率设置预览SurfaceView的大小以防止变形
     *
     * @param surfaceSize
     */
    private void setSurfaceViewSize(String surfaceSize) {
        ViewGroup.LayoutParams params = surface_camera.getLayoutParams();
        if (surfaceSize.equals("16:9")) {
            params.height = ViewGroup.LayoutParams.MATCH_PARENT;
        } else if (surfaceSize.equals("4:3")) {
            params.height = 4 * screenWidth / 3;
        }
        surface_camera.setLayoutParams(params);
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
     * 获得一个Camera
     */
    private Camera getCamera() {
        Camera camera = Camera.open();
        return camera;
    }

    /**
     * 开启Camera预览
     */
    private void setStartPreview(Camera camera, SurfaceHolder holder) {
        try {
            Camera.Parameters parameters = camera.getParameters();
            List<Camera.Size> size2 = parameters.getSupportedPreviewSizes();     //得到手机支持的预览分辨率
            parameters.setPreviewSize(size2.get(0).width, size2.get(0).height);
            camera.setPreviewDisplay(holder);//绑定holder
            camera.setDisplayOrientation(getPreviewDegree(SurfaceViewActivity.this));//将系统Camera角度进行调整
            camera.startPreview();//开启预览
            camera.setParameters(parameters);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 释放Camera
     */
    private void releasePreview() {
        if (mCamera == null) return;
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();//停止预览
        mCamera.release();
        mCamera = null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        setStartPreview(mCamera, sHolder);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mCamera.stopPreview();
        setStartPreview(mCamera, sHolder);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        releasePreview();
    }

    /**
     * 点击拍照
     */
    private void capture() {
        Camera.Parameters parameters = mCamera.getParameters();
        List<Camera.Size> supportedPictureSizes = parameters.getSupportedPictureSizes();
        parameters.setPictureFormat(ImageFormat.JPEG);//设置图片样式
        parameters.setPictureSize(supportedPictureSizes.get(0).width, supportedPictureSizes.get(0).height);//设置图片大小
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);//自动对焦
        mCamera.setParameters(parameters);
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                if (success) {
                    mCamera.takePicture(null, null, pictureCallback);
                }
            }
        });
    }

    /**
     * 点击屏幕对焦
     */
    @Override
    public void onClick(View v) {
        if (mCamera == null) return;
        mCamera.autoFocus(null);
    }

    // 提供一个静态方法，用于根据手机方向获得相机预览画面旋转的角度
    public static int getPreviewDegree(Activity activity) {
        // 获得手机的方向
        int rotation = activity.getWindowManager().getDefaultDisplay()
                .getRotation();
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
}
