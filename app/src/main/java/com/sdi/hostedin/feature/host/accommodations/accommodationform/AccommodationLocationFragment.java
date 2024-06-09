package com.sdi.hostedin.feature.host.accommodations.accommodationform;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsRequest;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.Location;
import com.sdi.hostedin.databinding.FragmentAccommodationLocationBinding;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AccommodationLocationFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AccommodationLocationFragment extends Fragment implements OnMapReadyCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final int LOCAL_FRAGMENT_NUMBER = 2;
    private static final int MIN_LATITUDE_VALUE_ALLOWED = -90;
    private static final int MIN_LONGITUDE_VALUE_ALLOWED = -180;
    private FragmentAccommodationLocationBinding binding;
    private AccommodationFormViewModel accommodationFormViewModel;
    private Location location;
    private MapView mpvLocation;
    private GoogleMap gMap;
    private PlacesClient placesClient;
    private Marker currentMarker;

    private static Accommodation accommodationToEdit;
    private static boolean isEdition = false;

    public AccommodationLocationFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AccommodationLocationFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AccommodationLocationFragment newInstance(String param1, String param2) {
        AccommodationLocationFragment fragment = new AccommodationLocationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static AccommodationLocationFragment newInstance(Accommodation accommodation, boolean isEdition) {
        AccommodationLocationFragment fragment = new AccommodationLocationFragment();
        Bundle args = new Bundle();
        accommodationToEdit = accommodation;
        AccommodationLocationFragment.isEdition = isEdition;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentAccommodationLocationBinding.inflate(getLayoutInflater());

        accommodationFormViewModel =
                new ViewModelProvider(getActivity(), new ViewModelFactory(requireActivity().getApplication()))
                        .get(AccommodationFormViewModel.class);

        mpvLocation = binding.mpvAccommodationLocation;
        mpvLocation.onCreate(savedInstanceState);
        mpvLocation.getMapAsync(this);

        Places.initialize(this.getContext(), getString(R.string.google_maps_key));
        placesClient = Places.createClient(this.getContext());

        binding.btnSearchLocation.setOnClickListener( v -> {
            String query = binding.etxLocationSearch.getText().toString();
            searchPlace(query);
        });



        return binding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gMap = googleMap;

        if (gMap != null) {
            LatLng loc = new LatLng(19.541313423005903, -96.9271922754975);
            if(isEdition){
                LatLng accommodationLocation = new LatLng(accommodationToEdit.getLocation().getLatitude(), accommodationToEdit.getLocation().getLongitude());
                setLocation(accommodationLocation);
            }else{
                setLocation(loc);
            }
        }
    }

    private void setLocation(LatLng loc){
        MarkerOptions mko = new MarkerOptions()
                .position(loc)
                .draggable(true)
                .title(getString(R.string.app_name));

        currentMarker = gMap.addMarker(mko);
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 14));

        location = accommodationFormViewModel.getAccommodationMutableLiveData().getValue().getLocation();

        if (currentMarker != null && location != null && location.getLatitude() != 0 && location.getLongitude() != 0) {
            moveMarkerToSavedLocation(location);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        location = new Location();
        location.setLatitude(-100);
        location.setLongitude(-200);

        accommodationFormViewModel.getFragmentNumberMutableLiveData().observe(getViewLifecycleOwner(), fragmentNumber -> {
            if (fragmentNumber == LOCAL_FRAGMENT_NUMBER) {
                ValidateAccommodationLocationSelected();
            }
        });
    }

    private void moveMarkerToSavedLocation(Location savedLocation) {
        LatLng latLng = new LatLng(savedLocation.getLatitude(), savedLocation.getLongitude());
        if (currentMarker != null) {
            currentMarker.setPosition(latLng);
            currentMarker.setTitle(savedLocation.getAddress());
        } else {
            MarkerOptions mko = new MarkerOptions().position(latLng).draggable(true).title(savedLocation.getAddress());
            currentMarker = gMap.addMarker(mko);
        }
        gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14));
    }

    private void searchPlace(String query) {
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                .setQuery(query)
                .build();

        placesClient.findAutocompletePredictions(request).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FindAutocompletePredictionsResponse response = task.getResult();
                if (response != null) {
                    List<AutocompletePrediction> predictions = response.getAutocompletePredictions();
                    if (!predictions.isEmpty()) {
                            AutocompletePrediction firstPrediction = predictions.get(0);
                            String placeId = firstPrediction.getPlaceId();
                            List<Place.Field> placeDetailFields = Arrays.asList(Place.Field.LAT_LNG, Place.Field.NAME, Place.Field.ADDRESS);
                            FetchPlaceRequest placeRequest = FetchPlaceRequest.builder(placeId, placeDetailFields).build();
                            placesClient.fetchPlace(placeRequest).addOnCompleteListener(fetchTask -> {
                                if (fetchTask.isSuccessful()) {
                                    FetchPlaceResponse fetchResponse = fetchTask.getResult();
                                    if (fetchResponse != null) {
                                        Place place = fetchResponse.getPlace();
                                        LatLng latLng = place.getLatLng();
                                        if (latLng != null) {
                                            moveMarker(place);
                                            gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                            location.setLatitude(latLng.latitude);
                                            location.setLongitude(latLng.longitude);
                                            location.setAddress(place.getAddress());
                                        }
                                    }
                                } else {
                                    ToastUtils.showShortInformationMessage(this.getContext(), "Failed to fetch place details: " + fetchTask.getException());
                                }
                            });
                    }
                }
            } else {
                Exception exception = task.getException();
                if (exception != null) {
                    ToastUtils.showShortInformationMessage(this.getContext(), "Autocomplete prediction fetching failed: " + exception.getMessage());
                }
            }
        });
    }

    private void moveMarker(Place place) {
        if (currentMarker != null && place.getLatLng() != null) {
            currentMarker.setPosition(place.getLatLng());
            currentMarker.setTitle(place.getName());

            gMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
        }
    }

    private void ValidateAccommodationLocationSelected() {
        if (location != null) {
            if (location.getLatitude() >= MIN_LATITUDE_VALUE_ALLOWED && location.getLongitude() >= MIN_LONGITUDE_VALUE_ALLOWED) {
                updateLatLng();
                accommodationFormViewModel.selectAccommodationLocation(location);
                accommodationFormViewModel.nextFragment(LOCAL_FRAGMENT_NUMBER + 1);
            } else {
                ToastUtils.showShortInformationMessage(this.getContext(), getString(R.string.choose_accommodation_location_message));
            }
        }
    }

    private void updateLatLng() {
        if (currentMarker != null) {
            LatLng position = currentMarker.getPosition();
            location.setLatitude(position.latitude);
            location.setLongitude(position.longitude);
            setAddressFromLatLng(position);
        }
    }

    private void setAddressFromLatLng(LatLng position) {
        Geocoder geocoder = new Geocoder(this.getContext(), Locale.getDefault());

        try {
            List<Address> addresses = geocoder.getFromLocation(position.latitude, position.longitude, 1);
            if (addresses != null && !addresses.isEmpty()) {
                String address = addresses.get(0).getAddressLine(0);
                location.setAddress(address);
            } else {
                location.setAddress(getString(R.string.app_name));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}