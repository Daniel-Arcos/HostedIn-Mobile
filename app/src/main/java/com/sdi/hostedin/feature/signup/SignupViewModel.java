package com.sdi.hostedin.feature.signup;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.domain.CreateAccountUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

public class SignupViewModel extends AndroidViewModel {

    MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();

    public SignupViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public void SignUp(User user) {
        CreateAccountUseCase createAccountUseCase = new CreateAccountUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));

        createAccountUseCase.createAccount(user, new CreateAccountUseCase.CreateAccountCallback() {
            @Override
            public void onSuccess(User user, String token) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, "Account created"));
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }
}
