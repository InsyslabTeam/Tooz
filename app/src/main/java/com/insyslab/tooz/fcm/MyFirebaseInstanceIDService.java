package com.insyslab.tooz.fcm;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by TaNMay on 18/04/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private final String TAG = "FCMInstanceService ==> ";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
}
