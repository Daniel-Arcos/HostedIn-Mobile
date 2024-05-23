package com.sdi.hostedin.feature.cancelation.cancelationdetails;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.Cancellation;
import com.sdi.hostedin.databinding.FragmentCancelationDetailsBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CancelationDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CancelationDetailsFragment extends Fragment {

    public static final String CANCELATION = "cancelation";
    FragmentCancelationDetailsBinding binding;

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

        binding.txvTitle.setText(cancellation.getBooking().getAccommodation().getTitle());
        return binding.getRoot();

    }
}