package com.collecdoo;

import android.content.Context;
import android.content.SharedPreferences;

import com.collecdoo.control.GsonGenericList;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by kiemhao on 2/25/16.
 */
public class MyPreference {


    public static SharedPreferences getDefault() {
        String packageName = MyApplicationContext.getInstance().getAppContext()
                .getPackageName();
        return MyApplicationContext
                .getInstance()
                .getAppContext()
                .getSharedPreferences(packageName, Context.MODE_WORLD_WRITEABLE);

    }


    public static void setString(String key, String value) {
        getDefault().edit().putString(key, value).commit();
    }

    public static String getString(String key) {
        return getDefault().getString(key, "");
    }

    public static void setInt(String key, int value) {
        getDefault().edit().putInt(key, value).commit();
    }

    public static int getInt(String key) {
        return getDefault().getInt(key, 0);
    }

    public static void setBoolean(String key, boolean value) {
        getDefault().edit().putBoolean(key, value).commit();
    }

    public static boolean getBoolean(String key) {
        return getDefault().getBoolean(key, false);
    }


    public static void setListObject(String key, List<?> listObject) {
        Gson gson = new Gson();
        getDefault().edit()
                .putString(key, gson.toJson(listObject)).commit();
    }

    //
//    public static void setHashsetObject(String key, HashSet<?> listObject) {
//        Gson gson = new Gson();
//        getDefault().edit()
//                .putString(key, gson.toJson(listObject)).commit();
//    }
    public static <T> ArrayList<T> getListObject(String key, Class<T> classType) {
        try {
            String result = getDefault().getString(key, "");
            Gson gson = new Gson();
            return gson.fromJson(result, new GsonGenericList<T>(classType));
        } catch (Exception exp) {
            return null;
        }
    }

//    public static <T> HashSet<HashMap<String, String>> getListHashMap(String key, Class<T> classType) {
//        try {
//            String result = getDefault().getString(key, "");
//            Gson gson = new Gson();
//            return gson.fromJson(result, new GsonGenericList<HashSet>((Class<HashSet>) classType));
//        } catch (Exception exp) {
//            exp.printStackTrace();
//            return null;
//        }
//    }


    public static void setObject(String key, Object value) {
        Gson gson = new Gson();
        getDefault().edit().putString(key, gson.toJson(value))
                .commit();
    }

    public static Object getObject(String key, Class classType) {
        String result = getDefault().getString(key, "");
        Gson gson = new Gson();
        return gson.fromJson(result, classType);
    }

    public static HashSet getObjectHashsetMap(String key, Class classType) {
        String result = getDefault().getString(key, "");
        Gson gson = new Gson();
        HashSet<HashMap> object = (HashSet<HashMap>) gson.fromJson(result, classType);
        if (object == null) {
            HashSet<HashMap> hashSet = new HashSet<HashMap>();
            setObject(key, hashSet);
            return hashSet;
        } else return object;
    }
}
