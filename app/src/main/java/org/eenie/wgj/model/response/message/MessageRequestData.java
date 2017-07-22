package org.eenie.wgj.model.response.message;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

/**
 * Created by Eenie on 2017/7/21 at 20:54
 * Email: 472279981@qq.com
 * Des:
 */

public class MessageRequestData implements Parcelable {

    /**
     * from : 1
     * last_page : 13
     * per_page : 15
     * to : 15
     * total : 187
     */
    private int current_page;
    private int from;
    private int last_page;
    private int per_page;
    private int to;
    private int total;
    private List<DataBean> data;

    public int getCurrent_page() {
        return current_page;
    }

    public void setCurrent_page(int current_page) {
        this.current_page = current_page;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean implements Parcelable {

        /**
         * id : 2131
         * key : 1101
         * title : 会议室申请
         * alert : 有个会议室申请请去审核！
         * created_at : 2017-07-21 15:25:59
         * parameter : {"id":53}
         * hasread : 0
         */

        private int id;
        private int key;
        private String title;
        private String alert;
        private String created_at;
        private ParameterBean parameter;
        private int hasread;


        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getAlert() {
            return alert;
        }

        public void setAlert(String alert) {
            this.alert = alert;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public ParameterBean getParameter() {
            return parameter;
        }

        public void setParameter(ParameterBean parameter) {
            this.parameter = parameter;
        }

        public int getHasread() {
            return hasread;
        }

        public void setHasread(int hasread) {
            this.hasread = hasread;
        }

        public static class ParameterBean implements Parcelable {
            /**
             * id : 53
             */


            private int id;

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
                dest.writeInt(this.id);
            }

            public ParameterBean() {
            }

            protected ParameterBean(Parcel in) {
                this.id = in.readInt();
            }

            public static final Parcelable.Creator<ParameterBean> CREATOR = new Parcelable.Creator<ParameterBean>() {
                @Override
                public ParameterBean createFromParcel(Parcel source) {
                    return new ParameterBean(source);
                }

                @Override
                public ParameterBean[] newArray(int size) {
                    return new ParameterBean[size];
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
            dest.writeInt(this.key);
            dest.writeString(this.title);
            dest.writeString(this.alert);
            dest.writeString(this.created_at);
            dest.writeParcelable(this.parameter, flags);
            dest.writeInt(this.hasread);
        }

        public DataBean() {
        }

        protected DataBean(Parcel in) {
            this.id = in.readInt();
            this.key = in.readInt();
            this.title = in.readString();
            this.alert = in.readString();
            this.created_at = in.readString();
            this.parameter = in.readParcelable(ParameterBean.class.getClassLoader());
            this.hasread = in.readInt();
        }

        public static final Parcelable.Creator<DataBean> CREATOR = new Parcelable.Creator<DataBean>() {
            @Override
            public DataBean createFromParcel(Parcel source) {
                return new DataBean(source);
            }

            @Override
            public DataBean[] newArray(int size) {
                return new DataBean[size];
            }
        };
    }

    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getLast_page() {
        return last_page;
    }

    public void setLast_page(int last_page) {
        this.last_page = last_page;
    }

    public int getPer_page() {
        return per_page;
    }

    public void setPer_page(int per_page) {
        this.per_page = per_page;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.current_page);
        dest.writeInt(this.from);
        dest.writeInt(this.last_page);
        dest.writeInt(this.per_page);
        dest.writeInt(this.to);
        dest.writeInt(this.total);
        dest.writeTypedList(this.data);
    }

    public MessageRequestData() {
    }

    protected MessageRequestData(Parcel in) {
        this.current_page = in.readInt();
        this.from = in.readInt();
        this.last_page = in.readInt();
        this.per_page = in.readInt();
        this.to = in.readInt();
        this.total = in.readInt();
        this.data = in.createTypedArrayList(DataBean.CREATOR);
    }

    public static final Parcelable.Creator<MessageRequestData> CREATOR = new Parcelable.Creator<MessageRequestData>() {
        @Override
        public MessageRequestData createFromParcel(Parcel source) {
            return new MessageRequestData(source);
        }

        @Override
        public MessageRequestData[] newArray(int size) {
            return new MessageRequestData[size];
        }
    };
}
