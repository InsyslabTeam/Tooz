package com.insyslab.tooz.ui.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.model.LatLng;
import com.insyslab.tooz.R;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.ui.fragments.AddContactFragment;
import com.insyslab.tooz.ui.fragments.LocationSelectorFragment;
import com.insyslab.tooz.ui.fragments.SetReminderFragment;

import static com.insyslab.tooz.utils.AppConstants.KEY_SET_REMINDER_TYPE;
import static com.insyslab.tooz.utils.AppConstants.KEY_TO_ACTIONS;
import static com.insyslab.tooz.utils.AppConstants.VAL_SEND_REMINDER;
import static com.insyslab.tooz.utils.AppConstants.VAL_SET_PERSONAL_REMINDER;

public class ActionsActivity extends BaseActivity implements LocationSelectorFragment.OnLocationSelectedListener {

    private static final String TAG = "Actions ==> ";

    private Toolbar toolbar;

    private String currentFragment = null, toFragment = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actions);

        toFragment = getIntent().getStringExtra(KEY_TO_ACTIONS);
        String fragmentType = getIntent().getStringExtra(KEY_SET_REMINDER_TYPE);

        initView();
        setUpToolbar();
        setUpActions();

        getFragmentStatus();
        if (toFragment != null) {
            Bundle bundle = new Bundle();
            bundle.putString(KEY_SET_REMINDER_TYPE, fragmentType);
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
        } else if (fragmentTag.equals(SetReminderFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .replace(R.id.aa_fragment_container, SetReminderFragment.newInstance((Bundle) bundle), fragmentTag)
                    .commit();
        } else if (fragmentTag.equals(LocationSelectorFragment.TAG)) {
            fragmentManager.beginTransaction()
                    .add(R.id.aa_fragment_container, LocationSelectorFragment.newInstance((Bundle) bundle), fragmentTag)
                    .addToBackStack(TAG)
                    .commit();
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

    private void setUpActions() {

    }

    public void onEvent(FragmentState fragmentState) {
        Log.d(TAG, fragmentState.getVisibleFragment());
        currentFragment = fragmentState.getVisibleFragment();

        if (fragmentState.getVisibleFragment().equals(AddContactFragment.TAG)) {
            updateToolbar("Add Contact");
        } else if (fragmentState.getVisibleFragment().equals(SetReminderFragment.TAG)
                && fragmentState.getFragmentDetailedName().equals(VAL_SET_PERSONAL_REMINDER)) {
            updateToolbar("Set Personal Reminder");
        } else if (fragmentState.getVisibleFragment().equals(SetReminderFragment.TAG)
                && fragmentState.getFragmentDetailedName().equals(VAL_SEND_REMINDER)) {
            updateToolbar("Send Reminder");
        } else if (fragmentState.getVisibleFragment().equals(LocationSelectorFragment.TAG)) {
            updateToolbar("Select Location");
        }
    }

    private void updateToolbar(String toolbarTitle) {
        getSupportActionBar().setTitle(toolbarTitle);
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
        getMenuInflater().inflate(R.menu.menu_actions, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_save) {
            onToolbarSaveClick();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void onToolbarSaveClick() {
        switch (currentFragment) {
            case AddContactFragment.TAG:
                AddContactFragment fragment1 = (AddContactFragment) getSupportFragmentManager().findFragmentById(R.id.aa_fragment_container);
                fragment1.onSaveClick();
                break;
            case SetReminderFragment.TAG:
                SetReminderFragment fragment2 = (SetReminderFragment) getSupportFragmentManager().findFragmentById(R.id.aa_fragment_container);
                fragment2.onSaveClick();
                break;
            case LocationSelectorFragment.TAG:
                LocationSelectorFragment fragment3 = (LocationSelectorFragment) getSupportFragmentManager().findFragmentById(R.id.aa_fragment_container);
                fragment3.onSaveClick();
                break;
        }
    }

    @Override
    public void onLocationSelected(LatLng latLng, String address) {
        SetReminderFragment fragment = (SetReminderFragment) getSupportFragmentManager().findFragmentById(R.id.aa_fragment_container);
        fragment.onLocationSet(address);
    }
}
