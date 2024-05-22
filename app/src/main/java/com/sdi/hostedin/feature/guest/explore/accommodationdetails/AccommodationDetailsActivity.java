package com.sdi.hostedin.feature.guest.explore.accommodationdetails;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sdi.hostedin.data.model.Location;
import com.sdi.hostedin.databinding.ActivityAccommodationDetailsBinding;
import com.sdi.hostedin.databinding.ItemHostDetailsBinding;
import com.sdi.hostedin.feature.guest.bookings.accommodationbooking.AccommodationBookingActivity;
import com.sdi.hostedin.grpc.GrpcAccommodationMultimedia;
import com.sdi.hostedin.grpc.GrpcServerData;
import com.sdi.hostedin.utils.ImageUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class AccommodationDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String ACCOMMODATION_KEY = "accommodation_key";
    private ActivityAccommodationDetailsBinding binding;
    private MapView mpvLocation;
    private GoogleMap gMap;
    private BottomSheetBehavior<RelativeLayout> bottomSheetBehavior;
    private ItemHostDetailsBinding inclHostData;
    private GrpcAccommodationMultimedia grpcClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccommodationDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        binding.setAccommodationData(extras.getParcelable(ACCOMMODATION_KEY));

        this.inclHostData = binding.inclHostData;

        //viewModel

        mpvLocation = binding.mpvAccommodationLocation;
        mpvLocation.onCreate(savedInstanceState);
        mpvLocation.getMapAsync(this);

        loadAccommodationData();
        configureBottomSheet();
        configureButtons();
        manageProgressBarCircle();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (grpcClient != null) {
                grpcClient.shutdown();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void configureBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.btmshMoreDetails);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
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

        binding.btnGoAhead.setOnClickListener( v -> binding.vflpAccommodationMultimedia.showNext() );
        binding.btnGoBack.setOnClickListener( v -> binding.vflpAccommodationMultimedia.showPrevious() );
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
        loadMultimedia();
        binding.vflpAccommodationMultimedia.setFlipInterval(60000);
        binding.vflpAccommodationMultimedia.setAutoStart(true);

        loadNightPrice();

        loadAccommodationBasics();

        binding.txvAccommodationsServices.setText(loadAccommodationServices());

        // TODO: load Rating

        // TODO: load Reviews

        // TODO: Load accommodation types (map with type)

        loadHostData();
    }

    private void loadMultimedia() {
        ManagedChannel channel = ManagedChannelBuilder.forAddress(GrpcServerData.HOST, GrpcServerData.PORT)
                .usePlaintext()
                .build();

        for (int i = 0 ; i < 4 ; i ++) {
            byte[] bytes = GrpcAccommodationMultimedia.downloadAccommodationMultimedia(channel, binding.getAccommodationData().getId(),i );
            //ImageView imageView1 = binding.imvFirstImage;
            //ImageUtils.loadAccommodationImage(bytes, imageView1);
            //binding.vflpAccommodationMultimedia.setDisplayedChild(0);
            insertMultimediaIntoViewFlipper(i, bytes);
        }

        channel.shutdown();

//        byte[] bytes = GrpcAccommodationMultimedia.downloadAccommodationMultimedia(channel, binding.getAccommodationData().getId(),0 );
//        ImageView imageView1 = binding.imvFirstImage;
//                ImageUtils.loadAccommodationImage(bytes, imageView1);
//                binding.vswchAccommodationMultimedia.setDisplayedChild(0);
//
//        byte[] bytes2 = GrpcAccommodationMultimedia.downloadAccommodationMultimedia(channel, binding.getAccommodationData().getId(),1 );
//        ImageView imageView2 = binding.imvFirstImage;
//        ImageUtils.loadAccommodationImage(bytes2, imageView2);
//        binding.vswchAccommodationMultimedia.setDisplayedChild(1);
//
//
//        binding.vswchAccommodationMultimedia.setDisplayedChild(0);
    }

    private void insertMultimediaIntoViewFlipper(int index, byte[] multimediaBytes) {
        switch (index) {
            case 0:
                ImageView imageView1 = binding.imvFirstImage;
                ImageUtils.loadAccommodationImage(multimediaBytes, imageView1);
                binding.vflpAccommodationMultimedia.setDisplayedChild(0);
                break;
            case 1:
                ImageView imageView2 = binding.imvSecondImage;
                ImageUtils.loadAccommodationImage(multimediaBytes, imageView2);
                break;
            case 2:
                ImageView imageView3 = binding.imvThirdImage;
                ImageUtils.loadAccommodationImage(multimediaBytes, imageView3);
                break;
            case 3:
                loadVideo(multimediaBytes);
                break;
        }
    }

    private void showFirstImage() {
        binding.vflpAccommodationMultimedia.setDisplayedChild(0);
        // Aquí puedes cargar la imagen en el primer ImageView si es necesario
    }

    private void showSecondImage() {
        binding.vflpAccommodationMultimedia.setDisplayedChild(1);
        // Aquí puedes cargar la imagen en el segundo ImageView si es necesario
    }

    private void showThirdImage() {
        binding.vflpAccommodationMultimedia.setDisplayedChild(2);
        // Aquí puedes cargar la imagen en el tercer ImageView si es necesario
    }

    private void showVideo() {
        binding.vflpAccommodationMultimedia.setDisplayedChild(3);
        // Aquí puedes cargar el video en el VideoView si es necesario
    }

    private void loadVideo(byte[] videoBytes) {
        try {
            File tempFile = File.createTempFile("video", ".mp4", getCacheDir());
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(videoBytes);
            fos.close();

            VideoView videoView = binding.vdvFourthVideo;
            videoView.setVideoURI(Uri.parse(tempFile.getAbsolutePath()));
            videoView.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
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
                gMap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 12));
            }
        }
    }

    private void loadHostData() {
        inclHostData.txvHostName.setText(binding.getAccommodationData().getUser().getFullName());
        inclHostData.txvHostPhoneNumber.setText(binding.getAccommodationData().getUser().getPhoneNumber());
        inclHostData.txvHostOccupation.setVisibility(View.VISIBLE);
        inclHostData.txvHostOccupation.setText(binding.getAccommodationData().getUser().getOccupation());
        inclHostData.txvHostResidence.setVisibility(View.VISIBLE);
        inclHostData.txvHostResidence.setText(binding.getAccommodationData().getUser().getResidence());
    }
}