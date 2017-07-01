package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/27 at 18:34
 * Email: 472279981@qq.com
 * Des:
 */

public class PointNeedResponse {
   // positions=[{"longitude":121.4961,"time":"2017-06-27 18:36:08","latitude":31.19861}]
    private  double longitude;
    private double latitude;
    private String time;


    public PointNeedResponse(double longitude, double latitude, String time) {
        this.longitude = longitude;
        this.latitude = latitude;
        this.time = time;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


}
