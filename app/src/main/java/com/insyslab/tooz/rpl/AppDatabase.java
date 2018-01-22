package com.insyslab.tooz.rpl;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.insyslab.tooz.models.PhoneContact;
import com.insyslab.tooz.models.Reminder;
import com.insyslab.tooz.models.User;

/**
 * Created by TaNMay on 07/12/17.
 */

@Database(version = 1, entities = {User.class, Reminder.class, PhoneContact.class})
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "tooz_db";

    public abstract UserDao userDao();

    public abstract ReminderDao reminderDao();

    public abstract PhoneContactDao phoneContactDao();

}
