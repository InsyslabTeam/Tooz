package com.insyslab.tooz.rpl;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.insyslab.tooz.models.LocalReminder;
import com.insyslab.tooz.models.Reminder;

import java.util.Date;
import java.util.List;

/**
 * Created by TaNMay on 27/12/17.
 */

@Dao
@TypeConverters(TimestampConverter.class)
public interface LocalReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<LocalReminder> localReminders);

    @Query("delete from localreminder where id=:id")
    int deleteLocalReminder(int id);

    @Query("delete from localreminder")
    int deleteAllLocalReminders();

    @Query("select * from localreminder where id=:id")
    LiveData<LocalReminder> fetchLocalReminderById(int id);

    @Query("select * from localreminder where uniqueInt=:uniqueInt")
    LiveData<LocalReminder> fetchLocalReminderByUniqueInt(int uniqueInt);

    @Query("select * from localreminder")
    LiveData<List<LocalReminder>> fetchAllLocalReminders();

    @Query("select * from localreminder where date < :date")
    LiveData<List<LocalReminder>> fetchPastLocalReminders(Date date);

    @Query("select * from localreminder where date > :date")
    LiveData<List<LocalReminder>> fetchUpcomingLocalReminders(Date date);
}