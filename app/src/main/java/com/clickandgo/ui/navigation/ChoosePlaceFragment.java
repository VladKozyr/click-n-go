package com.clickandgo.ui.navigation;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.clickandgo.R;
import com.clickandgo.di.viewmodel.ViewModelFactory;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class ChoosePlaceFragment extends Fragment implements OnMapReadyCallback, AdapterView.OnItemClickListener {

    private static final String TAG = ChoosePlaceFragment.class.getSimpleName();

    private GoogleMap mMap;
    private SupportMapFragment mapFragment;
    private AutoCompleteTextView textView;

    private static final float DEFAULT_ZOOM = 12f;

    @Inject
    public ViewModelFactory factory;
    public SearchViewModel searchViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchViewModel = ViewModelProviders.of(requireActivity(), factory).get(SearchViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choose_place, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initMap();

        String[] subwayStations = getResources().getStringArray(R.array.subway_stations);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_dropdown_item_1line, subwayStations);

        textView = getView().findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);
        textView.setOnItemClickListener(this);
    }

    private void initMap() {
        Activity activity = getActivity();
        if (activity != null) {
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);

            assert mapFragment != null;
            mapFragment.getMapAsync(this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        boolean success = googleMap.setMapStyle(
                MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style));

        if (!success) {
            Log.e(TAG, "Style parsing failed.");
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(50.45466, 30.5238), DEFAULT_ZOOM));
    }

    private void moveCamera(LatLng latLng, float zoom, String title) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        MarkerOptions options = new MarkerOptions()
                .position(latLng)
                .title(title);

        mMap.addMarker(options);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String searchQuery = textView.getText().toString() + " метро";
        Geocoder geocoder = new Geocoder(getContext());
        List<Address> list = new ArrayList<>();
        try {
            list = geocoder.getFromLocationName(searchQuery, 1);
        } catch (IOException e) {
            Log.e(TAG, "geolocate exception");
        }


        if (!list.isEmpty()) {
            Address address = list.get(0);
            moveCamera(new LatLng(
                            address.getLatitude(),
                            address.getLongitude()),
                    DEFAULT_ZOOM,
                    address.getAddressLine(0)
            );
            InputMethodManager in = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            in.hideSoftInputFromWindow(view.getApplicationWindowToken(), 0);
        } else {
            Log.e(TAG, "Place not found");
        }
    }

    @Override
    public void onDestroyView() {
        mMap.clear();
        mapFragment.onDestroy();
        mapFragment = null;
        super.onDestroyView();
    }
}