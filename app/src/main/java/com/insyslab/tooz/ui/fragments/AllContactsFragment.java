package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnUserContactClickListener;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.eventbus.FragmentState;
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

    private RelativeLayout content, noContentView;
    private LinearLayout noAppUserView;
    private NestedScrollView scrollContent;
    private TextView tvNcvTitle, tvNoAppUserTitle;
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

        if (contactsSynced()) {
            noContentView.setVisibility(View.GONE);
            scrollContent.setVisibility(View.VISIBLE);

            setUpAppUserContactsRv();
            setUpNonAppUserContactsRv();
        } else {
            scrollContent.setVisibility(View.GONE);
            noContentView.setVisibility(View.VISIBLE);

            tvNcvTitle.setText("You haven't synced your contacts yet!");
        }

        return layout;
    }

    private boolean contactsSynced() {
        if (appUserContactsList != null && nonAppUserContactsList != null) {
            if (appUserContactsList.size() > 0 || nonAppUserContactsList.size() > 0) return true;
            else return false;
        } else if (appUserContactsList == null && nonAppUserContactsList == null) {
            return false;
        } else {
            return true;
        }
    }

    private void setUpAppUserContactsRv() {
        if (appUserContactsList != null && appUserContactsList.size() > 0) {
            noAppUserView.setVisibility(View.GONE);
            appUserContactsRv.setVisibility(View.VISIBLE);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            appUserContactsAdapter = new AppUserContactsAdapter(this, appUserContactsList);
            appUserContactsRv.setLayoutManager(layoutManager);
            appUserContactsRv.setAdapter(appUserContactsAdapter);
        } else {
            appUserContactsRv.setVisibility(View.GONE);
            noAppUserView.setVisibility(View.VISIBLE);

            tvNoAppUserTitle.setText("None of your contacts use " + getString(R.string.app_name) + "! You can invite some of them here.");
        }
    }

    private void setUpNonAppUserContactsRv() {
        int maxSize = nonAppUserContactsList.size() > 10 ? 10 : nonAppUserContactsList.size();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        nonAppUserContactsAdapter = new NonAppUserContactsAdapter(this, nonAppUserContactsList.subList(0, maxSize));
        nonAppUserContactsRv.setLayoutManager(layoutManager);
        nonAppUserContactsRv.setAdapter(nonAppUserContactsAdapter);
    }

    private void initView(View rootView) {
        appUserContactsRv = rootView.findViewById(R.id.fac_app_user_contacts_rv);
        nonAppUserContactsRv = rootView.findViewById(R.id.fac_non_app_user_contacts_rv);
        noContentView = rootView.findViewById(R.id.ncv_content);
        tvNcvTitle = rootView.findViewById(R.id.ncv_text);
        scrollContent = rootView.findViewById(R.id.fac_scroll_content);
        noAppUserView = rootView.findViewById(R.id.fac_no_content);
        tvNoAppUserTitle = rootView.findViewById(R.id.fac_ncv_text);
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
        appUserContactsList.clear();
        appUserContactsList.addAll(list);
        if (appUserContactsAdapter != null) appUserContactsAdapter.notifyDataSetChanged();
        else setUpAppUserContactsRv();
    }

    public void updateNonAppUserContactsRv(List<User> list) {
        nonAppUserContactsList.clear();
        nonAppUserContactsList.addAll(list);
        if (nonAppUserContactsAdapter != null) nonAppUserContactsAdapter.notifyDataSetChanged();
        else setUpNonAppUserContactsRv();
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