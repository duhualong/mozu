package org.eenie.wgj;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


/**
 * Created by Eenie on 2017/7/14 at 16:41
 * Email: 472279981@qq.com
 * Des:
 */

public class MainTestPictureActivity extends Activity {
    private static final int CAMERA_CODE = 3022;
    private static final int SD_CODE = 3020;
    private ArrayList<Shapes> shapes = new ArrayList<>();
    private ImageView iv;//展示图片
    private Bitmap copyPic;//编辑图片
    private Canvas canvas;//画板
    private Paint paint;//画笔
    private Matrix matrix;//矩阵
    private Bitmap srcPic;//原图
    private int color = Color.RED;//画笔颜色
    private int width = 10;//画笔大小
    private int circle;//形状
    /* 用来标识请求照相功能的activity */
    private static final int CAMERA_WITH_DATA = 3023;

    /* 用来标识请求gallery的activity */
    private static final int PHOTO_PICKED_WITH_DATA = 3021;

    private String photoPath, camera_path, tempPhotoPath;
    //图片保存路径
    public static final String filePath = Environment.getExternalStorageDirectory() + "/PictureTest/";
    private int screenWidth;
    private File mCurrentPhotoFile;
    private LinearLayout ll;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_picture_edit);
        ll = (LinearLayout) findViewById(R.id.ll);
        iv = (ImageView) findViewById(R.id.iv);
        DisplayMetrics metric = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metric);
        // 屏幕宽度（像素）
        screenWidth = metric.widthPixels;
    }

    /**
     * 画画
     */
    private void drawPic() {
        srcPic = BitmapFactory.decodeFile(camera_path);
        copyPic = Bitmap.createBitmap(srcPic.getWidth(), srcPic.getHeight(),
                srcPic.getConfig());
        iv.setLayoutParams(new LinearLayout.LayoutParams(
                srcPic.getWidth(), srcPic.getHeight()));
        canvas = new Canvas(copyPic);
        paint = new Paint();
        paint.setAntiAlias(true);
        //绘制原图
        drawOld();
        iv.setImageBitmap(copyPic);
        //触摸事件
        iv.setOnTouchListener(new View.OnTouchListener() {

            private float endY;
            private float endX;
            private float startX;
            private float startY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:// 按下的事件类型
                        startX = event.getX();
                        startY = event.getY();
                        drawGuiji();
                        break;

                    case MotionEvent.ACTION_MOVE:// 移动的事件类型
                        // 得到结束位置的坐标点
                        endX = event.getX();
                        endY = event.getY();
                        // 清除之前轨迹
                        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
                        canvas.drawPaint(paint);
                        drawGuiji();
                        paint.setStrokeWidth(width);
                        paint.setColor(color);
                        if (circle == 1) {
                            paint.setStyle(Paint.Style.STROKE);//设置边框
                            canvas.drawRect(startX, startY, endX, endY, paint);// 正方形
                        } else if (circle == 0) {
                            paint.setStyle(Paint.Style.STROKE);//设置边框
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                canvas.drawOval(startX, startY, endX, endY, paint);
                            }
                        } else if (circle == 2) {
                            paint.setStyle(Paint.Style.FILL);//设置边框
                            drawArrow(startX, startY, endX, endY, width, paint);
                        }
                        iv.setImageBitmap(copyPic);
                        break;

                    case MotionEvent.ACTION_UP:// 移动的事件类型
                        shapes.add(new Shapes(startX, startY, endX, endY, width, paint.getColor(), circle));//保存历史轨迹
                        break;
                }
                return true;
            }
        });
    }

    private void drawGuiji() {
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC));
        drawOld();
        for (Shapes sp : shapes) {//画历史轨迹
            paint.setColor(sp.color);
            paint.setStrokeWidth(sp.width);
            if (sp.circle == 1) {
                paint.setStyle(Paint.Style.STROKE);//设置边框
                canvas.drawRect(sp.startX, sp.startY, sp.endX, sp.endY, paint);// 正方形
            } else if (sp.circle == 0) {
                paint.setStyle(Paint.Style.STROKE);//设置边框
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//api21之后的方法
                    canvas.drawOval(sp.startX, sp.startY, sp.endX, sp.endY, paint);//椭圆
                }
            } else if (sp.circle == 2) {
                paint.setStyle(Paint.Style.FILL);//设置边框
                drawArrow(sp.startX, sp.startY, sp.endX, sp.endY, sp.width, paint);//箭头
            }
        }
        iv.setImageBitmap(copyPic);
    }

    /**
     * 绘制底图
     */
    private void drawOld() {
        // 给画笔设置默认的颜色，在画画的过程中会使用原图的颜色来画画
        paint.setColor(Color.BLACK);

        // 处理图形
        matrix = new Matrix();
        // 5、使用画笔在画板上画画
        // 参看原图画画
        // srcPic 原图
        // matrix 表示图形的矩阵对象,封装了处理图形的api
        // paint 画画时使用的画笔
        canvas.drawBitmap(srcPic, matrix, paint);
    }

    /**
     * 红色按钮
     *
     * @param view
     */
    public void red(View view) {

        color = Color.RED;
    }

    /**
     * 绿色按钮
     *
     * @param view
     */
    public void green(View view) {
        color = Color.GREEN;

    }

    /**
     * 蓝色按钮
     *
     * @param view
     */
    public void blue(View view) {
        color = Color.BLUE;
    }

    public void small(View view) {
        //改变刷子的宽度
        width = 1;
    }

    public void zhong(View view) {
        //改变刷子的宽度
        width = 5;
    }

    public void big(View view) {
        //改变刷子的宽度
        width = 10;
    }

    /**
     * 圆形
     *
     * @param view
     */
    public void circle(View view) {
        circle = 0;
    }

    /**
     * 矩形
     *
     * @param view
     */
    public void fang(View view) {
        circle = 1;
    }

    /**
     * 矩形
     *
     * @param view
     */
    public void arrow(View view) {
        circle = 2;
    }

    /**
     * 相册
     *
     * @param view
     */
    public void pics(View view) {
        //申请照相机权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        SD_CODE);
            } else {
                getPictureFromPhoto();
            }
        } else {
            getPictureFromPhoto();
        }
    }

    /**
     * 拍照
     *
     * @param view
     */
    public void photo(View view) {
        //申请照相机权限
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA)
                    != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.CAMERA},
                        CAMERA_CODE);
            } else {
                getPictureFromCamera();
            }
        } else {
            getPictureFromCamera();
        }
    }

    /**
     * 单步撤销
     *
     * @param view
     */
    public void one(View view) {
        int size = shapes.size();
        if (size > 0) {
            shapes.remove(size - 1);
            drawGuiji();
        }
    }

    /**
     * 全部撤销
     *
     * @param view
     */
    public void all(View view) {
        shapes.clear();
        drawGuiji();
    }

    /**
     * 保存编辑图片
     *
     * @param view
     */
    public void save(View view) {
        saveBitmap(copyPic, getNewFileName());
    }

    /**
     * 画箭头
     *
     * @param sx
     * @param sy
     * @param ex
     * @param ey
     * @param paint
     */
    private void drawArrow(float sx, float sy, float ex, float ey, int width, Paint paint) {
        int size = 5;
        int count = 20;
        switch (width) {
            case 0:
                size = 5;
                count = 20;
                break;
            case 5:
                size = 8;
                count = 30;
                break;
            case 10:
                size = 11;
                count = 40;
                break;
        }
        float x = ex - sx;
        float y = ey - sy;
        double d = x * x + y * y;
        double r = Math.sqrt(d);
        float zx = (float) (ex - (count * x / r));
        float zy = (float) (ey - (count * y / r));
        float xz = zx - sx;
        float yz = zy - sy;
        double zd = xz * xz + yz * yz;
        double zr = Math.sqrt(zd);
        Path triangle = new Path();
        triangle.moveTo(sx, sy);
        triangle.lineTo((float) (zx + size * yz / zr), (float) (zy - size * xz / zr));
        triangle.lineTo((float) (zx + size * 2 * yz / zr), (float) (zy - size * 2 * xz / zr));
        triangle.lineTo(ex, ey);
        triangle.lineTo((float) (zx - size * 2 * yz / zr), (float) (zy + size * 2 * xz / zr));
        triangle.lineTo((float) (zx - size * yz / zr), (float) (zy + size * xz / zr));
        triangle.close();
        canvas.drawPath(triangle, paint);
    }

    /* 从相册中获取照片 */
    private void getPictureFromPhoto() {
        Intent openphotoIntent = new Intent(Intent.ACTION_PICK,
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(openphotoIntent, PHOTO_PICKED_WITH_DATA);
    }

    /* 从相机中获取照片 */
    private void getPictureFromCamera() {
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");

        tempPhotoPath = filePath + getNewFileName()
                + ".png";

        mCurrentPhotoFile = new File(tempPhotoPath);

        if (!mCurrentPhotoFile.exists()) {
            try {
                mCurrentPhotoFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                Uri.fromFile(mCurrentPhotoFile));
        startActivityForResult(intent, CAMERA_WITH_DATA);
    }

    /**
     * 根据时间戳生成文件名
     *
     * @return
     */
    public static String getNewFileName() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        Date curDate = new Date(System.currentTimeMillis());
        return formatter.format(curDate);
    }

    /**
     * 将生成的图片保存到内存中
     *
     * @param bitmap
     * @param name
     * @return
     */
    public String saveBitmap(Bitmap bitmap, String name) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            File dir = new File(filePath);
            if (!dir.exists())
                dir.mkdir();
            File file = new File(filePath + name + ".jpg");
            FileOutputStream out;
            try {
                out = new FileOutputStream(file);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out)) {
                    out.flush();
                    out.close();
                }
                return file.getAbsolutePath();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    /**
     * 根据路径获取图片并且压缩，适应view
     *
     * @param filePath    图片路径
     * @param contentView 适应的view
     * @return Bitmap 压缩后的图片
     */
    public Bitmap compressionFiller(String filePath, View contentView) {
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        Bitmap bitmap = BitmapFactory.decodeFile(filePath, opt);
        int layoutHeight = contentView.getHeight();
        float scale = 0f;
        int bitmapHeight = bitmap.getHeight();
        int bitmapWidth = bitmap.getWidth();
        scale = bitmapHeight > bitmapWidth
                ? layoutHeight / (bitmapHeight * 1f)
                : screenWidth / (bitmapWidth * 1f);
        Bitmap resizeBmp;
        if (scale != 0) {
            int bitmapheight = bitmap.getHeight();
            int bitmapwidth = bitmap.getWidth();
            Matrix matrix = new Matrix();
            matrix.postScale(scale, scale); // 长和宽放大缩小的比例
            resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmapwidth,
                    bitmapheight, matrix, true);
        } else {
            resizeBmp = bitmap;
        }
        return resizeBmp;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CAMERA_WITH_DATA://拍照
                photoPath = tempPhotoPath;
                break;
            case PHOTO_PICKED_WITH_DATA://相册
                Uri selectedImage = data.getData();
                photoPath = getImageAbsolutePath(MainTestPictureActivity.this, selectedImage);
                break;
        }
        shapes.clear();//轨迹清空
        Bitmap bitmap = compressionFiller(photoPath, ll);//图片缩放
        camera_path = saveBitmap(bitmap, "saveTemp");
        drawPic();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // 调用相机
                getPictureFromCamera();
            } else {
                // 没有权限
                Toast.makeText(MainTestPictureActivity.this, "没有权限", Toast.LENGTH_SHORT).show();
            }
        }

        if (requestCode == SD_CODE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                // 调用相册
                getPictureFromPhoto();
            } else {
                // 没有权限
                Toast.makeText(MainTestPictureActivity.this, "没有权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param imageUri
     * @return
     */
    @TargetApi(19)
    public String getImageAbsolutePath(Context context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        } else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    /**
     * 4.4以前
     *
     * @param context
     * @param uri
     * @param selection
     * @param selectionArgs
     * @return
     */
    public String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}

