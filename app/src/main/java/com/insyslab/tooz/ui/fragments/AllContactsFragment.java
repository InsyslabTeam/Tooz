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
import com.insyslab.tooz.interfaces.OnUserContactClickListener;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.ui.activities.DashboardActivity;
import com.insyslab.tooz.ui.adapters.AppUserContactsAdapter;
import com.insyslab.tooz.ui.adapters.NonAppUserContactsAdapter;

import java.util.List;

/**
 * Created by TaNMay on 26/09/16.
 */

public class AllContactsFragment extends BaseFragment implements OnUserContactClickListener {

    public static final String TAG = "AllContactsFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content;
    private RecyclerView appUserContactsRv, nonAppUserContactsRv;

    private RecyclerView.Adapter appUserContactsAdapter, nonAppUserContactsAdapter;

    private List<User> appUserContactsList, nonAppUserContactsList;

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

        appUserContactsList = ((DashboardActivity) getActivity()).getAppUserList();
        nonAppUserContactsList = ((DashboardActivity) getActivity()).getNonAppUserList();
        setUpAppUserContactsRv();
        setUpNonAppUserContactsRv();

        return layout;
    }

    private void setUpAppUserContactsRv() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        appUserContactsAdapter = new AppUserContactsAdapter(this, appUserContactsList);
        appUserContactsRv.setLayoutManager(layoutManager);
        appUserContactsRv.setAdapter(appUserContactsAdapter);
    }

    private void setUpNonAppUserContactsRv() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        nonAppUserContactsAdapter = new NonAppUserContactsAdapter(this, nonAppUserContactsList);
        nonAppUserContactsRv.setLayoutManager(layoutManager);
        nonAppUserContactsRv.setAdapter(nonAppUserContactsAdapter);
    }

    private void initView(View rootView) {
        appUserContactsRv = rootView.findViewById(R.id.fac_app_user_contacts_rv);
        nonAppUserContactsRv = rootView.findViewById(R.id.fac_non_app_user_contacts_rv);
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

    public void updateAppUserContactsRv(List<User> list) {
        appUserContactsList = list;
        if (appUserContactsAdapter != null) appUserContactsAdapter.notifyDataSetChanged();
    }

    public void updateNonAppUserContactsRv(List<User> list) {
        nonAppUserContactsList = list;
        if (nonAppUserContactsAdapter != null) nonAppUserContactsAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAppUserContactClick(View view) {
        int position = appUserContactsRv.getChildAdapterPosition(view);
    }

    @Override
    public void onNonAppUserContactClick(View view) {
        int position = nonAppUserContactsRv.getChildAdapterPosition(view);
    }

    @Override
    public void onNonAppUserInviteClick(int position) {

    }
}