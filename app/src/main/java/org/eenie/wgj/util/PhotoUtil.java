package org.eenie.wgj.util;

import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Photo Process Helper Utilities.
 * Created by Zac on 2016/5/24.
 */
public class PhotoUtil {

  /**
   * Verify if there has
   *
   * @param context system context
   * @param photoFile photo file
   * @return true if can take photo
   */
  public static boolean isCanCapturePhoto(Context context, File photoFile) {
    return photoFile != null
        && new Intent(MediaStore.ACTION_IMAGE_CAPTURE).resolveActivity(context.getPackageManager())
        != null;
  }

  /**
   * Get a photo filename
   *
   * @return a temporary photo file name.
   */
  private static String getPhotoFilename() {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
    String timestamp = sdf.format(new Date());
    return "IMG_" + timestamp;
  }

  /**
   * Create a temporary photo file
   *
   * @return a temporary photo file.
   */
  public static File createImageFile() {
    File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
    try {
      return File.createTempFile(getPhotoFilename(), /* prefix */
          ".png", /* suffix */
          storageDir /* directory */);
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }
}
