package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/23 at 9:44
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceGoOutResponse {

    /**
     * id : 137
     * created_at : 2017-03-17 09:45:22
     * address : 上海市黄浦区斜土路靠近上海药房(鲁班路)
     * description : 测试
     * user : null
     */

    private int id;
    private String created_at;
    private String address;
    private String description;
    private String type;
    private String time;
    private String descr;

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


}
