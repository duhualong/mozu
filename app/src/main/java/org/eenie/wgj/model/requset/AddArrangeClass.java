package org.eenie.wgj.model.requset;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/5 at 16:28
 * Email: 472279981@qq.com
 * Des:
 */

public class AddArrangeClass {
    private String projectid;
    private InfoArrange infos;
    private String info;

    public AddArrangeClass(String projectid, String info) {
        this.projectid = projectid;
        this.info = info;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public InfoArrange getInfos() {
        return infos;
    }
    public void setInfos(InfoArrange infos) {
        this.infos = infos;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public static class InfoArrange{


        /**
         * date : 2017-06-08
         * ranks : [{"users":[],"name":"日班","serviceid":132},{"users":
         * [{"id":"19","name":"韩红","lineid":16},{"id":"7","name":"何潇潇","lineid":17}],
         * "name":"夜班","serviceid":133},{"users":[{"id":"6","name":"唐海斌"}],"name":"测试数据","serviceid":156}]
         */

        private String date;
        private ArrayList<RanksBean> ranks;

        public InfoArrange(String date, ArrayList<RanksBean> ranks) {
            this.date = date;
            this.ranks = ranks;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public ArrayList<RanksBean> getRanks() {
            return ranks;
        }

        public void setRanks(ArrayList<RanksBean> ranks) {
            this.ranks = ranks;
        }

        public static class RanksBean {
            /**
             * users : []
             * name : 日班
             * serviceid : 132
             */

            private String name;
            private int serviceid;
            private ArrayList<UserInfo> users;

            public RanksBean(String name, int serviceid, ArrayList<UserInfo> users) {
                this.name = name;
                this.serviceid = serviceid;
                this.users = users;
            }

            public static class UserInfo{
                private int id;
                private String name;
                private int lineid;

                public UserInfo(int id, String name, int lineid) {
                    this.id = id;
                    this.name = name;
                    this.lineid = lineid;
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

                public int getLineid() {
                    return lineid;
                }

                public void setLineid(int lineid) {
                    this.lineid = lineid;
                }
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public int getServiceid() {
                return serviceid;
            }

            public void setServiceid(int serviceid) {
                this.serviceid = serviceid;
            }


        }
    }


}
