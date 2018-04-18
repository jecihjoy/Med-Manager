package com.example.jecihjoy.medmanager.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.example.jecihjoy.medmanager.LoginActivity;
import com.example.jecihjoy.medmanager.R;

import java.util.ArrayList;

/**
 * Created by Jecihjoy on 4/4/2018.
 */

public class NotificationUtils {
    private static final int MED_NOTIFICATION = 1;
    private static  String myBody = "You are supposed to take  ";




    public static void remindUser(Context context, ArrayList<String> medsName) {
        if (medsName.size() == 0){
            myBody = "You are not supposed to take  any medicine now";
        }else {
            //StringBuilder sb = new StringBuilder();
            for (String med : medsName) {
                myBody += med+ "t";
            }

        }
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel mChannel = new NotificationChannel(
                    "reminder_notification_channel",
                    context.getString(R.string.main_notification_channel_name),
                    NotificationManager.IMPORTANCE_HIGH);
            notificationManager.createNotificationChannel(mChannel);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.medicon)
                .setLargeIcon(largeIcon(context))
                .setContentTitle(context.getString(R.string.medicine_reminder_notification_title))
                .setContentText(myBody)
                .setStyle(new NotificationCompat.BigTextStyle().bigText("You are supposed to take your medicines now. We at med managers are wishing you quick recovery "))
                .setDefaults(Notification.DEFAULT_VIBRATE)
                .setContentIntent(contentIntent(context))
                .setAutoCancel(true);


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        notificationManager.notify(MED_NOTIFICATION, notificationBuilder.build());
    }
    private static PendingIntent contentIntent(Context context) {
        Intent startActivityIntent = new Intent(context, LoginActivity.class);
        return PendingIntent.getActivity(context,MED_NOTIFICATION,startActivityIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private static Bitmap largeIcon(Context context) {
        Resources res = context.getResources();
        Bitmap largeIcon = BitmapFactory.decodeResource(res, R.drawable.medicon);
        return largeIcon;
    }



}
