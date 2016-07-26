package com.collecdoo.fragment.pickup;

import android.Manifest;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
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
import com.collecdoo.fragment.ServiceGenerator;
import com.collecdoo.fragment.home.AccountFragment;
import com.collecdoo.fragment.home.HomeFragment;
import com.collecdoo.fragment.home.ListOfDriveFragment;
import com.collecdoo.fragment.home.StatusLoginFragment;
import com.collecdoo.fragment.home.driver.DriverSignatureFragment;
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
import java.util.ArrayList;
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
public class DriverPickupHomeFragment extends Fragment implements View.OnClickListener{
    @BindView(R.id.btnBack)
    View btnBack;
    @BindView(R.id.btnCall)
    View btnCall;
    @BindView(R.id.btnDone)
    View btnDone;
    @BindView(R.id.btnStops)
    View btnStops;
    @BindView(R.id.btnMap)
    View btnMap;

    @BindView(R.id.btnNext)
    View btnNext;


    private Unbinder unbinder;
    private String currentFragmentTag;



    public static DriverPickupHomeFragment init(boolean isPickup,List<PathOfRouteInfo> pathOfRouteInfoList,int viewIndex) {
        DriverPickupHomeFragment registerFragment = new DriverPickupHomeFragment();
        Bundle bundle = new Bundle();
        bundle.putBoolean("isPickup", isPickup);
        bundle.putInt("viewIndex", viewIndex);
        bundle.putParcelableArrayList("list", (ArrayList<? extends Parcelable>) pathOfRouteInfoList);
        registerFragment.setArguments(bundle);
        return registerFragment;
    }

    private Context context;

    public DriverPickupHomeFragment() {
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
        View view = inflater.inflate(R.layout.driver_pickup_home_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        btnBack.setOnClickListener(this);
        btnCall.setOnClickListener(this);
        btnDone.setOnClickListener(this);
        btnStops.setOnClickListener(this);
        btnMap.setOnClickListener(this);
        btnNext.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getChildFragmentManager().beginTransaction().
                replace(R.id.fragment, DriverPickupDropFragment.init(getArguments().getBoolean("isPickup"),
                        getArguments().getParcelableArrayList("list"),
                        getArguments().getInt("viewIndex")), StatusLoginFragment.class.getName()).
                commit();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btnBack:
                getFragmentManager().beginTransaction().replace(R.id.fragment, HomeFragment.init(),HomeFragment.class.getName())
                        .commit();

                break;
            case R.id.btnCall:
                HomeNavigationListener navigationListener = (HomeNavigationListener) getChildFragmentManager().findFragmentById(R.id.fragment);
                navigationListener.onButton1();

                break;
            case R.id.btnDone:
                navigationListener = (HomeNavigationListener) getChildFragmentManager().findFragmentById(R.id.fragment);
                navigationListener.onButton2();

                break;
            case R.id.btnStops:
                navigationListener = (HomeNavigationListener) getChildFragmentManager().findFragmentById(R.id.fragment);
                navigationListener.onButton3();
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

}
