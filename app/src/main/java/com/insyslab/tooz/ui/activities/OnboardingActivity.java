package com.insyslab.tooz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.widget.Toast;

import com.insyslab.tooz.R;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.ui.fragments.CreateProfileFragment;
import com.insyslab.tooz.ui.fragments.MobileNumberFragment;
import com.insyslab.tooz.ui.fragments.OtpVerificationFragment;
import com.insyslab.tooz.ui.fragments.SyncContactsFragment;
import com.insyslab.tooz.utils.Util;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;

public class OnboardingActivity extends BaseActivity {

    private final String TAG = "Onboarding ==> ";

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
    protected void onResume() {
        super.onResume();
        if (currentFragment.equals(SyncContactsFragment.TAG)) {
            if (!Util.verifyPermission(this, READ_CONTACTS) || !Util.verifyPermission(this, WRITE_CONTACTS))
                requestContactsPermissions();
        }
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
