package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnUserContactClickListener;
import com.insyslab.tooz.models.User;
import com.insyslab.tooz.models.UserGroup;
import com.insyslab.tooz.models.eventbus.FragmentState;
import com.insyslab.tooz.models.responses.BlockUserResponse;
import com.insyslab.tooz.models.responses.Error;
import com.insyslab.tooz.models.responses.InviteNonAppUserResponse;
import com.insyslab.tooz.restclient.BaseResponseInterface;
import com.insyslab.tooz.restclient.GenericDataHandler;
import com.insyslab.tooz.restclient.RequestBuilder;
import com.insyslab.tooz.ui.activities.DashboardActivity;
import com.insyslab.tooz.ui.adapters.AppUserContactsAdapter;
import com.insyslab.tooz.ui.adapters.MyGroupsAdapter;
import com.insyslab.tooz.ui.adapters.NonAppUserContactsAdapter;

import org.json.JSONObject;

import java.util.List;

import static com.insyslab.tooz.utils.AppConstants.KEY_GET_SELECTED_CONTACT_ID;
import static com.insyslab.tooz.utils.AppConstants.KEY_GET_SELECTED_GROUP_ID;
import static com.insyslab.tooz.utils.AppConstants.VAL_SEND_REMINDER;
import static com.insyslab.tooz.utils.ConstantClass.BLOCK_CONTACT_REQUEST_URL;
import static com.insyslab.tooz.utils.ConstantClass.INVITE_NON_APP_USER_REQUEST_URL;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_018;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_022;

/**
 * Created by TaNMay on 26/09/16.
 */

public class AllContactsFragment extends BaseFragment implements OnUserContactClickListener,
        BaseResponseInterface {

    public static final String TAG = "AllContactsFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content, noContentView;
    private LinearLayout noAppUserView;
    private NestedScrollView scrollContent;
    private TextView tvNcvTitle, tvNoAppUserTitle, tvGroupsTitle;
    private RecyclerView appUserContactsRv, nonAppUserContactsRv, myGroupsRv;
    private View groupsDivider;

    private RecyclerView.Adapter appUserContactsAdapter, nonAppUserContactsAdapter, myGroupsAdapter;

    private List<User> appUserContactsList, nonAppUserContactsList;
    private List<UserGroup> myGroupsList;
    private int selectedAppUserItemIndex = -1;

    public AllContactsFragment() {

    }

    public static AllContactsFragment newInstance(Bundle bundle) {
        AllContactsFragment fragment = new AllContactsFragment();
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
        View layout = inflater.inflate(R.layout.fragment_all_contacts, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        appUserContactsList = ((DashboardActivity) getActivity()).getAppUserList();
        List<User> tempList = ((DashboardActivity) getActivity()).getNonAppUserList();
        nonAppUserContactsList = tempList.subList(0, tempList.size() > 10 ? 10 : tempList.size());
        myGroupsList = ((DashboardActivity) getActivity()).getUserGroupList();

        if (contactsSynced()) {
            noContentView.setVisibility(View.GONE);
            scrollContent.setVisibility(View.VISIBLE);

            setUpAppUserContactsRv();
            setUpMyGroupsRv();
            setUpNonAppUserContactsRv();
        } else {
            scrollContent.setVisibility(View.GONE);
            noContentView.setVisibility(View.VISIBLE);

            tvNcvTitle.setText("You haven't synced your contacts yet!");
        }

        return layout;
    }

    private boolean contactsSynced() {
        if (appUserContactsList != null && nonAppUserContactsList != null) {
            if (appUserContactsList.size() > 0 || nonAppUserContactsList.size() > 0) return true;
            else return false;
        } else if (appUserContactsList == null && nonAppUserContactsList == null) {
            return false;
        } else {
            return true;
        }
    }

    private void setUpAppUserContactsRv() {
        if (appUserContactsList != null && appUserContactsList.size() > 0) {
            noAppUserView.setVisibility(View.GONE);
            appUserContactsRv.setVisibility(View.VISIBLE);

            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            appUserContactsAdapter = new AppUserContactsAdapter(this, appUserContactsList);
            appUserContactsRv.setLayoutManager(layoutManager);
            appUserContactsRv.setAdapter(appUserContactsAdapter);
        } else {
            appUserContactsRv.setVisibility(View.GONE);
            noAppUserView.setVisibility(View.VISIBLE);

            tvNoAppUserTitle.setText("None of your contacts use " + getString(R.string.app_name) + "!\nYou can invite some of them here.");
        }
    }

    private void setUpNonAppUserContactsRv() {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        nonAppUserContactsAdapter = new NonAppUserContactsAdapter(this, nonAppUserContactsList);
        nonAppUserContactsRv.setLayoutManager(layoutManager);
        nonAppUserContactsRv.setAdapter(nonAppUserContactsAdapter);
    }

    private void setUpMyGroupsRv() {
        if (myGroupsList.size() > 0) {
            showGroupsSec();
            RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
            myGroupsAdapter = new MyGroupsAdapter(this, myGroupsList);
            myGroupsRv.setLayoutManager(layoutManager);
            myGroupsRv.setAdapter(myGroupsAdapter);
        } else {
            hideGroupsSec();
        }
    }

    private void initView(View rootView) {
        appUserContactsRv = rootView.findViewById(R.id.fac_app_user_contacts_rv);
        nonAppUserContactsRv = rootView.findViewById(R.id.fac_non_app_user_contacts_rv);
        myGroupsRv = rootView.findViewById(R.id.fac_my_groups_rv);
        noContentView = rootView.findViewById(R.id.ncv_content);
        tvNcvTitle = rootView.findViewById(R.id.ncv_text);
        scrollContent = rootView.findViewById(R.id.fac_scroll_content);
        noAppUserView = rootView.findViewById(R.id.fac_no_content);
        tvNoAppUserTitle = rootView.findViewById(R.id.fac_ncv_text);
        tvGroupsTitle = rootView.findViewById(R.id.fac_groups_title);
        groupsDivider = rootView.findViewById(R.id.fac_divider_2);
    }

    private void setUpActions() {

    }

    private void showGroupsSec() {
        tvGroupsTitle.setVisibility(View.VISIBLE);
        groupsDivider.setVisibility(View.VISIBLE);
    }

    private void hideGroupsSec() {
        tvGroupsTitle.setVisibility(View.GONE);
        groupsDivider.setVisibility(View.GONE);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void updateAppUserContactsRv(List<User> list) {
        appUserContactsList.clear();
        appUserContactsList.addAll(list);
        if (appUserContactsAdapter != null) appUserContactsAdapter.notifyDataSetChanged();
        else setUpAppUserContactsRv();
    }

    public void updateNonAppUserContactsRv(List<User> list) {
        nonAppUserContactsList.clear();
        nonAppUserContactsList.addAll(list);
        if (nonAppUserContactsAdapter != null) nonAppUserContactsAdapter.notifyDataSetChanged();
        else setUpNonAppUserContactsRv();
    }

    public void updateUserGroupsRv(List<UserGroup> list) {
        myGroupsList.clear();
        myGroupsList.addAll(list);
        if (myGroupsAdapter != null) myGroupsAdapter.notifyDataSetChanged();
        else setUpMyGroupsRv();
    }

//    @Override
//    public void onAppUserContactClick(View view) {
//        int position = appUserContactsRv.getChildAdapterPosition(view);
//    }

    @Override
    public void onNonAppUserContactClick(View view) {
        int position = nonAppUserContactsRv.getChildAdapterPosition(view);
    }

    @Override
    public void onNonAppUserInviteClick(int position) {
        initNonAppUserInviteRequest(nonAppUserContactsList.get(position).getMobile());
    }

    @Override
    public void onAppUserSendReminderClick(int position) {
        openSendReminderView(appUserContactsList.get(position), null);
    }

    private void openSendReminderView(User user, UserGroup userGroup) {
        Bundle bundle = new Bundle();
        if (user != null) bundle.putString(KEY_GET_SELECTED_CONTACT_ID, user.getId());
        if (userGroup != null) bundle.putString(KEY_GET_SELECTED_GROUP_ID, userGroup.getId());
        if (getActivity() != null) {
            ((DashboardActivity) getActivity()).openActionsActivity(SetReminderFragment.TAG, VAL_SEND_REMINDER, bundle);
        }
    }

    @Override
    public void onAppUserBlockClick(int position) {
        selectedAppUserItemIndex = position;
        initBlockUserRequest(appUserContactsList.get(position));
    }

    @Override
    public void onMyGroupsSendReminderClick(int position) {
        openSendReminderView(null, myGroupsList.get(position));
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

    private void initNonAppUserInviteRequest(String mobile) {
        showProgressDialog(getString(R.string.loading));

        String requestUrl = INVITE_NON_APP_USER_REQUEST_URL;
        JSONObject requestObject = new RequestBuilder().getInviteRequestPayload(mobile);

        if (requestObject != null) {
            GenericDataHandler req1GenericDataHandler = new GenericDataHandler(this, getContext(), REQUEST_TYPE_022);
            req1GenericDataHandler.jsonObjectRequest(requestObject, requestUrl, Request.Method.POST, InviteNonAppUserResponse.class);
        }
    }

    @Override
    public void onResponse(Object success, Object error, final int requestCode) {
        hideProgressDialog();
        if (error == null) {
            switch (requestCode) {
                case REQUEST_TYPE_022:
                    onInviteNonAppUserResponse((InviteNonAppUserResponse) success);
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
//            Log.d(TAG, "Error: " + customError.getMessage() + " -- " + customError.getStatus() + " -- ");
            if (customError.getStatus() == 000) {
                hideProgressDialog();
                showNetworkErrorSnackbar(content, getString(R.string.error_no_internet), getString(R.string.retry),
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                switch (requestCode) {
                                    case REQUEST_TYPE_022:
                                        break;
                                    default:
                                        showToastMessage(getString(R.string.error_unknown), false);
                                }
                            }
                        });
            } else {
                showSnackbarMessage(getActivity().findViewById(R.id.ad_fragment_container), customError.getMessage(), true, getString(R.string.ok), null, true);

            }
        }
    }

    private void onBlockContactResponse(BlockUserResponse success) {
        appUserContactsList.remove(selectedAppUserItemIndex);
        appUserContactsAdapter.notifyItemChanged(selectedAppUserItemIndex);

        initLocalDbContactDeletion(appUserContactsList.get(selectedAppUserItemIndex).getId());
    }

    private void initLocalDbContactDeletion(String userId) {
        ((DashboardActivity) getActivity()).deleteUserFromDb(userId);
    }

    private void onInviteNonAppUserResponse(InviteNonAppUserResponse success) {
        showToastMessage("Invitation sent successfully!", false);
    }
}