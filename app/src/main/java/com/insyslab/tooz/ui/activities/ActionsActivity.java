package com.insyslab.tooz.ui.activities;

import android.arch.lifecycle.Observer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnReminderFetchedListener;
import com.insyslab.tooz.interfaces.OnUserFetchedListener;
import com.insyslab.tooz.models.PhoneContact;
import com.insyslab.tooz.models.Reminder;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.UserGroup;
import com.insyslab.tooz.models.eventbus.FragmentState;
import com.insyslab.tooz.ui.fragments.AddContactFragment;
import com.insyslab.tooz.ui.fragments.CreateGroupFragment;
import com.insyslab.tooz.ui.fragments.EditReminderFragment;
import com.insyslab.tooz.ui.fragments.LocationSelectorFragment;
import com.insyslab.tooz.ui.fragments.SelectContactsFragment;
import com.insyslab.tooz.ui.fragments.SetReminderFragment;

import java.util.ArrayList;
import java.util.List;

import static com.insyslab.tooz.utils.AppConstants.KEY_GET_REMINDER_ID;
import static com.insyslab.tooz.utils.AppConstants.KEY_GET_SELECTED_CONTACT_ID;
import static com.insyslab.tooz.utils.AppConstants.KEY_GET_SELECTED_GROUP_ID;
import static com.insyslab.tooz.utils.AppConstants.KEY_SELECTED_CONTACT_BUNDLE;
import static com.insyslab.tooz.utils.AppConstants.KEY_SET_REMINDER_TYPE;
import static com.insyslab.tooz.utils.AppConstants.KEY_TO_ACTIONS;
import static com.insyslab.tooz.utils.AppConstants.VAL_SEND_REMINDER;
import static com.insyslab.tooz.utils.AppConstants.VAL_SET_PERSONAL_REMINDER;

public class ActionsActivity extends BaseActivity
        implements LocationSelectorFragment.OnLocationSelectedListener,
        SelectContactsFragment.OnContactsSelectedListener {

    private static final String TAG = ActionsActivity.class.getSimpleName() + " ==>";

    public static OnReminderFetchedListener onReminderFetchedListener;
    public static OnUserFetchedListener onUserFetchedListener;

    private Toolbar toolbar;

    private String currentFragment = null;
    private String currentDetailFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions);

        String toFragment = getIntent().getStringExtra(KEY_TO_ACTIONS);
        String fragmentType = getIntent().getStringExtra(KEY_SET_REMINDER_TYPE);
        String reminderId = getIntent().getStringExtra(KEY_GET_REMINDER_ID);
        Bundle contactBundle = getIntent().getBundleExtra(KEY_SELECTED_CONTACT_BUNDLE);

        initView();
        setUpToolbar();

        fetchAppUserContactsFromDb();
        fetchUserGroupListFromDb();

        setUpActions();

        getFragmentStatus();
        if (toFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_SET_REMINDER_TYPE, fragmentType);
            if (contactBundle != null) {
                bundle.putString(KEY_GET_SELECTED_CONTACT_ID, contactBundle.getString(KEY_GET_SELECTED_CONTACT_ID));
                bundle.putString(KEY_GET_SELECTED_GROUP_ID, contactBundle.getString(KEY_GET_SELECTED_GROUP_ID));
            }
            if (reminderId != null) bundle.putString(KEY_GET_REMINDER_ID, reminderId);
            openThisFragment(toFragment, bundle);
        } else {
            showToastMessage("Some error occurred!", false);
            finish();
        }
    }

    public void openThisFragment(String fragmentTag, Object bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentTag.equals(AddContactFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.aa_fragment_container, AddContactFragment.newInstance((Bundle) bundle), fragmentTag)
                    .commit();

        } else if (fragmentTag.equals(CreateGroupFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.aa_fragment_container, CreateGroupFragment.newInstance((Bundle) bundle), fragmentTag)
                    .commit();

        } else if (fragmentTag.equals(SetReminderFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.aa_fragment_container, SetReminderFragment.newInstance((Bundle) bundle), fragmentTag)
                    .commit();

        } else if (fragmentTag.equals(LocationSelectorFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .add(R.id.aa_fragment_container, LocationSelectorFragment.newInstance((Bundle) bundle), fragmentTag)
                    .addToBackStack(TAG)
                    .commit();

        } else if (fragmentTag.equals(SelectContactsFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .add(R.id.aa_fragment_container, SelectContactsFragment.newInstance((Bundle) bundle), fragmentTag)
                    .addToBackStack(TAG)
                    .commit();

        } else if (fragmentTag.equals(EditReminderFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.aa_fragment_container, EditReminderFragment.newInstance((Bundle) bundle), fragmentTag)
                    .commit();

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

    private void setUpActions() {

    }

    public void onEvent(FragmentState fragmentState) {
//        Log.d(TAG, fragmentState.getVisibleFragment());
        currentFragment = fragmentState.getVisibleFragment();
        currentDetailFragment = fragmentState.getFragmentDetailedName();
        invalidateOptionsMenu();

        if (fragmentState.getVisibleFragment().equals(AddContactFragment.TAG)) {
            updateToolbar("Add Contact");
        } else if (fragmentState.getVisibleFragment().equals(CreateGroupFragment.TAG)) {
            updateToolbar("Create a Group");
        } else if (fragmentState.getVisibleFragment().equals(SetReminderFragment.TAG)
                && fragmentState.getFragmentDetailedName().equals(VAL_SET_PERSONAL_REMINDER)) {
            updateToolbar("Set Personal Reminder");
        } else if (fragmentState.getVisibleFragment().equals(SetReminderFragment.TAG)
                && fragmentState.getFragmentDetailedName().equals(VAL_SEND_REMINDER)) {
            updateToolbar("Send Reminder");
        } else if (fragmentState.getVisibleFragment().equals(LocationSelectorFragment.TAG)) {
            updateToolbar("Select Location");
        } else if (fragmentState.getVisibleFragment().equals(SelectContactsFragment.TAG)) {
            updateToolbar("Select Contacts");
        }
    }

    private void updateToolbar(String toolbarTitle) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) actionBar.setTitle(toolbarTitle);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.fade_out);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (currentDetailFragment != null && currentDetailFragment.equals(VAL_SEND_REMINDER))
            getMenuInflater().inflate(R.menu.menu_send, menu);
        else if (currentFragment != null && currentFragment.equals(SelectContactsFragment.TAG))
            getMenuInflater().inflate(R.menu.menu_done, menu);
        else
            getMenuInflater().inflate(R.menu.menu_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save || id == R.id.action_send || id == R.id.action_done) {
            onToolbarSaveClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onToolbarSaveClick() {
        if (currentFragment.equals(AddContactFragment.TAG)) {
            AddContactFragment fragment1 = (AddContactFragment) getSupportFragmentManager().findFragmentById(R.id.aa_fragment_container);
            fragment1.onSaveClick();

        } else if (currentFragment.equals(SetReminderFragment.TAG)) {
            SetReminderFragment fragment2 = (SetReminderFragment) getSupportFragmentManager().findFragmentById(R.id.aa_fragment_container);
            fragment2.onSaveClick();

        } else if (currentFragment.equals(LocationSelectorFragment.TAG)) {
            LocationSelectorFragment fragment3 = (LocationSelectorFragment) getSupportFragmentManager().findFragmentById(R.id.aa_fragment_container);
            fragment3.onSaveClick();

        } else if (currentFragment.equals(CreateGroupFragment.TAG)) {
            CreateGroupFragment fragment4 = (CreateGroupFragment) getSupportFragmentManager().findFragmentById(R.id.aa_fragment_container);
            fragment4.onSaveClick();

        } else if (currentFragment.equals(SelectContactsFragment.TAG)) {
            SelectContactsFragment fragment5 = (SelectContactsFragment) getSupportFragmentManager().findFragmentById(R.id.aa_fragment_container);
            fragment5.onSaveClick();

        } else if (currentFragment.equals(EditReminderFragment.TAG)) {
            EditReminderFragment fragment6 = (EditReminderFragment) getSupportFragmentManager().findFragmentById(R.id.aa_fragment_container);
            fragment6.onSaveClick();

        }
    }

    @Override
    public void onLocationSelected(LatLng latLng, String address) {
        SetReminderFragment fragment = (SetReminderFragment) getSupportFragmentManager().findFragmentById(R.id.aa_fragment_container);
        fragment.onLocationSet(latLng, address);
    }

    @Override
    public void onContactsSelected(List<User> contactItemList, String from) {
        if (from.equals(SetReminderFragment.TAG)) {
            SetReminderFragment fragment = (SetReminderFragment) getSupportFragmentManager().findFragmentById(R.id.aa_fragment_container);
            fragment.onMembersSelected(contactItemList);
        } else if (from.equals(CreateGroupFragment.TAG)) {
            CreateGroupFragment fragment = (CreateGroupFragment) getSupportFragmentManager().findFragmentById(R.id.aa_fragment_container);
            fragment.onMembersSelected(contactItemList);
        } else if (from.equals(EditReminderFragment.TAG)) {
            EditReminderFragment fragment = (EditReminderFragment) getSupportFragmentManager().findFragmentById(R.id.aa_fragment_container);
            fragment.onMembersSelected(contactItemList);
        }
    }

    public void fetchAppUserContactsFromDb() {
        setAppUserList(new ArrayList<User>());
        userRepository.getAppUserContacts().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> list) {
                setAppUserList(list);
            }
        });
    }

    public void fetchUserGroupListFromDb() {
        setUserGroupList(new ArrayList<UserGroup>());
        userGroupRepository.getAllUserGroups().observe(this, new Observer<List<UserGroup>>() {
            @Override
            public void onChanged(@Nullable List<UserGroup> list) {
                setUserGroupList(list);
            }
        });
    }

    public void addContactToLocalDb(PhoneContact phoneContact) {
        phoneContactRepository.insertPhoneContact(phoneContact);
    }

    public void getReminderFromId(final String reminderId) {
        reminderRepository.getUpcomingReminderFromId(reminderId).observe(this, new Observer<Reminder>() {
            @Override
            public void onChanged(@Nullable Reminder reminder) {
                onReminderFetchedListener.onReminderFetched(reminder);
            }
        });
    }

    public void getUserFromId(String selectedUserId) {
        userRepository.getUserFromId(selectedUserId).observe(this, new Observer<User>() {
            @Override
            public void onChanged(@Nullable User user) {
                onUserFetchedListener.onUserFetched(user);
            }
        });
    }

    public void getGroupFromId(String selectedGroupId) {
        userGroupRepository.getGroupFromId(selectedGroupId).observe(this, new Observer<UserGroup>() {
            @Override
            public void onChanged(@Nullable UserGroup userGroup) {
                onUserFetchedListener.onGroupFetched(userGroup);
            }
        });
    }
}
