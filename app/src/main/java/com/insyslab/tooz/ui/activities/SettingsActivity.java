package com.insyslab.tooz.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.insyslab.tooz.R;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.ui.fragments.BlockedContactsFragment;
import com.insyslab.tooz.ui.fragments.HelpFragment;
import com.insyslab.tooz.ui.fragments.PreferencesFragment;
import com.insyslab.tooz.ui.fragments.PrivacyFragment;
import com.insyslab.tooz.ui.fragments.SettingsFragment;
import com.insyslab.tooz.ui.fragments.UpdateProfileFragment;

public class SettingsActivity extends BaseActivity {

    private static final String TAG = "Settings ==> ";

    private Toolbar toolbar;

    private String currentFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
        setUpToolbar();
        setUpActions();

        getFragmentStatus();
        openThisFragment(SettingsFragment.TAG, null);
    }

    public void openThisFragment(String fragmentTag, Object bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentTag.equals(SettingsFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.as_fragment_container, SettingsFragment.newInstance((Bundle) bundle), fragmentTag)
                    .commit();
        } else if (fragmentTag.equals(BlockedContactsFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .add(R.id.as_fragment_container, BlockedContactsFragment.newInstance((Bundle) bundle), fragmentTag)
                    .addToBackStack(TAG)
                    .commit();
        } else if (fragmentTag.equals(PreferencesFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .add(R.id.as_fragment_container, PreferencesFragment.newInstance((Bundle) bundle), fragmentTag)
                    .addToBackStack(TAG)
                    .commit();
        } else if (fragmentTag.equals(UpdateProfileFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .add(R.id.as_fragment_container, UpdateProfileFragment.newInstance((Bundle) bundle), fragmentTag)
                    .addToBackStack(TAG)
                    .commit();
        } else if (fragmentTag.equals(PrivacyFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .add(R.id.as_fragment_container, PrivacyFragment.newInstance((Bundle) bundle), fragmentTag)
                    .addToBackStack(TAG)
                    .commit();
        } else if (fragmentTag.equals(HelpFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .add(R.id.as_fragment_container, HelpFragment.newInstance((Bundle) bundle), fragmentTag)
                    .addToBackStack(TAG)
                    .commit();
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setUpActions() {

    }

    public void onEvent(FragmentState fragmentState) {
        Log.d(TAG, fragmentState.getVisibleFragment());
        currentFragment = fragmentState.getVisibleFragment();

        if (fragmentState.getVisibleFragment().equals(SettingsFragment.TAG)) {
            updateToolbar("Settings");
        } else if (fragmentState.getVisibleFragment().equals(BlockedContactsFragment.TAG)) {
            updateToolbar("Blocked Contacts");
        } else if (fragmentState.getVisibleFragment().equals(PreferencesFragment.TAG)) {
            updateToolbar("Preferences");
        } else if (fragmentState.getVisibleFragment().equals(UpdateProfileFragment.TAG)) {
            updateToolbar("Update Profile");
        } else if (fragmentState.getVisibleFragment().equals(PrivacyFragment.TAG)) {
            updateToolbar("Privacy");
        } else if (fragmentState.getVisibleFragment().equals(HelpFragment.TAG)) {
            updateToolbar("Help");
        }
    }

    private void updateToolbar(String toolbarTitle) {
        getSupportActionBar().setTitle(toolbarTitle);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);

    }
}
