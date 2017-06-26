package org.eenie.wgj.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Eenie on 2017/5/24 at 14:25
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportPostList implements Parcelable {

    /**
     * id : 101
     * reportingtime : ["01:30","02:30","04:30","06:00","23:30"]
     * jetlag : 10分钟
     * service : {"id":133,"servicesname":"夜班","starttime":39600,"endtime":1860}
     * postsetting : {"id":73,"post":"大堂岗"}
     */

    private int id;
    private List<String> reportingtime;
    private String jetlag;
    private String latitude;
    private String longitude;
    private ServiceBean service;
    private PostsettingBean postsetting;



    public ReportPostList(int id, List<String> reportingtime, String jetlag,
                          ServiceBean service, PostsettingBean postsetting) {
        this.id = id;
        this.reportingtime = reportingtime;
        this.jetlag = jetlag;
        this.service = service;
        this.postsetting = postsetting;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getReportingtime() {
        return reportingtime;
    }

    public void setReportingtime(List<String> reportingtime) {
        this.reportingtime = reportingtime;
    }

    public String getJetlag() {
        return jetlag;
    }

    public void setJetlag(String jetlag) {
        this.jetlag = jetlag;
    }

    public ServiceBean getService() {
        return service;
    }

    public void setService(ServiceBean service) {
        this.service = service;
    }

    public PostsettingBean getPostsetting() {
        return postsetting;
    }

    public void setPostsetting(PostsettingBean postsetting) {
        this.postsetting = postsetting;
    }

    public static class ServiceBean implements Parcelable {
        public ServiceBean(int id, String servicesname) {
            this.id = id;
            this.servicesname = servicesname;
        }

        /**
         * id : 133
         * servicesname : 夜班
         * starttime : 39600
         * endtime : 1860
         */

        private int id;
        private String servicesname;
        private int starttime;
        private int endtime;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getServicesname() {
            return servicesname;
        }

        public void setServicesname(String servicesname) {
            this.servicesname = servicesname;
        }

        public int getStarttime() {
            return starttime;
        }

        public void setStarttime(int starttime) {
            this.starttime = starttime;
        }

        public int getEndtime() {
            return endtime;
        }

        public void setEndtime(int endtime) {
            this.endtime = endtime;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.servicesname);
            dest.writeInt(this.starttime);
            dest.writeInt(this.endtime);
        }

        public ServiceBean() {
        }

        protected ServiceBean(Parcel in) {
            this.id = in.readInt();
            this.servicesname = in.readString();
            this.starttime = in.readInt();
            this.endtime = in.readInt();
        }

        public static final Parcelable.Creator<ServiceBean> CREATOR = new Parcelable.Creator<ServiceBean>() {
            @Override
            public ServiceBean createFromParcel(Parcel source) {
                return new ServiceBean(source);
            }

            @Override
            public ServiceBean[] newArray(int size) {
                return new ServiceBean[size];
            }
        };
    }

    public static class PostsettingBean implements Parcelable {
        /**
         * id : 73
         * post : 大堂岗
         */

        private int id;
        private String post;
        private String selectTime;

        public String getSelectTime() {
            return selectTime;
        }

        public void setSelectTime(String selectTime) {
            this.selectTime = selectTime;
        }

        public PostsettingBean(int id, String post) {
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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.post);
        }

        public PostsettingBean() {
        }

        protected PostsettingBean(Parcel in) {
            this.id = in.readInt();
            this.post = in.readString();
        }

        public static final Parcelable.Creator<PostsettingBean> CREATOR = new Parcelable.Creator<PostsettingBean>() {
            @Override
            public PostsettingBean createFromParcel(Parcel source) {
                return new PostsettingBean(source);
            }

            @Override
            public PostsettingBean[] newArray(int size) {
                return new PostsettingBean[size];
            }
        };
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeStringList(this.reportingtime);
        dest.writeString(this.jetlag);
        dest.writeString(this.latitude);
        dest.writeString(this.longitude);
        dest.writeParcelable(this.service, flags);
        dest.writeParcelable(this.postsetting, flags);
    }

    protected ReportPostList(Parcel in) {
        this.id = in.readInt();
        this.reportingtime = in.createStringArrayList();
        this.jetlag = in.readString();
        this.latitude = in.readString();
        this.longitude = in.readString();
        this.service = in.readParcelable(ServiceBean.class.getClassLoader());
        this.postsetting = in.readParcelable(PostsettingBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ReportPostList> CREATOR = new Parcelable.Creator<ReportPostList>() {
        @Override
        public ReportPostList createFromParcel(Parcel source) {
            return new ReportPostList(source);
        }

        @Override
        public ReportPostList[] newArray(int size) {
            return new ReportPostList[size];
        }
    };
}
