package org.eenie.wgj.model.requset;

import org.eenie.wgj.model.response.Contacts;

import java.util.List;

/**
 * Created by Eenie on 2017/4/11 at 14:15
 * Email: 472279981@qq.com
 * Des:
 */

public class ContactsData {
    private int resultCode;
    private List<Contacts> resultMessage;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public List<Contacts> getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(List<Contacts> resultMessage) {
        this.resultMessage = resultMessage;
    }
}
