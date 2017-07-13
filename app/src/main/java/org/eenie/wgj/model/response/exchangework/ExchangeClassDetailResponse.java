package org.eenie.wgj.model.response.exchangework;


import java.util.List;

/**
 * Created by Eenie on 2017/7/13 at 14:59
 * Email: 472279981@qq.com
 * Des:
 */

public class ExchangeClassDetailResponse {

    /**
     * id : 209
     * mattername : 日班
     * matter : 对讲机
     测试
     */

    private String id;
    private String mattername;
    private String matter;
    private List<ImageBean>image;

    public List<ImageBean> getImage() {
        return image;
    }

    public void setImage(List<ImageBean> image) {
        this.image = image;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMattername() {
        return mattername;
    }

    public void setMattername(String mattername) {
        this.mattername = mattername;
    }

    public String getMatter() {
        return matter;
    }

    public void setMatter(String matter) {
        this.matter = matter;
    }

    public static  class ImageBean{
        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }
}
