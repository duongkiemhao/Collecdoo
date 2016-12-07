package com.collecdoo.fragment;

import android.location.Location;

import com.collecdoo.MyPreference;

/**
 * Created by kiemhao on 11/5/16.
 */

public class LocationManger {
    public static LocationManger instance = new LocationManger();
    private Location location;

    public static LocationManger getInstance() {
        return instance;
    }

    public Location getLocation() {
        if (location == null) {
            location = new Location("dummy");
            location.setLatitude(MyPreference.getObject("lat", Double.class) != null ? (double) MyPreference.getObject("lat", Double.class) : 0D);
            location.setLongitude(MyPreference.getObject("lon", Double.class) != null ? (double) MyPreference.getObject("lon", Double.class) : 0D);
        }
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        MyPreference.setObject("lat", location.getLatitude());
        MyPreference.setObject("lon", location.getLongitude());

    }
}
