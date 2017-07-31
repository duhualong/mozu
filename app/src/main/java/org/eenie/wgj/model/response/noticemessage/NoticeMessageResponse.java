package org.eenie.wgj.model.response.noticemessage;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/7/31 at 13:40
 * Email: 472279981@qq.com
 * Des:
 */

public class NoticeMessageResponse implements Parcelable {
    private String title;
    private String date;
    private String content;
    private String notice_people;
    private int id;

    public NoticeMessageResponse(String title, String date, String content,
                                 String notice_people, int id) {
        this.title = title;
        this.date = date;
        this.content = content;
        this.notice_people = notice_people;
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getNotice_people() {
        return notice_people;
    }

    public void setNotice_people(String notice_people) {
        this.notice_people = notice_people;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.title);
        dest.writeString(this.date);
        dest.writeString(this.content);
        dest.writeString(this.notice_people);
        dest.writeInt(this.id);
    }

    protected NoticeMessageResponse(Parcel in) {
        this.title = in.readString();
        this.date = in.readString();
        this.content = in.readString();
        this.notice_people = in.readString();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<NoticeMessageResponse> CREATOR = new Parcelable.Creator<NoticeMessageResponse>() {
        @Override
        public NoticeMessageResponse createFromParcel(Parcel source) {
            return new NoticeMessageResponse(source);
        }

        @Override
        public NoticeMessageResponse[] newArray(int size) {
            return new NoticeMessageResponse[size];
        }
    };
}
