package org.eenie.wgj.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/11 at 14:54
 * Email: 472279981@qq.com
 * Des:
 */

public class TakePhotoApiResponse implements Parcelable {


    /**
     * id : 77
     * title : 东藏西躲时尚似懂非懂说分手分手分手分手分手
     * text : 发货回国的广告更好的车发广告
     thjvvt和 v 分哈哈哈个vhh
     * created_at : 2017-04-11 10:19:59
     * username : 范宝珅
     * image : [{"image":"/images/readilyShoot/20170411/20170411101959YC1010238578.jpg"},{"image":"/images/readilyShoot/20170411/20170411101959YC508030777.jpg"}]
     */

    private int id;
    private String title;
    private String text;
    private String created_at;
    private String username;
    private ArrayList<ImageBean> image;

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

    public ArrayList<ImageBean> getImage() {
        return image;
    }

    public void setImage(ArrayList<ImageBean> image) {
        this.image = image;
    }

    public static class ImageBean implements Parcelable {
        /**
         * image : /images/readilyShoot/20170411/20170411101959YC1010238578.jpg
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
        dest.writeString(this.title);
        dest.writeString(this.text);
        dest.writeString(this.created_at);
        dest.writeString(this.username);
        dest.writeTypedList(this.image);
    }

    public TakePhotoApiResponse() {
    }

    protected TakePhotoApiResponse(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.text = in.readString();
        this.created_at = in.readString();
        this.username = in.readString();
        this.image = in.createTypedArrayList(ImageBean.CREATOR);
    }

    public static final Parcelable.Creator<TakePhotoApiResponse> CREATOR = new Parcelable.Creator<TakePhotoApiResponse>() {
        @Override
        public TakePhotoApiResponse createFromParcel(Parcel source) {
            return new TakePhotoApiResponse(source);
        }

        @Override
        public TakePhotoApiResponse[] newArray(int size) {
            return new TakePhotoApiResponse[size];
        }
    };
}
