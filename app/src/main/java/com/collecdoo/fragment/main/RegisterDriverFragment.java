package com.collecdoo.fragment.main;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.collecdoo.MyPreference;
import com.collecdoo.R;
import com.collecdoo.Utility;
import com.collecdoo.dto.CountryInfo;
import com.collecdoo.dto.UserInfo;
import com.collecdoo.fragment.adapter.MySimpleSpinnerAdapter;
import com.collecdoo.fragment.home.StatusLoginFragment;
import com.collecdoo.helper.UIHelper;
import com.collecdoo.interfaces.HomeListener;
import com.collecdoo.interfaces.OnBackListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.TreeSet;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A placeholder fragment containing a simple view.
 */
public class RegisterDriverFragment extends Fragment implements View.OnClickListener,OnBackListener {
    @BindView(R.id.txtTitle) TextView txtTitle;
    @BindView(R.id.ediStreet) EditText ediStreet;
    @BindView(R.id.ediHouseNo) EditText ediHouseNo;
    @BindView(R.id.ediCity) EditText ediCity;
    @BindView(R.id.ediPostcode) EditText ediPostcode;
    @BindView(R.id.spiCountry) Spinner spiCountry;
    private UserInfo userInfo;
    private boolean wasPassenger;


    @BindView(R.id.btnOk) Button btnOk;

    private Unbinder unbinder;

//    public static RegisterDriverFragment init(boolean wasPassenger){
//        RegisterDriverFragment registerDriverFragment=new RegisterDriverFragment();
//        Bundle bundle=new Bundle();
//        bundle.putBoolean("wasPassenger",wasPassenger);
//        registerDriverFragment.setArguments(bundle);
//        return registerDriverFragment;
//    }
    public static RegisterDriverFragment init(boolean wasPassenger,UserInfo userInfo){
        RegisterDriverFragment registerDriverFragment=new RegisterDriverFragment();
        Bundle bundle=new Bundle();
        bundle.putBoolean("wasPassenger",wasPassenger);
        bundle.putParcelable("userInfo",userInfo);
        registerDriverFragment.setArguments(bundle);
        return registerDriverFragment;
    }
    private Context context;

    public RegisterDriverFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        userInfo= getArguments().getParcelable("userInfo");
        wasPassenger =getArguments().getBoolean("wasPassenger");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.register_driver_fragment, container, false);
        unbinder=ButterKnife.bind(this, view);
        btnOk.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getParentFragment() instanceof HomeListener)
            ((HomeListener)getParentFragment()).hideNavigationBar();
        setColorSpan(txtTitle, 0, 9);


//        List<CountryInfo> countryInfoList=new ArrayList<>();
//        countryInfoList.add(new CountryInfo("1","Germany"));
//        countryInfoList.add(new CountryInfo("2", "Italy"));
//
//        Locale[] locales = Locale.getAvailableLocales();
//
//        for (Locale locale : locales) {
//            String country = locale.getDisplayCountry();
//            if (country.trim().length()>0 ) {
//                countryInfoList.add(new CountryInfo(country,country));
//            }
//        }

        spiCountry.setAdapter(new MySimpleSpinnerAdapter(context, R.layout.simple_spinner_view,
                listAll()));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(spiCountry!=null)
                spiCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View view,
                                               int arg2, long arg3) {
                        ((TextView) view.findViewById(R.id.textView))
                                .setText(((CountryInfo) arg0.getAdapter()
                                        .getItem(arg2)).value);
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub

                    }
                });
                spiCountry.setSelection(((MySimpleSpinnerAdapter)spiCountry.getAdapter()).findPosition(userInfo.country));
            }
        }, 1000);

        ediStreet.setText(userInfo.street);
        ediCity.setText(userInfo.location);
        ediPostcode.setText(userInfo.post_code);
        ediHouseNo.setText(userInfo.house_no);



    }

    private void setColorSpan(TextView textView,  int fromPos,int toPos){
        SpannableStringBuilder sb = new SpannableStringBuilder(textView.getText());
        ForegroundColorSpan fcs = new ForegroundColorSpan(Color.RED);
        sb.setSpan(fcs, fromPos, toPos, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        textView.setText(sb);
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if(getParentFragment() instanceof HomeListener)
        ((HomeListener)getParentFragment()).showNavigationBar();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){

            default:
                if(validate()){
                    userInfo.street= UIHelper.getStringFromEditText(ediStreet);
                    userInfo.house_no= UIHelper.getStringFromEditText(ediHouseNo);
                    userInfo.country= ((CountryInfo)spiCountry.getSelectedItem()).value;
                    userInfo.location= UIHelper.getStringFromEditText(ediCity);
                    userInfo.post_code= UIHelper.getStringFromEditText(ediPostcode);

                    //MyPreference.setObject("userInfo",userInfo);
                    getFragmentManager().beginTransaction().
                            setCustomAnimations(R.anim.slide_in_left, R.anim.slide_out_right).
                            replace(R.id.fragment, RegisterDriverPhotoFragment.init(userInfo, wasPassenger), RegisterDriverPhotoFragment.class.getName()).
                            commit();
                }
                break;
        }

    }



    private boolean validate(){
        StringBuffer mesError=new StringBuffer();
        if(!TextUtils.isEmpty(mesError.toString())) {
            Utility.showMessage(context, mesError.toString());
            return false;
        }
        return true;
    }

    @Override
    public void onBackPress() {
        if(wasPassenger) {
            getFragmentManager().beginTransaction().
                    setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).
                    replace(R.id.fragment, StatusLoginFragment.init(), StatusLoginFragment.class.getName()).
                    commit();
        }
        else {
            getFragmentManager().beginTransaction().
                    setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left).
                    replace(R.id.fragment, RegisterFragment.init(), RegisterFragment.class.getName()).
                    commit();
        }
    }




    public  List<CountryInfo> listAll() {
        Set<MyLocale> byLocale = new TreeSet();
        // Gather them all up.
        for (Locale locale : Locale.getAvailableLocales()) {
            final String isoCountry = locale.getCountry();
            if (isoCountry.length() > 0) {
                //System.out.println(locale.getCountry() + ":" + isoCountry + ":" + locale.getDisplayName());
                byLocale.add(new MyLocale(locale));
            }
        }
        // Roll them out of the set.
        ArrayList<CountryInfo> list = new ArrayList<>();
        for (MyLocale l : byLocale) {
            if(!TextUtils.isEmpty(l.getCountry()))
            list.add(new CountryInfo(l.getCountry(),l.getCountry()));
        }
        return list;
    }


    private  class MyLocale implements Comparable<MyLocale> {
        // My Locale.
        private final Locale me;

        public MyLocale(Locale me) {
            this.me = me;
        }

        // Convenience
        public String getCountry() {
            return me.getDisplayCountry();
        }

        @Override
        public int compareTo(MyLocale it) {
            // No duplicates in the country field.
            if (getCountry().equals(it.getCountry())) {
                return 0;
            }

            // Default to straight comparison.
            return getCountry().compareTo(it.getCountry());
        }
    }

}
