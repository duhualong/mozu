package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/6/28 at 9:36
 * Email: 472279981@qq.com
 * Des:
 */

public class StartRoutingRecord {
    private String inspectionday_id;
    private String projectid;

    public StartRoutingRecord(String inspectionday_id, String projectid) {
        this.inspectionday_id = inspectionday_id;
        this.projectid = projectid;
    }
}
