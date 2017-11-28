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

import com.insyslab.tooz.R;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.models.responses.CreateProfileResponse;
import com.insyslab.tooz.models.responses.Error;
import com.insyslab.tooz.restclient.BaseResponseInterface;
import com.insyslab.tooz.ui.activities.OnboardingActivity;
import com.insyslab.tooz.utils.Validator;

import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_001;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_004;

/**
 * Created by TaNMay on 26/09/16.
 */

public class CreateProfileFragment extends BaseFragment implements BaseResponseInterface {

    public static final String TAG = "CreateProfileFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content;
    private TextInputEditText tietName, tietMobileNumber;
    private ImageView ivProfilePicture, ivProceed;

    public CreateProfileFragment() {

    }

    public static CreateProfileFragment newInstance(Bundle bundle) {
        CreateProfileFragment fragment = new CreateProfileFragment();
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
        View layout = inflater.inflate(R.layout.fragment_create_profile, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        setUpViewDetails();

        return layout;
    }

    private void setUpViewDetails() {
        disableEdittext(tietMobileNumber);
        tietMobileNumber.setText("8888888888");
    }

    private void onProceedClick() {
        tietName.setError(null);
        String name = tietName.getText().toString();

        if (name == null && name.isEmpty()) {
            tietName.setError(getString(R.string.error_empty_field));
        } else if (!Validator.isValidName(name)) {
            tietName.setError("Please enter a valid name!");
        } else {
            initCreateProfileRequest();
        }
    }

    private void initCreateProfileRequest() {
        openSyncContactsFragment();
//        showProgressDialog(getString(R.string.loading));
//
//        String requestUrl = CREATE_PROFIL_REQUEST_URL;
//        JSONObject requestObject = new RequestBuilder().getCreateProfileRequestPayload(tietName.getText().toString());
//
//        if (requestObject != null) {
//            GenericDataHandler req1GenericDataHandler = new GenericDataHandler(this, getContext(), REQUEST_TYPE_004);
//            req1GenericDataHandler.jsonObjectRequest(requestObject, requestUrl, Request.Method.POST, CreateProfileResponse.class);
//        }
    }

    private void openSyncContactsFragment() {
        ((OnboardingActivity) getActivity()).openThisFragment(SyncContactsFragment.TAG, null);
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.fcp_content);
        ivProfilePicture = rootView.findViewById(R.id.fcp_profile_picture);
        tietMobileNumber = rootView.findViewById(R.id.fcp_mobile_number);
        tietName = rootView.findViewById(R.id.fcp_name);
        ivProceed = rootView.findViewById(R.id.fcp_proceed);

        tietName.requestFocus();
    }

    private void setUpActions() {
        ivProceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProceedClick();
            }
        });

        ivProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onEditProfilePictureClick();
            }
        });
    }

    private void onEditProfilePictureClick() {

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
                case REQUEST_TYPE_004:
                    onCreateProfileResponse((CreateProfileResponse) success);
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
                                        initCreateProfileRequest();
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

    private void onCreateProfileResponse(CreateProfileResponse success) {
        openSyncContactsFragment();
    }
}