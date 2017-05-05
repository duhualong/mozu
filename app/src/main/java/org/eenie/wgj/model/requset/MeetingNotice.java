package org.eenie.wgj.model.requset;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/5/4 at 9:39
 * Email: 472279981@qq.com
 * Des:
 */

public class MeetingNotice implements Parcelable {


    /**
     * id : 32
     * name : 12312
     * username : 范宝珅
     * room_name : B203
     * checkstatus : 1    1代表通过2表示待审核3表示拒绝
     * start : 2017-04-17 15:32:00
     * end : 2017-04-17 18:32:00
     * created_at : 2017-04-17 15:32:45
     * checkstatus_name : 批准
     */

    private int id;
    private String name;
    private String username;
    private String room_name;
    private int checkstatus;
    private String start;
    private String end;
    private String created_at;
    private String checkstatus_name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRoom_name() {
        return room_name;
    }

    public void setRoom_name(String room_name) {
        this.room_name = room_name;
    }

    public int getCheckstatus() {
        return checkstatus;
    }

    public void setCheckstatus(int checkstatus) {
        this.checkstatus = checkstatus;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getCheckstatus_name() {
        return checkstatus_name;
    }

    public void setCheckstatus_name(String checkstatus_name) {
        this.checkstatus_name = checkstatus_name;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.username);
        dest.writeString(this.room_name);
        dest.writeInt(this.checkstatus);
        dest.writeString(this.start);
        dest.writeString(this.end);
        dest.writeString(this.created_at);
        dest.writeString(this.checkstatus_name);
    }

    public MeetingNotice() {
    }

    protected MeetingNotice(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.username = in.readString();
        this.room_name = in.readString();
        this.checkstatus = in.readInt();
        this.start = in.readString();
        this.end = in.readString();
        this.created_at = in.readString();
        this.checkstatus_name = in.readString();
    }

    public static final Creator<MeetingNotice> CREATOR = new Creator<MeetingNotice>() {
        @Override
        public MeetingNotice createFromParcel(Parcel source) {
            return new MeetingNotice(source);
        }

        @Override
        public MeetingNotice[] newArray(int size) {
            return new MeetingNotice[size];
        }
    };
}
