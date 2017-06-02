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

    public ProjectTimeRequest(String date, ArrayList<Integer> serviceid, ArrayList<Integer> servicepeople, String projectid) {
        this.date = date;
        this.serviceid = serviceid;
        this.servicepeople = servicepeople;
        this.projectid = projectid;
    }
}
