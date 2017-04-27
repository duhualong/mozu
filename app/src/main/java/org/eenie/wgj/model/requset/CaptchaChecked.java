package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/4/27 at 19:02
 * Email: 472279981@qq.com
 * Des:
 */

public class CaptchaChecked {
    private String username;
    private String verify;

    public CaptchaChecked(String username, String verify) {
        this.username = username;
        this.verify = verify;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getVerify() {
        return verify;
    }

    public void setVerify(String verify) {
        this.verify = verify;
    }
}
