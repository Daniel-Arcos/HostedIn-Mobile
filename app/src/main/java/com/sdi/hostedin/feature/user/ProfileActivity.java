package com.sdi.hostedin.feature.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.ActivityProfileBinding;
import com.sdi.hostedin.feature.host.accommodations.AccommodationFormActivity;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        RelativeLayout bottomSheet = findViewById(R.id.standard_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setDraggable(false);
        binding.changePasswordBtn.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));
        binding.cancelChangePasswordBtn.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));
        binding.myAccountBtn.setOnClickListener( v -> openEditProfileActivity());
        binding.deleteAccountBtn.setOnClickListener( v -> openDeleteAccountActivity());
    }

    private void openEditProfileActivity() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        startActivity(intent);
    }

    private void openDeleteAccountActivity() {
        Intent intent = new Intent(this, DeleteAccountActivity.class);
        startActivity(intent);
    }
}