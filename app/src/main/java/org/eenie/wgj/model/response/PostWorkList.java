package org.eenie.wgj.model.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/5/24 at 10:41
 * Email: 472279981@qq.com
 * Des:
 */

public class PostWorkList implements Parcelable {
    private int id;
    private String post;
    private String jobassignment;

    public PostWorkList(int id, String post, String jobassignment) {
        this.id = id;
        this.post = post;
        this.jobassignment = jobassignment;
    }

    public PostWorkList(int id, String post) {
        this.id = id;
        this.post = post;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPost() {
        return post;
    }

    public void setPost(String post) {
        this.post = post;
    }

    public String getJobassignment() {
        return jobassignment;
    }

    public void setJobassignment(String jobassignment) {
        this.jobassignment = jobassignment;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.post);
        dest.writeString(this.jobassignment);
    }

    protected PostWorkList(Parcel in) {
        this.id = in.readInt();
        this.post = in.readString();
        this.jobassignment = in.readString();
    }

    public static final Parcelable.Creator<PostWorkList> CREATOR = new Parcelable.Creator<PostWorkList>() {
        @Override
        public PostWorkList createFromParcel(Parcel source) {
            return new PostWorkList(source);
        }

        @Override
        public PostWorkList[] newArray(int size) {
            return new PostWorkList[size];
        }
    };
}
