package org.eenie.wgj.model.response.newattendancestatistic;

import java.util.List;

/**
 * Created by Eenie on 2017/7/26 at 18:07
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceTotalDataMonth {

    /**
     * projectname : 办公室管理
     * total_late : 12
     * total_early : 2
     * total_goout : 32
     * total_absent : 28
     * total_intern : 0
     * total_leave : 0
     * total_extra : 0
     * total_tempor : 0
     * total_resign : 5
     * resign_rate : 29.41
     * new_employee : 18
     */

    private String projectname;
    private int total_late;
    private int total_early;
    private int total_goout;
    private int total_absent;
    private int total_intern;
    private int total_leave;
    private int total_extra;
    private int total_tempor;
    private int total_resign;
    private double resign_rate;
    private int new_employee;
    private List<ServiceBean>rank_list;
    private List<ServiceBean>first_list;
    private List<ServiceBean>refue_list;

    public List<ServiceBean> getRank_list() {
        return rank_list;
    }

    public void setRank_list(List<ServiceBean> rank_list) {
        this.rank_list = rank_list;
    }

    public List<ServiceBean> getFirst_list() {
        return first_list;
    }

    public void setFirst_list(List<ServiceBean> first_list) {
        this.first_list = first_list;
    }

    public List<ServiceBean> getRefue_list() {
        return refue_list;
    }

    public void setRefue_list(List<ServiceBean> refue_list) {
        this.refue_list = refue_list;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public int getTotal_late() {
        return total_late;
    }

    public void setTotal_late(int total_late) {
        this.total_late = total_late;
    }

    public int getTotal_early() {
        return total_early;
    }

    public void setTotal_early(int total_early) {
        this.total_early = total_early;
    }

    public int getTotal_goout() {
        return total_goout;
    }

    public void setTotal_goout(int total_goout) {
        this.total_goout = total_goout;
    }

    public int getTotal_absent() {
        return total_absent;
    }

    public void setTotal_absent(int total_absent) {
        this.total_absent = total_absent;
    }

    public int getTotal_intern() {
        return total_intern;
    }

    public void setTotal_intern(int total_intern) {
        this.total_intern = total_intern;
    }

    public int getTotal_leave() {
        return total_leave;
    }

    public void setTotal_leave(int total_leave) {
        this.total_leave = total_leave;
    }

    public int getTotal_extra() {
        return total_extra;
    }

    public void setTotal_extra(int total_extra) {
        this.total_extra = total_extra;
    }

    public int getTotal_tempor() {
        return total_tempor;
    }

    public void setTotal_tempor(int total_tempor) {
        this.total_tempor = total_tempor;
    }

    public int getTotal_resign() {
        return total_resign;
    }

    public void setTotal_resign(int total_resign) {
        this.total_resign = total_resign;
    }

    public double getResign_rate() {
        return resign_rate;
    }

    public void setResign_rate(double resign_rate) {
        this.resign_rate = resign_rate;
    }

    public int getNew_employee() {
        return new_employee;
    }

    public void setNew_employee(int new_employee) {
        this.new_employee = new_employee;
    }


    public static class ServiceBean{

        /**
         * id : 109
         * name : 裴铁军
         * id_card_head_image : /images/user/20170717/20170717204943YC907230565.jpg
         * refue : 26
         * first : 2
         * rank : -16
         * typename : 平台管理员
         */

        private int id;
        private String name;
        private String id_card_head_image;
        private int refue;
        private int first;
        private int rank;
        private String typename;

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

        public int getRefue() {
            return refue;
        }

        public void setRefue(int refue) {
            this.refue = refue;
        }

        public int getFirst() {
            return first;
        }

        public void setFirst(int first) {
            this.first = first;
        }

        public int getRank() {
            return rank;
        }

        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getTypename() {
            return typename;
        }

        public void setTypename(String typename) {
            this.typename = typename;
        }
    }
}
