package org.eenie.wgj.model.response.meeting;

/**
 * Created by Eenie on 2017/7/12 at 11:46
 * Email: 472279981@qq.com
 * Des:
 */

public class AddMeetingClassRequest {

    /**
     * start : 2017-04-11 16:59
     * end : 2017-04-11 17:59
     * detail : CeShi
     * name : 123
     * meeting_room_id : 1
     */

    private String start;
    private String end;
    private String detail;
    private String name;
    private int meeting_room_id;
    private int department_id;


    public AddMeetingClassRequest(String start, String end, String detail, String name, int meeting_room_id) {
        this.start = start;
        this.end = end;
        this.detail = detail;
        this.name = name;
        this.meeting_room_id = meeting_room_id;

    }

    public int getDepartment_id() {
        return department_id;
    }

    public void setDepartment_id(int department_id) {
        this.department_id = department_id;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getMeeting_room_id() {
        return meeting_room_id;
    }

    public void setMeeting_room_id(int meeting_room_id) {
        this.meeting_room_id = meeting_room_id;
    }
}
