package com.insyslab.tooz.rpl;

import android.arch.persistence.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.UserGroup;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by TaNMay on 04/06/18.
 */

public class UserListConverter {

    @TypeConverter
    public ArrayList<User> storedStringToUsers(String value) {
        Type type = new TypeToken<List<User>>() {
        }.getType();
        return new Gson().fromJson(value, type);
    }

    @TypeConverter
    public String usersToStoredString(ArrayList<User> users) {
        return new Gson().toJson(users);
    }
}
