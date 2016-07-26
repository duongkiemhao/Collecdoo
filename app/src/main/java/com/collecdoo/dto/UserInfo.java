package com.collecdoo.dto;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;

public class UserInfo implements Parcelable {


    public String first_name;

    public String last_name;

    public String email;

    public String password;

    public String phoneNo;

    public String birthday;

    public String gent;

    public String passenger_type;

    public String driver_type;

    public String prof_driver_type;

    public String image_file_path;

    public String house_no;

    public String street;

    public String post_code;

    public String location;

    public String country;

    public String address_optional;

    public String push_registered_id;

    public String user_id;

    public UserInfo(String first_name, String last_name, String email, String password, String phoneNo, String birthday, String gent, String passenger_type, String driver_type, String prof_driver_type, String push_registered_id) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.birthday = birthday;
        this.gent = gent;
        this.passenger_type = passenger_type;
        this.driver_type = driver_type;
        this.prof_driver_type = prof_driver_type;

        this.push_registered_id = push_registered_id;

    }

    public UserInfo(String first_name, String last_name, String email, String password, String phoneNo,
                    String birthday, String gent, String passenger_type, String driver_type, String prof_driver_type,
                    String image_file_path, String house_no, String street, String post_code, String location,
                    String country, String address_optional, String push_registered_id) {
        this.first_name = first_name;
        this.last_name = last_name;
        this.email = email;
        this.password = password;
        this.phoneNo = phoneNo;
        this.birthday = birthday;
        this.gent = gent;
        this.passenger_type = passenger_type;
        this.driver_type = driver_type;
        this.prof_driver_type = prof_driver_type;
        this.image_file_path = image_file_path;
        this.house_no = house_no;
        this.street = street;
        this.post_code = post_code;
        this.location = location;
        this.country = country;
        this.address_optional = address_optional;
        this.push_registered_id = push_registered_id;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.first_name);
        dest.writeString(this.last_name);
        dest.writeString(this.email);
        dest.writeString(this.password);
        dest.writeString(this.phoneNo);
        dest.writeString(this.birthday);
        dest.writeString(this.gent);
        dest.writeString(this.passenger_type);
        dest.writeString(this.driver_type);
        dest.writeString(this.prof_driver_type);
        dest.writeString(this.image_file_path);
        dest.writeString(this.house_no);
        dest.writeString(this.street);
        dest.writeString(this.post_code);
        dest.writeString(this.location);
        dest.writeString(this.country);
        dest.writeString(this.address_optional);
        dest.writeString(this.push_registered_id);
        dest.writeString(this.user_id);
    }

    public UserInfo() {
    }

    protected UserInfo(Parcel in) {
        this.first_name = in.readString();
        this.last_name = in.readString();
        this.email = in.readString();
        this.password = in.readString();
        this.phoneNo = in.readString();
        this.birthday = in.readString();
        this.gent = in.readString();
        this.passenger_type = in.readString();
        this.driver_type = in.readString();
        this.prof_driver_type = in.readString();
        this.image_file_path = in.readString();
        this.house_no = in.readString();
        this.street = in.readString();
        this.post_code = in.readString();
        this.location = in.readString();
        this.country = in.readString();
        this.address_optional = in.readString();
        this.push_registered_id = in.readString();
        this.user_id = in.readString();
    }

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel source) {
            return new UserInfo(source);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };

//    @Override
//    public String toString() {
//       return new Gson().fr
//    }
}