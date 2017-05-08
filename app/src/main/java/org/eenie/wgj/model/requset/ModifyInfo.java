package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/5/8 at 18:26
 * Email: 472279981@qq.com
 * Des:
 */

public class ModifyInfo {
    private String avatar;
    private String height;
    private String graduate;
    private String telephone;
    private String living_address;
    private EmergencyContactBean emergency_contact;
    private String industry;
    private String skill;
    private String channel;
    private String work_experience;
    private String bank_card;
    private String security_card;

    public ModifyInfo(String avatar, String height, String graduate, String telephone,
                      String living_address, EmergencyContactBean emergency_contact,
                      String industry, String skill, String channel, String work_experience,
                      String bank_card, String security_card) {
        this.avatar = avatar;
        this.height = height;
        this.graduate = graduate;
        this.telephone = telephone;
        this.living_address = living_address;
        this.emergency_contact = emergency_contact;
        this.industry = industry;
        this.skill = skill;
        this.channel = channel;
        this.work_experience = work_experience;
        this.bank_card = bank_card;
        this.security_card = security_card;
    }

    public static class EmergencyContactBean {
        /**
         * name : 回家
         * phone : 18817772485
         * relation : 儿子
         */

        private String name;
        private String phone;
        private String relation;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getRelation() {
            return relation;
        }

        public void setRelation(String relation) {
            this.relation = relation;
        }
    }
}
