package org.eenie.wgj.model.response.meeting;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/7/10 at 15:54
 * Email: 472279981@qq.com
 * Des:
 */

public class MeetingPeople implements Parcelable {
    private String name;
    private int id;
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
        dest.writeString(this.name);
        dest.writeInt(this.id);
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
    }

    public MeetingPeople() {
    }

    protected MeetingPeople(Parcel in) {
        this.name = in.readString();
        this.id = in.readInt();
        this.checked = in.readByte() != 0;
    }

    public static final Parcelable.Creator<MeetingPeople> CREATOR = new Parcelable.Creator<MeetingPeople>() {
        @Override
        public MeetingPeople createFromParcel(Parcel source) {
            return new MeetingPeople(source);
        }

        @Override
        public MeetingPeople[] newArray(int size) {
            return new MeetingPeople[size];
        }
    };
}
