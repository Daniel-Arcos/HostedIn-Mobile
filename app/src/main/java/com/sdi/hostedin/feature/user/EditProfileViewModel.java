package com.sdi.hostedin.feature.user;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.domain.EditProfileUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

import java.util.List;

public class EditProfileViewModel extends AndroidViewModel {
    private static final String ON_SUCCESS_EDIT_MESSAGE = "edited account";
    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public EditProfileViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void editProfile(User user) {
        EditProfileUseCase editProfileUseCase = new EditProfileUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));

        editProfileUseCase.editProfile(user, new EditProfileUseCase.EditProfileCallback() {
            @Override
            public void onSuccess(User user, String token) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, ON_SUCCESS_EDIT_MESSAGE));
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }

    public void getUserById(String userId) {
        EditProfileUseCase editProfileUseCase = new EditProfileUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));

        editProfileUseCase.getUserById(userId, new EditProfileUseCase.EditProfileCallback() {
            @Override
            public void onSuccess(User user, String token) {
                userMutableLiveData.setValue(user);
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, "Account recovered"));
            }

            @Override
            public void onError(String errorMessage) {
                Log.d("PRUEBA", "onError: " + errorMessage);
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });


    }
}
