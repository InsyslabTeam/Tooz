package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnRuntimePermissionsResultListener;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.eventbus.FragmentState;
import com.insyslab.tooz.models.eventbus.GroupCreated;
import com.insyslab.tooz.models.eventbus.ReminderCreated;
import com.insyslab.tooz.models.responses.CreateGroupResponse;
import com.insyslab.tooz.models.responses.CreateReminderResponse;
import com.insyslab.tooz.models.responses.Error;
import com.insyslab.tooz.models.responses.GroupImageUploadResponse;
import com.insyslab.tooz.restclient.BaseResponseInterface;
import com.insyslab.tooz.restclient.GenericDataHandler;
import com.insyslab.tooz.restclient.RequestBuilder;
import com.insyslab.tooz.restclient.VolleyMultipartRequest;
import com.insyslab.tooz.ui.activities.ActionsActivity;
import com.insyslab.tooz.ui.activities.BaseActivity;
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
import java.util.List;
import java.util.Map;

import de.greenrobot.event.EventBus;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.app.Activity.RESULT_OK;
import static com.insyslab.tooz.utils.AppConstants.KEY_FROM_FRAGMENT;
import static com.insyslab.tooz.utils.AppConstants.VAL_SEND_REMINDER;
import static com.insyslab.tooz.utils.ConstantClass.CREATE_GROUP_REQUEST_URL;
import static com.insyslab.tooz.utils.ConstantClass.GROUP_PICTURE_REQUEST_URL;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_004;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_008;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_013;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_017;
import static com.insyslab.tooz.utils.MultipartFileHelper.convertStreamToByteArray;

/**
 * Created by TaNMay on 26/09/16.
 */

public class CreateGroupFragment extends BaseFragment implements OnRuntimePermissionsResultListener, BaseResponseInterface {

    public static final String TAG = "CreateGroupFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content;
    private ImageView ivProfilePicture;
    private TextInputEditText tietName, tietMembers;

    private Bitmap profilePictureSelected = null;
    private List<User> selectedMembers = null;
    private String imageUrl = null, createrId;

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

        createrId = LocalStorage.getInstance(getContext()).getUser().getId();

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
        Bundle bundle = new Bundle();
        bundle.putString(KEY_FROM_FRAGMENT, TAG);
        ((ActionsActivity) getActivity()).openThisFragment(SelectContactsFragment.TAG, bundle);
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

        String name = tietName.getText().toString().trim();

        if (name != null && name.isEmpty()) {
            tietName.setError(getString(R.string.error_empty_field));
        } else if (!Validator.isValidName(name)) {
            tietName.setError(getString(R.string.error_invalid_name));
        } else if (selectedMembers == null) {
            showToastMessage("Please select a few members for your group!", false);
        } else if (selectedMembers.size() == 0) {
            showToastMessage("Please select a few members for your group!", false);
        } else {
            initCreateGroupRequest();
        }
    }

    private void initCreateGroupRequest() {
        showProgressDialog(getString(R.string.loading));

        String requestUrl = CREATE_GROUP_REQUEST_URL;
        JSONObject requestObject = new RequestBuilder().getCreateGroupRequestPayload(tietName.getText().toString(), selectedMembers, imageUrl, createrId);

        if (requestObject != null) {
            GenericDataHandler req1GenericDataHandler = new GenericDataHandler(this, getContext(), REQUEST_TYPE_013);
            req1GenericDataHandler.jsonObjectRequest(requestObject, requestUrl, Request.Method.POST, CreateGroupResponse.class);
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

        initUploadGroupPicture(resultUri);
    }

    private void initUploadGroupPicture(Uri resultUri) {
        showProgressDialog(getString(R.string.loading));

        String requestUrl = GROUP_PICTURE_REQUEST_URL;
        Map<String, VolleyMultipartRequest.DataPart> partMap = getByteDataParams(resultUri);
        Map<String, String> paramsMap = null;

        GenericDataHandler req1GenericDataHandler = new GenericDataHandler(this, getContext(), REQUEST_TYPE_017);
        req1GenericDataHandler.multipartRequest(requestUrl, Request.Method.POST, partMap, paramsMap, String.class);
    }

    private Map<String, VolleyMultipartRequest.DataPart> getByteDataParams(Uri uri) {
        Map<String, VolleyMultipartRequest.DataPart> params = new HashMap<>();
        InputStream inputStream = null;
        String imageName = LocalStorage.getInstance(getContext()).getUser().getMobile() + "_" + System.currentTimeMillis() + ".jpg";

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

    public void onMembersSelected(List<User> contactItemList) {
        selectedMembers = contactItemList;
        String memberListStr = "";
        for (int i = 0; i < selectedMembers.size(); i++) {
            memberListStr += selectedMembers.get(i).getName();
            if (i != selectedMembers.size() - 1) memberListStr += ", ";
        }

        tietMembers.setText(memberListStr);
    }

    @Override
    public void onResponse(Object success, Object error, final int requestCode) {
        hideProgressDialog();
        if (error == null) {
            switch (requestCode) {
                case REQUEST_TYPE_017:
                    onUploadGroupPictureResponse((String)success);
                    break;
                case REQUEST_TYPE_013:
                    onCreateGroupResponse((CreateGroupResponse) success);
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
                                    case REQUEST_TYPE_017:
                                        initImageSelector();
                                    case REQUEST_TYPE_013:
                                        initCreateGroupRequest();
                                        break;
                                    default:
                                        showToastMessage(getString(R.string.error_unknown), false);
                                }
                            }
                        });
            } else {
                if (requestCode == REQUEST_TYPE_017) {
                    onImageUploadError();
                }
                showSnackbarMessage(content, customError.getMessage(), true, getString(R.string.ok), null, true);
            }
        }

    }

    private void onImageUploadError() {
        ivProfilePicture.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.ic_user));
    }

    private void onUploadGroupPictureResponse(String success) {
        GroupImageUploadResponse groupImageUploadResponse = new Gson().fromJson(success, GroupImageUploadResponse.class);
        imageUrl = groupImageUploadResponse.getUrl();

    }

    private void onCreateGroupResponse(CreateGroupResponse success) {
        EventBus.getDefault().postSticky(new GroupCreated(true));
        showToastMessage(getString(R.string.group_created_successfully), false);
        closeThisFragment();
    }
}