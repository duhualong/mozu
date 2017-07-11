package org.eenie.wgj.model.response.meeting;

import java.util.List;

/**
 * Created by Eenie on 2017/7/11 at 20:23
 * Email: 472279981@qq.com
 * Des:
 */

public class MeetingEndDetail {

    /**
     * id : 48
     * username : 韩红
     * id_card_head_image : /images/user/20170707/20170707191314YC392204109.jpg
     * user_id : 19
     * name : 啦啦啦
     * time_type : 2
     * start : 2017-07-07 15:00
     * end : 2017-07-07 18:00
     * address : 太可怜了
     * host : 易善涛
     * recorder : 杜华龙
     * meeting_purpose : 天天熬夜
     * meeting_agenda : 你可以把经常要输入的内容放在这里轻轻一点，直接输入上屏长句、短语、邮箱、号码任你自定义！你可以把经常要输入的内容放在这里
     * record : 测呵呵
     * checkstatus : 2
     * permissions : 3
     * state : 已结束
     */

    private int id;
    private String username;
    private String id_card_head_image;
    private int user_id;
    private String name;
    private int time_type;
    private String start;
    private String end;
    private String address;
    private String host;
    private String recorder;
    private String meeting_purpose;
    private String meeting_agenda;
    private String record;
    private int checkstatus;
    private int permissions;
    private String state;
    private List<ImageBean> image;


    public static class ImageBean {
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

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

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getTime_type() {
        return time_type;
    }

    public void setTime_type(int time_type) {
        this.time_type = time_type;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getRecorder() {
        return recorder;
    }

    public void setRecorder(String recorder) {
        this.recorder = recorder;
    }

    public String getMeeting_purpose() {
        return meeting_purpose;
    }

    public void setMeeting_purpose(String meeting_purpose) {
        this.meeting_purpose = meeting_purpose;
    }

    public String getMeeting_agenda() {
        return meeting_agenda;
    }

    public void setMeeting_agenda(String meeting_agenda) {
        this.meeting_agenda = meeting_agenda;
    }

    public String getRecord() {
        return record;
    }

    public void setRecord(String record) {
        this.record = record;
    }

    public int getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(int checkstatus) {
        this.checkstatus = checkstatus;
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
