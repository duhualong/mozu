package org.eenie.wgj.model.response;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/5 at 11:34
 * Email: 472279981@qq.com
 * Des:
 */

public class MonthTimeWorkingArrange {

    /**
     * id : 1090
     * date : 2017-06-02
     * service : [{"service_id":132,"servicesname":"日班","time":10.5,"starttime":"09:00:00","endtime":"19:30:00","service_people":"3"}]
     */

    private int id;
    private String date;
    private ArrayList<ServiceBean> service;
    private boolean select;

    public boolean isSelect() {
        return select;
    }

    public void setSelect(boolean select) {
        this.select = select;
    }

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

    public ArrayList<ServiceBean> getService() {
        return service;
    }

    public void setService(ArrayList<ServiceBean> service) {
        this.service = service;
    }

    public static class ServiceBean {
        /**
         * service_id : 132
         * servicesname : 日班
         * time : 10.5
         * starttime : 09:00:00
         * endtime : 19:30:00
         * service_people : 3
         */
        private int id;
        private int service_id;
        private String servicesname;
        private double time;
        private String starttime;
        private String endtime;
        private String service_people;
        private ArrayList<UserBean> user;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public ArrayList<UserBean> getUser() {
            return user;
        }

        public void setUser(ArrayList<UserBean> user) {
            this.user = user;
        }


        public static class UserBean {
            private int user_id;
            private String name;
            private LineRound line;

            public int getUser_id() {
                return user_id;
            }

            public void setUser_id(int user_id) {
                this.user_id = user_id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public LineRound getLine() {
                return line;
            }

            public void setLine(LineRound line) {
                this.line = line;
            }

            public UserBean(int user_id, String name, LineRound line) {
                this.user_id = user_id;
                this.name = name;
                this.line = line;
            }

            public static class LineRound {
                private int id;
                private String name;

                public LineRound(int id, String name) {
                    this.id = id;
                    this.name = name;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
                }
            }


        }

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

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
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

        public String getService_people() {
            return service_people;
        }

        public void setService_people(String service_people) {
            this.service_people = service_people;
        }
    }
}
