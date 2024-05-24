package com.sdi.hostedin.feature.cancelation.reasonselection;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.Booking;
import com.sdi.hostedin.data.model.Cancellation;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.FragmentCancelationReasonSelectionBinding;
import com.sdi.hostedin.feature.cancelation.CancelationViewModel;
import com.sdi.hostedin.feature.cancelation.cancelationdetails.CancelationDetailsFragment;
import com.sdi.hostedin.feature.guest.explore.accommodations.ExploreViewModel;
import com.sdi.hostedin.utils.DateFormatterUtils;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

import java.util.Date;


public class CancelationReasonSelectionFragment extends Fragment {

    public static final String BOOKING = "booking";
    private final String GUEST_REASON_ONE = "Ya no necesito un alojamiento";
    private final String GUEST_REASON_TWO = "Hice la reservación por error";
    private final String GUEST_REASON_THREE = "Me incomoda el anfitrión";
    private final String HOST_REASON_ONE = "El huésped no me inspira confianza.";
    private final String HOST_REASON_TWO = "Ocuparé el alojamiento para otra actividad.";
    private final String HOST_REASON_THREE = "Causa de fuerza mayor: siniestro, interrupción de servicios básicos, conflicto armado.";
    private final String GENERAL_REASON = "No deseo especificar el motivo";
    RxDataStore<Preferences> dataStoreRX;
    private Booking booking;
    private CancelationViewModel cancelationViewModel;


    private FragmentCancelationReasonSelectionBinding binding;


    public CancelationReasonSelectionFragment() {
        // Required empty public constructor
    }

    public static CancelationReasonSelectionFragment newInstance(String param1, String param2) {
        CancelationReasonSelectionFragment fragment = new CancelationReasonSelectionFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCancelationReasonSelectionBinding.inflate(inflater, container, false);
        cancelationViewModel =
                new ViewModelProvider(requireActivity(), new ViewModelFactory(getActivity().getApplication())).get(CancelationViewModel.class);
        booking = getArguments().getParcelable(BOOKING);
        configureCancelButton();
        configureReasons();
        configureListeners();
        manageLoading();
        binding.rbtnReasonOne.setChecked(true);
        return binding.getRoot();
    }

    private void manageLoading() {
        cancelationViewModel.getRequestStatusMutableLiveData().observe(getViewLifecycleOwner(), requestStatus -> {
            switch (requestStatus.getRequestStatus()) {
                case LOADING:
                    binding.pgbCancelBooking.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbCancelBooking.setVisibility(View.GONE);
                    goToCancellationDetails();
                    break;
                case ERROR:
                    binding.pgbCancelBooking.setVisibility(View.GONE);
                    Toast.makeText(this.getContext(),requestStatus.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void configureCancelButton() {
        binding.btnCancelBooking.setOnClickListener(v -> cancelBooking());
    }

    public void cancelBooking() {
        if (!cancelationViewModel.getReasonsCancellation().getValue().isEmpty()) {
            cancelationViewModel.cancelBooking(createCancellation());
        } else {
            ToastUtils.showShortInformationMessage(this.getContext(), "Debes seleccionar un motivo");
        }
    }

    public Cancellation createCancellation() {
        Cancellation cancellation = new Cancellation();
        cancellation.setReason(cancelationViewModel.getReasonsCancellation().getValue());
        cancellation.setCancellationDate(new Date());
        User user = new User();
        user.setId(DataStoreAccess.accessUserId(getActivity().getApplication()));
        cancellation.setCancellator(user);
        cancellation.setBooking(booking);
        return cancellation;
    }

    private void goToCancellationDetails() {
        if (cancelationViewModel.getCancellationResponse().getValue() != null) {
            Bundle bundle = new Bundle();
            bundle.putParcelable(CancelationDetailsFragment.CANCELATION, cancelationViewModel.getCancellationResponse().getValue());
            getParentFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(R.id.fgcv_book_details_fragment_container, CancelationDetailsFragment.class, bundle)
                    .commit();
        }
    }

    private void configureListeners() {
        binding.rbtnReasonOne.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cancelationViewModel.getReasonsCancellation().setValue(binding.rbtnReasonOne.getText().toString());
            }
        });
        binding.rbtnReasonTwo.setOnCheckedChangeListener( (buttonView, isChecked) -> {
            cancelationViewModel.getReasonsCancellation().setValue(binding.rbtnReasonTwo.getText().toString());

        });
        binding.rbtnReasonThree.setOnCheckedChangeListener( (buttonView, isChecked) -> {
            cancelationViewModel.getReasonsCancellation().setValue(binding.rbtnReasonThree.getText().toString());

        });
        binding.rbtnReasonThree.setOnCheckedChangeListener( (buttonView, isChecked) -> {
            cancelationViewModel.getReasonsCancellation().setValue(binding.rbtnReasonFour.getText().toString());
        });
    }

    private void configureReasons() {
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(this.getContext(),"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(this.getActivity(), dataStoreRX);
        boolean isHostEstablished = dataStoreHelper.getBoolValue("START_HOST");
        if (isHostEstablished) {
            configureHostReasons();
        } else {
            configureGuestReasons();
        }
    }

    private void configureHostReasons() {
        binding.rbtnReasonOne.setText(HOST_REASON_ONE);
        binding.rbtnReasonTwo.setText(HOST_REASON_TWO);
        binding.rbtnReasonThree.setText(HOST_REASON_THREE);
        binding.rbtnReasonFour.setText(GENERAL_REASON);
    }

    private void configureGuestReasons() {
        binding.rbtnReasonOne.setText(GUEST_REASON_ONE);
        binding.rbtnReasonTwo.setText(GUEST_REASON_TWO);
        binding.rbtnReasonThree.setText(GUEST_REASON_THREE);
        binding.rbtnReasonFour.setText(GENERAL_REASON);
    }
}