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

import com.android.volley.Request;
import com.insyslab.tooz.R;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.responses.Error;
import com.insyslab.tooz.restclient.BaseResponseInterface;
import com.insyslab.tooz.restclient.GenericDataHandler;
import com.insyslab.tooz.restclient.RequestBuilder;
import com.insyslab.tooz.ui.activities.OnboardingActivity;
import com.insyslab.tooz.utils.LocalStorage;
import com.insyslab.tooz.utils.Util;

import org.json.JSONObject;

import static com.insyslab.tooz.utils.AppConstants.KEY_OTP_MESSAGE;
import static com.insyslab.tooz.utils.AppConstants.KEY_OTP_NUMBER;
import static com.insyslab.tooz.utils.AppConstants.KEY_OTP_SMS;
import static com.insyslab.tooz.utils.AppConstants.VAL_OTP_NUMBER;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_002;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_003;
import static com.insyslab.tooz.utils.ConstantClass.RESEND_OTP_REQUEST_URL;
import static com.insyslab.tooz.utils.ConstantClass.VERIFY_OTP_REQUEST_URL;

/**
 * Created by TaNMay on 26/09/16.
 */

public class OtpVerificationFragment extends BaseFragment implements BaseResponseInterface {

    public static final String TAG = "OtpVerifFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content;
    private TextView tvMobileNumber, tvResendOtp;
    private EditText etOtpOne, etOtpTwo, etOtpThree, etOtpFour;
    private ImageView ivProceed;

    private User user;

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
            Bundle bundle = getArguments().getBundle(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_otp_verification, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        user = LocalStorage.getInstance(getContext()).getUser();

        initAutoReadSms();
        setUpViewDetails();

        return layout;
    }

    private void setUpViewDetails() {
        tvMobileNumber.setText(Util.getFormattedMobileNumber(user.getMobile()));
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
        String otp = getInputOtp();
        if (otp.length() == 4) {
            initVerifyOtpRequest(otp);
        } else {
            showSnackbarMessage(content, "Please enter a valid OTP!", true, getString(R.string.ok), null, true);
        }
    }

    private String getInputOtp() {
        String otp = "";
        otp += etOtpOne.getText().toString();
        otp += etOtpTwo.getText().toString();
        otp += etOtpThree.getText().toString();
        otp += etOtpFour.getText().toString();

        return otp;
    }

    private void initVerifyOtpRequest(String otp) {
        showProgressDialog(getString(R.string.loading));

        String requestUrl = VERIFY_OTP_REQUEST_URL;
        JSONObject requestObject = new RequestBuilder().getVerifyOtpRequestPayload(user.getMobile(), otp);

        if (requestObject != null) {
            GenericDataHandler req1GenericDataHandler = new GenericDataHandler(this, getContext(), REQUEST_TYPE_002);
            req1GenericDataHandler.jsonObjectRequest(requestObject, requestUrl, Request.Method.POST, User.class);
        }
    }

    private void initAutoReadSms() {
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(smsBroadcastReceiver, new IntentFilter("otp"));
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.fov_content);
        tvMobileNumber = rootView.findViewById(R.id.fov_mobile_number);
        tvResendOtp = rootView.findViewById(R.id.fov_resend_otp);
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

        tvResendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResendOtpClick();
            }
        });

        setUpOtpInputSection();
    }

    private void onResendOtpClick() {
        initResendOtpRequest();
    }

    private void initResendOtpRequest() {
        showProgressDialog(getString(R.string.loading));

        String requestUrl = RESEND_OTP_REQUEST_URL;
        JSONObject requestObject = new RequestBuilder().getResendOtpRequestPayload(user.getMobile());

        if (requestObject != null) {
            GenericDataHandler req1GenericDataHandler = new GenericDataHandler(this, getContext(), REQUEST_TYPE_003);
            req1GenericDataHandler.jsonObjectRequest(requestObject, requestUrl, Request.Method.POST, User.class);
        }
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
                } else if (nextEditText == null) {
                    if (getInputOtp().length() == 4)
                        onProceedClick();
                } else Log.d(TAG, "Some OTP input error!");
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

    @Override
    public void onResponse(Object success, Object error, final int requestCode) {
        hideProgressDialog();
        if (error == null) {
            switch (requestCode) {
                case REQUEST_TYPE_002:
                    onVerifyOtpResponse((User) success);
                    break;
                case REQUEST_TYPE_003:
                    onResendOtpResponse((User) success);
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
                                    case REQUEST_TYPE_002:
                                        initVerifyOtpRequest(getInputOtp());
                                        break;
                                    case REQUEST_TYPE_003:
                                        initResendOtpRequest();
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

    private void onResendOtpResponse(User success) {
        user = success;
        LocalStorage.getInstance(getContext()).setUser(success);
    }

    private void onVerifyOtpResponse(User success) {
        LocalStorage.getInstance(getContext()).setUser(success);
        openCreateProfileFragment();
    }

    private void openCreateProfileFragment() {
        ((OnboardingActivity) getActivity()).openThisFragment(CreateProfileFragment.TAG, null);
    }
}