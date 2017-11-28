package com.insyslab.tooz.ui.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnRuntimePermissionsResultListener;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.ui.fragments.CreateProfileFragment;
import com.insyslab.tooz.ui.fragments.MobileNumberFragment;
import com.insyslab.tooz.ui.fragments.OtpVerificationFragment;
import com.insyslab.tooz.ui.fragments.SyncContactsFragment;
import com.insyslab.tooz.utils.Util;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static android.Manifest.permission.WRITE_CONTACTS;

public class OnboardingActivity extends BaseActivity {

    public static OnRuntimePermissionsResultListener onRuntimePermissionsResultListener;

    private final String TAG = "Onboarding ==> ";

    private final int REQUEST_SMS_PERMISSION_CODE = 1;
    private final int REQUEST_CONTACTS_PERMISSION_CODE = 2;

    private String currentFragment = null;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

        doubleBackToExitPressedOnce = false;
        getFragmentStatus();
        openThisFragment(MobileNumberFragment.TAG, null);
    }

    public void openThisFragment(String fragmentTag, Object bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentTag.equals(MobileNumberFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.ao_fragment_container, MobileNumberFragment.newInstance(), fragmentTag)
                    .commit();
        } else if (fragmentTag.equals(OtpVerificationFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .add(R.id.ao_fragment_container, OtpVerificationFragment.newInstance((Bundle) bundle), fragmentTag)
                    .addToBackStack(TAG)
                    .commitAllowingStateLoss();
        } else if (fragmentTag.equals(CreateProfileFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .add(R.id.ao_fragment_container, CreateProfileFragment.newInstance((Bundle) bundle), fragmentTag)
                    .addToBackStack(TAG)
                    .commit();
        } else if (fragmentTag.equals(SyncContactsFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .add(R.id.ao_fragment_container, SyncContactsFragment.newInstance((Bundle) bundle), fragmentTag)
                    .addToBackStack(TAG)
                    .commit();
        }
    }

    public void onEvent(FragmentState fragmentState) {
        Log.d(TAG, fragmentState.getVisibleFragment());
        currentFragment = fragmentState.getVisibleFragment();

        if (fragmentState.getVisibleFragment().equals(MobileNumberFragment.TAG)) {

        }
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
                break;

            case REQUEST_CONTACTS_PERMISSION_CODE:
                if (grantResults.length > 0) {
                    boolean smsPermissions = grantResults[0] == PackageManager.PERMISSION_GRANTED
                            && grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (!smsPermissions) {
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
                            onRuntimePermissionsResultListener.onContactsPermissionsResult(smsPermissions);
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
                        onRuntimePermissionsResultListener.onContactsPermissionsResult(smsPermissions);
                    }
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentFragment.equals(SyncContactsFragment.TAG)) {
            if (!Util.verifyPermission(this, READ_CONTACTS) || !Util.verifyPermission(this, WRITE_CONTACTS))
                requestContactsPermissions();
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

    public void proceedToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        if (currentFragment.equals(CreateProfileFragment.TAG)) {
            if (doubleBackToExitPressedOnce) {
                finish();
                return;
            }
            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }
            doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        }

    }
}
