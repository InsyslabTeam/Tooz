package com.insyslab.tooz.ui.activities;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.reflect.TypeToken;
import com.insyslab.tooz.R;
import com.insyslab.tooz.models.PhoneContact;
import com.insyslab.tooz.models.Reminder;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.UserGroup;
import com.insyslab.tooz.models.eventbus.ContactAdded;
import com.insyslab.tooz.models.eventbus.ContactSyncUpdate;
import com.insyslab.tooz.models.eventbus.FragmentState;
import com.insyslab.tooz.models.eventbus.ReminderCreated;
import com.insyslab.tooz.models.requests.ContactSyncRequest;
import com.insyslab.tooz.models.requests.Contact_;
import com.insyslab.tooz.models.responses.ContactSyncResponse;
import com.insyslab.tooz.models.responses.Error;
import com.insyslab.tooz.models.responses.GetContactsResponse;
import com.insyslab.tooz.models.responses.GetDeleteReminderResponse;
import com.insyslab.tooz.restclient.BaseResponseInterface;
import com.insyslab.tooz.restclient.GenericDataHandler;
import com.insyslab.tooz.restclient.RequestBuilder;
import com.insyslab.tooz.ui.fragments.AddContactFragment;
import com.insyslab.tooz.ui.fragments.AllContactsFragment;
import com.insyslab.tooz.ui.fragments.CreateGroupFragment;
import com.insyslab.tooz.ui.fragments.PastRemindersFragment;
import com.insyslab.tooz.ui.fragments.SetReminderFragment;
import com.insyslab.tooz.ui.fragments.UpcomingRemindersFragment;
import com.insyslab.tooz.utils.LocalStorage;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static com.insyslab.tooz.utils.AppConstants.KEY_SELECTED_CONTACT_BUNDLE;
import static com.insyslab.tooz.utils.AppConstants.KEY_SET_REMINDER_TYPE;
import static com.insyslab.tooz.utils.AppConstants.KEY_TO_ACTIONS;
import static com.insyslab.tooz.utils.AppConstants.VAL_SEND_REMINDER;
import static com.insyslab.tooz.utils.AppConstants.VAL_SET_PERSONAL_REMINDER;
import static com.insyslab.tooz.utils.ConstantClass.CONTACTS_SYNC_REQUEST_URL;
import static com.insyslab.tooz.utils.ConstantClass.DELETE_REMINDER_REQUEST_URL;
import static com.insyslab.tooz.utils.ConstantClass.GET_ALL_REMINDERS_REQUEST_URL;
import static com.insyslab.tooz.utils.ConstantClass.GET_CONTACTS_REQUEST_URL;
import static com.insyslab.tooz.utils.ConstantClass.GET_MY_GROUPS_REQUEST_URL;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_005;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_006;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_011;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_012;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_016;

public class DashboardActivity extends BaseActivity implements BaseResponseInterface {

    private static final String TAG = "Dashboard ==> ";

    private Toolbar toolbar;
    private RelativeLayout upcomingTab, pastTab, allContactsTab;
    private TextView tvUpcoming, tvPast, tvAllContacts;
    private FloatingActionButton fabAddContact, fabCreateGroup, fabPersonalReminder, fabSendReminder;
    private FloatingActionMenu floatingActionMenu;

    private String currentFragment = null, reminderCreationType = null;
    private boolean doubleBackToExitPressedOnce;
    private List<Reminder> upcomingRemindersList = new ArrayList<>(), pastRemindersList = new ArrayList<>();
    private Integer responseCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        responseCount = 0;

        initLocationService();

        initView();
        setUpToolbar();

        fetchUpcomingRemindersFromDb();
        fetchPastRemindersFromDb();
        fetchAppUserContactsFromDb();
        fetchNonAppUserContactsFromDb();

        setUpActions();

        doubleBackToExitPressedOnce = false;
        getFragmentStatus();

        if (LocalStorage.getInstance(this).isFirstLogin()) {
            initializeUserData();
        } else {
            initGetContactsRequest();
            initGetAllRemindersRequest();
            openThisFragment(UpcomingRemindersFragment.TAG, null);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initializeUserData() {
        showProgressDialog(getString(R.string.loading));

        initGetContactsRequest();
        initGetAllRemindersRequest();
    }

    private void initGetAllRemindersRequest() {
        String requestUrl = GET_ALL_REMINDERS_REQUEST_URL;
        Type responseType = new TypeToken<List<Reminder>>() {
        }.getType();

        GenericDataHandler req2GenericDataHandler = new GenericDataHandler(this, this, REQUEST_TYPE_011);
        req2GenericDataHandler.jsonArrayRequest(requestUrl, responseType);
    }

    private void initGetContactsRequest() {
        String requestUrl = GET_CONTACTS_REQUEST_URL;

        GenericDataHandler req1GenericDataHandler = new GenericDataHandler(this, this, REQUEST_TYPE_006);
        req1GenericDataHandler.jsonObjectRequest(null, requestUrl, Request.Method.GET, GetContactsResponse.class);

        initGetContactGroupsRequest();
    }

    private void initGetContactGroupsRequest() {
        String requestUrl = GET_MY_GROUPS_REQUEST_URL;
        Type responseType = new TypeToken<List<UserGroup>>() {
        }.getType();

        GenericDataHandler req4GenericDataHandler = new GenericDataHandler(this, this, REQUEST_TYPE_016);
        req4GenericDataHandler.jsonArrayRequest(requestUrl, responseType);
    }

    public void openThisFragment(String fragmentTag, Object bundle) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragmentTag.equals(UpcomingRemindersFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.ad_fragment_container, UpcomingRemindersFragment.newInstance((Bundle) bundle), fragmentTag)
                    .commit();
        } else if (fragmentTag.equals(PastRemindersFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.ad_fragment_container, PastRemindersFragment.newInstance((Bundle) bundle), fragmentTag)
                    .commit();
        } else if (fragmentTag.equals(AllContactsFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.ad_fragment_container, AllContactsFragment.newInstance((Bundle) bundle), fragmentTag)
                    .commit();
        }
    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("  " + getString(R.string.app_name));

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setIcon(R.drawable.ic_toolbar_icon);
    }

    private void setUpActions() {
        upcomingTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onUpcomingTabClick();
            }
        });

        pastTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onPastTabClick();
            }
        });

        allContactsTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAllContactsTabClick();
            }
        });

        fabAddContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAddContactClick();
            }
        });

        fabCreateGroup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onCreateGroupClick();
            }
        });

        fabPersonalReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSetPersonalReminderClick();
            }
        });

        fabSendReminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSendReminderClick();
            }
        });
    }

    private void onCreateGroupClick() {
        floatingActionMenu.close(true);
        openActionsActivity(CreateGroupFragment.TAG, null, null);
    }

    private void onSendReminderClick() {
        floatingActionMenu.close(true);
        openActionsActivity(SetReminderFragment.TAG, VAL_SEND_REMINDER, null);
    }

    private void onSetPersonalReminderClick() {
        floatingActionMenu.close(true);
        openActionsActivity(SetReminderFragment.TAG, VAL_SET_PERSONAL_REMINDER, null);
    }

    private void onAddContactClick() {
        floatingActionMenu.close(true);
        openActionsActivity(AddContactFragment.TAG, null, null);
    }

    public void openActionsActivity(String fragmentTag, String fragmentType, Bundle bundle) {
        Intent intent = new Intent(this, ActionsActivity.class);
        intent.putExtra(KEY_TO_ACTIONS, fragmentTag);
        intent.putExtra(KEY_SET_REMINDER_TYPE, fragmentType);
        intent.putExtra(KEY_SELECTED_CONTACT_BUNDLE, bundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_from_bottom, 0);
    }

    private void onToolbarSettingsClick() {
        openSettingsActivity();
    }

    private void openSettingsActivity() {
        floatingActionMenu.close(true);
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }

    public void onEvent(FragmentState fragmentState) {
        Log.d(TAG, fragmentState.getVisibleFragment());
        currentFragment = fragmentState.getVisibleFragment();

        if (fragmentState.getVisibleFragment().equals(UpcomingRemindersFragment.TAG)) {
            updateToolbar("  " + "Upcoming Reminders");
            updateFooter(0);
        } else if (fragmentState.getVisibleFragment().equals(PastRemindersFragment.TAG)) {
            updateToolbar("  " + "Past Reminders");
            updateFooter(1);
        } else if (fragmentState.getVisibleFragment().equals(AllContactsFragment.TAG)) {
            updateToolbar("  " + "All Contacts");
            updateFooter(2);
        }
    }

    public void onEvent(ContactSyncUpdate contactSyncUpdate) {
        Log.d(TAG, "Contact Update: " + contactSyncUpdate.isContactSynced());

        if (contactSyncUpdate.isContactSynced()) {
            initGetContactsRequest();
        }
    }

    public void onEvent(ContactAdded contactAdded) {
        Log.d(TAG, "Contact Added: " + contactAdded.isContactAdded());

        if (contactAdded.isContactAdded()) {
            initContactSyncProcess();
        }
    }

    public void onEvent(ReminderCreated reminderCreated) {
        Log.d(TAG, "ReminderCreated: " + reminderCreated.isReminderCreated());

        if (reminderCreated.isReminderCreated()) {
            initGetAllRemindersRequest();
            reminderCreationType = reminderCreated.isPersonalReminder() ? VAL_SET_PERSONAL_REMINDER : VAL_SEND_REMINDER;
        }
    }

    private void initContactSyncProcess() {
        phoneContactRepository.getSyncedPhoneContacts().observe(this, new Observer<List<PhoneContact>>() {
            @Override
            public void onChanged(@Nullable List<PhoneContact> list) {
                if (list != null && list.size() > 0) {
                    proceedToContactSync(list);
                }
            }
        });
    }

    private void proceedToContactSync(List<PhoneContact> list) {
        ContactSyncRequest contactSyncRequest = new ContactSyncRequest();
        contactSyncRequest.setContacts(getListOfSelectedContacts(list));
        initContactSyncRequest(contactSyncRequest);
    }

    private List<Contact_> getListOfSelectedContacts(List<PhoneContact> list) {
        List<Contact_> contact_s = new ArrayList<>();
        for (int i = 0; i < list.size(); i++) {
            Contact_ contact_ = new Contact_();
            contact_.setName(list.get(i).getName());
            contact_.setMobile(list.get(i).getPhoneNumber());
            contact_s.add(contact_);
        }
        return contact_s;
    }

    private void initContactSyncRequest(ContactSyncRequest contactSyncRequest) {
        showProgressDialog(getString(R.string.loading));

        String requestUrl = CONTACTS_SYNC_REQUEST_URL;
        JSONObject requestObject = new RequestBuilder().getContactSyncRequestPayload(contactSyncRequest);

        if (requestObject != null) {
            GenericDataHandler reqGenericDataHandler = new GenericDataHandler(this, this, REQUEST_TYPE_005);
            reqGenericDataHandler.jsonObjectRequest(requestObject, requestUrl, Request.Method.POST, ContactSyncResponse.class);
        }
    }

    private void updateFooter(int selectedTab) {
        switch (selectedTab) {
            case 0:
                tvUpcoming.setTextColor(ContextCompat.getColor(this, R.color.black));
                tvPast.setTextColor(ContextCompat.getColor(this, R.color.dashboard_footer_deafult_tab_title));
                tvAllContacts.setTextColor(ContextCompat.getColor(this, R.color.dashboard_footer_deafult_tab_title));
                break;
            case 1:
                tvPast.setTextColor(ContextCompat.getColor(this, R.color.black));
                tvUpcoming.setTextColor(ContextCompat.getColor(this, R.color.dashboard_footer_deafult_tab_title));
                tvAllContacts.setTextColor(ContextCompat.getColor(this, R.color.dashboard_footer_deafult_tab_title));
                break;
            case 2:
                tvAllContacts.setTextColor(ContextCompat.getColor(this, R.color.black));
                tvUpcoming.setTextColor(ContextCompat.getColor(this, R.color.dashboard_footer_deafult_tab_title));
                tvPast.setTextColor(ContextCompat.getColor(this, R.color.dashboard_footer_deafult_tab_title));
                break;
            default:
                break;
        }
    }

    private void updateToolbar(String toolbarTitle) {
        getSupportActionBar().setTitle(toolbarTitle);
    }

    private void onAllContactsTabClick() {
        floatingActionMenu.close(true);
        openThisFragment(AllContactsFragment.TAG, null);
    }

    private void onPastTabClick() {
        floatingActionMenu.close(true);
        openThisFragment(PastRemindersFragment.TAG, null);
    }

    private void onUpcomingTabClick() {
        floatingActionMenu.close(true);
        openThisFragment(UpcomingRemindersFragment.TAG, null);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);

        upcomingTab = findViewById(R.id.ad_upcoming);
        pastTab = findViewById(R.id.ad_past);
        allContactsTab = findViewById(R.id.ad_all_contacts);

        tvUpcoming = findViewById(R.id.ad_upcoming_title);
        tvPast = findViewById(R.id.ad_past_title);
        tvAllContacts = findViewById(R.id.ad_all_contacts_title);

        floatingActionMenu = findViewById(R.id.fam);
        fabAddContact = findViewById(R.id.fab_add_contact);
        fabCreateGroup = findViewById(R.id.fab_create_group);
        fabPersonalReminder = findViewById(R.id.fab_personal_reminder);
        fabSendReminder = findViewById(R.id.fab_send_reminder);
    }

    @Override
    public void onBackPressed() {
        if (floatingActionMenu.isOpened()) floatingActionMenu.close(true);
        else {
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            onToolbarSettingsClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        stopService(locationServiceIntent);
        Log.i(TAG, "onDestroy!");
        super.onDestroy();

    }

    @Override
    public void onResponse(Object success, Object error, final int requestCode) {
        ++responseCount;
        if (error == null) {
            switch (requestCode) {
                case REQUEST_TYPE_005:
                    onContactSyncResponse((ContactSyncResponse) success);
                    break;
                case REQUEST_TYPE_006:
                    onGetContactsResponse((GetContactsResponse) success);
                    break;
                case REQUEST_TYPE_011:
                    onGetAllRemindersResponse((List<Reminder>) success);
                    break;
                case REQUEST_TYPE_012:
                    onGetDeleteReminderResponse((GetDeleteReminderResponse) success);
                    break;
                case REQUEST_TYPE_016:
                    onGetMyGroupsResponse((List<UserGroup>) success);
                    break;
                default:
                    showToastMessage("ERROR " + requestCode + "!", false);
                    break;
            }
        } else {
            Error customError = (Error) error;
            Log.d(TAG, "Error: " + customError.getMessage() + " -- " + customError.getStatus() + " -- ");
            if (customError.getStatus() == 000) {
                hideProgressDialog();
                showNetworkErrorSnackbar(findViewById(R.id.ad_fragment_container), getString(R.string.error_no_internet), getString(R.string.retry),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (requestCode) {
                                    case REQUEST_TYPE_006:
                                        initGetContactsRequest();
                                        break;
                                    case REQUEST_TYPE_011:
                                        initGetAllRemindersRequest();
                                        break;
                                    default:
                                        showToastMessage(getString(R.string.error_unknown), false);
                                }
                            }
                        });
            } else {
                showSnackbarMessage(findViewById(R.id.ad_fragment_container), customError.getMessage(), true, getString(R.string.ok), null, true);
            }
        }
    }

    private void onGetMyGroupsResponse(List<UserGroup> success) {
        initLocalDbGroupsUpdate(success);
        if (responseCount > 2) resumeNormalApp();
    }

    private void initLocalDbGroupsUpdate(List<UserGroup> userGroups) {
        userGroupRepository.insertGroups(userGroups);
    }

    private void onGetDeleteReminderResponse(GetDeleteReminderResponse success) {
        if (success.getStatus() == 200) {
            initLocalDbReminderDeletion(success.getId());
        } else {
            if (success != null && success.getMessage() != null && !success.getMessage().isEmpty())
                showToastMessage(success.getMessage(), true);
        }
    }

    private void initLocalDbReminderDeletion(final String id) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reminderRepository.deleteReminder(id);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                showToastMessage("Reminder deleted successfully!", true);
            }
        }.execute();
    }

    private void onContactSyncResponse(ContactSyncResponse success) {
        if (success.getStatus() == 200) {
            initGetContactsRequest();
        } else
            showSnackbarMessage(findViewById(R.id.ad_fragment_container), success.getMessage(), true, getString(R.string.ok), null, true);
    }

    private void onGetAllRemindersResponse(List<Reminder> success) {
        initLocalDbRemindersUpdate(success);
        if (responseCount > 2) resumeNormalApp();
    }

    private void onGetContactsResponse(GetContactsResponse success) {
        initLocalDbContactsUpdate(success.getAppUser());
        initLocalDbContactsUpdate(success.getNonAppUser());
        if (responseCount > 2) resumeNormalApp();
    }

    private void initLocalDbRemindersUpdate(final List<Reminder> reminderList) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                reminderRepository.insertReminders(reminderList);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                startReminderSchedulingService();
            }
        }.execute();
    }

    private void fetchUpcomingRemindersFromDb() {
        reminderRepository.getUpcomingReminders(Calendar.getInstance().getTime()).observe(this, new Observer<List<Reminder>>() {
            @Override
            public void onChanged(@Nullable List<Reminder> list) {
                upcomingRemindersList = list;
                updateUpcomingReminders();
            }
        });
    }

    private void updateUpcomingReminders() {
        try {
            UpcomingRemindersFragment fragment = (UpcomingRemindersFragment) getSupportFragmentManager().findFragmentById(R.id.ad_fragment_container);
            if (fragment != null) fragment.updateRemindersRv(upcomingRemindersList);
        } catch (ClassCastException e) {
//            e.printStackTrace();
            Log.d(TAG, "ERROR: updateUpcomingReminders - " + e.getMessage());
        }
    }

    public List<Reminder> getUpcomingRemindersList() {
        return upcomingRemindersList;
    }

    private void fetchPastRemindersFromDb() {
        reminderRepository.getPastReminders(Calendar.getInstance().getTime()).observe(this, new Observer<List<Reminder>>() {
            @Override
            public void onChanged(@Nullable List<Reminder> list) {
                pastRemindersList = list;
                updatePastReminders();
            }
        });
    }

    private void updatePastReminders() {
        try {
            PastRemindersFragment fragment = (PastRemindersFragment) getSupportFragmentManager().findFragmentById(R.id.ad_fragment_container);
            if (fragment != null) fragment.updateRemindersRv(pastRemindersList);
        } catch (ClassCastException e) {
//            e.printStackTrace();
            Log.d(TAG, "ERROR: updatePastReminders - " + e.getMessage());
        }
    }

    public List<Reminder> getPastRemindersList() {
        return pastRemindersList;
    }

    private void initLocalDbContactsUpdate(List<User> users) {
        userRepository.insertUsers(users);
    }

    public void fetchAppUserContactsFromDb() {
        setAppUserList(new ArrayList<User>());
        userRepository.getAppUserContacts().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> list) {
                setAppUserList(list);
                updateAppUserContacts();
            }
        });
    }

    private void updateAppUserContacts() {
        try {
            AllContactsFragment fragment = (AllContactsFragment) getSupportFragmentManager().findFragmentById(R.id.ad_fragment_container);
            if (fragment != null) fragment.updateAppUserContactsRv(getAppUserList());
        } catch (ClassCastException e) {
            Log.d(TAG, "ERROR: updateAppUserContacts - " + e.getMessage());
        }
    }


    public void fetchNonAppUserContactsFromDb() {
        setNonAppUserList(new ArrayList<User>());
        userRepository.getNonAppUserContacts().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(@Nullable List<User> list) {
                setNonAppUserList(list);
                updateNonAppUserContacts();
            }
        });
    }

    private void updateNonAppUserContacts() {
        try {
            AllContactsFragment fragment = (AllContactsFragment) getSupportFragmentManager().findFragmentById(R.id.ad_fragment_container);
            if (fragment != null) fragment.updateNonAppUserContactsRv(getNonAppUserList());
        } catch (ClassCastException e) {
            Log.d(TAG, "ERROR: updateNonAppUserContacts - " + e.getMessage());
        }
    }

    private void resumeNormalApp() {
        hideProgressDialog();
        if (LocalStorage.getInstance(this).isFirstLogin()) {
            openThisFragment(UpcomingRemindersFragment.TAG, null);
        }
        LocalStorage.getInstance(this).firstLoginCompleted();
    }

    public void deleteReminder(String reminderId) {
        initDeleteReminderRequest(reminderId);
    }

    public void deleteUserFromDb(final String userId) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... voids) {
                userRepository.deleteUserFromUserId(userId);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                showToastMessage("User deleted successfully!", true);
            }
        }.execute();
    }

    private void initDeleteReminderRequest(String reminderId) {
        String requestUrl = DELETE_REMINDER_REQUEST_URL + reminderId;

        GenericDataHandler req3GenericDataHandler = new GenericDataHandler(this, this, REQUEST_TYPE_012);
        req3GenericDataHandler.jsonObjectRequest(null, requestUrl, Request.Method.GET, GetDeleteReminderResponse.class);
    }
}
