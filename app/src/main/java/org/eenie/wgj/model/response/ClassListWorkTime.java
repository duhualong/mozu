package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/4 at 11:18
 * Email: 472279981@qq.com
 * Des:
 */

public class ClassListWorkTime {


    /**
     * service : {"id":132,"servicesname":"日班","starttime":"09:00:00","endtime":"19:30:00","time":10.5}
     * day : 8
     */


    private ServiceBean service;
    private String day;
    private int addDay;

    public int getAddDay() {
        return addDay;
    }

    public void setAddDay(int addDay) {
        this.addDay = addDay;
    }

    public ClassListWorkTime(ServiceBean service, String day) {
        this.service = service;
        this.day = day;
    }

    public ServiceBean getService() {
        return service;
    }

    public void setService(ServiceBean service) {
        this.service = service;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public static class ServiceBean {
        public ServiceBean(int id, String servicesname, String starttime, String endtime, double time) {
            this.id = id;
            this.servicesname = servicesname;
            this.starttime = starttime;
            this.endtime = endtime;
            this.time = time;
        }

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
