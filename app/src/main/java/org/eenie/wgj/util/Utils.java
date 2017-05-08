package org.eenie.wgj.util;

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.os.Environment;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.util.Base64;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.matches;

/**
 * Utilities
 */
public class Utils {

    public static String countDown(long millis) {
        long time = millis / 1000L;

        int minute = (int) (time / 60);
        int second = (int) (time % 60);
        if (second >= 10) {
            return String.format("%s:%s", minute, second);
        } else {
            return String.format("%s:0%s", minute, second);
        }
    }

    /**
     * 获取两点间距离
     *
     * @return 两点间距离
     */
    public static double getDistance(double lat1, double lon1, double lat2, double lon2) {
        float[] result = new float[1];

        Location.distanceBetween(lat1, lon1, lat2, lon2, result);

        return result[0];
    }

    // 完整的判断中文汉字和符号
    public static boolean isChineseInput(String strName) {
        char[] ch = strName.toCharArray();
        for (char c : ch) {
            if (isChinese(c)) {
                return true;
            }
        }
        return false;
    }

    // 根据Unicode编码完美的判断中文汉字和符号
    private static boolean isChinese(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        return ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION;
    }

    /**
     * Close closable
     *
     * @param closeable closable to be close
     */
    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void close(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }
    }

    //  手机号正则表达式

    public static boolean isMobile(String phone) {
        Pattern p;
        Matcher m;
        boolean b;
        p = Pattern.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[3|6|7|9])|(18[0-9]))\\d{8}$");
        m = p.matcher(phone);
        b = m.matches();
        return b;
    }

    /*** 验证电话号码
     * @return 如果是符合格式的字符串, 返回 <b>true </b>,否则为 <b>false </b>
     */
    public static boolean IsTelephone(String str) {
        String regex = "^0(10|2[0-5789]-|\\d{3})-?\\d{7,8}$";
        return matches(regex, str);
    }

    //根据boole值设置密码是否显示
    public static void setShowHide(CheckBox checkBox, EditText editText) {
        if (checkBox.isChecked()) {
            editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
        }
        setSelection(editText);
    }

    //设置光标在尾部位置
    public static void setSelection(EditText editText) {
        Editable etext = editText.getText();
        Selection.setSelection(etext, etext.length());
    }


    public static File base64ToFile(String base64, Context context) throws IOException {
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        FileOutputStream fos = new FileOutputStream(image);
        BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fos);
        bufferedOutputStream.write(decodedString);
        bufferedOutputStream.close();
        return image;
    }
//拼接字符串
public static String getStr(List<String> strList) {
    String resultStr = "";
    if (strList != null && strList.size() > 0) {
        for (int i = 0; i < strList.size(); i++) {
            resultStr = resultStr  + strList.get(i)  + ',';
        }
        resultStr = resultStr.substring(0, resultStr.length() - 1);
    }
    return resultStr;
}


    /**
     * 摘
     *
     * @return MD5值
     */
    public static final String md5(final String s) {
        final String MD5 = "MD5";
        try {
            // Create MD5 Hash
            MessageDigest digest = MessageDigest.getInstance(MD5);
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (byte aMessageDigest : messageDigest) {
                String h = Integer.toHexString(0xFF & aMessageDigest);
                while (h.length() < 2) h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

}
