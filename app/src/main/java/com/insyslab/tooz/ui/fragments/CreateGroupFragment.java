package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnRuntimePermissionsResultListener;
import com.insyslab.tooz.models.ContactItem;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.ui.activities.ActionsActivity;
import com.insyslab.tooz.ui.activities.BaseActivity;
import com.insyslab.tooz.ui.customui.CircleTransform;
import com.insyslab.tooz.utils.Util;
import com.insyslab.tooz.utils.Validator;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.IOException;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;

/**
 * Created by TaNMay on 26/09/16.
 */

public class CreateGroupFragment extends BaseFragment implements OnRuntimePermissionsResultListener {

    public static final String TAG = "CreateGroupFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content;
    private ImageView ivProfilePicture;
    private TextInputEditText tietName, tietMembers;

    private Bitmap profilePictureSelected = null;
    private List<ContactItem> selectedMembers = null;

    public CreateGroupFragment() {

    }

    public static CreateGroupFragment newInstance(Bundle bundle) {
        CreateGroupFragment fragment = new CreateGroupFragment();
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
        ActionsActivity.onRuntimePermissionsResultListener = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_create_group, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        return layout;
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.fcg_content);
        tietName = rootView.findViewById(R.id.fcg_name);
        tietMembers = rootView.findViewById(R.id.fcg_group_members);
        ivProfilePicture = rootView.findViewById(R.id.fcg_profile_picture);

        clickableEdittext(tietMembers);
    }

    private void setUpActions() {
        ivProfilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProfilePictureClick();
            }
        });

        tietMembers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSelectGroupMembersClick();
            }
        });
    }

    private void onSelectGroupMembersClick() {

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
        tietMembers.setError(null);

        String name = tietName.getText().toString();

        if (name != null && name.isEmpty()) {
            tietName.setError(getString(R.string.error_empty_field));
        } else if (!Validator.isValidName(name)) {
            tietName.setError(getString(R.string.error_invalid_name));
        } else if (selectedMembers == null) {
            showToastMessage("Please select a few members for your group!", false);
        } else if (selectedMembers.size() == 0) {
            showToastMessage("Please select a few members for your group!", false);
        } else {
            closeThisFragment();
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                Uri resultUri = result.getUri();

                try {
                    profilePictureSelected = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), resultUri);
                } catch (IOException e) {
                    profilePictureSelected = null;
                    e.printStackTrace();
                }

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

    public void onMembersSelected(List<ContactItem> contactItemList) {
        selectedMembers = contactItemList;
    }
}