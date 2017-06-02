package org.eenie.wgj.model.response;

import java.util.List;

/**
 * Created by Eenie on 2017/5/31 at 16:23
 * Email: 472279981@qq.com
 * Des:
 */

public class DayMonthTime {

    /**
     * id : 1085
     * date : 2017-06-13
     * service : [{"service":{"id":132,"servicesname":"日班","starttime":"09:00:00","endtime":"19:30:00","time":10.5},"service_people":"1"},{"service":{"id":133,"servicesname":"夜班","starttime":"19:00:00","endtime":"08:31:00","time":10.5},"service_people":"1"}]
     */

    private int id;
    private String date;
    private List<ServiceBeanX> service;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<ServiceBeanX> getService() {
        return service;
    }

    public void setService(List<ServiceBeanX> service) {
        this.service = service;
    }

    public static class ServiceBeanX {
        /**
         * service : {"id":132,"servicesname":"日班","starttime":"09:00:00","endtime":"19:30:00","time":10.5}
         * service_people : 1
         */


        private int service_id;
        private String servicesname;
        private String starttime;
        private String endtime;
        private double time;
        private String service_people;

        public int getService_id() {
            return service_id;
        }

        public void setService_id(int service_id) {
            this.service_id = service_id;
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

        public String getService_people() {
            return service_people;
        }

        public void setService_people(String service_people) {
            this.service_people = service_people;
        }
    }
}
