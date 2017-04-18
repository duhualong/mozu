package org.eenie.wgj.model;

import org.eenie.wgj.model.response.Infomation;

import java.util.List;

/**
 * Created by Eenie on 2017/4/14 at 19:00
 * Email: 472279981@qq.com
 * Des:
 */

public class Api {
    public Message message;
    public List<Infomation> cardsinfo;

    public Message getMessage() {
        return message;
    }

    public void setMessage(Message message) {
        this.message = message;
    }

    public List<Infomation> getCardsinfo() {
        return cardsinfo;
    }

    public void setCardsinfo(List<Infomation> cardsinfo) {
        this.cardsinfo = cardsinfo;
    }


}
