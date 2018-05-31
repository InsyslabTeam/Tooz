package com.insyslab.tooz.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnPastReminderClickListener;
import com.insyslab.tooz.models.Reminder;
import com.insyslab.tooz.models.eventbus.FragmentState;
import com.insyslab.tooz.ui.activities.DashboardActivity;
import com.insyslab.tooz.ui.adapters.PastRemindersAdapter;

import java.util.List;

public class PastRemindersFragment extends BaseFragment implements OnPastReminderClickListener {

    public static final String TAG = PastRemindersFragment.class.getSimpleName() + " ==>";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout noContentView;
    private TextView tvNcvTitle;
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
//        if (getArguments() != null) {
//            Bundle bundle = getArguments().getBundle(ARG_PARAM1);
//        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_past_reminders, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        if (getActivity() != null) {
            pastReminderList = ((DashboardActivity) getActivity()).getPastRemindersList();
        }
        setUpPastRv();

        return layout;
    }

    @SuppressLint("SetTextI18n")
    private void setUpPastRv() {
        if (pastReminderList != null && pastReminderList.size() > 0) {
            noContentView.setVisibility(View.GONE);
            pastRv.setVisibility(View.VISIBLE);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            pastAdapter = new PastRemindersAdapter(this, pastReminderList);
            pastRv.setLayoutManager(layoutManager);
            pastRv.setAdapter(pastAdapter);
        } else {
            pastRv.setVisibility(View.GONE);
            noContentView.setVisibility(View.VISIBLE);

            tvNcvTitle.setText("No past reminders for you!");
        }
    }

    private void initView(View rootView) {
        pastRv = rootView.findViewById(R.id.fpr_past_rv);
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
    public void onPastReminderClick(View view) {
        int position = pastRv.getChildAdapterPosition(view);
        pastReminderList.get(position).setExpanded(!pastReminderList.get(position).isExpanded());
        pastAdapter.notifyItemChanged(position);
    }

    public void updateRemindersRv(List<Reminder> list) {
        pastReminderList.clear();
        pastReminderList.addAll(list);
        if (pastAdapter != null) pastAdapter.notifyDataSetChanged();
        else setUpPastRv();
    }
}