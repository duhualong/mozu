package org.eenie.wgj.model.response;

import java.util.List;

/**
 * Created by Eenie on 2017/6/19 at 18:28
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceMonthItem {

    /**
     * absenteeism : 0
     * early : 0
     * go_out : 0
     * late : 0
     * leave : 0
     * month_integrated : [{"id":6,"id_card_head_image":"/images/user/20170320/20170320124620YC470557938.jpg","integrated":0,"name":"唐海斌","rank":0},{"id":19,"id_card_head_image":"/images/user/20170614/20170614114336YC1961746353.jpg","integrated":0,"name":"韩红","rank":0},{"id":28,"id_card_head_image":"","integrated":0,"name":"杜华龙","rank":0}]
     * month_rank : [{"id":6,"id_card_head_image":"/images/user/20170320/20170320124620YC470557938.jpg","integrated":0,"name":"唐海斌","rank":0},{"id":19,"id_card_head_image":"/images/user/20170614/20170614114336YC1961746353.jpg","integrated":0,"name":"韩红","rank":0},{"id":28,"id_card_head_image":"","integrated":0,"name":"杜华龙","rank":0}]
     * month_refuel : [{"id":6,"id_card_head_image":"/images/user/20170320/20170320124620YC470557938.jpg","name":"唐海斌","rank":0,"refuel":0},{"id":19,"id_card_head_image":"/images/user/20170614/20170614114336YC1961746353.jpg","name":"韩红","rank":0,"refuel":0},{"id":28,"id_card_head_image":"","name":"杜华龙","rank":0,"refuel":0}]
     * new_employees : 0
     * outgoing_employee : 0
     * overtime : 0
     * practice : 0
     * seconded : 0
     * turnover_rate : 0
     */

    private int absenteeism;
    private int early;
    private int go_out;
    private int late;
    private int leave;
    private int new_employees;
    private int outgoing_employee;
    private int overtime;
    private int practice;
    private int seconded;
    private double turnover_rate;
    private List<MonthIntegratedBean> month_integrated;
    private List<MonthRankBean> month_rank;
    private List<MonthRefuelBean> month_refuel;

    public int getAbsenteeism() {
        return absenteeism;
    }

    public void setAbsenteeism(int absenteeism) {
        this.absenteeism = absenteeism;
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

    public int getLate() {
        return late;
    }

    public void setLate(int late) {
        this.late = late;
    }

    public int getLeave() {
        return leave;
    }

    public void setLeave(int leave) {
        this.leave = leave;
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
         * id : 6
         * id_card_head_image : /images/user/20170320/20170320124620YC470557938.jpg
         * integrated : 0
         * name : 唐海斌
         * rank : 0
         */

        private int id;
        private String id_card_head_image;
        private int integrated;
        private String name;
        private int rank;
        private int refuel;

        public int getRefuel() {
            return refuel;
        }

        public void setRefuel(int refuel) {
            this.refuel = refuel;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getId_card_head_image() {
            return id_card_head_image;
        }

        public void setId_card_head_image(String id_card_head_image) {
            this.id_card_head_image = id_card_head_image;
        }

        public int getIntegrated() {
            return integrated;
        }

        public void setIntegrated(int integrated) {
            this.integrated = integrated;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }

    public static class MonthRankBean {
        /**
         * id : 6
         * id_card_head_image : /images/user/20170320/20170320124620YC470557938.jpg
         * integrated : 0
         * name : 唐海斌
         * rank : 0
         */

        private int id;
        private String id_card_head_image;
        private int integrated;
        private String name;
        private int rank;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getId_card_head_image() {
            return id_card_head_image;
        }

        public void setId_card_head_image(String id_card_head_image) {
            this.id_card_head_image = id_card_head_image;
        }

        public int getIntegrated() {
            return integrated;
        }

        public void setIntegrated(int integrated) {
            this.integrated = integrated;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }
    }

    public static class MonthRefuelBean {
        /**
         * id : 6
         * id_card_head_image : /images/user/20170320/20170320124620YC470557938.jpg
         * name : 唐海斌
         * rank : 0
         * refuel : 0
         */

        private int id;
        private String id_card_head_image;
        private String name;
        private int rank;
        private int refuel;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getId_card_head_image() {
            return id_card_head_image;
        }

        public void setId_card_head_image(String id_card_head_image) {
            this.id_card_head_image = id_card_head_image;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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
    }
}
