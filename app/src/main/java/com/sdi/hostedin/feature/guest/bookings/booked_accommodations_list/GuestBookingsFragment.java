package com.sdi.hostedin.feature.guest.bookings.booked_accommodations_list;

import android.graphics.Color;
import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.GuestBooking;
import com.sdi.hostedin.databinding.FragmentGuestBookingsBinding;
import com.sdi.hostedin.utils.ViewModelFactory;

public class GuestBookingsFragment extends Fragment {
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
        guestBookingsViewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(getActivity().getApplication())).get(GuestBookingsViewModel.class);
        if (Boolean.TRUE.equals(guestBookingsViewModel.getIsNew().getValue())){
            guestBookingsViewModel.getCurrentBookedAccommodations();
            guestBookingsViewModel.setIsNew(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentGuestBookingsBinding.inflate(inflater, container, false);
        binding.bttOlds.setBackgroundColor(Color.LTGRAY);

        binding.recyclerPublicationView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        binding.bttOlds.setOnClickListener(v -> getOldBookingds());
        binding.bttCurrents.setOnClickListener(v -> getCurrentBookings());

        adapter = new GuestBookingsAdapter(this.getContext(), true);
        adapter.setOnItemClickListener(this::wathcBookingDetails);
        adapter.setOnRateButtonClick(this::rateAccommodation);
        binding.recyclerPublicationView.setAdapter(adapter);
        guestBookingsViewModel.getBookedAccommodations().observe(getViewLifecycleOwner(), accommodations ->{
            adapter.submitList(accommodations);
            if(accommodations.size() > 0) binding.txvNoAccommodations.setVisibility(View.INVISIBLE);
        });
        manageLoading();
        return binding.getRoot();
    }

    private void getCurrentBookings() {
        binding.bttOlds.setBackgroundColor(Color.LTGRAY);
        binding.bttCurrents.setBackgroundColor(Color.BLUE);
        adapter.setShowButton(false);
        guestBookingsViewModel.getCurrentBookedAccommodations();
    }

    private void getOldBookingds() {
        binding.bttCurrents.setBackgroundColor(Color.LTGRAY);
        binding.bttOlds.setBackgroundColor(Color.BLUE);
        adapter.setShowButton(true);
        guestBookingsViewModel.getOverDueBookedAccommodations();
    }

    private void rateAccommodation(GuestBooking booking) {

    }

    private void wathcBookingDetails(GuestBooking booking) {

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