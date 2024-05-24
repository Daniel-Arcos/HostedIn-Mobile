package com.sdi.hostedin.feature.host.accommodations.edition;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.databinding.FragmentEditAccommodationFormBinding;
import com.sdi.hostedin.feature.host.accommodations.accommodationform.AccommodationBasicsFragment;
import com.sdi.hostedin.feature.host.accommodations.accommodationform.AccommodationFormViewModel;
import com.sdi.hostedin.feature.host.accommodations.accommodationform.AccommodationInformationFragment;
import com.sdi.hostedin.feature.host.accommodations.accommodationform.AccommodationLocationFragment;
import com.sdi.hostedin.feature.host.accommodations.accommodationform.AccommodationMultimediaFragment;
import com.sdi.hostedin.feature.host.accommodations.accommodationform.AccommodationServicesFragment;
import com.sdi.hostedin.feature.host.accommodations.accommodationform.AccommodationTypeFragment;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

public class EditAccommodationFormFragment extends Fragment {

    FragmentEditAccommodationFormBinding binding;
    public static final String ACCOMMODATION_KEY = "accommodation_key";
    public static final String SECTION_TO_EDIT_KEY = "section_to_edit_key";
    public static final int EDIT_TYPE = 1;
    public static final int EDIT_MAP_UBICATION = 2;
    public static final int EDIT_NUMBERS_OF = 3;
    public static final int EDIT_SERVICES = 4;
    public static final int EDIT_MULTIMEDIA = 5;
    public static final int EDIT_DESCRIPTIONS = 6;

    private static Accommodation accommodationToEdit;

    private AccommodationFormViewModel accommodationFormViewModel;
    private static int sectionToEdit;
    public EditAccommodationFormFragment() {
        // Required empty public constructor
    }

    public static EditAccommodationFormFragment newInstance(Accommodation accommodation, int section) {
        EditAccommodationFormFragment fragment = new EditAccommodationFormFragment();
        Bundle args = new Bundle();
        accommodationToEdit = accommodation;
        sectionToEdit = section;
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentEditAccommodationFormBinding.inflate(inflater, container, false);
        accommodationFormViewModel = new ViewModelProvider(getActivity(), new ViewModelFactory(getActivity().getApplication())).get(AccommodationFormViewModel.class);
        accommodationFormViewModel.setAccommodationMutableLiveData(accommodationToEdit);
        binding.btnSaveChanges.setOnClickListener( v -> updateAccommodation());
        accommodationFormViewModel.getAccommodationMutableLiveData().observe(getViewLifecycleOwner(), acco -> {
            accommodationToEdit = acco;
        });
        manageProgressBar();
        return binding.getRoot();
    }
    @Override
    public void onResume() {
        super.onResume();
        choseFragment();
    }


    private void updateAccommodation() {
        accommodationFormViewModel.nextFragment(accommodationFormViewModel.getFragmentNumberMutableLiveData().getValue());
        accommodationFormViewModel.updateAccommodation(accommodationToEdit);
    }

    private void choseFragment(){
        switch (sectionToEdit){
            case EDIT_TYPE:
                accommodationFormViewModel.nextFragment(1);
                AccommodationTypeFragment accommodationTypeFragment = AccommodationTypeFragment.newInstance(accommodationToEdit, true);
                loadFragment(accommodationTypeFragment);
                break;
            case EDIT_MAP_UBICATION:
                accommodationFormViewModel.nextFragment(2);
                AccommodationLocationFragment accommodationLocationFragment = AccommodationLocationFragment.newInstance(accommodationToEdit, true);
                loadFragment(accommodationLocationFragment);
                break;
            case EDIT_NUMBERS_OF:
                accommodationFormViewModel.nextFragment(3);
                AccommodationBasicsFragment accommodationBasicsFragment = AccommodationBasicsFragment.newInstance(accommodationToEdit, true);
                loadFragment(accommodationBasicsFragment);
                break;
            case EDIT_SERVICES:
                accommodationFormViewModel.nextFragment(4);
                AccommodationServicesFragment accommodationServices = AccommodationServicesFragment.newInstance(accommodationToEdit,true);
                loadFragment(accommodationServices);
                break;
            case EDIT_MULTIMEDIA:
                accommodationFormViewModel.nextFragment(5);
                AccommodationMultimediaFragment accommodationMultimediaFragment = AccommodationMultimediaFragment.newInstance(accommodationToEdit,true);
                loadFragment(accommodationMultimediaFragment);
                break;
            case EDIT_DESCRIPTIONS:
                accommodationFormViewModel.nextFragment(6);
                AccommodationInformationFragment accommodationInformationFragment = AccommodationInformationFragment.newInstance(accommodationToEdit,true);
                loadFragment(accommodationInformationFragment);
                break;
            default:
                break;
        }
    }

    private void loadFragment(Fragment fragment) {
        Fragment existingFragment = getChildFragmentManager().findFragmentById(binding.fragmentContainer.getId());
        if (existingFragment != null) {
            getChildFragmentManager().beginTransaction().remove(existingFragment).commit();
        }
        getChildFragmentManager().beginTransaction()
                .replace(binding.fragmentContainer.getId(), fragment)
                .commit();
    }


    private void manageProgressBar(){
        accommodationFormViewModel.getRequestStatusMutableLiveData().observe(getViewLifecycleOwner(), status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbSaveEditedAccommodation.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbSaveEditedAccommodation.setVisibility(View.GONE);
                    ToastUtils.showShortInformationMessage(getContext(), "Alojamiento actualizado con éxito");
                    break;
                case ERROR:
                    binding.pgbSaveEditedAccommodation.setVisibility(View.GONE);
                    ToastUtils.showShortInformationMessage(getContext(), status.getMessage());
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        accommodationFormViewModel.restartViewModel();
    }

}