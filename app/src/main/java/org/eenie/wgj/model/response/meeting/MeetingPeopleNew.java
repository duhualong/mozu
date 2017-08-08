package org.eenie.wgj.model.response.meeting;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Eenie on 2017/8/8 at 17:47
 * Email: 472279981@qq.com
 * Des:
 */

public class MeetingPeopleNew implements Parcelable {
    private int id;
    private String name;
    private boolean checked;
    private ArrayList<UserBean> users;
    public void toggle() {
        this.checked = !this.checked;
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

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public ArrayList<UserBean> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<UserBean> users) {
        this.users = users;
    }

    public static class UserBean implements Parcelable {

        private int id;
        private String name;
        private String username;
        private boolean check;

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

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public boolean isCheck() {
            return check;
        }

        public void setCheck(boolean check) {
            this.check = check;
        }
        public void toggle() {
            this.check = !this.check;
        }


        @Override
        public int describeContents() {
            return 0;
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            dest.writeInt(this.id);
            dest.writeString(this.name);
            dest.writeString(this.username);
            dest.writeByte(this.check ? (byte) 1 : (byte) 0);
        }

        public UserBean() {
        }

        protected UserBean(Parcel in) {
            this.id = in.readInt();
            this.name = in.readString();
            this.username = in.readString();
            this.check = in.readByte() != 0;
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


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeByte(this.checked ? (byte) 1 : (byte) 0);
        dest.writeTypedList(this.users);
    }

    public MeetingPeopleNew() {
    }

    protected MeetingPeopleNew(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.checked = in.readByte() != 0;
        this.users = in.createTypedArrayList(UserBean.CREATOR);
    }

    public static final Parcelable.Creator<MeetingPeopleNew> CREATOR = new Parcelable.Creator<MeetingPeopleNew>() {
        @Override
        public MeetingPeopleNew createFromParcel(Parcel source) {
            return new MeetingPeopleNew(source);
        }

        @Override
        public MeetingPeopleNew[] newArray(int size) {
            return new MeetingPeopleNew[size];
        }
    };
}
