package com.sdi.hostedin.feature.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.ActivityDeleteAccountBinding;
import com.sdi.hostedin.utils.ToastUtils;

public class DeleteAccountActivity extends AppCompatActivity {

    private ActivityDeleteAccountBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // loadUserData();
        binding.btnDeleteAccount.setOnClickListener( v -> deleteAccount() );
    }

    private void loadUserData() {
        // TODO
    }

    private void deleteAccount() {
        if (isDeleteAccountValid()) {
            // TODO
        }
    }

    private boolean isDeleteAccountValid() {
        boolean isDeleteAccountValid = true;
        String passwordConfirmation = String.valueOf(binding.etxConfirmPassword.getText()).trim();

        if (passwordConfirmation.isEmpty()) {
            ToastUtils.showShortInformationMessage(this, "Debes ingresar tu contraseña actual");
            isDeleteAccountValid = false;
        } else {
            // TODO
        }

        return isDeleteAccountValid;
    }


}