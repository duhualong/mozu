package org.eenie.wgj.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Eenie on 2017/7/16 at 17:06
 * Email: 472279981@qq.com
 * Des:
 */

public class PackageUtil {
    public static PackageInfo getPackageInfo(Context context){
        PackageManager pm = context.getPackageManager();
        try {
            return pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return  new PackageInfo();
    }
}
