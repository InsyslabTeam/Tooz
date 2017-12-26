package com.insyslab.tooz.rpl;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 * Created by TaNMay on 07/12/17.
 */

@Dao
public interface ContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(Contact... contacts);

    @Update
    void updateContacts(Contact... contacts);

    @Delete
    void deleteContacts(Contact... contacts);

    @Query("delete from contact where id=:id")
    int deleteContact(int id);

    @Query("SELECT * FROM contact")
    List<Contact> getAllContacts();

}
