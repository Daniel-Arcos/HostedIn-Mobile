package com.sdi.hostedin.feature.host.accommodations.edition;

import android.net.Uri;
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
import com.sdi.hostedin.utils.ImageUtils;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class EditAccommodationFormFragment extends Fragment {

    FragmentEditAccommodationFormBinding binding;
    public static final String ACCOMMODATION_KEY = "accommodation_key";
    public static final String SECTION_TO_EDIT_KEY = "section_to_edit_key";
    private static final int LOCAL_FRAGMENT_NUMBER = 2;
    public static final int EDIT_TYPE = 1;
    public static final int EDIT_MAP_UBICATION = 2;
    public static final int EDIT_NUMBERS_OF = 3;
    public static final int EDIT_SERVICES = 4;
    public static final int EDIT_MULTIMEDIA = 5;
    public static final int EDIT_DESCRIPTIONS = 6;

    private static Accommodation accommodationToEdit;

    private Fragment currentFragment;
    private AccommodationFormViewModel accommodationFormViewModel;
    private static int sectionToEdit;
    private static List<byte[]> multimedia;

    public EditAccommodationFormFragment() {
        // Required empty public constructor
    }

    public static EditAccommodationFormFragment newInstance(Accommodation accommodation, int section, List<byte[]> multi) {
        EditAccommodationFormFragment fragment = new EditAccommodationFormFragment();
        Bundle args = new Bundle();
        accommodationToEdit = accommodation;
        sectionToEdit = section;
        multimedia = multi;
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
        accommodationFormViewModel = new ViewModelProvider(this, new ViewModelFactory(getActivity().getApplication())).get(AccommodationFormViewModel.class);

        accommodationFormViewModel.setAccommodationMutableLiveData(accommodationToEdit);
        binding.btnSaveChanges.setOnClickListener( v -> updateAccommodation());
        manageProgressBar();

        choseFragment();
        return binding.getRoot();
    }


    @Override
    public void onResume() {
        super.onResume();
    }


    private void updateAccommodation() {
        switch (sectionToEdit){
            case EDIT_TYPE:
                AccommodationTypeFragment accommodationTypeFragment = (AccommodationTypeFragment) currentFragment;
                accommodationTypeFragment.ValidateAccommodationTypeSelected();
                break;
            case EDIT_MAP_UBICATION:
                AccommodationLocationFragment accommodationLocationFragment = (AccommodationLocationFragment) currentFragment;
                accommodationLocationFragment.ValidateAccommodationLocationSelected();
                break;
            case EDIT_NUMBERS_OF:
                AccommodationBasicsFragment accommodationBasicsFragment = (AccommodationBasicsFragment) currentFragment;
                accommodationBasicsFragment.SetBasicsForEdition();
                break;
            case EDIT_SERVICES:
                AccommodationServicesFragment accommodationServicesFragment = (AccommodationServicesFragment) currentFragment;
                accommodationServicesFragment.validateAccommodationServicesSelected();
                break;
            case EDIT_MULTIMEDIA:
                AccommodationMultimediaFragment accommodationMultimediaFragment = (AccommodationMultimediaFragment) currentFragment;
                accommodationMultimediaFragment.validateAccommodationMultimediaSelected();
                break;
            case EDIT_DESCRIPTIONS:
                AccommodationInformationFragment accommodationInformationFragment = (AccommodationInformationFragment) currentFragment;
                accommodationInformationFragment.collectInformation();
                break;
            default:
                break;
        }
        accommodationToEdit = accommodationFormViewModel.getAccommodationMutableLiveData().getValue();
        accommodationFormViewModel.updateAccommodation(accommodationToEdit);
    }

    private void choseFragment(){
        switch (sectionToEdit){
            case EDIT_TYPE:
                accommodationFormViewModel.nextFragment(1);
                AccommodationTypeFragment accommodationTypeFragment = AccommodationTypeFragment.newInstance(accommodationToEdit, true, accommodationFormViewModel);
                loadFragment(accommodationTypeFragment);
                break;
            case EDIT_MAP_UBICATION:
                accommodationFormViewModel.nextFragment(2);
                AccommodationLocationFragment accommodationLocationFragment = AccommodationLocationFragment.newInstance(accommodationToEdit, true, accommodationFormViewModel);
                loadFragment(accommodationLocationFragment);
                break;
            case EDIT_NUMBERS_OF:
                accommodationFormViewModel.nextFragment(3);
                AccommodationBasicsFragment accommodationBasicsFragment = AccommodationBasicsFragment.newInstance(accommodationToEdit, true, accommodationFormViewModel);
                loadFragment(accommodationBasicsFragment);
                break;
            case EDIT_SERVICES:
                accommodationFormViewModel.nextFragment(4);
                AccommodationServicesFragment accommodationServices = AccommodationServicesFragment.newInstance(accommodationToEdit,true, accommodationFormViewModel);
                loadFragment(accommodationServices);
                break;
            case EDIT_MULTIMEDIA:
                AccommodationMultimediaFragment accommodationMultimediaFragment = AccommodationMultimediaFragment.newInstance( multimedia,true, accommodationFormViewModel);
                loadMultimediasUris();
                loadFragment(accommodationMultimediaFragment);
                accommodationFormViewModel.nextFragment(5);
                break;
            case EDIT_DESCRIPTIONS:
                accommodationFormViewModel.nextFragment(6);
                AccommodationInformationFragment accommodationInformationFragment = AccommodationInformationFragment.newInstance(accommodationToEdit,true, accommodationFormViewModel);
                loadFragment(accommodationInformationFragment);
                break;
            default:
                break;
        }
    }

    private void loadMultimediasUris()  {
        int i = 0;
        for(byte[] media:multimedia) {
            try {
                File file;
                Uri uri;
                if (i < 3) {
                    file = ImageUtils.createTempFileFromBytes(getActivity(), media, String.valueOf(LocalDateTime.now()));
                    uri = ImageUtils.getUriFromFile(getActivity(), file);
                    List<Uri> uris =  accommodationFormViewModel.getImagesUri().getValue();
                    uris.add(uri);
                    accommodationFormViewModel.getImagesUri().setValue(uris);
                }
                else{
                    file = ImageUtils.createTempVideoFile(getActivity(), media);
                    uri = Uri.fromFile(file);
                    accommodationFormViewModel.getVideoUri().setValue(uri);
                }
            } catch (IOException e) {

            }
            i++;
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
        currentFragment = fragment;
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
                    if(accommodationFormViewModel.getFragmentNumberMutableLiveData().getValue() != AccommodationMultimediaFragment.LOCAL_FRAGMENT_NUMBER){
                        requireActivity().getSupportFragmentManager().popBackStack();
                    }
                    break;
                case ERROR:
                    binding.pgbSaveEditedAccommodation.setVisibility(View.GONE);
                    ToastUtils.showShortInformationMessage(getContext(), status.getMessage());
            }
        });
    }



}