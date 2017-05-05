package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/5/4 at 18:28
 * Email: 472279981@qq.com
 * Des:
 */

public class NoticeMessage {

    /**
     * id : 1137
     * key : 2103
     * title : 巡检提醒
     * alert : 你该去巡检了
     * created_at : 2017-05-04 10:25:00
     * parameter :
     */

    private int id;
    private int key;
    private String title;
    private String alert;
    private String created_at;
    private String parameter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getParameter() {
        return parameter;
    }

    public void setParameter(String parameter) {
        this.parameter = parameter;
    }
}
