package org.eenie.wgj.model.response;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Eenie on 2017/4/5 at 14:44
 *登录返回值
 */

public class Login extends RealmObject{

    /**
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0OTEzNzE4NDcsIm5iZiI6MTQ5MTM3MTg0OCwiZXhwIjoxNTIyNDc1ODQ4LCJkYXRhIjp7ImlkIjoxfX0.IjlMijZTZF5VnZRXYv0Q9Mo-Vyrip-3dyxhzTTf8VNI
     * unique : 28c8edde3d61a0411511d3b1866f0636
     * type : 1
     * permissions : 3
     * permissions_name : 总部职员
     * username : 13800138004
     * project_id : 1
     * bank_card :
     * security_card :
     * name : 唐海斌
     * people : 汉
     * avatar : /images/user/20170320/20170320154553YC996510108.jpg
     * gender : 男
     * birthday : 2017-03-31
     * address : 广西玉林
     * number : 450902199211011735
     * publisher : 玉林公安局
     * validate : 2016.11.11
     * id_card_positive : /images/user/20161104/20161104014632YC354918448.jpg
     * id_card_negative : /images/user/20161104/20161104014632YC985585644.jpg
     * id_card_head_image : /images/user/20161104/20161104014632YC954564236.jpg
     * height : 190
     * graduate : 本科
     * telephone : 未婚
     * living_address : 上海龙华
     * emergency_contact : {"name": "唐太宗", "phone": "1388680869", "relation": "父亲"}
     * industry : ["保安", "物业", "其他"]
     * skill : ["武术", "退伍", "其他"]
     * channel : 赶集网
     * employer : 上海公
     * work_content : 很多内容
     * work_experience : 很多经验
     */
    @PrimaryKey
    private String token;

    private String unique;
    private int type;
    private int permissions;
    private String permissions_name;
    private String username;
    private int project_id;
    private String bank_card;
    private String security_card;
    private String name;
    private String people;
    private String avatar;
    private String gender;
    private String birthday;
    private String address;
    private String number;
    private String publisher;
    private String validate;
    private String id_card_positive;
    private String id_card_negative;
    private String id_card_head_image;
    private int height;
    private String graduate;
    private String telephone;
    private String living_address;
    private String emergency_contact;
    private String industry;
    private String skill;
    private String channel;
    private String employer;
    private String work_content;
    private String work_experience;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUnique() {
        return unique;
    }

    public void setUnique(String unique) {
        this.unique = unique;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getPermissions() {
        return permissions;
    }

    public void setPermissions(int permissions) {
        this.permissions = permissions;
    }

    public String getPermissions_name() {
        return permissions_name;
    }

    public void setPermissions_name(String permissions_name) {
        this.permissions_name = permissions_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getBank_card() {
        return bank_card;
    }

    public void setBank_card(String bank_card) {
        this.bank_card = bank_card;
    }

    public String getSecurity_card() {
        return security_card;
    }

    public void setSecurity_card(String security_card) {
        this.security_card = security_card;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeople() {
        return people;
    }

    public void setPeople(String people) {
        this.people = people;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getValidate() {
        return validate;
    }

    public void setValidate(String validate) {
        this.validate = validate;
    }

    public String getId_card_positive() {
        return id_card_positive;
    }

    public void setId_card_positive(String id_card_positive) {
        this.id_card_positive = id_card_positive;
    }

    public String getId_card_negative() {
        return id_card_negative;
    }

    public void setId_card_negative(String id_card_negative) {
        this.id_card_negative = id_card_negative;
    }

    public String getId_card_head_image() {
        return id_card_head_image;
    }

    public void setId_card_head_image(String id_card_head_image) {
        this.id_card_head_image = id_card_head_image;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getGraduate() {
        return graduate;
    }

    public void setGraduate(String graduate) {
        this.graduate = graduate;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getLiving_address() {
        return living_address;
    }

    public void setLiving_address(String living_address) {
        this.living_address = living_address;
    }

    public String getEmergency_contact() {
        return emergency_contact;
    }

    public void setEmergency_contact(String emergency_contact) {
        this.emergency_contact = emergency_contact;
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getEmployer() {
        return employer;
    }

    public void setEmployer(String employer) {
        this.employer = employer;
    }

    public String getWork_content() {
        return work_content;
    }

    public void setWork_content(String work_content) {
        this.work_content = work_content;
    }

    public String getWork_experience() {
        return work_experience;
    }

    public void setWork_experience(String work_experience) {
        this.work_experience = work_experience;
    }
}
