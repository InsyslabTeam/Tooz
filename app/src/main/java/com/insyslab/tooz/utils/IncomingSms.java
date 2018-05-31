package com.insyslab.tooz.utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.telephony.SmsMessage;
import android.util.Log;

import static com.insyslab.tooz.utils.AppConstants.KEY_OTP_MESSAGE;
import static com.insyslab.tooz.utils.AppConstants.KEY_OTP_NUMBER;
import static com.insyslab.tooz.utils.AppConstants.KEY_OTP_SMS;
import static com.insyslab.tooz.utils.AppConstants.KEY_SMS_PDUS;

public class IncomingSms extends BroadcastReceiver {

    private final String TAG = IncomingSms.class.getSimpleName() + " ==>";

    @Override
    public void onReceive(Context context, Intent intent) {
        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null) {
                final Object[] pdusObj = (Object[]) bundle.get(KEY_SMS_PDUS);

                if (pdusObj != null) {
                    for (Object aPdusObj : pdusObj) {
                        SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) aPdusObj);

                        String senderNum = currentMessage.getDisplayOriginatingAddress();
                        String smsMessage = currentMessage.getDisplayMessageBody();
                        Log.i(TAG, "SMS: " + "From: " + senderNum + "; Message: " + smsMessage);

                        Intent myIntent = new Intent(KEY_OTP_SMS);
                        myIntent.putExtra(KEY_OTP_MESSAGE, smsMessage);
                        myIntent.putExtra(KEY_OTP_NUMBER, senderNum);
                        LocalBroadcastManager.getInstance(context).sendBroadcast(myIntent);
                    }
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "SMS ERROR: " + "Exception smsReceiver " + e);
        }
    }
}


