package com.insyslab.tooz.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.insyslab.tooz.R;
import com.insyslab.tooz.models.eventbus.FragmentState;
import com.insyslab.tooz.ui.activities.SettingsActivity;
import com.insyslab.tooz.utils.Util;

public class PreferencesFragment extends BaseFragment {

    public static final String TAG = PreferencesFragment.class.getSimpleName() + " ==>";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private final int LOCATION_BOUNDS_LIMIT = 5000;
    private final int ADVANCE_ALERT_TIME_LIMIT = 120;
    private final int SNOOZE_TIME_LIMIT = 120;

    private EditText etLocationBounds, etAdvanceTimeAlert, etSnoozeTime;
    private TextView tvLocationBoundsHint, tvAdvanceTimeAlertHint, tvSnoozeTimeHint;

    private Context context;

    public PreferencesFragment() {

    }

    public static PreferencesFragment newInstance(Bundle bundle) {
        PreferencesFragment fragment = new PreferencesFragment();
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
        View layout = inflater.inflate(R.layout.fragment_preferences, container, false);

        context = getContext();
        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        setUpViewDetails();

        return layout;
    }

    @SuppressLint("SetTextI18n")
    private void setUpViewDetails() {
        etLocationBounds.setText("1500");
        etAdvanceTimeAlert.setText("120");
        etSnoozeTime.setText("60");
    }

    private void initView(View rootView) {
        etLocationBounds = rootView.findViewById(R.id.fprf_location_bounds);
        etAdvanceTimeAlert = rootView.findViewById(R.id.fprf_advance_alert);
        etSnoozeTime = rootView.findViewById(R.id.fprf_snooze_time);

        tvLocationBoundsHint = rootView.findViewById(R.id.fprf_location_bounds_hint);
        tvAdvanceTimeAlertHint = rootView.findViewById(R.id.fprf_advance_alert_hint);
        tvSnoozeTimeHint = rootView.findViewById(R.id.fprf_snooze_time_hint);
    }

    private void setUpActions() {
        etLocationBounds.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String locBounds = etLocationBounds.getText().toString();

                if (!locBounds.isEmpty()) {
                    String hintPart2;
                    Double locBoundsDouble = Double.parseDouble(locBounds);
                    if (locBoundsDouble < 1000) {
                        hintPart2 = locBounds + " meter ";
                    } else {
                        Double locBoundsKm = locBoundsDouble / 1000;
                        hintPart2 = Util.getToTwoDecimalPlaces(locBoundsKm) + " km ";
                    }
                    String hintPart1 = "We will notify you about location based reminders when you enter a ";
                    String hintPart3 = "radius of the location.";

                    if (Integer.parseInt(locBounds) > LOCATION_BOUNDS_LIMIT) {
                        setError(tvLocationBoundsHint, "Location bounds cannot be greater than 5000 meters!");
                    } else {
                        setHint(tvLocationBoundsHint, hintPart1 + hintPart2 + hintPart3);
                    }
                }
            }
        });

        etAdvanceTimeAlert.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String advTime = etAdvanceTimeAlert.getText().toString();

                if (!advTime.isEmpty()) {
                    String hintPart2;
                    int advTimeInt = Integer.parseInt(advTime);
                    if (advTimeInt < 60) {
                        hintPart2 = advTime + " seconds ";
                    } else {
                        int minInInt = advTimeInt / 60;
                        int secInInt = advTimeInt - (minInInt * 60);
                        hintPart2 = minInInt + " hour";
                        if (secInInt == 0) {
                            hintPart2 += "s ";
                        } else {
                            hintPart2 += " " + secInInt + " minutes ";
                        }
                    }

                    String hintPart1 = "We will notify you about time based reminders ";
                    String hintPart3 = "before the time.";

                    if (Integer.parseInt(advTime) > ADVANCE_ALERT_TIME_LIMIT) {
                        setError(tvAdvanceTimeAlertHint, "Advance alert time cannot be greater than 120 minutes!");
                    } else {
                        setHint(tvAdvanceTimeAlertHint, hintPart1 + hintPart2 + hintPart3);
                    }
                }
            }
        });

        etSnoozeTime.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String snzTime = etSnoozeTime.getText().toString();

                if (!snzTime.isEmpty()) {
                    String hintPart2;
                    int snzTimeInt = Integer.parseInt(snzTime);
                    if (snzTimeInt < 60) {
                        hintPart2 = snzTime + " seconds ";
                    } else {
                        int minInInt = snzTimeInt / 60;
                        int secInInt = snzTimeInt - (minInInt * 60);
                        hintPart2 = minInInt + " hour";
                        if (secInInt == 0) {
                            hintPart2 += "s ";
                        } else {
                            hintPart2 += " " + secInInt + " minutes ";
                        }
                    }

                    String hintPart1 = "We will remind you about reminders every ";
                    String hintPart3 = "if you snooze the reminder.";

                    if (Integer.parseInt(snzTime) > SNOOZE_TIME_LIMIT) {
                        setError(tvSnoozeTimeHint, "Snooze time cannot be greater than 120 minutes!");
                    } else {
                        setHint(tvSnoozeTimeHint, hintPart1 + hintPart2 + hintPart3);
                    }
                }
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        updateFragment(new FragmentState(SettingsFragment.TAG));
        super.onDetach();
    }

    public void onSaveClick() {
        String locationBounds = etLocationBounds.getText().toString();
        String advanceTimeAlert = etAdvanceTimeAlert.getText().toString();
        String snoozeTime = etSnoozeTime.getText().toString();

        if (locationBounds.isEmpty()) {
            setError(tvLocationBoundsHint, getString(R.string.error_empty_field));
        } else if (Integer.parseInt(locationBounds) > LOCATION_BOUNDS_LIMIT) {
            setError(tvLocationBoundsHint, "Location bounds cannot be greater than 5000 meters!");
        } else if (advanceTimeAlert.isEmpty()) {
            setError(tvAdvanceTimeAlertHint, getString(R.string.error_empty_field));
        } else if (Integer.parseInt(advanceTimeAlert) > ADVANCE_ALERT_TIME_LIMIT) {
            setError(tvAdvanceTimeAlertHint, "Advance alert time cannot be greater than 120 minutes!");
        } else if (snoozeTime.isEmpty()) {
            setError(tvSnoozeTimeHint, getString(R.string.error_empty_field));
        } else if (Integer.parseInt(snoozeTime) > SNOOZE_TIME_LIMIT) {
            setError(tvSnoozeTimeHint, "Snooze time cannot be greater than 120 minutes!");
        } else {
            closeThisFragment();
        }
    }

    private void setError(TextView textView, String errorMessage) {
        textView.setText(errorMessage);
        textView.setTextColor(ContextCompat.getColor(context, R.color.error_red));
    }

    private void setHint(TextView textView, String hintMessage) {
        textView.setText(hintMessage);
        textView.setTextColor(ContextCompat.getColor(context, R.color.preferences_hint_text));
    }

    private void closeThisFragment() {
        if (getActivity() != null) {
            ((SettingsActivity) getActivity()).closeCurrentFragment();
        }
    }
}