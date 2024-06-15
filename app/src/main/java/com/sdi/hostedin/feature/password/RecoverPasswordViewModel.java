package com.sdi.hostedin.feature.password;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.R;
import com.sdi.hostedin.domain.RecoverPasswordUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

public class RecoverPasswordViewModel extends AndroidViewModel {

    MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();
    MutableLiveData<String> token = new MutableLiveData<>();


    public RecoverPasswordViewModel(@NonNull Application application) { super(application); }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public MutableLiveData<String> getToken(){
        return token;
    }

    public void confirmEmail(String email){
        RecoverPasswordUseCase recoverPasswordUseCase = new RecoverPasswordUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, getApplication().getString(R.string.messg_generic_loading)));

        recoverPasswordUseCase.confirmEmail(email, new RecoverPasswordUseCase.RecoverPasswordCallback() {
            @Override
            public void onSucces(String message) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE,message));
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }

    public void verifyCode(String code){
        RecoverPasswordUseCase recoverPasswordUseCase = new RecoverPasswordUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, getApplication().getString(R.string.messg_generic_loading)));
        recoverPasswordUseCase.verifyPasswordCode(code, new RecoverPasswordUseCase.RecoverPasswordCallback() {
            @Override
            public void onSucces(String message) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE,message));
                token.setValue(message);
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }

    public void changePasswordWitCodeRecovery(String token, String newPassword, String email){
        RecoverPasswordUseCase recoverPasswordUseCase = new RecoverPasswordUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, getApplication().getString(R.string.messg_generic_loading)));
        recoverPasswordUseCase.updatePassword(token, newPassword, email, new RecoverPasswordUseCase.RecoverPasswordCallback() {
            @Override
            public void onSucces(String message) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE,message));
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }

}
