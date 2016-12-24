package com.collecdoo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.collecdoo.MyPreference;
import com.collecdoo.R;
import com.collecdoo.Utility;
import com.collecdoo.config.Constant;
import com.collecdoo.dto.UserInfo;
import com.collecdoo.fragment.home.profile.LanguageFragment;
import com.collecdoo.fragment.main.MainFragment;
import com.collecdoo.interfaces.OnBackListener;
import com.collecdoo.service.gcm.RegistrationIntentService;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.main_activity);

        if (MyPreference.getString(LanguageFragment.LANGUAGE) == null)
            LanguageFragment.setDefaultLanguage(this);
        else LanguageFragment.setLanguageToActitity(this);


        checkPlayServices();
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);


        if (MyPreference.getObject("userInfo", UserInfo.class) == null)
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment, MainFragment.init(), MainFragment.class.getName()).
                    commit();
        else {
            Intent intentHome = new Intent(this, HomeActivity.class);
            intentHome.putExtra("route_id", getIntent().getStringExtra("route_id"));
            startActivity(intentHome);
            finish();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {

        Utility.hideKeyboard(this);
        String currentTag = getSupportFragmentManager().findFragmentById(R.id.fragment).getTag();

        if (currentTag.equals(MainFragment.class.getName())
                ) {
            if (doubleBackToExitPressedOnce) {
                finish();

            }
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, R.string.msg_exit, Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(
                    R.id.fragment);
            if (fragment instanceof OnBackListener)
                ((OnBackListener) fragment).onBackPress();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

    }


    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        Constant.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("-----", "This device is not supported.");

            }
            return false;
        }
        return true;
    }

}
