package org.eenie.wgj.model.response.projectworktime;

import java.util.List;

/**
 * Created by Eenie on 2017/8/7 at 17:55
 * Email: 472279981@qq.com
 * Des:
 */

public class NewProjectTime {
    private String date;
    private List<serviceBean>services;

    public NewProjectTime(String date, List<serviceBean> services) {
        this.date = date;
        this.services = services;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<serviceBean> getServices() {
        return services;
    }

    public void setServices(List<serviceBean> services) {
        this.services = services;
    }

    public static  class serviceBean{
        private int id;
        private  int persons;

        public serviceBean(int id, int persons) {
            this.id = id;
            this.persons = persons;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPersons() {
            return persons;
        }

        public void setPersons(int persons) {
            this.persons = persons;
        }
    }
}
