package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/23 at 10:12
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendancePracticeResponse {
    private int id;
    private String created_at;
    private String user;
    private String service;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getService() {
        return service;
    }

    public void setService(String service) {
        this.service = service;
    }
}
