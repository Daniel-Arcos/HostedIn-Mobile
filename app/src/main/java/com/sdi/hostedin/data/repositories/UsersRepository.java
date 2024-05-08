package com.sdi.hostedin.data.repositories;

import com.sdi.hostedin.data.datasource.remote.RemoteUsersDataSource;
import com.sdi.hostedin.data.model.User;

public class UsersRepository {

    private RemoteUsersDataSource remoteUsersDataSource = new RemoteUsersDataSource();

    public interface SignupCallback {
        void onSuccess(User user, String token);
        void onError(String errorMessage);
    }

    public void signUp(User user, SignupCallback signupCallback) {
        remoteUsersDataSource.createUserAccount(user, new RemoteUsersDataSource.CreateUserCallback() {
            @Override
            public void onSuccess(User user, String token) {
                signupCallback.onSuccess(user, token);
            }

            @Override
            public void onError(String errorMessage) {
                signupCallback.onError(errorMessage);
            }
        });

    }

}