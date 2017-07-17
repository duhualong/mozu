package org.eenie.wgj.model.response.meeting;

/**
 * Created by Eenie on 2017/7/14 at 14:29
 * Email: 472279981@qq.com
 * Des:
 */

public class AuditMeetingRequest {
    private String check_feedback;
    private int checkstatus;
    //1：通过  3拒绝
    private int id;

    public AuditMeetingRequest(String check_feedback, int checkstatus, int id) {
        this.check_feedback = check_feedback;
        this.checkstatus = checkstatus;
        this.id = id;
    }
}
