package com.insyslab.tooz.utils;


import android.app.Application;
import android.content.Context;
import android.location.Location;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.insyslab.tooz.rpl.AppDatabase;

/**
 * Created by TaNMay on 31-08-2016.
 */
public class ToozApplication extends MultiDexApplication {

    private static ToozApplication toozApplication = null;

    private final String TAG = "ToozApplication ==>";

    private RequestQueue mRequestQueue;

    public static synchronized ToozApplication getInstance() {
        return toozApplication;
    }

    private Location mLastLocation;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        toozApplication = this;
//        deleteDatabase(AppDatabase.DB_NAME);

    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        }
        mRequestQueue.getCache().clear();
        return mRequestQueue;
    }

    public <T> void addToRequestQueue(Request<T> req, String tag) {
        // set the default tag if tag is empty
        req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public Location getLastLocation() {
        return mLastLocation;
    }

    public void setLastLocation(Location mLastLocation) {
        this.mLastLocation = mLastLocation;
    }
}
