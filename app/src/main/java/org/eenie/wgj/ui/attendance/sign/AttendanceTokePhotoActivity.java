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

import butterknife.BindView;
import butterknife.OnClick;

import static android.Manifest.permission.CAMERA;
import static org.eenie.wgj.ui.mytest.PictureFragment.CROP_PICTURE;

/**
 * Created by Eenie on 2017/6/18 at 15:42
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceTokePhotoActivity extends BaseActivity implements
        SurfaceHolder.Callback {

    private static final String TAG = "moubiao";

    public static final int REQUEST_CODE = 1010;
    public static final int REQUEST_CODE_NEED_EXTRA = 1013;
    public static final int RESULT_CODE = 1011;

    private int camera_flag = 1;

    @BindView(R.id.preview_sv)
    FocusSurfaceView previewSFV;
    @BindView(R.id.take_bt)
    Button mTakeBT;

    private Camera mCamera;
    private SurfaceHolder mHolder;
    private boolean focus = false;


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
    }

    private void initData() {
        DetectScreenOrientation detectScreenOrientation = new DetectScreenOrientation(this);
        detectScreenOrientation.enable();
    }

    private void initView() {
        mHolder = previewSFV.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        initCamera();
        setCameraParames();
        previewSFV.setCropMode(FocusSurfaceView.CropMode.RATIO_3_4);
    }


    private void initCamera() {
        if (checkPermission()) {
            try {
                mCamera = android.hardware.Camera.open(camera_flag);//1:采集指纹的摄像头. 0:拍照的摄像头.
                mCamera.setPreviewDisplay(mHolder);
                mCamera.cancelAutoFocus();
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

    private void setCameraParames() {
        if (mCamera == null) {
            return;
        }
//
//        try {
//            int PreviewWidth = 0;
//            int PreviewHeight = 0;
//            WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);//获取窗口的管理器
//            Display display = wm.getDefaultDisplay();//获得窗口里面的屏幕
//            Camera.Parameters parameters  = mCamera.getParameters();
//            // 选择合适的预览尺寸
//            List<Camera.Size> sizeList = parameters.getSupportedPreviewSizes();
//
//            // 如果sizeList只有一个我们也没有必要做什么了，因为就他一个别无选择
//            if (sizeList.size() > 1) {
//                Iterator<Camera.Size> itor = sizeList.iterator();
//                while (itor.hasNext()) {
//                    Camera.Size cur = itor.next();
//                    if (cur.width >= PreviewWidth
//                            && cur.height >= PreviewHeight) {
//                        PreviewWidth = cur.width;
//                        PreviewHeight = cur.height;
//                        break;
//                    }
//                }
//            }
//
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
//            parameters.setPreviewSize(PreviewWidth, PreviewHeight); //获得摄像区域的大小
//            parameters.setPreviewFrameRate(3);//每秒3帧  每秒从摄像头里面获得3个画面
//            parameters.setPictureFormat(PixelFormat.JPEG);//设置照片输出的格式
//            parameters.set("jpeg-quality", 85);//设置照片质量
//            parameters.setPictureSize(PreviewWidth, PreviewHeight);//设置拍出来的屏幕大小
//            //
//            mCamera.setParameters(parameters);//把上面的设置 赋给摄像头
//            mCamera.setPreviewDisplay(previewSFV.getHolder());//把摄像头获得画面显示在SurfaceView控件里面
//            mCamera.startPreview();//开始预览
//           // mPreviewRunning = true;
//        } catch (IOException e) {
//            Log.e(TAG, e.toString());
//        }


        try {
            Camera.Parameters parameters = mCamera.getParameters();

            int w = 0;
            int h = 0;


            for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
                if (size.width > w) {
                    w = size.width;
                    h = size.height;
                }
            }

            int pw = 0;
            int ph = 0;
            for (Camera.Size size : parameters.getSupportedPictureSizes()) {
                if (size.width > w) {
                    pw = size.width;
                    ph = size.height;
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
            LogUtil.e(String.format("PreviewSize w = %s h = %s", w, h));
            parameters.setPictureSize(pw, ph);
            parameters.setPictureSize(w, h);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    private void setCameraParams() {
//        if (mCamera == null) {
//            return;
//        }
//        try {
//            Camera.Parameters parameters = mCamera.getParameters();
//            int w = 0;
//            int h = 0;
//            for (Camera.Size size : parameters.getSupportedPreviewSizes()) {
//                if (size.width > w) {
//                    w = size.width;
//                    h = size.height;
//                }
////                LogUtil.e(String.format("PreviewSize w = %s h = %s", size.width, size.height));
//            }
//
//
//            int pw = 0;
//            int ph = 0;
//
//            for (Camera.Size size : parameters.getSupportedPictureSizes()) {
//                if (size.width > w) {
//                    pw = size.width;
//                    ph = size.height;
//                }
//            }
//
//
//            int orientation = judgeScreenOrientation();
//
//
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
//
//            LogUtil.e(String.format("PreviewSize w = %s h = %s", w, h));
//            parameters.setPictureSize(pw, ph);
//            parameters.setPreviewSize(w, h);
//            mCamera.setParameters(parameters);
//            mCamera.startPreview();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

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

    @OnClick(R.id.take_bt)
    public void onClick() {
        if (mCamera == null) return;
        mCamera.autoFocus(null);
        takePicture();
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

                    Bitmap cropBitmap = previewSFV.getPicture(data);
//                    Bitmap bMap = BitmapFactory.decodeByteArray(data, 0,data.length);
//                    Bitmap out = Bitmap.createScaledBitmap(bMap, 1024, 768, true);
                    PictureFragment pictureFragment = new PictureFragment();
                    Bundle bundle = new Bundle();
//                       bundle.putParcelable(ORIGIN_PICTURE, originBitmap);
                    bundle.putParcelable(CROP_PICTURE, cropBitmap);
                    pictureFragment.setArguments(bundle);
                    pictureFragment.show(getFragmentManager(), null);
                    focus = false;
                     mCamera.startPreview();
                });
            }
        });

    }



//    private void takePicture() {
//        mCamera.autoFocus((success, camera) -> {
//            focus = success;
//            if (success) {
//                mCamera.cancelAutoFocus();
//                mCamera.takePicture(() -> {
//                }, null, null, (data, camera1) -> {
////                    Bitmap originBitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//                    Bitmap cropBitmap = previewSFV.getPicture(data);
//                    Uri uri = Uri.parse(MediaStore.Images.Media.insertImage(getContentResolver(),
//                            rotateBitmap(cropBitmap, 90), null, null));
//                    Intent intent = new Intent();
//                    intent.putExtra("uri", uri.toString());
//                    setResult(RESULT_OK, intent);
//                    finish();
//
//                });
//            }
//        });
//    }

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
                setCameraParames();
            } else if (80 < orientation && orientation < 100) {
                setCameraParames();
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




        String path = getExternalCacheDir().getAbsolutePath() + "/sign_" +
                System.currentTimeMillis() + "_attendance.jpeg";
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
