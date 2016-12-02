package com.collecdoo.fragment.home.driver;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collecdoo.R;

import com.collecdoo.fragment.home.HomeFragment;
import com.collecdoo.interfaces.HomeNavigationListener;
import com.collecdoo.interfaces.OnBackListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class DriverHomeFragment extends Fragment implements View.OnClickListener ,HomeNavigationListener,OnBackListener {
    @BindView(R.id.btnPlanSingleAdvance) View btnPlanSingleAdvance;
    @BindView(R.id.btnPlanAdHoc) View btnPlanAdHoc;
    @BindView(R.id.btnPreriodic) View btnPreriodic;
    @BindView(R.id.btnPlanWorkingTime) View btnPlanWorkingTime;
    private final String TAG = "--Driver home--";
    private Unbinder unbinder;

    public static DriverHomeFragment init(){
        DriverHomeFragment registerFragment=new DriverHomeFragment();
        Bundle bundle=new Bundle();

        registerFragment.setArguments(bundle);
        return registerFragment;
    }

    private Context context;

    public DriverHomeFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.driver_fragment, container, false);
        unbinder=ButterKnife.bind(this, view);
        btnPlanSingleAdvance.setOnClickListener(this);
        btnPlanAdHoc.setOnClickListener(this);
        btnPreriodic.setOnClickListener(this);
        btnPlanWorkingTime.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPlanSingleAdvance:

                    getFragmentManager().beginTransaction().
                            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                            replace(R.id.fragment, DriverSingleDriveFragment.init(0), DriverSingleDriveFragment.class.getName()).
                            commit();

                break;
            case R.id.btnPlanAdHoc:
                getFragmentManager().beginTransaction().
                        setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                        replace(R.id.fragment, DriverAdhocDriveFragment.init(), DriverAdhocDriveFragment.class.getName()).
                        commit();
                break;
            case R.id.btnPreriodic:
                getFragmentManager().beginTransaction().
                        setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                        replace(R.id.fragment, DriverSingleDriveFragment.init(2), DriverSingleDriveFragment.class.getName()).
                        commit();
                break;
            case R.id.btnPlanWorkingTime:
                getFragmentManager().beginTransaction().
                        setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                        replace(R.id.fragment, DriverWorkingTimeFragment.init(), DriverWorkingTimeFragment.class.getName()).
                        commit();
                break;
        }

    }

    @Override
    public void onBackClick() {
        getFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).
                replace(R.id.fragment, HomeFragment.init(), HomeFragment.class.getName()).
                commit();
    }

    @Override
    public void onButton1() {

    }

    @Override
    public void onButton2() {

    }

    @Override
    public void onButton3() {

    }

    @Override
    public void onMapClick() {

    }


    @Override
    public void onNextClick() {

    }


    @Override
    public void onBackPress() {
        getFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).
                replace(R.id.fragment, HomeFragment.init(),HomeFragment.class.getName()).
                commit();
    }


}
