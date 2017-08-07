package org.eenie.wgj.model.response.sortclass;

/**
 * Created by Eenie on 2017/6/7 at 15:06
 * Email: 472279981@qq.com
 * Des:
 */

public class ArrangeProjectService {

    /**
     * service_id : 132
     * servicesname : 日班
     * time : 10.5
     * starttime : 09:00:00
     * endtime : 19:30:00
     * service_people : 3
     */

    private int service_id;
    private String servicesname;
    private double time;
    private String starttime;
    private String endtime;
    private String service_people;
    private int persons;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public String getServicesname() {
        return servicesname;
    }

    public void setServicesname(String servicesname) {
        this.servicesname = servicesname;
    }

    public double getTime() {
        return time;
    }

    public void setTime(double time) {
        this.time = time;
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

    public String getService_people() {
        return service_people;
    }

    public void setService_people(String service_people) {
        this.service_people = service_people;
    }
}
