package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/11 at 13:40
 * Email: 472279981@qq.com
 * Des:
 */

public class AddWorkShow {

    /**
     * type : 1
     * theme : 234234
     */

    private int type;
    private String theme;

    public AddWorkShow(int type, String theme) {
        this.type = type;
        this.theme = theme;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }
}
