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
import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnUserFetchedListener;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.UserGroup;
import com.insyslab.tooz.models.eventbus.FragmentState;
import com.insyslab.tooz.models.eventbus.ReminderCreated;
import com.insyslab.tooz.models.responses.CreateReminderResponse;
import com.insyslab.tooz.models.responses.Error;
import com.insyslab.tooz.restclient.BaseResponseInterface;
import com.insyslab.tooz.restclient.GenericDataHandler;
import com.insyslab.tooz.restclient.RequestBuilder;
import com.insyslab.tooz.ui.activities.ActionsActivity;
import com.insyslab.tooz.utils.LocalStorage;
import com.insyslab.tooz.utils.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.greenrobot.event.EventBus;

import static com.insyslab.tooz.utils.AppConstants.KEY_FROM_FRAGMENT;
import static com.insyslab.tooz.utils.AppConstants.KEY_FROM_FRAGMENT_DETAIL;
import static com.insyslab.tooz.utils.AppConstants.KEY_GET_SELECTED_CONTACT_ID;
import static com.insyslab.tooz.utils.AppConstants.KEY_GET_SELECTED_GROUP_ID;
import static com.insyslab.tooz.utils.AppConstants.KEY_SET_REMINDER_TYPE;
import static com.insyslab.tooz.utils.AppConstants.KEY_TO_CONTACTS_SELECTOR_BUNDLE;
import static com.insyslab.tooz.utils.AppConstants.VAL_SEND_REMINDER;
import static com.insyslab.tooz.utils.AppConstants.VAL_SET_PERSONAL_REMINDER;
import static com.insyslab.tooz.utils.ConstantClass.CREATE_REMINDER_REQUEST_URL;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_009;
import static com.insyslab.tooz.utils.Util.getAmPmFromIndex;
import static com.insyslab.tooz.utils.Util.getDateExtension;
import static com.insyslab.tooz.utils.Util.getDateInDefaultDateFormat;
import static com.insyslab.tooz.utils.Util.getDayOfWeekFromIndex;
import static com.insyslab.tooz.utils.Util.getFormattedHourOrMinute;
import static com.insyslab.tooz.utils.Util.getMonthFromIndex;

/**
 * Created by TaNMay on 26/09/16.
 */

public class SetReminderFragment extends BaseFragment implements BaseResponseInterface, OnUserFetchedListener {

    public static final String TAG = "SetReminderFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private LinearLayout content, optionsSec;
    private TextInputEditText tietTask, tietTime, tietLocation, tietContact;
    private TextInputLayout tilTask, tilTime, tilLocation, tilContact;
    private ImageView ivOptionTime, ivOptionLocation;

    private String fragmentType = null;
    private String timeSelected = null, addressSelected = null;
    private LatLng latLngSelected = null;
    private Boolean isTimeSelected = true;
    private List<User> selectedMembers = null;
    private List<UserGroup> selectedGroups = null;
    private User user;
    private String selectedUserId = null, selectedGroupId = null;

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
            selectedUserId = bundle.getString(KEY_GET_SELECTED_CONTACT_ID);
            selectedGroupId = bundle.getString(KEY_GET_SELECTED_GROUP_ID);
        }
        ActionsActivity.onUserFetchedListener = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_set_reminder, container, false);

        FragmentState fragmentState = new FragmentState(TAG);
        fragmentState.setFragmentDetailedName(fragmentType);
        updateFragment(fragmentState);
        initView(layout);

        user = LocalStorage.getInstance(getContext()).getUser();

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

        ivOptionLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onLocationOptionClick();
            }
        });

        ivOptionTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onTimeOptionClick();
            }
        });
    }

    private void onTimeOptionClick() {
        isTimeSelected = true;
        updateSelectionView();
    }

    private void onLocationOptionClick() {
        isTimeSelected = false;
        updateSelectionView();
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
        bundle.putString(KEY_FROM_FRAGMENT_DETAIL, fragmentType);
        bundle.putSerializable(KEY_TO_CONTACTS_SELECTOR_BUNDLE, (ArrayList<User>) selectedMembers);
        ((ActionsActivity) getActivity()).openThisFragment(SelectContactsFragment.TAG, bundle);
    }

    private void onLocationClick() {
        Util.hideSoftKeyboard(getActivity());
        openLocationSelectorFragment();
    }

    private void openLocationSelectorFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SET_REMINDER_TYPE, fragmentType);
        bundle.putString(KEY_FROM_FRAGMENT_DETAIL, fragmentType);
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

        if (selectedUserId != null) {
            setUpSelectedContacts();
        } else if (selectedGroupId != null) {
            setUpSelectedGroups();
        }
    }

    private void setUpSelectedGroups() {
        ((ActionsActivity) getActivity()).getGroupFromId(selectedGroupId);
    }

    private void setUpSelectedContacts() {
        ((ActionsActivity) getActivity()).getUserFromId(selectedUserId);
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
            initCreateReminderRequest();
        }
    }

    private void initCreateReminderRequest() {
        showProgressDialog(getString(R.string.loading));

        String requestUrl = CREATE_REMINDER_REQUEST_URL;
        JSONObject requestObject = new RequestBuilder().getCreateReminderRequestPayload(
                fragmentType,
                tietTask.getText().toString().trim(),
                isTimeSelected ? timeSelected : null,
                !isTimeSelected ? latLngSelected : null,
                user.getId(),
                selectedMembers
        );

        if (requestObject != null) {
            GenericDataHandler req1GenericDataHandler = new GenericDataHandler(this, getContext(), REQUEST_TYPE_009);
            req1GenericDataHandler.jsonObjectRequest(requestObject, requestUrl, Request.Method.POST, CreateReminderResponse.class);
        }
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
        String contactListStr = "";
        for (int i = 0; i < selectedMembers.size(); i++) {
            contactListStr += selectedMembers.get(i).getName();
            if (i != selectedMembers.size() - 1) contactListStr += ", ";
        }
        tietContact.setText(contactListStr);
    }

    public void onGroupsSelected(List<UserGroup> contactItemList) {
        selectedGroups = contactItemList;
        String contactListStr = "";
        for (int i = 0; i < selectedGroups.size(); i++) {
            contactListStr += selectedGroups.get(i).getName();
            if (i != selectedGroups.size() - 1) contactListStr += ", ";
        }
        tietContact.setText(contactListStr);
    }

    @Override
    public void onResponse(Object success, Object error, final int requestCode) {
        hideProgressDialog();
        if (error == null) {
            switch (requestCode) {
                case REQUEST_TYPE_009:
                    onCreateReminderResponse((CreateReminderResponse) success);
                    break;
                default:
                    showToastMessage("ERROR " + requestCode + "!", false);
                    break;
            }
        } else {
            Error customError = (Error) error;
            Log.d(TAG, "Error: " + customError.getMessage() + " -- " + customError.getStatus() + " -- ");
            if (customError.getStatus() == 000) {
                hideProgressDialog();
                showNetworkErrorSnackbar(content, getString(R.string.error_no_internet), getString(R.string.retry),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (requestCode) {
                                    case REQUEST_TYPE_009:
                                        initCreateReminderRequest();
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

    private void onCreateReminderResponse(CreateReminderResponse success) {
        EventBus.getDefault().postSticky(new ReminderCreated(true, !fragmentType.equals(VAL_SEND_REMINDER)));
        if (fragmentType.equals(VAL_SEND_REMINDER))
            showToastMessage("Reminder sent to " + selectedMembers.size() + " contact(s)!", true);
        else showToastMessage("Personal reminder created successfully!", true);
        closeThisFragment();
    }

    @Override
    public void onUserFetched(User user) {
        List<User> list = new ArrayList<>();
        list.add(user);
        onMembersSelected(list);
    }

    @Override
    public void onGroupFetched(UserGroup group) {
        List<UserGroup> list = new ArrayList<>();
        list.add(group);
        onGroupsSelected(list);
    }
}