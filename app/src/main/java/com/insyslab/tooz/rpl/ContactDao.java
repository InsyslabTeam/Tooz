package com.insyslab.tooz.rpl;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.insyslab.tooz.models.User;

import java.util.List;

/**
 * Created by TaNMay on 07/12/17.
 */

@Dao
public interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(User... contacts);

    @Update
    void updateUsers(User... contacts);

    @Delete
    void deleteUsers(User... contacts);

    @Query("delete from user where id=:id")
    int deleteContact(int id);

    @Query("SELECT * FROM user")
    List<User> getAllContacts();

}
