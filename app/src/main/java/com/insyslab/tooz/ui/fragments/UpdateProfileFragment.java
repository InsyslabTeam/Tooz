package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnRuntimePermissionsResultListener;
import com.insyslab.tooz.models.eventbus.FragmentState;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.responses.Error;
import com.insyslab.tooz.restclient.BaseResponseInterface;
import com.insyslab.tooz.restclient.GenericDataHandler;
import com.insyslab.tooz.restclient.RequestBuilder;
import com.insyslab.tooz.restclient.VolleyMultipartRequest;
import com.insyslab.tooz.ui.activities.BaseActivity;
import com.insyslab.tooz.ui.activities.SettingsActivity;
import com.insyslab.tooz.ui.customui.CircleTransform;
import com.insyslab.tooz.utils.LocalStorage;
import com.insyslab.tooz.utils.Util;
import com.insyslab.tooz.utils.Validator;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONObject;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static com.insyslab.tooz.utils.ConstantClass.CREATE_PROFILE_REQUEST_URL;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_004;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_008;
import static com.insyslab.tooz.utils.ConstantClass.UPDATE_PROFILE_PICTURE_REQUEST_URL;
import static com.insyslab.tooz.utils.MultipartFileHelper.convertStreamToByteArray;

/**
 * Created by TaNMay on 26/09/16.
 */

public class UpdateProfileFragment extends BaseFragment implements BaseResponseInterface,
        OnRuntimePermissionsResultListener {

    public static final String TAG = "UpdateProfileFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content;
    private ImageView ivProfilePicture;
    private TextInputEditText tietName, tietNumber;
    private TextView tvProfilePicHint;

    private User user;

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
        SettingsActivity.onRuntimePermissionsResultListener = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_update_profile, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        user = LocalStorage.getInstance(getContext()).getUser();

        setUpProfileDetails();

        return layout;
    }

    private void setUpProfileDetails() {
        disableEdittext(tietNumber);
        tietNumber.setText(user.getMobile());

        if (user.getName() != null && !user.getName().isEmpty())
            tietName.setText(user.getName());

        if (user.getProfileImage() != null && !user.getProfileImage().isEmpty()) {
            setImageInImageView(user.getProfileImage());
            tvProfilePicHint.setText("");
        } else {
            tvProfilePicHint.setText(getString(R.string.select_a_profile_picture));
        }
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.fup_content);
        tietName = rootView.findViewById(R.id.fup_name);
        tietNumber = rootView.findViewById(R.id.fup_mobile_number);
        ivProfilePicture = rootView.findViewById(R.id.fup_profile_picture);
        tvProfilePicHint = rootView.findViewById(R.id.fup_profile_picture_hint);

        disableEdittext(tietNumber);
    }

    private void setImageInImageView(String url) {
        Picasso.with(getContext())
                .load(url)
                .placeholder(R.drawable.ic_user)
                .error(R.drawable.ic_default_user)
                .resizeDimen(R.dimen.create_profile_picture, R.dimen.create_profile_picture)
                .centerCrop()
                .transform(new CircleTransform())
                .into(ivProfilePicture);
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
        if (!verifyStoragePermissions()) {
            initRuntimeStoragePermissions();
        } else {
            initImageSelector();
        }
    }

    private boolean verifyStoragePermissions() {
        if (Util.verifyPermission(getContext(), READ_EXTERNAL_STORAGE)
                && Util.verifyPermission(getContext(), WRITE_EXTERNAL_STORAGE))
            return true;
        else return false;
    }

    private void initRuntimeStoragePermissions() {
        ((BaseActivity) getActivity()).requestStoragePermissions();
    }

    private void initImageSelector() {
        CropImage.activity()
                .setAspectRatio(1, 1)
                .start(getContext(), this);
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
            initUpdateProfileRequest();
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

    @Override
    public void onSmsPermissionsResult(boolean granted) {

    }

    @Override
    public void onContactsPermissionsResult(boolean granted) {

    }

    @Override
    public void onStoragePermissionsResult(boolean granted) {
        if (granted) {
            initImageSelector();
        } else {
            showSnackbarMessage(
                    content,
                    "Please enable storage permissions to add a picture!",
                    true,
                    getString(R.string.ok), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onProfilePictureClick();
                        }
                    },
                    false
            );
        }
    }

    @Override
    public void onLocationPermissionsResult(boolean granted) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();
                setImageInImageView(resultUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.d(TAG, "Error occurred: " + error.getMessage());
            }
        }
    }

    private void setImageInImageView(Uri resultUri) {
        Picasso.with(getContext())
                .load(resultUri)
                .placeholder(R.drawable.ic_default_user)
                .error(R.drawable.ic_default_user)
                .resizeDimen(R.dimen.create_profile_picture, R.dimen.create_profile_picture)
                .centerCrop()
                .transform(new CircleTransform())
                .into(ivProfilePicture);

        initUploadProfilePicture(resultUri);
    }

    private void initUploadProfilePicture(Uri uri) {
        showProgressDialog(getString(R.string.loading));

        String requestUrl = UPDATE_PROFILE_PICTURE_REQUEST_URL;
        Map<String, VolleyMultipartRequest.DataPart> partMap = getByteDataParams(uri);
        Map<String, String> paramsMap = null;

        GenericDataHandler req1GenericDataHandler = new GenericDataHandler(this, getContext(), REQUEST_TYPE_008);
        req1GenericDataHandler.multipartRequest(requestUrl, Request.Method.POST, partMap, paramsMap, User.class);
    }

    private Map<String, VolleyMultipartRequest.DataPart> getByteDataParams(Uri uri) {
        Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
        InputStream inputStream = null;
        String imageName = user.getMobile() + "_" + System.currentTimeMillis() + ".jpg";

        try {
            inputStream = getContext().getContentResolver().openInputStream(uri);
            byte[] byteFile = convertStreamToByteArray(inputStream);
            params.put("media", new VolleyMultipartRequest.DataPart(imageName, byteFile, "image/jpeg"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return params;
    }

    @Override
    public void onResponse(Object success, Object error, final int requestCode) {
        hideProgressDialog();
        if (error == null) {
            switch (requestCode) {
                case REQUEST_TYPE_004:
                    onUpdateProfileResponse((User) success);
                    break;
                case REQUEST_TYPE_008:
                    onUpdateProfilePictureResponse((String) success);
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
                                    case REQUEST_TYPE_004:
                                        initUpdateProfileRequest();
                                        break;
                                    case REQUEST_TYPE_008:
                                        initImageSelector();
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

    private void onUpdateProfilePictureResponse(String success) {
        Log.d(TAG, "onUpdateProfilePictureResponse: " + success);
    }

    private void onUpdateProfilePictureResponse(User success) {
        user = success;
        LocalStorage.getInstance(getContext()).setUser(success);
    }

    private void onUpdateProfileResponse(User success) {
        user = success;
        LocalStorage.getInstance(getContext()).setUser(success);
        closeThisFragment();
    }

    private void initUpdateProfileRequest() {
        showProgressDialog(getString(R.string.loading));

        String requestUrl = CREATE_PROFILE_REQUEST_URL;
        JSONObject requestObject = new RequestBuilder().getCreateProfileRequestPayload(tietName.getText().toString(), user);

        if (requestObject != null) {
            GenericDataHandler req1GenericDataHandler = new GenericDataHandler(this, getContext(), REQUEST_TYPE_004);
            req1GenericDataHandler.jsonObjectRequest(requestObject, requestUrl, Request.Method.POST, User.class);
        }
    }

}