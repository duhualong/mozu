package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/5/24 at 11:58
 * Email: 472279981@qq.com
 * Des:
 */

public class PostWorkRequest {
    private int id;
    private String post;
    private String jobassignment;
    private String projectid;

//编辑岗位设置
    public PostWorkRequest(int id, String post, String jobassignment, String projectid) {
        this.id = id;
        this.post = post;
        this.jobassignment = jobassignment;
        this.projectid = projectid;
    }
    //新建岗位设置

    public PostWorkRequest(String post, String jobassignment, String projectid) {
        this.post = post;
        this.jobassignment = jobassignment;
        this.projectid = projectid;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getJobassignment() {
        return jobassignment;
    }

    public void setJobassignment(String jobassignment) {
        this.jobassignment = jobassignment;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }
}
