package org.eenie.wgj.model.response.exchangework;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Eenie on 2017/7/31 at 16:55
 * Email: 472279981@qq.com
 * Des:
 */

public class ExchangeWorkHistoryTakeResponse implements Parcelable {

    /**
     * explanation : 周五下班前打扫办公室
     * mattername : 常日班
     * matter : 1.每天值日生必须提前20分钟到公司打扫卫生
     * 2.包括卫生间水池也需要打扫
     * created_at : 2017-07-28 11:52:37
     */

    private String explanation;
    private String mattername;
    private String matter;
    private String created_at;
    private List<ImageBean> image;
    private List<ToBean> to;
    private ToBean from;

    public ToBean getFrom() {
        return from;
    }

    public void setFrom(ToBean from) {
        this.from = from;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public List<ImageBean> getImage() {
        return image;
    }

    public void setImage(List<ImageBean> image) {
        this.image = image;
    }

    public List<ToBean> getTo() {
        return to;
    }

    public void setTo(List<ToBean> to) {
        this.to = to;
    }



    public static class ToBean implements Parcelable {
        private int user_id;
        private String name;

        public int getUser_id() {
            return user_id;
        }

        public void setUser_id(int user_id) {
            this.user_id = user_id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.user_id);
            dest.writeString(this.name);
        }

        public ToBean() {
        }

        protected ToBean(Parcel in) {
            this.user_id = in.readInt();
            this.name = in.readString();
        }

        public static final Parcelable.Creator<ToBean> CREATOR = new Parcelable.Creator<ToBean>() {
            @Override
            public ToBean createFromParcel(Parcel source) {
                return new ToBean(source);
            }

            @Override
            public ToBean[] newArray(int size) {
                return new ToBean[size];
            }
        };
    }


    public static class ImageBean implements Parcelable {

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
        dest.writeString(this.explanation);
        dest.writeString(this.mattername);
        dest.writeString(this.matter);
        dest.writeString(this.created_at);
        dest.writeTypedList(this.image);
        dest.writeTypedList(this.to);
        dest.writeParcelable(this.from, flags);
    }

    public ExchangeWorkHistoryTakeResponse() {
    }

    protected ExchangeWorkHistoryTakeResponse(Parcel in) {
        this.explanation = in.readString();
        this.mattername = in.readString();
        this.matter = in.readString();
        this.created_at = in.readString();
        this.image = in.createTypedArrayList(ImageBean.CREATOR);
        this.to = in.createTypedArrayList(ToBean.CREATOR);
        this.from = in.readParcelable(ToBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ExchangeWorkHistoryTakeResponse> CREATOR = new Parcelable.Creator<ExchangeWorkHistoryTakeResponse>() {
        @Override
        public ExchangeWorkHistoryTakeResponse createFromParcel(Parcel source) {
            return new ExchangeWorkHistoryTakeResponse(source);
        }

        @Override
        public ExchangeWorkHistoryTakeResponse[] newArray(int size) {
            return new ExchangeWorkHistoryTakeResponse[size];
        }
    };
}
