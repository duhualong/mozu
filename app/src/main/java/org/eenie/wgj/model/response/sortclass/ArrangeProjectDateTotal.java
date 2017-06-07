package org.eenie.wgj.model.response.sortclass;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/7 at 15:08
 * Email: 472279981@qq.com
 * Des:
 */

public class ArrangeProjectDateTotal {
    private String date;
    private ArrayList<ArrangeServiceTotal> service;
    private boolean checked;


    public ArrangeProjectDateTotal(String date, ArrayList<ArrangeServiceTotal> service) {
        this.date = date;
        this.service = service;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<ArrangeServiceTotal> getService() {
        return service;
    }

    public void setService(ArrayList<ArrangeServiceTotal> service) {
        this.service = service;
    }
}
