package com.sdi.hostedin.feature.password;

import android.os.Bundle;
import android.transition.TransitionInflater;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.sdi.hostedin.R;
import com.sdi.hostedin.databinding.FragmentRecoverPasswordEmailEntryBinding;
import com.sdi.hostedin.utils.ViewModelFactory;


public class RecoverPasswordEmailEntryFragment extends Fragment {

    private FragmentRecoverPasswordEmailEntryBinding binding;
    String regex = "^(?=.{1,50}$)[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";
    private RecoverPasswordViewModel recoverPasswordViewModel;
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

        recoverPasswordViewModel = new ViewModelProvider(getActivity(), new ViewModelFactory(requireActivity().getApplication())).get(RecoverPasswordViewModel.class);

        recoverPasswordViewModel.getRequestStatusMutableLiveData().observe(getViewLifecycleOwner(), status -> {
            switch (status.getRequestStatus()) {
                case LOADING:
                    binding.pgbLoadingWheel.setVisibility(View.VISIBLE);
                    break;
                case DONE:
                    binding.pgbLoadingWheel.setVisibility(View.GONE);
                    break;
                case ERROR:
                    Toast.makeText(this.getContext(),status.getMessage(), Toast.LENGTH_SHORT).show();
                    binding.pgbLoadingWheel.setVisibility(View.GONE);
                    Log.e("testDaniel", status.getMessage());
            }
        });

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
        recoverPasswordViewModel.sendEmailCode(email);
    }
}