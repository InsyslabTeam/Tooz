package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnSettingItemClickListener;
import com.insyslab.tooz.models.eventbus.FragmentState;
import com.insyslab.tooz.models.SettingsItem;
import com.insyslab.tooz.models.responses.Error;
import com.insyslab.tooz.models.responses.LogoutResponse;
import com.insyslab.tooz.restclient.BaseResponseInterface;
import com.insyslab.tooz.restclient.GenericDataHandler;
import com.insyslab.tooz.ui.activities.OnboardingActivity;
import com.insyslab.tooz.ui.activities.SettingsActivity;
import com.insyslab.tooz.ui.adapters.SettingsAdapter;
import com.insyslab.tooz.utils.CustomShareIntent;
import com.insyslab.tooz.utils.LocalStorage;

import java.util.ArrayList;
import java.util.List;

import static com.insyslab.tooz.utils.ConstantClass.APP_URL;
import static com.insyslab.tooz.utils.ConstantClass.DEFAULT_APP_SHARE_TEXT;
import static com.insyslab.tooz.utils.ConstantClass.LOGOUT_REQUEST_URL;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_007;

/**
 * Created by TaNMay on 26/09/16.
 */

public class SettingsFragment extends BaseFragment implements OnSettingItemClickListener, BaseResponseInterface {

    public static final String TAG = "SettingsFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content;
    private TextView tvVersionInfo;
    private RecyclerView settingsRv;

    public SettingsFragment() {

    }

    public static SettingsFragment newInstance(Bundle bundle) {
        SettingsFragment fragment = new SettingsFragment();
        Bundle args = new Bundle();
        args.putBundle(ARG_PARAM1, bundle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Bundle bundle = getArguments().getBundle(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_settings, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        setUpVersionInfo();
        setUpSettingsRv();

        return layout;
    }

    private void setUpVersionInfo() {
        try {
            PackageInfo pInfo = getActivity().getPackageManager().getPackageInfo(getContext().getPackageName(), 0);
            String version = pInfo.versionName;
            tvVersionInfo.setText("Tooz - v" + version);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void setUpSettingsRv() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.Adapter adapter = new SettingsAdapter(this, getSettingsList());
        settingsRv.setLayoutManager(layoutManager);
        settingsRv.setAdapter(adapter);
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.fs_content);
        tvVersionInfo = rootView.findViewById(R.id.fs_version_info);
        settingsRv = rootView.findViewById(R.id.fs_settings_rv);
    }

    private void setUpActions() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private List<SettingsItem> getSettingsList() {
        List<SettingsItem> settings = new ArrayList<>();
        String[] settingsList = getResources().getStringArray(R.array.settings_list);

        for (int i = 0; i < settingsList.length; i++) {
            SettingsItem settingsItem = new SettingsItem();
            settingsItem.setSetting(settingsList[i]);
            settings.add(settingsItem);
        }

        return settings;
    }

    /**
     * 0. Sync Contacts
     * 1. Blocked Contacts
     * 2. Preferences
     * 3. Update Profile
     * 4. Privacy Settings
     * 5. Tell a Friend
     * 6. Notifications
     * 7. Feedback
     * 8. Terms and Privacy Policy
     * 9. Help
     * 10. Logout
     */

    @Override
    public void onSettingItemClick(View view) {
        int position = settingsRv.getChildAdapterPosition(view);
        switch (position) {
            case 0:
                redirectToThisFragment(ManualContactSyncFragment.TAG);
                break;
            case 1:
                redirectToThisFragment(BlockedContactsFragment.TAG);
                break;
            case 2:
                redirectToThisFragment(PreferencesFragment.TAG);
                break;
            case 3:
                redirectToThisFragment(UpdateProfileFragment.TAG);
                break;
            case 4:
                redirectToThisFragment(PrivacySettingsFragment.TAG);
                break;
            case 5:
                initAppReferal();
                break;
            case 6:
                redirectToThisFragment(NotificationSettingsFragment.TAG);
                break;
            case 7:
                redirectToThisFragment(FeedbackFragment.TAG);
                break;
            case 8:
                redirectToThisFragment(TermsPrivPolicyFragment.TAG);
                break;
            case 9:
                redirectToThisFragment(HelpFragment.TAG);
                break;
            case 10:
                initLogoutRequest();
                break;
            default:
                Log.d(TAG, "Some fragment error!");
                break;
        }
    }

    private void initLogoutRequest() {
        showProgressDialog(getString(R.string.loading));

        String requestUrl = LOGOUT_REQUEST_URL;
        GenericDataHandler req1GenericDataHandler = new GenericDataHandler(this, getContext(), REQUEST_TYPE_007);
        req1GenericDataHandler.jsonObjectRequest(null, requestUrl, Request.Method.GET, LogoutResponse.class);
    }


    private void proceedToLogout() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgressDialog(getString(R.string.loading));
            }

            @Override
            protected Void doInBackground(Void... voids) {
                ((SettingsActivity) getActivity()).clearRoomDatabase();
                LocalStorage.getInstance(getContext()).clearUserSharedPreferences();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                hideProgressDialog();
                performLogout();
            }
        }.execute();
    }

    private void performLogout() {
        Intent i = new Intent(getContext(), OnboardingActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
        getActivity().finish();
    }

    private void initAppReferal() {
        String shareText = DEFAULT_APP_SHARE_TEXT + "\n\n" + APP_URL;
        new CustomShareIntent(getContext(), shareText).shareToAllApps();
    }

    private void redirectToThisFragment(String tag) {
        ((SettingsActivity) getActivity()).openThisFragment(tag, null);
    }

    @Override
    public void onResponse(Object success, Object error, final int requestCode) {
        hideProgressDialog();
        if (error == null) {
            switch (requestCode) {
                case REQUEST_TYPE_007:
                    onLogoutResponse((LogoutResponse) success);
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
                showNetworkErrorSnackbar(content, getString(R.string.error_no_internet), getString(R.string.retry),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (requestCode) {
                                    case REQUEST_TYPE_007:
                                        initLogoutRequest();
                                        break;
                                    default:
                                        showToastMessage(getString(R.string.error_unknown), false);
                                }
                            }
                        });
            } else {
                showSnackbarMessage(content, customError.getMessage(), true, getString(R.string.ok), null, true);
            }

        }

    }

    private void onLogoutResponse(LogoutResponse success) {
        showToastMessage(success.getMsg(), false);
        proceedToLogout();
    }
}