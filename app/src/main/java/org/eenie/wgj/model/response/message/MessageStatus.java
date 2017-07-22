package org.eenie.wgj.model.response.message;

/**
 * Created by Eenie on 2017/7/22 at 16:41
 * Email: 472279981@qq.com
 * Des:
 */

public class MessageStatus {
    private int hasread;
    private int noticeid;

    public MessageStatus(int hasread, int noticeid) {
        this.hasread = hasread;
        this.noticeid = noticeid;
    }
}
