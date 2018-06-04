package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnSelectContactItemClickListener;
import com.insyslab.tooz.interfaces.OnSelectGroupItemClickListener;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.UserGroup;
import com.insyslab.tooz.models.eventbus.FragmentState;
import com.insyslab.tooz.ui.activities.ActionsActivity;
import com.insyslab.tooz.ui.adapters.SelectContactsAdapter;
import com.insyslab.tooz.ui.adapters.SelectGroupsAdapter;
import com.insyslab.tooz.utils.LocalStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

import static com.insyslab.tooz.utils.AppConstants.KEY_FROM_FRAGMENT;
import static com.insyslab.tooz.utils.AppConstants.KEY_FROM_FRAGMENT_DETAIL;
import static com.insyslab.tooz.utils.AppConstants.KEY_TO_CONTACTS_SELECTOR_BUNDLE;

public class SelectContactsFragment extends BaseFragment implements OnSelectContactItemClickListener, OnSelectGroupItemClickListener {

    public static final String TAG = SelectContactsFragment.class.getSimpleName() + " ==>";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content;
    private CheckBox cbSelectAll;
    private RecyclerView rvContacts, rvGroups;

    private RecyclerView.Adapter contactsAdapter, groupsAdapter;

    private List<User> contactItems;
    private List<UserGroup> groupItems;
    private List<User> selectedContacts = null;
    private List<UserGroup> selectedGroups = null;
    private String fromFragment = null, fromFragmentDetails = null;
    private User user;

    private OnContactsSelectedListener onContactsSelectedListener;

    public SelectContactsFragment() {

    }

    public static SelectContactsFragment newInstance(Bundle bundle) {
        SelectContactsFragment fragment = new SelectContactsFragment();
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
            if (bundle != null) {
                fromFragment = bundle.getString(KEY_FROM_FRAGMENT);
                fromFragmentDetails = bundle.getString(KEY_FROM_FRAGMENT_DETAIL);
                selectedContacts = (List<User>) bundle.getSerializable(KEY_TO_CONTACTS_SELECTOR_BUNDLE);
            }
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_select_contacts, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        user = LocalStorage.getInstance(getContext()).getUser();

        if (selectedContacts == null) selectedContacts = new ArrayList<>();
        if (selectedGroups == null) selectedGroups = new ArrayList<>();
        if (getActivity() != null) {
            contactItems = ((ActionsActivity) getActivity()).getAppUserList();
            groupItems = ((ActionsActivity) getActivity()).getUserGroupList();
        }
        setUpContactsRv();

        return layout;
    }

    private void setUpContactsRv() {
        if (selectedContacts.size() > 0) getUpgradedContactList();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        contactsAdapter = new SelectContactsAdapter(this, contactItems);
        rvContacts.setLayoutManager(layoutManager);
        rvContacts.setAdapter(contactsAdapter);

        setUpGroupsRv();
    }

    private void setUpGroupsRv() {
        if (selectedGroups.size() > 0) getUpgradedGroupList();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        groupsAdapter = new SelectGroupsAdapter(this, groupItems);
        rvGroups.setLayoutManager(layoutManager);
        rvGroups.setAdapter(groupsAdapter);
    }

    private void getUpgradedContactList() {
        HashMap<String, User> hashMap = new HashMap<>();
        for (int i = 0; i < selectedContacts.size(); i++) {
            hashMap.put(selectedContacts.get(i).getId(), selectedContacts.get(i));
        }

        for (int i = 0; i < contactItems.size(); i++) {
            if (hashMap.get(contactItems.get(i).getId()) != null) {
                contactItems.get(i).setSelected(true);
            }
        }
    }

    private void getUpgradedGroupList() {
        HashMap<String, UserGroup> hashMap = new HashMap<>();
        for (int i = 0; i < selectedGroups.size(); i++) {
            hashMap.put(selectedGroups.get(i).getId(), selectedGroups.get(i));
        }

        for (int i = 0; i < groupItems.size(); i++) {
            if (hashMap.get(groupItems.get(i).getId()) != null) {
                groupItems.get(i).setSelected(true);
            }
        }
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.fslc_content);
        rvContacts = rootView.findViewById(R.id.fslc_contacts);
        rvGroups = rootView.findViewById(R.id.fslc_groups);
        cbSelectAll = rootView.findViewById(R.id.fslc_select_all);
    }

    private void setUpActions() {
        cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) modifySelection(true);
                else modifySelection(false);
            }
        });
    }

    private void modifySelection(boolean isSelected) {
        for (int i = 0; i < contactItems.size(); i++) contactItems.get(i).setSelected(isSelected);
        contactsAdapter.notifyDataSetChanged();
        for (int i = 0; i < groupItems.size(); i++) groupItems.get(i).setSelected(isSelected);
        groupsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onContactsSelectedListener = (OnContactsSelectedListener) context;
    }

    @Override
    public void onDetach() {
        selectedContacts = getListOfSelectedContacts();
        FragmentState fragmentState = new FragmentState(fromFragment);
        fragmentState.setFragmentDetailedName(fromFragmentDetails);
        updateFragment(fragmentState);
        onContactsSelectedListener.onContactsSelected(selectedContacts, fromFragment);
        super.onDetach();
    }

    public void onSaveClick() {
        selectedContacts = getListOfSelectedContacts();

        if (selectedContacts != null && selectedContacts.size() == 0) {
            showSnackbarMessage(content,
                    "Please select some contacts to add to your group!",
                    true,
                    getString(R.string.ok),
                    null,
                    true);
        } else {
            closeThisFragment();
        }

    }

    private void closeThisFragment() {
        if (getActivity() != null)
            getActivity().onBackPressed();
    }

    private List<User> getListOfSelectedContacts() {
        HashMap<String, User> hashMap = new HashMap<>();
        List<User> list;
        for (int i = 0; i < contactItems.size(); i++) {
            if (contactItems.get(i).isSelected()) {
                if (!contactItems.get(i).getId().equals(user.getId()))
                    hashMap.put(contactItems.get(i).getId(), contactItems.get(i));
            }
        }

        for (int i = 0; i < groupItems.size(); i++) {
            if (groupItems.get(i).isSelected()) {
                for (int j = 0; j < groupItems.get(i).getUsers().size(); j++) {
                    if (!groupItems.get(i).getUsers().get(j).getId().equals(user.getId())) {
                        hashMap.put(groupItems.get(i).getUsers().get(j).getId(), groupItems.get(i).getUsers().get(j));
                    }
                }
            }
        }

        Collection<User> values = hashMap.values();
        list = new ArrayList<>(values);
        return list;
    }

    @Override
    public void onContactSelectorClick(int position) {
        contactItems.get(position).setSelected(!contactItems.get(position).isSelected());
        contactsAdapter.notifyItemChanged(position);
    }

    @Override
    public void onGroupSelectorClick(int position) {
        groupItems.get(position).setSelected(!groupItems.get(position).isSelected());
        groupsAdapter.notifyItemChanged(position);
    }

    public interface OnContactsSelectedListener {

        void onContactsSelected(List<User> contactItemList, String from);

    }

}