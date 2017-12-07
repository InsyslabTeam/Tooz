package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnSettingItemClickListener;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.models.SettingsItem;
import com.insyslab.tooz.ui.activities.SettingsActivity;
import com.insyslab.tooz.ui.adapters.SettingsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TaNMay on 26/09/16.
 */

public class SettingsFragment extends BaseFragment implements OnSettingItemClickListener {

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
     * 0. Blocked Contacts
     * 1. Preferences
     * 2. Update Profile
     * 3. Privacy Settings
     * 4. Tell a Friend
     * 5. Notifications
     * 6. Feedback
     * 7. Terms and Privacy Policy
     * 8. Help
     */

    @Override
    public void onSettingItemClick(View view) {
        int position = settingsRv.getChildAdapterPosition(view);
        switch (position) {
            case 0:
                redirectToThisFragment(BlockedContactsFragment.TAG);
                break;
            case 1:
                redirectToThisFragment(PreferencesFragment.TAG);
                break;
            case 2:
                redirectToThisFragment(UpdateProfileFragment.TAG);
                break;
            case 3:
                redirectToThisFragment(PrivacySettingsFragment.TAG);
                break;
            case 4:
                initAppReferal();
                break;
            case 5:
                redirectToThisFragment(NotificationSettingsFragment.TAG);
                break;
            case 6:
                redirectToThisFragment(FeedbackFragment.TAG);
                break;
            case 7:
                redirectToThisFragment(TermsPrivPolicyFragment.TAG);
                break;
            case 8:
                redirectToThisFragment(HelpFragment.TAG);
                break;
            default:
                Log.d(TAG, "Some fragment error!");
                break;
        }
    }

    private void initAppReferal() {

    }

    private void redirectToThisFragment(String tag) {
        ((SettingsActivity) getActivity()).openThisFragment(tag, null);
    }
}