package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/4/26 at 18:57
 * Email: 472279981@qq.com
 * Des:
 */

public class MLogin {
    private String username;
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public MLogin(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
