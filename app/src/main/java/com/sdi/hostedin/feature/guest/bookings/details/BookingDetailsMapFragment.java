package com.sdi.hostedin.feature.guest.bookings.details;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.databinding.FragmentBookingDetailsMapBinding;

public class BookingDetailsMapFragment extends Fragment {

    private static final String ACCOMMODATION_KEY = "accommodation_key";
    private Accommodation accommodation;
    private FragmentBookingDetailsMapBinding binding;


    public static BookingDetailsMapFragment newInstance(Accommodation accomm) {
        BookingDetailsMapFragment fragment = new BookingDetailsMapFragment();
        Bundle args = new Bundle();
        args.putParcelable(ACCOMMODATION_KEY, accomm);
        fragment.setArguments(args);
        return fragment;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(GoogleMap googleMap) {
            LatLng location = new LatLng(accommodation.getLocation().getLatitude(), accommodation.getLocation().getLongitude());
            googleMap.addMarker(new MarkerOptions().position(location).title(accommodation.getLocation().getAddress()));
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 15));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = FragmentBookingDetailsMapBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if(getArguments() != null){
            accommodation = getArguments().getParcelable(ACCOMMODATION_KEY);
        }

        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.booking_details_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}