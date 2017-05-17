package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/5/16 at 16:26
 * Email: 472279981@qq.com
 * Des:
 */

public class ProjectList {

    /**
     * id : 1
     * projectname : 静安嘉里中心
     * companyid : 1
     * created_at : 2016-11-14 14:16:41
     * updated_at : 2017-03-05 17:48:59
     * status : 1
     * project_logo :
     */

    private int id;
    private String projectname;
    private int companyid;
    private String created_at;
    private String updated_at;
    private int status;
    private String project_logo;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getProject_logo() {
        return project_logo;
    }

    public void setProject_logo(String project_logo) {
        this.project_logo = project_logo;
    }
}
