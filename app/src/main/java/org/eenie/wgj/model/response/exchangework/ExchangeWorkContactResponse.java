package org.eenie.wgj.model.response.exchangework;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/7/13 at 15:36
 * Email: 472279981@qq.com
 * Des:
 */

public class ExchangeWorkContactResponse implements Parcelable {

    /**
     * id : 3
     * name : 刘德华
     * position : 项目经理
     */

    private int id;
    private String name;
    private String position;
    private boolean checked;

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
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

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.position);
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
    }

    public ExchangeWorkContactResponse() {
    }

    protected ExchangeWorkContactResponse(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.position = in.readString();
        this.checked = in.readByte() != 0;
    }

    public static final Parcelable.Creator<ExchangeWorkContactResponse> CREATOR = new Parcelable.Creator<ExchangeWorkContactResponse>() {
        @Override
        public ExchangeWorkContactResponse createFromParcel(Parcel source) {
            return new ExchangeWorkContactResponse(source);
        }

        @Override
        public ExchangeWorkContactResponse[] newArray(int size) {
            return new ExchangeWorkContactResponse[size];
        }
    };
}
