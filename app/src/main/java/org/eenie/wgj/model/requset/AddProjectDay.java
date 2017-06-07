package org.eenie.wgj.model.requset;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/2 at 18:53
 * Email: 472279981@qq.com
 * Des:
 */

public class AddProjectDay {
    private String projectid;
    private ArrayList<Integer> userid;
    private ArrayList<String> serviceid;
    private ArrayList<String> day;
    private String date;

    public AddProjectDay(String projectid, ArrayList<Integer> userid,
                         ArrayList<String> serviceid, ArrayList<String> day, String date) {
        this.projectid = projectid;
        this.userid = userid;
        this.serviceid = serviceid;
        this.day = day;
        this.date = date;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public ArrayList<Integer> getUserid() {
        return userid;
    }

    public void setUserid(ArrayList<Integer> userid) {
        this.userid = userid;
    }

    public ArrayList<String> getServiceid() {
        return serviceid;
    }

    public void setServiceid(ArrayList<String> serviceid) {
        this.serviceid = serviceid;
    }

    public ArrayList<String> getDay() {
        return day;
    }

    public void setDay(ArrayList<String> day) {
        this.day = day;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
