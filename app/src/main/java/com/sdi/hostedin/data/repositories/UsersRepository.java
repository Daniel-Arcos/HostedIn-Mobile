package com.sdi.hostedin.data.repositories;

import com.sdi.hostedin.data.datasource.remote.RemotePasswordCodeDataSource;
import com.sdi.hostedin.data.datasource.remote.RemoteUsersDataSource;
import com.sdi.hostedin.data.model.User;

public class UsersRepository {

    private RemoteUsersDataSource remoteUsersDataSource = new RemoteUsersDataSource();

    public interface SignupCallback {
        void onSuccess(User user, String token);
        void onError(String errorMessage);
    }

    public interface SendVerificationCodeCallback{
        void onSucces(String message);
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

    public void sendEmailPasswordCode(String email, SendVerificationCodeCallback sendVerificationCodeCallback){
        RemotePasswordCodeDataSource remotePasswordCodeDataSource = new RemotePasswordCodeDataSource();
         remotePasswordCodeDataSource.sendEmailPasswordCode(email, new RemotePasswordCodeDataSource.SendPasswordCodeCallback() {
             @Override
             public void onSucces(String message) {
                 sendVerificationCodeCallback.onSucces("Succes");
             }

             @Override
             public void onError(String errorMessage) {
                sendVerificationCodeCallback.onError(errorMessage);
             }
         });

    }

}
