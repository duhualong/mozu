package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/20 at 18:07
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceLate {
    private String created_at;
    private int id;
    private  String name;
    private int user_id;
    private String late;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getLate() {
        return late;
    }

    public void setLate(String late) {
        this.late = late;
    }
}
