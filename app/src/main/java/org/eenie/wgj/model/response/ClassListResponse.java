package org.eenie.wgj.model.response;

import java.util.List;

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
    private int persons;
    private String date;
    private List<ServiceBean> services;

    public ClassListResponse(String date, List<ServiceBean> services) {
        this.date = date;
        this.services = services;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ServiceBean> getServices() {
        return services;
    }

    public void setServices(List<ServiceBean> services) {
        this.services = services;
    }

    public static class ServiceBean{
        private int id;
        private int persons;

        public ServiceBean(int id, int persons) {
            this.id = id;
            this.persons = persons;
        }

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
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }

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
