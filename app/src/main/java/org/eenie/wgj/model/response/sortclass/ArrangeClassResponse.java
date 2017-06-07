package org.eenie.wgj.model.response.sortclass;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/7 at 14:58
 * Email: 472279981@qq.com
 * Des:
 */

public class ArrangeClassResponse {
    private String day;
    private ArrayList<ArrangeService> service;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public ArrayList<ArrangeService> getService() {
        return service;
    }

    public void setService(ArrayList<ArrangeService> service) {
        this.service = service;
    }
}
