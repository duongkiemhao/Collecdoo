package com.collecdoo;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Point;
import android.location.Address;
import android.location.Geocoder;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;

import com.collecdoo.interfaces.HomeListener;
import com.google.android.gms.maps.model.LatLng;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Utility {

    public static void showMessage(Context context, String message) {

        if (context == null)
            return;
        if (TextUtils.isEmpty(message))
            return;
        //showDialog(context, message);
        //Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        showDefaultSnackbar(context,
                message, Snackbar.LENGTH_SHORT);

    }

    public static void showMessage(Context context, int resourceId) {

        if (context == null)
            return;
        if (TextUtils.isEmpty(context.getResources().getString(resourceId)))
            return;
        //showDialog(context, message);
        //Toast.makeText(context, context.getResources().getString(resourceId), Toast.LENGTH_SHORT).show();
        showMessage(context, context.getResources().getString(resourceId));
    }

    public static void showShortMessage(Context context, String message) {
        // MyApplicationContext.log(message);
        if (context == null)
            return;
        if (TextUtils.isEmpty(message))
            return;
        showDefaultSnackbar(context
                , message, Snackbar.LENGTH_SHORT);
        //showDialog(context, message);
        // Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showException(Context context, Exception exp) {
        if (context == null)
            return;
        MyApplicationContext.log(exp);
        //showMessage(context, exp.getMessage());
        showMessage(context, "Unable to retrieve data");

    }

    public static void showOfflineMessage(Context context) {
        //showDialog(context, context.getString(R.string.offline_connect_msg));
        showMessage(context, "Unable to retrieve data");
    }

    public static void setListViewAutoHeight(Context context,
                                             BaseAdapter baseAdapter, ListView listview) {
        Integer lvHeight = 0;
        for (int i = 0; i < baseAdapter.getCount(); i++) {
            View listItem = baseAdapter.getView(i, null, listview);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            lvHeight += listItem.getMeasuredHeight();
        }
        LayoutParams params = listview.getLayoutParams();
        params.height = lvHeight
                + (listview.getDividerHeight() * (baseAdapter.getCount() - 1))
                + 5;
        listview.setLayoutParams(params);
        listview.requestLayout();
    }

    public static void setExpandableListViewAutoHeight(Context context,
                                                       BaseExpandableListAdapter baseAdapter, ExpandableListView listview) {

        Integer lvHeight = 0;
        int childCount = 0;

        for (int i = 0; i < baseAdapter.getGroupCount(); i++) {

            ViewGroup parent = (ViewGroup) baseAdapter.getGroupView(i, true,
                    null, listview);
            if (parent instanceof ViewGroup) {
                parent.setLayoutParams(new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
            parent.measure(0, 0);
            lvHeight += parent.getMeasuredHeight();

            for (int y = 0; y < baseAdapter.getChildrenCount(i); y++) {
                View childItem = baseAdapter.getChildView(i, y, true, null,
                        parent);
                if (childItem instanceof ViewGroup) {
                    childItem.setLayoutParams(new LayoutParams(
                            LayoutParams.WRAP_CONTENT,
                            LayoutParams.WRAP_CONTENT));
                }
                childItem.measure(0, 0);

                lvHeight += childItem.getMeasuredHeight();
                childCount++;
            }
        }
        if (baseAdapter.getGroupCount() > 0) {
            LayoutParams params = listview.getLayoutParams();
            params.height = lvHeight
                    + (listview.getDividerHeight() * baseAdapter
                    .getGroupCount())
                    + (listview.getDividerHeight() * childCount);
            listview.setLayoutParams(params);
        }
        if (lvHeight == 0)
            listview.setVisibility(View.GONE);
        else
            listview.setVisibility(View.VISIBLE);
        listview.requestLayout();
    }

    public static void setGridViewAutoHeight(Context context,
                                             BaseAdapter baseAdapter, GridView gridview) {

        View lastChild = gridview.getChildAt(gridview.getChildCount() - 1);
        LayoutParams params = gridview.getLayoutParams();
        params.height = lastChild.getHeight() * 7;
        gridview.setLayoutParams(params);
        gridview.requestLayout();
    }


    public static void toFragmentWithoutBackstack(
            FragmentActivity fragmentActivity, Fragment sherlockFragment,
            String tagFragmentNew, int fragmentId) {
        try {
            final FragmentManager fm = fragmentActivity
                    .getSupportFragmentManager();
            final FragmentTransaction ft = fm.beginTransaction();
            Fragment old = fm.findFragmentByTag(tagFragmentNew);
            if (old != null) {

                ft.remove(old);
            }
            ft.replace(fragmentId, sherlockFragment, tagFragmentNew);

            ft.commit();
            hideKeyboard(fragmentActivity);
        } catch (Exception exp) {
            Utility.showException(fragmentActivity, exp);
        }

    }
//
//    public static void toFragmentWithoutBackstack(
//            FragmentActivity fragmentActivity, Fragment sherlockFragment,
//            String tagFragmentNew, int fragmentId, boolean isNext) {
//        try {
//            final FragmentManager fm = fragmentActivity
//                    .getSupportFragmentManager();
//            final FragmentTransaction ft = fm.beginTransaction();
//            Fragment old = fm.findFragmentByTag(tagFragmentNew);
//            if (old != null) {
//
//                ft.remove(old);
//            }
//
//            ft.setCustomAnimations(isNext ? R.anim.slide_in_left
//                    : R.anim.slide_in_right, isNext ? R.anim.slide_out_right
//                    : R.anim.slide_out_left);
//            ft.replace(fragmentId, sherlockFragment, tagFragmentNew);
//            ft.commit();
//            hideKeyboard(fragmentActivity);
//        } catch (Exception exp) {
//            Utility.showException(fragmentActivity, exp);
//        }
//
//    }

    public static void hideKeyboard(final Context context) {
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    InputMethodManager inputManager = (InputMethodManager) context
                            .getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputManager.hideSoftInputFromWindow(((Activity) context)
                                    .getCurrentFocus().getWindowToken(),
                            InputMethodManager.HIDE_NOT_ALWAYS);
                }
            }).run();

        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    public static String formatWebViewContent(String content) {

        content = content.replace("#", "%23").replace("%", "%25")
                .replace("\\", "%27").replace("?", "%3f");
        content = "<html><head>"
                + "<meta http-equiv=\"content-type\" content=\"text/html; charset=utf-8\" />"
                + "<head><body>" + content
                + "</body>";
        return content;

    }

    public static String getMacAddess() {

        WifiManager manager = (WifiManager) MyApplicationContext.getInstance()
                .getAppContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo info = manager.getConnectionInfo();
        return info.getMacAddress() != null ? info.getMacAddress()
                : "123456789";

    }

    public static String getTokenKey(String key, String message) {
        String sEncodedString = null;
        try {
            SecretKeySpec secKey = new SecretKeySpec((key).getBytes("UTF-8"),
                    "HmacMD5");
            Mac mac = Mac.getInstance("HmacMD5");
            mac.init(secKey);

            byte[] bytes = mac.doFinal(message.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();

            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            sEncodedString = hash.toString();
        } catch (UnsupportedEncodingException e) {
        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return sEncodedString;
    }

    public static long getFileFolderSize(File dir) {
        long size = 0;
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if (file.isFile()) {
                    size += file.length();
                } else
                    size += getFileFolderSize(file);
            }
        } else if (dir.isFile()) {
            size += dir.length();
        }
        return size;
    }

    public static void showNetworkErrorMessage(Context context) {
        showMessage(context, context.getString(R.string.offline_connect_msg));
    }


    public static float convertDPtoPIXEL(int typeValue, int number) {
        DisplayMetrics metrics = MyApplicationContext.getInstance()
                .getAppContext().getResources().getDisplayMetrics();
        return TypedValue.applyDimension(typeValue, number, metrics);
    }

    /**
     * @return Application's version code from the {@code PackageManager}.
     */
    public static int getAppVersionCode(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static String getAppVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static boolean isHigher2x() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB)
            return true;
        else
            return false;
    }

    public static int getActionHeight() {
        if (isHigher2x()) {

            final TypedArray styledAttributes = MyApplicationContext
                    .getInstance()
                    .getAppContext()
                    .getTheme()
                    .obtainStyledAttributes(
                            new int[]{android.R.attr.actionBarSize});
            int mActionBarSize = (int) styledAttributes.getDimension(0, 0);
            styledAttributes.recycle();
            return mActionBarSize;
        } else
            return Math.round(Utility.convertDPtoPIXEL(
                    TypedValue.COMPLEX_UNIT_DIP, 48));

    }

    public static String getFormat2Character(int value) {
        if (value < 10)
            return "0" + value;
        else
            return value + "";
    }


    public static void unbindDrawables(View view) {
        if (view.getBackground() != null) {
            view.getBackground().setCallback(null);
        }
        if (view instanceof ViewGroup && !(view instanceof AdapterView)) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                unbindDrawables(((ViewGroup) view).getChildAt(i));
            }
            ((ViewGroup) view).removeAllViews();
        }
    }

    public static Point getScreenSize(Context context) {
        Display display = ((Activity) context).getWindowManager()
                .getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size;
    }

//    public  static void showSnackbar(View view,String text){
//        final Snackbar snackbar = Snackbar
//                .make(view, text, Snackbar.LENGTH_LONG);
//        snackbar.setAction("Dismiss", new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                snackbar.dismiss();
//            }
//        });
//        snackbar.setDuration(Snackbar.LENGTH_INDEFINITE);
//        snackbar.show();
//    }

//
//    public static Snackbar showIndefiniteSnackbar(Context context) {
//        View view = ((AppCompatActivity) context).getWindow().getDecorView().findViewById(R.id.fragment);
//        final Snackbar snackbar = Snackbar
//                .make(view, "", Snackbar.LENGTH_INDEFINITE);
//
//        ViewGroup snackBarView = (ViewGroup) snackbar.getView();
//        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);
//        textView.setVisibility(View.INVISIBLE);
//        View progressView = LayoutInflater.from(context).inflate(R.layout.init_progress_dialog, null);
//        snackBarView.addView(progressView, 0);
//        snackbar.show();
//        return snackbar;
//
//    }

    public static void showDefaultSnackbar(Context context, String text, int length) {
        View view = ((AppCompatActivity) context).getWindow().getDecorView().findViewById(R.id.fragment);
        final Snackbar snackbar = Snackbar
                .make(view, text, length);
        ViewGroup snackBarView = (ViewGroup) snackbar.getView();
        TextView textView = (TextView) snackBarView.findViewById(android.support.design.R.id.snackbar_text);


        snackBarView.setBackgroundColor(Color.BLACK);
                textView.setBackgroundColor(Color.BLACK);
                textView.setTextColor(Color.WHITE);


        snackbar.show();
    }

    public static String getAddressFromLatLng(Context context,LatLng latLng) throws IOException,NullPointerException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(context, Locale.getDefault());

//        LatLng latLng=((HomeListener)fragment.getParentFragment()).getLatLng();
        addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        if (addresses.size() > 0)
        {
            String yourAddress = addresses.get(0).getAddressLine(0);
            String yourCity = addresses.get(0).getAddressLine(1);
            String yourCountry = addresses.get(0).getAddressLine(2);
            return yourAddress+","+yourCity+","+yourCountry;
        }
        else return "";
    }

    public static double calculationByDistance(LatLng StartP, LatLng EndP) {
        int Radius = 6371;// radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon / 2)
                * Math.sin(dLon / 2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult = Radius * c;
        double km = valueResult / 1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec = Integer.valueOf(newFormat.format(km));
        double meter = valueResult % 1000;
        int meterInDec = Integer.valueOf(newFormat.format(meter));
        Log.d("Utility", "Radius Value  " + valueResult + "   KM  " + kmInDec
                + " Meter   " + meterInDec);

        return (int)(Radius * c*1000);
        //return meterInDec;
    }
}
