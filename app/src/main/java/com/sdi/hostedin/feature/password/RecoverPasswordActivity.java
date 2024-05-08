package com.sdi.hostedin.feature.password;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.ContentUris;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.ActivityRecoverPasswordBinding;
import com.sdi.hostedin.feature.host.HostMainActivity;

public class RecoverPasswordActivity extends AppCompatActivity {

    private ActivityRecoverPasswordBinding binding;
    private int fragmentNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRecoverPasswordBinding.inflate(getLayoutInflater());
        fragmentNumber = 1;
        setContentView(binding.getRoot());
        binding.bttConfirmEmail.setOnClickListener(v -> clicButtonConfirm());
        binding.imgbttGoBack.setOnClickListener(v -> onBackPressed());
    }

    private void clicButtonConfirm(){
        switch (fragmentNumber){
            case 1:
                RecoverPasswordEmailEntry fragment = (RecoverPasswordEmailEntry) getSupportFragmentManager().findFragmentById(binding.fragContainerRecoverPass.getId());
                int isValid = fragment.validateEmail();
                if(isValid == 1) {
                    ChangeFragment(binding.fragContainerRecoverPass.getId(), new RecoverPasswordCodeEntry(), 2);
                    binding.bttConfirmEmail.setText("Confirm code");
                }else if (isValid == 2){
                    Toast.makeText(this, "Formato incorrecto", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Campos email obligatorio", Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                RecoverPasswordCodeEntry fragment1 = (RecoverPasswordCodeEntry) getSupportFragmentManager().findFragmentById(binding.fragContainerRecoverPass.getId());
                if(fragment1.validarCodigo()) {
                    ChangeFragment(binding.fragContainerRecoverPass.getId(), new RecoverPasswordNewPassEntry(), 3);
                    binding.bttConfirmEmail.setText("Confirm password");
                }
                else{
                    Toast.makeText(this, "Codigo incorrecto", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                RecoverPasswordNewPassEntry fragment2 = (RecoverPasswordNewPassEntry) getSupportFragmentManager().findFragmentById(binding.fragContainerRecoverPass.getId());
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
                    binding.bttConfirmEmail.setText("Confirm code");
                    break;
                case 2:
                    fragmentNumber = 1;
                    binding.bttConfirmEmail.setText("Confirm email");
                    break;
            }
        } else {
            super.onBackPressed();
        }
    }




}