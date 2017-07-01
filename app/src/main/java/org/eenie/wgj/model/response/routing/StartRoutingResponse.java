package org.eenie.wgj.model.response.routing;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Eenie on 2017/6/24 at 17:09
 * Email: 472279981@qq.com
 * Des:
 */

public class StartRoutingResponse implements Parcelable {

    /**
     * id : 73
     * info : [{"id":292,"inspectionpoint":111,"inspectionname":"点位3","inspectiontime":"18:00:00",
     * "inspectioncontent":"345235","image":[{"image":"/images/project/20170607/20170607062838YC1361839955.jpg"},
     * {"image":"/images/project/20170607/20170607062838YC1783244268.jpg"}]}]
     */

    private int id;
    private List<InfoBean> info;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public static class InfoBean implements Parcelable {
        /**
         * id : 292
         * inspectionpoint : 111
         * inspectionname : 点位3
         * inspectiontime : 18:00:00
         * inspectioncontent : 345235
         * image : [{"image":"/images/project/20170607/20170607062838YC1361839955.jpg"},{"image":"/images/project/20170607/20170607062838YC1783244268.jpg"}]
         */

        private int id;
        private int inspectionpoint;
        private String inspectionname;
        private String inspectiontime;
        private String inspectioncontent;
        private List<ImageBean> image;
        private InspectionBean inspection;


        public InspectionBean getInspection() {
            return inspection;
        }

        public void setInspection(InspectionBean inspection) {
            this.inspection = inspection;
        }

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

        public String getInspectiontime() {
            return inspectiontime;
        }

        public void setInspectiontime(String inspectiontime) {
            this.inspectiontime = inspectiontime;
        }

        public String getInspectioncontent() {
            return inspectioncontent;
        }

        public void setInspectioncontent(String inspectioncontent) {
            this.inspectioncontent = inspectioncontent;
        }

        public List<ImageBean> getImage() {
            return image;
        }

        public void setImage(List<ImageBean> image) {
            this.image = image;
        }

        public static class Warranty implements Parcelable {
            private int id;
            private String content;
            private String uniqueid;
            private List<ImageBean> image;


            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getUniqueid() {
                return uniqueid;
            }

            public void setUniqueid(String uniqueid) {
                this.uniqueid = uniqueid;
            }

            public List<ImageBean> getImage() {
                return image;
            }

            public void setImage(List<ImageBean> image) {
                this.image = image;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeString(this.content);
                dest.writeString(this.uniqueid);
                dest.writeTypedList(this.image);
            }

            public Warranty() {
            }

            protected Warranty(Parcel in) {
                this.id = in.readInt();
                this.content = in.readString();
                this.uniqueid = in.readString();
                this.image = in.createTypedArrayList(ImageBean.CREATOR);
            }

            public static final Parcelable.Creator<Warranty> CREATOR = new Parcelable.Creator<Warranty>() {
                @Override
                public Warranty createFromParcel(Parcel source) {
                    return new Warranty(source);
                }

                @Override
                public Warranty[] newArray(int size) {
                    return new Warranty[size];
                }
            };
        }

        public static class InspectionBean implements Parcelable {

            /**
             * id : 281
             * situation : 1
             * completed : 1
             * time : 18:18
             * time_type : 2
             */

            private int id;
            private int situation;
            private int completed;
            private String time;
            private int time_type;
            private List<ImageBean> image;
            private Warranty warranty;


            public Warranty getWarranty() {
                return warranty;
            }

            public void setWarranty(Warranty warranty) {
                this.warranty = warranty;
            }

            public List<ImageBean> getImage() {
                return image;
            }

            public void setImage(List<ImageBean> image) {
                this.image = image;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
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

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public int getTime_type() {
                return time_type;
            }

            public void setTime_type(int time_type) {
                this.time_type = time_type;
            }


            @Override
            public int describeContents() {
                return 0;
            }

            @Override
            public void writeToParcel(Parcel dest, int flags) {
                dest.writeInt(this.id);
                dest.writeInt(this.situation);
                dest.writeInt(this.completed);
                dest.writeString(this.time);
                dest.writeInt(this.time_type);
                dest.writeTypedList(this.image);
                dest.writeParcelable(this.warranty, flags);
            }

            public InspectionBean() {
            }

            protected InspectionBean(Parcel in) {
                this.id = in.readInt();
                this.situation = in.readInt();
                this.completed = in.readInt();
                this.time = in.readString();
                this.time_type = in.readInt();
                this.image = in.createTypedArrayList(ImageBean.CREATOR);
                this.warranty = in.readParcelable(Warranty.class.getClassLoader());
            }

            public static final Parcelable.Creator<InspectionBean> CREATOR = new Parcelable.Creator<InspectionBean>() {
                @Override
                public InspectionBean createFromParcel(Parcel source) {
                    return new InspectionBean(source);
                }

                @Override
                public InspectionBean[] newArray(int size) {
                    return new InspectionBean[size];
                }
            };
        }

        public static class ImageBean implements Parcelable {

            /**
             * image : /images/project/20170607/20170607062838YC1361839955.jpg
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
            dest.writeInt(this.inspectionpoint);
            dest.writeString(this.inspectionname);
            dest.writeString(this.inspectiontime);
            dest.writeString(this.inspectioncontent);
            dest.writeTypedList(this.image);
            dest.writeParcelable(this.inspection, flags);
        }

        public InfoBean() {
        }

        protected InfoBean(Parcel in) {
            this.id = in.readInt();
            this.inspectionpoint = in.readInt();
            this.inspectionname = in.readString();
            this.inspectiontime = in.readString();
            this.inspectioncontent = in.readString();
            this.image = in.createTypedArrayList(ImageBean.CREATOR);
            this.inspection = in.readParcelable(InspectionBean.class.getClassLoader());
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
        dest.writeInt(this.id);
        dest.writeTypedList(this.info);
    }

    public StartRoutingResponse() {
    }

    protected StartRoutingResponse(Parcel in) {
        this.id = in.readInt();
        this.info = in.createTypedArrayList(InfoBean.CREATOR);
    }

    public static final Parcelable.Creator<StartRoutingResponse> CREATOR = new Parcelable.Creator<StartRoutingResponse>() {
        @Override
        public StartRoutingResponse createFromParcel(Parcel source) {
            return new StartRoutingResponse(source);
        }

        @Override
        public StartRoutingResponse[] newArray(int size) {
            return new StartRoutingResponse[size];
        }
    };
}
