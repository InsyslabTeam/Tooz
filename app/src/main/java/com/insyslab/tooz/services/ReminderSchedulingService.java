package com.insyslab.tooz.services;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.IBinder;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.insyslab.tooz.R;
import com.insyslab.tooz.broadcastreceivers.ReminderReceiver;
import com.insyslab.tooz.models.LocalReminder;
import com.insyslab.tooz.models.Reminder;
import com.insyslab.tooz.rpl.LocalReminderRepository;
import com.insyslab.tooz.rpl.ReminderRepository;
import com.insyslab.tooz.ui.activities.DashboardActivity;
import com.insyslab.tooz.utils.ToozApplication;
import com.insyslab.tooz.utils.Util;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import static com.insyslab.tooz.utils.AppConstants.KEY_REMINDER_NOTIFICATION_OBJECT;
import static com.insyslab.tooz.utils.AppConstants.KEY_REMINDER_NOTIFICATION_UNIQUE_ID;
import static com.insyslab.tooz.utils.AppConstants.VAL_REMINDER_NOTIFICATION_ACTION;

/**
 * Created by TaNMay on 22/01/18.
 */

public class ReminderSchedulingService extends Service implements LifecycleOwner {

    private final String TAG = "ReminderSched ==>> ";

    private LifecycleRegistry mLifecycleRegistry;

    private ReminderRepository reminderRepository;
    private LocalReminderRepository localReminderRepository;

    private List<Reminder> reminderList;
    private List<LocalReminder> localReminderList;
    private HashMap<String, LocalReminder> localReminderHashMap;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        mLifecycleRegistry = new LifecycleRegistry(this);
        mLifecycleRegistry.markState(Lifecycle.State.CREATED);

        reminderRepository = new ReminderRepository((ToozApplication) getApplicationContext());
        reminderList = new ArrayList<>();

        localReminderRepository = new LocalReminderRepository((ToozApplication) getApplicationContext());
        localReminderList = new ArrayList<>();
        localReminderHashMap = new HashMap<>();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mLifecycleRegistry.markState(Lifecycle.State.STARTED);

        fetchLocalRemindersFromDb();
        fetchUpcomingRemindersFromDb();

        return Service.START_NOT_STICKY;
    }

    private void fetchLocalRemindersFromDb() {
        localReminderRepository.getAllLocalReminders().observe(this, new Observer<List<LocalReminder>>() {
            @Override
            public void onChanged(@Nullable List<LocalReminder> localReminders) {
                Log.d(TAG, "fetchLocalRemindersFromDb: size = " + localReminders.size());
                localReminderList = localReminders;
                if (localReminderHashMap.size() == 0)
                    createLocalRemindersHashmap();
            }
        });
    }

    private void createLocalRemindersHashmap() {
        for (int i = 0; i < localReminderList.size(); i++) {
            LocalReminder locRem = localReminderList.get(i);
            localReminderHashMap.put(locRem.getId(), locRem);
        }
    }

    private void fetchUpcomingRemindersFromDb() {
        reminderRepository.getUpcomingReminders(Calendar.getInstance().getTime()).observe(this, new Observer<List<Reminder>>() {
            @Override
            public void onChanged(@Nullable List<Reminder> reminders) {
                Log.d(TAG, "fetchUpcomingRemindersFromDb: size = " + reminders.size());
                reminderList = reminders;
                initReminderNotifications();
            }
        });

    }

    private void initReminderNotifications() {
        for (int i = 0; i < reminderList.size(); i++) {
            Reminder rem = reminderList.get(i);
            if (!localReminderHashMap.containsKey(rem.getId()))
                initSetLocalReminder(rem, i == reminderList.size() - 1);
            else if (i == reminderList.size() - 1)
                updateLocalRemindersDb();
            else
                Log.d(TAG, "not last rem, already in db");
        }
    }

    private void updateLocalRemindersDb() {
        localReminderRepository.insertLocalReminders(localReminderList);
    }

    private void initSetLocalReminder(Reminder rem, boolean isLastReminder) {
        Long currentTimeInMillis = Calendar.getInstance().getTimeInMillis();
        Calendar cal = Util.getCalenderFormatDate(rem.getDate());
        Long reminderTimeInMillis = cal.getTimeInMillis();
        Long remainingTime = reminderTimeInMillis - currentTimeInMillis;

        if (remainingTime > 0) {
            scheduleNotificationForReminder(rem, remainingTime, isLastReminder);
        } else {
            if (isLastReminder)
                updateLocalRemindersDb();
            Log.d(TAG, "remaining time is less that 0");
        }
    }

    private void scheduleNotificationForReminder(Reminder rem, Long remainingTime, boolean isLastReminder) {
        int uniqueInt = (int) (System.currentTimeMillis() & 0xfffffff);
        Notification remNotification = createNotificationObject(rem);

        Intent notificationIntent = new Intent(this, ReminderReceiver.class);
        notificationIntent.setAction(VAL_REMINDER_NOTIFICATION_ACTION);
        notificationIntent.putExtra(KEY_REMINDER_NOTIFICATION_UNIQUE_ID, uniqueInt);
        notificationIntent.putExtra(KEY_REMINDER_NOTIFICATION_OBJECT, remNotification);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, uniqueInt, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        updateListForLocalReminderDb(rem, uniqueInt, isLastReminder);

        long timeInMillis = Util.getCalenderFormatDate(rem.getDate()).getTimeInMillis();
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeInMillis, pendingIntent);
    }

    private void updateListForLocalReminderDb(Reminder rem, int uniqueInt, boolean isLastReminder) {
        LocalReminder localReminder = new LocalReminder();
        localReminder.setId(rem.getId());
        localReminder.setTask(rem.getTask());
        localReminder.setDate(rem.getDate());
        localReminder.setLatitude(rem.getLatitude());
        localReminder.setLongitude(rem.getLongitude());
        localReminder.setUniqueInt(uniqueInt);
        localReminder.setCreatedAt(rem.getCreatedAt());
        localReminder.setUpdatedAt(rem.getUpdatedAt());

        localReminderList.add(localReminder);

        if (isLastReminder)
            updateLocalRemindersDb();
    }

    private Notification createNotificationObject(Reminder rem) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        Intent resultIntent = new Intent(this, DashboardActivity.class);
        PendingIntent mainActivityPendingIntent = PendingIntent.getActivity(this, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        builder.setContentTitle(rem.getTask());
        builder.setContentText("by " + rem.getUser().getName());
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setSound(Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.penny));
        builder.setAutoCancel(true);
//        builder.setDeleteIntent(getDeleteIntent());           //set the behaviour when the notification is cleared
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
