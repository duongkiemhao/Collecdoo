package com.collecdoo.fragment.pickup;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collecdoo.R;
import com.collecdoo.dto.PathOfRouteInfo;
import com.collecdoo.fragment.home.HomeFragment;
import com.collecdoo.fragment.home.StatusLoginFragment;
import com.collecdoo.interfaces.HomeNavigationListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

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
