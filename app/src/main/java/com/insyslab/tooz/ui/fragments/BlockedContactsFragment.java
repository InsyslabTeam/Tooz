package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnBlockedContactsClickListener;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.eventbus.FragmentState;
import com.insyslab.tooz.ui.activities.SettingsActivity;
import com.insyslab.tooz.ui.adapters.BlockedContactsAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TaNMay on 26/09/16.
 */

public class BlockedContactsFragment extends BaseFragment implements OnBlockedContactsClickListener {

    public static final String TAG = "BlockedFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content;
    private TextView tvCount;
    private RecyclerView blockedContactsRv;

    private RecyclerView.Adapter blockedContactsAdapter;

    private List<User> contactItems;

    public BlockedContactsFragment() {

    }

    public static BlockedContactsFragment newInstance(Bundle bundle) {
        BlockedContactsFragment fragment = new BlockedContactsFragment();
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
        View layout = inflater.inflate(R.layout.fragment_blocked_contacts, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        createDummyContactList();
        setUpBlockedContactsRv();

        return layout;
    }

    private void createDummyContactList() {
        contactItems = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            User contactItem = new User();
            contactItem.setName("Blocked Vodafone");
            contactItem.setMobile("+91 88888 88888");
            contactItems.add(contactItem);
        }
    }

    private void setUpBlockedContactsRv() {
        tvCount.setText(contactItems.size() + " blocked contacts.");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        blockedContactsAdapter = new BlockedContactsAdapter(this, contactItems);
        blockedContactsRv.setLayoutManager(layoutManager);
        blockedContactsRv.setAdapter(blockedContactsAdapter);
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.fbc_content);
        tvCount = rootView.findViewById(R.id.fbc_count);
        blockedContactsRv = rootView.findViewById(R.id.fbc_blocked_contacts);
    }

    private void setUpActions() {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        updateFragment(new FragmentState(SettingsFragment.TAG));
        super.onDetach();
    }

    @Override
    public void onUnblockClick(int position) {
        contactItems.get(position).setBlocked(!contactItems.get(position).isBlocked());
        blockedContactsAdapter.notifyItemChanged(position);
    }

    public void onSaveClick() {
        closeThisFragment();
    }

    private void closeThisFragment() {
        ((SettingsActivity) getActivity()).closeCurrentFragment();
    }
}