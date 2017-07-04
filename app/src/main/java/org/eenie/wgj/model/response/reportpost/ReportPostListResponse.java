package org.eenie.wgj.model.response.reportpost;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/7/3 at 17:10
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportPostListResponse implements Parcelable {
    private String id;
    private String post;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.post);
    }

    public ReportPostListResponse() {
    }

    protected ReportPostListResponse(Parcel in) {
        this.id = in.readString();
        this.post = in.readString();
    }

    public static final Parcelable.Creator<ReportPostListResponse> CREATOR = new Parcelable.Creator<ReportPostListResponse>() {
        @Override
        public ReportPostListResponse createFromParcel(Parcel source) {
            return new ReportPostListResponse(source);
        }

        @Override
        public ReportPostListResponse[] newArray(int size) {
            return new ReportPostListResponse[size];
        }
    };
}
