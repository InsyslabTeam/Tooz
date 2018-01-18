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
import com.insyslab.tooz.interfaces.OnAllContactClickListener;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.ui.activities.DashboardActivity;
import com.insyslab.tooz.ui.adapters.AllContactsAdapter;

import java.util.List;

/**
 * Created by TaNMay on 26/09/16.
 */

public class AllContactsFragment extends BaseFragment implements OnAllContactClickListener {

    public static final String TAG = "AllContactsFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content;
    private RecyclerView allContactsRv;

    private RecyclerView.Adapter allContactsAdapter;

    private List<User> allContactsList;

    public AllContactsFragment() {

    }

    public static AllContactsFragment newInstance(Bundle bundle) {
        AllContactsFragment fragment = new AllContactsFragment();
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
        View layout = inflater.inflate(R.layout.fragment_all_contacts, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        allContactsList = ((DashboardActivity) getActivity()).getContactList();
        setUpAllContactsRv();

        return layout;
    }

    private void setUpAllContactsRv() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        allContactsAdapter = new AllContactsAdapter(this, allContactsList);
        allContactsRv.setLayoutManager(layoutManager);
        allContactsRv.setAdapter(allContactsAdapter);
    }

    private void initView(View rootView) {
        allContactsRv = rootView.findViewById(R.id.fac_contacts_rv);
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

    @Override
    public void onContactClick(View view) {
        int position = allContactsRv.getChildAdapterPosition(view);
    }

    public void updateContactsRv(List<User> list) {
        allContactsList = list;
        if (allContactsAdapter != null) allContactsAdapter.notifyDataSetChanged();
    }
}