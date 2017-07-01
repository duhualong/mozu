package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/27 at 15:40
 * Email: 472279981@qq.com
 * Des:
 */

public class RoutingPointUpload {

    /**
     * inspectionpointid : 292
     * situation : 1
     * location_name : 上海市徐汇区斜土路靠近日晖绿地
     * completed : 1
     * latitude : 31.19659
     * longitude : 121.4606
     */

    private int inspectionpointid;
    private int situation;
    private String location_name;
    private int completed;
    private double latitude;
    private double longitude;

    public int getInspectionpointid() {
        return inspectionpointid;
    }

    public void setInspectionpointid(int inspectionpointid) {
        this.inspectionpointid = inspectionpointid;
    }

    public int getSituation() {
        return situation;
    }

    public void setSituation(int situation) {
        this.situation = situation;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
