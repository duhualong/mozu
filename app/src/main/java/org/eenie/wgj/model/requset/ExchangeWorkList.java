package org.eenie.wgj.model.requset;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/5/19 at 11:46
 * Email: 472279981@qq.com
 * Des:
 */

public class ExchangeWorkList implements Parcelable {

    /**
     * id : 151
     * mattername : 日班
     * matter : 1.注意物品的移交。
     2.晚上有台风，请注意
     3.陌陌陌陌
     4.啦啦啦啦啦啦啦
     * image : [{"image":"/images/project/20170104/20170104223419YC491481940.jpg"},{"image":"/images/project/20170104/20170104223419YC1112383826.jpg"}]
     */

    private int id;
    private String mattername;
    private String matter;
    private ArrayList<ImageBean> image;

    public ExchangeWorkList(int id, String mattername, String matter, ArrayList<ImageBean> image) {
        this.id = id;
        this.mattername = mattername;
        this.matter = matter;
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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

    public ArrayList<ImageBean> getImage() {
        return image;
    }

    public void setImage(ArrayList<ImageBean> image) {
        this.image = image;
    }

    public static class ImageBean implements Parcelable {

        /**
         * image : /images/project/20170104/20170104223419YC491481940.jpg
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
        dest.writeString(this.mattername);
        dest.writeString(this.matter);
        dest.writeTypedList(this.image);
    }

    public ExchangeWorkList() {
    }

    protected ExchangeWorkList(Parcel in) {
        this.id = in.readInt();
        this.mattername = in.readString();
        this.matter = in.readString();
        this.image = in.createTypedArrayList(ImageBean.CREATOR);
    }

    public static final Parcelable.Creator<ExchangeWorkList> CREATOR = new Parcelable.Creator<ExchangeWorkList>() {
        @Override
        public ExchangeWorkList createFromParcel(Parcel source) {
            return new ExchangeWorkList(source);
        }

        @Override
        public ExchangeWorkList[] newArray(int size) {
            return new ExchangeWorkList[size];
        }
    };
}
