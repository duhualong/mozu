package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/5/9 at 10:26
 * Email: 472279981@qq.com
 * Des:
 */

public class RegisterSuccessData {

    /**
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0OTQyOTc0NTIsIm5iZiI6MTQ5NDI5NzQ1MywiZXhwIjoxNTI1NDAxNDUzLCJkYXRhIjp7ImlkIjo1NX19.PrAfNkXfMftUdVKMR75HnovRjWGmcRVR2mFaFueRfXQ
     * user_id : 55
     * type : 0
     */

    private String token;
    private int user_id;
    private int type;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
