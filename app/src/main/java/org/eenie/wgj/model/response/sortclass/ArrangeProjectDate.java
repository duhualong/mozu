package org.eenie.wgj.model.response.sortclass;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/7 at 15:05
 * Email: 472279981@qq.com
 * Des:
 */

public class ArrangeProjectDate {
    private int id;
    private String date;
    private ArrayList<ArrangeProjectService> service;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ArrangeProjectService> getService() {
        return service;
    }

    public void setService(ArrayList<ArrangeProjectService> service) {
        this.service = service;
    }
}
