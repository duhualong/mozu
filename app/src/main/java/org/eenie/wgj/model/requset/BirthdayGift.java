package org.eenie.wgj.model.requset;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eenie on 2017/5/5 at 13:44
 * Email: 472279981@qq.com
 * Des:
 */

public class BirthdayGift {
    private int id;
    private int icon;
    private String name;
    List<BirthdayGift> mBirthdayGifts = new ArrayList<>();
    public BirthdayGift(int id, int icon, String name) {
        this.id = id;
        this.icon = icon;
        this.name = name;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
