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
import com.insyslab.tooz.interfaces.OnUpcomingReminderClickListener;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.models.Reminder;
import com.insyslab.tooz.ui.adapters.UpcomingRemindersAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TaNMay on 26/09/16.
 */

public class UpcomingRemindersFragment extends BaseFragment implements OnUpcomingReminderClickListener {

    public static final String TAG = "UpcomingFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content, noContentView;
    private TextView tvNcvTitle;
    private RecyclerView upcomingRv;

    private RecyclerView.Adapter upcomingAdapter;

    private List<Reminder> upcomingReminderList;

    public UpcomingRemindersFragment() {

    }

    public static UpcomingRemindersFragment newInstance(Bundle bundle) {
        UpcomingRemindersFragment fragment = new UpcomingRemindersFragment();
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
        View layout = inflater.inflate(R.layout.fragment_upcoming_reminders, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        createDummyList();
        setUpUpcomingRv();

        return layout;
    }

    private void createDummyList() {
        upcomingReminderList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            Reminder reminder = new Reminder();
            reminder.setTitle("This is some upcoming testing reminder!");
            upcomingReminderList.add(reminder);
        }
    }

    private void setUpUpcomingRv() {
        if (upcomingReminderList != null && upcomingReminderList.size() > 0) {
            noContentView.setVisibility(View.GONE);
            upcomingRv.setVisibility(View.VISIBLE);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            upcomingAdapter = new UpcomingRemindersAdapter(this, upcomingReminderList);
            upcomingRv.setLayoutManager(layoutManager);
            upcomingRv.setAdapter(upcomingAdapter);
        } else {
            upcomingRv.setVisibility(View.GONE);
            noContentView.setVisibility(View.VISIBLE);

            tvNcvTitle.setText("No upcoming reminders for you!");
        }
    }

    private void initView(View rootView) {
        upcomingRv = rootView.findViewById(R.id.fur_upcoming_rv);
        noContentView = rootView.findViewById(R.id.ncv_content);
        tvNcvTitle = rootView.findViewById(R.id.ncv_text);
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
    public void onUpcomingReminderClick(View view) {
        int position = upcomingRv.getChildAdapterPosition(view);
        upcomingReminderList.get(position).setExpanded(!upcomingReminderList.get(position).isExpanded());
        upcomingAdapter.notifyItemChanged(position);
    }
}