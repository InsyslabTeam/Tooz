package com.insyslab.tooz.rpl;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.insyslab.tooz.models.User;

import java.util.List;

/**
 * Created by TaNMay on 27/12/17.
 */

@Dao
public interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<User> user);

    @Query("delete from user where id=:id")
    int deleteUser(int id);

    @Query("delete from user")
    int deleteAllUsers();

    @Query("select * from user where id=:id")
    LiveData<User> fetchUserByUserId(int id);

    @Query("select * from user")
    LiveData<List<User>> fetchAllUsers();

    @Query("select * from user where id IS NOT NULL")
    LiveData<List<User>> fetchAppUserContacts();

    @Query("select * from user where id IS NULL")
    LiveData<List<User>> fetchNonAppUserContacts();
}