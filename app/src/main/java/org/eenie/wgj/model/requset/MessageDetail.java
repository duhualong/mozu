package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/5/4 at 16:29
 * Email: 472279981@qq.com
 * Des:
 */

public class MessageDetail {

    /**
     * id : 32
     * name : 12312
     * department_id : 2
     * username : 范宝珅
     * room_name : B203
     * checkstatus : 1
     * start : 2017-04-17 15:32:00
     * end : 2017-04-17 18:32:00
     * detail : Saga wife
     * checkid : 12
     * check_feedback : Ok
     * avatar : /images/user/20170419/20170419134301YC316154848.jpg
     * checkname : 范宝珅
     * check_image : /images/user/20170419/20170419134301YC316154848.jpg
     * checkstatus_name : 批准
     */

    private int id;
    private String name;
    private int department_id;
    private String username;
    private String room_name;
    private int checkstatus;
    private String start;
    private String end;
    private String detail;
    private String checkid;
    private String check_feedback;
    private String avatar;
    private String checkname;
    private String check_image;
    private String checkstatus_name;
    private String operator_avatar;

    public String getOperator_avatar() {
        return operator_avatar;
    }

    public void setOperator_avatar(String operator_avatar) {
        this.operator_avatar = operator_avatar;
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

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public int getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(int checkstatus) {
        this.checkstatus = checkstatus;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getCheckid() {
        return checkid;
    }

    public void setCheckid(String checkid) {
        this.checkid = checkid;
    }

    public String getCheck_feedback() {
        return check_feedback;
    }

    public void setCheck_feedback(String check_feedback) {
        this.check_feedback = check_feedback;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getCheckname() {
        return checkname;
    }

    public void setCheckname(String checkname) {
        this.checkname = checkname;
    }

    public String getCheck_image() {
        return check_image;
    }

    public void setCheck_image(String check_image) {
        this.check_image = check_image;
    }

    public String getCheckstatus_name() {
        return checkstatus_name;
    }

    public void setCheckstatus_name(String checkstatus_name) {
        this.checkstatus_name = checkstatus_name;
    }
}
