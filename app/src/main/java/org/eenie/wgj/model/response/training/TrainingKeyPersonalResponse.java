package org.eenie.wgj.model.response.training;

/**
 * Created by Eenie on 2017/7/7 at 11:59
 * Email: 472279981@qq.com
 * Des:
 */

public class TrainingKeyPersonalResponse {

    /**
     * id : 214
     * name : 刘总
     * images : /images/user/20170619/20170619091243YC756877605.jpg
     * sex : 1
     * age : 40
     * height : 175
     * job : 总经理
     * workinghours : 8:00-17:00
     * numberplates : 沪12356
     * remarks : 要求比较高
     * phone : 13888888888
     * whether : true
     */

    private int id;
    private String name;
    private String images;
    private int sex;
    private int age;
    private int height;
    private String job;
    private String workinghours;
    private String numberplates;
    private String remarks;
    private String phone;
    private boolean whether;

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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getWorkinghours() {
        return workinghours;
    }

    public void setWorkinghours(String workinghours) {
        this.workinghours = workinghours;
    }

    public String getNumberplates() {
        return numberplates;
    }

    public void setNumberplates(String numberplates) {
        this.numberplates = numberplates;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isWhether() {
        return whether;
    }

    public void setWhether(boolean whether) {
        this.whether = whether;
    }
}
