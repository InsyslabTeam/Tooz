package com.insyslab.tooz.ui.fragments;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnReminderFetchedListener;
import com.insyslab.tooz.models.Reminder;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.eventbus.FragmentState;
import com.insyslab.tooz.models.eventbus.ReminderCreated;
import com.insyslab.tooz.models.responses.Error;
import com.insyslab.tooz.restclient.BaseResponseInterface;
import com.insyslab.tooz.restclient.GenericDataHandler;
import com.insyslab.tooz.ui.activities.ActionsActivity;
import com.insyslab.tooz.utils.LocalStorage;
import com.insyslab.tooz.utils.Util;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.insyslab.tooz.utils.AppConstants.KEY_FROM_FRAGMENT;
import static com.insyslab.tooz.utils.AppConstants.KEY_GET_REMINDER_ID;
import static com.insyslab.tooz.utils.AppConstants.KEY_SET_REMINDER_TYPE;
import static com.insyslab.tooz.utils.AppConstants.VAL_SEND_REMINDER;
import static com.insyslab.tooz.utils.AppConstants.VAL_SET_PERSONAL_REMINDER;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_010;
import static com.insyslab.tooz.utils.ConstantClass.UPDATE_REMINDER_REQUEST_URL;
import static com.insyslab.tooz.utils.Util.getAddressFromCoords;
import static com.insyslab.tooz.utils.Util.getAmPmFromIndex;
import static com.insyslab.tooz.utils.Util.getCalenderFormatDate;
import static com.insyslab.tooz.utils.Util.getDateExtension;
import static com.insyslab.tooz.utils.Util.getDateInDefaultDateFormat;
import static com.insyslab.tooz.utils.Util.getDayOfWeekFromIndex;
import static com.insyslab.tooz.utils.Util.getFormattedHourOrMinute;
import static com.insyslab.tooz.utils.Util.getMonthFromIndex;

/**
 * Created by TaNMay on 26/09/16.
 */

public class EditReminderFragment extends BaseFragment implements OnReminderFetchedListener, BaseResponseInterface {

    public static final String TAG = "EditReminderFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private LinearLayout content, optionsSec;
    private TextInputEditText tietTask, tietTime, tietLocation, tietContact;
    private TextInputLayout tilTask, tilTime, tilLocation, tilContact;
    private ImageView ivOptionTime, ivOptionLocation;

    private String fragmentType = null;
    private String timeSelected = null, addressSelected = null;
    private LatLng latLngSelected = null;
    private Boolean isTimeSelected = true;
    private String reminderId = null;

    private List<User> selectedMembers = null;
    private User user;
    private Reminder reminder;

    public EditReminderFragment() {

    }

    public static EditReminderFragment newInstance(Bundle bundle) {
        EditReminderFragment fragment = new EditReminderFragment();
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
            reminderId = bundle.getString(KEY_GET_REMINDER_ID);
        }
        ActionsActivity.onReminderFetchedListener = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_set_reminder, container, false);

        FragmentState fragmentState = new FragmentState(TAG);
        fragmentState.setFragmentDetailedName("Edit Reminder");
        updateFragment(fragmentState);
        initView(layout);

        user = LocalStorage.getInstance(getContext()).getUser();

        ((ActionsActivity) getActivity()).getReminderFromId(reminderId);

        setUpActions();

        return layout;
    }

    private void setUpInitialView() {
        tietTask.setText(reminder.getTask());
        if (isTimeSelected) {
            Calendar selectedCal = getCalenderFormatDate(reminder.getDate());
            setUpDateAndTime(selectedCal);
        } else {
            Double latDouble = Double.parseDouble(reminder.getLatitude());
            Double longDouble = Double.parseDouble(reminder.getLongitude());
            String location = getAddressFromCoords(getContext(), new LatLng(latDouble, longDouble));
            if (location != null) tietLocation.setText(location);
            else tietLocation.setText(reminder.getLatitude() + ", " + reminder.getLongitude());
        }

        if (reminder.getContacts() != null && reminder.getContacts().size() > 0) {
            onMembersSelected(reminder.getContacts());
        }
    }

    private void setUpActions() {
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

    private void updateSelectionView() {
        if (isTimeSelected) {
            ivOptionLocation.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_option_location));
            ivOptionTime.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_option_time_selected));
            enableTimeView();
        } else {
            ivOptionTime.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_option_time));
            ivOptionLocation.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_option_location_selected));
            enableLocationView();
        }
        if (fragmentType.equals(VAL_SEND_REMINDER)) enableContactsView();
    }

    private void enableContactsView() {
        tilContact.setVisibility(View.VISIBLE);
    }

    private void enableLocationView() {
        tilTime.setVisibility(View.GONE);
        tietTime.setVisibility(View.GONE);
        tilLocation.setVisibility(View.VISIBLE);
        tietLocation.setVisibility(View.VISIBLE);
    }

    private void enableTimeView() {
        tilLocation.setVisibility(View.GONE);
        tietLocation.setVisibility(View.GONE);
        tilTime.setVisibility(View.VISIBLE);
        tietTime.setVisibility(View.VISIBLE);
    }

    private void onSelectContactsClick() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_FROM_FRAGMENT, TAG);
        ((ActionsActivity) getActivity()).openThisFragment(SelectContactsFragment.TAG, bundle);
    }

    private void onLocationClick() {
        Util.hideSoftKeyboard(getActivity());
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

        DatePicker datePicker = datePickerDialog.getDatePicker();
        datePicker.setMinDate(System.currentTimeMillis());
        calendar.add(Calendar.YEAR, +1);
        datePicker.setMaxDate(calendar.getTimeInMillis());

        datePickerDialog.show();
    }

    private void showTimePicker(final int i, final int i1, final int i2) {
        Calendar calendar = Calendar.getInstance();
        final long currTime = calendar.getTimeInMillis() + 1000;

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(),
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i3, int i4) {
                        Calendar cal1 = Calendar.getInstance();
                        cal1.set(i, i1, i2, i3, i4);
                        long selectedTime = cal1.getTimeInMillis();

                        if (selectedTime <= currTime) {
                            showToastMessage("Please select a time in the future!", false);
                        } else {
                            timeSelected = getDateInDefaultDateFormat(cal1);
                            setUpDateAndTime(cal1);
                        }
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
        String hour = getFormattedHourOrMinute(cal.get(Calendar.HOUR));
        String minute = getFormattedHourOrMinute(cal.get(Calendar.MINUTE));

        String dateTime = date + " " + month + " (" + day + "),"
                + " " + cal.get(Calendar.YEAR)
                + "  " + hour
                + ":" + minute
                + " " + meridian;

        tietTime.setText(dateTime);
    }

    private void setUpSendReminderView() {
        tilTask.setHint("REMIND THEM TO...");
        tilTask.setVisibility(View.VISIBLE);
        optionsSec.setVisibility(View.VISIBLE);
        updateSelectionView();
    }

    private void setUpPersonalReminderView() {
        tilTask.setHint("REMIND ME TO...");
        tilTask.setVisibility(View.VISIBLE);
        optionsSec.setVisibility(View.VISIBLE);
        updateSelectionView();
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

        optionsSec = rootView.findViewById(R.id.fsr_option_sec);
        ivOptionTime = rootView.findViewById(R.id.fsr_option_time);
        ivOptionLocation = rootView.findViewById(R.id.fsr_option_location);

        clickableEdittext(tietTime);
        clickableEdittext(tietLocation);
        clickableEdittext(tietContact);
    }

    public void onLocationSet(LatLng latLng, String locationSelected) {
        latLngSelected = latLng;
        addressSelected = locationSelected;

        tietLocation.setText(locationSelected);
    }

    public void onSaveClick() {
        tietTask.setError(null);
        tietContact.setError(null);

        String task = tietTask.getText().toString().trim();

        if (isTimeSelected && timeSelected == null) {
            Calendar cal = getCalenderFormatDate(reminder.getDate());
            timeSelected = getDateInDefaultDateFormat(cal);
        }

        if (!isTimeSelected && latLngSelected == null) {
            Double latDouble = Double.parseDouble(reminder.getLatitude());
            Double longDouble = Double.parseDouble(reminder.getLongitude());
            latLngSelected = new LatLng(latDouble, longDouble);
        }

        if (task != null && task.isEmpty()) {
            tietTask.setError(getString(R.string.error_empty_field));
        } else if (isTimeSelected && timeSelected == null) {
            showToastMessage("Please select a time for the reminder!", true);
        } else if (!isTimeSelected && latLngSelected == null) {
            showToastMessage("Please select a location for the reminder!", true);
        } else if (fragmentType.equals(VAL_SEND_REMINDER) && selectedMembers == null) {
            showToastMessage("Please select a contact to send the reminder!", false);
        } else if (fragmentType.equals(VAL_SEND_REMINDER) && selectedMembers.size() == 0) {
            showToastMessage("Please select a contact to send the reminder!", false);
        } else {
            reminder.setTask(tietTask.getText().toString().trim());
            if (latLngSelected != null) {
                reminder.setLatitude(latLngSelected.latitude + "");
                reminder.setLongitude(latLngSelected.longitude + "");
            }
            if (selectedMembers != null) reminder.setContacts(selectedMembers);
            initUpdateReminderRequest();
        }
    }

    private void initUpdateReminderRequest() {
        String requestUrl = UPDATE_REMINDER_REQUEST_URL;
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(new Gson().toJson(reminder));
            if (timeSelected != null) {
                jsonObject.put("date", timeSelected);
                jsonObject.put("time", timeSelected);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        GenericDataHandler reqGenericDataHandler = new GenericDataHandler(this, getContext(), REQUEST_TYPE_010);
        reqGenericDataHandler.jsonObjectRequest(jsonObject, requestUrl, Request.Method.POST, Reminder.class);
    }

    private String getLatitudeFromInput() {
        String locationInput = tietLocation.getText().toString().trim();
        if (locationInput != null && !locationInput.isEmpty()) {
            String[] coords = locationInput.split(",");
            return coords[0].trim();
        } else return null;
    }

    private String getLongitudeFromInput() {
        String locationInput = tietLocation.getText().toString().trim();
        if (locationInput != null && !locationInput.isEmpty()) {
            String[] coords = locationInput.split(",");
            return coords[1].trim();
        } else return null;
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

    public void onMembersSelected(List<User> contactItemList) {
        selectedMembers = contactItemList;
        String contactListStr = getStringFormatContactList();
        tietContact.setText(contactListStr);
    }

    private String getStringFormatContactList() {
        String contactListStr = "";
        for (int i = 0; i < selectedMembers.size(); i++) {
            contactListStr += selectedMembers.get(i).getName();
            if (i != selectedMembers.size() - 1) contactListStr += ", ";
        }
        return contactListStr;
    }

    private void onUpdateReminderResponse(Reminder success) {
        EventBus.getDefault().postSticky(new ReminderCreated(true, !fragmentType.equals(VAL_SEND_REMINDER)));
        if (fragmentType.equals(VAL_SEND_REMINDER))
            showToastMessage("Updated reminder sent to " + selectedMembers.size() + " contact(s)!", true);
        else showToastMessage("Personal reminder updated successfully!", true);
        closeThisFragment();
    }

    @Override
    public void onReminderFetched(Reminder reminder) {
        if (reminder != null) {
            this.reminder = reminder;
            fragmentType = reminder.getContacts() != null && reminder.getContacts().size() > 0 ? VAL_SEND_REMINDER : VAL_SET_PERSONAL_REMINDER;

            if (fragmentType.equals(VAL_SET_PERSONAL_REMINDER)) {
                setUpPersonalReminderView();
            } else if (fragmentType.equals(VAL_SEND_REMINDER)) {
                setUpSendReminderView();
            } else {
                showToastMessage("Some error occurred!", false);
                getActivity().finish();
            }

            isTimeSelected = reminder.getLatitude() == null && reminder.getLongitude() == null;
            updateSelectionView();

            setUpInitialView();
        }
    }

    @Override
    public void onResponse(Object success, Object error, final int requestCode) {
        hideProgressDialog();
        if (error == null) {
            switch (requestCode) {
                case REQUEST_TYPE_010:
                    onUpdateReminderResponse((Reminder) success);
                    break;
                default:
                    showToastMessage("ERROR " + requestCode + "!", false);
                    break;
            }
        } else {
            Error customError = (Error) error;
//            Log.d(TAG, "Error: " + customError.getMessage() + " -- " + customError.getStatus() + " -- ");
            if (customError.getStatus() == 000) {
                hideProgressDialog();
                showNetworkErrorSnackbar(content, getString(R.string.error_no_internet), getString(R.string.retry),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (requestCode) {
                                    case REQUEST_TYPE_010:
                                        initUpdateReminderRequest();
                                        break;
                                    default:
                                        showToastMessage(getString(R.string.error_unknown), false);
                                }
                            }
                        });
            } else {
                showSnackbarMessage(content, customError.getMessage(), true, getString(R.string.ok), null, true);
            }
        }

    }
}