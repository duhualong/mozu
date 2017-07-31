package org.eenie.wgj.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.location.Location;
import android.os.Environment;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.util.Base64;
import android.widget.CheckBox;
import android.widget.EditText;

import org.eenie.wgj.R;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
    public static int getThemeColor(@NonNull Context context) {
        return getThemeAttrColor(context, R.attr.colorPrimary);
    }
    public static int getThemeAttrColor(@NonNull Context context, @AttrRes int attr) {
        TypedArray a = context.obtainStyledAttributes(null, new int[]{attr});
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
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
            resultStr = resultStr  + strList.get(i)  + '/';
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
    static char[] numArray = {'零','一','二','三','四','五','六','七','八','九'};
    static String[] units = {"","十","百","千","万","十万","百万","千万","亿","十亿","百亿","千亿","万亿" };
    /**
     * 将整数转换成汉字数字
     * @param num 需要转换的数字
     * @return 转换后的汉字
     */
    public static String formatInteger(int num) {
        char[] val = String.valueOf(num).toCharArray();
        int len = val.length;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < len; i++) {
            String m = val[i] + "";
            int n = Integer.valueOf(m);
            boolean isZero = n == 0;
            String unit = units[(len - 1) - i];
            if (isZero) {
                if ('0' == val[i - 1]) {
                    continue;
                } else {
                    sb.append(numArray[n]);
                }
            } else {
                sb.append(numArray[n]);
                sb.append(unit);
            }
        }
        return sb.toString();
    }


    public static Calendar covertTime(String time) {
        Calendar dayc1 = new GregorianCalendar();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date daystart = null;
        try {
            daystart = df.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        dayc1.setTime(daystart);
        return dayc1;
    }


    public static String covertWeek(String date) {

        String week = "";
        switch (covertTime(date).get(Calendar.DAY_OF_WEEK)) {
            case 1:
                week = "周日";
                break;
            case 2:
                week = "周一";
                break;
            case 3:
                week = "周二";
                break;
            case 4:
                week = "周三";
                break;
            case 5:
                week = "周四";
                break;
            case 6:
                week = "周五";
                break;
            case 7:
                week = "周六";
                break;

            default:
                week = "未知";
                break;
        }
        return week;
    }
}
