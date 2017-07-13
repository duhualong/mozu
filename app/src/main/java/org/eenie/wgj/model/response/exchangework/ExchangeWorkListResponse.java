package org.eenie.wgj.model.response.exchangework;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/7/13 at 14:18
 * Email: 472279981@qq.com
 * Des:
 */

public class ExchangeWorkListResponse implements Parcelable {
    private String id;
    private String name;

    public String getId() {
        return id;
    }

    public void setId(String id) {
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
        dest.writeString(this.id);
        dest.writeString(this.name);
    }

    public ExchangeWorkListResponse() {
    }

    protected ExchangeWorkListResponse(Parcel in) {
        this.id = in.readString();
        this.name = in.readString();
    }

    public static final Parcelable.Creator<ExchangeWorkListResponse> CREATOR =
            new Parcelable.Creator<ExchangeWorkListResponse>() {
        @Override
        public ExchangeWorkListResponse createFromParcel(Parcel source) {
            return new ExchangeWorkListResponse(source);
        }

        @Override
        public ExchangeWorkListResponse[] newArray(int size) {
            return new ExchangeWorkListResponse[size];
        }
    };
}
