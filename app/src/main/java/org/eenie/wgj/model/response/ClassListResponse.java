package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/5/31 at 17:54
 * Email: 472279981@qq.com
 * Des:
 */

public class ClassListResponse {


    private ServiceBean service;
    private int service_people;
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public ServiceBean getService() {
        return service;
    }

    public void setService(ServiceBean service) {
        this.service = service;
    }

    public int getService_people() {
        return service_people;
    }

    public void setService_people(int service_people) {
        this.service_people = service_people;
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
