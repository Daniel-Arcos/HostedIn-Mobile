package com.sdi.hostedin.feature.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sdi.hostedin.MainActivity;
import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.ActivityProfileBinding;
import com.sdi.hostedin.utils.ImageUtils;
import com.sdi.hostedin.utils.ProgressBarUtils;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private User user;
    private BottomSheetBehavior bottomSheetBehavior;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        configureBottomSheet();
        configureButtons();

        profileViewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication())).get(ProfileViewModel.class);
        manageProgressBarCircle();
        //loadUserAccountData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        manageProgressBarCircle();
        loadUserAccountData();
    }

    private void configureBottomSheet() {
        RelativeLayout bottomSheet = findViewById(R.id.standard_bottom_sheet);
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        bottomSheetBehavior.setDraggable(false);
    }

    private void configureButtons() {
        binding.btnChangePassword.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED));
        binding.cancelChangePasswordBtn.setOnClickListener(v -> bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN));
        binding.btnMyAccount.setOnClickListener( v -> openEditProfileActivity());
        binding.btnDeleteAccount.setOnClickListener( v -> openDeleteAccountActivity());
    }

    private void manageProgressBarCircle() {
        ProgressBarUtils.observeRequestStatus(profileViewModel.getRequestStatusMutableLiveData(),
                this,
                binding.pgbProfile,
                this);
    }

    private void loadUserAccountData() {
        // TODO: Get user id from DataStorage
        String userId = "66467c2bdc480001e4adc8bb";

        profileViewModel.getUserById(userId);
        profileViewModel.getUserMutableLiveData().observe(this, user -> {
            if (user != null) {
                this.user = user;
                binding.txvFullName.setText(user.getFullName());
                ImageUtils.loadProfilePhoto(user, binding.imvProfilePhoto);
            } else {
                goToLogin();
            }
        });
    }

    private void openEditProfileActivity() {
        Intent intent = new Intent(this, EditProfileActivity.class);
        if (this.user != null) {
            intent.putExtra(EditProfileActivity.USER_KEY, user);
            startActivity(intent);
        } else {
            ToastUtils.showShortInformationMessage(this, "El usuario viene nulo");
            goToLogin();
        }
    }

    private void openDeleteAccountActivity() {
        Intent intent = new Intent(this, DeleteAccountActivity.class);
        if (this.user != null) {
            intent.putExtra(DeleteAccountActivity.USER_KEY, user);
            startActivity(intent);
        } else {
            ToastUtils.showShortInformationMessage(this, "El usuario viene nulo");
            goToLogin();
        }
    }

    private void goToLogin() {
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}