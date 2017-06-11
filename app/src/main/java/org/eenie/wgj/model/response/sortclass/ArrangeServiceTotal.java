package org.eenie.wgj.model.response.sortclass;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/6/7 at 15:09
 * Email: 472279981@qq.com
 * Des:
 */

public class ArrangeServiceTotal implements Parcelable {

    private int serviceId;
    private String serviceName;
    private String servicePeople;
    private ArrayList<ArrangeClassUserResponse> user;


    public ArrangeServiceTotal(int serviceId, String serviceName, String servicePeople) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.servicePeople = servicePeople;
    }

    public ArrangeServiceTotal(int serviceId, String serviceName, String servicePeople,
                               ArrayList<ArrangeClassUserResponse> user) {
        this.serviceId = serviceId;
        this.serviceName = serviceName;
        this.servicePeople = servicePeople;
        this.user = user;
    }

    public int getServiceId() {
        return serviceId;
    }

    public void setServiceId(int serviceId) {
        this.serviceId = serviceId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getServicePeople() {
        return servicePeople;
    }

    public void setServicePeople(String servicePeople) {
        this.servicePeople = servicePeople;
    }

    public ArrayList<ArrangeClassUserResponse> getUser() {
        return user;
    }

    public void setUser(ArrayList<ArrangeClassUserResponse> user) {
        this.user = user;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.serviceId);
        dest.writeString(this.serviceName);
        dest.writeString(this.servicePeople);
        dest.writeTypedList(this.user);
    }

    protected ArrangeServiceTotal(Parcel in) {
        this.serviceId = in.readInt();
        this.serviceName = in.readString();
        this.servicePeople = in.readString();
        this.user = in.createTypedArrayList(ArrangeClassUserResponse.CREATOR);
    }

    public static final Parcelable.Creator<ArrangeServiceTotal> CREATOR = new Parcelable.Creator<ArrangeServiceTotal>() {
        @Override
        public ArrangeServiceTotal createFromParcel(Parcel source) {
            return new ArrangeServiceTotal(source);
        }

        @Override
        public ArrangeServiceTotal[] newArray(int size) {
            return new ArrangeServiceTotal[size];
        }
    };
}
