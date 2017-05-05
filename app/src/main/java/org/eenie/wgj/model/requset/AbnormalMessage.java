package org.eenie.wgj.model.requset;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/5/5 at 14:28
 * Email: 472279981@qq.com
 * Des:
 */

public class AbnormalMessage  implements Parcelable {

    /**
     * id : 17
     * title : 阿尔
     * text : going
     * username : 范宝珅
     * created_at : 2017-03-28 18:41:24
     * image : [{"image":"/images/readilyShoot/20170328/20170328184124YC545855735.jpg"},{"image":"/images/readilyShoot/20170328/20170328184124YC590157187.jpg"},{"image":"/images/readilyShoot/20170328/20170328184124YC232662651.jpg"},{"image":"/images/readilyShoot/20170328/20170328184124YC44075790.jpg"}]
     */

    private int id;
    private String title;
    private String text;
    private String username;
    private String created_at;
    private ArrayList<ImageBean> image;

    public ArrayList<ImageBean> getImage() {
        return image;
    }

    public void setImage(ArrayList<ImageBean> image) {
        this.image = image;
    }

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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }


    public static class ImageBean implements Parcelable {
        /**
         * image : /images/readilyShoot/20170328/20170328184124YC545855735.jpg
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

        public static final Creator<ImageBean> CREATOR = new Creator<ImageBean>() {
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
        dest.writeString(this.username);
        dest.writeString(this.created_at);
        dest.writeList(this.image);
    }

    public AbnormalMessage() {
    }

    protected AbnormalMessage(Parcel in) {
        this.id = in.readInt();
        this.title = in.readString();
        this.text = in.readString();
        this.username = in.readString();
        this.created_at = in.readString();
        this.image = new ArrayList<ImageBean>();
        in.readList(this.image, ImageBean.class.getClassLoader());
    }

    public static final Creator<AbnormalMessage> CREATOR = new Creator<AbnormalMessage>() {
        @Override
        public AbnormalMessage createFromParcel(Parcel source) {
            return new AbnormalMessage(source);
        }

        @Override
        public AbnormalMessage[] newArray(int size) {
            return new AbnormalMessage[size];
        }
    };
}
