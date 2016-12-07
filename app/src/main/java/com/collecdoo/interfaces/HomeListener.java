package com.collecdoo.interfaces;

import com.google.android.gms.maps.model.LatLng;

/**
 * Created by kiemhao on 5/15/16.
 */
public interface HomeListener {
    void changeIcon(int index, int resId);

    void hideNavigationBar();

    void showNavigationBar();

    void updateDriverActivity();

    LatLng getLatLng();

    void logOut();

    void onGotPush(String pushId);

}
