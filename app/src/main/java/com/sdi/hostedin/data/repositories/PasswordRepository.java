package com.sdi.hostedin.data.repositories;

import com.sdi.hostedin.data.datasource.remote.RemotePasswordCodeDataSource;

public class PasswordRepository {

    public interface PasswordRecoveryCallBack {
        void onSucces(String message);
        void onError(String errorMessage);
    }


    public void confirmEmail(String email, PasswordRecoveryCallBack sendVerificationCodeCallback){
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

    public void verifyPasswordCode(String code, PasswordRecoveryCallBack passwordCodeCallback){
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

    public void updatePassword(String token, String newPassword, String email, PasswordRecoveryCallBack passwordVerificationCodeCallback){
        RemotePasswordCodeDataSource remotePasswordCodeDataSource = new RemotePasswordCodeDataSource();
        remotePasswordCodeDataSource.changePassword(token, newPassword, email, new RemotePasswordCodeDataSource.PasswordCodeCallback() {
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
