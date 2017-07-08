package org.eenie.wgj.model.response.training;


import java.util.List;

/**
 * Created by Eenie on 2017/7/7 at 13:02
 * Email: 472279981@qq.com
 * Des:
 */

public class TrainingStatisticListResponse {
    private int id;
    private String name;
    private List<InfoBean> info;

    public List<InfoBean> getInfo() {
        return info;
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

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean {
        private double schedule;
        private int type;

        public double getSchedule() {
            return schedule;
        }

        public void setSchedule(double schedule) {
            this.schedule = schedule;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

}
