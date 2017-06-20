package org.eenie.wgj.model.response;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/19 at 13:53
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceStaticsMonth {

    private ArrayList<String> month;
    private ArrayList<String> year;

    public ArrayList<String> getMonth() {
        return month;
    }

    public void setMonth(ArrayList<String> month) {
        this.month = month;
    }

    public ArrayList<String> getYear() {
        return year;
    }

    public void setYear(ArrayList<String> year) {
        this.year = year;
    }
}
