package com.insyslab.tooz.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.insyslab.tooz.utils.ToozApplication;

public class LocationService extends Service implements LocationListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final long INTERVAL = 1000 * 10;                                     // in millis
    private static final long FASTEST_INTERVAL = 1000 * 5;                              // in millis

    private final String TAG = LocationService.class.getSimpleName() + " ==>";

    private GoogleApiClient mGoogleApiClient;

    private Location mCurrentLocation;

    public LocationService() {
        Log.d(TAG, "Default Constructor!");

    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate");
        buildGoogleApiClient();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(TAG, "onStartCommand");

        mGoogleApiClient.connect();

        return START_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public synchronized void buildGoogleApiClient() {
        Log.d(TAG, "buildGoogleApiClient");

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        createLocationRequest();
    }

    private void createLocationRequest() {
        Log.d(TAG, "createLocationRequest");

        LocationRequest mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy");
        Intent broadcastIntent = new Intent();
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        ToozApplication.getInstance().setLastLocation(mCurrentLocation);
        Log.d(TAG, "onLocationChanged: " + (mCurrentLocation != null ? mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude() : "null"));
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {
        Log.d(TAG, "onStatusChanged");

    }

    @Override
    public void onProviderEnabled(String s) {
        Log.d(TAG, "onProviderEnabled");

    }

    @Override
    public void onProviderDisabled(String s) {
        Log.d(TAG, "onProviderDisabled");

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        ToozApplication.getInstance().setLastLocation(mCurrentLocation);
        Log.d(TAG, "onLocationChanged: " + (mCurrentLocation != null ? mCurrentLocation.getLatitude() + ", " + mCurrentLocation.getLongitude() : "null"));
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.d(TAG, "onConnectionSuspended");
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.d(TAG, "onConnectionFailed");
        mGoogleApiClient.connect();
    }

    public Location getLocation() {
        return mCurrentLocation;
    }
}