package com.sdi.hostedin.feature.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.ActivityProfileBinding;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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