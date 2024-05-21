package com.sdi.hostedin.feature.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.domain.DeleteAccountUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

public class DeleteAccountViewModel extends AndroidViewModel {

    public static final String ON_SUCCESS_DELETION_MESSAGE = "deleted account";
    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();

    public DeleteAccountViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public void deleteAccount(String userId) {
        DeleteAccountUseCase deleteAccountUseCase = new DeleteAccountUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));
        String token = DataStoreAccess.accessToken(getApplication());
        deleteAccountUseCase.deleteAccount(userId, token, new DeleteAccountUseCase.DeleteAccountCallback() {
            @Override
            public void onSuccess(String userId) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, ON_SUCCESS_DELETION_MESSAGE));
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }
}
