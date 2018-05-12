package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
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
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.eventbus.FragmentState;
import com.insyslab.tooz.ui.activities.ActionsActivity;
import com.insyslab.tooz.ui.adapters.SelectContactsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.insyslab.tooz.utils.AppConstants.KEY_FROM_FRAGMENT;
import static com.insyslab.tooz.utils.AppConstants.KEY_FROM_FRAGMENT_DETAIL;
import static com.insyslab.tooz.utils.AppConstants.KEY_TO_CONTACTS_SELECTOR_BUNDLE;

/**
 * Created by TaNMay on 26/09/16.
 */

public class SelectContactsFragment extends BaseFragment implements OnSelectContactItemClickListener {

    public static final String TAG = "SelectContactsFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content, selectAllSec;
    private CheckBox cbSelectAll;
    private RecyclerView rvContacts;

    private RecyclerView.Adapter contactsAdapter;

    private List<User> contactItems;
    private List<User> selectedContacts = null;
    private String fromFragment = null, fromFragmentDetails = null;

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
            fromFragment = bundle.getString(KEY_FROM_FRAGMENT);
            fromFragmentDetails = bundle.getString(KEY_FROM_FRAGMENT_DETAIL);
            selectedContacts = (List<User>) bundle.getSerializable(KEY_TO_CONTACTS_SELECTOR_BUNDLE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_select_contacts, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        if (selectedContacts == null) selectedContacts = new ArrayList<>();
        contactItems = ((ActionsActivity) getActivity()).getAppUserList();
        setUpContactsRv();

        return layout;
    }

    private void setUpContactsRv() {
        if (selectedContacts.size() > 0) getUpgradedContactList();

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        contactsAdapter = new SelectContactsAdapter(this, contactItems);
        rvContacts.setLayoutManager(layoutManager);
        rvContacts.setAdapter(contactsAdapter);
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

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.fslc_content);
        rvContacts = rootView.findViewById(R.id.fslc_contacts);
        selectAllSec = rootView.findViewById(R.id.fslc_select_all_sec);
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
        for (int i = 0; i < contactItems.size(); i++) {
            contactItems.get(i).setSelected(isSelected);
        }
        contactsAdapter.notifyDataSetChanged();
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
        getActivity().onBackPressed();
    }

    private List<User> getListOfSelectedContacts() {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < contactItems.size(); i++) {
            if (contactItems.get(i).isSelected()) list.add(contactItems.get(i));
        }
        return list;
    }

    @Override
    public void onContactSelectorClick(int position) {
        contactItems.get(position).setSelected(!contactItems.get(position).isSelected());
        contactsAdapter.notifyItemChanged(position);
    }

    public interface OnContactsSelectedListener {

        void onContactsSelected(List<User> contactItemList, String from);

    }

}