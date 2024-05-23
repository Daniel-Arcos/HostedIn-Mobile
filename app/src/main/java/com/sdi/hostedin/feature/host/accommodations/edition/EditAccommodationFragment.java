package com.sdi.hostedin.feature.host.accommodations.edition;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

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
       configurateSections();


        return binding.getRoot();
    }

    private void setAccommodatioInfo(){
        binding.txvTitle.setText(accommodation.getTitle());
        binding.txvPrice.setText("$ " + String.valueOf(accommodation.getNightPrice()) + " MXN");
        binding.txvDescription.setText(accommodation.getDescription());
        binding.txvServices.setText("Services: " + accommodation.getAccommodationServices());
        binding.vflpAccommodationMultimedia.setBackgroundColor(Color.LTGRAY);
    }

    private void configurateSections(){
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
    }

    private void editAccommodationMedia(View view) {
    }

    private void editAccommodationServices(View view) {
    }

    private void editAccommodationNumberGuest(View view) {
    }

    private void editAccommodationUbication(View view) {
    }

    private void editAccommodationType(View view) {
    }
}