package org.eenie.wgj.model.response.sortclass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/6/8 at 18:54
 * Email: 472279981@qq.com
 * Des:
 */

public class AddPersonalSortClass implements Parcelable {
    private int userId;
    private String userName;
    private int addDay;
    private  int totalDay;
    private int serviceId;
    private boolean checked;


    public AddPersonalSortClass(int userId, String userName, int addDay, int totalDay, int serviceId) {
        this.userId = userId;
        this.userName = userName;
        this.addDay = addDay;
        this.totalDay = totalDay;
        this.serviceId = serviceId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getAddDay() {
        return addDay;
    }

    public void setAddDay(int addDay) {
        this.addDay = addDay;
    }

    public int getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(int totalDay) {
        this.totalDay = totalDay;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.userId);
        dest.writeString(this.userName);
        dest.writeInt(this.addDay);
        dest.writeInt(this.totalDay);
        dest.writeInt(this.serviceId);
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
    }

    protected AddPersonalSortClass(Parcel in) {
        this.userId = in.readInt();
        this.userName = in.readString();
        this.addDay = in.readInt();
        this.totalDay = in.readInt();
        this.serviceId = in.readInt();
        this.checked = in.readByte() != 0;
    }

    public static final Parcelable.Creator<AddPersonalSortClass> CREATOR = new Parcelable.Creator<AddPersonalSortClass>() {
        @Override
        public AddPersonalSortClass createFromParcel(Parcel source) {
            return new AddPersonalSortClass(source);
        }

        @Override
        public AddPersonalSortClass[] newArray(int size) {
            return new AddPersonalSortClass[size];
        }
    };
}
