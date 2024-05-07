package com.sdi.hostedin.feature.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.ActivityDeleteAccountBinding;

public class DeleteAccountActivity extends AppCompatActivity {

    private ActivityDeleteAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }
}