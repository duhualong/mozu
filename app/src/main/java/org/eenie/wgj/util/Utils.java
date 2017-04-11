package org.eenie.wgj.util;

import android.database.Cursor;
import android.location.Location;
import android.text.Editable;
import android.text.InputType;
import android.text.Selection;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.Closeable;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    Pattern p ;
    Matcher m ;
    boolean b ;
    p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
    m = p.matcher(phone);
    b = m.matches();
    return b;
  }

  //根据boole值设置密码是否显示
  public static void setShowHide(CheckBox checkBox, EditText editText){
    if (checkBox.isChecked()){
      editText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
    }else {
      editText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
    }
    setSelection(editText);
  }
  //设置光标在尾部位置
  public static void setSelection(EditText editText){
    Editable etext = editText.getText();
    Selection.setSelection(etext, etext.length());
  }


}
