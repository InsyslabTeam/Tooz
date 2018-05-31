package com.insyslab.tooz.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.insyslab.tooz.services.ReminderSchedulingService;

import java.util.Objects;

public class ReminderServiceRestartReceiver extends BroadcastReceiver {

    private final String TAG = ReminderServiceRestartReceiver.class.getSimpleName() + " ==>";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");

        if (Objects.equals(intent.getAction(), "android.intent.action.BOOT_COMPLETED")) {
            startReminderSchedulingService(context);
        }
    }

    public void startReminderSchedulingService(Context context) {
        Intent intent = new Intent(context, ReminderSchedulingService.class);
        context.startService(intent);
    }

}
