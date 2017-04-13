package org.eenie.wgj.model;

/**
 * Created by Eenie on 2017/4/13 at 12:53
 * Email: 472279981@qq.com
 * Des:
 */

public class ApiRes<T> {
    private int resultCode;
    private T resultMessage;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public T getResultMessage() {
        return resultMessage;
    }

    public void setResultMessage(T resultMessage) {
        this.resultMessage = resultMessage;
    }
}
