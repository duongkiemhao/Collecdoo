package com.collecdoo.interfaces;

import com.collecdoo.dto.PathOfRouteInfo;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;

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

}
