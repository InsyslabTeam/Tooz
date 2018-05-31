package com.insyslab.tooz.ui.activities;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.insyslab.tooz.R;
import com.insyslab.tooz.models.PhoneContact;
import com.insyslab.tooz.models.eventbus.FragmentState;
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

import java.util.List;

public class SettingsActivity extends BaseActivity {

    private static final String TAG = SettingsActivity.class.getSimpleName() + " ==>";

    private Toolbar toolbar;

    private String currentFragment = null;
    private List<PhoneContact> syncedPhoneContactsList, nonSyncedPhoneContactsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        initView();
        setUpToolbar();

        fetchSyncedPhoneContatcsFromDb();
        fetchNonSyncedPhoneContatcsFromDb();

        getFragmentStatus();
        openThisFragment(SettingsFragment.TAG, null);
    }

    public void openThisFragment(String fragmentTag, Object bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        switch (fragmentTag) {
            case SettingsFragment.TAG:
                fragmentManager.beginTransaction()
                        .replace(R.id.as_fragment_container, SettingsFragment.newInstance((Bundle) bundle), fragmentTag)
                        .commit();
                break;
            case BlockedContactsFragment.TAG:
                fragmentManager.beginTransaction()
                        .add(R.id.as_fragment_container, BlockedContactsFragment.newInstance((Bundle) bundle), fragmentTag)
                        .addToBackStack(TAG)
                        .commit();
                break;
            case PreferencesFragment.TAG:
                fragmentManager.beginTransaction()
                        .add(R.id.as_fragment_container, PreferencesFragment.newInstance((Bundle) bundle), fragmentTag)
                        .addToBackStack(TAG)
                        .commit();
                break;
            case UpdateProfileFragment.TAG:
                fragmentManager.beginTransaction()
                        .add(R.id.as_fragment_container, UpdateProfileFragment.newInstance((Bundle) bundle), fragmentTag)
                        .addToBackStack(TAG)
                        .commit();
                break;
            case PrivacySettingsFragment.TAG:
                fragmentManager.beginTransaction()
                        .add(R.id.as_fragment_container, PrivacySettingsFragment.newInstance((Bundle) bundle), fragmentTag)
                        .addToBackStack(TAG)
                        .commit();
                break;
            case HelpFragment.TAG:
                fragmentManager.beginTransaction()
                        .add(R.id.as_fragment_container, HelpFragment.newInstance((Bundle) bundle), fragmentTag)
                        .addToBackStack(TAG)
                        .commit();
                break;
            case TermsPrivPolicyFragment.TAG:
                fragmentManager.beginTransaction()
                        .add(R.id.as_fragment_container, TermsPrivPolicyFragment.newInstance((Bundle) bundle), fragmentTag)
                        .addToBackStack(TAG)
                        .commit();
                break;
            case NotificationSettingsFragment.TAG:
                fragmentManager.beginTransaction()
                        .add(R.id.as_fragment_container, NotificationSettingsFragment.newInstance((Bundle) bundle), fragmentTag)
                        .addToBackStack(TAG)
                        .commit();
                break;
            case FeedbackFragment.TAG:
                fragmentManager.beginTransaction()
                        .add(R.id.as_fragment_container, FeedbackFragment.newInstance((Bundle) bundle), fragmentTag)
                        .addToBackStack(TAG)
                        .commit();
                break;
            case ManualContactSyncFragment.TAG:
                fragmentManager.beginTransaction()
                        .add(R.id.as_fragment_container, ManualContactSyncFragment.newInstance((Bundle) bundle), fragmentTag)
                        .addToBackStack(TAG)
                        .commit();
                break;
            default:
                showToastMessage("Some error occurred!", false);
                break;
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setDisplayHomeAsUpEnabled(true);

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

        switch (fragmentState.getVisibleFragment()) {
            case SettingsFragment.TAG:
                updateToolbar("Settings");
                Util.hideSoftKeyboard(this);
                break;
            case BlockedContactsFragment.TAG:
                updateToolbar("Blocked Contacts");
                break;
            case PreferencesFragment.TAG:
                updateToolbar("Preferences");
                break;
            case UpdateProfileFragment.TAG:
                updateToolbar("Update Profile");
                break;
            case PrivacySettingsFragment.TAG:
                updateToolbar("Privacy Settings");
                break;
            case HelpFragment.TAG:
                updateToolbar("Help");
                break;
            case NotificationSettingsFragment.TAG:
                updateToolbar("Notifications");
                break;
            case TermsPrivPolicyFragment.TAG:
                updateToolbar("Terms and Privacy Policy");
                break;
            case FeedbackFragment.TAG:
                updateToolbar("Share your Feedback");
                break;
            case ManualContactSyncFragment.TAG:
                updateToolbar("Sync your contacts");
                break;
        }
    }

    private void updateToolbar(String toolbarTitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(toolbarTitle);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentFragment.equals(ManualContactSyncFragment.TAG)) {
            getMenuInflater().inflate(R.menu.menu_sync, menu);
        } else if (currentFragment.equals(FeedbackFragment.TAG)) {
            getMenuInflater().inflate(R.menu.menu_submit, menu);
        } else if (!currentFragment.equals(SettingsFragment.TAG)
                && !currentFragment.equals(TermsPrivPolicyFragment.TAG)
                && !currentFragment.equals(FeedbackFragment.TAG)
                && !currentFragment.equals(HelpFragment.TAG)) {
            getMenuInflater().inflate(R.menu.menu_save, menu);
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

        if (id == R.id.action_submit) {
            onToolbarSaveClick();
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
            case FeedbackFragment.TAG:
                FeedbackFragment fragment4 = (FeedbackFragment) getSupportFragmentManager().findFragmentById(R.id.as_fragment_container);
                fragment4.onSaveClick();
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

    private void fetchSyncedPhoneContatcsFromDb() {
        phoneContactRepository.getSyncedPhoneContacts().observe(this, new Observer<List<PhoneContact>>() {
            @Override
            public void onChanged(@Nullable List<PhoneContact> list) {
                syncedPhoneContactsList = list;
                updateSyncedPhoneContacts();
            }
        });
    }

    private void updateSyncedPhoneContacts() {
        try {
            ManualContactSyncFragment fragment = (ManualContactSyncFragment) getSupportFragmentManager().findFragmentById(R.id.as_fragment_container);
            if (fragment != null) fragment.updateSyncedContactsInRv(syncedPhoneContactsList);
        } catch (ClassCastException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR: updateSyncedPhoneContacts - " + e.getMessage());
        }
    }

    public List<PhoneContact> getSyncedPhoneContactsList() {
        return syncedPhoneContactsList;
    }

    private void fetchNonSyncedPhoneContatcsFromDb() {
        phoneContactRepository.getNonSyncedPhoneContacts().observe(this, new Observer<List<PhoneContact>>() {
            @Override
            public void onChanged(@Nullable List<PhoneContact> list) {
                nonSyncedPhoneContactsList = list;
                updateNonSyncedPhoneContacts();
            }
        });
    }

    private void updateNonSyncedPhoneContacts() {
        try {
            ManualContactSyncFragment fragment = (ManualContactSyncFragment) getSupportFragmentManager().findFragmentById(R.id.as_fragment_container);
            if (fragment != null) fragment.updateNonSyncedContactsInRv(nonSyncedPhoneContactsList);
        } catch (ClassCastException e) {
            e.printStackTrace();
            Log.d(TAG, "ERROR: updateNonSyncedPhoneContacts - " + e.getMessage());
        }
    }

    public List<PhoneContact> getNonSyncedPhoneContactsList() {
        return nonSyncedPhoneContactsList;
    }

    public void initLocalDbPhoneContactsUpdate(List<PhoneContact> phoneContactList) {
        phoneContactRepository.insertPhoneContacts(phoneContactList);
    }
}
