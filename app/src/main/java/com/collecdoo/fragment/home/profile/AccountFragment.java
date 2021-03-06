package com.collecdoo.fragment.home.profile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.collecdoo.MyPreference;
import com.collecdoo.R;
import com.collecdoo.dto.UserInfo;
import com.collecdoo.fragment.UserManager;
import com.collecdoo.fragment.home.HomeWrapperFragment;
import com.collecdoo.fragment.main.RegisterDriverFragment;
import com.collecdoo.interfaces.HomeListener;
import com.collecdoo.interfaces.HomeNavigationListener;
import com.collecdoo.interfaces.OnBackListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class AccountFragment extends Fragment implements View.OnClickListener, OnBackListener,
        HomeNavigationListener {

    @BindView(R.id.btnProfil)
    View btnProfil;
    @BindView(R.id.btnAddress)
    View btnAddress;
    @BindView(R.id.btnBankAcc)
    View btnBankAcc;
    @BindView(R.id.btnUpgrade)
    View btnUpgrade;
    @BindView(R.id.btnLogout)
    View btnLogout;
    @BindView(R.id.btnLanguage)
    View btnLanguage;


    private Unbinder unbinder;
    private Context context;

    public static AccountFragment init() {
        AccountFragment registerFragment = new AccountFragment();
        Bundle bundle = new Bundle();

        registerFragment.setArguments(bundle);
        return registerFragment;
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
        View view = inflater.inflate(R.layout.account_fragment, container, false);
        unbinder = ButterKnife.bind(this, view);
        btnProfil.setOnClickListener(this);
        btnAddress.setOnClickListener(this);
        btnBankAcc.setOnClickListener(this);
        btnUpgrade.setOnClickListener(this);
        btnLogout.setOnClickListener(this);
        btnLanguage.setOnClickListener(this);
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
        switch (v.getId()) {

            case R.id.btnProfil:
                getFragmentManager().beginTransaction().add(R.id.fragment, ProfileFragment.instantiate(context, ProfileFragment.class.getName()),
                        ProfileFragment.class.getName()).addToBackStack(ProfileFragment.class.getName()).commit();
                break;
            case R.id.btnAddress:
                getFragmentManager().beginTransaction().add(R.id.fragment, AddressFragment.instantiate(context, AddressFragment.class.getName()),
                        AddressFragment.class.getName()).addToBackStack(AddressFragment.class.getName()).commit();
                break;
            case R.id.btnBankAcc:
                getFragmentManager().beginTransaction().add(R.id.fragment, BankAccFragment.instantiate(context, BankAccFragment.class.getName()),
                        BankAccFragment.class.getName()).addToBackStack(BankAccFragment.class.getName()).commit();
                break;
            case R.id.btnUpgrade:
                UserInfo userInfo= UserManager.getInstance().getUserInfo();
                if (userInfo.isOnlyPassenger())
                getFragmentManager().beginTransaction().
                        setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                        replace(R.id.fragment, RegisterDriverFragment.init(true, (UserInfo) MyPreference.getObject("userInfo", UserInfo.class)), RegisterDriverFragment.class.getName()).
                        commit();
                break;
            case R.id.btnLogout:
                ((HomeListener) getParentFragment().getFragmentManager().findFragmentByTag(HomeWrapperFragment.class.getName())).logOut();
                break;
            case R.id.btnLanguage:
                getFragmentManager().beginTransaction().add(R.id.fragment, LanguageFragment.instantiate(context, LanguageFragment.class.getName()),
                        LanguageFragment.class.getName()).addToBackStack(LanguageFragment.class.getName()).commit();
                break;

        }

    }


    @Override
    public void onBackPress() {
        getFragmentManager().popBackStack();
//        ParentFragmentListener homeTagListener = (ParentFragmentListener) getParentFragment();
//        FragmentManager fm = getFragmentManager();
//        FragmentTransaction ft = fm.beginTransaction();
//        ft.remove(this);
//        ft.show(fm.findFragmentByTag(homeTagListener.getCurrentTag()));
//        ft.commit();
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
