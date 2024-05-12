package com.sdi.hostedin.feature.password;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.FragmentRecoverPasswordCodeEntryBinding;


public class RecoverPasswordCodeEntryFragment extends Fragment {

    FragmentRecoverPasswordCodeEntryBinding binding;

    public RecoverPasswordCodeEntryFragment() {
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

    public boolean validateCode(){
        String code1 = binding.etxFirstNumber.getText().toString();
        String code2 = binding.etxSecondNumber.getText().toString();
        String code3 = binding.etxThirdNumber.getText().toString();
        String code4 = binding.etxForthNumber.getText().toString();
        String code5 = binding.etxFifthNumber.getText().toString();
        if(!code1.isEmpty() && !code2.isEmpty() && !code3.isEmpty() && !code4.isEmpty() && !code5.isEmpty()){
            return true;
        }
        else{
            return false;
        }
    }
}