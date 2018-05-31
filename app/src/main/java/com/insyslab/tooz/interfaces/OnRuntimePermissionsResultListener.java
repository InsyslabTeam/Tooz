package com.insyslab.tooz.interfaces;

public interface OnRuntimePermissionsResultListener {

    void onSmsPermissionsResult(boolean granted);

    void onContactsPermissionsResult(boolean granted);

    void onStoragePermissionsResult(boolean granted);

    void onLocationPermissionsResult(boolean granted);
}
