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
    private int service_id;

    private String servicesname;
    private String offduty_time;
    private String onduty_time;

    public int getService_id() {
        return service_id;
    }

    public void setService_id(int service_id) {
        this.service_id = service_id;
    }

    public String getServicesname() {
        return servicesname;
    }

    public void setServicesname(String servicesname) {
        this.servicesname = servicesname;
    }

    public String getOffduty_time() {
        return offduty_time;
    }

    public void setOffduty_time(String offduty_time) {
        this.offduty_time = offduty_time;
    }

    public String getOnduty_time() {
        return onduty_time;
    }

    public void setOnduty_time(String onduty_time) {
        this.onduty_time = onduty_time;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.service_id);
        dest.writeString(this.servicesname);
        dest.writeString(this.offduty_time);
        dest.writeString(this.onduty_time);
    }

    public SignOutInfor() {
    }

    protected SignOutInfor(Parcel in) {
        this.service_id = in.readInt();
        this.servicesname = in.readString();
        this.offduty_time = in.readString();
        this.onduty_time = in.readString();
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
