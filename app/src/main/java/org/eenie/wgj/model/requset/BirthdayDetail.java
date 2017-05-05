package org.eenie.wgj.model.requset;

import java.util.List;

/**
 * Created by Eenie on 2017/5/5 at 10:38
 * Email: 472279981@qq.com
 * Des:
 */

public class BirthdayDetail {

    /**
     * id : 3
     * user_id : 1
     * name : 唐海斌
     * avatar : /images/user/20170320/20170320154553YC996510108.jpg
     * info : []
     * blessing : 0
     */

    private int id;
    private int user_id;
    private String name;
    private String avatar;
    private int blessing;
    private List<?> info;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public int getBlessing() {
        return blessing;
    }

    public void setBlessing(int blessing) {
        this.blessing = blessing;
    }

    public List<?> getInfo() {
        return info;
    }

    public void setInfo(List<?> info) {
        this.info = info;
    }
}
