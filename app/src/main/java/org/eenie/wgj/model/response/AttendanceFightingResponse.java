package org.eenie.wgj.model.response;

import java.util.List;

/**
 * Created by Eenie on 2017/6/20 at 17:13
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceFightingResponse  {

    /**
     * id : 6
     * name : 唐海斌
     * permissions : 项目主管
     * id_card_head_image : /images/user/20170320/20170320124620YC470557938.jpg
     * refue : 2
     * work_number : 0
     * service : [{"servicesname":"休息日","work_day":31}]
     */

    private int id;
    private String name;
    private String permissions;
    private String id_card_head_image;
    private int refue;
    private int work_number;
    private List<ServiceBean> service;

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

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
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

    public int getWork_number() {
        return work_number;
    }

    public void setWork_number(int work_number) {
        this.work_number = work_number;
    }

    public List<ServiceBean> getService() {
        return service;
    }

    public void setService(List<ServiceBean> service) {
        this.service = service;
    }

    public static class ServiceBean {
        /**
         * servicesname : 休息日
         * work_day : 31
         */

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
}
