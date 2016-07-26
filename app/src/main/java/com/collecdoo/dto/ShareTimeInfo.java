package com.collecdoo.dto;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

public class ShareTimeInfo  {

    @SerializedName("user_id")
    @Expose
    private String userId;
    @SerializedName("from_time")
    @Expose
    private String fromTime;
    @SerializedName("to_time")
    @Expose
    private String toTime;
    @SerializedName("lat1")
    @Expose
    private String lat1;
    @SerializedName("lon1")
    @Expose
    private String lon1;
    @SerializedName("start_point")
    @Expose
    private String startPoint;
    @SerializedName("seat_capacity")
    @Expose
    private String seatCapacity;
    @SerializedName("payment_method")
    @Expose
    private String paymentMethod;
    @SerializedName("desired_start_time")
    @Expose
    private String desiredStartTime;
    @SerializedName("desired_end_time")
    @Expose
    private String desiredEndTime;

    public ShareTimeInfo() {

    }

    /**
     *
     * @return
     * The userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     *
     * @param userId
     * The user_id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     *
     * @return
     * The fromTime
     */
    public String getFromTime() {
        return fromTime;
    }

    /**
     *
     * @param fromTime
     * The from_time
     */
    public void setFromTime(String fromTime) {
        this.fromTime = fromTime;
    }

    /**
     *
     * @return
     * The toTime
     */
    public String getToTime() {
        return toTime;
    }

    /**
     *
     * @param toTime
     * The to_time
     */
    public void setToTime(String toTime) {
        this.toTime = toTime;
    }

    /**
     *
     * @return
     * The lat1
     */
    public String getLat1() {
        return lat1;
    }

    /**
     *
     * @param lat1
     * The lat1
     */
    public void setLat1(String lat1) {
        this.lat1 = lat1;
    }

    /**
     *
     * @return
     * The lon1
     */
    public String getLon1() {
        return lon1;
    }

    /**
     *
     * @param lon1
     * The lon1
     */
    public void setLon1(String lon1) {
        this.lon1 = lon1;
    }

    /**
     *
     * @return
     * The startPoint
     */
    public String getStartPoint() {
        return startPoint;
    }

    /**
     *
     * @param startPoint
     * The start_point
     */
    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    /**
     *
     * @return
     * The seatCapacity
     */
    public String getSeatCapacity() {
        return seatCapacity;
    }

    /**
     *
     * @param seatCapacity
     * The seat_capacity
     */
    public void setSeatCapacity(String seatCapacity) {
        this.seatCapacity = seatCapacity;
    }

    /**
     *
     * @return
     * The paymentMethod
     */
    public String getPaymentMethod() {
        return paymentMethod;
    }

    /**
     *
     * @param paymentMethod
     * The payment_method
     */
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    /**
     *
     * @return
     * The desiredStartTime
     */
    public String getDesiredStartTime() {
        return desiredStartTime;
    }

    /**
     *
     * @param desiredStartTime
     * The desired_start_time
     */
    public void setDesiredStartTime(String desiredStartTime) {
        this.desiredStartTime = desiredStartTime;
    }

    /**
     *
     * @return
     * The desiredEndTime
     */
    public String getDesiredEndTime() {
        return desiredEndTime;
    }

    /**
     *
     * @param desiredEndTime
     * The desired_end_time
     */
    public void setDesiredEndTime(String desiredEndTime) {
        this.desiredEndTime = desiredEndTime;
    }


    @Override
    public String toString() {
        return "ShareTimeInfo{" +
                "userId='" + userId + '\'' +
                ", fromTime='" + fromTime + '\'' +
                ", toTime='" + toTime + '\'' +
                ", lat1='" + lat1 + '\'' +
                ", lon1='" + lon1 + '\'' +
                ", startPoint='" + startPoint + '\'' +
                ", seatCapacity='" + seatCapacity + '\'' +
                ", paymentMethod='" + paymentMethod + '\'' +
                ", desiredStartTime='" + desiredStartTime + '\'' +
                ", desiredEndTime='" + desiredEndTime + '\'' +
                '}';
    }


}

