package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.insyslab.tooz.R;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.ui.activities.ActionsActivity;
import com.insyslab.tooz.ui.activities.DashboardActivity;

import java.util.List;

import static com.insyslab.tooz.utils.AppConstants.KEY_SET_REMINDER_TYPE;

/**
 * Created by TaNMay on 26/09/16.
 */

public class LocationSelectorFragment extends BaseFragment implements OnMapReadyCallback {

    public static final String TAG = "LocationSelFrag ==> ";

    private static final String ARG_PARAM1 = "ARG_PARAM1";

    private OnLocationSelectedListener onLocationSelectedListener;

    private RelativeLayout content;
    private AutoCompleteTextView actvSearchLocation;
    private GoogleMap mMap;

    private String fragmentType = null;
    private List<String> strings = null;

    public LocationSelectorFragment() {

    }

    public static LocationSelectorFragment newInstance(Bundle bundle) {
        LocationSelectorFragment fragment = new LocationSelectorFragment();
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
            fragmentType = bundle.getString(KEY_SET_REMINDER_TYPE);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View layout = inflater.inflate(R.layout.fragment_location_selector, container, false);

        updateFragment(new FragmentState(TAG));
        initView(layout);
        setUpActions();

        initGoogleMaps();
        initAutoCompleteSearchView();

        return layout;
    }

    private void initAutoCompleteSearchView() {
        ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, strings);
        actvSearchLocation.setAdapter(adapter);
        actvSearchLocation.setThreshold(1);
    }

    private void initGoogleMaps() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fls_map);
        mapFragment.getMapAsync(this);
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.fls_content);
        actvSearchLocation = rootView.findViewById(R.id.fls_search_location);
    }

    private void setUpActions() {

    }

    public void onSaveClick() {
        closeThisFragment();
    }

    private void closeThisFragment() {
        getActivity().onBackPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        onLocationSelectedListener = (OnLocationSelectedListener) context;
    }

    @Override
    public void onDetach() {
        FragmentState fragmentState = new FragmentState(SetReminderFragment.TAG);
        fragmentState.setFragmentDetailedName(fragmentType);

        updateFragment(fragmentState);
        onLocationSelectedListener.onLocationSelected(1001);
        super.onDetach();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        LatLng sydney = new LatLng(-34, 151);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    public interface OnLocationSelectedListener {

        void onLocationSelected(int position);

    }
}