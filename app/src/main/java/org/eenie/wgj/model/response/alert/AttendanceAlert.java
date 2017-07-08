package org.eenie.wgj.model.response.alert;

/**
 * Created by Eenie on 2017/7/8 at 15:31
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceAlert {

    /**
     * start : 07:30:00
     * open : 0
     * end : 20:40:00
     */

    private String start;
    private int open;
    private String end;

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public int getOpen() {
        return open;
    }

    public void setOpen(int open) {
        this.open = open;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
