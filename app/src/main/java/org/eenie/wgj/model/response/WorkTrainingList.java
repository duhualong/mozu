package org.eenie.wgj.model.response;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/5/21 at 14:50
 * Email: 472279981@qq.com
 * Des:
 */

public class WorkTrainingList implements Parcelable {

    /**
     * id : 95
     * trainingname : 消防灭火器的使用
     * trainingcontent : 1.拉点消火栓
     2.按下鸭下巴
     3.站在上风口对着火焰的根部喷洒
     * image : [{"image":"/images/project/20170302/20170302204227YC436280741.jpg"}]
     */

    private int id;
    private String trainingname;
    private String trainingcontent;
    private ArrayList<ImageBean>image;



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

    public String getTrainingcontent() {
        return trainingcontent;
    }

    public void setTrainingcontent(String trainingcontent) {
        this.trainingcontent = trainingcontent;
    }

    public ArrayList<ImageBean> getImage() {
        return image;
    }

    public void setImage(ArrayList<ImageBean> image) {
        this.image = image;
    }

    public static class ImageBean implements Parcelable {
        /**
         * image : /images/project/20170302/20170302204227YC436280741.jpg
         */

        private String image;


        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.image);
        }

        public ImageBean() {
        }

        protected ImageBean(Parcel in) {
            this.image = in.readString();
        }

        public static final Parcelable.Creator<ImageBean> CREATOR = new Parcelable.Creator<ImageBean>() {
            @Override
            public ImageBean createFromParcel(Parcel source) {
                return new ImageBean(source);
            }

            @Override
            public ImageBean[] newArray(int size) {
                return new ImageBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.trainingname);
        dest.writeString(this.trainingcontent);
        dest.writeTypedList(this.image);
    }

    public WorkTrainingList() {
    }

    protected WorkTrainingList(Parcel in) {
        this.id = in.readInt();
        this.trainingname = in.readString();
        this.trainingcontent = in.readString();
        this.image = in.createTypedArrayList(ImageBean.CREATOR);
    }

    public static final Parcelable.Creator<WorkTrainingList> CREATOR = new Parcelable.Creator<WorkTrainingList>() {
        @Override
        public WorkTrainingList createFromParcel(Parcel source) {
            return new WorkTrainingList(source);
        }

        @Override
        public WorkTrainingList[] newArray(int size) {
            return new WorkTrainingList[size];
        }
    };
}
