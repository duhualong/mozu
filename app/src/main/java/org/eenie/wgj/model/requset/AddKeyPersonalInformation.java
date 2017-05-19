package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/5/18 at 10:43
 * Email: 472279981@qq.com
 * Des:
 */

public class AddKeyPersonalInformation {
    private String age;
    private String height;
    private String image;
    private String job;
    private String name;
    private String numberplates;
    private String phone;
    private String project_id;
    private String sex;
    private String workinghours;
    private int id;
    private String remarks;
//添加关键人信息

    public AddKeyPersonalInformation(String age, String height, String image, String job,
                                     String name, String numberplates, String phone, String sex,
                                     String workinghours, int id) {
        this.age = age;
        this.height = height;
        this.image = image;
        this.job = job;
        this.name = name;
        this.numberplates = numberplates;
        this.phone = phone;
        this.sex = sex;
        this.workinghours = workinghours;
        this.id = id;
    }

    //修改关键人信息


    public AddKeyPersonalInformation(String age, String height, String image, String job,
                                     String name, String numberplates, String phone,
                                     String project_id, String sex, String workinghours,
                                     String remarks,int id) {
        this.age = age;
        this.height = height;
        this.image = image;
        this.job = job;
        this.name = name;
        this.numberplates = numberplates;
        this.phone = phone;
        this.project_id = project_id;
        this.sex = sex;
        this.workinghours = workinghours;
        this.remarks=remarks;
        this.id = id;
    }
}
