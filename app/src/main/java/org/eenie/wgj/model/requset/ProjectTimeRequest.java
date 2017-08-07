package org.eenie.wgj.model.requset;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/1 at 10:51
 * Email: 472279981@qq.com
 * Des:
 */

public class ProjectTimeRequest {
    private String date;
    private ArrayList<Integer> serviceid;
    private ArrayList<Integer> servicepeople;
    private String projectid;
    private String data;

    public ProjectTimeRequest(String projectid, String data) {
        this.projectid = projectid;
        this.data = data;
    }

    public ProjectTimeRequest(String date, ArrayList<Integer> serviceid, ArrayList<Integer> servicepeople, String projectid) {
        this.date = date;
        this.serviceid = serviceid;
        this.servicepeople = servicepeople;
        this.projectid = projectid;
    }
}
