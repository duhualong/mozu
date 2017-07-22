package org.eenie.wgj.model.response.attendancestatistic;

/**
 * Created by Eenie on 2017/7/18 at 19:22
 * Email: 472279981@qq.com
 * Des:
 */

public class NewAddPeopleResponse {

    /**
     * id : 114
     * username : 13918256837
     * name : 李景艳
     * created_at : 2017-07-18 09:32:11
     * type : 1
     * channel : 赶集网
     * permissions : 普通员工
     */

    private int id;
    private String username;
    private String name;
    private String created_at;
    private int type;
    private String channel;
    private String permissions;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }
}
