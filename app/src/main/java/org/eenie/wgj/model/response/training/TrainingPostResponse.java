package org.eenie.wgj.model.response.training;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/7/7 at 9:39
 * Email: 472279981@qq.com
 * Des:
 */

public class TrainingPostResponse {
    private int id;
    private String trainingname;
    private String trainingcontent;
    private boolean whether;
    private ArrayList<ImageBean> image;

    public String getTrainingcontent() {
        return trainingcontent;
    }

    public void setTrainingcontent(String trainingcontent) {
        this.trainingcontent = trainingcontent;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTrainingname() {
        return trainingname;
    }

    public void setTrainingname(String trainingname) {
        this.trainingname = trainingname;
    }

    public boolean isWhether() {
        return whether;
    }

    public void setWhether(boolean whether) {
        this.whether = whether;
    }

    public ArrayList<ImageBean> getImage() {
        return image;
    }

    public void setImage(ArrayList<ImageBean> image) {
        this.image = image;
    }

    public static class ImageBean{
        private String image;
        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }
    }

}
