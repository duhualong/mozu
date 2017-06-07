package org.eenie.wgj.model.requset;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Eenie on 2017/6/7 at 10:47
 * Email: 472279981@qq.com
 * Des:
 */

public class UpdateRoundPoint implements Parcelable {
    private String projectid;
    private String inspectionname;
    private String inspectioncontent;
    private List<String> image;
    private  int id;

    public UpdateRoundPoint(String projectid, String inspectionname, String inspectioncontent, List<String> image, int id) {
        this.projectid = projectid;
        this.inspectionname = inspectionname;
        this.inspectioncontent = inspectioncontent;
        this.image = image;
        this.id = id;
    }

    public String getProjectid() {
        return projectid;
    }

    public void setProjectid(String projectid) {
        this.projectid = projectid;
    }

    public String getInspectionname() {
        return inspectionname;
    }

    public void setInspectionname(String inspectionname) {
        this.inspectionname = inspectionname;
    }

    public String getInspectioncontent() {
        return inspectioncontent;
    }

    public void setInspectioncontent(String inspectioncontent) {
        this.inspectioncontent = inspectioncontent;
    }

    public List<String> getImage() {
        return image;
    }

    public void setImage(List<String> image) {
        this.image = image;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.projectid);
        dest.writeString(this.inspectionname);
        dest.writeString(this.inspectioncontent);
        dest.writeStringList(this.image);
        dest.writeInt(this.id);
    }

    protected UpdateRoundPoint(Parcel in) {
        this.projectid = in.readString();
        this.inspectionname = in.readString();
        this.inspectioncontent = in.readString();
        this.image = in.createStringArrayList();
        this.id = in.readInt();
    }

    public static final Parcelable.Creator<UpdateRoundPoint> CREATOR = new Parcelable.Creator<UpdateRoundPoint>() {
        @Override
        public UpdateRoundPoint createFromParcel(Parcel source) {
            return new UpdateRoundPoint(source);
        }

        @Override
        public UpdateRoundPoint[] newArray(int size) {
            return new UpdateRoundPoint[size];
        }
    };
}
