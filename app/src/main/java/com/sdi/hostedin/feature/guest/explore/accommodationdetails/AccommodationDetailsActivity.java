package com.sdi.hostedin.feature.guest.explore.accommodationdetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.inputmethod.InputMethodManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.Location;
import com.sdi.hostedin.databinding.ActivityAccommodationDetailsBinding;
import com.sdi.hostedin.feature.guest.bookings.AccommodationBookingActivity;

public class AccommodationDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String ACCOMMODATION_KEY = "accommodation_key";
    private ActivityAccommodationDetailsBinding binding;
    private MapView mpvLocation;
    private GoogleMap gMap;
    private BottomSheetBehavior<RelativeLayout> bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccommodationDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        binding.setAccommodationData(extras.getParcelable(ACCOMMODATION_KEY));

        //viewModel

        mpvLocation = binding.mpvAccommodationLocation;
        mpvLocation.onCreate(savedInstanceState);
        mpvLocation.getMapAsync(this);

        loadAccommodationData();
        configureBottomSheet();
        configureButtons();
        manageProgressBarCircle();
    }

    private void configureBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.btmshMoreDetails);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        // bottomSheetBehavior.setDraggable(false);
    }

    private void configureButtons() {

        binding.btnBooking.setOnClickListener( v -> showAccommodationBooking() );

        binding.btnShowMoreDescription.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            binding.txvMoreDetailsTitle.setText("Descripción");
            binding.txvMoreDetailsDescription.setText(binding.getAccommodationData().getDescription());
        });

        binding.btnShowMoreServices.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            binding.txvMoreDetailsTitle.setText("Servicios");
            binding.txvMoreDetailsDescription.setText(loadAccommodationServices());
        });

        binding.btnShowMoreRules.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            binding.txvMoreDetailsTitle.setText("Reglas");
            binding.txvMoreDetailsDescription.setText(binding.getAccommodationData().getRules());
        });

        binding.btnCloseDetails.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            binding.txvMoreDetailsTitle.setText("");
            binding.txvMoreDetailsDescription.setText("");
        });

    }

    private void showAccommodationBooking() {
        Intent intent = new Intent(this, AccommodationBookingActivity.class);
        intent.putExtra(ACCOMMODATION_KEY, binding.getAccommodationData());
        startActivity(intent);
    }

    private void manageProgressBarCircle() {
        //TODO
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        loadLocation();
    }

    private void loadAccommodationData() {
        // TODO: Save the address Name on CREATING accommodation

        loadNightPrice();

        loadAccommodationBasics();

        binding.txvAccommodationsServices.setText(loadAccommodationServices());

        // TODO: load Rating

        // TODO: Reviews

        // TODO: Load accommodation types (map with type)

        // Host data
    }

    private void loadNightPrice() {
        String price = String.valueOf(binding.getAccommodationData().getNightPrice());
        String detailPrice = " MXN por noche";
        String nightPrice = "$" + price + detailPrice;

        binding.txvNightPrice.setText(nightPrice);
    }

    private void loadAccommodationBasics() {
        String guestsNumber = String.valueOf(binding.getAccommodationData().getGuestsNumber());
        String roomsNumber = String.valueOf(binding.getAccommodationData().getRoomsNumber());
        String bedsNumber = String.valueOf(binding.getAccommodationData().getBedsNumber());
        String bathroomsNumber = String.valueOf(binding.getAccommodationData().getBathroomsNumber());

        String[] basics = {guestsNumber, roomsNumber, bedsNumber, bathroomsNumber};
        String[] basicsNames = {" Huéspedes", " Habitaciones", " Camas", " Baños"};

        String[] basicsDetails = new String[basics.length];
        for (int i = 0; i < basics.length; i++) {
            basicsDetails[i] = basics[i] + basicsNames[i];
        }

        String delimiter = " · ";
        String basicsJoined = String.join(delimiter, basicsDetails);

        binding.txvBasics.setText(basicsJoined);
    }

    private String loadAccommodationServices() {
        // TODO: map with services names

        String[] services = binding.getAccommodationData().getAccommodationServices();
        String delimiter = " · ";
        String servicesJoined = String.join(delimiter, services);

        return servicesJoined;
    }

    private void loadLocation() {
        Location location = binding.getAccommodationData().getLocation();
        String accommodationTitle = binding.getAccommodationData().getTitle();

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            if (gMap != null) {
                LatLng loc = new LatLng(latitude, longitude);

                MarkerOptions mko = new MarkerOptions()
                        .position(loc)
                        .draggable(false)
                        .title(accommodationTitle);

                gMap.addMarker(mko);
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 8));
            }
        }
    }

    private void loadHostData() {
        // TODO
    }
}