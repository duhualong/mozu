package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/5/11 at 17:53
 * Email: 472279981@qq.com
 * Des:
 */

public class JoinCompany {

    /**
     * user_id : 21
     * companyid : 1
     */

    private int user_id;
    private int companyid;

    public JoinCompany(int user_id, int companyid) {
        this.user_id = user_id;
        this.companyid = companyid;
    }
}
