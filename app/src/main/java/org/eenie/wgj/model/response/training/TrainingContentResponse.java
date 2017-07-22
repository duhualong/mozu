package org.eenie.wgj.model.response.training;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Eenie on 2017/7/6 at 17:49
 * Email: 472279981@qq.com
 * Des:
 */

public class TrainingContentResponse implements Parcelable {

    /**
     * schedule : 0
     * totalpage : 0
     * currentpage : 1
     * type : 1
     */
    private double schedule;
    private int currentpage;
    private int type;
    private int current_page;
    private List<Integer> total_pages;
    private int current_index;

    public int getCurrent_index() {
        return current_index;
    }

    public void setCurrent_index(int current_index) {
        this.current_index = current_index;
    }

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public List<Integer> getTotal_pages() {
        return total_pages;
    }

    public void setTotal_pages(List<Integer> total_pages) {
        this.total_pages = total_pages;
    }

    public double getSchedule() {
        return schedule;
    }

    public void setSchedule(double schedule) {
        this.schedule = schedule;
    }



    public int getCurrentpage() {
        return currentpage;
    }

    public void setCurrentpage(int currentpage) {
        this.currentpage = currentpage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.schedule);
        dest.writeInt(this.currentpage);
        dest.writeInt(this.type);
        dest.writeInt(this.current_page);
        dest.writeList(this.total_pages);
        dest.writeInt(this.current_index);
    }

    public TrainingContentResponse() {
    }

    protected TrainingContentResponse(Parcel in) {
        this.schedule = in.readDouble();
        this.currentpage = in.readInt();
        this.type = in.readInt();
        this.current_page = in.readInt();
        this.total_pages = new ArrayList<Integer>();
        in.readList(this.total_pages, Integer.class.getClassLoader());
        this.current_index = in.readInt();
    }

    public static final Parcelable.Creator<TrainingContentResponse> CREATOR = new Parcelable.Creator<TrainingContentResponse>() {
        @Override
        public TrainingContentResponse createFromParcel(Parcel source) {
            return new TrainingContentResponse(source);
        }

        @Override
        public TrainingContentResponse[] newArray(int size) {
            return new TrainingContentResponse[size];
        }
    };
}
