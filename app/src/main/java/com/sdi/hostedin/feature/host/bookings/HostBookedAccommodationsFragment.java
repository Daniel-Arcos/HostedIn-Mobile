package com.sdi.hostedin.feature.host.bookings;

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
import com.sdi.hostedin.data.model.BookedAccommodation;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.FragmentHostBookedAccommodationsBinding;
import com.sdi.hostedin.feature.guest.GuestMainActivity;
import com.sdi.hostedin.feature.host.bookings.list.HostAccommodationBookingsListFragment;
import com.sdi.hostedin.feature.user.ProfileActivity;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;


public class HostBookedAccommodationsFragment extends Fragment {

    public static final String USER_KEY = "user_key";
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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  FragmentHostBookedAccommodationsBinding.inflate(inflater, container, false);
        binding.rcyvBookedPublications.setLayoutManager(new LinearLayoutManager(this.getContext()));

        hostBookedAccommodationsViewModel = new ViewModelProvider(requireActivity(), new ViewModelFactory(getActivity().getApplication())).get(HostBookedAccommodationsViewModel.class);
        if (Boolean.TRUE.equals(hostBookedAccommodationsViewModel.getIsNew().getValue())){
            hostBookedAccommodationsViewModel.getHostBookedAccommodations();
            hostBookedAccommodationsViewModel.setIsNew(false);
        }

        User user = (User) getArguments().getParcelable(USER_KEY);
        hostBookedAccommodationsViewModel.setUserMutableLiveData(user);
        if (!hostBookedAccommodationsViewModel.getUserMutableLiveData().getValue().getRoles().contains("Guest")) {
            binding.btnChangeToHost.setVisibility(View.GONE);
        }
        binding.btnChangeToHost.setOnClickListener(v -> {goToGuestActivity();});
        binding.profileBtn.setOnClickListener(v -> changeToUserProfile());

        hostBookedAccommodationsAdapter = new HostBookedAccommodationsAdapter(this.getContext());
        hostBookedAccommodationsAdapter.setOnItemClickListener(this:: watchBookings);
        binding.rcyvBookedPublications.setAdapter(hostBookedAccommodationsAdapter);
        hostBookedAccommodationsViewModel.getAccommodations().observe(getViewLifecycleOwner(), accommodations -> {
            hostBookedAccommodationsAdapter.submitList(accommodations);
            if(accommodations.size() < 0) {binding.txvNoAccommodations.setVisibility(View.VISIBLE);}
        });

        binding.swiperefresh.setOnRefreshListener(this:: refreshBookedAccommodations);

        manageLoading();
        return binding.getRoot();
    }

    private void refreshBookedAccommodations() {
        hostBookedAccommodationsViewModel.getHostBookedAccommodations();
        binding.swiperefresh.setRefreshing(false);
    }

    private void watchBookings(BookedAccommodation accommodation) {
        HostAccommodationBookingsListFragment hostAccommodationBookingsListFragment =  new HostAccommodationBookingsListFragment(accommodation);
        hostAccommodationBookingsListFragment.show(getChildFragmentManager(),"BookingList");
    }

    private void manageLoading() {
        hostBookedAccommodationsViewModel.getRequestStatusMutableLiveData().observe(getViewLifecycleOwner(), status -> {
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

    private void goToGuestActivity() {
        Intent intent = new Intent(this.getActivity(), GuestMainActivity.class);
        if (hostBookedAccommodationsViewModel.getUserMutableLiveData().getValue() != null) {
            intent.putExtra(GuestMainActivity.USER_KEY, hostBookedAccommodationsViewModel.getUserMutableLiveData().getValue());
            startActivity(intent);
            this.getActivity().finish();
        } else {
            ToastUtils.showShortInformationMessage(this.getContext(), String.valueOf(R.string.there_is_a_problem));
        }
    }

    private void changeToUserProfile() {
        Intent intent = new Intent(getContext(), ProfileActivity.class);
        startActivity(intent);
    }
}