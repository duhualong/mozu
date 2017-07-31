package org.eenie.wgj.ui.attendance;

import android.graphics.Color;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Eenie on 2017/6/13 at 16:21
 * Email: 472279981@qq.com
 * Des:
 */

public class RankItemColorAgent {

    Map<String, Integer> colorMap = new HashMap<>();


    public RankItemColorAgent() {
        colorMap.put("常日班", Color.parseColor("#B0D5FC"));
        colorMap.put("日班", Color.parseColor("#EFEFEF"));
        colorMap.put("休息日", Color.parseColor("#FDC3C2"));
        colorMap.put("请假", Color.parseColor("#EADF76"));
        colorMap.put("夜班", Color.parseColor("#989898"));
        colorMap.put("实习", Color.parseColor("#CFADE4"));
    }


    public int getColorByRankName(String name) {
        return colorMap.get(name) != null ? colorMap.get(name) : Color.parseColor("#BB547D");
    }
}
