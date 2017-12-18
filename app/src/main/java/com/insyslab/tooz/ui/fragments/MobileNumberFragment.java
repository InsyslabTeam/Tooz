package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnRuntimePermissionsResultListener;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.responses.Error;
import com.insyslab.tooz.restclient.BaseResponseInterface;
import com.insyslab.tooz.restclient.GenericDataHandler;
import com.insyslab.tooz.restclient.RequestBuilder;
import com.insyslab.tooz.ui.activities.BaseActivity;
import com.insyslab.tooz.ui.activities.OnboardingActivity;
import com.insyslab.tooz.utils.LocalStorage;
import com.insyslab.tooz.utils.Util;
import com.insyslab.tooz.utils.Validator;

import org.json.JSONObject;

import static android.Manifest.permission.READ_SMS;
import static android.Manifest.permission.RECEIVE_SMS;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_001;
import static com.insyslab.tooz.utils.ConstantClass.SIGN_IN_REQUEST_URL;

/**
 * Created by TaNMay on 26/09/16.
 */

public class MobileNumberFragment extends BaseFragment implements BaseResponseInterface,
        OnRuntimePermissionsResultListener {

    public static final String TAG = "MobileNumberFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content;
    private TextInputEditText tietMobileNumber;
    private ImageView ivProceed;

    public MobileNumberFragment() {

    }

    public static MobileNumberFragment newInstance() {
        MobileNumberFragment fragment = new MobileNumberFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
        OnboardingActivity.onRuntimePermissionsResultListener = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_mobile_number, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        return layout;
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.fmn_content);
        tietMobileNumber = rootView.findViewById(R.id.fmn_mobile_number);
        ivProceed = rootView.findViewById(R.id.fmn_proceed);
    }

    private void setUpActions() {
        ivProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProceedClick();
            }
        });
    }

    private void onProceedClick() {
        tietMobileNumber.setError(null);
        String mobileNumber = tietMobileNumber.getText().toString();

        if (mobileNumber == null && mobileNumber.isEmpty()) {
            tietMobileNumber.setError(getString(R.string.error_empty_field));
        } else if (!Validator.isValidMobileNumber(mobileNumber)) {
            tietMobileNumber.setError(getString(R.string.error_invalid_mobile_number));
        } else {
            if (!verifySmsPermissions()) {
                initRuntimePermissions();
            } else {
                initSignInRequest();
            }
        }
    }

    private void initSignInRequest() {
        showProgressDialog(getString(R.string.loading));

        String requestUrl = SIGN_IN_REQUEST_URL;
        JSONObject requestObject = new RequestBuilder().getSignInRequestPayload(tietMobileNumber.getText().toString());

        if (requestObject != null) {
            GenericDataHandler req1GenericDataHandler = new GenericDataHandler(this, getContext(), REQUEST_TYPE_001);
            req1GenericDataHandler.jsonObjectRequest(requestObject, requestUrl, Request.Method.POST, User.class);
        }
    }

    private boolean verifySmsPermissions() {
        if (Util.verifyPermission(getContext(), RECEIVE_SMS)
                && Util.verifyPermission(getContext(), READ_SMS))
            return true;
        else return false;
    }

    private void initRuntimePermissions() {
        ((BaseActivity) getActivity()).requestSmsPermissions();
    }

    private void openOtpVerificationFragment() {
        ((OnboardingActivity) getActivity()).openThisFragment(OtpVerificationFragment.TAG, null);
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
    public void onSmsPermissionsResult(boolean granted) {
        initSignInRequest();
    }

    @Override
    public void onContactsPermissionsResult(boolean granted) {

    }

    @Override
    public void onStoragePermissionsResult(boolean granted) {

    }

    @Override
    public void onLocationPermissionsResult(boolean granted) {

    }

    @Override
    public void onResponse(Object success, Object error, final int requestCode) {
        hideProgressDialog();
        if (error == null) {
            switch (requestCode) {
                case REQUEST_TYPE_001:
                    onSignInResponse((User) success);
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
                                    case REQUEST_TYPE_001:
                                        initSignInRequest();
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

    private void onSignInResponse(User success) {
        LocalStorage.getInstance(getContext()).setUser(success);
        openOtpVerificationFragment();
    }
}