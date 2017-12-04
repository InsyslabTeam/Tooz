package com.insyslab.tooz.broadcastreceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.insyslab.tooz.services.LocationService;

/**
 * Created by TaNMay on 04/12/17.
 */

public class LocationServiceRestartReceiver extends BroadcastReceiver {

    private final String TAG = "LocRestartReceiver ==> ";

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        context.startService(new Intent(context, LocationService.class));
    }
}