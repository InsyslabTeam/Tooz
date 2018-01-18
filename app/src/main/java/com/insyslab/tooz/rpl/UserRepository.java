package com.insyslab.tooz.rpl;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;

import com.insyslab.tooz.models.User;
import com.insyslab.tooz.utils.ToozApplication;

import java.util.List;

/**
 * Created by TaNMay on 20/12/17.
 */

public class UserRepository {

    private AppDatabase appDatabase;

    public UserRepository(ToozApplication toozApplication) {
        appDatabase = Room.databaseBuilder(toozApplication, AppDatabase.class, AppDatabase.DB_NAME).build();
    }

    public void insertUsers(final List<User> userList) {
        final AsyncTask<Void, Void, Void> asyncTask = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.userDao().insertAll(userList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }.execute();
    }


    public LiveData<List<User>> getAllUser() {
        return appDatabase.userDao().fetchAllUsers();
    }
}
