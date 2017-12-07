package com.insyslab.tooz.rpl;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

/**
 * Created by TaNMay on 07/12/17.
 */

@Database(version = 1, entities = {Contact.class})
public abstract class AppDatabase extends RoomDatabase {

    public static final String DB_NAME = "tooz_db";

    private static AppDatabase INSTANCE;

    public abstract ContactDao contactDao();

    public static AppDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, DB_NAME)
                            .allowMainThreadQueries()
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }

}
