package org.eenie.wgj.model.response.newattendancestatistic;

import java.util.List;

/**
 * Created by Eenie on 2017/7/27 at 10:36
 * Email: 472279981@qq.com
 * Des:
 */

public class UserAttendanceStatisticData {
    public List<SchedualBean> schedul;
    public List<ExceptionlistBean> exception_list;


    public List<SchedualBean> getSchedul() {
        return schedul;
    }

    public void setSchedul(List<SchedualBean> schedul) {
        this.schedul = schedul;
    }

    public List<ExceptionlistBean> getException_list() {
        return exception_list;
    }

    public void setException_list(List<ExceptionlistBean> exception_list) {
        this.exception_list = exception_list;
    }

    public static class SchedualBean {
        public SchedualBean(String day, int absent) {
            this.day = day;
            this.absent = absent;
        }

        /**
         * day : 2017-07-20
         * service_id : 213
         * start_time : 09:00:00
         * workstime : 30600
         * servicesname : 常日班
         * end_time : 17:30:00
         * checkin : {"checkin_id":295,"late":0,"goout":0,"latetime":0,"descr":null,"time":"2017-07-20 08:56:12"}
         * signback : {"signback_id":162,"late":0,"goout":0,"latetime":0,"descr":null,"time":"2017-07-20 18:02:23"}
         * absent : 0
         */

        private String day;
        private int service_id;
        private String start_time;
        private int workstime;
        private String servicesname;
        private String end_time;
        private CheckinBean checkin;
        private SignbackBean signback;
        private int absent;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public int getService_id() {
            return service_id;
        }

        public void setService_id(int service_id) {
            this.service_id = service_id;
        }

        public String getStart_time() {
            return start_time;
        }

        public void setStart_time(String start_time) {
            this.start_time = start_time;
        }

        public int getWorkstime() {
            return workstime;
        }

        public void setWorkstime(int workstime) {
            this.workstime = workstime;
        }

        public String getServicesname() {
            return servicesname;
        }

        public void setServicesname(String servicesname) {
            this.servicesname = servicesname;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
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

        public int getAbsent() {
            return absent;
        }

        public void setAbsent(int absent) {
            this.absent = absent;
        }

        public static class CheckinBean {
            /**
             * checkin_id : 295
             * late : 0
             * goout : 0
             * latetime : 0
             * descr : null
             * time : 2017-07-20 08:56:12
             */

            private int checkin_id;
            private int late;
            private int goout;
            private int latetime;
            private Object descr;
            private String time;

            public int getCheckin_id() {
                return checkin_id;
            }

            public void setCheckin_id(int checkin_id) {
                this.checkin_id = checkin_id;
            }

            public int getLate() {
                return late;
            }

            public void setLate(int late) {
                this.late = late;
            }

            public int getGoout() {
                return goout;
            }

            public void setGoout(int goout) {
                this.goout = goout;
            }

            public int getLatetime() {
                return latetime;
            }

            public void setLatetime(int latetime) {
                this.latetime = latetime;
            }

            public Object getDescr() {
                return descr;
            }

            public void setDescr(Object descr) {
                this.descr = descr;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }

        public static class SignbackBean {
            /**
             * signback_id : 162
             * late : 0
             * goout : 0
             * latetime : 0
             * descr : null
             * time : 2017-07-20 18:02:23
             */

            private int signback_id;
            private int late;
            private int goout;
            private int latetime;
            private String descr;
            private String time;
            private int forget;

            public String getDescr() {
                return descr;
            }

            public void setDescr(String descr) {
                this.descr = descr;
            }

            public int getForget() {
                return forget;
            }

            public void setForget(int forget) {
                this.forget = forget;
            }

            public int getSignback_id() {
                return signback_id;
            }

            public void setSignback_id(int signback_id) {
                this.signback_id = signback_id;
            }

            public int getLate() {
                return late;
            }

            public void setLate(int late) {
                this.late = late;
            }

            public int getGoout() {
                return goout;
            }

            public void setGoout(int goout) {
                this.goout = goout;
            }

            public int getLatetime() {
                return latetime;
            }

            public void setLatetime(int latetime) {
                this.latetime = latetime;
            }


            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }


    public static class ExceptionlistBean {

        /**
         * day : 2017-07-18 09:00:00 - 17:30:00
         * type : 迟到
         * address : 上海市黄浦区半淞园路街道苗江路848号
         * time : 2017-07-18 09:00:07
         */

        private String day;
        private String type;
        private String address;
        private String time;

        public String getDay() {
            return day;
        }

        public void setDay(String day) {
            this.day = day;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
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
    }
}
