package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

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

        setUpSettingsRv();

        return layout;
    }

    private void setUpSettingsRv() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        RecyclerView.Adapter adapter = new SettingsAdapter(this, getSettingsList());
        settingsRv.setLayoutManager(layoutManager);
        settingsRv.setAdapter(adapter);
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.fs_content);
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
                redirectToThisFragment(PrivacyFragment.TAG);
                break;
            case 4:
                redirectToThisFragment(HelpFragment.TAG);
                break;
            default:
                break;
        }
    }

    private void redirectToThisFragment(String tag) {
        ((SettingsActivity) getActivity()).openThisFragment(tag, null);
    }
}