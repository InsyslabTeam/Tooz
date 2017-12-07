package com.insyslab.tooz.rpl;


import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;

public class DatabaseInitializer {

    private static final String TAG = DatabaseInitializer.class.getName();

    public static void populateAsync(@NonNull final AppDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void populateSync(@NonNull final AppDatabase db) {
        populateWithTestData(db);
    }

    private static Contact addContact(final AppDatabase db, Contact contact) {
        db.contactDao().insertAll(contact);
        return contact;
    }

    private static void populateWithTestData(AppDatabase db) {
        Contact contact = new Contact();
        contact.setName("Name");
        contact.setUserId("id");
        addContact(db, contact);

        List<Contact> contactList = db.contactDao().getAllContacts();
        Log.d(DatabaseInitializer.TAG, "Rows Count: " + contactList.size());
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }

    }
}
