package org.eenie.wgj.model.requset;

/**
 * Created by Eenie on 2017/4/19 at 11:14
 * Email: 472279981@qq.com
 * Des:
 */

public class Muser  {




    private EmergencyContactMod emergencyContact;




    public EmergencyContactMod getEmergencyContact() {
        return emergencyContact;
    }


    public void setEmergencyContact(EmergencyContactMod emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

//
//    @Override
//    public int describeContents() {
//        return 0;
//    }
//
//    @Override
//    public void writeToParcel(Parcel dest, int flags) {
//
//        dest.writeParcelable(this.emergencyContact, flags);
//
//    }
//
//    public Muser(Parcel in) {
//
//        this.emergencyContact = in.readParcelable(EmergencyContactMod.class.getClassLoader());
//
//    }
//
//    public static final Parcelable.Creator<Muser> CREATOR = new Parcelable.Creator<Muser>() {
//        @Override
//        public Muser createFromParcel(Parcel source) {
//            return new Muser(source);
//        }
//
//        @Override
//        public Muser[] newArray(int size) {
//            return new Muser[size];
//        }
//    };
}
