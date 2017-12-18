package com.insyslab.tooz.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.insyslab.tooz.models.User;

import static com.insyslab.tooz.utils.AppConstants.KEY_SHARED_PREFS_FIRST_LOGIN;
import static com.insyslab.tooz.utils.AppConstants.KEY_SHARED_PREFS_USER;
import static com.insyslab.tooz.utils.AppConstants.KEY_TOOZ_SHARED_PREFERENCE;
import static com.insyslab.tooz.utils.AppConstants.KEY_USER_SHARED_PREFERENCE;

/**
 * Created by TaNMay on 27/09/16.
 */

public class LocalStorage {

    private static LocalStorage instance = null;
    private SharedPreferences userSharedPreferences;
//    private SharedPreferences toozSharedPreferences;

    public LocalStorage(Context context) {
        userSharedPreferences = context.getSharedPreferences(KEY_USER_SHARED_PREFERENCE, 0);
//        toozSharedPreferences = context.getSharedPreferences(KEY_TOOZ_SHARED_PREFERENCE, 0);
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

//    public void clearToozSharedPreferences() {
//        SharedPreferences.Editor editor = toozSharedPreferences.edit();
//        editor.clear();
//        editor.apply();
//    }

    public String getToken() {
        User user = getUser();
        if (user != null && user.getToken() != null) return user.getToken().getAccessToken();
        else return null;
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

    public Boolean isFirstLogin() {
        if (userSharedPreferences.contains(KEY_SHARED_PREFS_FIRST_LOGIN)) {
            return userSharedPreferences.getBoolean(KEY_SHARED_PREFS_FIRST_LOGIN, true);
        } else return true;
    }

    public void firstLoginCompleted() {
        SharedPreferences.Editor editor = userSharedPreferences.edit();
        editor.putBoolean(KEY_SHARED_PREFS_FIRST_LOGIN, false);
        editor.commit();
    }
}