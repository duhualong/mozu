package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/5/9 at 10:34
 * Email: 472279981@qq.com
 * Des:
 */

public class CreataCompanyRequest {

    /**
     * user_id : 43
     * company_name : 上海优驰
     * email : 145621998@qq.com
     * city : 上海长宁
     * location : 上海火车站
     * representative : 张三
     * intro : 保安
     * logo :
     * license_pic : ssssss
     */

    private int user_id;
    private String company_name;
    private String email;
    private String city;
    private String location;
    private String representative;
    private String intro;
    private String logo;
    private String license_pic;

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getRepresentative() {
        return representative;
    }

    public void setRepresentative(String representative) {
        this.representative = representative;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getLicense_pic() {
        return license_pic;
    }

    public void setLicense_pic(String license_pic) {
        this.license_pic = license_pic;
    }
}
