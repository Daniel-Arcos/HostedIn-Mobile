package com.sdi.hostedin.feature.signup;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.FragmentSignupBinding;
import com.sdi.hostedin.feature.guest.GuestMainActivity;
import com.sdi.hostedin.feature.login.LoginFragment;
import com.sdi.hostedin.utils.DatePickerConfigurator;
import com.sdi.hostedin.utils.TextChangedListener;
import com.sdi.hostedin.utils.ViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {

    FragmentSignupBinding binding;
    SignupViewModel signupViewModel;

    public SignupFragment() {
        // Required empty public constructor
    }

    public static SignupFragment newInstance(String param1, String param2) {
        SignupFragment fragment = new SignupFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSignupBinding.inflate(getLayoutInflater());
        signupViewModel =
                new ViewModelProvider(getActivity(), new ViewModelFactory(requireActivity().getApplication())).get(SignupViewModel.class);


        DatePickerConfigurator.configureDatePicker(binding.etxBirthDate.getEditText());
        binding.btnLogin.setOnClickListener(v -> {
            goToSignUp();
        });
        binding.etxPassword.getEditText().addTextChangedListener(new TextChangedListener<EditText>(binding.etxPassword.getEditText()) {
            @Override
            public void onTextChanged(EditText target, Editable s) {
                validatePassword(binding.etxPassword.getEditText().getText().toString());
            }
        });
        binding.btnSignup.setOnClickListener(v -> {
            if (validateFields()) {
                signUp();
            }
        });

        signupViewModel.getRequestStatusMutableLiveData().observe(getViewLifecycleOwner(), status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbSignup.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbSignup.setVisibility(View.GONE);
                    goToGuestMenu();
                    break;
                case ERROR:
                    Toast.makeText(this.getContext(),status.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.pgbSignup.setVisibility(View.GONE);
            }
        });

        return  binding.getRoot();
    }

    private void goToGuestMenu() {
        Intent intent = new Intent(this.getActivity(), GuestMainActivity.class);
        startActivity(intent);
        this.getActivity().finish();
    }

    private void signUp() {
        User user = new User();
        user.setFullName(binding.etxFullName.getEditText().getText().toString());
        user.setBirthDate(binding.etxBirthDate.getEditText().getText().toString());
        user.setPhoneNumber(binding.etxPhoneNumber.getEditText().getText().toString());
        user.setEmail(binding.etxEmail.getEditText().getText().toString());
        user.setPassword(binding.etxPassword.getEditText().getText().toString());
        signupViewModel.signUp(user);
    }

    private void goToSignUp() {
        getParentFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_main_container, LoginFragment.class, null)
                .commit();
    }

    private void validatePassword(String password) {
        if (password.length() > 0 && password.length() < 8) {
            binding.txvPassword.setText("Password debe tener al menos 8 caracteres");
        }
        else {
            binding.txvPassword.setText("");
        }
    }

    private boolean validateFields() {
        boolean full = true;
        if (binding.etxFullName.getEditText().getText().toString().trim().isEmpty()) {
            binding.txvName.setText("Requerido");
            full = false;
        }
        if (binding.etxBirthDate.getEditText().getText().toString().trim().isEmpty()) {
            binding.txvDateBirth.setText("Requerido");
            full = false;
        }
        if (binding.etxPhoneNumber.getEditText().getText().toString().trim().isEmpty()) {
            binding.txvPhoneNumber.setText("Requerido");
            full = false;
        }
        if (binding.etxEmail.getEditText().getText().toString().trim().isEmpty()) {
            binding.txvEmail.setText("Requerido");
            full = false;
        }
        if (binding.etxPassword.getEditText().getText().toString().trim().isEmpty()) {
            binding.txvPassword.setText("Requerido");
            full = false;
        }
        if (binding.etxConfirmPassword.getEditText().getText().toString().trim().isEmpty()) {
            binding.txvConfirmPassword.setText("Requerido");
            full = false;
        }
        return full;
    }
}