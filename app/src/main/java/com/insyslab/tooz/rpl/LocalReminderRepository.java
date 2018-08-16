package com.insyslab.tooz.rpl;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;

import com.insyslab.tooz.models.LocalReminder;
import com.insyslab.tooz.utils.ToozApplication;

import java.util.List;

public class LocalReminderRepository {

    private AppDatabase appDatabase;

    public LocalReminderRepository(ToozApplication toozApplication) {
        appDatabase = Room.databaseBuilder(toozApplication, AppDatabase.class, AppDatabase.DB_NAME).build();
    }

    public void insertLocalReminders(final List<LocalReminder> localReminderList) {
        final AsyncTask<Void, Void, Void> asyncTaskIns = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.localReminderDao().insertAll(localReminderList);
                return null;
            }


            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    public LiveData<List<LocalReminder>> getAllLocalReminders() {
        return appDatabase.localReminderDao().fetchAllLocalReminders();
    }

    public LiveData<LocalReminder> getLocalReminderFromUniqueInt(int uniqueInt) {
        return appDatabase.localReminderDao().fetchLocalReminderByUniqueInt(uniqueInt);
    }

//    public int clearLocalReminderTable() {
//        return appDatabase.localReminderDao().deleteAllLocalReminders();
//    }
}
