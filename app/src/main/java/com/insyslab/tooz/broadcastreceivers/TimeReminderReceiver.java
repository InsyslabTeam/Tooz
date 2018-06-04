package com.insyslab.tooz.broadcastreceivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import static com.insyslab.tooz.utils.AppConstants.KEY_REMINDER_NOTIFICATION_OBJECT;
import static com.insyslab.tooz.utils.AppConstants.KEY_REMINDER_NOTIFICATION_UNIQUE_ID;

public class TimeReminderReceiver extends BroadcastReceiver {

    private final String TAG = TimeReminderReceiver.class.getSimpleName() + " ==>";

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.d(TAG, "onReceive");

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (intent != null) {
            Notification notification = intent.getParcelableExtra(KEY_REMINDER_NOTIFICATION_OBJECT);
            int id = intent.getIntExtra(KEY_REMINDER_NOTIFICATION_UNIQUE_ID, 0);
            if (notificationManager != null) notificationManager.notify(id, notification);
        } else {
            Log.e(TAG, "Intent is null!");
        }
    }
}
