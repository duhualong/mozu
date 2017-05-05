package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/5/5 at 10:02
 * Email: 472279981@qq.com
 * Des:
 */

public class BirthdayAlert {

    /**
     * id : 3
     * user_id : 1
     * name : 唐海斌
     * time : 2017-03-22
     * created_at : 2017-03-17 15:07:28
     * text : 2017-03-22是你的生日，快去看看那些人送给你祝福去吧
     */

    private int id;
    private int user_id;
    private String name;
    private String time;
    private String created_at;
    private String text;

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

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
