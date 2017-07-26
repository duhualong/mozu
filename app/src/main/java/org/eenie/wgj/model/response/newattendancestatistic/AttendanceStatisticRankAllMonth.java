package org.eenie.wgj.model.response.newattendancestatistic;

import java.util.List;

/**
 * Created by Eenie on 2017/7/26 at 17:16
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceStatisticRankAllMonth {

    /**
     * id : 116
     * name : 韩红
     * id_card_head_image : null
     * refue : 8
     * first : 2
     * rank : 2
     * typename : 部门领导
     */

    private int id;
    private String name;
    private String id_card_head_image;
    private int refue;
    private int first;
    private int rank;
    private String typename;
    private List<ServiceBean> service;
    private int exceptions;

    public int getExceptions() {
        return exceptions;
    }

    public void setExceptions(int exceptions) {
        this.exceptions = exceptions;
    }

    public List<ServiceBean> getService() {
        return service;
    }

    public void setService(List<ServiceBean> service) {
        this.service = service;
    }

    public static class ServiceBean{
        private String servicesname;
        private int work_day;

        public String getServicesname() {
            return servicesname;
        }

        public void setServicesname(String servicesname) {
            this.servicesname = servicesname;
        }

        public int getWork_day() {
            return work_day;
        }

        public void setWork_day(int work_day) {
            this.work_day = work_day;
        }
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
