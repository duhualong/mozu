package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/14 at 11:41
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceAbnormal {

    /**
     * date : 2017-06-13
     * stateDes : 异常
     * checkin : {"service_id":132,"image":"/images/attendance/20170613/20170613090321YC692783375.jpg","late":2,"address":"上海市黄浦区半淞园路街道苗江路850号","status":"迟到"}
     * signback : {"late":2,"status":"没有签退"}
     * service : {"id":132,"servicesname":"日班","starttime":"09:00","endtime":"19:30","time":10.5}
     * stateCode : 1
     */

    private String date;
    private String stateDes;
    private CheckinBean checkin;
    private SignbackBean signback;
    private ServiceBean service;
    private int stateCode;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStateDes() {
        return stateDes;
    }

    public void setStateDes(String stateDes) {
        this.stateDes = stateDes;
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

    public int getStateCode() {
        return stateCode;
    }

    public void setStateCode(int stateCode) {
        this.stateCode = stateCode;
    }

    public static class CheckinBean {
        /**
         * service_id : 132
         * image : /images/attendance/20170613/20170613090321YC692783375.jpg
         * late : 2
         * address : 上海市黄浦区半淞园路街道苗江路850号
         * status : 迟到
         */

        private int service_id;
        private String image;
        private int late;
        private String address;
        private String status;
        private String description;
        private String complete_time;

        public String getComplete_time() {
            return complete_time;
        }

        public void setComplete_time(String complete_time) {
            this.complete_time = complete_time;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public int getService_id() {
            return service_id;
        }

        public void setService_id(int service_id) {
            this.service_id = service_id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getLate() {
            return late;
        }

        public void setLate(int late) {
            this.late = late;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class SignbackBean {
        /**
         * late : 2
         * status : 没有签退
         */

        private int late;
        private String status;
        private String address;
        private String image;
        private String description;
        private String complete_time;

        public String getComplete_time() {
            return complete_time;
        }

        public void setComplete_time(String complete_time) {
            this.complete_time = complete_time;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public int getLate() {
            return late;
        }

        public void setLate(int late) {
            this.late = late;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }

    public static class ServiceBean {
        /**
         * id : 132
         * servicesname : 日班
         * starttime : 09:00
         * endtime : 19:30
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
