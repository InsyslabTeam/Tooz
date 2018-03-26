package com.insyslab.tooz.rpl;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.TypeConverters;

import com.insyslab.tooz.models.Reminder;

import java.util.Date;
import java.util.List;

/**
 * Created by TaNMay on 27/12/17.
 */

@Dao
@TypeConverters(TimestampConverter.class)
public interface ReminderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Reminder> reminders);

    @Query("delete from reminder where id=:id")
    int deleteReminder(String id);

    @Query("delete from reminder")
    int deleteAllReminders();

    @Query("select * from reminder where id=:id")
    LiveData<Reminder> fetchReminderByReminderId(int id);

    @Query("select * from reminder")
    LiveData<List<Reminder>> fetchAllReminders();

    @Query("select * from reminder where date < :date")
    LiveData<List<Reminder>> fetchPastReminders(Date date);

    @Query("select * from reminder where date > :date")
    LiveData<List<Reminder>> fetchUpcomingReminders(Date date);
}