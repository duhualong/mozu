package org.eenie.wgj.model.requset;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/5/4 at 18:28
 * Email: 472279981@qq.com
 * Des:
 */

public class NoticeMessage implements Parcelable {

    /**
     * id : 1137
     * key : 2103
     * title : 巡检提醒
     * alert : 你该去巡检了
     * created_at : 2017-05-04 10:25:00
     * parameter :
     */

    private int id;
    private int key;
    private String title;
    private String alert;
    private String created_at;

//    private String parameter;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getKey() {
        return key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAlert() {
        return alert;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

//    public String getParameter() {
//        return parameter;
//    }
//
//    public void setParameter(String parameter) {
//        this.parameter = parameter;
//    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeInt(this.key);
        dest.writeString(this.title);
        dest.writeString(this.alert);
        dest.writeString(this.created_at);
    }

    public NoticeMessage() {
    }

    protected NoticeMessage(Parcel in) {
        this.id = in.readInt();
        this.key = in.readInt();
        this.title = in.readString();
        this.alert = in.readString();
        this.created_at = in.readString();
    }

    public static final Parcelable.Creator<NoticeMessage> CREATOR = new Parcelable.Creator<NoticeMessage>() {
        @Override
        public NoticeMessage createFromParcel(Parcel source) {
            return new NoticeMessage(source);
        }

        @Override
        public NoticeMessage[] newArray(int size) {
            return new NoticeMessage[size];
        }
    };
}
