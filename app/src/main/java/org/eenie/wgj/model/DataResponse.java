package org.eenie.wgj.model;

/**
 * Created by Eenie on 2017/5/11 at 15:07
 * Email: 472279981@qq.com
 * Des:
 */

public class DataResponse {
    private int resultCode;
    private String resultMessage;
    private Object data;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(String resultMessage) {
        this.resultMessage = resultMessage;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
