package org.eenie.wgj.ui.attendance.sign;

import android.app.DialogFragment;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.eenie.wgj.R;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Eenie on 2017/6/18 at 15:50
 * Email: 472279981@qq.com
 * Des:
 */

public class PictureFragment extends DialogFragment {

    public static final String CROP_PICTURE = "cropPic";
    @BindView(R.id.advance_edit_dialog_title)
    TextView mAdvanceEditDialogTitle;
    @BindView(R.id.crop_picture_img)
    ImageView mCropPictureImg;
    @BindView(R.id.advance_edit_dialog_cancel_button)
    Button mAdvanceEditDialogCancelButton;
    @BindView(R.id.advance_edit_dialog_confirm_button)
    Button mAdvanceEditDialogConfirmButton;
    private Bitmap mCropPicBitmap;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle data = getArguments();
        if (data != null) {
//            mOriginPicBitmap = data.getParcelable(ORIGIN_PICTURE);
            mCropPicBitmap = data.getParcelable(CROP_PICTURE);
            mCropPicBitmap = drawTextToBitmap(getActivity(), mCropPicBitmap,
                    "摩助 " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(System.currentTimeMillis()));
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.picture_layout, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        mCropPictureImg.setImageBitmap(mCropPicBitmap);
    }

    @OnClick({R.id.advance_edit_dialog_cancel_button, R.id.advance_edit_dialog_confirm_button})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.advance_edit_dialog_cancel_button:
                dismiss();
                break;
            case R.id.advance_edit_dialog_confirm_button:

                ((AttendanceTokePhotoActivity) getActivity()).onTakePhoto(ImageCompressL(mCropPicBitmap));
                dismiss();
                break;
        }
    }
    private Bitmap ImageCompressL(Bitmap bitmap) {
        double targetwidth = Math.sqrt(1000.00 * 1000);
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
        }
        return bitmap;
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
        paint.setTextSize((int) (18 * scale));
        paint.setDither(true); //获取跟清晰的图像采样
        paint.setFilterBitmap(true);//过滤一些
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = 30;
        int y = 30;
        canvas.drawText(gText, x * scale, y * scale, paint);
        return bitmap;
    }

    public Bitmap drawTextToBitmap(Context gContext, int gResId, String gText) {
        Resources resources = gContext.getResources();
        float scale = resources.getDisplayMetrics().density;
        Bitmap bitmap = BitmapFactory.decodeResource(resources, gResId);
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
        paint.setTextSize((int) (18 * scale));
        paint.setDither(true); //获取跟清晰的图像采样
        paint.setFilterBitmap(true);//过滤一些
        Rect bounds = new Rect();
        paint.getTextBounds(gText, 0, gText.length(), bounds);
        int x = 30;
        int y = 30;
        canvas.drawText(gText, x * scale, -canvas.getHeight() * scale, paint);
        return bitmap;
    }


    public static Bitmap scaleWithWH(Bitmap src, double w, double h) {
        if (w == 0 || h == 0 || src == null) {
            return src;
        } else {
            // 记录src的宽高
            int width = src.getWidth();
            int height = src.getHeight();
            // 创建一个matrix容器
            Matrix matrix = new Matrix();
            // 计算缩放比例
            float scaleWidth = (float) (w / width);
            float scaleHeight = (float) (h / height);
            // 开始缩放
            matrix.postScale(scaleWidth, scaleHeight);
            // 创建缩放后的图片
            return Bitmap.createBitmap(src, 0, 0, width, height, matrix, true);
        }
    }


}
