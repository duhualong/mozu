package org.eenie.wgj.model.response.meeting;

/**
 * Created by Eenie on 2017/7/11 at 13:52
 * Email: 472279981@qq.com
 * Des:
 */

public class MeetingFeedbackResponseList {

    /**
     * id : 33
     * name : 123
     * created_at : 2017-05-05 11:10:20
     * checkstatus : 2
     * username : 范宝珅
     * id_card_head_image : /images/user/20170419/20170419134301YC316154848.jpg
     * checkname : 待审核
     */

    private int id;
    private String name;
    private String created_at;
    private int checkstatus;
    private String username;
    private String id_card_head_image;
    private String checkname;

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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(int checkstatus) {
        this.checkstatus = checkstatus;
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

    public String getCheckname() {
        return checkname;
    }

    public void setCheckname(String checkname) {
        this.checkname = checkname;
    }
}
