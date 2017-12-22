package com.insyslab.tooz.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.insyslab.tooz.R;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.ui.fragments.BlockedContactsFragment;
import com.insyslab.tooz.ui.fragments.FeedbackFragment;
import com.insyslab.tooz.ui.fragments.HelpFragment;
import com.insyslab.tooz.ui.fragments.ManualContactSyncFragment;
import com.insyslab.tooz.ui.fragments.NotificationSettingsFragment;
import com.insyslab.tooz.ui.fragments.PreferencesFragment;
import com.insyslab.tooz.ui.fragments.PrivacySettingsFragment;
import com.insyslab.tooz.ui.fragments.SettingsFragment;
import com.insyslab.tooz.ui.fragments.TermsPrivPolicyFragment;
import com.insyslab.tooz.ui.fragments.UpdateProfileFragment;
import com.insyslab.tooz.utils.Util;

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
        } else if (fragmentTag.equals(PrivacySettingsFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .add(R.id.as_fragment_container, PrivacySettingsFragment.newInstance((Bundle) bundle), fragmentTag)
                    .addToBackStack(TAG)
                    .commit();
        } else if (fragmentTag.equals(HelpFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .add(R.id.as_fragment_container, HelpFragment.newInstance((Bundle) bundle), fragmentTag)
                    .addToBackStack(TAG)
                    .commit();
        } else if (fragmentTag.equals(TermsPrivPolicyFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .add(R.id.as_fragment_container, TermsPrivPolicyFragment.newInstance((Bundle) bundle), fragmentTag)
                    .addToBackStack(TAG)
                    .commit();
        } else if (fragmentTag.equals(NotificationSettingsFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .add(R.id.as_fragment_container, NotificationSettingsFragment.newInstance((Bundle) bundle), fragmentTag)
                    .addToBackStack(TAG)
                    .commit();
        } else if (fragmentTag.equals(FeedbackFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .add(R.id.as_fragment_container, FeedbackFragment.newInstance((Bundle) bundle), fragmentTag)
                    .addToBackStack(TAG)
                    .commit();
        } else if (fragmentTag.equals(ManualContactSyncFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .add(R.id.as_fragment_container, ManualContactSyncFragment.newInstance((Bundle) bundle), fragmentTag)
                    .addToBackStack(TAG)
                    .commit();
        } else {
            showToastMessage("Some error occurred!", false);
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    public void onEvent(FragmentState fragmentState) {
        Log.d(TAG, fragmentState.getVisibleFragment());
        currentFragment = fragmentState.getVisibleFragment();
        invalidateOptionsMenu();

        if (fragmentState.getVisibleFragment().equals(SettingsFragment.TAG)) {
            updateToolbar("Settings");
            Util.hideSoftKeyboard(this);
        } else if (fragmentState.getVisibleFragment().equals(BlockedContactsFragment.TAG)) {
            updateToolbar("Blocked Contacts");
        } else if (fragmentState.getVisibleFragment().equals(PreferencesFragment.TAG)) {
            updateToolbar("Preferences");
        } else if (fragmentState.getVisibleFragment().equals(UpdateProfileFragment.TAG)) {
            updateToolbar("Update Profile");
        } else if (fragmentState.getVisibleFragment().equals(PrivacySettingsFragment.TAG)) {
            updateToolbar("Privacy Settings");
        } else if (fragmentState.getVisibleFragment().equals(HelpFragment.TAG)) {
            updateToolbar("Help");
        } else if (fragmentState.getVisibleFragment().equals(NotificationSettingsFragment.TAG)) {
            updateToolbar("Notifications");
        } else if (fragmentState.getVisibleFragment().equals(TermsPrivPolicyFragment.TAG)) {
            updateToolbar("Terms and Privacy Policy");
        } else if (fragmentState.getVisibleFragment().equals(FeedbackFragment.TAG)) {
            updateToolbar("Share your Feedback");
        } else if (fragmentState.getVisibleFragment().equals(ManualContactSyncFragment.TAG)) {
            updateToolbar("Sync your contacts");
        }
    }

    private void updateToolbar(String toolbarTitle) {
        getSupportActionBar().setTitle(toolbarTitle);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentFragment.equals(ManualContactSyncFragment.TAG)) {
            getMenuInflater().inflate(R.menu.menu_sync, menu);
        } else if (!currentFragment.equals(SettingsFragment.TAG)
                && !currentFragment.equals(TermsPrivPolicyFragment.TAG)
                && !currentFragment.equals(FeedbackFragment.TAG)
                && !currentFragment.equals(HelpFragment.TAG)) {
            getMenuInflater().inflate(R.menu.menu_actions, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            onToolbarSaveClick();
            return true;
        }

        if (id == R.id.action_sync) {
            onToolbarSyncClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onToolbarSaveClick() {
        switch (currentFragment) {
            case BlockedContactsFragment.TAG:
                BlockedContactsFragment fragment1 = (BlockedContactsFragment) getSupportFragmentManager().findFragmentById(R.id.as_fragment_container);
                fragment1.onSaveClick();
                break;
            case PreferencesFragment.TAG:
                PreferencesFragment fragment2 = (PreferencesFragment) getSupportFragmentManager().findFragmentById(R.id.as_fragment_container);
                fragment2.onSaveClick();
                break;
            case UpdateProfileFragment.TAG:
                UpdateProfileFragment fragment3 = (UpdateProfileFragment) getSupportFragmentManager().findFragmentById(R.id.as_fragment_container);
                fragment3.onSaveClick();
                break;
            default:
                showToastMessage("Some error occurred!", false);
                break;
        }
    }

    private void onToolbarSyncClick() {
        switch (currentFragment) {
            case ManualContactSyncFragment.TAG:
                ManualContactSyncFragment fragment1 = (ManualContactSyncFragment) getSupportFragmentManager().findFragmentById(R.id.as_fragment_container);
                fragment1.onSyncClick();
                break;
            default:
                showToastMessage("Some error occurred!", false);
                break;
        }
    }

    public void closeCurrentFragment() {
        getSupportFragmentManager().popBackStack();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
