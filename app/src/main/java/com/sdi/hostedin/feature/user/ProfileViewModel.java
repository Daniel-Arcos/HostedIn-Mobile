package com.sdi.hostedin.feature.user;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.domain.EditProfileUseCase;
import com.sdi.hostedin.domain.GetAccountUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

public class ProfileViewModel extends AndroidViewModel {

    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public ProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void getUserById(String userId) {

        GetAccountUseCase getAccountUseCase = new GetAccountUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));

        getAccountUseCase.getUserById(userId, new GetAccountUseCase.GetAccountCallback() {
            @Override
            public void onSuccess(User user, String token) {
                userMutableLiveData.setValue(user);
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, "Account recovered"));
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }
}
