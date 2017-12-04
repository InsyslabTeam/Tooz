package com.insyslab.tooz.ui.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.insyslab.tooz.R;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.ui.activities.ActionsActivity;
import com.insyslab.tooz.ui.activities.DashboardActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import static com.insyslab.tooz.utils.AppConstants.KEY_SET_REMINDER_TYPE;
import static com.insyslab.tooz.utils.AppConstants.VAL_SEND_REMINDER;
import static com.insyslab.tooz.utils.AppConstants.VAL_SET_PERSONAL_REMINDER;
import static com.insyslab.tooz.utils.Util.DEFAULT_DATE_FORMAT;
import static com.insyslab.tooz.utils.Util.getAmPmFromIndex;
import static com.insyslab.tooz.utils.Util.getDateExtension;
import static com.insyslab.tooz.utils.Util.getDayOfWeekFromIndex;
import static com.insyslab.tooz.utils.Util.getMonthFromIndex;

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
    private String timeSelected = null, locationSelected = null;

    private SimpleDateFormat simpleDateFormat;

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
        simpleDateFormat = new SimpleDateFormat(DEFAULT_DATE_FORMAT, Locale.getDefault());

        tietTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTimeClick();
            }
        });

        tietLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLocationClick();
            }
        });

        tietContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectContactsClick();
            }
        });
    }

    private void onSelectContactsClick() {

    }

    private void onLocationClick() {
        openLocationSelectorFragment();
    }

    private void openLocationSelectorFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SET_REMINDER_TYPE, fragmentType);
        ((ActionsActivity) getActivity()).openThisFragment(LocationSelectorFragment.TAG, bundle);
    }

    private void onTimeClick() {
        initDateAndTimeSelection();
    }

    private void initDateAndTimeSelection() {
        showDatePicker();
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(),
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        showTimePicker(i, i1, i2);
                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );

        datePickerDialog.show();
    }

    private void showTimePicker(final int i, final int i1, final int i2) {
        Calendar calendar = Calendar.getInstance();

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i3, int i4) {
                        Calendar cal1 = Calendar.getInstance();
                        cal1.set(i, i1, i2, i3, i4);
                        timeSelected = simpleDateFormat.format(cal1.getTime());

                        setUpDateAndTime(cal1);
                    }
                },
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
        );

        timePickerDialog.show();
    }

    private void setUpDateAndTime(Calendar cal) {
        String date = cal.get(Calendar.DAY_OF_MONTH) + getDateExtension(cal.get(Calendar.DAY_OF_MONTH));
        String month = getMonthFromIndex(cal.get(Calendar.MONTH));
        String day = getDayOfWeekFromIndex(cal.get(Calendar.DAY_OF_WEEK));
        String meridian = getAmPmFromIndex(cal.get(Calendar.AM_PM));

        String dateTime = date + " " + month + " (" + day + "),"
                + " " + cal.get(Calendar.YEAR)
                + "  " + cal.get(Calendar.HOUR)
                + ":" + cal.get(Calendar.MINUTE)
                + " " + meridian;

        tietTime.setText(dateTime);
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

        clickableEdittext(tietTime);
        clickableEdittext(tietLocation);
        clickableEdittext(tietContact);
    }

    public void onLocationSet(String locationSelected) {
        tietLocation.setText(locationSelected);
    }

    public void onSaveClick() {
        tietTask.setError(null);
        tietContact.setError(null);

        String task = tietTask.getText().toString();
        String contacts = tietContact.getText().toString();

        if (task != null && task.isEmpty()) {
            tietTask.setError(getString(R.string.error_empty_field));
        } else if (timeSelected == null) {
            showToastMessage("Please select either a time or a location for the reminder!", true);
        } else if (fragmentType.equals(VAL_SEND_REMINDER) && contacts != null && contacts.isEmpty()) {
            tietContact.setError(getString(R.string.error_empty_field));
        } else {
            closeThisFragment();
        }
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