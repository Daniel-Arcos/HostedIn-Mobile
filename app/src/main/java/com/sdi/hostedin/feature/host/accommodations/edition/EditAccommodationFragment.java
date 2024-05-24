package com.sdi.hostedin.feature.host.accommodations.edition;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.databinding.FragmentEditAccommodationBinding;

public class EditAccommodationFragment extends Fragment {

    private FragmentEditAccommodationBinding binding;
    private static Accommodation accommodation;
    public EditAccommodationFragment() {

    }

    public static EditAccommodationFragment newInstance(Accommodation accommodation) {
        EditAccommodationFragment fragment = new EditAccommodationFragment();
        Bundle args = new Bundle();
        EditAccommodationFragment.accommodation = accommodation;
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
        binding = FragmentEditAccommodationBinding.inflate(inflater, container, false);
       setAccommodatioInfo();
       configureSections();


        return binding.getRoot();
    }

    private void setAccommodatioInfo(){
        binding.txvTitle.setText(accommodation.getTitle());
        binding.txvPrice.setText("$ " + String.valueOf(accommodation.getNightPrice()) + " MXN");
        binding.txvDescription.setText(accommodation.getDescription());
        binding.txvServices.setText("Services: " + accommodation.getAccommodationServices().toString());
        binding.vflpAccommodationMultimedia.setBackgroundColor(Color.LTGRAY);
    }

    private void configureSections(){
        binding.inclAccommodationType.imageView.setBackgroundResource(R.drawable.accommodation_icon);
        binding.inclAccommodationType.txvGenericText.setText("Accommodation type");
        binding.inclAccommodationType.bttGenericButton.setOnClickListener(this::editAccommodationType);

        binding.inclUbication.imageView.setBackgroundResource(R.drawable.map_icon);
        binding.inclUbication.txvGenericText.setText("Ubication");
        binding.inclUbication.bttGenericButton.setOnClickListener(this::editAccommodationUbication);

        binding.inclNumberGuests.imageView.setBackgroundResource(R.drawable.bed_icon);
        binding.inclNumberGuests.txvGenericText.setText("Rooms and guests...");
        binding.inclNumberGuests.bttGenericButton.setOnClickListener(this::editAccommodationNumberGuest);

        binding.inclServices.imageView.setBackgroundResource(R.drawable.service_icon);
        binding.inclServices.txvGenericText.setText("Services");
        binding.inclServices.bttGenericButton.setOnClickListener(this::editAccommodationServices);

        binding.inclMedia.imageView.setBackgroundResource(R.drawable.camera_icon);
        binding.inclMedia.txvGenericText.setText("Pictures and video");
        binding.inclMedia.bttGenericButton.setOnClickListener(this::editAccommodationMedia);

        binding.inclPublicationInfo.imageView.setBackgroundResource(R.drawable.edit_acc_icon);
        binding.inclPublicationInfo.txvGenericText.setText("Title, description and price");
        binding.inclPublicationInfo.bttGenericButton.setOnClickListener(this::editAccommodationInfo);

        binding.bttDeleteAccommodation.setOnClickListener(this::deleteAccommodation);
    }

    private void deleteAccommodation(View view) {

    }

    private void editAccommodationInfo(View view) {
        EditAccommodationFormFragment editAccommodationFormFragment = EditAccommodationFormFragment.newInstance(accommodation, EditAccommodationFormFragment.EDIT_DESCRIPTIONS);
        loadFragmentEdition(editAccommodationFormFragment);
    }

    private void editAccommodationMedia(View view) {
        EditAccommodationFormFragment editAccommodationFormFragment = EditAccommodationFormFragment.newInstance(accommodation, EditAccommodationFormFragment.EDIT_MULTIMEDIA);
        loadFragmentEdition(editAccommodationFormFragment);
    }

    private void editAccommodationServices(View view) {
        EditAccommodationFormFragment editAccommodationFormFragment = EditAccommodationFormFragment.newInstance(accommodation, EditAccommodationFormFragment.EDIT_SERVICES);
        loadFragmentEdition(editAccommodationFormFragment);
    }

    private void editAccommodationNumberGuest(View view) {
        EditAccommodationFormFragment editAccommodationFormFragment = EditAccommodationFormFragment.newInstance(accommodation, EditAccommodationFormFragment.EDIT_NUMBERS_OF);
        loadFragmentEdition(editAccommodationFormFragment);
    }

    private void editAccommodationUbication(View view) {
        EditAccommodationFormFragment editAccommodationFormFragment = EditAccommodationFormFragment.newInstance(accommodation, EditAccommodationFormFragment.EDIT_MAP_UBICATION);
        loadFragmentEdition(editAccommodationFormFragment);
    }

    private void editAccommodationType(View view) {
        EditAccommodationFormFragment editAccommodationFormFragment = EditAccommodationFormFragment.newInstance(accommodation, EditAccommodationFormFragment.EDIT_TYPE);
        loadFragmentEdition(editAccommodationFormFragment);
    }

    private void loadFragmentEdition(Fragment fragment) {
        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fgcv_main_edit_accomm_fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}