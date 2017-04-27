package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/4/26 at 10:02
 * Email: 472279981@qq.com
 * Des:
 */

public class TestLogin {

    /**
     * token : eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpYXQiOjE0OTMxNzEzNzcsIm5iZiI6MTQ5MzE3MTM3OCwiZXhwIjoxNTI0Mjc1Mzc4LCJkYXRhIjp7ImlkIjozMn19.zHsSc1Lh513jc3yhQyx6Ob8teRRk-0BXNY-VQluysrU
     * unique : 817808d063b210535f9a3ebbf173ea3d
     * type : 1
     * permissions : 2
     * permissions_name : 项目主管
     * username : 18817772486
     * project_id :
     * bank_card :
     * security_card :
     * name : 张三
     * people : 汉
     * avatar :
     * gender : 男
     * birthday : 1993-03-02
     * address : 上海
     * number : 411721199305063425
     * publisher : 上海
     * validate : 1992-02-05
     * id_card_positive : /images/user/20170421/20170421163556YC1760931687.jpg
     * id_card_negative : /images/user/20170421/20170421163556YC677503516.jpg
     * id_card_head_image : /images/user/20170421/20170421163556YC580811895.jpg
     * height : 150
     * graduate : sss
     * telephone : 1
     * living_address : sss
     * emergency_contact : {"name": "BeJson", "phone": "15071234893 ", "relation": "儿子"}
     * industry : [111, 1111]
     * skill : [111, 1111]
     * channel : sss
     * employer :
     * work_content :
     * work_experience :
     */
private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private String token;
    private String unique;
    private int type;
    private int permissions;
    private String permissions_name;
    private String username;
    private String project_id;
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
    private Mergency_contact emergency_contact;
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

    public String getProject_id() {
        return project_id;
    }

    public void setProject_id(String project_id) {
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

    public Mergency_contact getEmergency_contact() {
        return emergency_contact;
    }

    public void setEmergency_contact(Mergency_contact emergency_contact) {
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
    class Mergency_contact{
        private String name;
        private String relation;
        private String phone;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }
    }

}
