package com.sdi.hostedin.feature.signup;

import android.content.Intent;
import android.os.Bundle;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.FragmentSignupBinding;
import com.sdi.hostedin.feature.guest.GuestMainActivity;
import com.sdi.hostedin.feature.login.LoginFragment;
import com.sdi.hostedin.utils.DateFormatterUtils;
import com.sdi.hostedin.utils.DatePickerConfigurator;
import com.sdi.hostedin.utils.TextChangedListener;
import com.sdi.hostedin.utils.ViewModelFactory;

import org.apache.commons.validator.routines.EmailValidator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SignupFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SignupFragment extends Fragment {

    private FragmentSignupBinding binding;
    private SignupViewModel signupViewModel;
    private RxDataStore<Preferences> dataStoreRX;
    private String REGEX = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$";

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
        binding.etxFullName.getEditText().addTextChangedListener(
                new TextChangedListener<EditText>(binding.etxFullName.getEditText()) {
                    @Override
                    public void onTextChanged(EditText target, Editable s) {
                        binding.txvName.setVisibility(View.GONE);
                    }
                }
        );
        binding.etxBirthDate.getEditText().addTextChangedListener(
                new TextChangedListener<EditText>(binding.etxBirthDate.getEditText()) {
                    @Override
                    public void onTextChanged(EditText target, Editable s) {
                        binding.txvDateBirth.setVisibility(View.GONE);
                    }
                }
        );
        binding.etxPhoneNumber.getEditText().addTextChangedListener(
                new TextChangedListener<EditText>(binding.etxPhoneNumber.getEditText()) {
                    @Override
                    public void onTextChanged(EditText target, Editable s) {
                        binding.txvPhoneNumber.setVisibility(View.GONE);
                    }
                }
        );
        binding.etxEmail.getEditText().addTextChangedListener(
                new TextChangedListener<EditText>(binding.etxEmail.getEditText()) {
                    @Override
                    public void onTextChanged(EditText target, Editable s) {
                        binding.txvEmail.setVisibility(View.GONE);
                    }
                }
        );
        binding.etxPassword.getEditText().addTextChangedListener(
            new TextChangedListener<EditText>(binding.etxPassword.getEditText()) {
                @Override
                public void onTextChanged(EditText target, Editable s) {
                    validatePassword(binding.etxPassword.getEditText().getText().toString());
                }
            });
        binding.etxConfirmPassword.getEditText().addTextChangedListener(
                new TextChangedListener<EditText>(binding.etxConfirmPassword.getEditText()) {
                    @Override
                    public void onTextChanged(EditText target, Editable s) {
                        validatePasswordsMatches();
                    }
                }
        );
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

    private boolean validatePasswordsMatches() {
        boolean validPasswords = true;
        String password = binding.etxPassword.getEditText().getText().toString();
        String passwordConfirmation = binding.etxConfirmPassword.getEditText().getText().toString();
        if (!passwordConfirmation.equals(password)) {
            validPasswords = false;
            binding.txvConfirmPassword.setText("La contraseñas no coinciden");
            binding.txvConfirmPassword.setVisibility(View.VISIBLE);
        } else {
            binding.txvConfirmPassword.setVisibility(View.GONE);
        }
        return validPasswords;
    }

    private void goToGuestMenu() {
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(this.getContext(),"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(this.getActivity(), dataStoreRX);
        dataStoreHelper.putStringValue("USER_ID", signupViewModel.getUserId().getValue());
        Intent intent = new Intent(this.getActivity(), GuestMainActivity.class);
        startActivity(intent);
        this.getActivity().finish();
    }

    private void signUp() {
        User user = new User();
        user.setFullName(binding.etxFullName.getEditText().getText().toString());
        String birthdateMongoDb = DateFormatterUtils.parseDateForMongoDB(binding.etxBirthDate.getEditText().getText().toString().trim());
        user.setBirthDate(birthdateMongoDb);
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

    private boolean validatePassword(String password) {
        boolean validPassword = true;
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(password);

        if (!matcher.matches()) {
            validPassword = false;
            binding.txvPassword.setText("Minimo 8 caracteres, una letra minúscula, mayúscula y un número.");
            binding.txvPassword.setVisibility(View.VISIBLE);
        } else {
            binding.txvPassword.setVisibility(View.GONE);
        }
        return validPassword;
    }

    private boolean validateFields() {
        boolean valid = true;
        if (!validateNotEmptyFields()) {
            valid = false;
        }
        if (binding.etxPhoneNumber.getEditText().getText().toString().isEmpty() || !validateNumberPhone()) {
            valid = false;
        }
        if (binding.etxEmail.getEditText().getText().toString().isEmpty() || !validateIsEmail()) {
            valid = false;
        }
        if (!binding.etxPassword.getEditText().getText().toString().isEmpty() &&
                !binding.etxConfirmPassword.getEditText().getText().toString().isEmpty()) {
            if (!validatePassword(binding.etxPassword.getEditText().getText().toString())) {
                valid = false;
            }
            if (!validatePasswordsMatches()) {
                valid = false;
            }
        }
        return valid;
    }

    private boolean validateNotEmptyFields() {
        boolean notEmpty = true;
        if (binding.etxFullName.getEditText().getText().toString().trim().isEmpty()) {
            binding.txvName.setText("Este campo es requerido.");
            binding.txvName.setVisibility(View.VISIBLE);
            notEmpty = false;
        }
        if (binding.etxBirthDate.getEditText().getText().toString().trim().isEmpty()) {
            binding.txvDateBirth.setText("Este campo es requerido.");
            binding.txvDateBirth.setVisibility(View.VISIBLE);
            notEmpty = false;
        }
        if (binding.etxPhoneNumber.getEditText().getText().toString().trim().isEmpty()) {
            binding.txvPhoneNumber.setText("Este campo es requerido.");
            binding.txvPhoneNumber.setVisibility(View.VISIBLE);
            notEmpty = false;
        }
        if (binding.etxEmail.getEditText().getText().toString().trim().isEmpty()) {
            binding.txvEmail.setText("Este campo es requerido.");
            binding.txvEmail.setVisibility(View.VISIBLE);
            notEmpty = false;
        }
        if (binding.etxPassword.getEditText().getText().toString().trim().isEmpty()) {
            binding.txvPassword.setText("Este campo es requerido.");
            binding.txvPassword.setVisibility(View.VISIBLE);
            notEmpty = false;
        }
        if (binding.etxConfirmPassword.getEditText().getText().toString().trim().isEmpty()) {
            binding.txvConfirmPassword.setText("Este campo es requerido.");
            binding.txvConfirmPassword.setVisibility(View.VISIBLE);
            notEmpty = false;
        }
        return notEmpty;
    }

    private boolean validateIsEmail() {
        boolean isEmail = true;
        String email = binding.etxEmail.getEditText().getText().toString();
        EmailValidator emailValidator = EmailValidator.getInstance();
        if (!emailValidator.isValid(email)) {
            isEmail = false;
            binding.txvEmail.setText("El correo electronico ingresado no tiene el formato correcto");
            binding.txvEmail.setVisibility(View.VISIBLE);
        } else {
            binding.txvEmail.setVisibility(View.GONE);
        }
        return isEmail;
    }

    private boolean validateNumberPhone() {
        boolean isPhoneNumber = true;
        if (!(binding.etxPhoneNumber.getEditText().getText().toString().length() == 10)) {
            isPhoneNumber = false;
            binding.txvPhoneNumber.setText("El numero de telefono debe tener exactamente 10 numeros.");
            binding.txvPhoneNumber.setVisibility(View.VISIBLE);
        } else {
            binding.txvPhoneNumber.setVisibility(View.GONE);
        }
        return isPhoneNumber;
    }
}