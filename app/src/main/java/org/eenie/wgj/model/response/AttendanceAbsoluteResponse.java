package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/20 at 18:23
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceAbsoluteResponse  {
    private String day;
    private String endtime;
    private String name;
    private String servicesname;
    private String starttime;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
