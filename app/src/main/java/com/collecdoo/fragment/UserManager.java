package com.collecdoo.fragment;

import com.collecdoo.MyPreference;
import com.collecdoo.dto.BankInfo;
import com.collecdoo.dto.UserInfo;

/**
 * Created by kiemhao on 12/18/16.
 */

public class UserManager {

    private static UserManager instance;

    public UserInfo getUserInfo() {
        return (UserInfo) MyPreference.getObject("userInfo", UserInfo.class);
    }

    public void setUserInfo(UserInfo userInfo) {
        MyPreference.setObject("userInfo", userInfo);

    }

    public BankInfo getBankInfo() {
        BankInfo bankInfo=(BankInfo) MyPreference.getObject("bankInfo", BankInfo.class);
        return bankInfo==null?new BankInfo():bankInfo;
    }

    public void setBankInfo(BankInfo userInfo) {
        MyPreference.setObject("bankInfo", userInfo);

    }

    //private UserInfo userInfo;

    public static UserManager getInstance(){
        if(instance==null)
            instance=new UserManager();
        return instance;
    }
}
