package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/5/23 at 14:45
 * Email: 472279981@qq.com
 * Des:
 */

public class LocationAddress {

    /**
     * name : 上海文化广场4号门
     * location : {"lat":31.21758,"lng":121.469658}
     * uid : 671c4ebb437c8572018972ea
     * city : 上海市
     * district : 卢湾区
     * business :
     * cityid : 289
     */

    private String name;
    private LocationBean location;
    private String uid;
    private String city;
    private String district;
    private String business;
    private String cityid;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocationBean getLocation() {
        return location;
    }

    public void setLocation(LocationBean location) {
        this.location = location;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public String getCityid() {
        return cityid;
    }

    public void setCityid(String cityid) {
        this.cityid = cityid;
    }

    public static class LocationBean {
        /**
         * lat : 31.21758
         * lng : 121.469658
         */

        private double lat;
        private double lng;

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }
    }
}
