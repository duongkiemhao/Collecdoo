package com.collecdoo.fragment.home;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collecdoo.MyPreference;
import com.collecdoo.MyRetrofitService;
import com.collecdoo.R;
import com.collecdoo.Utility;
import com.collecdoo.activity.MainActivity;
import com.collecdoo.config.Config;
import com.collecdoo.config.Constant;
import com.collecdoo.dto.DriverActivityInfo;
import com.collecdoo.dto.PathOfRouteInfo;
import com.collecdoo.dto.PushInfo;
import com.collecdoo.dto.ResponseInfo;
import com.collecdoo.dto.UserInfo;
import com.collecdoo.fragment.LocationManger;
import com.collecdoo.fragment.ServiceGenerator;
import com.collecdoo.fragment.pickup.DriverPickupDropFragment;
import com.collecdoo.fragment.pickup.DriverPickupHomeFragment;
import com.collecdoo.helper.UserHelper;
import com.collecdoo.interfaces.HomeListener;
import com.collecdoo.interfaces.HomeNavigationListener;
import com.collecdoo.interfaces.ParentFragmentListener;
import com.collecdoo.service.gcm.QuickstartPreferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.JsonObject;

import java.text.DateFormat;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class HomeWrapperFragment extends Fragment implements View.OnClickListener, HomeListener,
        ParentFragmentListener, LocationListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        ResultCallback<LocationSettingsResult> {
    @BindView(R.id.btnBack)
    View btnBack;
    @BindView(R.id.btnListDrive)
    View btnListDrive;
    @BindView(R.id.btnAccount)
    View btnAccount;
    @BindView(R.id.btnDriveRating)
    View btnDriveRating;
    @BindView(R.id.navigationBar)
    View navigationBar;
    @BindView(R.id.btnMap)
    View btnMap;
    @BindView(R.id.btnNext)
    View btnNext;
    private final String TAG = "--home--";

    private Unbinder unbinder;
    private String currentFragmentTag;

    //Location service
    protected static final int REQUEST_CHECK_SETTINGS = 0x1;
    protected LocationSettingsRequest mLocationSettingsRequest;

    private static final long INTERVAL = 1000 * 3;
    private static final long FASTEST_INTERVAL = 1000 * 1;
    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mCurrentLocation;
    private String mLastUpdateTime;
    private LatLng latLng;



    public static HomeWrapperFragment init() {
        HomeWrapperFragment registerFragment = new HomeWrapperFragment();
        Bundle bundle = new Bundle();

        registerFragment.setArguments(bundle);
        return registerFragment;
    }

    private Context context;

    public HomeWrapperFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_wrapper_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        btnBack.setOnClickListener(this);
        btnListDrive.setOnClickListener(this);
        btnAccount.setOnClickListener(this);
        btnDriveRating.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getChildFragmentManager().beginTransaction().
                replace(R.id.fragment, HomeFragment.init(), HomeFragment.class.getName()).
                commit();

        updatePushId(MyPreference.getString(QuickstartPreferences.TOKEN_STRING));
    }



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();


    }


    private void driverActivity(int actionIndex) {

        if (mCurrentLocation == null) {
            if(actionIndex==Config.ACTION_LOGOUT)
                toMainActivity();
            return;
        }
        DriverActivityInfo activityInfo = new DriverActivityInfo();
        activityInfo.user_id = UserHelper.getUserId();
        activityInfo.latitude = LocationManger.getInstance().getLocation().getLatitude()+ "";
        activityInfo.longitude = LocationManger.getInstance().getLocation().getLongitude()+ "";
        activityInfo.action = actionIndex + "";
        driverActivityTask(activityInfo);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnBack:
                HomeNavigationListener navigationListener = (HomeNavigationListener) getChildFragmentManager().findFragmentById(R.id.fragment);
                navigationListener.onBackClick();

                break;
            case R.id.btnListDrive:

                    FragmentManager fm = getChildFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();
                    if (fm.getBackStackEntryCount() > 0) {
                        Log.d(Constant.DEBUG_TAG, fm.getBackStackEntryCount() + "");
                        fm.popBackStack();
                    }

                    currentFragmentTag = fm.findFragmentById(R.id.fragment).getTag();
                    Fragment fragment = ListOfDriveFragment.init();
                    ft.hide(fm.findFragmentByTag(currentFragmentTag));
                    ft.add(R.id.fragment, fragment, ListOfDriveFragment.class.getName()).
                            addToBackStack(ListOfDriveFragment.class.getName());
                    ft.show(fragment);
                    ft.commit();

                break;
            case R.id.btnAccount:

                fm = getChildFragmentManager();
                ft = fm.beginTransaction();
                    if (fm.getBackStackEntryCount() > 0) {
                        Log.d(Constant.DEBUG_TAG, fm.getBackStackEntryCount() + "");
                        fm.popBackStack();
                    }

                    currentFragmentTag = fm.findFragmentById(R.id.fragment).getTag();
                fragment = AccountFragment.init();
                    ft.hide(fm.findFragmentByTag(currentFragmentTag));
                    ft.add(R.id.fragment, fragment, AccountFragment.class.getName()).
                            addToBackStack(AccountFragment.class.getName());
                    ft.show(fragment);
                    ft.commit();



                break;
            case R.id.btnDriveRating:


                break;
            case R.id.btnMap:
                navigationListener = (HomeNavigationListener) getChildFragmentManager().findFragmentById(R.id.fragment);
                navigationListener.onMapClick();
                break;
            case R.id.btnNext:
                navigationListener = (HomeNavigationListener) getChildFragmentManager().findFragmentById(R.id.fragment);
                navigationListener.onNextClick();
                break;

        }

    }

    @Override
    public void changeIcon(int index, int resId) {
        switch (index) {
            case 1:
                btnListDrive.setBackgroundResource(resId);
                break;
            case 2:
                btnAccount.setBackgroundResource(resId);
                break;
            case 3:
                btnDriveRating.setBackgroundResource(resId);
                break;
        }
    }

    @Override
    public void hideNavigationBar() {
        navigationBar.setVisibility(View.GONE);
    }

    @Override
    public void showNavigationBar() {
        navigationBar.setVisibility(View.VISIBLE);
    }

    @Override
    public String getCurrentTag() {
        return currentFragmentTag;
    }


    private boolean isGooglePlayServicesAvailable() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        if (ConnectionResult.SUCCESS == status) {
            return true;
        } else {
            GooglePlayServicesUtil.getErrorDialog(status, (Activity) context, 0).show();
            return false;
        }
    }

    private void initGoogleLocationService() {

        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        createLocationRequest();
        buildLocationSettingsRequest();
        checkLocationSettings();
    }

    //step 3
    protected void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();

        builder.addLocationRequest(mLocationRequest);
        builder.setAlwaysShow(true);
        mLocationSettingsRequest = builder.build();
    }

    protected void checkLocationSettings() {

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(
                        mGoogleApiClient,
                        mLocationSettingsRequest
                );

        result.setResultCallback(this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Log.d(Constant.DEBUG_TAG, "onConnected - isConnected ...............: " + mGoogleApiClient.isConnected());
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
        Log.d(Constant.DEBUG_TAG, "Location update started ..............: ");
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d(Constant.DEBUG_TAG, "Connection failed: " + connectionResult.toString());
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(Constant.DEBUG_TAG, "Firing onLocationChanged..............................................");
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        LocationManger.getInstance().setLocation(mCurrentLocation);
        updateUI();
    }

    private void updateUI() {
        Log.d(Constant.DEBUG_TAG, "UI update initiated .............");
        if (null != mCurrentLocation) {
            latLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
            if(UserHelper.isDriver())
            driverActivity(Config.ACTION_LOGIN);
        } else {
            Log.d(Constant.DEBUG_TAG, "location is null ...............");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient != null)
            stopLocationUpdates();

    }


    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            startLocationUpdates();
            Log.d(Constant.DEBUG_TAG, "Location update resumed .....................");
        }

    }


    protected void stopLocationUpdates() {
        if (mGoogleApiClient.isConnected())
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        Log.d(Constant.DEBUG_TAG, "Location update stopped .......................");
    }


    @Override
    public void onStart() {
        super.onStart();
        Log.d(Constant.DEBUG_TAG, "onStart fired ..............");

    }

    @Override
    public void onStop() {
        super.onStop();

        if (mGoogleApiClient != null) {
            Log.d(Constant.DEBUG_TAG, "onStop fired ..............");
            mGoogleApiClient.disconnect();
            Log.d(Constant.DEBUG_TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());
        }
    }


    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }


    @Override
    public void onResult(LocationSettingsResult locationSettingsResult) {

        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.i(Constant.DEBUG_TAG, "All location settings are satisfied.");


                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.i(Constant.DEBUG_TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    //Toast.makeText(FusedLocationWithSettingsDialog.this, "Location dialog will be open", Toast.LENGTH_SHORT).show();
                    //
                    Log.i(Constant.DEBUG_TAG, "Location dialog will be open");

                    //move to step 6 in onActivityResult to check what action user has taken on settings dialog
                    status.startResolutionForResult((Activity) context, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.i(Constant.DEBUG_TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.i(Constant.DEBUG_TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " +
                        "not created.");
                break;
        }
    }


    private void driverActivityTask(final DriverActivityInfo activityInfo) {

        MyRetrofitService taskService = ServiceGenerator.createService(MyRetrofitService.class);

        Call<ResponseInfo> call = taskService.driverActivity(activityInfo);
        call.enqueue(new Callback<ResponseInfo>() {

            @Override
            public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
                if(Integer.parseInt(activityInfo.action)==Config.ACTION_LOGOUT) {
                    toMainActivity();
                    return;
                }
                if(Integer.parseInt(activityInfo.action)==Config.ACTION_WORKING) {
                    return;
                }
                if (response.isSuccessful()) {
                    ResponseInfo responseInfo = response.body();
                    if (responseInfo.status.toLowerCase().equals("ok")) {
                        Log.d(TAG, "----action logined sent---");

                        if (mGoogleApiClient != null)
                            stopLocationUpdates();
                        mGoogleApiClient.disconnect();
                        Log.d(TAG, "isConnected ...............: " + mGoogleApiClient.isConnected());

                    } else Utility.showMessage(context, responseInfo.message);
                } else Utility.showMessage(context, response.message());
            }

            @Override
            public void onFailure(Call<ResponseInfo> call, Throwable t) {
                Log.d(Constant.DEBUG_TAG, "error" + t.getMessage());
                if(Integer.parseInt(activityInfo.action)==Config.ACTION_LOGOUT) {
                    toMainActivity();
                }

            }
        });

    }


    @Override
    public void updateDriverActivity() {
        if (UserHelper.isDriver()) {
            if (isGooglePlayServicesAvailable()) {
                initGoogleLocationService();
                stopLocationUpdates();
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public LatLng getLatLng() {
        return latLng;
    }

    @Override
    public void logOut() {
        if(UserHelper.isDriver())
            driverActivity(Config.ACTION_LOGOUT);
        else toMainActivity();
    }

    @Override
    public void onGotPush(String pushId) {
        if(UserHelper.isDriver())
            getPathOfRoute(pushId);
    }


    private void getPathOfRoute(String route_id) {

        MyRetrofitService taskService = ServiceGenerator.createService(MyRetrofitService.class);
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("route_id", route_id);
        Call<List<PathOfRouteInfo>> call = taskService.getPathOfRoute(jsonObject);

        call.enqueue(new Callback<List<PathOfRouteInfo>>() {

            @Override
            public void onResponse(Call<List<PathOfRouteInfo>> call, Response<List<PathOfRouteInfo>> response) {

                if (response.isSuccessful()) {

                    List<PathOfRouteInfo> pathOfRouteInfoList = response.body();

                    if (pathOfRouteInfoList.size() > 0) {
                        getFragmentManager().beginTransaction().
                                setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                                replace(R.id.fragment, DriverPickupHomeFragment.init(pathOfRouteInfoList.get(0).is_delivery.equals("0") ? true : false,
                                        pathOfRouteInfoList,0),
                                        DriverPickupDropFragment.class.getName()).
                                commit();
                    }


                } else Utility.showMessage(context, response.message());
            }

            @Override
            public void onFailure(Call<List<PathOfRouteInfo>> call, Throwable t) {

                Utility.showMessage(context, t.getMessage());
                Log.d(Constant.DEBUG_TAG, "error" + t.getMessage());
            }
        });

    }



    private void updatePushId(String token) {
        PushInfo pushInfo=new PushInfo();
        UserInfo userInfo= (UserInfo) MyPreference.getObject("userInfo",UserInfo.class);
        if(userInfo!=null){
            pushInfo.user_id=userInfo.user_id;
            pushInfo.push_registered_id=token;
            updatePush(pushInfo);
        }
    }


    private void updatePush(PushInfo pushInfo){

        MyRetrofitService taskService = ServiceGenerator.createService(MyRetrofitService.class);

        Call<ResponseInfo> call = taskService.updatePushId(pushInfo);
        call.enqueue(new Callback<ResponseInfo>() {

            @Override
            public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
                if(response.isSuccessful()){
                    ResponseInfo responseInfo= response.body();

                    if(responseInfo.status.toLowerCase().equals("ok")) {
                        Log.i(TAG, "update push to server ok");
                    }
                    else Log.i(TAG, "update push to server failed");
                }
                else Log.i(TAG, "update push to server failed");
            }

            @Override
            public void onFailure(Call<ResponseInfo> call, Throwable t) {
                Log.d(Constant.DEBUG_TAG,t.getMessage()!=null?t.getMessage():"");
            }
        });

    }


    private void toMainActivity(){
        MyPreference.setObject("userInfo",null);
        context.startActivity(new Intent(context, MainActivity.class));
        getActivity().finish();
    }
}
