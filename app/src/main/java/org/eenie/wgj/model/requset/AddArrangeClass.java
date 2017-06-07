package org.eenie.wgj.model.requset;

import java.util.List;

/**
 * Created by Eenie on 2017/6/5 at 16:28
 * Email: 472279981@qq.com
 * Des:
 */

public class AddArrangeClass {
    private String projectid;
    private InfoArrange info;

    public AddArrangeClass(String projectid, InfoArrange info) {
        this.projectid = projectid;
        this.info = info;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public InfoArrange getInfo() {
        return info;
    }

    public void setInfo(InfoArrange info) {
        this.info = info;
    }

    public static class InfoArrange{


        /**
         * date : 2017-06-08
         * ranks : [{"users":[],"name":"日班","serviceid":132},{"users":[{"id":"19","name":"韩红","lineid":16},{"id":"7","name":"何潇潇","lineid":17}],"name":"夜班","serviceid":133},{"users":[{"id":"6","name":"唐海斌"}],"name":"测试数据","serviceid":156}]
         */

        private String date;
        private List<RanksBean> ranks;


        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public List<RanksBean> getRanks() {
            return ranks;
        }

        public void setRanks(List<RanksBean> ranks) {
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
            private List<UserInfo> users;
            public static class UserInfo{
                private int id;
                private String name;
                private int lineid;

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
