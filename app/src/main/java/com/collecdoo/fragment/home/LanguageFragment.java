package com.collecdoo.fragment.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.collecdoo.MyPreference;
import com.collecdoo.R;
import com.collecdoo.activity.HomeActivity;
import com.collecdoo.interfaces.HomeNavigationListener;
import com.collecdoo.interfaces.OnBackListener;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

/**
 * Created by kiemhao on 12/2/16.
 */

public class LanguageFragment extends Fragment implements OnBackListener,HomeNavigationListener {

    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    private Context context;
    private Unbinder unbinder;

    public static String IS_CONFIG_CHANGED="isConfigChanged";
    public static String LANGUAGE="language";
    public static String LANGUAGE_DE="de";
    public static String LANGUAGE_EN="en";



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context=context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.language_fragment,null);
        unbinder= ButterKnife.bind(this,view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(MyPreference.getString(LANGUAGE).equals(LANGUAGE_DE)){
            ((RadioButton)radioGroup.getChildAt(1)).setChecked(true);
        }
        else ((RadioButton)radioGroup.getChildAt(0)).setChecked(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPress() {
        getFragmentManager().popBackStack();

    }

    @OnClick(R.id.btnSave)
    void save(){
        switch (radioGroup.getCheckedRadioButtonId()){
            case R.id.rb_de:
                setLocale(LANGUAGE_DE);
                break;
            case R.id.rb_us:
                setLocale(LANGUAGE_EN);
                break;
        }
    }

    private void setLocale(String lang) {
        if(lang.equals(MyPreference.getString(LANGUAGE)))
            return;
        Locale myLocale = new Locale(lang);
        Resources res = getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
        MyPreference.setString(LANGUAGE,lang);
        Intent refresh = new Intent(context, HomeActivity.class);
        refresh.putExtra(IS_CONFIG_CHANGED,true);
        startActivity(refresh);
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

    public static void setLanguageToActitity(Activity actitity){
        Locale myLocale = new Locale(MyPreference.getString("language"));
        Resources res = actitity.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }

    public static void setDefaultLanguage(Activity activity){
        setLanguageToActitity(activity);
    }

    public static boolean isLanguageEn(Context context){
        return MyPreference.getString(LANGUAGE).equals(LANGUAGE_EN)?true:false;
    }
}
