package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/2 at 12:51
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceDay {

    /**
     * project_id : 3
     * address : 上海文化广场(4号门)
     * attendance : 21
     * attendancescope : 300.36312866211
     * fingerprinttime : 5
     * contracttime : 15
     * longitude : 121.463143
     * latitude : 31.211470
     * area : 上海市
     */

    private int project_id;
    private String address;
    private String attendance;
    private String attendancescope;
    private String fingerprinttime;
    private String contracttime;
    private String longitude;
    private String latitude;
    private String area;
    private String projectid;

    public AttendanceDay(String address, String attendance, String attendancescope,
                         String fingerprinttime, String contracttime, String longitude,
                         String latitude, String area, String projectid) {
        this.address = address;
        this.attendance = attendance;
        this.attendancescope = attendancescope;
        this.fingerprinttime = fingerprinttime;
        this.contracttime = contracttime;
        this.longitude = longitude;
        this.latitude = latitude;
        this.area = area;
        this.projectid = projectid;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

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

    public String getFingerprinttime() {
        return fingerprinttime;
    }

    public void setFingerprinttime(String fingerprinttime) {
        this.fingerprinttime = fingerprinttime;
    }

    public String getContracttime() {
        return contracttime;
    }

    public void setContracttime(String contracttime) {
        this.contracttime = contracttime;
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
