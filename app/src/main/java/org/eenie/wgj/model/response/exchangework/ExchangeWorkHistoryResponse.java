package org.eenie.wgj.model.response.exchangework;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Eenie on 2017/7/31 at 16:55
 * Email: 472279981@qq.com
 * Des:
 */

public class ExchangeWorkHistoryResponse implements Parcelable {
    private int id;
    private int type;
    private String classname;
    private String date;
    private List<peopleBean> people;
    private List<IamgeBean> image;
    private String content;
    private String instruction;


    public ExchangeWorkHistoryResponse(int id, int type, String classname,
                                       String date, List<peopleBean> people,
                                       List<IamgeBean> image, String content, String instruction) {
        this.id = id;
        this.type = type;
        this.classname = classname;
        this.date = date;
        this.people = people;
        this.image = image;
        this.content = content;
        this.instruction = instruction;
    }

    public String getInstruction() {
        return instruction;
    }

    public void setInstruction(String instruction) {
        this.instruction = instruction;
    }

    public List<IamgeBean> getImage() {
        return image;
    }

    public void setImage(List<IamgeBean> image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<peopleBean> getPeople() {
        return people;
    }

    public void setPeople(List<peopleBean> people) {
        this.people = people;
    }

    public static class IamgeBean implements Parcelable {

        private String image;

        public IamgeBean(String image) {
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

        protected IamgeBean(Parcel in) {
            this.image = in.readString();
        }

        public static final Parcelable.Creator<IamgeBean> CREATOR = new Parcelable.Creator<IamgeBean>() {
            @Override
            public IamgeBean createFromParcel(Parcel source) {
                return new IamgeBean(source);
            }

            @Override
            public IamgeBean[] newArray(int size) {
                return new IamgeBean[size];
            }
        };
    }
    public static class peopleBean implements Parcelable {

        private int id;
        private String name;

        public peopleBean(int id, String name) {
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

        protected peopleBean(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
        }

        public static final Parcelable.Creator<peopleBean> CREATOR = new Parcelable.Creator<peopleBean>() {
            @Override
            public peopleBean createFromParcel(Parcel source) {
                return new peopleBean(source);
            }

            @Override
            public peopleBean[] newArray(int size) {
                return new peopleBean[size];
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
        dest.writeInt(this.type);
        dest.writeString(this.classname);
        dest.writeString(this.date);
        dest.writeTypedList(this.people);
        dest.writeTypedList(this.image);
        dest.writeString(this.content);
        dest.writeString(this.instruction);
    }

    protected ExchangeWorkHistoryResponse(Parcel in) {
        this.id = in.readInt();
        this.type = in.readInt();
        this.classname = in.readString();
        this.date = in.readString();
        this.people = in.createTypedArrayList(peopleBean.CREATOR);
        this.image = in.createTypedArrayList(IamgeBean.CREATOR);
        this.content = in.readString();
        this.instruction = in.readString();
    }

    public static final Parcelable.Creator<ExchangeWorkHistoryResponse> CREATOR = new Parcelable.Creator<ExchangeWorkHistoryResponse>() {
        @Override
        public ExchangeWorkHistoryResponse createFromParcel(Parcel source) {
            return new ExchangeWorkHistoryResponse(source);
        }

        @Override
        public ExchangeWorkHistoryResponse[] newArray(int size) {
            return new ExchangeWorkHistoryResponse[size];
        }
    };
}
