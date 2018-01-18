package com.insyslab.tooz.rpl;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.annotation.Nullable;

import com.insyslab.tooz.models.Reminder;
import com.insyslab.tooz.utils.ToozApplication;

import java.util.List;

/**
 * Created by TaNMay on 20/12/17.
 */

public class ReminderRepository {

    private AppDatabase appDatabase;

    public ReminderRepository(ToozApplication toozApplication) {
        appDatabase = Room.databaseBuilder(toozApplication, AppDatabase.class, AppDatabase.DB_NAME).build();
    }

    public void insertReminders(final List<Reminder> reminderList) {
        final AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.reminderDao().insertAll(reminderList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    public LiveData<List<Reminder>> getAllReminder() {
        return appDatabase.reminderDao().fetchAllReminders();
    }
}
