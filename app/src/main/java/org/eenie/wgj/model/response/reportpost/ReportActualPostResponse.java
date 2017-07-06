package org.eenie.wgj.model.response.reportpost;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;



/**
 * Created by Eenie on 2017/7/6 at 13:29
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportActualPostResponse implements Parcelable {
    private String date;
    private ArrayList<PostsBean>posts;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<PostsBean> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<PostsBean> posts) {
        this.posts = posts;
    }

    public static class PostsBean implements Parcelable {
        private int id;
        private String project_id;
        private String user_id;
        private String postsetting_id;
        private int situation;
        private double longitude;
        private double latitude;
        private String location_name;
        private String completetime;
        private int completed;
        private String created_at;
        private String user_name;
        private String postsetting_name;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getProject_id() {
            return project_id;
        }

        public void setProject_id(String project_id) {
            this.project_id = project_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getPostsetting_id() {
            return postsetting_id;
        }

        public void setPostsetting_id(String postsetting_id) {
            this.postsetting_id = postsetting_id;
        }

        public int getSituation() {
            return situation;
        }

        public void setSituation(int situation) {
            this.situation = situation;
        }

        public double getLongitude() {
            return longitude;
        }

        public void setLongitude(double longitude) {
            this.longitude = longitude;
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

        public String getCompletetime() {
            return completetime;
        }

        public void setCompletetime(String completetime) {
            this.completetime = completetime;
        }

        public int getCompleted() {
            return completed;
        }

        public void setCompleted(int completed) {
            this.completed = completed;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getPostsetting_name() {
            return postsetting_name;
        }

        public void setPostsetting_name(String postsetting_name) {
            this.postsetting_name = postsetting_name;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.project_id);
            dest.writeString(this.user_id);
            dest.writeString(this.postsetting_id);
            dest.writeInt(this.situation);
            dest.writeDouble(this.longitude);
            dest.writeDouble(this.latitude);
            dest.writeString(this.location_name);
            dest.writeString(this.completetime);
            dest.writeInt(this.completed);
            dest.writeString(this.created_at);
            dest.writeString(this.user_name);
            dest.writeString(this.postsetting_name);
        }

        public PostsBean() {
        }

        protected PostsBean(Parcel in) {
            this.id = in.readInt();
            this.project_id = in.readString();
            this.user_id = in.readString();
            this.postsetting_id = in.readString();
            this.situation = in.readInt();
            this.longitude = in.readDouble();
            this.latitude = in.readDouble();
            this.location_name = in.readString();
            this.completetime = in.readString();
            this.completed = in.readInt();
            this.created_at = in.readString();
            this.user_name = in.readString();
            this.postsetting_name = in.readString();
        }

        public static final Creator<PostsBean> CREATOR = new Creator<PostsBean>() {
            @Override
            public PostsBean createFromParcel(Parcel source) {
                return new PostsBean(source);
            }

            @Override
            public PostsBean[] newArray(int size) {
                return new PostsBean[size];
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
        dest.writeTypedList(this.posts);
    }

    public ReportActualPostResponse() {
    }

    protected ReportActualPostResponse(Parcel in) {
        this.date = in.readString();
        this.posts = in.createTypedArrayList(PostsBean.CREATOR);
    }

    public static final Parcelable.Creator<ReportActualPostResponse> CREATOR = new Parcelable.Creator<ReportActualPostResponse>() {
        @Override
        public ReportActualPostResponse createFromParcel(Parcel source) {
            return new ReportActualPostResponse(source);
        }

        @Override
        public ReportActualPostResponse[] newArray(int size) {
            return new ReportActualPostResponse[size];
        }
    };
}
