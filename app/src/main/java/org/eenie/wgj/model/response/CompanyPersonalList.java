package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/16 at 17:50
 * Email: 472279981@qq.com
 * Des:
 */

public class CompanyPersonalList {

    /**
     * userid : 3
     * name : 1111
     * project_id : 1
     */

    private int userid;
    private String name;
    private int project_id;

    public int getUserid() {
        return userid;
    }

    public void setUserid(int userid) {
        this.userid = userid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }
}
