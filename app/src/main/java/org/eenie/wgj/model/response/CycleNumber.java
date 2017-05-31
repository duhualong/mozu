package org.eenie.wgj.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/5/26 at 11:47
 * Email: 472279981@qq.com
 * Des:
 */

public class CycleNumber implements Parcelable {
private  int inspectiondayid;
    private ArrayList<Info> info;

    public int getInspectiondayid() {
        return inspectiondayid;
    }

    public void setInspectiondayid(int inspectiondayid) {
        this.inspectiondayid = inspectiondayid;
    }

    public ArrayList<Info> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<Info> info) {
        this.info = info;
    }

    public static class Info implements Parcelable {

        /**
         * id : 239
         * inspectiondayid : 68
         * inspectionpoint : 105
         * inspectiontime : 14:46
         * inspectionname : 105商户二装
         */

        private int id;
        private int inspectiondayid;
        private int inspectionpoint;
        private String inspectiontime;
        private String inspectionname;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getInspectiondayid() {
            return inspectiondayid;
        }

        public void setInspectiondayid(int inspectiondayid) {
            this.inspectiondayid = inspectiondayid;
        }

        public int getInspectionpoint() {
            return inspectionpoint;
        }

        public void setInspectionpoint(int inspectionpoint) {
            this.inspectionpoint = inspectionpoint;
        }

        public String getInspectiontime() {
            return inspectiontime;
        }

        public void setInspectiontime(String inspectiontime) {
            this.inspectiontime = inspectiontime;
        }

        public String getInspectionname() {
            return inspectionname;
        }

        public void setInspectionname(String inspectionname) {
            this.inspectionname = inspectionname;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.inspectiondayid);
            dest.writeInt(this.inspectionpoint);
            dest.writeString(this.inspectiontime);
            dest.writeString(this.inspectionname);
        }

        public Info() {
        }

        protected Info(Parcel in) {
            this.id = in.readInt();
            this.inspectiondayid = in.readInt();
            this.inspectionpoint = in.readInt();
            this.inspectiontime = in.readString();
            this.inspectionname = in.readString();
        }

        public static final Parcelable.Creator<Info> CREATOR = new Parcelable.Creator<Info>() {
            @Override
            public Info createFromParcel(Parcel source) {
                return new Info(source);
            }

            @Override
            public Info[] newArray(int size) {
                return new Info[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.inspectiondayid);
        dest.writeTypedList(this.info);
    }

    public CycleNumber() {
    }

    protected CycleNumber(Parcel in) {
        this.inspectiondayid = in.readInt();
        this.info = in.createTypedArrayList(Info.CREATOR);
    }

    public static final Parcelable.Creator<CycleNumber> CREATOR = new Parcelable.Creator<CycleNumber>() {
        @Override
        public CycleNumber createFromParcel(Parcel source) {
            return new CycleNumber(source);
        }

        @Override
        public CycleNumber[] newArray(int size) {
            return new CycleNumber[size];
        }
    };
}
