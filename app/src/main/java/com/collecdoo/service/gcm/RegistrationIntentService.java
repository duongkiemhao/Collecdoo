package com.collecdoo.service.gcm;


import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.collecdoo.MyPreference;
import com.collecdoo.R;
import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class RegistrationIntentService extends IntentService {

    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};

    public RegistrationIntentService() {
        super(TAG);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        try {
            synchronized (TAG) {

                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                MyPreference.setString(QuickstartPreferences.TOKEN_STRING, token);
                // [END get_token]
//                if(MyPreference.getString(QuickstartPreferences.TOKEN_STRING)!=null
//                    || !token.equals(MyPreference.getString(QuickstartPreferences.TOKEN_STRING))){
//                    sendRegistrationToServer(token);
//                    Log.i(TAG, "update Registration Token: " + token);
//                }
                Log.i(TAG, "GCM Registration Token: " + token);


                // TODO: Implement this method to send any registration to your app's servers.


                // Subscribe to topic channels
                subscribeTopics(token);

                // You should store a boolean that indicates whether the generated token has been
                // sent to your server. If the boolean is false, send the token to your server,
                // otherwise your server should have already received the token.
                sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, true).apply();
                sharedPreferences.edit().putString(QuickstartPreferences.TOKEN_STRING, token).apply();

                // [END register_for_gcm]
            }
        } catch (Exception e) {
            //Log.d(TAG, "Failed to complete token refresh", e);
            // If an exception happens while fetching the new token or updating our registration data
            // on a third-party server, this ensures that we'll attempt the update at a later time.
            sharedPreferences.edit().putBoolean(QuickstartPreferences.SENT_TOKEN_TO_SERVER, false).apply();
        }
        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent(QuickstartPreferences.REGISTRATION_COMPLETE);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    /**
     * Persist registration to third-party servers.
     * <p/>
     * Modify this method to associate the user's GCM registration token with any server-side account
     * maintained by your application.
     *
     * @param token The new token.
     */
//    private void sendRegistrationToServer(String token) {
//        PushInfo pushInfo=new PushInfo();
//        UserInfo userInfo= (UserInfo) MyPreference.getObject("userInfo",UserInfo.class);
//        if(userInfo!=null){
//            pushInfo.user_id=userInfo.user_id;
//            pushInfo.push_registered_id=token;
//            updatePush(pushInfo);
//        }
//    }
//
//    private void updatePush(PushInfo pushInfo){
//
//        MyRetrofitService taskService = ServiceGenerator.createService(MyRetrofitService.class);
//
//        Call<ResponseInfo> call = taskService.updatePushId(pushInfo);
//        call.enqueue(new Callback<ResponseInfo>() {
//
//            @Override
//            public void onResponse(Call<ResponseInfo> call, Response<ResponseInfo> response) {
//                if(response.isSuccessful()){
//                    ResponseInfo responseInfo= response.body();
//
//                    if(responseInfo.status.toLowerCase().equals("ok")) {
//                        Log.i(TAG, "update push to server ok");
//                    }
//                    else Log.i(TAG, "update push to server failed");
//                }
//                else Log.i(TAG, "update push to server failed");
//            }
//
//            @Override
//            public void onFailure(Call<ResponseInfo> call, Throwable t) {
//                Log.d(Constant.DEBUG_TAG,t.getMessage()!=null?t.getMessage():"");
//            }
//        });
//
//    }
//


    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token GCM token
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    private void subscribeTopics(String token) throws IOException {
        for (String topic : TOPICS) {
            GcmPubSub pubSub = GcmPubSub.getInstance(this);
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
    // [END subscribe_topics]

}