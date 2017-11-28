package com.insyslab.tooz.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
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

import com.insyslab.tooz.R;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.ui.fragments.AllContactsFragment;
import com.insyslab.tooz.ui.fragments.PastRemindersFragment;
import com.insyslab.tooz.ui.fragments.UpcomingRemindersFragment;

public class DashboardActivity extends BaseActivity {

    private static final String TAG = "Dashboard ==> ";

    private Toolbar toolbar;
    private FloatingActionButton fab;
    private RelativeLayout upcomingTab, pastTab, allContactsTab;
    private TextView tvUpcoming, tvPast, tvAllContacts;

    private String currentFragment = null;
    private boolean doubleBackToExitPressedOnce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initView();
        setUpToolbar();
        setUpActions();

        doubleBackToExitPressedOnce = false;
        getFragmentStatus();
        openThisFragment(UpcomingRemindersFragment.TAG, null);
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
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onDashboardFabClick();
            }
        });

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
    }

    private void onDashboardFabClick() {

    }

    private void onToolbarSettingsClick() {
        openSettingsActivity();
    }

    private void openSettingsActivity() {
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
        openThisFragment(AllContactsFragment.TAG, null);
    }

    private void onPastTabClick() {
        openThisFragment(PastRemindersFragment.TAG, null);
    }

    private void onUpcomingTabClick() {
        openThisFragment(UpcomingRemindersFragment.TAG, null);
    }

    private void initView() {
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);

        upcomingTab = findViewById(R.id.ad_upcoming);
        pastTab = findViewById(R.id.ad_past);
        allContactsTab = findViewById(R.id.ad_all_contacts);

        tvUpcoming = findViewById(R.id.ad_upcoming_title);
        tvPast = findViewById(R.id.ad_past_title);
        tvAllContacts = findViewById(R.id.ad_all_contacts_title);
    }

    @Override
    public void onBackPressed() {
//        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//            getSupportFragmentManager().popBackStack();
//        } else {
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
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_dashboard, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            onToolbarSettingsClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
