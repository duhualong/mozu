package org.eenie.wgj.util;

import android.content.Context;
import android.support.annotation.StringRes;
import android.widget.Toast;

/**
 * Created by Eenie on 2017/5/8 at 9:35
 * Email: 472279981@qq.com
 * Des:
 */

public class ToastUtil {
    private static Context mContext;

    private static Toast mToast;

    private ToastUtil() {
    }

    public static void register(Context context) {
        mContext = context.getApplicationContext();
        mToast = Toast.makeText(mContext, "", Toast.LENGTH_SHORT);
    }

    public static void showToast(@StringRes int resId) {
        mToast.setText(resId);
        mToast.show();
    }

    public static void showToast(String msg) {
        mToast.setText(msg);
        mToast.show();
    }
}
