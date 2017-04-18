package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/4/11 at 14:15
 * Des:
 */

public class Contacts {

    /**
     * id : 1
     * name : 唐海斌
     * phone : 13800138004
     * id_card_head_image : /images/user/20170320/20170320154553YC996510108.jpg
     * duties : 项目经理
     */
//    private String firstLetter;
//    private String pinyin; // 姓名对应的拼音
    private int id;
    private String name;
    private String phone;
    private String id_card_head_image;
    private String duties;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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

    public String getId_card_head_image() {
        return id_card_head_image;
    }

    public void setId_card_head_image(String id_card_head_image) {
        this.id_card_head_image = id_card_head_image;
    }

    public String getDuties() {
        return duties;
    }

    public void setDuties(String duties) {
        this.duties = duties;
    }

//    public Contacts() {
//    }

//    public Contacts(String name) {
//        this.name = name;
//        pinyin = Cn2Spell.getPinYin(name); // 根据姓名获取拼音
//        firstLetter = pinyin.substring(0, 1).toUpperCase(); // 获取拼音首字母并转成大写
//        if (!firstLetter.matches("[A-Z]")) { // 如果不在A-Z中则默认为“#”
//            firstLetter = "#";
//        }
//    }



//
//    public String getFirstLetter() {
//        return firstLetter;
//    }
//
//    public String getPinyin() {
//        return pinyin;
//    }




}
