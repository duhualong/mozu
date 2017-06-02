package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/5/31 at 17:54
 * Email: 472279981@qq.com
 * Des:
 */

public class ClassListResponse {


    private boolean checked;
    private int id;
    private String servicesname;
    private String starttime;
    private String endtime;
    private double time;
    private int service_people;


    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

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

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
    }

    public int getService_people() {
        return service_people;
    }

    public void setService_people(int service_people) {
        this.service_people = service_people;
    }
}
