package org.eenie.wgj.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.CursorLoader;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

/**
 * Image Process Helper Utilities.
 * Created by Zac on 2016/5/24.
 */
public class ImageUtils {

  /**
   * Get file real path
   * @param context context
   * @param uri file uri
   * @return path with given uri
   */
  public static String getRealPath(Context context, Uri uri) {
    String scheme = uri.getScheme();
    String imagePath = null;
    switch (scheme) {
      case ContentResolver.SCHEME_FILE:
        imagePath = uri.getPath();
        break;
      case ContentResolver.SCHEME_CONTENT:
        String[] projection = { MediaStore.MediaColumns.DATA };
        CursorLoader cursorLoader = new CursorLoader(context, uri, projection, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();
        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        imagePath = cursor.getString(columnIndex);
        break;
    }
    return imagePath;
  }


  /**
   * Get bitmap from photo uri load in background
   *
   * @param uri photo uri
   * @param imageHolder image file place
   * @return photo bitmap
   */
//  public static Bitmap getScaledBitmap(Context context, Uri uri, ImageView imageHolder) {
//    String imagePath = getRealPath(context, uri);
//    return getScaledBitmap(imagePath, imageHolder);
//  }

  public static Bitmap getScaledBitmap(Context context, Uri uri, ImageView imageHolder) {
    String imagePath = getRealPath(context, uri);
    return getScaledBitmap(imagePath, imageHolder);
  }

  public static Bitmap getScaledBitmap(String path, ImageView imageHolder) {
    Bitmap bitmap;
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(path, options);
    // Determine how much to scale down the image
    options.inSampleSize = Math.min(options.outWidth / imageHolder.getWidth(),
            options.outHeight / imageHolder.getHeight());
    // Decode the image file into a Bitmap sized to fill the View;
    options.inJustDecodeBounds = false;
    bitmap = BitmapFactory.decodeFile(path, options);
    return bitmap;
  }

  /**
   * Get a scaled bitmap
   *
   * @param path image file path
   * @return target size scaled bitmap
//   */
//  public static Bitmap getScaledBitmap(String path, ImageView imageHolder) {
//    Bitmap bitmap;
//    BitmapFactory.Options options = new BitmapFactory.Options();
//    options.inJustDecodeBounds = true;
//    BitmapFactory.decodeFile(path, options);
//    // Determine how much to scale down the image
//    options.inSampleSize = Math.min(options.outWidth / imageHolder.getWidth(),
//        options.outHeight / imageHolder.getHeight());
//    // Decode the image file into a Bitmap sized to fill the View;
//    options.inJustDecodeBounds = false;
//    bitmap = BitmapFactory.decodeFile(path, options);
//    return bitmap;
//  }

  /**
   * Get a scaled bitmap from photo file.
   *
   * @param path photo file path
   * @param destWidth destine width
   * @param destHeight destine height
   * @return scaled bitmap.
   */
  public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
    // Read in the dimensions of the image or disk
    BitmapFactory.Options options = new BitmapFactory.Options();
    options.inJustDecodeBounds = true;
    BitmapFactory.decodeFile(path, options);
    options.inSampleSize = calculateInSampleSize(options, destWidth, destHeight);
    // Read in and create final bitmap
    return BitmapFactory.decodeFile(path, options);
  }

  /**
   * Calculate the Bitmap in sample size by Google Inc.
   *
   * @param options bitmapFactory options
   * @param reqWidth request width
   * @param reqHeight request height
   * @return in sample size.
   */
  public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth,
                                          int reqHeight) {
    int height = options.outHeight;
    int width = options.outWidth;
    int inSampleSize = 1;
    if (height > reqHeight || width > reqWidth) {

      final int halfHeight = height / 2;
      final int halfWidth = width / 2;

      // Calculate the largest inSampleSize value that is a power of 2 and keeps both
      // height and width larger than the requested height and width.
      while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
        inSampleSize *= 2;
      }
    }
    return inSampleSize;
  }

  /**
   * Generate barcode bitmap
   * @return barcode bitmap
   */
  public static Bitmap getBarcodeBitmap(String str, int width, int height) throws WriterException {
    BitMatrix result;
    try {
      result = new MultiFormatWriter().encode(str,
          BarcodeFormat.QR_CODE, width, height, null);
    } catch (IllegalArgumentException iae) {
      // Unsupported format
      return null;
    }
    int w = result.getWidth();
    int h = result.getHeight();
    int[] pixels = new int[w * h];
    for (int y = 0; y < h; y++) {
      int offset = y * w;
      for (int x = 0; x < w; x++) {
        pixels[offset + x] = result.get(x, y) ? Color.BLACK : Color.WHITE;
      }
    }
    Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
    bitmap.setPixels(pixels, 0, width, 0, 0, w, h);
    return bitmap;
  }

  /**
   * Pixel transfer to Dip
   *
   * @param px pixel
   * @return dip
   */
  public static float pxToDp(float px) {
    float densityDpi = Resources.getSystem().getDisplayMetrics().densityDpi;
    return px / (densityDpi / 160f);
  }

  /**
   * Dip transfer to Pixel
   *
   * @param dp dip
   * @return pixel
   */
  public static int dpToPx(int dp) {
    float density = Resources.getSystem().getDisplayMetrics().density;
    return Math.round(dp * density);
  }

}
