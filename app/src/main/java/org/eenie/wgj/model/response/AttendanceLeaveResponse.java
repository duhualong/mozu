package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/23 at 9:22
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceLeaveResponse  {

    /**
     * id : 68
     * user_id : 25
     * created_at : 2017-06-15 09:26:19
     * name : 范宝王申
     */

    private int id;
    private int user_id;
    private String created_at;
    private String name;

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
