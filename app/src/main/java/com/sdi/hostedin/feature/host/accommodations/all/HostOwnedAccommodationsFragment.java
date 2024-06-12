package com.sdi.hostedin.feature.host.accommodations.all;

import android.content.Intent;
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
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.databinding.FragmentHostOwnedAccommodationsBinding;
import com.sdi.hostedin.feature.guest.explore.accommodationdetails.AccommodationDetailsActivity;
import com.sdi.hostedin.feature.host.accommodations.edition.EditAccommodationActivity;
import com.sdi.hostedin.feature.host.accommodations.accommodationform.AccommodationFormActivity;
import com.sdi.hostedin.utils.ViewModelFactory;

public class HostOwnedAccommodationsFragment extends Fragment {

    private FragmentHostOwnedAccommodationsBinding binding;
    private HostOwnedAccommodationsViewModel hostOwnedAccommodationsViewModel;
    private HostOwnedAccommodationsAdapter hostOwnedAccommodationsAdapter;
    public HostOwnedAccommodationsFragment() {  }

    public static HostOwnedAccommodationsFragment newInstance() {
        HostOwnedAccommodationsFragment fragment = new HostOwnedAccommodationsFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade));
        setExitTransition(inflater.inflateTransition(R.transition.fade));

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHostOwnedAccommodationsBinding.inflate(inflater, container, false);
        binding.btnCreateAccommodation.setOnClickListener(v -> goToAccommodationForm());
        binding.recyclerPublicationView.setLayoutManager(new LinearLayoutManager(this.getContext()));

        hostOwnedAccommodationsViewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(requireActivity().getApplication())).get(HostOwnedAccommodationsViewModel.class);
        if(Boolean.TRUE.equals(hostOwnedAccommodationsViewModel.getIsNew().getValue())){
            hostOwnedAccommodationsViewModel.getAllHostOwnedAccommodations();
            hostOwnedAccommodationsViewModel.setIsNew(false);
        }

        hostOwnedAccommodationsAdapter = new HostOwnedAccommodationsAdapter(getContext());
        hostOwnedAccommodationsAdapter.setOnItemClickListener(this::watchAccommodationsDetails);
        hostOwnedAccommodationsAdapter.setOnEditButtonClick(this::goToEditAccommodation);
        binding.recyclerPublicationView.setAdapter(hostOwnedAccommodationsAdapter);
        hostOwnedAccommodationsViewModel.getAccommodations().observe(getViewLifecycleOwner(), accommodations -> {
            hostOwnedAccommodationsAdapter.submitList(accommodations);
            if (accommodations.size() < 0) { binding.txvNoAccommodations.setVisibility(View.VISIBLE); }
        });
        manageLoading();

        return binding.getRoot();
    }

    private void goToEditAccommodation(Accommodation accommodation) {
        Intent intent = new Intent(this.getActivity(), EditAccommodationActivity.class);
        intent.putExtra(EditAccommodationActivity.ACCOMMODATION_KEY, accommodation);
        startActivity(intent);
    }

    private void watchAccommodationsDetails(Accommodation accommodation) {
        Intent intent = new Intent(this.getActivity(), AccommodationDetailsActivity.class);
        intent.putExtra(AccommodationDetailsActivity.ACCOMMODATION_KEY, accommodation);
        intent.putExtra(AccommodationDetailsActivity.IS_HOST_KEY, true);
        startActivity(intent);
    }

    private void goToAccommodationForm() {
        getFragmentManager().popBackStack();
        Intent intent = new Intent(this.getActivity(), AccommodationFormActivity.class);
        startActivity(intent);
    }

    private void manageLoading() {
        hostOwnedAccommodationsViewModel.getRequestStatusMutableLiveData().observe(getViewLifecycleOwner(), status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.txvNoAccommodations.setVisibility(View.VISIBLE);
                    binding.pgbLoadingWheel.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.txvNoAccommodations.setVisibility(View.INVISIBLE);
                    binding.pgbLoadingWheel.setVisibility(View.GONE);
                    break;
                case ERROR:
                    binding.txvNoAccommodations.setVisibility(View.VISIBLE);
                    binding.pgbLoadingWheel.setVisibility(View.GONE);
                    Toast.makeText(this.getContext(),status.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}