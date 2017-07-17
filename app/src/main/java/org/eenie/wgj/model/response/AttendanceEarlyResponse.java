package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/23 at 9:37
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceEarlyResponse {
    private int id;
    private int user_id;
    private String created_at;
    private String name;
    private String complete_time;
    private String early;

    public String getComplete_time() {
        return complete_time;
    }

    public void setComplete_time(String complete_time) {
        this.complete_time = complete_time;
    }

    public String getEarly() {
        return early;
    }

    public void setEarly(String early) {
        this.early = early;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
