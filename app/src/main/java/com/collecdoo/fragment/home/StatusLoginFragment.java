package com.collecdoo.fragment.home;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collecdoo.MyPreference;
import com.collecdoo.R;
import com.collecdoo.config.ConstantTabTag;
import com.collecdoo.dto.UserInfo;
import com.collecdoo.fragment.home.customer.CustomerHomeFragment;
import com.collecdoo.fragment.home.driver.DriverHomeFragment;
import com.collecdoo.fragment.main.RegisterDriverFragment;
import com.collecdoo.interfaces.HomeListener;
import com.collecdoo.interfaces.HomeNavigationListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class StatusLoginFragment extends Fragment implements View.OnClickListener,HomeNavigationListener{

    @BindView(R.id.txtTitle) TextView txtTitle;
    @BindView(R.id.btnRequireRide) TextView btnRequireRide;
    @BindView(R.id.btnShareTrip) TextView btnShareTrip;


    private Unbinder unbinder;

    public static StatusLoginFragment init(){
        StatusLoginFragment registerFragment=new StatusLoginFragment();
        Bundle bundle=new Bundle();

        registerFragment.setArguments(bundle);
        return registerFragment;
    }

    private Context context;

    public StatusLoginFragment() {
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
        View view=inflater.inflate(R.layout.status_login_fragment, container, false);
        unbinder=ButterKnife.bind(this, view);
        btnRequireRide.setOnClickListener(this);
        btnShareTrip.setOnClickListener(this);


        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //((HomeListener)getParentFragment()).hideNavigationBar();
        setColorSpan(txtTitle, 0, 9);

        ((HomeListener)getParentFragment()).updateDriverActivity();
    }

    private void setColorSpan(TextView textView,  int fromPos,int toPos){
        SpannableStringBuilder sb = new SpannableStringBuilder(textView.getText());
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.RED);
        sb.setSpan(fcs, fromPos, toPos, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(sb);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        //((HomeListener)getParentFragment()).showNavigationBar();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnRequireRide:
                    getFragmentManager().beginTransaction().
                            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                            replace(R.id.fragment, CustomerHomeFragment.init(),
                                    CustomerHomeFragment.class.getName()).
                            commit();

                break;
            case R.id.btnShareTrip:
                UserInfo userInfo= (UserInfo) MyPreference.getObject("userInfo", UserInfo.class);
                if(userInfo.driver_type.equals("0"))
                    showDriverDialog();
                else {
                    getFragmentManager().beginTransaction().
                            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                            replace(R.id.fragment, DriverHomeFragment.init(),
                                    DriverHomeFragment.class.getName()).
                            commit();
                }
                break;


        }

    }

    private void showDriverDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage("Do you like to register as Driver?");

        String positiveText = "Yes";
        builder.setPositiveButton(positiveText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        getFragmentManager().beginTransaction().
                                setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                                replace(R.id.fragment, RegisterDriverFragment.init(true, (UserInfo) MyPreference.getObject("userInfo",UserInfo.class)), RegisterDriverFragment.class.getName()).
                                commit();

                    }
                });

        String negativeText = "No";
        builder.setNegativeButton(negativeText,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                    }
                });

        AlertDialog dialog = builder.create();
        // display dialog
        dialog.show();
    }

    @Override
    public void onBackClick() {
        getActivity().onBackPressed();
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
