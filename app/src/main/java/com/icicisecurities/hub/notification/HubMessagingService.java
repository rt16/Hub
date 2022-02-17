package com.icicisecurities.hub.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;


import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.icicisecurities.hub.R;

import java.util.Random;


public class HubMessagingService extends FirebaseMessagingService {


    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        showNotification(remoteMessage.getNotification().getTitle() , remoteMessage.getNotification().getBody());
    }

    private void showNotification(String title, String body) {

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel notificationChannel = new NotificationChannel(getResources().getString(R.string.app_name) , getResources().getString(R.string.all_notifications_spelling) , NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(getColor(R.color.orangeColorICICI));
            notificationChannel.setVibrationPattern(new long[]{0,1000,500,1000});
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        }
        NotificationCompat.Builder notifiactBuilder = new NotificationCompat.Builder(this , getResources().getString(R.string.app_name));
        notifiactBuilder.setAutoCancel(true);
        notifiactBuilder.setWhen(System.currentTimeMillis());
        notifiactBuilder.setSmallIcon(getNotificationIcon());
        notifiactBuilder.setColor(getResources().getColor(R.color.orangeColorICICI));
        notifiactBuilder.setContentTitle(title);
        notifiactBuilder.setContentText(body);
        notifiactBuilder.setChannelId(getResources().getString(R.string.app_name));
        notifiactBuilder.setStyle(new NotificationCompat.BigTextStyle());
        if (notificationManager != null) {
            notificationManager.notify(new Random().nextInt() , notifiactBuilder.build());
        }
    }

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);
    }


    private int getNotificationIcon(){
        boolean useWhiteIcon = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
        return useWhiteIcon ? R.drawable.ic_stat_name : R.drawable.ic_hub_launcher;
    }


}//AccountOpeningMessagingService closes here...
