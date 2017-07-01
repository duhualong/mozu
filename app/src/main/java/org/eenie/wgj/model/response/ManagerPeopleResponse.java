package org.eenie.wgj.model.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/6/27 at 11:46
 * Email: 472279981@qq.com
 * Des:
 */

public class ManagerPeopleResponse implements Parcelable {
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

    public ManagerPeopleResponse() {
    }

    protected ManagerPeopleResponse(Parcel in) {
        this.name = in.readString();
        this.id = in.readInt();
        this.checked = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ManagerPeopleResponse> CREATOR = new Parcelable.Creator<ManagerPeopleResponse>() {
        @Override
        public ManagerPeopleResponse createFromParcel(Parcel source) {
            return new ManagerPeopleResponse(source);
        }

        @Override
        public ManagerPeopleResponse[] newArray(int size) {
            return new ManagerPeopleResponse[size];
        }
    };
}
