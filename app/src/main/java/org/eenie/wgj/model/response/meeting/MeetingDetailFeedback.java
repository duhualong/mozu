package org.eenie.wgj.model.response.meeting;

/**
 * Created by Eenie on 2017/7/11 at 17:11
 * Email: 472279981@qq.com
 * Des:
 */

public class MeetingDetailFeedback {

    /**
     * id : 32
     * username : 范宝珅
     * id_card_head_image : /images/user/20170419/20170419134301YC316154848.jpg
     * created_at : 2017-04-17 15:32:45
     * name : 12312
     * start : 2017-04-17 15:32:00
     * end : 2017-04-17 18:32:00
     * room_name : B203
     * detail : Saga wife
     * checkid : 12
     * check_feedback : Ok
     * checkstatus : 1
     * checkusername : 范宝珅
     * check_image : /images/user/20170419/20170419134301YC316154848.jpg
     * checkname : 批准
     */

    private int id;
    private String username;
    private String id_card_head_image;
    private String created_at;
    private String name;
    private String start;
    private String end;
    private String room_name;
    private String detail;
    private String checkid;
    private String check_feedback;
    private int checkstatus;
    private String checkusername;
    private String check_image;
    private String checkname;

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

    public String getId_card_head_image() {
        return id_card_head_image;
    }

    public void setId_card_head_image(String id_card_head_image) {
        this.id_card_head_image = id_card_head_image;
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

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
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

    public int getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(int checkstatus) {
        this.checkstatus = checkstatus;
    }

    public String getCheckusername() {
        return checkusername;
    }

    public void setCheckusername(String checkusername) {
        this.checkusername = checkusername;
    }

    public String getCheck_image() {
        return check_image;
    }

    public void setCheck_image(String check_image) {
        this.check_image = check_image;
    }

    public String getCheckname() {
        return checkname;
    }

    public void setCheckname(String checkname) {
        this.checkname = checkname;
    }
}
