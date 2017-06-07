package org.eenie.wgj.model.response.sortclass;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/7 at 15:00
 * Email: 472279981@qq.com
 * Des:
 */

public class ArrangeService {
    private int id;
    private String servicesname;
    private String starttime;
    private String endtime;
    private String time;
    private ArrayList<ArrangeClassUserResponse> user;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServicesname() {
        return servicesname;
    }

    public void setServicesname(String servicesname) {
        this.servicesname = servicesname;
    }

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public ArrayList<ArrangeClassUserResponse> getUser() {
        return user;
    }

    public void setUser(ArrayList<ArrangeClassUserResponse> user) {
        this.user = user;
    }
}
