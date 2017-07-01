package org.eenie.wgj.model.response;

import java.util.List;

/**
 * Created by Eenie on 2017/6/29 at 18:29
 * Email: 472279981@qq.com
 * Des:
 */

public class RoutingPointLatLngResponse {
    private String user_id;
    private String inspectionday_id;
    private int id;
    private String created_at;
    private List<InfoBean> info;
    public static class InfoBean{
        private int id;
        private double latitude;
        private double longitude;
        private int location_id;
        private String time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
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

        public int getLocation_id() {
            return location_id;
        }

        public void setLocation_id(int location_id) {
            this.location_id = location_id;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getInspectionday_id() {
        return inspectionday_id;
    }

    public void setInspectionday_id(String inspectionday_id) {
        this.inspectionday_id = inspectionday_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }
}
