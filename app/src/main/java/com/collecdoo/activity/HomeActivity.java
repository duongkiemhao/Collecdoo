package com.collecdoo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.collecdoo.R;
import com.collecdoo.Utility;
import com.collecdoo.config.Constant;

import com.collecdoo.fragment.home.HomeFragment;
import com.collecdoo.fragment.home.StatusLoginFragment;
import com.collecdoo.fragment.home.customer.CustomerHomeFragment;
import com.collecdoo.fragment.home.driver.DriverHomeFragment;
import com.collecdoo.interfaces.OnBackListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


public class HomeActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        checkPlayServices();

        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment, HomeFragment.init(),HomeFragment.class.getName()).
                commit();
    }

    @Override
    public void onBackPressed() {

        Utility.hideKeyboard(this);
        String currentTag = getSupportFragmentManager().findFragmentById(R.id.fragment).
                getChildFragmentManager().findFragmentById(R.id.fragment).getTag();

        if (currentTag.equals(StatusLoginFragment.class.getName())
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
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment).
                    getChildFragmentManager().findFragmentById(R.id.fragment);
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
