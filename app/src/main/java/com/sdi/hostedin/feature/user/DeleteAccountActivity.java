package com.sdi.hostedin.feature.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.sdi.hostedin.MainActivity;
import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.ActivityDeleteAccountBinding;
import com.sdi.hostedin.utils.ToastUtils;
import com.sdi.hostedin.utils.ViewModelFactory;

import org.mindrot.jbcrypt.BCrypt;

public class DeleteAccountActivity extends AppCompatActivity {
    public static final String USER_KEY = "user_key";
    private ActivityDeleteAccountBinding binding;
    private DeleteAccountViewModel deleteAccountViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle extras = getIntent().getExtras();
        binding.setUserData(extras.getParcelable(USER_KEY));

        deleteAccountViewModel = new ViewModelProvider(this, new ViewModelFactory(getApplication())).get(DeleteAccountViewModel.class);

        manageProgressBarCircle();
        binding.btnDeleteAccount.setOnClickListener( v -> deleteAccount() );
    }

    private void manageProgressBarCircle() {
        deleteAccountViewModel.getRequestStatusMutableLiveData().observe(this, status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbDeleteAccount.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbDeleteAccount.setVisibility(View.GONE);
                    if (status.getMessage().equals(DeleteAccountViewModel.ON_SUCCESS_DELETION_MESSAGE)) {
                        manageSuccessDelete();
                    }
                    break;
                case ERROR:
                    binding.pgbDeleteAccount.setVisibility(View.GONE);
                    ToastUtils.showShortInformationMessage(this, status.getMessage());
            }
        });
    }

    private void deleteAccount() {
        if (isPasswordConfirmationValid()) {
            String userId = binding.getUserData().getId();

            if (isMatchPassword() && userId != null) {
                deleteAccountViewModel.deleteAccount(userId);
            } else {
                ToastUtils.showShortInformationMessage(this, getString(R.string.wrong_password));
            }
        }
    }

    private boolean isPasswordConfirmationValid() {
        boolean isPasswordConfirmationValid = true;
        String passwordConfirmation = String.valueOf(binding.etxConfirmPassword.getText()).trim();

        if (passwordConfirmation.isEmpty()) {
            ToastUtils.showShortInformationMessage(this, getString(R.string.enter_current_password));
            isPasswordConfirmationValid = false;
        }

        return isPasswordConfirmationValid;
    }

    private boolean isMatchPassword() {
        boolean isMatchPassword = false;
        String passwordConfirmation = String.valueOf(binding.etxConfirmPassword.getText()).trim();
        String originalPassword = binding.getUserData().getPassword();

        if (BCrypt.checkpw(passwordConfirmation, originalPassword)) {
            isMatchPassword = true;
        }

        return isMatchPassword;
    }


    private void manageSuccessDelete() {
        ToastUtils.showShortInformationMessage(this, getString(R.string.account_deleted_successfully));
        finish();
        goToLogin();
    }

    private void goToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

}