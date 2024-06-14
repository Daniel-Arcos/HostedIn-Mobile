package com.sdi.hostedin.feature.user;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.domain.EditProfileUseCase;
import com.sdi.hostedin.domain.GetAccountUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

public class ProfileViewModel extends AndroidViewModel {

    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<RequestStatus> requestChangePasswordStatusMutableLiveData = new MutableLiveData<>();
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

    public MutableLiveData<RequestStatus> getRequestChangePasswordStatusMutableLiveData() {
        return requestChangePasswordStatusMutableLiveData;
    }

    public void getUserById(String userId) {

        GetAccountUseCase getAccountUseCase = new GetAccountUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));
        String token = DataStoreAccess.accessToken(getApplication());
        getAccountUseCase.getUserById(userId, token, new GetAccountUseCase.GetAccountCallback() {
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

    public void changeUserPassword(User user, Context context) {
        EditProfileUseCase editProfileUseCase = new EditProfileUseCase();
        requestChangePasswordStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));

        String token = DataStoreAccess.accessToken(getApplication());
        editProfileUseCase.editProfile(user, token, new EditProfileUseCase.EditProfileCallback() {
            @Override
            public void onSuccess(User user, String token) {
                userMutableLiveData.setValue(user);
                requestChangePasswordStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, context.getString(R.string.updated_password)));
            }

            @Override
            public void onError(String errorMessage) {
                requestChangePasswordStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }
}
