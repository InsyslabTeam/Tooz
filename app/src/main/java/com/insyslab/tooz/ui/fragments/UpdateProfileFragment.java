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
import com.insyslab.tooz.ui.activities.SettingsActivity;
import com.insyslab.tooz.utils.Validator;

/**
 * Created by TaNMay on 26/09/16.
 */

public class UpdateProfileFragment extends BaseFragment {

    public static final String TAG = "UpdateProfileFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content;
    private ImageView ivProfilePicture;
    private TextInputEditText tietName, tietNumber;

    public UpdateProfileFragment() {

    }

    public static UpdateProfileFragment newInstance(Bundle bundle) {
        UpdateProfileFragment fragment = new UpdateProfileFragment();
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
        View layout = inflater.inflate(R.layout.fragment_update_profile, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        setUpProfileDetails();

        return layout;
    }

    private void setUpProfileDetails() {
        tietName.setText("Developer Name");
        tietNumber.setText("8888888888");
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.fup_content);
        tietName = rootView.findViewById(R.id.fup_name);
        tietNumber = rootView.findViewById(R.id.fup_mobile_number);
        ivProfilePicture = rootView.findViewById(R.id.fup_profile_picture);

        disableEdittext(tietNumber);
    }

    private void setUpActions() {
        ivProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProfilePictureClick();
            }
        });
    }

    private void onProfilePictureClick() {

    }

    public void onSaveClick() {
        tietName.setError(null);
        tietNumber.setError(null);

        String name = tietName.getText().toString();
        String number = tietNumber.getText().toString();

        if (name != null && name.isEmpty()) {
            tietName.setError(getString(R.string.error_empty_field));
        } else if (!Validator.isValidName(name)) {
            tietName.setError(getString(R.string.error_invalid_name));
        } else if (number != null && number.isEmpty()) {
            tietNumber.setError(getString(R.string.error_empty_field));
        } else if (!Validator.isValidMobileNumber(number)) {
            tietNumber.setError(getString(R.string.error_invalid_mobile_number));
        } else {
            closeThisFragment();
        }
    }

    private void closeThisFragment() {
        ((SettingsActivity) getActivity()).closeCurrentFragment();
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
}