package com.sdi.hostedin.feature.host.bookings.list;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.sdi.hostedin.databinding.FragmentAccommodationBookingsListBinding;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HostAccommodationBookingsListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HostAccommodationBookingsListFragment extends DialogFragment {

    private String accommodationId;
    private FragmentAccommodationBookingsListBinding binding;
    private HostAccBookingsListViewModel hostAccBookingsListViewModel;

    private HostBookingsListAdapter hostBookingsListAdapter;

    public HostAccommodationBookingsListFragment(String accommodationId) {
        this.accommodationId = accommodationId;
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
        hostBookingsListAdapter.setOnItemClicListener(v->{
            ToastUtils.showShortInformationMessage(getContext(), "Ver detalles");
        });
        hostAccBookingsListViewModel.getBookingList().observe(getViewLifecycleOwner(), bookings ->{
            hostBookingsListAdapter.submitList(bookings);
        });
        hostAccBookingsListViewModel.getBookingsOfAccommodation(accommodationId);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        // Configurar el tamaño del dialog fragment
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = getResources().getDisplayMetrics().heightPixels / 2; // La mitad de la pantalla
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setGravity(Gravity.BOTTOM); // Posiciona en la parte inferior
        }
    }
}