package com.insyslab.tooz.ui.activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnRuntimePermissionsResultListener;

import de.greenrobot.event.EventBus;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

/**
 * Created by TaNMay on 12/10/17.
 */

public abstract class BaseActivity extends AppCompatActivity {

    public static OnRuntimePermissionsResultListener onRuntimePermissionsResultListener;

    private final int REQUEST_SMS_PERMISSION_CODE = 1;
    private final int REQUEST_CONTACTS_PERMISSION_CODE = 2;
    private final int REQUEST_STORAGE_PERMISSION_CODE = 3;

    protected ProgressDialog mProgressDialog = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected void getFragmentStatus() {
        EventBus.getDefault().registerSticky(this);
    }

    protected void showToastMessage(String message, boolean isLong) {
        Toast toast = null;

        if (isLong) toast = toast.makeText(this, message, Toast.LENGTH_LONG);
        else toast = toast.makeText(this, message, Toast.LENGTH_SHORT);

        toast.show();
    }

    protected void showSnackbarMessage(View view, String message, boolean isLong, String action,
                                       View.OnClickListener onClickListener, boolean isDismissed) {
        Snackbar snackbar = null;

        if (isLong) snackbar = snackbar.make(view, message, Snackbar.LENGTH_LONG);
        else if (!isLong) snackbar = snackbar.make(view, message, Snackbar.LENGTH_SHORT);

        if (action != null && isDismissed == true) {
            final Snackbar finalSnackbar = snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE);
            snackbar = snackbar.setAction(action, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    finalSnackbar.dismiss();
                }
            });
        } else if (action != null)
            snackbar = snackbar.make(view, message, Snackbar.LENGTH_INDEFINITE).setAction(action, onClickListener);

        snackbar.show();
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
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
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

    public void requestStoragePermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                READ_EXTERNAL_STORAGE,
                WRITE_EXTERNAL_STORAGE
        }, REQUEST_STORAGE_PERMISSION_CODE);
    }
}
