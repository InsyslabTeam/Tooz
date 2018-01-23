package com.insyslab.tooz.broadcastreceivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static com.insyslab.tooz.utils.AppConstants.KEY_REMINDER_NOTIFICATION_OBJECT;
import static com.insyslab.tooz.utils.AppConstants.KEY_REMINDER_NOTIFICATION_UNIQUE_ID;

/**
 * Created by TaNMay on 23/01/18.
 */

public class ReminderReceiver extends BroadcastReceiver {

    private final String TAG = "ReminderReceiver ==>>";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = intent.getParcelableExtra(KEY_REMINDER_NOTIFICATION_OBJECT);
        int id = intent.getIntExtra(KEY_REMINDER_NOTIFICATION_UNIQUE_ID, 0);
        notificationManager.notify(id, notification);
    }

}
