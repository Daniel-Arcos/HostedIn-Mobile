package com.sdi.hostedin.feature.password;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.FragmentRecoverPasswordNewPassEntryBinding;

import kotlin.contracts.Returns;


public class RecoverPasswordNewPassEntry extends Fragment {


    private FragmentRecoverPasswordNewPassEntryBinding binding;
    public RecoverPasswordNewPassEntry() {
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
        binding = FragmentRecoverPasswordNewPassEntryBinding.inflate(getLayoutInflater());
        return  binding.getRoot();
    }

    public boolean validatePassword(){
        String password = binding.edtxNewPassword.toString();
        return password.isEmpty();
    }

}