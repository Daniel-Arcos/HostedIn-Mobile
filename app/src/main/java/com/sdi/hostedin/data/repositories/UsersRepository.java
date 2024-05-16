package com.sdi.hostedin.data.repositories;

import com.sdi.hostedin.data.datasource.remote.RemoteUsersDataSource;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.domain.GetAccountUseCase;

public class UsersRepository {

    private RemoteUsersDataSource remoteUsersDataSource = new RemoteUsersDataSource();

    public interface AuthCallback {
        void onSuccess(User user, String token);
        void onError(String errorMessage);
    }

    public interface EditProfileCallback {
        void onSuccess(User user, String token);
        void onError(String errorMessage);
    }

    public interface GetAccountCallback {
        void onSuccess(User user, String token);
        void onError(String errorMessage);
    }

    public interface DeleteAccountCallback {
        void onSuccess(String userId);
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

    public void getUserById(String userId, GetAccountCallback getAccountCallback) {
        remoteUsersDataSource.getUserById(userId, new RemoteUsersDataSource.GetAccountCallback() {
            @Override
            public void onSuccess(User user, String token) {
                getAccountCallback.onSuccess(user, token);
            }

            @Override
            public void onError(String errorMessage) {
                getAccountCallback.onError(errorMessage);
            }
        });
    }

    public void editProfile(User user, EditProfileCallback editProfileCallback) {
        remoteUsersDataSource.editUserAccount(user, new RemoteUsersDataSource.EditAccountCallback() {
            @Override
            public void onSuccess(User user, String token) {
                editProfileCallback.onSuccess(user, token);
            }

            @Override
            public void onError(String errorMessage) {
                editProfileCallback.onError(errorMessage);
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

    public void deleteAccount(String userId, DeleteAccountCallback deleteAccountCallback) {
        remoteUsersDataSource.deleteAccount(userId, new RemoteUsersDataSource.DeleteAccountCallback() {
            @Override
            public void onSuccess(String userId) {
                deleteAccountCallback.onSuccess(userId);
            }

            @Override
            public void onError(String errorMessage) {
                deleteAccountCallback.onError(errorMessage);
            }
        });
    }
}



