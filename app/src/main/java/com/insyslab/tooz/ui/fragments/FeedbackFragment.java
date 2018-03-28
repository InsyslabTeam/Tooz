package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.Request;
import com.insyslab.tooz.R;
import com.insyslab.tooz.models.eventbus.FragmentState;
import com.insyslab.tooz.models.responses.Error;
import com.insyslab.tooz.models.responses.SendFeedbackResponse;
import com.insyslab.tooz.restclient.BaseResponseInterface;
import com.insyslab.tooz.restclient.GenericDataHandler;
import com.insyslab.tooz.restclient.RequestBuilder;
import com.insyslab.tooz.ui.activities.SettingsActivity;

import org.json.JSONObject;

import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_021;
import static com.insyslab.tooz.utils.ConstantClass.SEND_FEEDBACK_REQUEST_URL;

/**
 * Created by TaNMay on 26/09/16.
 */

public class FeedbackFragment extends BaseFragment implements BaseResponseInterface {

    public static final String TAG = "FeedbackFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private LinearLayout content;
    private EditText etFeedbackInput;

    public FeedbackFragment() {

    }

    public static FeedbackFragment newInstance(Bundle bundle) {
        FeedbackFragment fragment = new FeedbackFragment();
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
        View layout = inflater.inflate(R.layout.fragment_feedback, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        return layout;
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.ff_content);
        etFeedbackInput = rootView.findViewById(R.id.ff_feedback_input);
    }

    private void setUpActions() {

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
        String feedbackInput = etFeedbackInput.getText().toString().trim();
        if (feedbackInput != null && feedbackInput.isEmpty()) {
            showToastMessage("You cannot submit an empty feedback!", true);
        } else {
            initSubmitFeedbackRequest();
        }
    }

    private void initSubmitFeedbackRequest() {
        showProgressDialog(getString(R.string.loading));

        String requestUrl = SEND_FEEDBACK_REQUEST_URL;
        JSONObject requestObject = new RequestBuilder().getFeedbackRequestPayload(etFeedbackInput.getText().toString().trim());

        if (requestObject != null) {
            GenericDataHandler reqGenericDataHandler = new GenericDataHandler(this, getContext(), REQUEST_TYPE_021);
            reqGenericDataHandler.jsonObjectRequest(requestObject, requestUrl, Request.Method.POST, SendFeedbackResponse.class);
        }
    }

    private void closeThisFragment() {
        ((SettingsActivity) getActivity()).closeCurrentFragment();
    }

    @Override
    public void onResponse(Object success, Object error, final int requestCode) {
        hideProgressDialog();
        if (error == null) {
            switch (requestCode) {
                case REQUEST_TYPE_021:
                    onSendFeedbackResponse((SendFeedbackResponse) success);
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
                                    case REQUEST_TYPE_021:
                                        initSubmitFeedbackRequest();
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

    private void onSendFeedbackResponse(SendFeedbackResponse success) {
        showToastMessage("Feedback submitted successfully!", false);
        closeThisFragment();
    }
}