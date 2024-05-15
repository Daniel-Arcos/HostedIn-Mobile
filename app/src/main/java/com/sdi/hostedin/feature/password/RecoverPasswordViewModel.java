package com.sdi.hostedin.feature.password;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.domain.RecoverPasswordUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

public class RecoverPasswordViewModel extends AndroidViewModel {

    public interface TokenPasswordCallBack {
        void onSucces(String token);
        void onError(String errorMessage);
    }
    MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();

    public RecoverPasswordViewModel(@NonNull Application application) { super(application); }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public void sendEmailCode(String email){
        RecoverPasswordUseCase recoverPasswordUseCase = new RecoverPasswordUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));

        recoverPasswordUseCase.sendEmailCode(email, new RecoverPasswordUseCase.RecoverPasswordCallback() {
            @Override
            public void onSucces(String message) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE,message));
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, errorMessage));
            }
        });
    }

    public void verifyCode(String code, TokenPasswordCallBack tokenPassword){
        RecoverPasswordUseCase recoverPasswordUseCase = new RecoverPasswordUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));
        recoverPasswordUseCase.verifyPasswordCode(code, new RecoverPasswordUseCase.RecoverPasswordCallback() {
            @Override
            public void onSucces(String message) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE,message));
                tokenPassword.onSucces(message);
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, errorMessage));
                tokenPassword.onError(errorMessage);
            }
        });
    }

    public void changePassswordWitCodeRecovery(String token, String newPassword, String email){
        RecoverPasswordUseCase recoverPasswordUseCase = new RecoverPasswordUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));
        recoverPasswordUseCase.changePasswordWithCode(token, newPassword, email, new RecoverPasswordUseCase.RecoverPasswordCallback() {
            @Override
            public void onSucces(String message) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE,message));
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, errorMessage));
            }
        });
    }

}
