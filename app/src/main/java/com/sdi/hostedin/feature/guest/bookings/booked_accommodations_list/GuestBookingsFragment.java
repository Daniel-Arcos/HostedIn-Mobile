package com.sdi.hostedin.feature.guest.bookings.booked_accommodations_list;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.Booking;
import com.sdi.hostedin.data.model.GuestBooking;
import com.sdi.hostedin.databinding.FragmentGuestBookingsBinding;
import com.sdi.hostedin.feature.guest.bookings.details.BookingDetailsActivity;
import com.sdi.hostedin.feature.guest.bookings.review.ReviewAccommodationFragment;
import com.sdi.hostedin.utils.ViewModelFactory;

public class GuestBookingsFragment extends Fragment  {
    public GuestBookingsFragment() {
        // Required empty public constructor
    }

    private FragmentGuestBookingsBinding binding;
    private GuestBookingsViewModel guestBookingsViewModel;
    private GuestBookingsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade));
        setExitTransition(inflater.inflateTransition(R.transition.fade));

    }

    @Override
    public void onResume() {
        super.onResume();
        if(Boolean.TRUE.equals(guestBookingsViewModel.getCurrentAccView().getValue())){
            getCurrentBookings();
        }
        else{
            getOldBookings();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGuestBookingsBinding.inflate(inflater, container, false);
        binding.setLifecycleOwner(this);


        guestBookingsViewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(getActivity().getApplication())).get(GuestBookingsViewModel.class);

        binding.recyclerPublicationView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.bttOlds.setOnClickListener(v -> getOldBookings());
        binding.bttCurrents.setOnClickListener(v -> getCurrentBookings());

        adapter = new GuestBookingsAdapter(this.getContext(), true);
        adapter.setOnItemClickListener(this::watchBookingDetails);
        adapter.setOnRateButtonClick(this::rateAccommodation);
        binding.recyclerPublicationView.setAdapter(adapter);
        guestBookingsViewModel.getBookedAccommodations().observe(getViewLifecycleOwner(), accommodations ->{
            adapter.submitList(accommodations);
            if(accommodations.size() > 0) binding.txvNoAccommodations.setVisibility(View.INVISIBLE);
        });

        binding.swiperefresh.setOnRefreshListener(this::refreshView);

        manageLoading();


        if(Boolean.TRUE.equals(guestBookingsViewModel.getCurrentAccView().getValue())){
            adapter.setShowButton(false);
            binding.bttOlds.setBackgroundColor(Color.LTGRAY);
            binding.bttCurrents.setBackgroundColor(Color.BLUE);
        }
        else{
            adapter.setShowButton(true);
            binding.bttCurrents.setBackgroundColor(Color.LTGRAY);
            binding.bttOlds.setBackgroundColor(Color.BLUE);
        }

        return binding.getRoot();
    }

    private void getCurrentBookings() {
        guestBookingsViewModel.getCurrentAccView().setValue(true);
        adapter.setShowButton(false);
        binding.bttOlds.setBackgroundColor(Color.LTGRAY);
        binding.bttCurrents.setBackgroundColor(Color.BLUE);
        guestBookingsViewModel.getCurrentBookedAccommodations();
    }

    private void getOldBookings() {
        guestBookingsViewModel.getCurrentAccView().setValue(false);
        adapter.setShowButton(true);
        binding.bttCurrents.setBackgroundColor(Color.LTGRAY);
        binding.bttOlds.setBackgroundColor(Color.BLUE);
        guestBookingsViewModel.getOverDueBookedAccommodations();
    }

    private void rateAccommodation(GuestBooking booking) {
        String userId = DataStoreAccess.accessUserId(getActivity().getApplication());
        ReviewAccommodationFragment reviewAccommodationFragment = ReviewAccommodationFragment.newInstance(booking.getAccommodation().getId(), userId);
        reviewAccommodationFragment.show(getChildFragmentManager(),"ReviewAccommodation");
    }

    private void watchBookingDetails(GuestBooking booking) {
        Intent intent = new Intent(this.getActivity(), BookingDetailsActivity.class);
        intent.putExtra(BookingDetailsActivity.THIRD_USER_KEY, booking.getAccommodation().getUser());
        intent.putExtra(BookingDetailsActivity.BOOKING_KEY, convertToBooking(booking));
        startActivity(intent);
    }

    @NonNull
    private Booking convertToBooking(GuestBooking guestBooking){
        Booking booking = new Booking();
                booking.set_id(guestBooking.get_id());
                booking.setAccommodation(guestBooking.getAccommodation());
                booking.setBeginningDate(guestBooking.getBeginningDate());
                booking.setEndingDate(guestBooking.getEndingDate());
                booking.setNumberOfGuests(guestBooking.getNumberOfGuests());
                booking.setTotalCost(guestBooking.getTotalCost());
                booking.setBookingStatus(guestBooking.getBookingStatus());
        return booking;
    }

    private void refreshView(){
        if(Boolean.TRUE.equals(guestBookingsViewModel.getCurrentAccView().getValue())){
            guestBookingsViewModel.getCurrentAccWereRecovered().setValue(false);
            getCurrentBookings();
        }
        else{
            guestBookingsViewModel.getOverdueAccWereRecovered().setValue(false);
            getOldBookings();
        }
        binding.swiperefresh.setRefreshing(false);
    }

    private void manageLoading() {
        guestBookingsViewModel.getRequestStatusMutableLiveData().observe(getViewLifecycleOwner(), status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbLoadingWheel.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbLoadingWheel.setVisibility(View.GONE);
                    break;
                case ERROR:
                    binding.pgbLoadingWheel.setVisibility(View.GONE);
                    Toast.makeText(this.getContext(),status.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}