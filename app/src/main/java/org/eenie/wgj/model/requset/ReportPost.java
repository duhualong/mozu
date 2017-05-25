package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/5/25 at 12:15
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportPost {
    private int id;
    private String jetlag;
    private String postsettingid;
    private String projectid;
    private String serviceid;
    private String reportingtime;

    public ReportPost(int id, String jetlag, String postsettingid, String projectid,
                      String serviceid, String reportingtime) {
        this.id = id;
        this.jetlag = jetlag;
        this.postsettingid = postsettingid;
        this.projectid = projectid;
        this.serviceid = serviceid;
        this.reportingtime = reportingtime;
    }

    public ReportPost(String jetlag, String postsettingid, String projectid, String serviceid,
                      String reportingtime) {
        this.jetlag = jetlag;
        this.postsettingid = postsettingid;
        this.projectid = projectid;
        this.serviceid = serviceid;
        this.reportingtime = reportingtime;
    }
}
