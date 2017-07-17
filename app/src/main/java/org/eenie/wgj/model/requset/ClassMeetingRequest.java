package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/5/22 at 11:18
 * Email: 472279981@qq.com
 * Des:
 */

public class ClassMeetingRequest {

    /**
     * projectid : 3
     * servicesname : test
     * starttime : 10:30
     * endtime : 20:20
     * id : 131
     */

    private String projectid;
    private String servicesname;
    private String starttime;
    private String endtime;
    private String id;
    private  int persons;

    //添加
    public ClassMeetingRequest(String projectid, String servicesname, String starttime,
                               String endtime) {
        this.projectid = projectid;
        this.servicesname = servicesname;
        this.starttime = starttime;
        this.endtime = endtime;
    }

    //编辑
    public ClassMeetingRequest(String projectid, String servicesname, String starttime,
                               String endtime, String id,int persons) {
        this.projectid = projectid;
        this.servicesname = servicesname;
        this.starttime = starttime;
        this.endtime = endtime;
        this.id = id;
        this.persons=persons;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
