package org.eenie.wgj.model.response.attendancestatistic;

import java.util.List;

/**
 * Created by Eenie on 2017/7/18 at 18:33
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceMonthItemResponse {

    /**
     * month_integrated : [{"id":109,"name":"裴铁军","id_card_head_image":"/images/user/20170717/20170717204943YC907230565.jpg","rank":1,"integrated":1},{"id":107,"name":"范宝珅","id_card_head_image":"/images/user/20170717/20170717161933YC2015947669.jpg","rank":0,"integrated":0},{"id":108,"name":"何潇潇","id_card_head_image":"/images/user/20170717/20170717165955YC1092276156.jpg","rank":0,"integrated":0}]
     * month_rank : [{"id":109,"name":"裴铁军","id_card_head_image":"/images/user/20170717/20170717204943YC907230565.jpg","rank":1,"integrated":1},{"id":107,"name":"范宝珅","id_card_head_image":"/images/user/20170717/20170717161933YC2015947669.jpg","rank":0,"integrated":0},{"id":108,"name":"何潇潇","id_card_head_image":"/images/user/20170717/20170717165955YC1092276156.jpg","rank":0,"integrated":0}]
     * month_refuel : [{"id":95,"name":"郑文杰","id_card_head_image":null,"rank":0,"refuel":2,"integrated":-2},{"id":104,"name":"王燕萍","id_card_head_image":null,"rank":0,"refuel":2,"integrated":-2},{"id":110,"name":"李强","id_card_head_image":null,"rank":0,"refuel":2,"integrated":-2}]
     * late : 2
     * early : 1
     * go_out : 2
     * leave : 0
     * absenteeism : 4
     * overtime : 0
     * practice : 0
     * seconded : 0
     * new_employees : 17
     * outgoing_employee : 4
     * turnover_rate : 23.53
     */

    private int late;
    private int early;
    private int go_out;
    private int leave;
    private int absenteeism;
    private int overtime;
    private int practice;
    private int seconded;
    private int new_employees;
    private int outgoing_employee;
    private double turnover_rate;
    private List<MonthIntegratedBean> month_integrated;
    private List<MonthRankBean> month_rank;
    private List<MonthRefuelBean> month_refuel;

    public int getLate() {
        return late;
    }

    public void setLate(int late) {
        this.late = late;
    }

    public int getEarly() {
        return early;
    }

    public void setEarly(int early) {
        this.early = early;
    }

    public int getGo_out() {
        return go_out;
    }

    public void setGo_out(int go_out) {
        this.go_out = go_out;
    }

    public int getLeave() {
        return leave;
    }

    public void setLeave(int leave) {
        this.leave = leave;
    }

    public int getAbsenteeism() {
        return absenteeism;
    }

    public void setAbsenteeism(int absenteeism) {
        this.absenteeism = absenteeism;
    }

    public int getOvertime() {
        return overtime;
    }

    public void setOvertime(int overtime) {
        this.overtime = overtime;
    }

    public int getPractice() {
        return practice;
    }

    public void setPractice(int practice) {
        this.practice = practice;
    }

    public int getSeconded() {
        return seconded;
    }

    public void setSeconded(int seconded) {
        this.seconded = seconded;
    }

    public int getNew_employees() {
        return new_employees;
    }

    public void setNew_employees(int new_employees) {
        this.new_employees = new_employees;
    }

    public int getOutgoing_employee() {
        return outgoing_employee;
    }

    public void setOutgoing_employee(int outgoing_employee) {
        this.outgoing_employee = outgoing_employee;
    }

    public double getTurnover_rate() {
        return turnover_rate;
    }

    public void setTurnover_rate(double turnover_rate) {
        this.turnover_rate = turnover_rate;
    }

    public List<MonthIntegratedBean> getMonth_integrated() {
        return month_integrated;
    }

    public void setMonth_integrated(List<MonthIntegratedBean> month_integrated) {
        this.month_integrated = month_integrated;
    }

    public List<MonthRankBean> getMonth_rank() {
        return month_rank;
    }

    public void setMonth_rank(List<MonthRankBean> month_rank) {
        this.month_rank = month_rank;
    }

    public List<MonthRefuelBean> getMonth_refuel() {
        return month_refuel;
    }

    public void setMonth_refuel(List<MonthRefuelBean> month_refuel) {
        this.month_refuel = month_refuel;
    }

    public static class MonthIntegratedBean {
        /**
         * id : 109
         * name : 裴铁军
         * id_card_head_image : /images/user/20170717/20170717204943YC907230565.jpg
         * rank : 1
         * integrated : 1
         */

        private int id;
        private String name;
        private String id_card_head_image;
        private int rank;
        private int integrated;

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

        public String getId_card_head_image() {
            return id_card_head_image;
        }

        public void setId_card_head_image(String id_card_head_image) {
            this.id_card_head_image = id_card_head_image;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getIntegrated() {
            return integrated;
        }

        public void setIntegrated(int integrated) {
            this.integrated = integrated;
        }
    }

    public static class MonthRankBean {
        /**
         * id : 109
         * name : 裴铁军
         * id_card_head_image : /images/user/20170717/20170717204943YC907230565.jpg
         * rank : 1
         * integrated : 1
         */

        private int id;
        private String name;
        private String id_card_head_image;
        private int rank;
        private int integrated;

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

        public String getId_card_head_image() {
            return id_card_head_image;
        }

        public void setId_card_head_image(String id_card_head_image) {
            this.id_card_head_image = id_card_head_image;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getIntegrated() {
            return integrated;
        }

        public void setIntegrated(int integrated) {
            this.integrated = integrated;
        }
    }

    public static class MonthRefuelBean {
        /**
         * id : 95
         * name : 郑文杰
         * id_card_head_image : null
         * rank : 0
         * refuel : 2
         * integrated : -2
         */

        private int id;
        private String name;
        private String id_card_head_image;
        private int rank;
        private int refuel;
        private int integrated;

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

        public String getId_card_head_image() {
            return id_card_head_image;
        }

        public void setId_card_head_image(String id_card_head_image) {
            this.id_card_head_image = id_card_head_image;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public int getRefuel() {
            return refuel;
        }

        public void setRefuel(int refuel) {
            this.refuel = refuel;
        }

        public int getIntegrated() {
            return integrated;
        }

        public void setIntegrated(int integrated) {
            this.integrated = integrated;
        }
    }
}
