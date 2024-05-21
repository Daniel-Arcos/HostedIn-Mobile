package com.sdi.hostedin.feature.host.bookings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.FragmentHostBookedAccommodationsBinding;
import com.sdi.hostedin.feature.guest.GuestMainActivity;
import com.sdi.hostedin.feature.host.bookings.list.HostAccommodationBookingsListFragment;
import com.sdi.hostedin.feature.signup.SignupViewModel;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HostBookedAccommodationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostBookedAccommodationsFragment extends Fragment {

    public static final String USER_KEY = "user_key";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private HostBookedAccommodationsViewModel hostBookingsViewModel;

    private FragmentHostBookedAccommodationsBinding binding;


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
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding =  FragmentHostBookedAccommodationsBinding.inflate(getLayoutInflater());
        hostBookingsViewModel =
                new ViewModelProvider(getActivity(), new ViewModelFactory(requireActivity().getApplication())).get(HostBookedAccommodationsViewModel.class);
        User user = (User) getArguments().getParcelable(USER_KEY);
        hostBookingsViewModel.setUserMutableLiveData(user);
        if (!hostBookingsViewModel.getUserMutableLiveData().getValue().getRoles().contains("Guest")) {
            binding.btnChangeToHost.setVisibility(View.GONE);
        }
        binding.bttPrueba.setOnClickListener(v->{
            HostAccommodationBookingsListFragment hostAccommodationBookingsListFragment =  new HostAccommodationBookingsListFragment("664b5263d67d7d6d857fcfeb");
            hostAccommodationBookingsListFragment.show(getChildFragmentManager(),"BookingList");
        });
        binding.btnChangeToHost.setOnClickListener(v -> {goToGuestActivity();});
        return binding.getRoot();
    }

    private void goToGuestActivity() {
        Intent intent = new Intent(this.getActivity(), GuestMainActivity.class);
        if (hostBookingsViewModel.getUserMutableLiveData().getValue() != null) {
            intent.putExtra(GuestMainActivity.USER_KEY, hostBookingsViewModel.getUserMutableLiveData().getValue());
            startActivity(intent);
            this.getActivity().finish();
        } else {
            ToastUtils.showShortInformationMessage(this.getContext(), "Ocurrio un problema");
        }
    }
}