package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/5/8 at 16:56
 * Email: 472279981@qq.com
 * Des:
 */

public class LoginData {

    /**
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0OTQyMzM2ODAsIm5iZiI6MTQ5NDIzMzY4MSwiZXhwIjoxNTI1MzM3NjgxLCJkYXRhIjp7ImlkIjo0Nn19.o3hRHw3dMMG6sHMwbX5B8RiQQXRFjdvlzSc-L_JdFIk
     * permissions : 0
     * permissions_name : 用户
     * userid : 46
     */

    private String token;
    private int permissions;

    private int user_id;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
}
