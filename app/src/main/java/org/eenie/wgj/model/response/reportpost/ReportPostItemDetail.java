package org.eenie.wgj.model.response.reportpost;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/7/4 at 17:13
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportPostItemDetail implements Parcelable {
    private DataBean data;
    private ArrayList<planBean>plan;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public ArrayList<planBean> getPlan() {
        return plan;
    }

    public void setPlan(ArrayList<planBean> plan) {
        this.plan = plan;
    }

    public static class planBean implements Parcelable {
        private String time;
        private int statusCode;
        private ActualBean actual;

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getStatusCode() {
            return statusCode;
        }

        public void setStatusCode(int statusCode) {
            this.statusCode = statusCode;
        }

        public ActualBean getActual() {
            return actual;
        }

        public void setActual(ActualBean actual) {
            this.actual = actual;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.time);
            dest.writeInt(this.statusCode);
            dest.writeParcelable(this.actual, flags);
        }

        public planBean() {
        }

        protected planBean(Parcel in) {
            this.time = in.readString();
            this.statusCode = in.readInt();
            this.actual = in.readParcelable(ActualBean.class.getClassLoader());
        }

        public static final Parcelable.Creator<planBean> CREATOR = new Parcelable.Creator<planBean>() {
            @Override
            public planBean createFromParcel(Parcel source) {
                return new planBean(source);
            }

            @Override
            public planBean[] newArray(int size) {
                return new planBean[size];
            }
        };
    }
    public static class ActualBean implements Parcelable {
        /**
         * id : 44
         * completetime : 16:08:28
         * location_name : 上海市黄浦区苗江路靠近中国航空馆
         * situation : 1
         * completed : 1
         */
        private int id;
        private String completetime;
        private String location_name;
        private int situation;
        private int completed;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getCompletetime() {
            return completetime;
        }

        public void setCompletetime(String completetime) {
            this.completetime = completetime;
        }

        public String getLocation_name() {
            return location_name;
        }

        public void setLocation_name(String location_name) {
            this.location_name = location_name;
        }

        public int getSituation() {
            return situation;
        }

        public void setSituation(int situation) {
            this.situation = situation;
        }

        public int getCompleted() {
            return completed;
        }

        public void setCompleted(int completed) {
            this.completed = completed;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.completetime);
            dest.writeString(this.location_name);
            dest.writeInt(this.situation);
            dest.writeInt(this.completed);
        }

        public ActualBean() {
        }

        protected ActualBean(Parcel in) {
            this.id = in.readInt();
            this.completetime = in.readString();
            this.location_name = in.readString();
            this.situation = in.readInt();
            this.completed = in.readInt();
        }

        public static final Parcelable.Creator<ActualBean> CREATOR = new Parcelable.Creator<ActualBean>() {
            @Override
            public ActualBean createFromParcel(Parcel source) {
                return new ActualBean(source);
            }

            @Override
            public ActualBean[] newArray(int size) {
                return new ActualBean[size];
            }
        };
    }
    public static  class DataBean implements Parcelable {
        /**
         * newspaperpost_id : 141
         * date : 15:59:05
         * postnumber : 6
         * actualpostnumber : 0
         * remaining : 6
         * randomtime : 02:30
         * postsetting : {"jetlag":"15分钟","service":"日班","postsetting_name":"大堂岗","postsetting_info":"1.上班时间不得玩手机\n2.进出服务有礼节"}
         */

        private int newspaperpost_id;
        private String date;
        private int postnumber;
        private int actualpostnumber;
        private int remaining;
        private String randomtime;
        private PostsettingBean postsetting;


        public int getNewspaperpost_id() {
            return newspaperpost_id;
        }

        public void setNewspaperpost_id(int newspaperpost_id) {
            this.newspaperpost_id = newspaperpost_id;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getPostnumber() {
            return postnumber;
        }

        public void setPostnumber(int postnumber) {
            this.postnumber = postnumber;
        }

        public int getActualpostnumber() {
            return actualpostnumber;
        }

        public void setActualpostnumber(int actualpostnumber) {
            this.actualpostnumber = actualpostnumber;
        }

        public int getRemaining() {
            return remaining;
        }

        public void setRemaining(int remaining) {
            this.remaining = remaining;
        }

        public String getRandomtime() {
            return randomtime;
        }

        public void setRandomtime(String randomtime) {
            this.randomtime = randomtime;
        }

        public PostsettingBean getPostsetting() {
            return postsetting;
        }

        public void setPostsetting(PostsettingBean postsetting) {
            this.postsetting = postsetting;
        }

        public static class PostsettingBean implements Parcelable {
            /**
             * jetlag : 15分钟
             * service : 日班
             * postsetting_name : 大堂岗
             * postsetting_info : 1.上班时间不得玩手机
             2.进出服务有礼节
             */

            private String jetlag;
            private String service;
            private String postsetting_name;
            private String postsetting_info;

            public String getJetlag() {
                return jetlag;
            }

            public void setJetlag(String jetlag) {
                this.jetlag = jetlag;
            }

            public String getService() {
                return service;
            }

            public void setService(String service) {
                this.service = service;
            }

            public String getPostsetting_name() {
                return postsetting_name;
            }

            public void setPostsetting_name(String postsetting_name) {
                this.postsetting_name = postsetting_name;
            }

            public String getPostsetting_info() {
                return postsetting_info;
            }

            public void setPostsetting_info(String postsetting_info) {
                this.postsetting_info = postsetting_info;
            }

            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeString(this.jetlag);
                dest.writeString(this.service);
                dest.writeString(this.postsetting_name);
                dest.writeString(this.postsetting_info);
            }

            public PostsettingBean() {
            }

            protected PostsettingBean(Parcel in) {
                this.jetlag = in.readString();
                this.service = in.readString();
                this.postsetting_name = in.readString();
                this.postsetting_info = in.readString();
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
            dest.writeInt(this.newspaperpost_id);
            dest.writeString(this.date);
            dest.writeInt(this.postnumber);
            dest.writeInt(this.actualpostnumber);
            dest.writeInt(this.remaining);
            dest.writeString(this.randomtime);
            dest.writeParcelable(this.postsetting, flags);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.newspaperpost_id = in.readInt();
            this.date = in.readString();
            this.postnumber = in.readInt();
            this.actualpostnumber = in.readInt();
            this.remaining = in.readInt();
            this.randomtime = in.readString();
            this.postsetting = in.readParcelable(PostsettingBean.class.getClassLoader());
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.data, flags);
        dest.writeTypedList(this.plan);
    }

    public ReportPostItemDetail() {
    }

    protected ReportPostItemDetail(Parcel in) {
        this.data = in.readParcelable(DataBean.class.getClassLoader());
        this.plan = in.createTypedArrayList(planBean.CREATOR);
    }

    public static final Parcelable.Creator<ReportPostItemDetail> CREATOR = new Parcelable.Creator<ReportPostItemDetail>() {
        @Override
        public ReportPostItemDetail createFromParcel(Parcel source) {
            return new ReportPostItemDetail(source);
        }

        @Override
        public ReportPostItemDetail[] newArray(int size) {
            return new ReportPostItemDetail[size];
        }
    };
}
