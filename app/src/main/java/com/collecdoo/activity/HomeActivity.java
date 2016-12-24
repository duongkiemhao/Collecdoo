package com.collecdoo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.collecdoo.R;
import com.collecdoo.Utility;
import com.collecdoo.config.Constant;
import com.collecdoo.fragment.home.HomeFragment;
import com.collecdoo.fragment.home.HomeWrapperFragment;
import com.collecdoo.fragment.home.profile.LanguageFragment;
import com.collecdoo.interfaces.HomeListener;
import com.collecdoo.interfaces.OnBackListener;
import com.crashlytics.android.Crashlytics;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import io.fabric.sdk.android.Fabric;

public class HomeActivity extends AppCompatActivity {

    private boolean doubleBackToExitPressedOnce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.main_activity);

        LanguageFragment.setLanguageToActitity(this);

        checkPlayServices();

        getSupportFragmentManager().beginTransaction().
                replace(R.id.fragment, HomeWrapperFragment.init(), HomeWrapperFragment.class.getName()).
                commitNow();

        if (getIntent().getStringExtra("route_id") != null) {
            processPush(getIntent().getStringExtra("route_id"));
        }



    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        //new route for driver
        if (!TextUtils.isEmpty(intent.getStringExtra("route_id")))
            processPush(intent.getStringExtra("route_id"));

        //language changed
        if (intent.getBooleanExtra(LanguageFragment.IS_CONFIG_CHANGED, false))
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment, HomeWrapperFragment.init(), HomeWrapperFragment.class.getName()).
                    commit();

    }

    private void processPush(String pushId) {
        if (getSupportFragmentManager().findFragmentById(R.id.fragment) instanceof HomeListener)
            ((HomeListener) getSupportFragmentManager().findFragmentById(R.id.fragment)).onGotPush(pushId);
    }

    @Override
    public void onBackPressed() {

        Utility.hideKeyboard(this);
        String currentTag = getSupportFragmentManager().findFragmentById(R.id.fragment).
                getChildFragmentManager().findFragmentById(R.id.fragment).getTag();

        if (currentTag.equals(HomeFragment.class.getName())
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
