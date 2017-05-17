package org.eenie.wgj.model.response;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Eenie on 2017/5/17 at 14:06
 * Email: 472279981@qq.com
 * Des:
 */

public class KeyContactList implements Parcelable {


    /**
     * id : 131
     * name : 飞飞
     * images : /images/project/20161224/20161224190203YC2131298348.jpg
     * info : {"sex":1,"age":11,"height":145,"job":"董事长","workinghours":"08:00-20:00","numberplates":"沪azv112","remarks":"无","phone":""}
     */

    private int id;
    private String name;
    private String images;
    private InfoBean info;

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

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public static class InfoBean implements Parcelable {

        /**
         * sex : 1
         * age : 11
         * height : 145
         * job : 董事长
         * workinghours : 08:00-20:00
         * numberplates : 沪azv112
         * remarks : 无
         * phone :
         */

        private int sex;
        private int age;
        private int height;
        private String job;
        private String workinghours;
        private String numberplates;
        private String remarks;
        private String phone;

        public int getSex() {
            return sex;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getAge() {
            return age;
        }

        public void setAge(int age) {
            this.age = age;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }

        public String getJob() {
            return job;
        }

        public void setJob(String job) {
            this.job = job;
        }

        public String getWorkinghours() {
            return workinghours;
        }

        public void setWorkinghours(String workinghours) {
            this.workinghours = workinghours;
        }

        public String getNumberplates() {
            return numberplates;
        }

        public void setNumberplates(String numberplates) {
            this.numberplates = numberplates;
        }

        public String getRemarks() {
            return remarks;
        }

        public void setRemarks(String remarks) {
            this.remarks = remarks;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.sex);
            dest.writeInt(this.age);
            dest.writeInt(this.height);
            dest.writeString(this.job);
            dest.writeString(this.workinghours);
            dest.writeString(this.numberplates);
            dest.writeString(this.remarks);
            dest.writeString(this.phone);
        }

        public InfoBean() {
        }

        protected InfoBean(Parcel in) {
            this.sex = in.readInt();
            this.age = in.readInt();
            this.height = in.readInt();
            this.job = in.readString();
            this.workinghours = in.readString();
            this.numberplates = in.readString();
            this.remarks = in.readString();
            this.phone = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeString(this.images);
        dest.writeParcelable(this.info, flags);
    }

    public KeyContactList() {
    }

    protected KeyContactList(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.images = in.readString();
        this.info = in.readParcelable(InfoBean.class.getClassLoader());
    }

    public static final Parcelable.Creator<KeyContactList> CREATOR = new Parcelable.Creator<KeyContactList>() {
        @Override
        public KeyContactList createFromParcel(Parcel source) {
            return new KeyContactList(source);
        }

        @Override
        public KeyContactList[] newArray(int size) {
            return new KeyContactList[size];
        }
    };
}
