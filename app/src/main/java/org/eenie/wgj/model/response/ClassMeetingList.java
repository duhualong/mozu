package org.eenie.wgj.model.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/5/22 at 9:01
 * Email: 472279981@qq.com
 * Des:
 */

public class ClassMeetingList implements Parcelable {

    /**
     * id : 131
     * servicesname : 常日班
     * starttime : 09:00
     * endtime : 17:00
     */

    private int id;
    private String servicesname;
    private String starttime;
    private String endtime;


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

    public String getStarttime() {
        return starttime;
    }

    public void setStarttime(String starttime) {
        this.starttime = starttime;
    }

    public String getEndtime() {
        return endtime;
    }

    public void setEndtime(String endtime) {
        this.endtime = endtime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.servicesname);
        dest.writeString(this.starttime);
        dest.writeString(this.endtime);
    }

    public ClassMeetingList() {
    }

    protected ClassMeetingList(Parcel in) {
        this.id = in.readInt();
        this.servicesname = in.readString();
        this.starttime = in.readString();
        this.endtime = in.readString();
    }

    public static final Parcelable.Creator<ClassMeetingList> CREATOR = new Parcelable.Creator<ClassMeetingList>() {
        @Override
        public ClassMeetingList createFromParcel(Parcel source) {
            return new ClassMeetingList(source);
        }

        @Override
        public ClassMeetingList[] newArray(int size) {
            return new ClassMeetingList[size];
        }
    };
}
