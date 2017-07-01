package org.eenie.wgj.model.requset;

import java.util.List;

/**
 * Created by Eenie on 2017/6/28 at 10:19
 * Email: 472279981@qq.com
 * Des:
 */

public class AddRoutingContent {

    /**
     * situation : 0 异常 1 正常
     * inspectionpointid : 292
     * location_name : 上海市黄浦区苗江路靠近中国航空馆
     * longitude : 121.4961
     * latitude : 31.19855
     * error : {"content":"吧","userid":[6]}
     * completed : 1
     */

    private int situation;
    private int inspectionpointid;
    private String location_name;
    private double longitude;
    private double latitude;
    private ErrorBean error;
    private int completed;

    //正常
    public AddRoutingContent(int situation, int inspectionpointid, String location_name,
                             double longitude, double latitude, int completed) {
        this.situation = situation;
        this.inspectionpointid = inspectionpointid;
        this.location_name = location_name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.completed = completed;
    }

    public AddRoutingContent(int situation, int inspectionpointid, String location_name,
                             double longitude, double latitude, ErrorBean error, int completed) {
        this.situation = situation;
        this.inspectionpointid = inspectionpointid;
        this.location_name = location_name;
        this.longitude = longitude;
        this.latitude = latitude;
        this.error = error;
        this.completed = completed;
    }

    public int getSituation() {
        return situation;
    }

    public void setSituation(int situation) {
        this.situation = situation;
    }

    public int getInspectionpointid() {
        return inspectionpointid;
    }

    public void setInspectionpointid(int inspectionpointid) {
        this.inspectionpointid = inspectionpointid;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public ErrorBean getError() {
        return error;
    }

    public void setError(ErrorBean error) {
        this.error = error;
    }

    public int getCompleted() {
        return completed;
    }

    public void setCompleted(int completed) {
        this.completed = completed;
    }

    public static class ErrorBean {
        /**
         * content : 吧
         * userid : [6]
         */

        private String content;
        private List<Integer> userid;

        public ErrorBean(String content, List<Integer> userid) {
            this.content = content;
            this.userid = userid;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public List<Integer> getUserid() {
            return userid;
        }

        public void setUserid(List<Integer> userid) {
            this.userid = userid;
        }
    }
}
