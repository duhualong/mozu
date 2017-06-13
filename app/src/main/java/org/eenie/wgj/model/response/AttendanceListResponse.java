package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/13 at 14:13
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceListResponse {
    /**
     * day : 2017-06-09
     * checkin : {"type":3,"address":"sdafdsaf","time":"09:43","attendance":"迟到"}
     * signback : {"type":1,"address":"上海市黄浦区半淞园路街道苗江路848号","time":"18:20","attendance":"早退"}
     * service : {"id":132,"servicesname":"日班","starttime":"09:00:00","endtime":"19:30:00","time":10.5}
     */

    private String day;
    private CheckinBean checkin;
    private SignbackBean signback;
    private ServiceBean service;

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public CheckinBean getCheckin() {
        return checkin;
    }

    public void setCheckin(CheckinBean checkin) {
        this.checkin = checkin;
    }

    public SignbackBean getSignback() {
        return signback;
    }

    public void setSignback(SignbackBean signback) {
        this.signback = signback;
    }

    public ServiceBean getService() {
        return service;
    }

    public void setService(ServiceBean service) {
        this.service = service;
    }

    public static class CheckinBean {
        /**
         * type : 3
         * address : sdafdsaf
         * time : 09:43
         * attendance : 迟到
         */
        private int type;
        private String address;
        private String time;
        private String attendance;

        public int getType() {
            return type;
        }
        public void setType(int type) {
            this.type = type;
        }
        public String getAddress() {
            return address;
        }
        public void setAddress(String address) {
            this.address = address;
        }
        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
        public String getAttendance() {
            return attendance;
        }

        public void setAttendance(String attendance) {
            this.attendance = attendance;
        }
    }

    public static class SignbackBean {
        /**
         * type : 1
         * address : 上海市黄浦区半淞园路街道苗江路848号
         * time : 18:20
         * attendance : 早退
         */
        private int type;
        private String address;
        private String time;
        private String attendance;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAttendance() {
            return attendance;
        }

        public void setAttendance(String attendance) {
            this.attendance = attendance;
        }
    }
    public static class ServiceBean {
        /**
         * id : 132
         * servicesname : 日班
         * starttime : 09:00:00
         * endtime : 19:30:00
         * time : 10.5
         */

        private int id;
        private String servicesname;
        private String starttime;
        private String endtime;
        private double time;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getServicesname() {
            return servicesname;
        }

        public void setServicesname(String servicesname) {
            this.servicesname = servicesname;
        }

        public String getStarttime() {
            return starttime;
        }

        public void setStarttime(String starttime) {
            this.starttime = starttime;
        }

        public String getEndtime() {
            return endtime;
        }

        public void setEndtime(String endtime) {
            this.endtime = endtime;
        }

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
        }
    }
}
