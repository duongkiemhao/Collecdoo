package com.collecdoo.fragment.home.customer;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collecdoo.R;
import com.collecdoo.config.Constant;
import com.collecdoo.config.ConstantTabTag;
import com.collecdoo.fragment.home.StatusLoginFragment;
import com.collecdoo.interfaces.HomeNavigationListener;
import com.collecdoo.interfaces.OnBackListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class CustomerHomeFragment extends Fragment implements View.OnClickListener,OnBackListener,
        HomeNavigationListener {
    @BindView(R.id.btnSingle) View btnSingle;
    @BindView(R.id.btnAdHoc) View btnAdHoc;
    @BindView(R.id.btnPreriodic) View btnPreriodic;
    @BindView(R.id.btnRequireDelivery) View btnRequireDelivery;

    private Unbinder unbinder;

    public static CustomerHomeFragment init(){
        CustomerHomeFragment registerFragment=new CustomerHomeFragment();
        Bundle bundle=new Bundle();

        registerFragment.setArguments(bundle);
        return registerFragment;
    }

    private Context context;

    public CustomerHomeFragment() {
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
        View view=inflater.inflate(R.layout.require_drive_fragment, container, false);
        unbinder=ButterKnife.bind(this, view);
        btnSingle.setOnClickListener(this);
        btnAdHoc.setOnClickListener(this);
        btnPreriodic.setOnClickListener(this);
        btnRequireDelivery.setOnClickListener(this);
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
            case R.id.btnSingle:
                    getFragmentManager().beginTransaction().
                            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                            replace(R.id.fragment, CustomerSingleDriveFragment.init(0), CustomerSingleDriveFragment.class.getName()).
                            commit();

                break;
            case R.id.btnAdHoc:
                getFragmentManager().beginTransaction().
                        setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                        replace(R.id.fragment, CustomerSingleDriveFragment.init(1), CustomerSingleDriveFragment.class.getName()).
                        commit();
                break;
            case R.id.btnPreriodic:
                getFragmentManager().beginTransaction().
                        setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                        replace(R.id.fragment, CustomerSingleDriveFragment.init(2),CustomerSingleDriveFragment.class.getName()).
                        commit();
                break;
            case R.id.btnRequireDelivery:
                getFragmentManager().beginTransaction().
                        setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                        replace(R.id.fragment, CustomerDelivery1Fragment.init(), CustomerDelivery1Fragment.class.getName()).
                        commit();
                break;
        }

    }


    @Override
    public void onBackPress() {
        Log.d(Constant.DEBUG_TAG, "on back require drive");
        getFragmentManager().beginTransaction().
                setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).
                replace(R.id.fragment, StatusLoginFragment.init(), StatusLoginFragment.class.getName()).
                commit();
    }

    @Override
    public void onBackClick() {
        onBackPress();
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
}
