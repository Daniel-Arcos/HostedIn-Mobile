package com.sdi.hostedin.feature.host.accommodations.accommodationform;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.sdi.hostedin.data.model.Location;
import com.sdi.hostedin.databinding.FragmentAccommodationLocationBinding;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

import java.util.Arrays;
import java.util.List;

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
    private FragmentAccommodationLocationBinding binding;
    private AccommodationFormViewModel accommodationFormViewModel;
    private Location location;
    private MapView mpvLocation;
    private GoogleMap gMap;
    private PlacesClient placesClient;
    private Marker currentMarker;

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

        binding.btnSearchh.setOnClickListener( v -> {
            String query = binding.tempSearch.getText().toString();
            searchPlace(query);
        });

        return binding.getRoot();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gMap = googleMap;

        if (gMap != null) {
            LatLng loc = new LatLng(19.541313423005903, -96.9271922754975);

            MarkerOptions mko = new MarkerOptions()
                    .position(loc)
                    .draggable(true)
                    .title("FEI");

            currentMarker = gMap.addMarker(mko);
            gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10));
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
                        //for (AutocompletePrediction prediction: predictions) {
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
                                            Log.d("PRUEBA", "searchPlace: " + place.getName() + "address: " + place.getAddress() + "latLng: " + place.getLatLng() + "\n");
                                            moveMarker(place);
                                            gMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                            location.setLatitude(latLng.latitude);
                                            location.setLongitude(latLng.longitude);
                                        }
                                    }
                                } else {
                                    ToastUtils.showShortInformationMessage(this.getContext(), "Failed to fetch place details: " + fetchTask.getException());
                                }
                            });
                        //}
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

//    private void createMarker(Place place) {
//        if (place.getLatLng() != null) {
//            MarkerOptions markerOptions = new MarkerOptions()
//                    .draggable(true)
//                    .position(place.getLatLng())
//                    .title(place.getName());
//
//            Marker marker = gMap.addMarker(markerOptions);
//            gMap.setOnMarkerClickListener(clickedMarker -> {
//                if (clickedMarker.equals(marker)) {
//                    clickedMarker.showInfoWindow();
//                }
//                return false;
//            });
//        }
//    }

    private void moveMarker(Place place) {
        if (currentMarker != null && place.getLatLng() != null) {
            currentMarker.setPosition(place.getLatLng());
            currentMarker.setTitle(place.getName());
        }
    }

    private void ValidateAccommodationLocationSelected() {
        if (location != null) {
            updateLatLng();
            if (location.getLatitude() >= -90 && location.getLongitude() >= -180 ) {
                accommodationFormViewModel.selectAccommodationLocation(location);
                accommodationFormViewModel.nextFragment(LOCAL_FRAGMENT_NUMBER + 1);
            } else {
                ToastUtils.showShortInformationMessage(this.getContext(), "Selecciona la ubicación de tu alojamiento" );
            }
        }
    }

    private void updateLatLng() {
        if (currentMarker != null) {
            LatLng position = currentMarker.getPosition();
            location.setLatitude(position.latitude);
            location.setLongitude(position.longitude);
        }
    }
}