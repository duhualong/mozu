package org.eenie.wgj.model.response;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/12 at 9:44
 * Email: 472279981@qq.com
 * Des:
 */

public class AddReportNumber {

    /**
     * id : 2
     * name : 财务部门
     * info : [{"user_id":3,"name":"1111"},{"user_id":5,"name":"易善涛"},
     * {"user_id":6,"name":"唐海斌"},{"user_id":7,"name":"何潇潇"},
     * {"user_id":12,"name":"范宝珅"},{"user_id":13,"name":"陈静"},{"user_id":19,"name":"韩红"}]
     */

    private int id;
    private String name;
    private ArrayList<InfoBean> info;
    private boolean checkReport;

    public boolean isCheckReport() {
        return checkReport;
    }

    public void setCheckReport(boolean checkReport) {
        this.checkReport = checkReport;
    }
    public void toggle() {
        this.checkReport = !this.checkReport;
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

    public ArrayList<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        /**
         * user_id : 3
         * name : 1111
         */

        private int user_id;
        private String name;
        private boolean checkInfo;

        public boolean isCheckInfo() {
            return checkInfo;
        }

        public void setCheckInfo(boolean checkInfo) {
            this.checkInfo = checkInfo;
        }

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
        public void toggle() {
            this.checkInfo = !this.checkInfo;
        }

    }
}
