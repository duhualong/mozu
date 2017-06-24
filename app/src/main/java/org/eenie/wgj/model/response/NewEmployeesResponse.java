package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/23 at 9:09
 * Email: 472279981@qq.com
 * Des:
 */

public class NewEmployeesResponse {

    /**
     * id : 43
     * name : 杜生龙
     * updated_at : 2017-06-19 21:55:21
     * permissions : 管理人员
     * channel : 58同城
     */

    private int id;
    private String name;
    private String updated_at;
    private String permissions;
    private String channel;

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

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }
}
