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
import com.collecdoo.config.ConstantTabTag;
import com.collecdoo.dto.UserInfo;
import com.collecdoo.fragment.main.MainFragment;
import com.collecdoo.interfaces.OnBackListener;
import com.collecdoo.service.gcm.RegistrationIntentService;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;


public class MainActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        checkPlayServices();
        Intent intent = new Intent(this, RegistrationIntentService.class);
        startService(intent);

//        getSupportFragmentManager().beginTransaction().
//                replace(R.id.fragment, MainFragment.init(), ConstantTabTag.MAIN).
//                commit();

        if(MyPreference.getObject("userInfo", UserInfo.class)==null)
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.fragment, MainFragment.init(),MainFragment.class.getName()).
                    commit();
        else{
            startActivity(new Intent(this,HomeActivity.class));
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

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
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