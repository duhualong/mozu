package org.eenie.wgj.model.response;

/**
 * Created by Eenie on 2017/6/20 at 11:19
 * Email: 472279981@qq.com
 * Des:
 */

public class MonthAllSort {

    /**
     * id : 6
     * name : 唐海斌
     * permissions : 项目主管
     * id_card_head_image : /images/user/20170320/20170320124620YC470557938.jpg
     * integrated : 0
     */

    private int id;
    private String name;
    private String permissions;
    private String id_card_head_image;
    private int integrated;

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

    public String getPermissions() {
        return permissions;
    }

    public void setPermissions(String permissions) {
        this.permissions = permissions;
    }

    public String getId_card_head_image() {
        return id_card_head_image;
    }

    public void setId_card_head_image(String id_card_head_image) {
        this.id_card_head_image = id_card_head_image;
    }

    public int getIntegrated() {
        return integrated;
    }

    public void setIntegrated(int integrated) {
        this.integrated = integrated;
    }
}
