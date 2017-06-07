package org.eenie.wgj.model.response;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/5/25 at 16:08
 * Email: 472279981@qq.com
 * Des:
 */

public class RoundPoint implements Parcelable {

    /**
     * id : 89
     * inspectionname : 卢湾支行监控室
     * inspectionmethod : 2
     * inspectioncontent : 1.监控画面拍照上传
     2.对执勤日志拍照
     */

    private int id;
    private String inspectionname;
    private int inspectionmethod;
    private String inspectioncontent;
    private ArrayList<ImageBean>image;
    private String time;
    private boolean checked;

    public RoundPoint(int id, String inspectionname, String inspectioncontent, ArrayList<ImageBean> image) {
        this.id = id;
        this.inspectionname = inspectionname;
        this.inspectioncontent = inspectioncontent;
        this.image = image;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public ArrayList<ImageBean> getImage() {
        return image;
    }

    public void setImage(ArrayList<ImageBean> image) {
        this.image = image;
    }

    public static class ImageBean implements Parcelable {


        /**
         * image : /images/readilyShoot/20170406/20170406160650YC392002694.jpg
         */

        private String image;

        public ImageBean(String image) {
            this.image = image;
        }

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
        dest.writeString(this.inspectionname);
        dest.writeInt(this.inspectionmethod);
        dest.writeString(this.inspectioncontent);
        dest.writeTypedList(this.image);
    }

    public RoundPoint() {
    }

    protected RoundPoint(Parcel in) {
        this.id = in.readInt();
        this.inspectionname = in.readString();
        this.inspectionmethod = in.readInt();
        this.inspectioncontent = in.readString();
        this.image = in.createTypedArrayList(ImageBean.CREATOR);
    }

    public static final Parcelable.Creator<RoundPoint> CREATOR = new Parcelable.Creator<RoundPoint>() {
        @Override
        public RoundPoint createFromParcel(Parcel source) {
            return new RoundPoint(source);
        }

        @Override
        public RoundPoint[] newArray(int size) {
            return new RoundPoint[size];
        }
    };
}
