package org.eenie.wgj.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

/**
 * Created by Eenie on 2017/4/12 at 9:45
 * Email: 472279981@qq.com
 * Des:
 */

public class PinYin4jUtil {
    public static String getFirstAlphabet(String str) {
        if (str != null && !str.equals("")){
            char firstChar = str.charAt(0);
            HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
            format.setCaseType(HanyuPinyinCaseType.UPPERCASE);
            format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
            format.setVCharType(HanyuPinyinVCharType.WITH_V);
            try {
                if (Character.toString(firstChar).matches("[\u4E00-\u9FA5]+")) {
                    return String.valueOf(PinyinHelper.toHanyuPinyinStringArray(firstChar, format)[0].charAt(0));
                }else{
                    return String.valueOf("#");
                }
            } catch (BadHanyuPinyinOutputFormatCombination e) {
                e.printStackTrace();
            }
        }
        return "";
    }
}
