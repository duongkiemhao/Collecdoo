package com.collecdoo.dto;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BookingHistoryPostInfo {

    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("from_time")
    @Expose
    public String fromTime;
    @SerializedName("to_time")
    @Expose
    public String toTime;
    @SerializedName("page_number")
    @Expose
    public String pageNumber;
    @SerializedName("page_size")
    @Expose
    public String pageSize;

}