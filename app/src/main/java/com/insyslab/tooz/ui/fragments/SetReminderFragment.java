package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.insyslab.tooz.R;
import com.insyslab.tooz.models.FragmentState;

import static com.insyslab.tooz.utils.AppConstants.KEY_SET_REMINDER_TYPE;
import static com.insyslab.tooz.utils.AppConstants.VAL_SEND_REMINDER;
import static com.insyslab.tooz.utils.AppConstants.VAL_SET_PERSONAL_REMINDER;

/**
 * Created by TaNMay on 26/09/16.
 */

public class SetReminderFragment extends BaseFragment {

    public static final String TAG = "SetReminderFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private LinearLayout content;
    private TextInputEditText tietTask, tietTime, tietLocation, tietContact;
    private TextInputLayout tilTask, tilTime, tilLocation, tilContact;

    private String fragmentType = null;

    public SetReminderFragment() {

    }

    public static SetReminderFragment newInstance(Bundle bundle) {
        SetReminderFragment fragment = new SetReminderFragment();
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
            fragmentType = bundle.getString(KEY_SET_REMINDER_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_set_reminder, container, false);

        FragmentState fragmentState = new FragmentState(TAG);
        fragmentState.setFragmentDetailedName(fragmentType);
        updateFragment(fragmentState);
        initView(layout);

        if (fragmentType.equals(VAL_SET_PERSONAL_REMINDER)) {
            setUpPersonalReminderView();
        } else if (fragmentType.equals(VAL_SEND_REMINDER)) {
            setUpSendReminderView();
        } else {
            showToastMessage("Some error occurred!", false);
            getActivity().finish();
        }

        setUpActions();

        return layout;
    }

    private void setUpActions() {
        tilTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTimeClick();
            }
        });

        tilLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLocationClick();
            }
        });

        tilContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectContactsClick();
            }
        });
    }

    private void onSelectContactsClick() {

    }

    private void onLocationClick() {

    }

    private void onTimeClick() {

    }

    private void setUpSendReminderView() {
        tilTask.setHint("REMIND THEM TO...");
        tilTask.setVisibility(View.VISIBLE);

        tietTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = tietTask.getText().toString();
                if (text != null && !text.isEmpty()) {
                    tilTime.setVisibility(View.VISIBLE);
                    tilLocation.setVisibility(View.VISIBLE);
                    tilContact.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void setUpPersonalReminderView() {
        tilTask.setHint("REMIND ME TO...");
        tilTask.setVisibility(View.VISIBLE);

        tietTask.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = tietTask.getText().toString();
                if (text != null && !text.isEmpty()) {
                    tilTime.setVisibility(View.VISIBLE);
                    tilLocation.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.fsr_content);

        tilTask = rootView.findViewById(R.id.fsr_task_sec);
        tilTime = rootView.findViewById(R.id.fsr_time_sec);
        tilLocation = rootView.findViewById(R.id.fsr_location_sec);
        tilContact = rootView.findViewById(R.id.fsr_contact_sec);

        tietTask = rootView.findViewById(R.id.fsr_task);
        tietTime = rootView.findViewById(R.id.fsr_time);
        tietLocation = rootView.findViewById(R.id.fsr_location);
        tietContact = rootView.findViewById(R.id.fsr_contact);

        disableEdittext(tietTime);
        disableEdittext(tietLocation);
        disableEdittext(tietContact);
    }

    public void onSaveClick() {
        closeThisFragment();
    }

    private void closeThisFragment() {
        getActivity().onBackPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}