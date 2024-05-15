package com.sdi.hostedin.feature.password;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sdi.hostedin.MainActivity;
import com.sdi.hostedin.databinding.ActivityRecoverPasswordBinding;
import com.sdi.hostedin.utils.ViewModelFactory;

public class RecoverPasswordActivity extends AppCompatActivity {

    private ActivityRecoverPasswordBinding binding;
    private RecoverPasswordViewModel recoverPasswordViewModel;
    private int fragmentNumber;
    private String email;

    private String token;
    private String errorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecoverPasswordBinding.inflate(getLayoutInflater());
        fragmentNumber = 1;
        setContentView(binding.getRoot());
        binding.bttConfirmAction.setOnClickListener(v -> clicButtonConfirm());

        recoverPasswordViewModel = new ViewModelProvider(this, new ViewModelFactory(this.getApplication())).get(RecoverPasswordViewModel.class);
        recoverPasswordViewModel.getRequestStatusMutableLiveData().observe(this, status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbLoadingWheel.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbLoadingWheel.setVisibility(View.GONE);
                    changeFragment();
                    break;
                case ERROR:
                    Toast.makeText(this,status.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.pgbLoadingWheel.setVisibility(View.GONE);
                    Log.e("testTris", status.getMessage());
            }
        });
        recoverPasswordViewModel.getToken().observe(this, tokenGenerated ->{
            this.token = tokenGenerated;
        });
        RecoverPasswordEmailEntryFragment fragment = (RecoverPasswordEmailEntryFragment) getSupportFragmentManager().findFragmentById(binding.fgcvRecoverPasswordFragmentContainer.getId());
        fragment.setRecoverPasswordViewModel(recoverPasswordViewModel);
    }

    private void clicButtonConfirm(){
        switch (fragmentNumber){
            case 1:
                RecoverPasswordEmailEntryFragment fragment = (RecoverPasswordEmailEntryFragment) getSupportFragmentManager().findFragmentById(binding.fgcvRecoverPasswordFragmentContainer.getId());
                int isValidEmail = fragment.validateEmail();
                if(isValidEmail == 1) {
                    this.email = fragment.getEmail();
                    fragment.trySendEmailCode();
                }else if (isValidEmail == 2){
                    Toast.makeText(this, "Formato incorrecto", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Campos email obligatorio", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                RecoverPasswordCodeEntryFragment fragment1 = (RecoverPasswordCodeEntryFragment) getSupportFragmentManager().findFragmentById(binding.fgcvRecoverPasswordFragmentContainer.getId());
                if(fragment1.codeIsEmpty()) {
                    fragment1.verifyCode();
                }
                else{
                    Toast.makeText(this, "Codigo incorrecto", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                RecoverPasswordNewPassEntryFragment fragment2 = (RecoverPasswordNewPassEntryFragment) getSupportFragmentManager().findFragmentById(binding.fgcvRecoverPasswordFragmentContainer.getId());
                if(fragment2.validatePassword()){
                    fragment2.changePassword(email, token);
                }
                else{
                    Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    public void changeFragment(){
        switch (fragmentNumber){
            case 1:
                ChangeFragment(binding.fgcvRecoverPasswordFragmentContainer.getId(), new RecoverPasswordCodeEntryFragment(recoverPasswordViewModel), 2);
                binding.bttConfirmAction.setText("Confirm code");
                break;
            case 2:
                ChangeFragment(binding.fgcvRecoverPasswordFragmentContainer.getId(), new RecoverPasswordNewPassEntryFragment(recoverPasswordViewModel), 3);
                binding.bttConfirmAction.setText("Confirm password");
                break;
            case 3:
                Toast.makeText(this, "Contraseña actualizada correctamente", Toast.LENGTH_SHORT).show();
                goToLogin();
                break;
            default:
        }
    }

    private void ChangeFragment(int fragmentId, Fragment fragment, int number){
        fragmentNumber = number;
        getSupportFragmentManager().beginTransaction()
                .replace(fragmentId, fragment)
                .addToBackStack(null).commit();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            switch (fragmentNumber) {
                case 3:
                    fragmentNumber = 2;
                    binding.bttConfirmAction.setText("Confirm code");
                    break;
                case 2:
                    fragmentNumber = 1;
                    binding.bttConfirmAction.setText("Confirm email");
                    break;
            }
        } else {
            super.onBackPressed();
        }
    }


    private void goToLogin() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

}