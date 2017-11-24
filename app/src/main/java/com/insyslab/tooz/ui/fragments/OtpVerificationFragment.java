package com.insyslab.tooz.ui.fragments;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.insyslab.tooz.R;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.utils.AppConstants;

import static com.insyslab.tooz.utils.AppConstants.KEY_OTP_MESSAGE;
import static com.insyslab.tooz.utils.AppConstants.KEY_OTP_NUMBER;
import static com.insyslab.tooz.utils.AppConstants.KEY_OTP_SMS;
import static com.insyslab.tooz.utils.AppConstants.VAL_OTP_NUMBER;

/**
 * Created by TaNMay on 26/09/16.
 */

public class OtpVerificationFragment extends BaseFragment {

    public static final String TAG = "OtpVerifFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private TextView tvMobileNumber;
    private EditText etOtpOne, etOtpTwo, etOtpThree, etOtpFour;
    private ImageView ivProceed;

    private BroadcastReceiver smsBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase(KEY_OTP_SMS)) {
                String message = intent.getStringExtra(KEY_OTP_MESSAGE);
                String number = intent.getStringExtra(KEY_OTP_NUMBER);

                Log.d(TAG, "OTP SMS Message: " + message + " from " + number);
                if (number.equalsIgnoreCase(VAL_OTP_NUMBER)) {
                    String otpFromMessage = getOtpFromMessage(message);
                    onOtpReceived(otpFromMessage);
                }
            }
        }
    };

    public OtpVerificationFragment() {

    }

    public static OtpVerificationFragment newInstance(Bundle bundle) {
        OtpVerificationFragment fragment = new OtpVerificationFragment();
        Bundle args = new Bundle();
        args.putBundle(ARG_PARAM1, bundle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_otp_verification, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        initAutoReadSms();

        return layout;
    }

    private String getOtpFromMessage(String message) {
        String otp = "";
        int messageLength = message.length();
        otp = message.substring(messageLength - 4);
        return otp;
    }

    private void onOtpReceived(String otpFromMessage) {
        etOtpOne.setText(otpFromMessage.charAt(0) + "");
        etOtpTwo.setText(otpFromMessage.charAt(1) + "");
        etOtpThree.setText(otpFromMessage.charAt(2) + "");
        etOtpFour.setText(otpFromMessage.charAt(3) + "");
        onProceedClick();
    }

    private void onProceedClick() {
        initRequest();
    }

    private void initRequest() {

    }

    private void initAutoReadSms() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(smsBroadcastReceiver, new IntentFilter("otp"));
    }

    private void initView(View rootView) {
        tvMobileNumber = rootView.findViewById(R.id.fov_mobile_number);
        etOtpOne = rootView.findViewById(R.id.fov_otp_digit_one);
        etOtpTwo = rootView.findViewById(R.id.fov_otp_digit_two);
        etOtpThree = rootView.findViewById(R.id.fov_otp_digit_three);
        etOtpFour = rootView.findViewById(R.id.fov_otp_digit_four);
        ivProceed = rootView.findViewById(R.id.fov_proceed);
    }

    private void setUpActions() {
        ivProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProceedClick();
            }
        });

        setUpOtpInputSection();
    }

    private void setUpOtpInputSection() {
        setUpTextChangeListener(etOtpOne, etOtpTwo, null);
        setUpTextChangeListener(etOtpTwo, etOtpThree, etOtpOne);
        setUpTextChangeListener(etOtpThree, etOtpFour, etOtpTwo);
        setUpTextChangeListener(etOtpFour, null, etOtpThree);
    }

    private void setUpTextChangeListener(final EditText currentEditText, final EditText nextEditText, final EditText previousEditText) {
        currentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (nextEditText != null && !isEditTextEmpty(currentEditText)) {
                    nextEditText.setText("");
                    nextEditText.requestFocus();
                } else if (nextEditText == null) onProceedClick();
                else Log.d(TAG, "Some OTP input error!");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        currentEditText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_DEL) {
                    if (previousEditText != null && isEditTextEmpty(currentEditText))
                        previousEditText.requestFocus();
                }
                return false;
            }
        });
    }

    private boolean isEditTextEmpty(EditText editText) {
        if (editText.getText().toString().trim().isEmpty()) return true;
        return false;
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