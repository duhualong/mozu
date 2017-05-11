package org.eenie.wgj.model;

import java.util.List;

/**
 * Created by Eenie on 2017/5/11 at 18:35
 * Email: 472279981@qq.com
 * Des:
 */

public class NewResponse {

    /**
     * resultCode : 101
     * resultMessage : 申请加入的公司id不能为空
     * data : []
     */

    private int resultCode;
    private String resultMessage;
    private List<?> data;

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

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
