package org.eenie.wgj.model.response.meeting;

import java.util.List;

/**
 * Created by Eenie on 2017/7/10 at 20:18
 * Email: 472279981@qq.com
 * Des:
 */

public class AddMeetingRequest {


    private String end;
    private String start;
    private String address;
    private String meeting_agenda;
    private int time_type;
    private int host;
    private int recorder;
    private String meeting_purpose;
    private String name;
    private List<Integer> userid;
    private int meeting_room_id;
    private String detail;

    public AddMeetingRequest(String end, String start, String name, int meeting_room_id, String detail) {
        this.end = end;
        this.start = start;
        this.name = name;
        this.meeting_room_id = meeting_room_id;
        this.detail = detail;
    }

    public AddMeetingRequest(String end, String start, String address, String meeting_agenda,
                             int time_type, int host, int recorder, String meeting_purpose,
                             String name, List<Integer> userid) {
        this.end = end;
        this.start = start;
        this.address = address;
        this.meeting_agenda = meeting_agenda;
        this.time_type = time_type;
        this.host = host;
        this.recorder = recorder;
        this.meeting_purpose = meeting_purpose;
        this.name = name;
        this.userid = userid;
    }

    public int getMeeting_room_id() {
        return meeting_room_id;
    }

    public void setMeeting_room_id(int meeting_room_id) {
        this.meeting_room_id = meeting_room_id;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMeeting_agenda() {
        return meeting_agenda;
    }

    public void setMeeting_agenda(String meeting_agenda) {
        this.meeting_agenda = meeting_agenda;
    }

    public int getTime_type() {
        return time_type;
    }

    public void setTime_type(int time_type) {
        this.time_type = time_type;
    }

    public int getHost() {
        return host;
    }

    public void setHost(int host) {
        this.host = host;
    }

    public int getRecorder() {
        return recorder;
    }

    public void setRecorder(int recorder) {
        this.recorder = recorder;
    }

    public String getMeeting_purpose() {
        return meeting_purpose;
    }

    public void setMeeting_purpose(String meeting_purpose) {
        this.meeting_purpose = meeting_purpose;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getUserid() {
        return userid;
    }

    public void setUserid(List<Integer> userid) {
        this.userid = userid;
    }
}
