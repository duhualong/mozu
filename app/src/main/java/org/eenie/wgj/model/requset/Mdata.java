package org.eenie.wgj.model.requset;

import org.eenie.wgj.model.response.ShootList;

import java.util.List;

/**
 * Created by Eenie on 2017/4/7 at 8:59
 * Email: 472279981@qq.com
 * Des:
 */

public class Mdata {
    private String resultCode;
    private List<ShootList> resultMessage;


    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public List<ShootList> getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(List<ShootList> resultMessage) {
        this.resultMessage = resultMessage;
    }
}
