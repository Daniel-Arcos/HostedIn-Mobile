package com.sdi.hostedin.feature.login;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.datastore.preferences.core.Preferences;
import androidx.datastore.preferences.rxjava2.RxPreferenceDataStoreBuilder;
import androidx.datastore.rxjava2.RxDataStore;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sdi.hostedin.MainActivity;
import com.sdi.hostedin.R;
import com.sdi.hostedin.data.datasource.DataStoreHelper;
import com.sdi.hostedin.data.datasource.DataStoreManager;
import com.sdi.hostedin.databinding.FragmentExploreBinding;
import com.sdi.hostedin.databinding.FragmentLoginBinding;
import com.sdi.hostedin.feature.guest.GuestMainActivity;
import com.sdi.hostedin.feature.guest.explore.ExploreFragment;
import com.sdi.hostedin.feature.host.HostMainActivity;
import com.sdi.hostedin.feature.signup.SignupFragment;
import com.sdi.hostedin.feature.password.RecoverPasswordActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FragmentLoginBinding binding;
    RxDataStore<Preferences> dataStoreRX;

    public LoginFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LoginFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static LoginFragment newInstance(String param1, String param2) {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
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
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        binding.btnLogin.setOnClickListener(v -> {
            Login();
        });
        binding.btnSignup.setOnClickListener(v -> {
            GoToSignUp();
        });
        binding.btnForgotPassword.setOnClickListener(v -> recoverPassword());
        return  binding.getRoot();
    }

    private void GoToSignUp() {
        getParentFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.fragment_main_container, SignupFragment.class, null)
                .commit();
    }

    private void Login() {
        DataStoreManager dataStoreSingleton = DataStoreManager.getInstance();
        if (dataStoreSingleton.getDataStore() == null) {
            dataStoreRX = new RxPreferenceDataStoreBuilder(this.getContext(),"USER_DATASTORE" ).build();
        } else {
            dataStoreRX = dataStoreSingleton.getDataStore();
        }
        dataStoreSingleton.setDataStore(dataStoreRX);
        DataStoreHelper dataStoreHelper = new DataStoreHelper(this.getActivity(), dataStoreRX);
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