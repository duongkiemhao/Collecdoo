package com.collecdoo.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kiemhao on 7/2/16.
 */
public class PathOfRouteInfo implements Parcelable {

    public static final Creator<PathOfRouteInfo> CREATOR = new Creator<PathOfRouteInfo>() {
        @Override
        public PathOfRouteInfo createFromParcel(Parcel source) {
            return new PathOfRouteInfo(source);
        }

        @Override
        public PathOfRouteInfo[] newArray(int size) {
            return new PathOfRouteInfo[size];
        }
    };
    @SerializedName("route_detail_id")
    @Expose
    public String routeDetailId;
    @SerializedName("destination_info")
    @Expose
    public String destinationInfo;
    @SerializedName("lat")
    @Expose
    public String lat;
    @SerializedName("lon")
    @Expose
    public String lon;
    @SerializedName("notes")
    @Expose
    public String notes;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("is_delivery")
    @Expose
    public String is_delivery;
    @SerializedName("customer_name")
    @Expose
    public String customer_name;
    @SerializedName("telephone")
    @Expose
    public String telephone;

    public PathOfRouteInfo() {
    }

    protected PathOfRouteInfo(Parcel in) {
        this.routeDetailId = in.readString();
        this.destinationInfo = in.readString();
        this.lat = in.readString();
        this.lon = in.readString();
        this.notes = in.readString();
        this.status = in.readString();
        this.is_delivery = in.readString();
        this.customer_name = in.readString();
        this.telephone = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.routeDetailId);
        dest.writeString(this.destinationInfo);
        dest.writeString(this.lat);
        dest.writeString(this.lon);
        dest.writeString(this.notes);
        dest.writeString(this.status);
        dest.writeString(this.is_delivery);
        dest.writeString(this.customer_name);
        dest.writeString(this.telephone);
    }
}
