package com.insyslab.tooz.rpl;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;

import com.insyslab.tooz.models.PhoneContact;
import com.insyslab.tooz.utils.ToozApplication;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TaNMay on 20/12/17.
 */

public class PhoneContactRepository {

    private AppDatabase appDatabase;

    public PhoneContactRepository(ToozApplication toozApplication) {
        appDatabase = Room.databaseBuilder(toozApplication, AppDatabase.class, AppDatabase.DB_NAME).build();
    }

    public void insertPhoneContacts(final List<PhoneContact> phoneContactList) {
        final AsyncTask<Void, Void, Void> asyncTaskIns = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.phoneContactDao().insertAll(phoneContactList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        };

        AsyncTask<Void, Void, Void> asyncTaskDel = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.phoneContactDao().deleteAllPhoneContacts();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                asyncTaskIns.execute();
            }
        }.execute();

    }

    public void insertPhoneContact(PhoneContact phoneContact) {
        final List<PhoneContact> list = new ArrayList<>();
        list.add(phoneContact);

        final AsyncTask<Void, Void, Void> asyncTaskIns = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.phoneContactDao().insertAll(list);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }

    public LiveData<List<PhoneContact>> getAllPhoneContacts() {
        return appDatabase.phoneContactDao().fetchAllPhoneContacts();
    }

    public LiveData<List<PhoneContact>> getSyncedPhoneContacts() {
        return appDatabase.phoneContactDao().fetchPhoneContacts(true);
    }

    public LiveData<List<PhoneContact>> getNonSyncedPhoneContacts() {
        return appDatabase.phoneContactDao().fetchPhoneContacts(false);
    }

    public int clearPhoneContactTable() {
        return appDatabase.phoneContactDao().deleteAllPhoneContacts();
    }
}
