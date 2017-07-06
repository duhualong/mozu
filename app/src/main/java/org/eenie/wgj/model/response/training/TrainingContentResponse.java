package org.eenie.wgj.model.response.training;

/**
 * Created by Eenie on 2017/7/6 at 17:49
 * Email: 472279981@qq.com
 * Des:
 */

public class TrainingContentResponse {

    /**
     * schedule : 0
     * totalpage : 0
     * currentpage : 1
     * type : 1
     */
    private double schedule;
    private int totalpage;
    private int currentpage;
    private int type;

    public double getSchedule() {
        return schedule;
    }

    public void setSchedule(double schedule) {
        this.schedule = schedule;
    }

    public int getTotalpage() {
        return totalpage;
    }

    public void setTotalpage(int totalpage) {
        this.totalpage = totalpage;
    }

    public int getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
