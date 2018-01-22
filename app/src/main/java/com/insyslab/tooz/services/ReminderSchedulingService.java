package com.insyslab.tooz.services;

import android.app.Service;
import android.arch.lifecycle.LiveData;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.insyslab.tooz.models.Reminder;
import com.insyslab.tooz.rpl.ReminderRepository;
import com.insyslab.tooz.utils.ToozApplication;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by TaNMay on 22/01/18.
 */

public class ReminderSchedulingService extends Service {

    private final String TAG = "ReminderSched ==> ";

    public ReminderRepository reminderRepository;

    private List<Reminder> reminderList;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        reminderRepository = new ReminderRepository((ToozApplication) getApplicationContext());

        reminderList = new ArrayList<>();

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        fetchUpcomingRemindersFromDb();

        return Service.START_NOT_STICKY;
    }

    private void fetchUpcomingRemindersFromDb() {
        LiveData<List<Reminder>> liveData = reminderRepository.getUpcomingReminders(Calendar.getInstance().getTime());

        Log.d(TAG, "fetchUpcomingRemindersFromDb: liveData.getValue().size() - " + (liveData.getValue() != null ? liveData.getValue().size() : "null"));
    }
}
