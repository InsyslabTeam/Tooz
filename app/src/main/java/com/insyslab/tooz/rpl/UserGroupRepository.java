package com.insyslab.tooz.rpl;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;

import com.insyslab.tooz.models.UserGroup;
import com.insyslab.tooz.utils.ToozApplication;

import java.util.List;

public class UserGroupRepository {

    private AppDatabase appDatabase;

    public UserGroupRepository(ToozApplication toozApplication) {
        appDatabase = Room.databaseBuilder(toozApplication, AppDatabase.class, AppDatabase.DB_NAME).build();
    }

    public void insertGroups(final List<UserGroup> userGroupList) {
        final AsyncTask<Void, Void, Void> asyncTaskIns = new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.userGroupDao().insertAll(userGroupList);
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
                appDatabase.userGroupDao().deleteAllUserGroups();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                asyncTaskIns.execute();
            }
        }.execute();
    }


    public LiveData<List<UserGroup>> getAllUserGroups() {
        return appDatabase.userGroupDao().fetchAllUserGroups();
    }

    public LiveData<UserGroup> getGroupFromId(String id) {
        return appDatabase.userGroupDao().fetchUserGroupById(id);
    }

    public void clearUserGroupTable() {
        appDatabase.userGroupDao().deleteAllUserGroups();
    }
}
