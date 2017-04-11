package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/4/5 at 14:47
 * 登录请求
 * Des:
 */

public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
