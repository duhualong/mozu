package org.eenie.wgj.util;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;

/**
 * Android API 23 Permission Process Manager

 */
public class PermissionManager {

  public static boolean checkCameraPermission(Context context) {
    return ActivityCompat.checkSelfPermission(context, Manifest.permission.CAMERA)
        == PackageManager.PERMISSION_GRANTED;
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  public static boolean checkReadExternalStoragePermission(Context context) {
    return ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED;
  }

  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  public static boolean checkWriteExternalStoragePermission(Context context) {
    return ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        == PackageManager.PERMISSION_GRANTED;
  }

  public static boolean checkLocation(Context context) {
    return ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)
        == PackageManager.PERMISSION_GRANTED;
  }

  public static boolean checkPhoneCall(Context context) {
    return ActivityCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE)
        == PackageManager.PERMISSION_GRANTED;
  }

  /**
   * Invoke system camera
   *
   * @param fragment ui fragment
   * @param requestCode request code
   */
  @TargetApi(Build.VERSION_CODES.M) public static void invokeCamera(Fragment fragment,
                                                                    int requestCode) {
    fragment.requestPermissions(new String[] {
        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    }, requestCode);
  }
  @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
  public static void requestCamera(Activity activity, int requestCode) {
    ActivityCompat.requestPermissions(activity, new String[] {
        Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    }, requestCode);
  }

  /**
   * Request location permission
   *
   * @param activity ui which page request location permission
   * @param requestCode request location code.
   */
  public static void requestLocation(Activity activity, int requestCode) {
    ActivityCompat.requestPermissions(activity, new String[] {
        Manifest.permission.ACCESS_FINE_LOCATION
    }, requestCode);
  }

  /**
   * request phone call permission
   * @param fragment fragment ui
   * @param requestCode request code.
   */
  @TargetApi(Build.VERSION_CODES.M) public static void requestPhoneCall(Fragment fragment, int requestCode) {
    fragment.requestPermissions(new String[] { Manifest.permission.CALL_PHONE }, requestCode);
  }
}
