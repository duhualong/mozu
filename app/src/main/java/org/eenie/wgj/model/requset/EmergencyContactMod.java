package org.eenie.wgj.model.requset;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/4/19 at 11:15
 * Email: 472279981@qq.com
 * Des:
 */
public class EmergencyContactMod implements Parcelable {
    private String name;
    private String relation;
    private String phone;


    public EmergencyContactMod(String name, String relation, String phone) {
        this.name = name;
        this.relation = relation;
        this.phone = phone;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.relation);
        dest.writeString(this.phone);
    }

    public EmergencyContactMod() {

    }

    protected EmergencyContactMod(Parcel in) {
        this.name = in.readString();
        this.relation = in.readString();
        this.phone = in.readString();
    }

    public static final Parcelable.Creator<EmergencyContactMod> CREATOR = new Parcelable.Creator<EmergencyContactMod>() {
        @Override
        public EmergencyContactMod createFromParcel(Parcel source) {
            return new EmergencyContactMod(source);
        }

        @Override
        public EmergencyContactMod[] newArray(int size) {
            return new EmergencyContactMod[size];
        }
    };


    @Override
    public String toString() {
        return String.format("%1s/%2s/%3s", name, relation, phone);
    }

}
