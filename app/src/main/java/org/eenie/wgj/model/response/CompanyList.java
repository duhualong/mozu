package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/5/10 at 15:40
 * Email: 472279981@qq.com
 * Des:
 */

public class CompanyList {


    /**
     * companyid : 1
     * user_id : 43
     * company_name : 上海优驰保安服务有限公司
     * email : Lionared@163.com
     * city : 上海市
     * location : 上海市文化广场
     * representative : 裴铁军
     * logo : null
     * intro : null
     * license_pic : /images/youchi_license.jpg
     * created_at : 2017-05-08 18:02:34
     * updated_at : 2017-05-08 18:02:38
     * status : 1
     */

    private int companyid;
    private int user_id;
    private String company_name;
    private String email;
    private String city;
    private String location;
    private String representative;
    private Object logo;
    private Object intro;
    private String license_pic;
    private String created_at;
    private String updated_at;
    private int status;

    public int getCompanyid() {
        return companyid;
    }

    public void setCompanyid(int companyid) {
        this.companyid = companyid;
    }

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

    public Object getLogo() {
        return logo;
    }

    public void setLogo(Object logo) {
        this.logo = logo;
    }

    public Object getIntro() {
        return intro;
    }

    public void setIntro(Object intro) {
        this.intro = intro;
    }

    public String getLicense_pic() {
        return license_pic;
    }

    public void setLicense_pic(String license_pic) {
        this.license_pic = license_pic;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
