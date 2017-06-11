package org.eenie.wgj.model.response;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/9 at 16:09
 * Email: 472279981@qq.com
 * Des:
 */

public class WorkShowList {

    /**
     * id : 67
     * name : 韩红
     * id_card_head_image : /images/user/20170524/20170524191121YC849919620.jpg
     * project_id : 3
     * projectname : 会得丰
     * theme : 何总设计师
     * type : 1
     * time : 2017-06-09 13:36:34
     * extra : [{"image":"/images/workshow/20170609/20170609133634YC595979133.jpg"}]
     * like : 1
     * praise : 0
     * myself : 1
     * rank : 1
     */

    private int id;
    private String name;
    private String id_card_head_image;
    private int project_id;
    private String projectname;
    private String theme;
    private int type;
    private String time;
    private int like;
    private int praise;
    private int myself;
    private int rank;
    private ArrayList<ExtraBean> extra;

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

    public String getId_card_head_image() {
        return id_card_head_image;
    }

    public void setId_card_head_image(String id_card_head_image) {
        this.id_card_head_image = id_card_head_image;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getProjectname() {
        return projectname;
    }

    public void setProjectname(String projectname) {
        this.projectname = projectname;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getLike() {
        return like;
    }

    public void setLike(int like) {
        this.like = like;
    }

    public int getPraise() {
        return praise;
    }

    public void setPraise(int praise) {
        this.praise = praise;
    }

    public int getMyself() {
        return myself;
    }

    public void setMyself(int myself) {
        this.myself = myself;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public ArrayList<ExtraBean> getExtra() {
        return extra;
    }

    public void setExtra(ArrayList<ExtraBean> extra) {
        this.extra = extra;
    }

    public static class ExtraBean {
        /**
         * image : /images/workshow/20170609/20170609133634YC595979133.jpg
         */

        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
