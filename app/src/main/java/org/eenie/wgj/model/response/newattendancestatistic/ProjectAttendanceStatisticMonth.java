package org.eenie.wgj.model.response.newattendancestatistic;

import java.util.List;

/**
 * Created by Eenie on 2017/7/26 at 16:58
 * Email: 472279981@qq.com
 * Des:
 */

public class ProjectAttendanceStatisticMonth {
    private List<String> month_list;
    private List<String>year_list;

    public List<String> getMonth_list() {
        return month_list;
    }

    public void setMonth_list(List<String> month_list) {
        this.month_list = month_list;
    }

    public List<String> getYear_list() {
        return year_list;
    }

    public void setYear_list(List<String> year_list) {
        this.year_list = year_list;
    }
}
