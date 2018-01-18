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
import com.insyslab.tooz.interfaces.OnPastReminderClickListener;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.models.Reminder;
import com.insyslab.tooz.ui.activities.DashboardActivity;
import com.insyslab.tooz.ui.adapters.PastRemindersAdapter;

import java.util.List;

/**
 * Created by TaNMay on 26/09/16.
 */

public class PastRemindersFragment extends BaseFragment implements OnPastReminderClickListener {

    public static final String TAG = "PastRemindFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content;
    private RecyclerView pastRv;

    private RecyclerView.Adapter pastAdapter;

    private List<Reminder> pastReminderList;

    public PastRemindersFragment() {

    }

    public static PastRemindersFragment newInstance(Bundle bundle) {
        PastRemindersFragment fragment = new PastRemindersFragment();
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
        View layout = inflater.inflate(R.layout.fragment_past_reminders, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        pastReminderList = ((DashboardActivity) getActivity()).getRemindersList();
        setUpPastRv();

        return layout;
    }

    private void setUpPastRv() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        pastAdapter = new PastRemindersAdapter(this, pastReminderList);
        pastRv.setLayoutManager(layoutManager);
        pastRv.setAdapter(pastAdapter);
    }

    private void initView(View rootView) {
        pastRv = rootView.findViewById(R.id.fpr_past_rv);
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
    public void onPastReminderClick(View view) {
        int position = pastRv.getChildAdapterPosition(view);
        pastReminderList.get(position).setExpanded(!pastReminderList.get(position).isExpanded());
        pastAdapter.notifyItemChanged(position);
    }

    public void updateRemindersRv(List<Reminder> list) {
        pastReminderList = list;
        if (pastAdapter != null) pastAdapter.notifyDataSetChanged();
    }
}