package com.collecdoo.fragment.pickup;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collecdoo.MyRetrofitService;
import com.collecdoo.R;
import com.collecdoo.Utility;
import com.collecdoo.config.Config;
import com.collecdoo.config.Constant;
import com.collecdoo.dto.DriverActivityInfo;
import com.collecdoo.dto.PathOfRouteInfo;
import com.collecdoo.dto.ResponseInfo;
import com.collecdoo.fragment.LocationManger;
import com.collecdoo.fragment.ServiceGenerator;
import com.collecdoo.fragment.home.HomeWrapperFragment;
import com.collecdoo.fragment.home.HomeFragment;
import com.collecdoo.helper.UserHelper;
import com.collecdoo.interfaces.HomeNavigationListener;

import java.util.ArrayList;
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
                        getArguments().getInt("viewIndex")), HomeFragment.class.getName()).
                commit();
        if(UserHelper.isDriver())
            driverActivity(Config.ACTION_WORKING);
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
                if(UserHelper.isDriver())
                    driverActivity(Config.ACTION_LOGIN);
                else{
                    getFragmentManager().beginTransaction().replace(R.id.fragment, HomeWrapperFragment.init(),HomeWrapperFragment.class.getName())
                            .commit();
                }
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

    private void driverActivity(int actionIndex) {


        DriverActivityInfo activityInfo = new DriverActivityInfo();
        activityInfo.user_id = UserHelper.getUserId();
        activityInfo.latitude = LocationManger.getInstance().getLocation().getLatitude()+ "";
        activityInfo.longitude = LocationManger.getInstance().getLocation().getLongitude()+ "";
        activityInfo.action = actionIndex + "";
        driverActivityTask(activityInfo);
    }

    private void driverActivityTask(final DriverActivityInfo activityInfo) {

        MyRetrofitService taskService = ServiceGenerator.createService(MyRetrofitService.class);

        Call<ResponseInfo> call = taskService.driverActivity(activityInfo);
        call.enqueue(new Callback<ResponseInfo>() {

            @Override
            public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
                if(Integer.parseInt(activityInfo.action)==Config.ACTION_LOGIN) {
                    getFragmentManager().beginTransaction().replace(R.id.fragment, HomeWrapperFragment.init(),HomeWrapperFragment.class.getName())
                            .commit();
                    return;
                }
                if (response.isSuccessful()) {
                    ResponseInfo responseInfo = response.body();
                    if (responseInfo.status.toLowerCase().equals("ok")) {
                        Log.d(DriverPickupHomeFragment.this.getClass().getName(),responseInfo.toString());


                    } else Utility.showMessage(context, responseInfo.message);
                } else Utility.showMessage(context, response.message());
            }

            @Override
            public void onFailure(Call<ResponseInfo> call, Throwable t) {
                Log.d(Constant.DEBUG_TAG, "error" + t.getMessage());

            }
        });

    }

}
