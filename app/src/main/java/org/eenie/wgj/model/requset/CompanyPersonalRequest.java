package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/6/16 at 18:33
 * Email: 472279981@qq.com
 * Des:
 */

public class CompanyPersonalRequest {
    private String companyid;
    private String projectid;
    private String userid;

    public CompanyPersonalRequest(String companyid, String projectid, String userid) {
        this.companyid = companyid;
        this.projectid = projectid;
        this.userid = userid;
    }
    public String getCompanyid() {
        return companyid;
    }

    public void setCompanyid(String companyid) {
        this.companyid = companyid;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
