package com.sdi.hostedin.feature.password;

import static java.security.AccessController.getContext;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainer;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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
    }

    private void clicButtonConfirm(){
        switch (fragmentNumber){
            case 1:
                RecoverPasswordEmailEntry fragment = (RecoverPasswordEmailEntry) getSupportFragmentManager().findFragmentById(binding.fragContainerRecoverPass.getId());
                int isValid = fragment.validateEmail();
                if(isValid == 1) {
                    ChangeFragment(binding.fragContainerRecoverPass.getId(), new RecoverPasswordCodeEntry(), 2);
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
                    ChangeFragment(binding.fragContainerRecoverPass.getId(), new RecoverPasswordNewPassEntry(), 2);
                }
                else{
                    Toast.makeText(this, "Codigo incorrecto", Toast.LENGTH_SHORT).show();
                }
                break;
            case 3:
                RecoverPasswordNewPassEntry fragment2 = (RecoverPasswordNewPassEntry) getSupportFragmentManager().findFragmentById(binding.fragContainerRecoverPass.getId());
                if(fragment2.validatePassword()){
                    fragmentNumber = 3;
                    Toast.makeText(this, "Cambio de contraseña correcta", Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(this, "Ingrese una contraseña", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                fragmentNumber=1;
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
        } else {
            super.onBackPressed();
        }
    }


}