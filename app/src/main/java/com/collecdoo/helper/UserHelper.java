package com.collecdoo.helper;

import com.collecdoo.MyPreference;
import com.collecdoo.dto.UserInfo;

/**
 * Created by kiemhao on 6/30/16.
 */
public class UserHelper {
    public static String getUserId(){
        UserInfo userInfo= (UserInfo) MyPreference.getObject("userInfo",UserInfo.class);
        return userInfo.user_id;
    }
    public static UserInfo getUserInfo(){
        return (UserInfo) MyPreference.getObject("userInfo",UserInfo.class);

    }
    public static boolean isDriver(){
        return UserHelper.getUserInfo().driver_type.equals("0")?false:true;
    }
}
