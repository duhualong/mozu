package org.eenie.wgj.model.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/5/25 at 19:34
 * Email: 472279981@qq.com
 * Des:
 */

public class RoundWayList implements Parcelable {

    /**
     * id : 9
     * name : 文化广场4号门
     * difference : 5分钟
     */

    private int id;
    private String name;
    private String difference;

    public RoundWayList(int id, String name) {
        this.id = id;
        this.name = name;
    }

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

    public String getDifference() {
        return difference;
    }

    public void setDifference(String difference) {
        this.difference = difference;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.difference);
    }

    public RoundWayList() {
    }


    protected RoundWayList(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.difference = in.readString();
    }

    public static final Parcelable.Creator<RoundWayList> CREATOR = new Parcelable.Creator<RoundWayList>() {
        @Override
        public RoundWayList createFromParcel(Parcel source) {
            return new RoundWayList(source);
        }

        @Override
        public RoundWayList[] newArray(int size) {
            return new RoundWayList[size];
        }
    };
}
