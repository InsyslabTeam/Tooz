package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.reflect.TypeToken;
import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnBlockedContactsClickListener;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.eventbus.FragmentState;
import com.insyslab.tooz.models.responses.BlockUserResponse;
import com.insyslab.tooz.models.responses.Error;
import com.insyslab.tooz.restclient.BaseResponseInterface;
import com.insyslab.tooz.restclient.GenericDataHandler;
import com.insyslab.tooz.restclient.RequestBuilder;
import com.insyslab.tooz.ui.activities.SettingsActivity;
import com.insyslab.tooz.ui.adapters.BlockedContactsAdapter;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.List;

import static com.insyslab.tooz.utils.ConstantClass.BLOCK_CONTACT_REQUEST_URL;
import static com.insyslab.tooz.utils.ConstantClass.GET_BLOCKED_CONTACTS_REQUEST_URL;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_018;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_019;

/**
 * Created by TaNMay on 26/09/16.
 */

public class BlockedContactsFragment extends BaseFragment implements OnBlockedContactsClickListener, BaseResponseInterface {

    public static final String TAG = "BlockedFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content;
    private TextView tvCount;
    private RecyclerView blockedContactsRv;

    private RecyclerView.Adapter blockedContactsAdapter;

    private List<User> contactItems;
    private int effectedIndex = -1;

    public BlockedContactsFragment() {

    }

    public static BlockedContactsFragment newInstance(Bundle bundle) {
        BlockedContactsFragment fragment = new BlockedContactsFragment();
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
        View layout = inflater.inflate(R.layout.fragment_blocked_contacts, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        initGetBlockedContactsRequest();

        return layout;
    }

    private void initGetBlockedContactsRequest() {
        showProgressDialog(getString(R.string.loading));
        String requestUrl = GET_BLOCKED_CONTACTS_REQUEST_URL;
        Type responseType = new TypeToken<List<User>>() {
        }.getType();

        GenericDataHandler req1GenericDataHandler = new GenericDataHandler(this, getContext(), REQUEST_TYPE_019);
        req1GenericDataHandler.jsonArrayRequest(requestUrl, responseType);
    }

    private void setUpBlockedContactsRv() {
        tvCount.setText(contactItems.size() + " blocked contacts.");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        blockedContactsAdapter = new BlockedContactsAdapter(this, contactItems);
        blockedContactsRv.setLayoutManager(layoutManager);
        blockedContactsRv.setAdapter(blockedContactsAdapter);
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.fbc_content);
        tvCount = rootView.findViewById(R.id.fbc_count);
        blockedContactsRv = rootView.findViewById(R.id.fbc_blocked_contacts);
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

    @Override
    public void onUnblockClick(int position) {
        effectedIndex = position;
        updateLocalList(position);
        initBlockUserRequest(contactItems.get(position));
    }

    private void initBlockUserRequest(User user) {
        showProgressDialog(getString(R.string.loading));

        String requestUrl = BLOCK_CONTACT_REQUEST_URL;
        JSONObject requestObject = new RequestBuilder().getFeedbackRequestPayload(user.getId());

        if (requestObject != null) {
            GenericDataHandler req2GenericDataHandler = new GenericDataHandler(this, getContext(), REQUEST_TYPE_018);
            req2GenericDataHandler.jsonObjectRequest(requestObject, requestUrl, Request.Method.POST, BlockUserResponse.class);
        }
    }

    private void updateLocalList(int pos) {
        contactItems.get(pos).setBlocked(!contactItems.get(pos).isBlocked());
        blockedContactsAdapter.notifyItemChanged(pos);
    }

    public void onSaveClick() {
        closeThisFragment();
    }

    private void closeThisFragment() {
        ((SettingsActivity) getActivity()).closeCurrentFragment();
    }

    @Override
    public void onResponse(Object success, Object error, final int requestCode) {
        hideProgressDialog();
        if (error == null) {
            switch (requestCode) {
                case REQUEST_TYPE_019:
                    onGetBlockedContactsResponse((List<User>) success);
                    break;
                case REQUEST_TYPE_018:
                    onBlockContactResponse((BlockUserResponse) success);
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
                                    case REQUEST_TYPE_019:
                                        initGetBlockedContactsRequest();
                                        break;
                                    case REQUEST_TYPE_018:
                                        updateLocalList(effectedIndex);
                                        break;
                                    default:
                                        showToastMessage(getString(R.string.error_unknown), false);
                                }
                            }
                        });
            } else {
                showSnackbarMessage(content, customError.getMessage(), true, getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        closeThisFragment();
                    }
                }, false);

            }
        }

    }

    private void onBlockContactResponse(BlockUserResponse success) {

    }

    private void onGetBlockedContactsResponse(List<User> success) {
        contactItems = success;
        setUpBlockedContactsRv();
    }
}