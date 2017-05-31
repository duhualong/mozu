package org.eenie.wgj.model.response;

import java.util.List;

/**
 * Created by Eenie on 2017/5/31 at 14:57
 * Email: 472279981@qq.com
 * Des:
 */

public class ProjectTimeTotal {


    /**
     * person : [{"id":6,"username":"18651736251","name":"唐海斌"},{"id":7,"username":"15317058010","name":"何潇潇"},{"id":12,"username":"15071233445","name":"范宝珅"},{"id":19,"username":"13916940450","name":"韩红"}]
     * hours : {"workinghours":871.5,"remaininghours":850.5,"approved":2,"actual":2,"attendance":"30"}
     */

    private HoursBean hours;
    private List<PersonBean> person;

    public HoursBean getHours() {
        return hours;
    }

    public void setHours(HoursBean hours) {
        this.hours = hours;
    }

    public List<PersonBean> getPerson() {
        return person;
    }

    public void setPerson(List<PersonBean> person) {
        this.person = person;
    }

    public static class HoursBean {
        /**
         * workinghours : 871.5
         * remaininghours : 850.5
         * approved : 2
         * actual : 2
         * attendance : 30
         */

        private double workinghours;
        private double remaininghours;
        private int approved;
        private int actual;
        private String attendance;

        public double getWorkinghours() {
            return workinghours;
        }

        public void setWorkinghours(double workinghours) {
            this.workinghours = workinghours;
        }

        public double getRemaininghours() {
            return remaininghours;
        }

        public void setRemaininghours(double remaininghours) {
            this.remaininghours = remaininghours;
        }

        public int getApproved() {
            return approved;
        }

        public void setApproved(int approved) {
            this.approved = approved;
        }

        public int getActual() {
            return actual;
        }

        public void setActual(int actual) {
            this.actual = actual;
        }

        public String getAttendance() {
            return attendance;
        }

        public void setAttendance(String attendance) {
            this.attendance = attendance;
        }
    }

    public static class PersonBean {
        /**
         * id : 6
         * username : 18651736251
         * name : 唐海斌
         */

        private int id;
        private String username;
        private String name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
