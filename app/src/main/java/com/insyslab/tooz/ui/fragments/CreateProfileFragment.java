package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.insyslab.tooz.R;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.ui.activities.OnboardingActivity;

/**
 * Created by TaNMay on 26/09/16.
 */

public class CreateProfileFragment extends BaseFragment {

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
//        ((OnboardingActivity) getActivity()).openThisFragment();
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.fcp_content);
        ivProfilePicture = rootView.findViewById(R.id.fcp_profile_picture);
        tietMobileNumber = rootView.findViewById(R.id.fcp_mobile_number);
        tietName = rootView.findViewById(R.id.fcp_name);
        ivProceed = rootView.findViewById(R.id.fcp_proceed);
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
}