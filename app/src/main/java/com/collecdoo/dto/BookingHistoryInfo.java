package com.collecdoo.dto;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BookingHistoryInfo implements Parcelable {

    @SerializedName("booking_id")
    @Expose
    public String bookingId;
    @SerializedName("created_on")
    @Expose
    public String createdOn;
    @SerializedName("pickup_info")
    @Expose
    public String pickupInfo;
    @SerializedName("drop_info")
    @Expose
    public String dropInfo;
    @SerializedName("desired_pickup_time")
    @Expose
    public String desiredPickupTime;
    @SerializedName("desired_drop_time")
    @Expose
    public String desiredDropTime;
    @SerializedName("real_pickup_time")
    @Expose
    public String realPickupTime;
    @SerializedName("real_drop_time")
    @Expose
    public String realDropTime;
    @SerializedName("rating")
    @Expose
    public String rating;
    @SerializedName("notes")
    @Expose
    public String notes;
    @SerializedName("fare")
    @Expose
    public String fare;
    @SerializedName("type")
    @Expose
    public String type;
    @SerializedName("status")
    @Expose
    public String status;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.bookingId);
        dest.writeString(this.createdOn);
        dest.writeString(this.pickupInfo);
        dest.writeString(this.dropInfo);
        dest.writeString(this.desiredPickupTime);
        dest.writeString(this.desiredDropTime);
        dest.writeString(this.realPickupTime);
        dest.writeString(this.realDropTime);
        dest.writeString(this.rating);
        dest.writeString(this.notes);
        dest.writeString(this.fare);
        dest.writeString(this.type);
        dest.writeString(this.status);
    }

    public BookingHistoryInfo() {
    }

    protected BookingHistoryInfo(Parcel in) {
        this.bookingId = in.readString();
        this.createdOn = in.readString();
        this.pickupInfo = in.readString();
        this.dropInfo = in.readString();
        this.desiredPickupTime = in.readString();
        this.desiredDropTime = in.readString();
        this.realPickupTime = in.readString();
        this.realDropTime = in.readString();
        this.rating = in.readString();
        this.notes = in.readString();
        this.fare = in.readString();
        this.type = in.readString();
        this.status = in.readString();
    }

    public static final Parcelable.Creator<BookingHistoryInfo> CREATOR = new Parcelable.Creator<BookingHistoryInfo>() {
        @Override
        public BookingHistoryInfo createFromParcel(Parcel source) {
            return new BookingHistoryInfo(source);
        }

        @Override
        public BookingHistoryInfo[] newArray(int size) {
            return new BookingHistoryInfo[size];
        }
    };

    public String getBookingId() {
        return bookingId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public String getDesiredDropTime() {
        return desiredDropTime!=null?desiredDropTime:"";
    }

    public String getDesiredPickupTime() {
        return desiredPickupTime!=null?desiredPickupTime:"";
    }

    public String getDropInfo() {
        return dropInfo;
    }

    public String getFare() {
        return fare!=null?fare+" Eur":"0 Eur";
    }

    public String getNotes() {
        return notes!=null?notes:"";
    }

    public String getPickupInfo() {
        return pickupInfo;
    }

    public String getRating() {
        return rating!=null?rating:"";
    }

    public String getRealDropTime() {
        return realDropTime!=null?realDropTime:"";
    }

    public String getRealPickupTime() {
        return realPickupTime!=null?realPickupTime:"";
    }

    public String getStatus() {
        return status;
    }

    public String getType() {
        return type;
    }
}