package com.sdi.hostedin.feature.guest.bookings.details;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava2.RxDataStore;

import com.sdi.hostedin.data.model.Booking;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.ActivityBookingDetailsBinding;

public class BookingDetailsActivity extends AppCompatActivity {

    private ActivityBookingDetailsBinding binding;
    public static final String THIRD_USER_KEY = "user_key";
    public static final String BOOKING_KEY = "booking_key";

    RxDataStore<Preferences> dataStoreRX;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();

        User thirdUser = extras.getParcelable(THIRD_USER_KEY);
        Booking bookingInfo = extras.getParcelable(BOOKING_KEY);

        BookingDetailsFragment defaultFragment;
        defaultFragment = BookingDetailsFragment.newInstance(thirdUser, bookingInfo);
        getSupportFragmentManager().beginTransaction()
                .add(binding.fgcvBookDetailsFragmentContainer.getId(), defaultFragment)
                .commit();
    }
}