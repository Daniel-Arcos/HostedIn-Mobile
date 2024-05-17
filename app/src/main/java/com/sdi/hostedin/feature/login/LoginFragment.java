package com.sdi.hostedin.feature.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
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
import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.databinding.FragmentLoginBinding;
import com.sdi.hostedin.feature.guest.GuestMainActivity;
import com.sdi.hostedin.feature.host.HostMainActivity;
import com.sdi.hostedin.feature.signup.SignupFragment;
import com.sdi.hostedin.feature.password.RecoverPasswordActivity;
import com.sdi.hostedin.feature.signup.SignupViewModel;
import com.sdi.hostedin.utils.TextChangedListener;
import com.sdi.hostedin.utils.ViewModelFactory;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    RxDataStore<Preferences> dataStoreRX;
    SigninViewModel signinViewModel;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        signinViewModel =
                new ViewModelProvider(getActivity(), new ViewModelFactory(requireActivity().getApplication())).get(SigninViewModel.class);
        binding.btnLogin.setOnClickListener(v -> {
            if (validateFields()) {
                Login();
            }
        });
        binding.btnSignup.setOnClickListener(v -> {GoToSignUp();});
        binding.btnForgotPassword.setOnClickListener(v -> recoverPassword());
        signinViewModel.getRequestStatusMutableLiveData().observe(getViewLifecycleOwner(), status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbSignin.setVisibility(View.VISIBLE);
                    binding.vwLoading.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbSignin.setVisibility(View.GONE);
                    binding.vwLoading.setVisibility(View.GONE);
                    enterToApp();
                    break;
                case ERROR:
                    Toast.makeText(this.getContext(),status.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.pgbSignin.setVisibility(View.GONE);
                    binding.vwLoading.setVisibility(View.GONE);
            }
        });
        return  binding.getRoot();
    }

    private boolean validateFields() {
        boolean valid = true;
        String email = binding.etxEmail.getEditText().getText().toString();
        String password = binding.etxPassword.getEditText().getText().toString();
        if (email.equals("")) {
            valid = false;
            binding.txvEmailError.setText("Requerido");
        }
        if (password.equals("")) {
            valid = false;
            binding.txvPasswordError.setText("Requerido");
        }
        return valid;
    }

    private void GoToSignUp() {
        getParentFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_main_container, SignupFragment.class, null)
                .commit();
    }

    private void Login() {
        String email = binding.etxEmail.getEditText().getText().toString();
        String password = binding.etxPassword.getEditText().getText().toString();
        signinViewModel.signIn(email, password);
    }

    private void enterToApp() {
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(this.getContext(),"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(this.getActivity(), dataStoreRX);
        dataStoreHelper.putStringValue("USER_ID", signinViewModel.getUserId().getValue());
        boolean isHostEstablished = dataStoreHelper.getBoolValue("START_HOST");
        if (isHostEstablished) {
            goToHostMenu();
        } else {
            goToGuestMenu();
        }
    }

    private void goToGuestMenu() {
        Intent intent = new Intent(this.getActivity(), GuestMainActivity.class);
        startActivity(intent);
        this.getActivity().finish();
    }

    private void goToHostMenu() {
        Intent intent = new Intent(this.getActivity(), HostMainActivity.class);
        startActivity(intent);
        this.getActivity().finish();
    }

    private void recoverPassword(){
        Intent intent = new Intent(this.getActivity(), RecoverPasswordActivity.class);
        startActivity(intent);
    }

}