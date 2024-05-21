package com.sdi.hostedin.feature.host.bookings;

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
import com.sdi.hostedin.data.model.BookedAccommodation;
import com.sdi.hostedin.databinding.FragmentHostBookedAccommodationsBinding;
import com.sdi.hostedin.feature.host.bookings.list.HostAccommodationBookingsListFragment;
import com.sdi.hostedin.utils.ViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HostBookedAccommodationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostBookedAccommodationsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private FragmentHostBookedAccommodationsBinding binding;
    private HostBookedAccommodationsAdapter hostBookedAccommodationsAdapter;
    private HostBookedAccommodationsViewModel hostBookedAccommodationsViewModel;


    public HostBookedAccommodationsFragment() {
    }

    public static HostBookedAccommodationsFragment newInstance(String param1, String param2) {
        HostBookedAccommodationsFragment fragment = new HostBookedAccommodationsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade));
        setExitTransition(inflater.inflateTransition(R.transition.fade));
        hostBookedAccommodationsViewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(getActivity().getApplication())).get(HostBookedAccommodationsViewModel.class);
        if (Boolean.TRUE.equals(hostBookedAccommodationsViewModel.getIsNew().getValue())){
            hostBookedAccommodationsViewModel.getHostBookedAccommodations();
            hostBookedAccommodationsViewModel.setIsNew(false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  FragmentHostBookedAccommodationsBinding.inflate(inflater, container, false);
        binding.rcyvBookedPublications.setLayoutManager(new LinearLayoutManager(this.getContext()));


        hostBookedAccommodationsAdapter = new HostBookedAccommodationsAdapter(this.getContext());
        hostBookedAccommodationsAdapter.setOnItemClickListener(this:: watchBookings);
        binding.rcyvBookedPublications.setAdapter(hostBookedAccommodationsAdapter);
        hostBookedAccommodationsViewModel.getAccommodations().observe(getViewLifecycleOwner(), accommodations -> {
            hostBookedAccommodationsAdapter.submitList(accommodations);
            if(accommodations.size() > 0) binding.txvNoAccommodations.setVisibility(View.INVISIBLE);
        });
        manageLoading();
        hostBookedAccommodationsViewModel.getHostBookedAccommodations();
        return binding.getRoot();
    }

    private void watchBookings(BookedAccommodation accommodation) {
        HostAccommodationBookingsListFragment hostAccommodationBookingsListFragment =  new HostAccommodationBookingsListFragment(accommodation);
        hostAccommodationBookingsListFragment.show(getChildFragmentManager(),"BookingList");
    }

    private void manageLoading() {
        hostBookedAccommodationsViewModel.getRequestStatusMutableLiveData().observe(getViewLifecycleOwner(), status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.rcyvBookedPublications.setVisibility(View.GONE);
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