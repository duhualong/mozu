package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/5/17 at 18:14
 * Email: 472279981@qq.com
 * Des:
 */

public class BuildNewProject {
    private String projectname;
    private String project_logo;
    private int project_id;

    public BuildNewProject(String projectname, String project_logo, int project_id) {
        this.projectname = projectname;
        this.project_logo = project_logo;
        this.project_id = project_id;
    }

    public BuildNewProject(String projectname, String project_logo) {
        this.projectname = projectname;
        this.project_logo = project_logo;
    }

    public BuildNewProject(int project_id) {
        this.project_id = project_id;
    }
}
