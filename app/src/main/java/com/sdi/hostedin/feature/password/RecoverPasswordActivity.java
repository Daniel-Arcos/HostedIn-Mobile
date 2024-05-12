package com.sdi.hostedin.feature.password;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.sdi.hostedin.databinding.ActivityRecoverPasswordBinding;

public class RecoverPasswordActivity extends AppCompatActivity {

    private ActivityRecoverPasswordBinding binding;
    private int fragmentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecoverPasswordBinding.inflate(getLayoutInflater());
        fragmentNumber = 1;
        setContentView(binding.getRoot());
        binding.bttConfirmAction.setOnClickListener(v -> clicButtonConfirm());
    }

    private void clicButtonConfirm(){
        switch (fragmentNumber){
            case 1:
                RecoverPasswordEmailEntryFragment fragment = (RecoverPasswordEmailEntryFragment) getSupportFragmentManager().findFragmentById(binding.fgcvRecoverPasswordFragmentContainer.getId());
                int isValid = fragment.validateEmail();
                if(isValid == 1) {
                    ChangeFragment(binding.fgcvRecoverPasswordFragmentContainer.getId(), new RecoverPasswordCodeEntryFragment(), 2);
                    binding.bttConfirmAction.setText("Confirm code");
                }else if (isValid == 2){
                    Toast.makeText(this, "Formato incorrecto", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Campos email obligatorio", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                RecoverPasswordCodeEntryFragment fragment1 = (RecoverPasswordCodeEntryFragment) getSupportFragmentManager().findFragmentById(binding.fgcvRecoverPasswordFragmentContainer.getId());
                if(fragment1.validateCode()) {
                    ChangeFragment(binding.fgcvRecoverPasswordFragmentContainer.getId(), new RecoverPasswordNewPassEntryFragment(), 3);
                    binding.bttConfirmAction.setText("Confirm password");
                }
                else{
                    Toast.makeText(this, "Codigo incorrecto", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                RecoverPasswordNewPassEntryFragment fragment2 = (RecoverPasswordNewPassEntryFragment) getSupportFragmentManager().findFragmentById(binding.fgcvRecoverPasswordFragmentContainer.getId());
                if(fragment2.validatePassword()){
                    Toast.makeText(this, "Cambio de contraseña correcta", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
                }
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




}