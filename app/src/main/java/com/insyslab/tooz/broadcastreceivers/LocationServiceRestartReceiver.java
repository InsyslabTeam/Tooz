package com.insyslab.tooz.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.insyslab.tooz.services.LocationService;

public class LocationServiceRestartReceiver extends BroadcastReceiver {

    private final String TAG = LocationServiceRestartReceiver.class.getSimpleName() + " ==>";

    @Override
    public void onReceive(Context context, Intent intent) {
//        Log.d(TAG, "onReceive");
        context.startService(new Intent(context, LocationService.class));
    }
}