package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/5/27 at 17:56
 * Email: 472279981@qq.com
 * Des:
 */

public class CycleNumberEdit {
    private int id;
    private String time;

    public CycleNumberEdit(int id, String time) {
        this.id = id;
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
