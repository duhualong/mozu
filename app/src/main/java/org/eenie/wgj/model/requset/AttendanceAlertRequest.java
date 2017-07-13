package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/7/12 at 8:59
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceAlertRequest {
    private String  start;
    private String end;
    private String time;

    public AttendanceAlertRequest(String time) {
        this.time = time;
    }

    public AttendanceAlertRequest(String start, String end) {
        this.start = start;
        this.end = end;
    }
}
