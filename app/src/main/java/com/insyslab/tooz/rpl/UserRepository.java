package com.insyslab.tooz.rpl;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;

import com.insyslab.tooz.models.User;
import com.insyslab.tooz.utils.ToozApplication;

import java.util.List;

public class UserRepository {

    private AppDatabase appDatabase;

    public UserRepository(ToozApplication toozApplication) {
        appDatabase = Room.databaseBuilder(toozApplication, AppDatabase.class, AppDatabase.DB_NAME).build();
    }

    public void insertUsers(final List<User> userList) {
        final AsyncTask<Void, Void, Void> asyncTaskIns = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.userDao().insertAll(userList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        };

        final AsyncTask<Void, Void, Void> asyncTaskDel = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.userDao().deleteAllUsers();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                asyncTaskIns.execute();
            }
        }.execute();
    }


    public LiveData<List<User>> getAllUser() {
        return appDatabase.userDao().fetchAllUsers();
    }

    public LiveData<List<User>> getAppUserContacts() {
        return appDatabase.userDao().fetchAppUserContacts();
    }

    public LiveData<User> getUserFromId(String id) {
        return appDatabase.userDao().fetchUserByUserId(id);
    }

    public LiveData<List<User>> getNonAppUserContacts() {
        return appDatabase.userDao().fetchNonAppUserContacts();
    }

    public void deleteUserFromUserId(final String id) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.userDao().deleteUser(id);
                return null;
            }
        }.execute();
    }

    public void clearUserTable() {
        appDatabase.userDao().deleteAllUsers();
    }
}
