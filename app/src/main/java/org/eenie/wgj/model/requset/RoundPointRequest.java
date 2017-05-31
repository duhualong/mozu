package org.eenie.wgj.model.requset;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/5/29 at 13:45
 * Email: 472279981@qq.com
 * Des:
 */

public class RoundPointRequest {

    /**
     * projectid : 3
     * inspectiondayid : 78
     * lineid : 19
     * inspections : [{"id":103,"time":"11:00"},{"id":105,"time":"10:00"}]
     */

    private int projectid;
    private int inspectiondayid;
    private int lineid;
    private ArrayList<CycleNumberEdit> inspections;

    public RoundPointRequest(int projectid, int lineid, ArrayList<CycleNumberEdit> inspections) {
        this.projectid = projectid;
        this.lineid = lineid;
        this.inspections = inspections;
    }

    public RoundPointRequest(int projectid, int inspectiondayid, int lineid, ArrayList<CycleNumberEdit> inspections) {
        this.projectid = projectid;
        this.inspectiondayid = inspectiondayid;
        this.lineid = lineid;
        this.inspections = inspections;
    }

    public int getProjectid() {
        return projectid;
    }

    public void setProjectid(int projectid) {
        this.projectid = projectid;
    }

    public int getInspectiondayid() {
        return inspectiondayid;
    }

    public void setInspectiondayid(int inspectiondayid) {
        this.inspectiondayid = inspectiondayid;
    }

    public int getLineid() {
        return lineid;
    }

    public void setLineid(int lineid) {
        this.lineid = lineid;
    }

    public ArrayList<CycleNumberEdit> getInspections() {
        return inspections;
    }

    public void setInspections(ArrayList<CycleNumberEdit> inspections) {
        this.inspections = inspections;
    }

}
