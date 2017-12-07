package com.insyslab.tooz.ui.fragments;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.insyslab.tooz.R;
import com.insyslab.tooz.models.FragmentState;
import com.insyslab.tooz.ui.adapters.PlacesAutoCompleteAdapter;
import com.insyslab.tooz.utils.PlaceAPI;
import com.insyslab.tooz.utils.ToozApplication;
import com.insyslab.tooz.utils.Util;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

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
    private ImageView ivClear;

    private String fragmentType = null;
    private List<String> strings = null;

    private LatLng selectedLocation = null;
    private String selectedAddress = null;

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
        actvSearchLocation.setAdapter(new PlacesAutoCompleteAdapter(getContext(), android.R.layout.simple_dropdown_item_1line, new PlaceAPI()));
        actvSearchLocation.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String clickedItem = (String) adapterView.getItemAtPosition(position);
                Log.d(TAG, "clickedItem: " + clickedItem);
                Util.hideSoftKeyboard(getActivity());
                onPlaceAutocompleteSelected(clickedItem);
            }
        });
    }

    private void onPlaceAutocompleteSelected(String locationSelected) {
        updateLocation(locationSelected);
    }

    private void initGoogleMaps() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.fls_map);
        mapFragment.getMapAsync(this);
    }

    private void initView(View rootView) {
        content = rootView.findViewById(R.id.fls_content);
        actvSearchLocation = rootView.findViewById(R.id.fls_search_location);
        ivClear = rootView.findViewById(R.id.fls_clear);
    }

    private void setUpActions() {
        ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onAutocompleteClearClick();
            }
        });
    }

    private void onAutocompleteClearClick() {
        actvSearchLocation.setText("");
    }

    public void onSaveClick() {
        if (selectedLocation == null) {
            showSnackbarMessage(
                    content,
                    "Please select a location!",
                    false,
                    getString(R.string.ok),
                    null,
                    true
            );
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
        onLocationSelectedListener = (OnLocationSelectedListener) context;
    }

    @Override
    public void onDetach() {
        FragmentState fragmentState = new FragmentState(SetReminderFragment.TAG);
        fragmentState.setFragmentDetailedName(fragmentType);
        updateFragment(fragmentState);
        onLocationSelectedListener.onLocationSelected(selectedLocation, selectedAddress);
        super.onDetach();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        Location currentLocation = ToozApplication.getInstance().getLastLocation();
        Log.d(TAG, "Location: " + (currentLocation != null ? "not null" : "null"));

        updateLocation(currentLocation);
    }

    private void updateLocation(Location currentLocation) {
        selectedLocation = null;
        selectedAddress = null;

        if (currentLocation != null) {
            LatLng currentLatLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
            mMap.addMarker(new MarkerOptions().position(currentLatLng));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(currentLatLng));
            mMap.animateCamera(CameraUpdateFactory.zoomTo(16));

            selectedLocation = currentLatLng;
        }

        if (selectedLocation != null) {
            actvSearchLocation.setText(selectedLocation.latitude + ", " + selectedLocation.longitude);
            String address = getAddressFromCoords(selectedLocation);
            if (address.isEmpty())
                address = "Address not available (" + selectedLocation.latitude + ", " + selectedLocation.longitude + ")";
            actvSearchLocation.setText(address);
            selectedAddress = address;
        }
    }

    private void updateLocation(String currentAddress) {
        selectedLocation = null;
        selectedAddress = null;

        if (currentAddress != null && !currentAddress.isEmpty()) {
            selectedAddress = currentAddress;
        }

        if (selectedAddress != null) {
            LatLng latLng = getCoordsFromAddress(selectedAddress);

            if (latLng != null) {
                mMap.addMarker(new MarkerOptions().position(latLng));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(16));
            } else {
                showSnackbarMessage(
                        content,
                        "Unable to get the location!",
                        false,
                        getString(R.string.ok),
                        null,
                        true
                );
                selectedAddress = null;

            }
            selectedLocation = latLng;
        }
    }

    private LatLng getCoordsFromAddress(String addressStr) {
        Geocoder geocoder = new Geocoder(getContext());
        LatLng latLng = null;
        try {
            List<Address> address = geocoder.getFromLocationName(addressStr, 1);
            if (address != null && address.size() > 0) {
                Address location = address.get(0);
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return latLng;
    }

    private String getAddressFromCoords(LatLng location) {
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        String finalAddress = "";

        try {
            List<Address> addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String postalCode = addresses.get(0).getPostalCode();
            String country = addresses.get(0).getCountryName();

            if (address != null) finalAddress += address;
            if (city != null) finalAddress += ", " + city;
            if (state != null) finalAddress += ", " + state;
            if (postalCode != null) finalAddress += " - " + postalCode;
            if (country != null) finalAddress += ", " + country.toUpperCase();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return finalAddress;
    }

    public interface OnLocationSelectedListener {

        void onLocationSelected(LatLng latLng, String address);

    }
}