package org.eenie.wgj.model.response.exchangework;

import java.util.List;

/**
 * Created by Eenie on 2017/7/13 at 16:25
 * Email: 472279981@qq.com
 * Des:
 */

public class AddExchangeWorkResponse {

    /**
     * explanation : 测试数据
     * userid : [3,1]
     * precautionid : 209
     * task : 1
     */

    private String explanation;
    private String precautionid;
    private int task;
    private List<Integer> userid;

    public AddExchangeWorkResponse(String explanation, String precautionid,
                                   int task, List<Integer> userid) {
        this.explanation = explanation;
        this.precautionid = precautionid;
        this.task = task;
        this.userid = userid;
    }


}
