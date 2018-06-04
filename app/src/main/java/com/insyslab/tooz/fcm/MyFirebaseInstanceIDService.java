package com.insyslab.tooz.fcm;

import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

//    private final String TAG = MyFirebaseInstanceIDService.class.getSimpleName() + " ==>";

    @Override
    public void onTokenRefresh() {
//        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
//        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
}
