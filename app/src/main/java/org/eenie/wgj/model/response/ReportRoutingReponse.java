package org.eenie.wgj.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Eenie on 2017/6/23 at 15:22
 * Email: 472279981@qq.com
 * Des:
 */

public class ReportRoutingReponse implements Parcelable {

    /**
     * id : 155
     * uniqueid : YC201703151542305744
     * title : 吃好
     * content : 和好
     * time : 2017-03-15 15:42:30
     * inspectionpoint : {"id":113,"inspectionpoint":77,"inspectionname":"大堂","inspectionmethod":2,"inspectioncontent":"1.注意哈金等人闯入","inspectiontime":"15:42:30"}
     * user : {"id":19,"name":"韩红"}
     * image : [{"image":"/images/inspection/20170315/20170315154230YC1521363747.jpg"}]
     */

    private int id;
    private String uniqueid;
    private String title;
    private String content;
    private String time;
    private InspectionpointBean inspectionpoint;
    private UserBean user;
    private List<ImageBean> image;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public InspectionpointBean getInspectionpoint() {
        return inspectionpoint;
    }

    public void setInspectionpoint(InspectionpointBean inspectionpoint) {
        this.inspectionpoint = inspectionpoint;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public List<ImageBean> getImage() {
        return image;
    }

    public void setImage(List<ImageBean> image) {
        this.image = image;
    }

    public static class InspectionpointBean implements Parcelable {
        /**
         * id : 113
         * inspectionpoint : 77
         * inspectionname : 大堂
         * inspectionmethod : 2
         * inspectioncontent : 1.注意哈金等人闯入
         * inspectiontime : 15:42:30
         */

        private int id;
        private int inspectionpoint;
        private String inspectionname;
        private int inspectionmethod;
        private String inspectioncontent;
        private String inspectiontime;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getInspectionpoint() {
            return inspectionpoint;
        }

        public void setInspectionpoint(int inspectionpoint) {
            this.inspectionpoint = inspectionpoint;
        }

        public String getInspectionname() {
            return inspectionname;
        }

        public void setInspectionname(String inspectionname) {
            this.inspectionname = inspectionname;
        }

        public int getInspectionmethod() {
            return inspectionmethod;
        }

        public void setInspectionmethod(int inspectionmethod) {
            this.inspectionmethod = inspectionmethod;
        }

        public String getInspectioncontent() {
            return inspectioncontent;
        }

        public void setInspectioncontent(String inspectioncontent) {
            this.inspectioncontent = inspectioncontent;
        }

        public String getInspectiontime() {
            return inspectiontime;
        }

        public void setInspectiontime(String inspectiontime) {
            this.inspectiontime = inspectiontime;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.inspectionpoint);
            dest.writeString(this.inspectionname);
            dest.writeInt(this.inspectionmethod);
            dest.writeString(this.inspectioncontent);
            dest.writeString(this.inspectiontime);
        }

        public InspectionpointBean() {
        }

        protected InspectionpointBean(Parcel in) {
            this.id = in.readInt();
            this.inspectionpoint = in.readInt();
            this.inspectionname = in.readString();
            this.inspectionmethod = in.readInt();
            this.inspectioncontent = in.readString();
            this.inspectiontime = in.readString();
        }

        public static final Parcelable.Creator<InspectionpointBean> CREATOR = new Parcelable.Creator<InspectionpointBean>() {
            @Override
            public InspectionpointBean createFromParcel(Parcel source) {
                return new InspectionpointBean(source);
            }

            @Override
            public InspectionpointBean[] newArray(int size) {
                return new InspectionpointBean[size];
            }
        };
    }

    public static class UserBean implements Parcelable {
        /**
         * id : 19
         * name : 韩红
         */

        private int id;
        private String name;


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

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
        }

        public UserBean() {
        }

        protected UserBean(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
        }

        public static final Parcelable.Creator<UserBean> CREATOR = new Parcelable.Creator<UserBean>() {
            @Override
            public UserBean createFromParcel(Parcel source) {
                return new UserBean(source);
            }

            @Override
            public UserBean[] newArray(int size) {
                return new UserBean[size];
            }
        };
    }

    public static class ImageBean implements Parcelable {
        /**
         * image : /images/inspection/20170315/20170315154230YC1521363747.jpg
         */

        private String image;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.image);
        }

        public ImageBean() {
        }

        protected ImageBean(Parcel in) {
            this.image = in.readString();
        }

        public static final Parcelable.Creator<ImageBean> CREATOR = new Parcelable.Creator<ImageBean>() {
            @Override
            public ImageBean createFromParcel(Parcel source) {
                return new ImageBean(source);
            }

            @Override
            public ImageBean[] newArray(int size) {
                return new ImageBean[size];
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
        dest.writeString(this.uniqueid);
        dest.writeString(this.title);
        dest.writeString(this.content);
        dest.writeString(this.time);
        dest.writeParcelable(this.inspectionpoint, flags);
        dest.writeParcelable(this.user, flags);
        dest.writeTypedList(this.image);
    }

    public ReportRoutingReponse() {
    }

    protected ReportRoutingReponse(Parcel in) {
        this.id = in.readInt();
        this.uniqueid = in.readString();
        this.title = in.readString();
        this.content = in.readString();
        this.time = in.readString();
        this.inspectionpoint = in.readParcelable(InspectionpointBean.class.getClassLoader());
        this.user = in.readParcelable(UserBean.class.getClassLoader());
        this.image = in.createTypedArrayList(ImageBean.CREATOR);
    }

    public static final Parcelable.Creator<ReportRoutingReponse> CREATOR = new Parcelable.Creator<ReportRoutingReponse>() {
        @Override
        public ReportRoutingReponse createFromParcel(Parcel source) {
            return new ReportRoutingReponse(source);
        }

        @Override
        public ReportRoutingReponse[] newArray(int size) {
            return new ReportRoutingReponse[size];
        }
    };
}
