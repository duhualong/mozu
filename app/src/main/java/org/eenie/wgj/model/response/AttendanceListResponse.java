package org.eenie.wgj.model.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/6/13 at 14:13
 * Email: 472279981@qq.com
 * Des:
 */

public class AttendanceListResponse implements Parcelable {
    /**
     * day : 2017-06-09
     * checkin : {"type":3,"address":"sdafdsaf","time":"09:43","attendance":"迟到"}
     * signback : {"type":1,"address":"上海市黄浦区半淞园路街道苗江路848号","time":"18:20","attendance":"早退"}
     * service : {"id":132,"servicesname":"日班","starttime":"09:00:00","endtime":"19:30:00","time":10.5}
     */

    private String day;
    private CheckinBean checkin;
    private SignbackBean signback;
    private ServiceBean service;


    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public CheckinBean getCheckin() {
        return checkin;
    }

    public void setCheckin(CheckinBean checkin) {
        this.checkin = checkin;
    }

    public SignbackBean getSignback() {
        return signback;
    }

    public void setSignback(SignbackBean signback) {
        this.signback = signback;
    }

    public ServiceBean getService() {
        return service;
    }

    public void setService(ServiceBean service) {
        this.service = service;
    }

    public static class CheckinBean implements Parcelable {
        /**
         * type : 3
         * address : sdafdsaf
         * time : 09:43
         * attendance : 迟到
         */
        private int type;
        private String address;
        private String time;
        private String attendance;


        public int getType() {
            return type;
        }
        public void setType(int type) {
            this.type = type;
        }
        public String getAddress() {
            return address;
        }
        public void setAddress(String address) {
            this.address = address;
        }
        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
        public String getAttendance() {
            return attendance;
        }

        public void setAttendance(String attendance) {
            this.attendance = attendance;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.type);
            dest.writeString(this.address);
            dest.writeString(this.time);
            dest.writeString(this.attendance);
        }

        public CheckinBean() {
        }

        protected CheckinBean(Parcel in) {
            this.type = in.readInt();
            this.address = in.readString();
            this.time = in.readString();
            this.attendance = in.readString();
        }

        public static final Parcelable.Creator<CheckinBean> CREATOR = new Parcelable.Creator<CheckinBean>() {
            @Override
            public CheckinBean createFromParcel(Parcel source) {
                return new CheckinBean(source);
            }

            @Override
            public CheckinBean[] newArray(int size) {
                return new CheckinBean[size];
            }
        };
    }

    public static class SignbackBean implements Parcelable {
        /**
         * type : 1
         * address : 上海市黄浦区半淞园路街道苗江路848号
         * time : 18:20
         * attendance : 早退
         */
        private int type;
        private String address;
        private String time;
        private String attendance;


        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getAttendance() {
            return attendance;
        }

        public void setAttendance(String attendance) {
            this.attendance = attendance;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.type);
            dest.writeString(this.address);
            dest.writeString(this.time);
            dest.writeString(this.attendance);
        }

        public SignbackBean() {
        }

        protected SignbackBean(Parcel in) {
            this.type = in.readInt();
            this.address = in.readString();
            this.time = in.readString();
            this.attendance = in.readString();
        }

        public static final Parcelable.Creator<SignbackBean> CREATOR = new Parcelable.Creator<SignbackBean>() {
            @Override
            public SignbackBean createFromParcel(Parcel source) {
                return new SignbackBean(source);
            }

            @Override
            public SignbackBean[] newArray(int size) {
                return new SignbackBean[size];
            }
        };
    }
    public static class ServiceBean implements Parcelable {
        /**
         * id : 132
         * servicesname : 日班
         * starttime : 09:00:00
         * endtime : 19:30:00
         * time : 10.5
         */

        private int id;
        private String servicesname;
        private String starttime;
        private String endtime;
        private double time;


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

        public double getTime() {
            return time;
        }

        public void setTime(double time) {
            this.time = time;
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
            dest.writeDouble(this.time);
        }

        public ServiceBean() {
        }

        protected ServiceBean(Parcel in) {
            this.id = in.readInt();
            this.servicesname = in.readString();
            this.starttime = in.readString();
            this.endtime = in.readString();
            this.time = in.readDouble();
        }

        public static final Parcelable.Creator<ServiceBean> CREATOR = new Parcelable.Creator<ServiceBean>() {
            @Override
            public ServiceBean createFromParcel(Parcel source) {
                return new ServiceBean(source);
            }

            @Override
            public ServiceBean[] newArray(int size) {
                return new ServiceBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.day);
        dest.writeParcelable(this.checkin, flags);
        dest.writeParcelable(this.signback, flags);
        dest.writeParcelable(this.service, flags);
    }

    public AttendanceListResponse() {
    }

    protected AttendanceListResponse(Parcel in) {
        this.day = in.readString();
        this.checkin = in.readParcelable(CheckinBean.class.getClassLoader());
        this.signback = in.readParcelable(SignbackBean.class.getClassLoader());
        this.service = in.readParcelable(ServiceBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<AttendanceListResponse> CREATOR = new Parcelable.Creator<AttendanceListResponse>() {
        @Override
        public AttendanceListResponse createFromParcel(Parcel source) {
            return new AttendanceListResponse(source);
        }

        @Override
        public AttendanceListResponse[] newArray(int size) {
            return new AttendanceListResponse[size];
        }
    };
}
