package com.sdi.hostedin.feature.password;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.FragmentRecoverPasswordEmailEntryBinding;


public class RecoverPasswordEmailEntryFragment extends Fragment {

    private FragmentRecoverPasswordEmailEntryBinding binding;
    String regex = "^(?=.{1,50}$)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    public void setRecoverPasswordViewModel(RecoverPasswordViewModel recoverPasswordViewModel) {
        this.recoverPasswordViewModel = recoverPasswordViewModel;
    }

    public RecoverPasswordViewModel recoverPasswordViewModel;
    public RecoverPasswordEmailEntryFragment() {
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
        binding = FragmentRecoverPasswordEmailEntryBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    public int validateEmail(){
        int isValid = 0;
        String email = binding.etxEmail.getEditText().getText().toString();
        if(!email.isEmpty()){
            if(email.matches(regex)){
                isValid = 1;
            }
            else{
                isValid = 2;
            }
        }
        else {
            isValid = 3;
        }
        return isValid;
    }

    public void trySendEmailCode(){
        String email = binding.etxEmail.getEditText().getText().toString();
        recoverPasswordViewModel.confirmEmail(email);
    }

    public String getEmail(){
        return binding.etxEmail.getEditText().getText().toString();
    }

}