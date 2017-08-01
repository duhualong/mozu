package org.eenie.wgj;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.linchaolong.android.imagepicker.ImagePicker;
import com.linchaolong.android.imagepicker.cropper.CropImage;
import com.linchaolong.android.imagepicker.cropper.CropImageView;

import org.eenie.wgj.base.BaseActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.ContentValues.TAG;


/**
 * Created by Eenie on 2017/7/14 at 16:41
 * Email: 472279981@qq.com
 * Des:
 */

public class MainTestPictureOneActivity extends BaseActivity {
    private static final int CAMERA_CODE = 3022;
    private static final int SD_CODE = 3020;
    private ArrayList<Shapes> shapes = new ArrayList<>();
    private ImageView iv;//展示图片
    private Bitmap copyPic;//编辑图片
    private Canvas canvas;//画板
    private Paint paint = new Paint();//画笔
    private Matrix matrix;//矩阵
    private Bitmap srcPic;//原图
    private int color = Color.RED;//画笔颜色
    private int width = 10;//画笔大小
    private int circle = -1;//形状
    /* 用来标识请求照相功能的activity */
    private static final int CAMERA_WITH_DATA = 3023;


    private String photoPath, camera_path, tempPhotoPath;
    private File mCurrentPhotoFile;
    //图片保存路径
    public static final String filePath = Environment.getExternalStorageDirectory() + "/PictureTest/";
    private int screenWidth;
    private LinearLayout ll;
    private ImageView imgJiantou;
    private ImageView imgCircle;
    private ImageView imgRectangle;
    private ImageView imgReRevoke;
    private TextView takeSave;
    private ImageView imgCrop;
    private LinearLayout mLinearLayout;
    private ImagePicker imagePicker = new ImagePicker();


    @Override
    protected int getContentView() {
        return R.layout.activity_photo_edit;
    }

    @Override
    protected void updateUI() {
        ll = (LinearLayout) findViewById(R.id.ll);
        iv = (ImageView) findViewById(R.id.iv);
//        DisplayMetrics metric = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metric);
//        // 屏幕宽度（像素）
//        screenWidth = metric.widthPixels;
        imgJiantou = (ImageView) findViewById(R.id.img_jiantou);
        imgCircle = (ImageView) findViewById(R.id.img_circle);
        imgRectangle = (ImageView) findViewById(R.id.img_rectangle);
        imgReRevoke = (ImageView) findViewById(R.id.img_revoke);
        takeSave = (TextView) findViewById(R.id.take_bt);
        imgCrop = (ImageView) findViewById(R.id.img_crop);
        mLinearLayout = (LinearLayout) findViewById(R.id.line_edit_photo_view);
        imagePicker.setCropImage(true);

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
        paint.reset();
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
                        // iv.setImageBitmap(copyPic);
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
                } else {
                    RectF oval2 = new RectF(sp.startX, sp.startY, sp.endX, sp.endY);
                    canvas.drawOval(oval2, paint);
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


//剪切

    public void crop(View view) {

        startChooser();
    }




    private void startChooser() {
        // 启动图片选择器
        imagePicker.startChooser(this, new ImagePicker.Callback() {
            // 选择图片回调
            @Override public void onPickImage(Uri imageUri) {

            }

            // 裁剪图片回调
            @Override public void onCropImage(Uri imageUri) {
                imgRectangle.setImageURI(imageUri);
//        im
//        draweeView.setImageURI(imageUri);
//        draweeView.getHierarchy().setRoundingParams(RoundingParams.asCircle());



            }

            // 自定义裁剪配置
            @Override public void cropConfig(CropImage.ActivityBuilder builder) {
                builder
                        // 是否启动多点触摸
                        .setMultiTouchEnabled(false)
                        // 设置网格显示模式
                        .setGuidelines(CropImageView.Guidelines.OFF);
                // 圆形/矩形
//            .setCropShape(CropImageView.CropShape.RECTANGLE);
                // 调整裁剪后的图片最终大小
//            .setRequestedSize(960, 540);
                // 宽高比
                //  .setAspectRatio(16, 9);
            }

            // 用户拒绝授权回调
            @Override public void onPermissionDenied(int requestCode, String[] permissions,
                                                     int[] grantResults) {
            }
        });
    }



    /**
     * 圆形
     *
     * @param view
     */
    public void circle(View view) {
        imgCircle.setImageResource(R.mipmap.ic_circle_edit_photo);
        imgReRevoke.setImageResource(R.mipmap.ic_white_revoke);
        imgRectangle.setImageResource(R.mipmap.ic_white_rectangle);
        imgJiantou.setImageResource(R.mipmap.ic_white_jiantou);
        circle = 0;
    }


    /**
     * 矩形
     *
     * @param view
     */
    public void fang(View view) {
        imgRectangle.setImageResource(R.mipmap.ic_rectangle_edit_photo);
        imgCircle.setImageResource(R.mipmap.ic_white_circle);
        imgReRevoke.setImageResource(R.mipmap.ic_white_revoke);
        imgJiantou.setImageResource(R.mipmap.ic_white_jiantou);
        circle = 1;
    }

    /**
     * 箭头
     *
     * @param view
     */
    public void arrow(View view) {
        imgJiantou.setImageResource(R.mipmap.ic_jiantou_edit_photo);
        imgRectangle.setImageResource(R.mipmap.ic_white_rectangle);
        imgCircle.setImageResource(R.mipmap.ic_white_circle);
        imgReRevoke.setImageResource(R.mipmap.ic_white_revoke);
        circle = 2;
    }


    public void back(View view) {
        onBackPressed();
    }

    /**
     * 单步撤销
     *
     * @param view
     */
    public void one(View view) {
        int size = shapes.size();
        if (size > 0) {
            imgReRevoke.setImageResource(R.mipmap.ic_revoke_edit_photo);
            imgJiantou.setImageResource(R.mipmap.ic_white_jiantou);
            imgRectangle.setImageResource(R.mipmap.ic_white_rectangle);
            imgCircle.setImageResource(R.mipmap.ic_white_circle);

            shapes.remove(size - 1);
            drawGuiji();
            circle = -1;
        }
    }

    /**
     * 保存编辑图片
     *
     * @param view
     */
    public void save(View view) {
        if (copyPic != null) {
            Bitmap copyPics = ImageCompressL(copyPic);
            setResult(4,
                    getIntent().putExtra("path", savePic(copyPics)));

            finish();
        } else {
            getPictureFromCamera();

        }

    }

//    public Bitmap compressImage(Bitmap image) {
//
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
//        int options = 100;
//        while (baos.toByteArray().length / 1024 > 300) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
//            baos.reset();//重置baos即清空baos
//            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
//            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
//            options -= 10;//每次都减少10
//        }
//        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
//        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
//        bitmap = drawTextToBitmap(this, bitmap, "摩助\n " +
//                new SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis()));
//        return bitmap;
//    }


    private Bitmap ImageCompressL(Bitmap bitmap) {
        double targetwidth = Math.sqrt(500 * 500);

        if (bitmap.getWidth() > targetwidth || bitmap.getHeight() > targetwidth) {
            // 创建操作图片用的matrix对象
            Matrix matrix = new Matrix();
            // 计算宽高缩放率
            double x = Math.max(targetwidth / bitmap.getWidth(), targetwidth
                    / bitmap.getHeight());


            // 缩放图片动作
            matrix.postScale((float) x, (float) x);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(),
                    bitmap.getHeight(), matrix, true);
            bitmap = drawTextToBitmap(this, bitmap, "摩助 " + "\n" +
                    new SimpleDateFormat("yyyy-MM-dd HH:mm").format(System.currentTimeMillis()));
        }
        return bitmap;
    }

    public String savePic(Bitmap bm) {


        String path = getExternalCacheDir().getAbsolutePath() + "/edit_" +
                System.currentTimeMillis() + "_photo.jpeg";
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


//        mImageUri = createImageUri(this);
//        Intent intent = new Intent();
//        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, mImageUri);//如果不设置EXTRA_OUTPUT getData()  获取的是bitmap数据  是压缩后的
//        startActivityForResult(intent, CAMERA_WITH_DATA);
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
                int angle = getBitmapDegree(photoPath);
                Bitmap bitmap = compressionFiller(photoPath, ll);//图片缩放
                camera_path = saveBitmap(rotaingImageView(angle, bitmap), "saveTemp");
                takeSave.setText("保存");
                mLinearLayout.setVisibility(View.VISIBLE);
                drawPic();
                break;

        }

        imagePicker.onActivityResult(this, requestCode, resultCode, data);


    }


    private int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    public static Bitmap rotaingImageView(int angle, Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

    public Bitmap drawTextToBitmap(Context context, Bitmap bitmap, String gText) {
        Resources resources = context.getResources();
        float scale = resources.getDisplayMetrics().density;
//        bitmap = scaleWithWH(bitmap, 300 * scale, 300 * scale);
        android.graphics.Bitmap.Config bitmapConfig = bitmap.getConfig();
        // set default bitmap config if none
        if (bitmapConfig == null) {
            bitmapConfig = android.graphics.Bitmap.Config.ARGB_8888;
        }
        // resource bitmaps are imutable,
        // so we need to convert it to mutable one
        bitmap = bitmap.copy(bitmapConfig, true);
        Canvas canvas = new Canvas(bitmap);
        // new antialised Paint
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        // text color - #3D3D3D
        paint.setColor(Color.RED);
        paint.setTextSize((int) (12 * scale));
        paint.setDither(true); //获取跟清晰的图像采样
        paint.setFilterBitmap(true);//过滤一些
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = 30;
        int y = 30;
        canvas.drawText(gText, x * scale, y * scale, paint);
//        canvas.restore();//别忘了restore
        return bitmap;
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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
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

