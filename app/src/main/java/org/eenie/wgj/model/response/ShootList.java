package org.eenie.wgj.model.response;

import java.util.List;

/**
 * Created by Eenie on 2017/4/6 at 16:34
 * Email: 472279981@qq.com
 * Des:
 */

public class ShootList {

    /**
     * id : 67
     * title : test2
     * text : test3
     * created_at : 2017-04-06 16:06:51
     * username : 唐海斌
     * image : [{"image":"/images/readilyShoot/20170406/20170406160650YC392002694.jpg"}]
     */

    private int id;
    private String title;
    private String text;
    private String created_at;
    private String username;
    private List<ImageBean> image;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public List<ImageBean> getImage() {
        return image;
    }

    public void setImage(List<ImageBean> image) {
        this.image = image;
    }

    public static class ImageBean {
        /**
         * image : /images/readilyShoot/20170406/20170406160650YC392002694.jpg
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
