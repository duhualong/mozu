package org.eenie.wgj.model.response.meeting;

/**
 * Created by Eenie on 2017/7/11 at 18:31
 * Email: 472279981@qq.com
 * Des:
 */

public class UnStartMeeting {

    /**
     * id : 49
     * start : 2017-07-11 19:46
     * end : 2017-07-11 20:46
     * name : MingCheng
     * username : 范宝珅
     * id_card_head_image : /images/user/20170419/20170419134301YC316154848.jpg
     * time_type : 2
     * permissions : 2
     * state : 未开始
     */

    private int id;
    private String start;
    private String end;
    private String name;
    private String username;
    private String id_card_head_image;
    private int time_type;
    private int permissions;
    private String state;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public int getTime_type() {
        return time_type;
    }

    public void setTime_type(int time_type) {
        this.time_type = time_type;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
