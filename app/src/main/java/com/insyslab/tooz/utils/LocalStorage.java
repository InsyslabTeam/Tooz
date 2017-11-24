package com.insyslab.tooz.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.insyslab.tooz.models.User;

import static com.insyslab.tooz.utils.AppConstants.KEY_SHARED_PREFS_TOKEN;
import static com.insyslab.tooz.utils.AppConstants.KEY_SHARED_PREFS_USER;
import static com.insyslab.tooz.utils.AppConstants.KEY_TOOZ_SHARED_PREFERENCE;
import static com.insyslab.tooz.utils.AppConstants.KEY_USER_SHARED_PREFERENCE;

/**
 * Created by TaNMay on 27/09/16.
 */

public class LocalStorage {

    private static LocalStorage instance = null;
    SharedPreferences userSharedPreferences;
    SharedPreferences sellfashSharedPreferences;

    public LocalStorage(Context context) {
        userSharedPreferences = context.getSharedPreferences(KEY_USER_SHARED_PREFERENCE, 0);
        sellfashSharedPreferences = context.getSharedPreferences(KEY_TOOZ_SHARED_PREFERENCE, 0);
    }

    public static LocalStorage getInstance(Context context) {
        if (instance == null) {
            synchronized (LocalStorage.class) {
                if (instance == null) {
                    instance = new LocalStorage(context);
                }
            }
        }
        return instance;
    }

    public void clearUserSharedPreferences() {
        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public void clearToozSharedPreferences() {
        SharedPreferences.Editor editor = sellfashSharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

    public String getToken() {
        if (userSharedPreferences.contains(KEY_SHARED_PREFS_TOKEN))
            return userSharedPreferences.getString(KEY_SHARED_PREFS_TOKEN, null);
        else return null;
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.putString(KEY_SHARED_PREFS_TOKEN, token);
        editor.commit();
    }

    public User getUser() {
        if (userSharedPreferences.contains(KEY_SHARED_PREFS_USER)) {
            return new Gson().fromJson(userSharedPreferences.getString(KEY_SHARED_PREFS_USER, null), User.class);
        } else return null;
    }

    public void setUser(User user) {
        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.putString(KEY_SHARED_PREFS_USER, new Gson().toJson(user));
        editor.commit();
    }
}