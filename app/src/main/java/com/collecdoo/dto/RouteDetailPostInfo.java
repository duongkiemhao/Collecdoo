package com.collecdoo.dto;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by kiemhao on 7/2/16.
 */
public class RouteDetailPostInfo {

    @SerializedName("route_id")
    @Expose
    public String routeId;

    @SerializedName("signature")
    @Expose
    public String signature;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("address")
    @Expose
    public String address;
    @SerializedName("telephone")
    @Expose
    public String telephone;

}
