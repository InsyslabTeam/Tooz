package com.insyslab.tooz.ui.activities;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnRuntimePermissionsResultListener;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.UserGroup;
import com.insyslab.tooz.rpl.PhoneContactRepository;
import com.insyslab.tooz.rpl.ReminderRepository;
import com.insyslab.tooz.rpl.UserGroupRepository;
import com.insyslab.tooz.rpl.UserRepository;
import com.insyslab.tooz.services.LocationService;
import com.insyslab.tooz.services.ReminderSchedulingService;
import com.insyslab.tooz.utils.ToozApplication;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public abstract class BaseActivity extends AppCompatActivity {

    public static OnRuntimePermissionsResultListener onRuntimePermissionsResultListener;

    private final String TAG = BaseActivity.class.getSimpleName() + " ==>";

    private final int REQUEST_SMS_PERMISSION_CODE = 1;
    private final int REQUEST_CONTACTS_PERMISSION_CODE = 2;
    private final int REQUEST_STORAGE_PERMISSION_CODE = 3;
    private final int REQUEST_LOCATION_PERMISSION_CODE = 4;

    public UserRepository userRepository;
    public UserGroupRepository userGroupRepository;
    public ReminderRepository reminderRepository;
    public PhoneContactRepository phoneContactRepository;

    public Intent locationServiceIntent;

    protected ProgressDialog mProgressDialog = null;

    private List<User> appUserList = new ArrayList<>(), nonAppUserList = new ArrayList<>();
    private List<UserGroup> userGroupList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        userRepository = new UserRepository((ToozApplication) getApplicationContext());
        userGroupRepository = new UserGroupRepository((ToozApplication) getApplicationContext());
        reminderRepository = new ReminderRepository((ToozApplication) getApplicationContext());
        phoneContactRepository = new PhoneContactRepository((ToozApplication) getApplicationContext());
        super.onCreate(savedInstanceState);
    }

    protected void getFragmentStatus() {
        EventBus.getDefault().registerSticky(this);
    }

    protected void showToastMessage(String message, boolean isLong) {
        Toast toast;

        if (isLong) toast = Toast.makeText(this, message, Toast.LENGTH_LONG);
        else toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);

        toast.show();
    }

    protected void showSnackbarMessage(View view, String message, boolean isLong, String action,
                                       View.OnClickListener onClickListener, boolean isDismissed) {
        if (view != null) {
            Snackbar snackbar = null;

            if (isLong) snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG);
            else if (!isLong) snackbar = Snackbar.make(view, message, Snackbar.LENGTH_SHORT);

            if (action != null && isDismissed) {
                final Snackbar finalSnackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
                snackbar = snackbar.setAction(action, new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finalSnackbar.dismiss();
                    }
                });
            } else if (action != null)
                snackbar = Snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction(action, onClickListener);

            snackbar.show();
        }
    }

    protected void showNetworkErrorSnackbar(View view, String message, String action, View.OnClickListener mOnClickListener) {
        Snackbar snackbar = Snackbar
                .make(view, message, Snackbar.LENGTH_INDEFINITE)
                .setAction(action, mOnClickListener);
        snackbar.show();
    }

    protected void showProgressDialog(String message) {
        if (mProgressDialog == null) {
            mProgressDialog = ProgressDialog.show(BaseActivity.this, null, message, true, false, null);
        } else {
            mProgressDialog.setMessage(message);
            mProgressDialog.show();
        }
    }

    protected void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    protected void showConfirmationDialog(
            String prompt, String positiveButton, String negativeButton,
            DialogInterface.OnClickListener positiveClick,
            DialogInterface.OnClickListener negativeClick) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(prompt)
                .setPositiveButton(positiveButton, positiveClick);
        if (negativeClick != null) {
            builder.setNegativeButton(negativeButton, negativeClick);
        } else {
            builder.setNegativeButton(negativeButton, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            });
        }
        builder.show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_SMS_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean smsPermissions = grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    onRuntimePermissionsResultListener.onSmsPermissionsResult(smsPermissions);

                    if (!smsPermissions) {

                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            if (shouldShowRequestPermissionRationale(READ_SMS)) {
                                showSnackbarMessage(
                                        findViewById(R.id.ao_fragment_container),
                                        "SMS Permissions Denied!",
                                        true,
                                        getString(R.string.retry),
                                        new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                requestSmsPermissions();
                                            }
                                        },
                                        false);
                            }
                        } else {
                            showSnackbarMessage(
                                    findViewById(R.id.ao_fragment_container),
                                    "SMS Permissions Denied!",
                                    true,
                                    getString(R.string.retry),
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            requestSmsPermissions();
                                        }
                                    },
                                    false);
                        }
                    }
                }
                break;

            case REQUEST_CONTACTS_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean contactPermissions = grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (!contactPermissions) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            if (!shouldShowRequestPermissionRationale(READ_CONTACTS)) {
                                showConfirmationDialog(
                                        "Please enable contact permissions to proceed!",
                                        getString(R.string.ok),
                                        null,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivityForResult(intent, REQUEST_CONTACTS_PERMISSION_CODE);
                                            }
                                        },
                                        null
                                );
                            }
                        } else {
                            onRuntimePermissionsResultListener.onContactsPermissionsResult(contactPermissions);
                            showSnackbarMessage(
                                    findViewById(R.id.ao_fragment_container),
                                    "Contacts Permissions Denied!",
                                    true,
                                    getString(R.string.retry),
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            requestContactsPermissions();
                                        }
                                    },
                                    false);
                        }
                    } else {
                        onRuntimePermissionsResultListener.onContactsPermissionsResult(contactPermissions);
                    }
                }
                break;

            case REQUEST_STORAGE_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean storagePermissions = grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (!storagePermissions) {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            if (!shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE)) {
                                showConfirmationDialog(
                                        "Please enable storage permissions to select a profile picture!",
                                        getString(R.string.ok),
                                        null,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivityForResult(intent, REQUEST_STORAGE_PERMISSION_CODE);
                                            }
                                        },
                                        null
                                );
                            } else {
                                onRuntimePermissionsResultListener.onStoragePermissionsResult(storagePermissions);
                            }
                        } else {
                            onRuntimePermissionsResultListener.onStoragePermissionsResult(storagePermissions);
                            showSnackbarMessage(
                                    findViewById(R.id.ao_fragment_container),
                                    "Storage Permissions Denied!",
                                    true,
                                    getString(R.string.retry),
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            requestContactsPermissions();
                                        }
                                    },
                                    false);
                        }
                    } else {
                        onRuntimePermissionsResultListener.onStoragePermissionsResult(storagePermissions);
                    }
                }
                break;
            case REQUEST_LOCATION_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean locationPermissions = grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (locationPermissions) {
                        onRuntimePermissionsResultListener.onLocationPermissionsResult(locationPermissions);
                    } else {
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                            if (!shouldShowRequestPermissionRationale(ACCESS_FINE_LOCATION)) {
                                showConfirmationDialog(
                                        "Please enable location permissions to proceed!",
                                        getString(R.string.ok),
                                        null,
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivityForResult(intent, REQUEST_LOCATION_PERMISSION_CODE);
                                            }
                                        },
                                        null
                                );
                            } else {
                                onRuntimePermissionsResultListener.onLocationPermissionsResult(locationPermissions);
                            }
                        } else {
                            onRuntimePermissionsResultListener.onLocationPermissionsResult(locationPermissions);
                        }
                    }
                }
                break;

        }
    }

    public void requestSmsPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                READ_SMS,
                RECEIVE_SMS
        }, REQUEST_SMS_PERMISSION_CODE);
    }

    public void requestContactsPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                READ_CONTACTS,
                WRITE_CONTACTS
        }, REQUEST_CONTACTS_PERMISSION_CODE);
    }

    public void requestLocationPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                ACCESS_FINE_LOCATION,
                ACCESS_COARSE_LOCATION
        }, REQUEST_LOCATION_PERMISSION_CODE);
    }

    public void requestStoragePermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
        }, REQUEST_STORAGE_PERMISSION_CODE);
    }

    public boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    Log.d(TAG, "isMyServiceRunning? " + true);
                    return true;
                }
            }
        }
        Log.i(TAG, "isMyServiceRunning? " + false);
        return false;
    }

    public List<User> getAppUserList() {
        return appUserList;
    }

    public void setAppUserList(List<User> list) {
        appUserList = list;
    }

    public List<User> getNonAppUserList() {
        return nonAppUserList;
    }

    public void setNonAppUserList(List<User> list) {
        nonAppUserList = list;
    }

    public List<UserGroup> getUserGroupList() {
        return userGroupList;
    }

    public void setUserGroupList(List<UserGroup> list) {
        userGroupList = list;
    }

    public void clearRoomDatabase() {
        userRepository.clearUserTable();
        userGroupRepository.clearUserGroupTable();
        reminderRepository.clearReminderTable();
        phoneContactRepository.clearPhoneContactTable();
    }

    public void startReminderSchedulingService() {
        Intent intent = new Intent(this, ReminderSchedulingService.class);
        startService(intent);
    }

    public void initLocationService() {
        LocationService locationService = new LocationService();
        locationServiceIntent = new Intent(this, locationService.getClass());
        if (!isMyServiceRunning(locationService.getClass())) {
            startService(locationServiceIntent);
        }
    }

}
