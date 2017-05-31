package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/5/26 at 9:18
 * Email: 472279981@qq.com
 * Des:
 */

public class AddRoundWay {
    private String projectid;
    private String name;
    private String difference;
    private int id;

    public AddRoundWay(String projectid, String name, String difference) {
        this.projectid = projectid;
        this.name = name;
        this.difference = difference;
    }

    public AddRoundWay(String projectid, String name, String difference, int id) {
        this.projectid = projectid;
        this.name = name;
        this.difference = difference;
        this.id = id;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDifference() {
        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
