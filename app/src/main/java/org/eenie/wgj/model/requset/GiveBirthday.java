package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/5/5 at 13:37
 * Email: 472279981@qq.com
 * Des:
 */

public class GiveBirthday {

    /**
     * id : 3
     * greetings : 生日快乐！
     * cake : 4003
     */

    private String id;
    private String greetings;
    private String cake;

    public GiveBirthday(String id, String greetings, String cake) {
        this.id = id;
        this.greetings = greetings;
        this.cake = cake;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGreetings() {
        return greetings;
    }

    public void setGreetings(String greetings) {
        this.greetings = greetings;
    }

    public String getCake() {
        return cake;
    }

    public void setCake(String cake) {
        this.cake = cake;
    }
}
