package com.sdi.hostedin.feature.login;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.domain.LogInUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

public class SigninViewModel extends AndroidViewModel {


    MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();
    MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    public SigninViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void signIn(String email, String password) {
        LogInUseCase logInUseCase = new LogInUseCase();
        User user = new User();
        user.setEmail(email);
        user.setPassword(password);
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, getApplication().getString(R.string.messg_generic_loading)));
        logInUseCase.LogIn(user, new LogInUseCase.LoginCallback() {
            @Override
            public void onSuccess(User user, String token) {
                DataStoreAccess.saveToken(getApplication(), token);
                DataStoreAccess.saveUserId(getApplication(), user.getId());
                userMutableLiveData.setValue(user);
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, getApplication().getString(R.string.messg_account_created)));
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }

}
