package com.insyslab.tooz.ui.fragments;

import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.insyslab.tooz.R;
import com.insyslab.tooz.interfaces.OnRuntimePermissionsResultListener;
import com.insyslab.tooz.interfaces.OnSyncContactItemClickListener;
import com.insyslab.tooz.models.eventbus.FragmentState;
import com.insyslab.tooz.models.PhoneContact;
import com.insyslab.tooz.models.requests.ContactSyncRequest;
import com.insyslab.tooz.models.requests.Contact_;
import com.insyslab.tooz.models.responses.ContactSyncResponse;
import com.insyslab.tooz.models.responses.Error;
import com.insyslab.tooz.restclient.BaseResponseInterface;
import com.insyslab.tooz.restclient.GenericDataHandler;
import com.insyslab.tooz.restclient.RequestBuilder;
import com.insyslab.tooz.ui.activities.BaseActivity;
import com.insyslab.tooz.ui.activities.OnboardingActivity;
import com.insyslab.tooz.ui.adapters.SyncContactsAdapter;
import com.insyslab.tooz.utils.UriDeserializer;
import com.insyslab.tooz.utils.Util;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static android.Manifest.permission.WRITE_CONTACTS;
import static com.insyslab.tooz.utils.ConstantClass.CONTACTS_SYNC_REQUEST_URL;
import static com.insyslab.tooz.utils.ConstantClass.REQUEST_TYPE_005;

/**
 * Created by TaNMay on 26/09/16.
 */

public class SyncContactsFragment extends BaseFragment implements OnSyncContactItemClickListener,
        OnRuntimePermissionsResultListener, BaseResponseInterface {

    public static final String TAG = "SyncContactsFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private RelativeLayout content, selectAllSec;
    private TextView tvSkip, tvSync;
    private CheckBox cbSelectAll;
    private RecyclerView rvContacts;

    private RecyclerView.Adapter contactsAdapter;
    private LinearLayoutManager layoutManager;

    private List<PhoneContact> phoneContacts;

    private Gson gson;

    public SyncContactsFragment() {

    }

    public static SyncContactsFragment newInstance(Bundle bundle) {
        SyncContactsFragment fragment = new SyncContactsFragment();
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
        OnboardingActivity.onRuntimePermissionsResultListener = this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_sync_contacts, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        initContactPermissions();

        return layout;
    }

    private void initContactPermissions() {
        if (!verifyContactsPermissions()) {
            initRuntimePermissions();
        } else {
            fetchContactsFromDevice();
        }
    }

    private void fetchContactsFromDevice() {
        showProgressDialog(getString(R.string.loading));
        phoneContacts = new ArrayList<>();

        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected Void doInBackground(Void... voids) {
                gson = new GsonBuilder()
                        .registerTypeAdapter(Uri.class, new UriDeserializer())
                        .create();

                ContentResolver cr = getContext().getContentResolver();
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                if (cursor.moveToFirst()) {
                    do {
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                        if (Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0) {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", new String[]{id}, null);
                            while (pCur.moveToNext()) {
                                String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                String contactImage = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI));
                                Uri contactImageUri = null;
                                if (contactImage != null) contactImageUri = Uri.parse(contactImage);

                                PhoneContact phoneContact = new PhoneContact();
                                phoneContact.setName(contactName);
                                phoneContact.setPhoneNumber(contactNumber);
                                phoneContact.setContactImageUri(contactImageUri);
                                phoneContacts.add(phoneContact);

                                break;
                            }
                            pCur.close();
                        }
                    } while (cursor.moveToNext());
                }
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                Log.d(TAG, "Contacts Synced!");
                setUpContactsRv();
            }
        }.execute();
    }

    private void initPhoneContactDbUpdate() {
        ((OnboardingActivity) getActivity()).initLocalDbPhoneContactsUpdate(phoneContacts);
    }

    private void setUpContactsRv() {
        layoutManager = new LinearLayoutManager(getContext());
        contactsAdapter = new SyncContactsAdapter(this, phoneContacts);
        rvContacts.setLayoutManager(layoutManager);
        rvContacts.setAdapter(contactsAdapter);
        hideProgressDialog();
    }

    private boolean verifyContactsPermissions() {
        if (Util.verifyPermission(getContext(), READ_CONTACTS)
                && Util.verifyPermission(getContext(), WRITE_CONTACTS))
            return true;
        else return false;
    }

    private void initRuntimePermissions() {
        ((BaseActivity) getActivity()).requestContactsPermissions();
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.fsc_content);
        tvSkip = rootView.findViewById(R.id.fsc_skip);
        tvSync = rootView.findViewById(R.id.fsc_sync);
        rvContacts = rootView.findViewById(R.id.fsc_contacts);
        selectAllSec = rootView.findViewById(R.id.fsc_select_all_sec);
        cbSelectAll = rootView.findViewById(R.id.fsc_select_all);
    }

    private void setUpActions() {
        tvSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSkipClick();
            }
        });

        tvSync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onSyncClick();
            }
        });

        cbSelectAll.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (compoundButton.isChecked()) modifySelection(true);
                else modifySelection(false);
            }
        });
    }

    private void modifySelection(boolean isSelected) {
        for (int i = 0; i < phoneContacts.size(); i++) {
            phoneContacts.get(i).setSelected(isSelected);
            phoneContacts.get(i).setSynced(isSelected);
        }
        contactsAdapter.notifyDataSetChanged();
    }

    private void onSyncClick() {
        ContactSyncRequest contactSyncRequest = getListOfSelectedContacts();

        if (contactSyncRequest != null && contactSyncRequest.getContacts() != null
                && contactSyncRequest.getContacts().size() == 0) {
            showSnackbarMessage(content,
                    "Please select some contacts to sync!",
                    true,
                    getString(R.string.ok),
                    null,
                    true);
        } else {
            initContactSyncRequest(contactSyncRequest);
        }

    }

    private ContactSyncRequest getListOfSelectedContacts() {
        ContactSyncRequest contactSyncRequest = new ContactSyncRequest();

        List<Contact_> list = new ArrayList<>();
        for (int i = 0; i < phoneContacts.size(); i++) {
            if (phoneContacts.get(i).getSelected()) {
                Contact_ contact = new Contact_();
                contact.setName(phoneContacts.get(i).getName());
                String cmpMobNum = Util.getCompactMobileNumber(phoneContacts.get(i).getPhoneNumber());
                contact.setMobile(cmpMobNum);
                if (cmpMobNum != null) list.add(contact);
            }
        }

        contactSyncRequest.setContacts(list);

        return contactSyncRequest;
    }

    private void initContactSyncRequest(ContactSyncRequest contactSyncRequest) {
        showProgressDialog(getString(R.string.loading));

        String requestUrl = CONTACTS_SYNC_REQUEST_URL;
        JSONObject requestObject = new RequestBuilder().getContactSyncRequestPayload(contactSyncRequest);

        if (requestObject != null) {
            GenericDataHandler reqGenericDataHandler = new GenericDataHandler(this, getContext(), REQUEST_TYPE_005);
            reqGenericDataHandler.jsonObjectRequest(requestObject, requestUrl, Request.Method.POST, ContactSyncResponse.class);
        }
    }

    private void onSkipClick() {
        ((OnboardingActivity) getActivity()).initProceedToDashboard();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        updateFragment(new FragmentState(CreateProfileFragment.TAG));
        super.onDetach();
    }

    @Override
    public void onSmsPermissionsResult(boolean granted) {
        Log.d(TAG, "Some error occurred!");
    }

    @Override
    public void onContactsPermissionsResult(boolean granted) {
        if (granted) {
            fetchContactsFromDevice();
        } else {
            showConfirmationDialog(
                    "Please enable contact permissions to proceed!",
                    getString(R.string.ok),
                    null,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            initContactPermissions();
                        }
                    },
                    null
            );
        }
    }

    @Override
    public void onStoragePermissionsResult(boolean granted) {

    }

    @Override
    public void onLocationPermissionsResult(boolean granted) {
        Log.d(TAG, "onLocationPermissionsResult");
    }

    @Override
    public void onResponse(Object success, Object error, final int requestCode) {
        hideProgressDialog();
        if (error == null) {
            switch (requestCode) {
                case REQUEST_TYPE_005:
                    onContactSyncResponse((ContactSyncResponse) success);
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
                                    case REQUEST_TYPE_005:
                                        onSyncClick();
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

    private void onContactSyncResponse(ContactSyncResponse success) {
        if (success.getStatus() == 200) openDashboardActivity();
        else
            showSnackbarMessage(content, success.getMessage(), true, getString(R.string.ok), null, true);

    }

    private void openDashboardActivity() {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgressDialog(getString(R.string.loading));
            }

            @Override
            protected Void doInBackground(Void... voids) {
                initPhoneContactDbUpdate();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                hideProgressDialog();
                ((OnboardingActivity) getActivity()).initProceedToDashboard();
            }
        }.execute();
    }

    @Override
    public void onContactSelectorClick(int position) {
        phoneContacts.get(position).setSelected(!phoneContacts.get(position).getSelected());
        phoneContacts.get(position).setSynced(!phoneContacts.get(position).isSynced());
//        contactsAdapter.notifyItemChanged(position);
    }
}