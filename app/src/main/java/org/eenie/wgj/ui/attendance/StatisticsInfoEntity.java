package org.eenie.wgj.ui.attendance;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/6/13 at 16:55
 * Email: 472279981@qq.com
 * Des:
 */

public class StatisticsInfoEntity {


    public static class ResultMessageBean implements Parcelable {
        /**
         * date : 2016-12-19
         * checkin : {"image":"/images/attendance/20161219/20161219143828YC797024118.jpg","type":1,"late":"迟到","address":"上海市黄浦区茂名南路靠近上海文化广场剧场"}
         * signback : {"image":"/images/attendance/20161219/20161219143844YC1739919519.jpg","type":1,"late":"早退","address":"上海市黄浦区茂名南路靠近上海文化广场剧场"}
         * stateDes : 异常
         * stateCode : 1
         * service : {"id":119,"servicesname":"常日班","starttime":3600,"endtime":32400}
         */

        private String date;
        private CheckinBean checkin;
        private SignbackBean signback;
        private String stateDes;
        private int stateCode;
        private ServiceBean service;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
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

        public String getStateDes() {
            return stateDes;
        }

        public void setStateDes(String stateDes) {
            this.stateDes = stateDes;
        }

        public int getStateCode() {
            return stateCode;
        }

        public void setStateCode(int stateCode) {
            this.stateCode = stateCode;
        }

        public ServiceBean getService() {
            return service;
        }

        public void setService(ServiceBean service) {
            this.service = service;
        }

        public static class CheckinBean implements Parcelable {
            /**
             * image : /images/attendance/20161219/20161219143828YC797024118.jpg
             * type : 1
             * late : 迟到
             * address : 上海市黄浦区茂名南路靠近上海文化广场剧场
             */

            private String image;
            private int type;
            private int late;
            private String address;
            private String status;
            private String description;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getLate() {
                return late;
            }

            public void setLate(int late) {
                this.late = late;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.image);
                dest.writeInt(this.type);
                dest.writeInt(this.late);
                dest.writeString(this.address);
                dest.writeString(this.status);
                dest.writeString(this.description);
            }

            public CheckinBean() {
            }

            protected CheckinBean(Parcel in) {
                this.image = in.readString();
                this.type = in.readInt();
                this.late = in.readInt();
                this.address = in.readString();
                this.status = in.readString();
                this.description = in.readString();
            }

            public static final Creator<CheckinBean> CREATOR = new Creator<CheckinBean>() {
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
             * image : /images/attendance/20161219/20161219143844YC1739919519.jpg
             * type : 1
             * late : 早退
             * address : 上海市黄浦区茂名南路靠近上海文化广场剧场
             */

            private String image;
            private int type;
            private int late;
            private String address;
            private String description;
            private String status;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

            public int getType() {
                return type;
            }

            public void setType(int type) {
                this.type = type;
            }

            public int getLate() {
                return late;
            }

            public void setLate(int late) {
                this.late = late;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }


            public String getDescription() {
                return description;
            }

            public void setDescription(String description) {
                this.description = description;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.image);
                dest.writeInt(this.type);
                dest.writeInt(this.late);
                dest.writeString(this.address);
                dest.writeString(this.description);
                dest.writeString(this.status);
            }

            public SignbackBean() {
            }

            protected SignbackBean(Parcel in) {
                this.image = in.readString();
                this.type = in.readInt();
                this.late = in.readInt();
                this.address = in.readString();
                this.description = in.readString();
                this.status = in.readString();
            }

            public static final Creator<SignbackBean> CREATOR = new Creator<SignbackBean>() {
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
             * id : 119
             * servicesname : 常日班
             * starttime : 3600
             * endtime : 32400
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

            public ServiceBean() {
            }

            protected ServiceBean(Parcel in) {
                this.id = in.readInt();
                this.servicesname = in.readString();
                this.starttime = in.readString();
                this.endtime = in.readString();
            }

            public static final Creator<ServiceBean> CREATOR = new Creator<ServiceBean>() {
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
            dest.writeString(this.date);
            dest.writeParcelable(this.checkin, flags);
            dest.writeParcelable(this.signback, flags);
            dest.writeString(this.stateDes);
            dest.writeInt(this.stateCode);
            dest.writeParcelable(this.service, flags);
        }

        public ResultMessageBean() {
        }

        protected ResultMessageBean(Parcel in) {
            this.date = in.readString();
            this.checkin = in.readParcelable(CheckinBean.class.getClassLoader());
            this.signback = in.readParcelable(SignbackBean.class.getClassLoader());
            this.stateDes = in.readString();
            this.stateCode = in.readInt();
            this.service = in.readParcelable(ServiceBean.class.getClassLoader());
        }

        public static final Parcelable.Creator<ResultMessageBean> CREATOR = new Parcelable.Creator<ResultMessageBean>() {
            @Override
            public ResultMessageBean createFromParcel(Parcel source) {
                return new ResultMessageBean(source);
            }

            @Override
            public ResultMessageBean[] newArray(int size) {
                return new ResultMessageBean[size];
            }
        };
    }
}
