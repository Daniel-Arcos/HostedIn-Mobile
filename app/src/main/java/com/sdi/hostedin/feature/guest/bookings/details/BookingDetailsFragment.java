package com.sdi.hostedin.feature.guest.bookings.details;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.data.model.Booking;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.FragmentBookingDetailsBinding;
import com.sdi.hostedin.enums.BookingSatuses;
import com.sdi.hostedin.feature.cancelation.reasonselection.CancelationReasonSelectionFragment;
import com.sdi.hostedin.feature.signup.SignupFragment;
import com.sdi.hostedin.utils.DateFormatterUtils;
import com.sdi.hostedin.utils.ToastUtils;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;

public class BookingDetailsFragment extends Fragment {

    public static final String USER = "user";
    public static final String BOOKING = "booking";
    private  FragmentBookingDetailsBinding binding;

    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
    private static User thirdUser;
    private RxDataStore<Preferences> dataStoreRX;
    private static Booking bookingInfo;
    public BookingDetailsFragment() {
    }


    public static BookingDetailsFragment newInstance(User user, Booking booking) {
        BookingDetailsFragment fragment = new BookingDetailsFragment();
        Bundle args = new Bundle();
        thirdUser = user;
        bookingInfo = booking;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBookingDetailsBinding.inflate(inflater, container, false);
        loadBookingInformation();
        loadCustomeButtons();
        return binding.getRoot();
    }

    private void loadBookingInformation(){
        thirdUser = getArguments().getParcelable(USER);
        bookingInfo = getArguments().getParcelable(BOOKING);

        binding.rcyvAccommodationMedia.setBackgroundColor(Color.LTGRAY);
        binding.txvStartingDate.setText(DateFormatterUtils.parseMongoDateToNaturalDate(bookingInfo.getBeginningDate()));
        binding.txvEndingDate.setText(DateFormatterUtils.parseMongoDateToNaturalDate(bookingInfo.getEndingDate()));
        binding.txvTotalGuest.setText(String.valueOf(bookingInfo.getNumberOfGuests())+ "guests");
        binding.txvTotalCost.setText("$ " + String.valueOf(bookingInfo.getTotalCost()) + " MXN");
        binding.txvHostName.setText(thirdUser.getFullName());
        String contactInfo = "Phone number:  " + thirdUser.getPhoneNumber() + "\n" +
                "Email: " + thirdUser.getEmail();
        binding.txvContactInfo.setText(contactInfo);
        if(isHost()){
            binding.txvHostTag.setText("Who will be your Guest?");
            binding.txvTotalCostTag.setText("How much will you earn?");
        }
        else {
            binding.txvHostTag.setText("Who will be your Host?");
            binding.txvTotalCostTag.setText("How much will you pay?");
        }
    }

    private void loadCustomeButtons(){
        binding.inclWatchMap.imageView.setBackgroundResource(R.drawable.map_icon);
        binding.inclWatchMap.txvGenericText.setText("Watch in map");
        binding.inclWatchMap.bttGenericButton.setOnClickListener(this:: watchAccommodationMap);
        if(bookingInfo.getBookingStatus().equals(BookingSatuses.CURRENT)){
            binding.inclRatePublication.imageView.setBackgroundResource(R.drawable.rate_icon);
            binding.inclRatePublication.txvGenericText.setText("Rate accommodation");
            binding.inclRatePublication.bttGenericButton.setOnClickListener(this:: openRateAccommodationWindow);
            binding.inclRatePublication.getRoot().setVisibility(View.VISIBLE);
        }
        else {
            binding.inclRatePublication.getRoot().setVisibility(View.INVISIBLE);
        }
        if(canBeCancelled(bookingInfo.getBeginningDate())){
            binding.inclCancellBook.imageView.setBackgroundResource(R.drawable.cancell_icon);
            binding.inclCancellBook.txvGenericText.setText("Cancel booking");
            binding.inclCancellBook.bttGenericButton.setOnClickListener(this::goToCancelFragment);
            binding.inclCancellBook.getRoot().setVisibility(View.VISIBLE);
        }
        else {
            binding.inclCancellBook.getRoot().setVisibility(View.INVISIBLE);
        }
    }

    private void goToCancellFragment(View view) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(CancelationReasonSelectionFragment.BOOKING, bookingInfo);
        getParentFragmentManager()
            .beginTransaction()
            .setReorderingAllowed(true)
                .addToBackStack(null)
            .replace(R.id.fgcv_book_details_fragment_container, CancelationReasonSelectionFragment.class, bundle)
            .commit();

    }

    private void openRateAccommodationWindow(View view) {
        ToastUtils.showShortInformationMessage(this.getContext(), "Rate");
    }

    private void watchAccommodationMap(View view) {
        ToastUtils.showShortInformationMessage(this.getContext(), "watch map");
    }


    private boolean canBeCancelled(String beginningDate) {
        try {
            ZonedDateTime dateToCompare = ZonedDateTime.parse(beginningDate, formatter);
            LocalDate actualDate = LocalDate.now();
            LocalDate localDateToCompare = dateToCompare.toLocalDate();
            long daysBetween = ChronoUnit.DAYS.between(actualDate, localDateToCompare);
            return (daysBetween > 1);
        } catch (DateTimeParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean isHost(){
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(this.getContext(),"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        DataStoreHelper dataStoreHelper = new DataStoreHelper(this.getActivity(), dataStoreRX);
        return dataStoreHelper.getBoolValue("START_HOST");
    }
}