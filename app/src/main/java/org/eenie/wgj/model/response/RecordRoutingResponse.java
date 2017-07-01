package org.eenie.wgj.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Eenie on 2017/6/28 at 18:13
 * Email: 472279981@qq.com
 * Des:
 */

public class RecordRoutingResponse implements Parcelable {

    /**
     * user : {"name":"韩红","id_card_head_image":"/images/user/20170614/20170614114336YC1961746353.jpg"}
     */


    /**
     * date : 2017-06-28
     * rate : {"turn":100,"turn_total":13,"turn_actual":13,"number":0,"number_actual":0,"ring":4,"turn_ring":4}
     */

    private String date;
    private RateBean rate;
    private List<InfoBean> info;
    private UserBean user;


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public RateBean getRate() {
        return rate;
    }

    public void setRate(RateBean rate) {
        this.rate = rate;
    }

    public List<InfoBean> getInfo() {
        return info;
    }

    public void setInfo(List<InfoBean> info) {
        this.info = info;
    }

    public UserBean getUser() {
        return user;
    }

    public void setUser(UserBean user) {
        this.user = user;
    }

    public static class UserBean implements Parcelable {
        /**
         * name : 韩红
         * id_card_head_image : /images/user/20170614/20170614114336YC1961746353.jpg
         */

        private String name;
        private String id_card_head_image;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId_card_head_image() {
            return id_card_head_image;
        }

        public void setId_card_head_image(String id_card_head_image) {
            this.id_card_head_image = id_card_head_image;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeString(this.name);
            dest.writeString(this.id_card_head_image);
        }

        public UserBean() {
        }

        protected UserBean(Parcel in) {
            this.name = in.readString();
            this.id_card_head_image = in.readString();
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
    public static class InfoBean implements Parcelable {
        private int inspectionday_id;
        private List<InspectionBean>inspection;

        public int getInspectionday_id() {
            return inspectionday_id;
        }

        public void setInspectionday_id(int inspectionday_id) {
            this.inspectionday_id = inspectionday_id;
        }

        public List<InspectionBean> getInspection() {
            return inspection;
        }

        public void setInspection(List<InspectionBean> inspection) {
            this.inspection = inspection;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.inspectionday_id);
            dest.writeTypedList(this.inspection);
        }

        public InfoBean() {
        }

        protected InfoBean(Parcel in) {
            this.inspectionday_id = in.readInt();
            this.inspection = in.createTypedArrayList(InspectionBean.CREATOR);
        }

        public static final Creator<InfoBean> CREATOR = new Creator<InfoBean>() {
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
    public static class InspectionBean implements Parcelable {
        private  int id;
        private int situation;
        private String time;
        private double longitude;
        private double latitude;
        private String location_name;
        private List<ImagesBean> images;
        private String inspectionname;
        private String happening;
        private WarrantyBean warranty;

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

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
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

        public List<ImagesBean> getImages() {
            return images;
        }

        public void setImages(List<ImagesBean> images) {
            this.images = images;
        }

        public String getInspectionname() {
            return inspectionname;
        }

        public void setInspectionname(String inspectionname) {
            this.inspectionname = inspectionname;
        }

        public String getHappening() {
            return happening;
        }

        public void setHappening(String happening) {
            this.happening = happening;
        }

        public WarrantyBean getWarranty() {
            return warranty;
        }

        public void setWarranty(WarrantyBean warranty) {
            this.warranty = warranty;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.situation);
            dest.writeString(this.time);
            dest.writeDouble(this.longitude);
            dest.writeDouble(this.latitude);
            dest.writeString(this.location_name);
            dest.writeTypedList(this.images);
            dest.writeString(this.inspectionname);
            dest.writeString(this.happening);
            dest.writeParcelable(this.warranty, flags);
        }

        public InspectionBean() {
        }

        protected InspectionBean(Parcel in) {
            this.id = in.readInt();
            this.situation = in.readInt();
            this.time = in.readString();
            this.longitude = in.readDouble();
            this.latitude = in.readDouble();
            this.location_name = in.readString();
            this.images = in.createTypedArrayList(ImagesBean.CREATOR);
            this.inspectionname = in.readString();
            this.happening = in.readString();
            this.warranty = in.readParcelable(WarrantyBean.class.getClassLoader());
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

    public static class RateBean implements Parcelable {
        /**
         * turn : 100
         * turn_total : 13
         * turn_actual : 13
         * number : 0
         * number_actual : 0
         * ring : 4
         * turn_ring : 4
         */

        private double turn;
        private int turn_total;
        private int turn_actual;
        private double number;
        private int number_actual;
        private int ring;
        private int turn_ring;


        public double getTurn() {
            return turn;
        }

        public void setTurn(double turn) {
            this.turn = turn;
        }

        public int getTurn_total() {
            return turn_total;
        }

        public void setTurn_total(int turn_total) {
            this.turn_total = turn_total;
        }

        public int getTurn_actual() {
            return turn_actual;
        }

        public void setTurn_actual(int turn_actual) {
            this.turn_actual = turn_actual;
        }

        public double getNumber() {
            return number;
        }

        public void setNumber(double number) {
            this.number = number;
        }

        public int getNumber_actual() {
            return number_actual;
        }

        public void setNumber_actual(int number_actual) {
            this.number_actual = number_actual;
        }

        public int getRing() {
            return ring;
        }

        public void setRing(int ring) {
            this.ring = ring;
        }

        public int getTurn_ring() {
            return turn_ring;
        }

        public void setTurn_ring(int turn_ring) {
            this.turn_ring = turn_ring;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeDouble(this.turn);
            dest.writeInt(this.turn_total);
            dest.writeInt(this.turn_actual);
            dest.writeDouble(this.number);
            dest.writeInt(this.number_actual);
            dest.writeInt(this.ring);
            dest.writeInt(this.turn_ring);
        }

        public RateBean() {
        }

        protected RateBean(Parcel in) {
            this.turn = in.readDouble();
            this.turn_total = in.readInt();
            this.turn_actual = in.readInt();
            this.number = in.readDouble();
            this.number_actual = in.readInt();
            this.ring = in.readInt();
            this.turn_ring = in.readInt();
        }

        public static final Creator<RateBean> CREATOR = new Creator<RateBean>() {
            @Override
            public RateBean createFromParcel(Parcel source) {
                return new RateBean(source);
            }

            @Override
            public RateBean[] newArray(int size) {
                return new RateBean[size];
            }
        };
    }
    public static class ImagesBean implements Parcelable {

        /**
         * id : 235
         * inspection_id : 302
         * image : /images/inspection/20170628/20170628133546YC796339533.jpg
         * created_at : 2017-06-28 13:35:46
         * updated_at : 2017-06-28 13:35:46
         * status : 1
         */

        private int id;
        private int inspection_id;
        private String image;
        private String created_at;
        private String updated_at;
        private int status;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getInspection_id() {
            return inspection_id;
        }

        public void setInspection_id(int inspection_id) {
            this.inspection_id = inspection_id;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeInt(this.inspection_id);
            dest.writeString(this.image);
            dest.writeString(this.created_at);
            dest.writeString(this.updated_at);
            dest.writeInt(this.status);
        }

        public ImagesBean() {
        }

        protected ImagesBean(Parcel in) {
            this.id = in.readInt();
            this.inspection_id = in.readInt();
            this.image = in.readString();
            this.created_at = in.readString();
            this.updated_at = in.readString();
            this.status = in.readInt();
        }

        public static final Parcelable.Creator<ImagesBean> CREATOR = new Parcelable.Creator<ImagesBean>() {
            @Override
            public ImagesBean createFromParcel(Parcel source) {
                return new ImagesBean(source);
            }

            @Override
            public ImagesBean[] newArray(int size) {
                return new ImagesBean[size];
            }
        };
    }
    public  static  class WarrantyBean implements Parcelable {

        /**
         * id : 219
         * uniqueid : YC201706281021281601
         * title : null
         * content : 吧
         * inspectionpoint_id : 292
         */

        private int id;
        private String uniqueid;
        private String content;
        private int inspectionpoint_id;
        private List<ImageBean>image;


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

        public String getUniqueid() {
            return uniqueid;
        }

        public void setUniqueid(String uniqueid) {
            this.uniqueid = uniqueid;
        }



        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public int getInspectionpoint_id() {
            return inspectionpoint_id;
        }

        public void setInspectionpoint_id(int inspectionpoint_id) {
            this.inspectionpoint_id = inspectionpoint_id;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.uniqueid);
            dest.writeString(this.content);
            dest.writeInt(this.inspectionpoint_id);
            dest.writeTypedList(this.image);
        }

        public WarrantyBean() {
        }

        protected WarrantyBean(Parcel in) {
            this.id = in.readInt();
            this.uniqueid = in.readString();
            this.content = in.readString();
            this.inspectionpoint_id = in.readInt();
            this.image = in.createTypedArrayList(ImageBean.CREATOR);
        }

        public static final Creator<WarrantyBean> CREATOR = new Creator<WarrantyBean>() {
            @Override
            public WarrantyBean createFromParcel(Parcel source) {
                return new WarrantyBean(source);
            }

            @Override
            public WarrantyBean[] newArray(int size) {
                return new WarrantyBean[size];
            }
        };
    }
   public static class ImageBean implements Parcelable {
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
        dest.writeString(this.date);
        dest.writeParcelable(this.rate, flags);
        dest.writeTypedList(this.info);
        dest.writeParcelable(this.user, flags);
    }

    public RecordRoutingResponse() {
    }

    protected RecordRoutingResponse(Parcel in) {
        this.date = in.readString();
        this.rate = in.readParcelable(RateBean.class.getClassLoader());
        this.info = in.createTypedArrayList(InfoBean.CREATOR);
        this.user = in.readParcelable(UserBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<RecordRoutingResponse> CREATOR = new Parcelable.Creator<RecordRoutingResponse>() {
        @Override
        public RecordRoutingResponse createFromParcel(Parcel source) {
            return new RecordRoutingResponse(source);
        }

        @Override
        public RecordRoutingResponse[] newArray(int size) {
            return new RecordRoutingResponse[size];
        }
    };
}
