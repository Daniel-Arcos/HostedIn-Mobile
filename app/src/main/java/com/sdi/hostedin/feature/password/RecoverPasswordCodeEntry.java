package com.sdi.hostedin.feature.password;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.FragmentRecoverPasswordCodeEntryBinding;
import com.sdi.hostedin.databinding.FragmentRecoverPasswordEmailEntryBinding;


public class RecoverPasswordCodeEntry extends Fragment {

    FragmentRecoverPasswordCodeEntryBinding binding;

    public RecoverPasswordCodeEntry() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TransitionInflater inflater = TransitionInflater.from(requireContext());
        setEnterTransition(inflater.inflateTransition(R.transition.fade));
        setExitTransition(inflater.inflateTransition(R.transition.fade));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRecoverPasswordCodeEntryBinding.inflate(getLayoutInflater());
        return  binding.getRoot();
    }

    public boolean validarCodigo(){
        String code1 = binding.txtCode1.getText().toString();
        String code2 = binding.txtCode2.getText().toString();
        String code3 = binding.txtCode3.getText().toString();
        String code4 = binding.txtCode4.getText().toString();
        String code5 = binding.txtCode5.getText().toString();
        if(!code1.isEmpty() && !code2.isEmpty() && !code3.isEmpty() && !code4.isEmpty() && !code5.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }
}