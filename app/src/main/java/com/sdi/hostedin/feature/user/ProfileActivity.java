package com.sdi.hostedin.feature.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.sdi.hostedin.MainActivity;
import com.sdi.hostedin.R;
import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.ActivityProfileBinding;
import com.sdi.hostedin.feature.host.accommodations.accommodationform.AccommodationFormActivity;
import com.sdi.hostedin.utils.ImageUtils;
import com.sdi.hostedin.utils.ProgressBarUtils;
import com.sdi.hostedin.utils.TextChangedListener;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

import org.mindrot.jbcrypt.BCrypt;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProfileActivity extends AppCompatActivity {

    private ActivityProfileBinding binding;
    private ProfileViewModel profileViewModel;
    private User user;
    private BottomSheetBehavior bottomSheetBehavior;
    private RxDataStore<Preferences> dataStoreRX;
    private String REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        configureBottomSheet();
        configureButtons();
        profileViewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication())).get(ProfileViewModel.class);
        manageProgressBarCircle();
        manageRequestChangePasswordStatus();
        configurePasswordFields();
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
        binding.btnChangePassword.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            binding.etxCurrentPassword.getEditText().requestFocus();
        });
        binding.cancelChangePasswordBtn.setOnClickListener(v -> {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(
                    getCurrentFocus().getWindowToken(), 0
            );
        });
        binding.btnMyAccount.setOnClickListener( v -> openEditProfileActivity());
        binding.btnDeleteAccount.setOnClickListener( v -> openDeleteAccountActivity());
        binding.btnSavePassword.setOnClickListener(v -> changeUserPassword());
        binding.btnLogout.setOnClickListener( v -> /*goToLogin()*/ goToAccommodationForm() );
    }

    //TODO: DELETE THIS METHOD
    private void goToAccommodationForm() {
        Intent intent = new Intent(this, AccommodationFormActivity.class);
        startActivity(intent);
    }

    private void closeChangePasswordSheet() {
        bottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
        binding.etxCurrentPassword.getEditText().setText("");
        binding.etxNewPassword.getEditText().setText("");
        binding.etxNewPasswordConfirmation.getEditText().setText("");
        binding.txvCurrentPassword.setVisibility(View.GONE);
        binding.txvNewPassword.setVisibility(View.GONE);
        binding.txvNewPasswordConfirmation.setVisibility(View.GONE);
    }

    private void changeUserPassword() {
        if (validateCurrentPassword() && validateNewPassword() && validateNewPasswordConfirmation()) {
            User user = profileViewModel.getUserMutableLiveData().getValue();
            user.setPassword(binding.etxNewPasswordConfirmation.getEditText().getText().toString());
            profileViewModel.changeUserPassword(user);
        }
    }

    private void configurePasswordFields() {
        binding.etxCurrentPassword.getEditText().addTextChangedListener(new TextChangedListener<EditText>(binding.etxCurrentPassword.getEditText()) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                validateCurrentPassword();
            }
        });
        binding.etxNewPassword.getEditText().addTextChangedListener(new TextChangedListener<EditText>(binding.etxNewPassword.getEditText()) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                validateNewPassword();
            }
        });
        binding.etxNewPasswordConfirmation.getEditText().addTextChangedListener(new TextChangedListener<EditText>(binding.etxNewPasswordConfirmation.getEditText()) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                validateNewPasswordConfirmation();
            }
        });
    }

    private boolean validateCurrentPassword() {
        boolean correct = true;
        String userPassword = profileViewModel.getUserMutableLiveData().getValue().getPassword();
        String currentPassword = binding.etxCurrentPassword.getEditText().getText().toString();
        if (currentPassword.equals("") || currentPassword.isEmpty()) {
            return false;
        }
        boolean equal = BCrypt.checkpw(currentPassword, userPassword);
        if (!equal) {
            binding.txvCurrentPassword.setVisibility(View.VISIBLE);
            correct = false;
        } else {
            binding.txvCurrentPassword.setVisibility(View.GONE);
        }
        return correct;
    }

    private boolean validateNewPassword() {
        boolean validPassword = true;
        String password = binding.etxNewPassword.getEditText().getText().toString();
        if (password.equals("") || password.isEmpty()) {
            return false;
        }
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(password);

        if (!matcher.matches()) {
            binding.txvNewPassword.setText("Minimo 8 caracteres, una letra minúscula, mayúscula y un número.");
            binding.txvNewPassword.setVisibility(View.VISIBLE);
            return false;
        } else if (BCrypt.checkpw(password, user.getPassword())) {
            validPassword = false;
            binding.txvNewPassword.setText("Las contraseña nueva no puede ser igual a la anterior.");
            binding.txvNewPassword.setVisibility(View.VISIBLE);
        } else {
            binding.txvNewPassword.setVisibility(View.GONE);
        }
        return validPassword;
    }

    private boolean validateNewPasswordConfirmation() {
        String newPassword = binding.etxNewPassword.getEditText().getText().toString();
        String newPasswordConfirmation = binding.etxNewPasswordConfirmation.getEditText().getText().toString();
        if (newPasswordConfirmation.equals("")||newPasswordConfirmation.isEmpty()) {
            return false;
        }
        boolean equal = true;
        if (!newPasswordConfirmation.equals(newPassword)) {
            equal = false;
            binding.txvNewPasswordConfirmation.setText("Las contraseñas no coinciden.");
            binding.txvNewPasswordConfirmation.setVisibility(View.VISIBLE);
        } else {
            binding.txvNewPasswordConfirmation.setVisibility(View.GONE);
        }
        return equal;
    }


    private void manageProgressBarCircle() {
        ProgressBarUtils.observeRequestStatus(profileViewModel.getRequestStatusMutableLiveData(),
                this,
                binding.pgbProfile,
                this);
    }

    private void manageRequestChangePasswordStatus() {
        profileViewModel.getRequestChangePasswordStatusMutableLiveData().observe(this, status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbProfile.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                case ERROR:
                    binding.pgbProfile.setVisibility(View.GONE);
                    Toast.makeText(this,status.getMessage(), Toast.LENGTH_SHORT).show();
                    closeChangePasswordSheet();
                    break;
            }
        });
    }

    private void loadUserAccountData() {
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(this,"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(this, dataStoreRX);
        String userId = dataStoreHelper.getStringValue("USER_ID");

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
        if (profileViewModel.getUserMutableLiveData().getValue() != null) {
            intent.putExtra(EditProfileActivity.USER_KEY, profileViewModel.getUserMutableLiveData().getValue());
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