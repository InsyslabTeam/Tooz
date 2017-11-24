package com.insyslab.tooz.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnRuntimePermissionsResultListener;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.ui.fragments.CreateProfileFragment;
import com.insyslab.tooz.ui.fragments.MobileNumberFragment;
import com.insyslab.tooz.ui.fragments.OtpVerificationFragment;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;

public class OnboardingActivity extends BaseActivity {

    public static OnRuntimePermissionsResultListener onRuntimePermissionsResultListener;

    private final String TAG = "Onboarding ==> ";

    private final int REQUEST_SMS_PERMISSION_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);

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
        }
    }

    public void onEvent(FragmentState fragmentState) {
        Log.d(TAG, fragmentState.getVisibleFragment());

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
        }
    }

    public void requestSmsPermissions() {
        ActivityCompat.requestPermissions(this, new String[]{
                READ_SMS,
                RECEIVE_SMS
        }, REQUEST_SMS_PERMISSION_CODE);
    }

    public void proceedToDashboard() {
        Intent intent = new Intent(this, DashboardActivity.class);
        startActivity(intent);
        finish();
    }
}
