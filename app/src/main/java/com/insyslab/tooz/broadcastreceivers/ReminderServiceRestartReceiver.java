package com.insyslab.tooz.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.insyslab.tooz.services.ReminderSchedulingService;

/**
 * Created by TaNMay on 20/02/18.
 */

public class ReminderServiceRestartReceiver extends BroadcastReceiver {

    private final String TAG = "ReminderRestart ==>>";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        startReminderSchedulingService(context);
    }

    public void startReminderSchedulingService(Context context) {
        Intent intent = new Intent(context, ReminderSchedulingService.class);
        context.startService(intent);
    }

}
