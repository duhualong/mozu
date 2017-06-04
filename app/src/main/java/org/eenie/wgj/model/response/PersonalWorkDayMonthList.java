package org.eenie.wgj.model.response;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/4 at 12:18
 * Email: 472279981@qq.com
 * Des:
 */

public class PersonalWorkDayMonthList {

    /**
     * id : 1191
     * user_id : 6
     * user_name : 唐海斌
     * info : [{"service":{"id":132,"servicesname":"日班","starttime":"09:00:00","endtime":"19:30:00","time":10.5},"day":"8"}]
     */

    private int id;
    private String user_id;
    private String user_name;
    private ArrayList<ClassListWorkTime> info;

    public PersonalWorkDayMonthList(String user_id, String user_name,ArrayList<ClassListWorkTime> info) {
        this.user_id = user_id;
        this.user_name = user_name;
        this.info = info;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }


    public ArrayList<ClassListWorkTime> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<ClassListWorkTime> info) {
        this.info = info;
    }
}
