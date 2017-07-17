package org.eenie.wgj.model.response.project;

/**
 * Created by Eenie on 2017/7/17 at 13:59
 * Email: 472279981@qq.com
 * Des:
 */

public class ServiceClassPeople {

    /**
     * id : 213
     * project_id : 43
     * servicesname : 常日班
     * start_time : 09:00:00
     * end_time : 17:30:00
     * persons : 10
     */

    private int id;
    private int project_id;
    private String servicesname;
    private String start_time;
    private String end_time;
    private int persons;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getServicesname() {
        return servicesname;
    }

    public void setServicesname(String servicesname) {
        this.servicesname = servicesname;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getEnd_time() {
        return end_time;
    }

    public void setEnd_time(String end_time) {
        this.end_time = end_time;
    }

    public int getPersons() {
        return persons;
    }

    public void setPersons(int persons) {
        this.persons = persons;
    }
}
