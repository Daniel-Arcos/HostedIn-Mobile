package com.sdi.hostedin.data.repositories;

import com.sdi.hostedin.data.datasource.remote.RemotePasswordCodeDataSource;

public class PasswordRepository {

    public interface PasswordVerificationCodeCallback {
        void onSucces(String message);
        void onError(String errorMessage);
    }


    public void sendEmailPasswordCode(String email, PasswordVerificationCodeCallback sendVerificationCodeCallback){
        RemotePasswordCodeDataSource remotePasswordCodeDataSource = new RemotePasswordCodeDataSource();
        remotePasswordCodeDataSource.sendEmailPasswordCode(email, new RemotePasswordCodeDataSource.PasswordCodeCallback() {
            @Override
            public void onSucces(String message) {
                sendVerificationCodeCallback.onSucces(message);
            }

            @Override
            public void onError(String errorMessage) {
                sendVerificationCodeCallback.onError(errorMessage);
            }
        });

    }

    public void verifyPasswordCode(String code, PasswordVerificationCodeCallback passwordCodeCallback){
        RemotePasswordCodeDataSource remotePasswordCodeDataSource = new RemotePasswordCodeDataSource();
        remotePasswordCodeDataSource.verifyPasswordCode(code, new RemotePasswordCodeDataSource.PasswordCodeCallback() {
            @Override
            public void onSucces(String message) {
                passwordCodeCallback.onSucces(message);
            }

            @Override
            public void onError(String errorMessage) {
                passwordCodeCallback.onError(errorMessage);
            }
        });
    }

    public void changePasswordWithCode(String token, String newPassword, String email, PasswordVerificationCodeCallback passwordVerificationCodeCallback){
        RemotePasswordCodeDataSource remotePasswordCodeDataSource = new RemotePasswordCodeDataSource();
        remotePasswordCodeDataSource.changePasswordWithCode(token, newPassword, email, new RemotePasswordCodeDataSource.PasswordCodeCallback() {
            @Override
            public void onSucces(String message) {
                passwordVerificationCodeCallback.onSucces(message);
            }

            @Override
            public void onError(String errorMessage) {
                passwordVerificationCodeCallback.onError(errorMessage);
            }
        });
    }
}
