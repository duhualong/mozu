package org.eenie.wgj.ui.news;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Eenie on 2017/4/13 at 16:06
 * Email: 472279981@qq.com
 * Des:
 */

public class Util {
    private static Toast toast;

    public static void showToast(Context context, String msg){
        if(toast == null){
            toast = Toast.makeText(context, "", Toast.LENGTH_SHORT);
        }

        toast.setText(msg);
        toast.show();
    }
}
