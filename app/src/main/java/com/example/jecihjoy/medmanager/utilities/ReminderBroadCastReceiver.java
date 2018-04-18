package com.example.jecihjoy.medmanager.utilities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.jecihjoy.medmanager.utilities.NotificationUtils;

/**
 * Created by Jecihjoy on 4/10/2018.
 */

public class ReminderBroadCastReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        NotificationUtils.remindUser(context,bundle.getStringArrayList("names"));
    }
}
