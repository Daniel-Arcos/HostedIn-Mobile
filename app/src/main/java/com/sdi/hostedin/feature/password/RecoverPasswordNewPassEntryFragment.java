package com.sdi.hostedin.feature.password;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.FragmentRecoverPasswordNewPassEntryBinding;
import com.sdi.hostedin.utils.ToastUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class RecoverPasswordNewPassEntryFragment extends Fragment {


    private RecoverPasswordViewModel recoverPasswordViewModel;
    private String REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*()]).{8,}$";

    private FragmentRecoverPasswordNewPassEntryBinding binding;
    public RecoverPasswordNewPassEntryFragment() {
        // Required empty public constructor
    }

    public RecoverPasswordNewPassEntryFragment(RecoverPasswordViewModel recoverPasswordViewModel) {
        this.recoverPasswordViewModel = recoverPasswordViewModel;
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
        String password = binding.etxNewPassword.getEditText().getText().toString();

        boolean validPassword = true;
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(password);

        if (!matcher.matches()) {
            validPassword = false;
            ToastUtils.showShortInformationMessage(getContext(), getContext().getString(R.string.password_rules));
        }
        return validPassword;
    }

    public void changePassword(String email, String token){
        String password = binding.etxNewPassword.getEditText().getText().toString();
        recoverPasswordViewModel.changePasswordWitCodeRecovery(token,password,email);
    }




}