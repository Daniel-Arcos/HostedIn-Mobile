package com.sdi.hostedin.data.repositories;

import com.sdi.hostedin.data.datasource.remote.RemoteUsersDataSource;
import com.sdi.hostedin.data.model.User;

public class UsersRepository {

    private RemoteUsersDataSource remoteUsersDataSource = new RemoteUsersDataSource();

    public interface AuthCallback {
        void onSuccess(User user, String token);
        void onError(String errorMessage);
    }


    public void signUp(User user, AuthCallback authCallback) {
        remoteUsersDataSource.createUserAccount(user, new RemoteUsersDataSource.AuthCallback() {
            @Override
            public void onSuccess(User user, String token) {
                authCallback.onSuccess(user, token);
            }

            @Override
            public void onError(String errorMessage) {
                authCallback.onError(errorMessage);
            }
        });

    }

    public void signIn(User user, UsersRepository.AuthCallback authCallback) {
        remoteUsersDataSource.login(user, new RemoteUsersDataSource.AuthCallback() {
            @Override
            public void onSuccess(User user, String token) {
                authCallback.onSuccess(user, token);
            }

            @Override
            public void onError(String errorMessage) {
                authCallback.onError(errorMessage);
            }
        });
    }


}
