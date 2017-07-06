package org.eenie.wgj.model.response.reportpost;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/7/6 at 10:42
 * Email: 472279981@qq.com
 * Des:
 */

public class NoReportMonthStatisticResponse implements Parcelable {
    private String date;
    private ArrayList<InfoBean>info;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(ArrayList<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean implements Parcelable {
        private String post;
        private String time;

        public String getPost() {
            return post;
        }

        public void setPost(String post) {
            this.post = post;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.post);
            dest.writeString(this.time);
        }

        public InfoBean() {
        }

        protected InfoBean(Parcel in) {
            this.post = in.readString();
            this.time = in.readString();
        }

        public static final Parcelable.Creator<InfoBean> CREATOR = new Parcelable.Creator<InfoBean>() {
            @Override
            public InfoBean createFromParcel(Parcel source) {
                return new InfoBean(source);
            }

            @Override
            public InfoBean[] newArray(int size) {
                return new InfoBean[size];
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
        dest.writeTypedList(this.info);
    }

    public NoReportMonthStatisticResponse() {
    }

    protected NoReportMonthStatisticResponse(Parcel in) {
        this.date = in.readString();
        this.info = in.createTypedArrayList(InfoBean.CREATOR);
    }

    public static final Parcelable.Creator<NoReportMonthStatisticResponse> CREATOR = new Parcelable.Creator<NoReportMonthStatisticResponse>() {
        @Override
        public NoReportMonthStatisticResponse createFromParcel(Parcel source) {
            return new NoReportMonthStatisticResponse(source);
        }

        @Override
        public NoReportMonthStatisticResponse[] newArray(int size) {
            return new NoReportMonthStatisticResponse[size];
        }
    };
}
