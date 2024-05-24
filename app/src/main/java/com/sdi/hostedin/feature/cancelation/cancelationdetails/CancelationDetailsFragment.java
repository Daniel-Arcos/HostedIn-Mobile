package com.sdi.hostedin.feature.cancelation.cancelationdetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.Cancellation;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.FragmentCancelationDetailsBinding;
import com.sdi.hostedin.utils.DateFormatterUtils;

import java.text.SimpleDateFormat;


public class CancelationDetailsFragment extends Fragment {

    public static final String CANCELATION = "cancelation";
    FragmentCancelationDetailsBinding binding;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd"); // Formato para la fecha
    private SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm:ss"); // Formato para la hora


    public CancelationDetailsFragment() {
        // Required empty public constructor
    }

    public static CancelationDetailsFragment newInstance(String param1, String param2) {
        CancelationDetailsFragment fragment = new CancelationDetailsFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentCancelationDetailsBinding.inflate(inflater, container, false);
        Cancellation cancellation = getArguments().getParcelable(CANCELATION);
        showData(cancellation);
        binding.btnAccept.setOnClickListener(v -> requireActivity().finish());


        return binding.getRoot();

    }

    private void showData(Cancellation cancellation) {
        if (cancellation != null) {
            Accommodation accommodation = cancellation.getBooking().getAccommodation();
            binding.txvTitleAccommodation.setText(accommodation.getTitle());
            binding.txvTypeAccommodation.setText(accommodation.getAccommodationType());
            binding.txvPlaceAccommodation.setText(accommodation.getLocation().getAddress());
            binding.txvAccommodationPrice.setText(String.valueOf(accommodation.getNightPrice()));

            User host = accommodation.getUser();
            binding.inclHostData.txvHostName.setText(host.getFullName());
            binding.inclHostData.txvHostPhoneNumber.setText(host.getPhoneNumber());

            binding.txvDate.setText(dateFormatter.format(cancellation.getCancellationDate()));
            binding.txvTime.setText(timeFormatter.format(cancellation.getCancellationDate()));
            binding.txvReason.setText(cancellation.getReason());

        }
    }
}