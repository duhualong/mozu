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
    private String marry;
    private String living_address;
    private String emergency_contact;
    private String industry;
    private String skill;
    private String channel;
    private String work_experience;
    private String bank_card;
    private String security_card;
    private String work_content;

    public ModifyInfo(String avatar, String bank_card, String security_card) {
        this.avatar = avatar;
        this.bank_card = bank_card;
        this.security_card = security_card;
    }

    public ModifyInfo(String height, String graduate, String marry, String living_address,
                      String emergency_contact, String industry, String skill, String channel) {
        this.height = height;
        this.graduate = graduate;
        this.marry = marry;
        this.living_address = living_address;
        this.emergency_contact = emergency_contact;
        this.industry = industry;
        this.skill = skill;
        this.channel = channel;
    }

    public ModifyInfo(String height, String graduate, String marry, String living_address,
                      String emergency_contact, String industry, String skill, String channel,
                      String work_experience, String work_content) {
        this.height = height;
        this.graduate = graduate;
        this.marry = marry;
        this.living_address = living_address;
        this.emergency_contact = emergency_contact;
        this.industry = industry;
        this.skill = skill;
        this.channel = channel;
        this.work_experience = work_experience;
        this.work_content = work_content;
    }

    public ModifyInfo(String avatar, String height, String graduate, String marry,
                      String living_address, String emergency_contact, String industry,
                      String skill, String channel, String work_experience, String bank_card,
                      String security_card) {
        this.avatar = avatar;
        this.height = height;
        this.graduate = graduate;
        this.marry = marry;
        this.living_address = living_address;
        this.emergency_contact = emergency_contact;
        this.industry = industry;
        this.skill = skill;
        this.channel = channel;
        this.work_experience = work_experience;
        this.bank_card = bank_card;
        this.security_card = security_card;
    }
}
