package com.sdi.hostedin.feature.guest.explore.accommodationdetails;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.RelativeLayout;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.Location;
import com.sdi.hostedin.data.model.Review;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.ActivityAccommodationDetailsBinding;
import com.sdi.hostedin.databinding.ItemHostDetailsBinding;
import com.sdi.hostedin.enums.AccommodationServices;
import com.sdi.hostedin.enums.AccommodationTypes;
import com.sdi.hostedin.feature.guest.bookings.accommodationbooking.AccommodationBookingActivity;
import com.sdi.hostedin.utils.ImageUtils;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AccommodationDetailsActivity extends AppCompatActivity implements OnMapReadyCallback {

    public static final String ACCOMMODATION_KEY = "accommodation_key";
    public static final String IS_HOST_KEY = "is_hsot_key";
    private static final int EMPTY_DATA_STRUCTURE = 0;
    public static final String DELIMITER_DETAILS = " · ";
    private ActivityAccommodationDetailsBinding binding;
    private MapView mpvLocation;
    private GoogleMap gMap;
    private BottomSheetBehavior<RelativeLayout> bottomSheetBehavior;
    private ItemHostDetailsBinding inclHostData;
    private AccommodationDetailsViewModel accommodationDetailsViewModel;
    private String servicesAccommodation = "";
    private Boolean isHost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAccommodationDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        binding.setAccommodationData(extras.getParcelable(ACCOMMODATION_KEY));
        isHost = extras.getBoolean(IS_HOST_KEY);

        if(isHost){
            binding.btnBooking.setVisibility(View.INVISIBLE);
        }else {
            binding.btnBooking.setVisibility(View.VISIBLE);
        }

        accommodationDetailsViewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication())).get(AccommodationDetailsViewModel.class);

        accommodationDetailsViewModel.getMultimediasListMutableLiveData().observe(this, multimedias -> {
            loadMultimedia(multimedias);
        });
        this.inclHostData = binding.inclHostData;

        mpvLocation = binding.mpvAccommodationLocation;
        mpvLocation.onCreate(savedInstanceState);
        mpvLocation.getMapAsync(this);

        loadAccommodationData();
        configureBottomSheet();
        configureButtons();
        manageProgressBarCircle();
        accommodationDetailsViewModel.loadAccommodationMultimedia(binding.getAccommodationData().getId());
    }

    private void configureBottomSheet() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.btmshMoreDetails);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    private void configureButtons() {

        binding.btnBooking.setOnClickListener( v -> showAccommodationBooking() );

        binding.btnShowMoreDescription.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            binding.txvMoreDetailsTitle.setText(R.string.hint_description);
            binding.txvMoreDetailsDescription.setText(binding.getAccommodationData().getDescription());
        });

        binding.btnShowMoreServices.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            binding.txvMoreDetailsTitle.setText(R.string.services);
            binding.txvMoreDetailsDescription.setText(servicesAccommodation);
        });

        binding.btnShowMoreRules.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HALF_EXPANDED);
            binding.txvMoreDetailsTitle.setText(R.string.rules);
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

        Uri imageUri = prepareImageForIntent();

        if (imageUri != null) {
            intent.putExtra(AccommodationBookingActivity.ACCOMMODATION_IMAGE_KEY, imageUri);
        }

        startActivity(intent);
    }

    private Uri prepareImageForIntent() {
        Drawable drawable1 = binding.imvFirstImage.getDrawable();
        Drawable drawable2 = binding.imvSecondImage.getDrawable();
        Drawable drawable3 = binding.imvThirdImage.getDrawable();

        Uri resultUri = null;
        if (drawable1 instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable1).getBitmap();
            resultUri = getImageUri(bitmap);
        } else if (drawable2 instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable2).getBitmap();
            resultUri = getImageUri(bitmap);
        } else if (drawable3 instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable3).getBitmap();
            resultUri = getImageUri(bitmap);
        }

        return resultUri;
    }

    private Uri getImageUri(Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "Accommodation photo", null);
        if (path != null) {
            return Uri.parse(path);
        } else {
            return null;
        }
    }

    private void manageProgressBarCircle() {
        accommodationDetailsViewModel.getRequestStatusMutableLiveData().observe(this, status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbAccommodationDetails.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbAccommodationDetails.setVisibility(View.GONE);
                    break;
                case ERROR:
                    binding.pgbAccommodationDetails.setVisibility(View.GONE);
                    ToastUtils.showShortInformationMessage(this, status.getMessage());
            }
        });
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.gMap = googleMap;
        loadLocation();
    }

    private void loadAccommodationData() {
        binding.vflpAccommodationMultimedia.setFlipInterval(60000);
        binding.vflpAccommodationMultimedia.setAutoStart(false);

        loadNightPrice();
        loadAccommodationBasics();
        loadAccommodationServices();
        loadReviews();
        loadAccommodationType();
        loadHostData();
    }

    private void loadMultimedia(List<byte[]> multimedias) {
        for (int i = 0; i < multimedias.size(); i++) {
            insertMultimediaIntoViewFlipper(i, multimedias.get(i));
        }
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

    private void loadVideo(byte[] videoBytes) {
        try {
            File tempFile = File.createTempFile("video", ".mp4", getCacheDir());
            FileOutputStream fos = new FileOutputStream(tempFile);
            fos.write(videoBytes);
            fos.close();

            VideoView videoView = binding.vdvFourthVideo;
            videoView.setVideoURI(Uri.parse(tempFile.getAbsolutePath()));

            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);

            videoView.setOnPreparedListener(mediaPlayer -> {
                mediaPlayer.setLooping(true);
            });

            videoView.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadNightPrice() {
        String price = String.valueOf(binding.getAccommodationData().getNightPrice());
        String detailPrice = getString(R.string.mxn_per_night_message);
        String nightPrice = getString(R.string.money_symbol) + price + detailPrice;

        binding.txvNightPrice.setText(nightPrice);
    }

    private void loadAccommodationBasics() {
        String guestsNumber = String.valueOf(binding.getAccommodationData().getGuestsNumber());
        String roomsNumber = String.valueOf(binding.getAccommodationData().getRoomsNumber());
        String bedsNumber = String.valueOf(binding.getAccommodationData().getBedsNumber());
        String bathroomsNumber = String.valueOf(binding.getAccommodationData().getBathroomsNumber());

        String[] basics = {guestsNumber, roomsNumber, bedsNumber, bathroomsNumber};
        String[] basicsNames =
        {
                " " + getString(R.string.guests),
                " " + getString(R.string.rooms),
                " " + getString(R.string.beds),
                " " + getString(R.string.bathrooms)
        };

        String[] basicsDetails = new String[basics.length];
        for (int i = 0; i < basics.length; i++) {
            basicsDetails[i] = basics[i] + basicsNames[i];
        }

        String basicsJoined = String.join(DELIMITER_DETAILS, basicsDetails);

        binding.txvBasics.setText(basicsJoined);
    }

    private void loadAccommodationServices() {
        String[] services = binding.getAccommodationData().getAccommodationServices();

        if (services != null) {
            int numOfServices = services.length;
            String[] accommodationServices = new String[numOfServices];

            for(int i = 0 ; i < services.length ; i++) {
                accommodationServices[i] = AccommodationServices.getDescriptionForService(this, services[i]);
            }

            if (accommodationServices != null) {
                servicesAccommodation = String.join(DELIMITER_DETAILS, accommodationServices);
            } else {
                servicesAccommodation = String.join(DELIMITER_DETAILS, services);
            }
        }

        binding.txvAccommodationsServices.setText(servicesAccommodation);
    }

    private void loadReviews() {
        accommodationDetailsViewModel.getAccommodationReviews(binding.getAccommodationData().getId());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        binding.rcyvReviews.setLayoutManager(layoutManager);

        ReviewAdapter reviewAdapter = new ReviewAdapter();
        binding.rcyvReviews.setAdapter(reviewAdapter);

        accommodationDetailsViewModel.getReviewsMutableLiveData().observe(this, reviews -> {
            if (reviews != null && reviews.size() > EMPTY_DATA_STRUCTURE) {
                binding.txvWithoutReviews.setVisibility(View.GONE);
                reviewAdapter.submitList(reviews);
                calculateScore(reviews);
            } else {
                binding.txvWithoutReviews.setVisibility(View.VISIBLE);
            }
        });
    }

    private void calculateScore(List<Review> reviews) {
        List<Float> scores = new ArrayList<>();
        for(Review review : reviews) {
            float score = review.getRating();
            if (score > EMPTY_DATA_STRUCTURE) {
                scores.add(score);
            }
        }

        if (scores.size() > EMPTY_DATA_STRUCTURE) {
            accommodationDetailsViewModel.calculateScore(scores);
        }

        accommodationDetailsViewModel.getScore().observe(this, accommodationScore -> {
            float scoreCalculated = accommodationDetailsViewModel.getScore().getValue();
            if (scoreCalculated > EMPTY_DATA_STRUCTURE) {
                binding.txvScore.setText(String.valueOf(scoreCalculated));
            }
        });
    }

    private void loadAccommodationType() {
        String type = binding.getAccommodationData().getAccommodationType();

        if (type != null) {
            String accommodationType = AccommodationTypes.getDescriptionForType(this, type);
            if (accommodationType != null) {
                binding.txvAccommodationType.setText(accommodationType);
            }
        }
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
        User user = binding.getAccommodationData().getUser();
        if (user != null) {
            inclHostData.txvHostName.setText(user.getFullName());
            inclHostData.txvHostPhoneNumber.setText(user.getPhoneNumber());
            inclHostData.txvHostOccupation.setVisibility(View.VISIBLE);
            inclHostData.txvHostOccupation.setText(user.getOccupation());
            inclHostData.txvHostResidence.setVisibility(View.VISIBLE);
            inclHostData.txvHostResidence.setText(user.getResidence());
            ImageUtils.loadProfilePhoto(user, binding.inclHostData.imvProfilePhoto);
        }
    }
}