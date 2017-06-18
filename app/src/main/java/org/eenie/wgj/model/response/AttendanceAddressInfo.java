package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/14 at 13:40
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceAddressInfo {

    /**
     * address : 中国航空馆
     * attendance : 23
     * attendancescope : 150
     * longitude : 121.49567
     * latitude : 31.198062
     * area : 上海市
     */

    private String address;
    private String attendance;
    private String attendancescope;
    private String longitude;
    private String latitude;
    private String area;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAttendance() {
        return attendance;
    }

    public void setAttendance(String attendance) {
        this.attendance = attendance;
    }

    public String getAttendancescope() {
        return attendancescope;
    }

    public void setAttendancescope(String attendancescope) {
        this.attendancescope = attendancescope;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }
}
