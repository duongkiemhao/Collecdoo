package com.collecdoo.dto;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DrivePostInfo {

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
    @SerializedName("person_count")
    @Expose
    private String personCount;
    @SerializedName("estimated_distance")
    @Expose
    private String estimatedDistance;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("desired_pickup_time")
    @Expose
    private String desiredPickupTime;
    @SerializedName("desired_drop_time")
    @Expose
    private String desiredDropTime;
    @SerializedName("drive_booking_id")
    @Expose
    private String driveBookingId;
    @SerializedName("type")
    @Expose
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
     * @return The personCount
     */
    public String getPersonCount() {
        return personCount;
    }

    /**
     * @param personCount The person_count
     */
    public void setPersonCount(String personCount) {
        this.personCount = personCount;
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
     * @return The paymentMethod
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     * @param paymentMethod The payment_method
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
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

    /**
     * @return The driveBookingId
     */
    public String getDriveBookingId() {
        return driveBookingId;
    }

    /**
     * @param driveBookingId The drive_booking_id
     */
    public void setDriveBookingId(String driveBookingId) {
        this.driveBookingId = driveBookingId;
    }

    @Override
    public String toString() {
        return "DrivePostInfo{" +
                "userId='" + userId + '\'' +
                ", pickupInfo='" + pickupInfo + '\'' +
                ", lat1='" + lat1 + '\'' +
                ", lon1='" + lon1 + '\'' +
                ", dropInfo='" + dropInfo + '\'' +
                ", lat2='" + lat2 + '\'' +
                ", lon2='" + lon2 + '\'' +
                ", personCount='" + personCount + '\'' +
                ", estimatedDistance='" + estimatedDistance + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", description='" + description + '\'' +
                ", desiredPickupTime='" + desiredPickupTime + '\'' +
                ", desiredDropTime='" + desiredDropTime + '\'' +
                ", driveBookingId='" + driveBookingId + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
