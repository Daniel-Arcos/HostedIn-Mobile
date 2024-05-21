package com.sdi.hostedin.feature.host.bookings.list;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sdi.hostedin.data.model.BookedAccommodation;
import com.sdi.hostedin.data.model.Booking;
import com.sdi.hostedin.databinding.FragmentAccommodationBookingsListBinding;
import com.sdi.hostedin.feature.guest.bookings.booked_accommodations_list.details.BookingDetailsActivity;
import com.sdi.hostedin.utils.ViewModelFactory;

public class HostAccommodationBookingsListFragment extends DialogFragment {

    private BookedAccommodation accommodationBooked;
    private FragmentAccommodationBookingsListBinding binding;
    private HostAccBookingsListViewModel hostAccBookingsListViewModel;

    private HostBookingsListAdapter hostBookingsListAdapter;

    public HostAccommodationBookingsListFragment(){

    }

    public HostAccommodationBookingsListFragment(BookedAccommodation accommodationId) {
        this.accommodationBooked = accommodationId;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAccommodationBookingsListBinding.inflate(getLayoutInflater());
        hostAccBookingsListViewModel = new  ViewModelProvider(getActivity(), new ViewModelFactory(getActivity().getApplication())).get(HostAccBookingsListViewModel.class);
        hostBookingsListAdapter = new HostBookingsListAdapter(getContext());

        binding.bttCloseBooks.setOnClickListener( v -> CloseDialogFragment());

        binding.rcyvBooksItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcyvBooksItems.setAdapter(hostBookingsListAdapter);
        hostAccBookingsListViewModel.getRequestStatusMutableLiveData().observe(getViewLifecycleOwner(), requestStatus -> {
            switch (requestStatus.getRequestStatus()) {
                case LOADING:
                    binding.pgbLoadingWheel.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbLoadingWheel.setVisibility(View.GONE);
                    break;
                case ERROR:
                    Toast.makeText(getActivity(),requestStatus.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.pgbLoadingWheel.setVisibility(View.GONE);
                    Log.e("testTris", requestStatus.getMessage());
            }
        });
        hostBookingsListAdapter.setOnItemClicListener(this:: watchBookingDetails);
        hostAccBookingsListViewModel.getBookingList().observe(getViewLifecycleOwner(), bookings ->{
            hostBookingsListAdapter.submitList(bookings);
        });
        hostAccBookingsListViewModel.getBookingsOfAccommodation(accommodationBooked.get_id());

        return binding.getRoot();
    }

    private void watchBookingDetails(Booking booking) {
        Intent intent = new Intent(getActivity(), BookingDetailsActivity.class);
        intent.putExtra(BookingDetailsActivity.THIRD_USER_KEY, booking.getGuestUser());
        intent.putExtra(BookingDetailsActivity.BOOKING_KEY, booking);
        startActivity(intent);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Configurar el tamaño del dialog fragment
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = getResources().getDisplayMetrics().heightPixels / 2;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setGravity(Gravity.BOTTOM);
        }
    }

    private void CloseDialogFragment(){
        this.dismiss();
    }
}