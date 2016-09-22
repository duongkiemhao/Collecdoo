package com.collecdoo;


import com.collecdoo.dto.BookingHistoryPostInfo;
import com.collecdoo.dto.DeliveryInfo;
import com.collecdoo.dto.DrivePostInfo;
import com.collecdoo.dto.DriverActivityInfo;

import com.collecdoo.dto.MyJsonObject;
import com.collecdoo.dto.PathOfRouteInfo;
import com.collecdoo.dto.PushInfo;
import com.collecdoo.dto.ResponseInfo;
import com.collecdoo.dto.RouteDetailPostInfo;
import com.collecdoo.dto.ShareDriveInfo;
import com.collecdoo.dto.ShareTimeInfo;
import com.collecdoo.dto.UserInfo;
import com.collecdoo.fragment.home.ListOfDriveFragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.POST;

public interface MyRetrofitService {

    @POST("user")
    Call<ResponseInfo> register(@Body UserInfo userInfo);

    @POST("upgrade-driver")
    Call<ResponseInfo> updateDriver(@Body UserInfo userInfo);

    @POST("update-push-id")
    Call<ResponseInfo> updatePushId(@Body PushInfo pushInfo);

    @POST("login")
    Call<MyJsonObject<UserInfo>> login(@Body JsonObject jsonObject);

    @POST("booking/drive")
    Call<ResponseInfo> driveBooking(@Body DrivePostInfo drivePostInfo);

    @POST("booking/delivery")
    Call<ResponseInfo> deliveryBooking(@Body DeliveryInfo deliveryInfo);

    @POST("booking/share")
    Call<ResponseInfo> shareDrive(@Body ShareDriveInfo shareInfo);

    @POST("booking/time")
    Call<ResponseInfo> shareTime(@Body ShareTimeInfo shareInfo);

    @POST("driver/activity")
    Call<ResponseInfo> driverActivity(@Body DriverActivityInfo shareInfo);

    @POST("route/path")
    Call<List<PathOfRouteInfo>> getPathOfRoute(@Body JsonObject jsonObject);

    @POST("route/update-detail")
    Call<ResponseInfo> updateRouteDetail(@Body JsonObject jsonObject);

    @POST("route/cancel-route-detail")
    Call<ResponseInfo> cancelRouteDetail(@Body JsonObject jsonObject);

    @POST("route/update-signature")
    Call<ResponseInfo> updateSignature(@Body JsonObject jsonObject);

    @POST("booking/history")
    Call<ResponseInfo> bookingHistory(@Body BookingHistoryPostInfo postInfo);


}