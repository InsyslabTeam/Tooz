package com.insyslab.tooz.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;
import com.insyslab.tooz.R;
import com.insyslab.tooz.models.LocalReminder;
import com.insyslab.tooz.rpl.LocalReminderRepository;
import com.insyslab.tooz.ui.activities.DashboardActivity;
import com.insyslab.tooz.utils.ToozApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TaNMay on 20/02/18.
 */

public class GeofenceTransitionsIntentService extends IntentService implements LifecycleOwner {

    private final String TAG = "GeofenceTransIS ==>>>";

    private LifecycleRegistry mLifecycleRegistry;

    private LocalReminderRepository localReminderRepository;
    private List<LocalReminder> localReminderList;

    public GeofenceTransitionsIntentService() {
        super("GeofenceTransitionsIntentService");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);
    }

    @Override
    public int onStartCommand(@Nullable Intent intent, int flags, int startId) {
        mLifecycleRegistry.markState(Lifecycle.State.STARTED);

        return START_NOT_STICKY;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d(TAG, "onHandleIntent");

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);
        if (geofencingEvent.hasError()) {
            Log.d(TAG, "geofencingEvent.hasError()");
            return;
        }

        int geofenceTransition = geofencingEvent.getGeofenceTransition();

        if (geofenceTransition == Geofence.GEOFENCE_TRANSITION_ENTER ||
                geofenceTransition == Geofence.GEOFENCE_TRANSITION_EXIT) {
            Log.d(TAG, "Success 101!");

            List<Geofence> triggeringGeofences = geofencingEvent.getTriggeringGeofences();
            getGeofenceTransitionDetails(geofenceTransition, triggeringGeofences);
        } else {
            Log.d(TAG, "Error 101!");
        }
    }

    private void getGeofenceTransitionDetails(int geofenceTransition, List<Geofence> triggeringGeofences) {
        ArrayList<String> triggeringGeofencesIdsList = new ArrayList<>();
        for (Geofence geofence : triggeringGeofences) {
            triggeringGeofencesIdsList.add(geofence.getRequestId());
        }

        if (triggeringGeofencesIdsList.size() > 0)
            initGeoReminderNotification(triggeringGeofencesIdsList);
    }

    private void initGeoReminderNotification(ArrayList<String> triggeringGeofencesIdsList) {
        localReminderList = new ArrayList<>();
        localReminderRepository = new LocalReminderRepository((ToozApplication) getApplicationContext());

        for (int i = 0; i < triggeringGeofencesIdsList.size(); i++) {
            fetchRemindersFromLocalDb(triggeringGeofencesIdsList.get(i));
        }
    }

    private void fetchRemindersFromLocalDb(String uniqueStr) {
        Integer uniqueInt = Integer.parseInt(uniqueStr);
        localReminderRepository.getLocalReminderFromUniqueInt(uniqueInt).observe(this, new Observer<LocalReminder>() {
            @Override
            public void onChanged(@Nullable LocalReminder localReminder) {
                localReminderList.add(localReminder);

                sendNotificationForLocalReminder(localReminder);
            }
        });
    }

    private void sendNotificationForLocalReminder(LocalReminder localReminder) {
        Notification notification = createNotificationObject(localReminder);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int id = localReminder.getUniqueInt();
        notificationManager.notify(id, notification);
    }

    private Notification createNotificationObject(LocalReminder rem) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent resultIntent = new Intent(this, DashboardActivity.class);
        PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle(rem.getTask());
        builder.setContentText("by " + rem.getFromUser());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.penny));
        builder.setAutoCancel(true);
        builder.setContentIntent(mainActivityPendingIntent);
        return builder.build();
    }

    @Override
    public void onDestroy() {
        mLifecycleRegistry.markState(Lifecycle.State.DESTROYED);
        super.onDestroy();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return mLifecycleRegistry;
    }
}
