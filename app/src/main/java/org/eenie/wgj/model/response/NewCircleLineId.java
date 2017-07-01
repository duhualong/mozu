package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/28 at 14:33
 * Email: 472279981@qq.com
 * Des:
 */

public class NewCircleLineId {

    /**
     * user_id : 51
     * project_id : 3
     * inspectionday_id : 94
     * updated_at : 2017-06-16 15:13:37
     * created_at : 2017-06-16 15:13:37
     * id : 4
     */

    private int user_id;
    private int project_id;
    private int inspectionday_id;
    private String updated_at;
    private String created_at;
    private int id;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public int getInspectionday_id() {
        return inspectionday_id;
    }

    public void setInspectionday_id(int inspectionday_id) {
        this.inspectionday_id = inspectionday_id;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
