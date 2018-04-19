package com.insyslab.tooz.rpl;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import com.insyslab.tooz.models.UserGroup;

import java.util.List;

/**
 * Created by TaNMay on 27/12/17.
 */

@Dao
public interface UserGroupDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<UserGroup> userGroups);

    @Query("delete from UserGroup where id=:id")
    int deleteUserGroup(int id);

    @Query("delete from UserGroup")
    int deleteAllUserGroups();

    @Query("select * from UserGroup where id=:id")
    LiveData<UserGroup> fetchUserGroupById(String id);

    @Query("select * from UserGroup")
    LiveData<List<UserGroup>> fetchAllUserGroups();
}