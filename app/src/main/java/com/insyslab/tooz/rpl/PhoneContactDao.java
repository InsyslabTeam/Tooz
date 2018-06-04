package com.insyslab.tooz.rpl;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.insyslab.tooz.models.PhoneContact;

import java.util.List;

@Dao
public interface PhoneContactDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PhoneContact> phoneContacts);

    @Query("delete from phonecontact where p_id=:id")
    int deletePhoneContact(int id);

    @Query("delete from phonecontact")
    int deleteAllPhoneContacts();

    @Query("select * from phonecontact where p_id=:id")
    LiveData<PhoneContact> fetchPhoneContactById(int id);

    @Query("select * from phonecontact")
    LiveData<List<PhoneContact>> fetchAllPhoneContacts();

    @Query("select * from phonecontact where isSynced=:isSynced")
    LiveData<List<PhoneContact>> fetchPhoneContacts(Boolean isSynced);
}