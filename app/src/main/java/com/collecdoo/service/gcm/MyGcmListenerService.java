package com.collecdoo.service.gcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.collecdoo.R;
import com.collecdoo.activity.HomeActivity;
import com.collecdoo.activity.MainActivity;
import com.collecdoo.config.Constant;
import com.google.android.gms.gcm.GcmListenerService;

import java.util.Random;


public class MyGcmListenerService extends GcmListenerService {

    @Override
    public void onMessageReceived(String from, Bundle data) {

        if(data.getString("route_id")!=null){
//            Intent intent = new Intent(QuickstartPreferences.PUSH_ARRIVED);
//            intent.putExtra("route_id", data.getString("route_id"));
//            LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
//            Log.d(Constant.DEBUG_TAG,data.toString());

            sendNotification("New Routes for Driver",data.getString("route_id"));
        }




    }

    private void sendNotification(String message,String routeId) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("route_id",routeId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Collecdoo")
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(1, notificationBuilder.build());
    }

}