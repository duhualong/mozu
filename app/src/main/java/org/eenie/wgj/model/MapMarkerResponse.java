package org.eenie.wgj.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/6/30 at 18:19
 * Email: 472279981@qq.com
 * Des:
 */

public class MapMarkerResponse implements Parcelable {
    private String inspectionname;
    private double latitude;
    private String location_name;
    private double longitude;
    private int situation;
    private String time;
    private String happening;
    private String avatarUrl;
    private int position;
    private String username;

    public MapMarkerResponse(String inspectionname, double latitude, String location_name,
                             double longitude, int situation, String time, String happening,
                             String avatarUrl, int position,String username) {
        this.inspectionname = inspectionname;
        this.latitude = latitude;
        this.location_name = location_name;
        this.longitude = longitude;
        this.situation = situation;
        this.time = time;
        this.happening = happening;
        this.avatarUrl = avatarUrl;
        this.position = position;
        this.username=username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getInspectionname() {
        return inspectionname;
    }

    public void setInspectionname(String inspectionname) {
        this.inspectionname = inspectionname;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getLocation_name() {
        return location_name;
    }

    public void setLocation_name(String location_name) {
        this.location_name = location_name;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getSituation() {
        return situation;
    }

    public void setSituation(int situation) {
        this.situation = situation;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getHappening() {
        return happening;
    }

    public void setHappening(String happening) {
        this.happening = happening;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.inspectionname);
        dest.writeDouble(this.latitude);
        dest.writeString(this.location_name);
        dest.writeDouble(this.longitude);
        dest.writeInt(this.situation);
        dest.writeString(this.time);
        dest.writeString(this.happening);
        dest.writeString(this.avatarUrl);
        dest.writeInt(this.position);
    }

    protected MapMarkerResponse(Parcel in) {
        this.inspectionname = in.readString();
        this.latitude = in.readDouble();
        this.location_name = in.readString();
        this.longitude = in.readDouble();
        this.situation = in.readInt();
        this.time = in.readString();
        this.happening = in.readString();
        this.avatarUrl = in.readString();
        this.position = in.readInt();
    }

    public static final Parcelable.Creator<MapMarkerResponse> CREATOR = new Parcelable.Creator<MapMarkerResponse>() {
        @Override
        public MapMarkerResponse createFromParcel(Parcel source) {
            return new MapMarkerResponse(source);
        }

        @Override
        public MapMarkerResponse[] newArray(int size) {
            return new MapMarkerResponse[size];
        }
    };
}
