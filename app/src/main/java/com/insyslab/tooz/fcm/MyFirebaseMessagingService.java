package com.insyslab.tooz.fcm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.insyslab.tooz.models.Reminder;
import com.insyslab.tooz.ui.activities.SplashActivity;
import com.insyslab.tooz.utils.Util;

import org.json.JSONObject;

import java.util.Date;

import static com.insyslab.tooz.utils.Util.getReminderFormatedDate;

/**
 * Created by TaNMay on 18/04/17.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private final String TAG = "FCM_Service ==> ";

    private final String KEY_PUSH_MESSAGE = "KEY_PUSH_MESSAGE";
    private final String PUSH_NOTIFICATION = "PUSH_NOTIFICATION";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getNotification() != null && remoteMessage.getData().size() == 0) {
            Log.d(TAG, "Push Notification has only notification object!");
            sendNotification(remoteMessage.getNotification().getBody(), remoteMessage.getNotification().getTitle(), "content_value");
        }

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Push Notification has data object!");
            Log.d(TAG, "Data: " + remoteMessage.getData().toString());

            try {
                JSONObject dataJson = new JSONObject(remoteMessage.getData());
                JSONObject actualData = new JSONObject(dataJson.optString("data"));
                handleDataMessage(actualData);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void sendNotification(String messageBody, String title, String content) {
        Intent intent = new Intent(this, SplashActivity.class);
        intent.putExtra(KEY_PUSH_MESSAGE, content);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(NotificationUtils.getNotificationIcon())
                .setContentTitle(title)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        int randomNonRepeatedNumber = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(randomNonRepeatedNumber, notificationBuilder.build());
    }

    private void handleDataMessage(JSONObject data) {
        Log.d(TAG, "Data Object: " + data.toString());

        try {
            Gson gson = new Gson();
            Reminder pushNotificationObject = gson.fromJson(data.toString(), Reminder.class);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent(PUSH_NOTIFICATION);
//                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                String notificationTitle = pushNotificationObject.getTask()
                        + " - sent by "
                        + pushNotificationObject.getUser().getName();
                Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
                showNotificationMessage(
                        getApplicationContext(),
                        notificationTitle,
                        getReminderFormatedDate(Util.getCalenderFormatDate(pushNotificationObject.getDate())),
                        "time_stamp",
                        resultIntent
                );

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                // app is in background, show the notification in notification tray
                String notificationTitle = pushNotificationObject.getTask()
                        + " - sent by "
                        + pushNotificationObject.getUser().getName();

                Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
                showNotificationMessage(
                        getApplicationContext(),
                        notificationTitle,
                        getReminderFormatedDate(Util.getCalenderFormatDate(pushNotificationObject.getDate())),
                        "time_stamp",
                        resultIntent
                );
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    private void showNotificationMessageWithBigImage(Context context, String title, String message,
                                                     String timeStamp, Intent intent, String imageUrl) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }

    private void showNotificationMessage(Context context, String title, String message,
                                         String timeStamp, Intent intent) {
        NotificationUtils notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }
}