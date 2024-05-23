package com.sdi.hostedin.feature.host.accommodations.edition;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.databinding.ActivityEditAccommodationBinding;

public class EditAccommodationActivity extends AppCompatActivity {

    public final static String ACCOMMODATION_KEY = "accomodation_key";
    private ActivityEditAccommodationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditAccommodationBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        Accommodation accommodation = extras.getParcelable(ACCOMMODATION_KEY);
        EditAccommodationFragment editAccommodationFragment = EditAccommodationFragment.newInstance(accommodation);
        getSupportFragmentManager().beginTransaction()
                .add(binding.fgcvMainEditAccommFragmentContainer.getId(), editAccommodationFragment)
                .commit();
    }
}