package org.eenie.wgj.model;

/**
 * Created by Eenie on 2017/4/5 at 14:39
 * Email: 472279981@qq.com
 * Des:
 */

public class ApiResponse<T> {
    private int resultCode;
    private String resultMessage;
    private T data;

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

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
