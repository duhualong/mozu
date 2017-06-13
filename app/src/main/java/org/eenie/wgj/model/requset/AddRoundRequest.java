package org.eenie.wgj.model.requset;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/12 at 15:55
 * Email: 472279981@qq.com
 * Des:
 */

public class AddRoundRequest {
    private String title;
    private String text;
    private ArrayList<Integer>userid;

    public AddRoundRequest(String title, String text, ArrayList<Integer> userid) {
        this.title = title;
        this.text = text;
        this.userid = userid;
    }
}
