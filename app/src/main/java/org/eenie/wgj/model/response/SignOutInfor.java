package org.eenie.wgj.model.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/6/19 at 11:46
 * Email: 472279981@qq.com
 *
 * Des:
 */

public class SignOutInfor implements Parcelable {

    /**
     * id : 168 班次id
     * servicesname : 日班 //班次名字
     */
    private int id;
    private String servicesname;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getServicesname() {
        return servicesname;
    }

    public void setServicesname(String servicesname) {
        this.servicesname = servicesname;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.servicesname);
    }

    public SignOutInfor() {
    }

    protected SignOutInfor(Parcel in) {
        this.id = in.readInt();
        this.servicesname = in.readString();
    }

    public static final Parcelable.Creator<SignOutInfor> CREATOR = new Parcelable.Creator<SignOutInfor>() {
        @Override
        public SignOutInfor createFromParcel(Parcel source) {
            return new SignOutInfor(source);
        }

        @Override
        public SignOutInfor[] newArray(int size) {
            return new SignOutInfor[size];
        }
    };
}
