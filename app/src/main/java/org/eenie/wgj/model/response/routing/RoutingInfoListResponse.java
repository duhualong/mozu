package org.eenie.wgj.model.response.routing;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/7/3 at 9:05
 * Email: 472279981@qq.com
 * Des:
 */

public class RoutingInfoListResponse implements Parcelable {
    private String date;
    private UserBean user;
    private RateBean rate;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public RateBean getRate() {
        return rate;
    }

    public void setRate(RateBean rate) {
        this.rate = rate;
    }

    public static class UserBean implements Parcelable {
        private int id;
        private String name;


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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
        }

        public UserBean() {
        }

        protected UserBean(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
        }

        public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
            @Override
            public UserBean createFromParcel(Parcel source) {
                return new UserBean(source);
            }

            @Override
            public UserBean[] newArray(int size) {
                return new UserBean[size];
            }
        };
    }

    public static class RateBean implements Parcelable {
        private double turn;
        private int turn_total;
        private int turn_actual;
        private double number;
        private int number_actual;
        private int ring;
        private int turn_ring;

        public double getTurn() {
            return turn;
        }

        public void setTurn(double turn) {
            this.turn = turn;
        }

        public int getTurn_total() {
            return turn_total;
        }

        public void setTurn_total(int turn_total) {
            this.turn_total = turn_total;
        }

        public int getTurn_actual() {
            return turn_actual;
        }

        public void setTurn_actual(int turn_actual) {
            this.turn_actual = turn_actual;
        }

        public double getNumber() {
            return number;
        }

        public void setNumber(double number) {
            this.number = number;
        }

        public int getNumber_actual() {
            return number_actual;
        }

        public void setNumber_actual(int number_actual) {
            this.number_actual = number_actual;
        }

        public int getRing() {
            return ring;
        }

        public void setRing(int ring) {
            this.ring = ring;
        }

        public int getTurn_ring() {
            return turn_ring;
        }

        public void setTurn_ring(int turn_ring) {
            this.turn_ring = turn_ring;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(this.turn);
            dest.writeInt(this.turn_total);
            dest.writeInt(this.turn_actual);
            dest.writeDouble(this.number);
            dest.writeInt(this.number_actual);
            dest.writeInt(this.ring);
            dest.writeInt(this.turn_ring);
        }

        public RateBean() {
        }

        protected RateBean(Parcel in) {
            this.turn = in.readDouble();
            this.turn_total = in.readInt();
            this.turn_actual = in.readInt();
            this.number = in.readDouble();
            this.number_actual = in.readInt();
            this.ring = in.readInt();
            this.turn_ring = in.readInt();
        }

        public static final Parcelable.Creator<RateBean> CREATOR = new Parcelable.Creator<RateBean>() {
            @Override
            public RateBean createFromParcel(Parcel source) {
                return new RateBean(source);
            }

            @Override
            public RateBean[] newArray(int size) {
                return new RateBean[size];
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
        dest.writeParcelable(this.user, flags);
        dest.writeParcelable(this.rate, flags);
    }

    public RoutingInfoListResponse() {
    }

    protected RoutingInfoListResponse(Parcel in) {
        this.date = in.readString();
        this.user = in.readParcelable(UserBean.class.getClassLoader());
        this.rate = in.readParcelable(RateBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<RoutingInfoListResponse> CREATOR = new Parcelable.Creator<RoutingInfoListResponse>() {
        @Override
        public RoutingInfoListResponse createFromParcel(Parcel source) {
            return new RoutingInfoListResponse(source);
        }

        @Override
        public RoutingInfoListResponse[] newArray(int size) {
            return new RoutingInfoListResponse[size];
        }
    };
}
