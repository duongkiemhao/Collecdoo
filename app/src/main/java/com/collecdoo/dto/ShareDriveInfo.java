package com.collecdoo.dto;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ShareDriveInfo {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("from_address")
    @Expose
    private String fromAddress;
    @SerializedName("lat1")
    @Expose
    private String lat1;
    @SerializedName("lon1")
    @Expose
    private String lon1;
    @SerializedName("to_address")
    @Expose
    private String toAddress;
    @SerializedName("lat2")
    @Expose
    private String lat2;
    @SerializedName("lon2")
    @Expose
    private String lon2;
    @SerializedName("free_seats")
    @Expose
    private String freeSeats;
    @SerializedName("own_distance")
    @Expose
    private String ownDistance;
    @SerializedName("desired_pickup_time")
    @Expose
    private String desiredPickupTime;
    @SerializedName("desired_drop_time")
    @Expose
    private String desiredDropTime;
    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("share_drive_id")
    @Expose
    private String share_drive_id;

    public ShareDriveInfo() {

    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getShare_drive_id() {
        return share_drive_id;
    }

    public void setShare_drive_id(String share_drive_id) {
        this.share_drive_id = share_drive_id;
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
     * @return The fromAddress
     */
    public String getFromAddress() {
        return fromAddress;
    }

    /**
     * @param fromAddress The from_address
     */
    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
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
     * @return The toAddress
     */
    public String getToAddress() {
        return toAddress;
    }

    /**
     * @param toAddress The to_address
     */
    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
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
     * @return The freeSeats
     */
    public String getFreeSeats() {
        return freeSeats;
    }

    /**
     * @param freeSeats The free_seats
     */
    public void setFreeSeats(String freeSeats) {
        this.freeSeats = freeSeats;
    }

    /**
     * @return The ownDistance
     */
    public String getOwnDistance() {
        return ownDistance;
    }

    /**
     * @param ownDistance The own_distance
     */
    public void setOwnDistance(String ownDistance) {
        this.ownDistance = ownDistance;
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
    public String toString() {
        return "ShareDriveInfo{" +
                "userId='" + userId + '\'' +
                ", fromAddress='" + fromAddress + '\'' +
                ", lat1='" + lat1 + '\'' +
                ", lon1='" + lon1 + '\'' +
                ", toAddress='" + toAddress + '\'' +
                ", lat2='" + lat2 + '\'' +
                ", lon2='" + lon2 + '\'' +
                ", freeSeats='" + freeSeats + '\'' +
                ", ownDistance='" + ownDistance + '\'' +
                ", desiredPickupTime='" + desiredPickupTime + '\'' +
                ", desiredDropTime='" + desiredDropTime + '\'' +
                ", type='" + type + '\'' +
                ", share_drive_id='" + share_drive_id + '\'' +
                '}';
    }


}

