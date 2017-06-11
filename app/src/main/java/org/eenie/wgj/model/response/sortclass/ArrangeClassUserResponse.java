package org.eenie.wgj.model.response.sortclass;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/6/7 at 14:59
 * Email: 472279981@qq.com
 * Des:
 */

public class ArrangeClassUserResponse implements Parcelable {

    /**
     * user_id : 6
     * name : 唐海斌
     * line : {"id":17,"name":"领班"}
     */

    private int user_id;
    private String name;
    private LineBean line;

    public ArrangeClassUserResponse(int user_id, String name) {
        this.user_id = user_id;
        this.name = name;

    }

    public ArrangeClassUserResponse(int user_id, String name, LineBean line) {
        this.user_id = user_id;
        this.name = name;
        this.line = line;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LineBean getLine() {
        return line;
    }

    public void setLine(LineBean line) {
        this.line = line;
    }

    public static class LineBean implements Parcelable {

        /**
         * id : 17
         * name : 领班
         */

        private int id;
        private String name;


        public LineBean(int id, String name) {
            this.id = id;
            this.name = name;
        }

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

        protected LineBean(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
        }

        public static final Parcelable.Creator<LineBean> CREATOR = new Parcelable.Creator<LineBean>() {
            @Override
            public LineBean createFromParcel(Parcel source) {
                return new LineBean(source);
            }

            @Override
            public LineBean[] newArray(int size) {
                return new LineBean[size];
            }
        };
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.user_id);
        dest.writeString(this.name);
        dest.writeParcelable(this.line, flags);
    }

    public ArrangeClassUserResponse() {
    }

    protected ArrangeClassUserResponse(Parcel in) {
        this.user_id = in.readInt();
        this.name = in.readString();
        this.line = in.readParcelable(LineBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<ArrangeClassUserResponse> CREATOR = new Parcelable.Creator<ArrangeClassUserResponse>() {
        @Override
        public ArrangeClassUserResponse createFromParcel(Parcel source) {
            return new ArrangeClassUserResponse(source);
        }

        @Override
        public ArrangeClassUserResponse[] newArray(int size) {
            return new ArrangeClassUserResponse[size];
        }
    };
}
