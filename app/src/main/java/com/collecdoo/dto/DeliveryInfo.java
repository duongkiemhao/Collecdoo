package com.collecdoo.dto;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DeliveryInfo implements Parcelable {

    public static final Creator<DeliveryInfo> CREATOR = new Creator<DeliveryInfo>() {
        @Override
        public DeliveryInfo createFromParcel(Parcel source) {
            return new DeliveryInfo(source);
        }

        @Override
        public DeliveryInfo[] newArray(int size) {
            return new DeliveryInfo[size];
        }
    };
    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("pickup_info")
    @Expose
    private String pickupInfo;
    @SerializedName("lat1")
    @Expose
    private String lat1;
    @SerializedName("lon1")
    @Expose
    private String lon1;
    @SerializedName("drop_info")
    @Expose
    private String dropInfo;
    @SerializedName("lat2")
    @Expose
    private String lat2;
    @SerializedName("lon2")
    @Expose
    private String lon2;
    @SerializedName("parcel_size")
    @Expose
    private String parcelSize;
    @SerializedName("estimated_distance")
    @Expose
    private String estimatedDistance;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("desired_pickup_time")
    @Expose
    private String desiredPickupTime;
    @SerializedName("desired_drop_time")
    @Expose
    private String desiredDropTime;
    @SerializedName("delivery_booking_id")
    @Expose
    private String delivery_booking_id;

    public DeliveryInfo() {
    }


    protected DeliveryInfo(Parcel in) {
        this.userId = in.readString();
        this.pickupInfo = in.readString();
        this.lat1 = in.readString();
        this.lon1 = in.readString();
        this.dropInfo = in.readString();
        this.lat2 = in.readString();
        this.lon2 = in.readString();
        this.parcelSize = in.readString();
        this.estimatedDistance = in.readString();
        this.description = in.readString();
        this.desiredPickupTime = in.readString();
        this.desiredDropTime = in.readString();
        this.delivery_booking_id = in.readString();
    }

    public String getDelivery_booking_id() {
        return delivery_booking_id;
    }

    public void setDelivery_booking_id(String delivery_booking_id) {
        this.delivery_booking_id = delivery_booking_id;
    }

    /**
     * @return The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * @param userId The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * @return The pickupInfo
     */
    public String getPickupInfo() {
        return pickupInfo;
    }

    /**
     * @param pickupInfo The pickup_info
     */
    public void setPickupInfo(String pickupInfo) {
        this.pickupInfo = pickupInfo;
    }

    /**
     * @return The lat1
     */
    public String getLat1() {
        return lat1;
    }

    /**
     * @param lat1 The lat1
     */
    public void setLat1(String lat1) {
        this.lat1 = lat1;
    }

    /**
     * @return The lon1
     */
    public String getLon1() {
        return lon1;
    }

    /**
     * @param lon1 The lon1
     */
    public void setLon1(String lon1) {
        this.lon1 = lon1;
    }

    /**
     * @return The dropInfo
     */
    public String getDropInfo() {
        return dropInfo;
    }

    /**
     * @param dropInfo The drop_info
     */
    public void setDropInfo(String dropInfo) {
        this.dropInfo = dropInfo;
    }

    /**
     * @return The lat2
     */
    public String getLat2() {
        return lat2;
    }

    /**
     * @param lat2 The lat2
     */
    public void setLat2(String lat2) {
        this.lat2 = lat2;
    }

    /**
     * @return The lon2
     */
    public String getLon2() {
        return lon2;
    }

    /**
     * @param lon2 The lon2
     */
    public void setLon2(String lon2) {
        this.lon2 = lon2;
    }

    /**
     * @return The parcelSize
     */
    public String getParcelSize() {
        return parcelSize;
    }

    /**
     * @param parcelSize The parcel_size
     */
    public void setParcelSize(String parcelSize) {
        this.parcelSize = parcelSize;
    }

    /**
     * @return The estimatedDistance
     */
    public String getEstimatedDistance() {
        return estimatedDistance;
    }

    /**
     * @param estimatedDistance The estimated_distance
     */
    public void setEstimatedDistance(String estimatedDistance) {
        this.estimatedDistance = estimatedDistance;
    }

    /**
     * @return The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description The description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return The desiredPickupTime
     */
    public String getDesiredPickupTime() {
        return desiredPickupTime;
    }

    /**
     * @param desiredPickupTime The desired_pickup_time
     */
    public void setDesiredPickupTime(String desiredPickupTime) {
        this.desiredPickupTime = desiredPickupTime;
    }

    /**
     * @return The desiredDropTime
     */
    public String getDesiredDropTime() {
        return desiredDropTime;
    }

    /**
     * @param desiredDropTime The desired_drop_time
     */
    public void setDesiredDropTime(String desiredDropTime) {
        this.desiredDropTime = desiredDropTime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeString(this.pickupInfo);
        dest.writeString(this.lat1);
        dest.writeString(this.lon1);
        dest.writeString(this.dropInfo);
        dest.writeString(this.lat2);
        dest.writeString(this.lon2);
        dest.writeString(this.parcelSize);
        dest.writeString(this.estimatedDistance);
        dest.writeString(this.description);
        dest.writeString(this.desiredPickupTime);
        dest.writeString(this.desiredDropTime);
        dest.writeString(this.delivery_booking_id);
    }

    @Override
    public String toString() {
        return "DeliveryInfo{" +
                "userId='" + userId + '\'' +
                ", pickupInfo='" + pickupInfo + '\'' +
                ", lat1='" + lat1 + '\'' +
                ", lon1='" + lon1 + '\'' +
                ", dropInfo='" + dropInfo + '\'' +
                ", lat2='" + lat2 + '\'' +
                ", lon2='" + lon2 + '\'' +
                ", parcelSize='" + parcelSize + '\'' +
                ", estimatedDistance='" + estimatedDistance + '\'' +
                ", description='" + description + '\'' +
                ", desiredPickupTime='" + desiredPickupTime + '\'' +
                ", desiredDropTime='" + desiredDropTime + '\'' +
                ", delivery_booking_id='" + delivery_booking_id + '\'' +
                '}';
    }
}

