package com.insyslab.tooz.interfaces;

/**
 * Created by TaNMay on 17/10/17.
 */

public interface OnRuntimePermissionsResultListener {

    void onSmsPermissionsResult(boolean granted);

    void onContactsPermissionsResult(boolean granted);
}
