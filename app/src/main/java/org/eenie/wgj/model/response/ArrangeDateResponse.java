package org.eenie.wgj.model.response;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/5 at 15:49
 * Email: 472279981@qq.com
 * Des:
 */

public class ArrangeDateResponse {

    /**
     * day : 2017-06-02
     * service : [{"id":132,"servicesname":"日班","starttime":"09:00:00","endtime":"19:30:00","time":10.5,"user":[{"user_id":19,"name":"韩红","line":{"id":17,"name":"领班"}}]}]
     */

    private String day;
    private ArrayList<MonthTimeWorkingArrange.ServiceBean> service;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<MonthTimeWorkingArrange.ServiceBean> getService() {
        return service;
    }

    public void setService(ArrayList<MonthTimeWorkingArrange.ServiceBean> service) {
        this.service = service;
    }


}
