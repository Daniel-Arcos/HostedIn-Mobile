package com.sdi.hostedin.feature.guest.bookings.details;

import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.fragment.app.Fragment;

import com.google.android.material.search.SearchView;
import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.Booking;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.ActivityBookingDetailsBinding;
import com.sdi.hostedin.feature.cancelation.cancelationdetails.CancelationDetailsFragment;
import com.sdi.hostedin.feature.guest.explore.accommodations.ExploreFragment;
import com.sdi.hostedin.feature.login.LoginFragment;

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

//        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
//            @Override
//            public void handleOnBackPressed() {
//                handleOnPressedButton();
//            }
//        });
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                handleOnPressedButton();
            }
        };
        getOnBackPressedDispatcher().addCallback(this, callback);
        Bundle extras = getIntent().getExtras();

        User thirdUser = extras.getParcelable(THIRD_USER_KEY);
        Booking bookingInfo = extras.getParcelable(BOOKING_KEY);
        Bundle bundle = new Bundle();
        bundle.putParcelable(BookingDetailsFragment.USER, thirdUser);
        bundle.putParcelable(BookingDetailsFragment.BOOKING, bookingInfo);
        getSupportFragmentManager().beginTransaction()
                .setReorderingAllowed(true)
                .add(binding.fgcvBookDetailsFragmentContainer.getId(), BookingDetailsFragment.class, bundle)
                .commit();
    }

    private void handleOnPressedButton() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.fgcv_book_details_fragment_container);
        if (currentFragment instanceof CancelationDetailsFragment) {
            finish();
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                getSupportFragmentManager().popBackStack();
            } else {
                finish();
            }
        }
    }
}