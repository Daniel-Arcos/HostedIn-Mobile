package com.sdi.hostedin.domain;

import com.sdi.hostedin.data.repositories.PasswordRepository;

public class RecoverPasswordUseCase {
    public interface RecoverPasswordCallback{
        void onSucces(String message);
        void onError(String errorMessage);
    }

    PasswordRepository passwordRepository = new PasswordRepository();

    public void sendEmailCode(String email, RecoverPasswordCallback recoverPasswordCallback){
        passwordRepository.sendEmailPasswordCode(email, new PasswordRepository.PasswordVerificationCodeCallback() {
            @Override
            public void onSucces(String message) {
                recoverPasswordCallback.onSucces(message);
            }

            @Override
            public void onError(String errorMessage) {
                recoverPasswordCallback.onError(errorMessage);
            }
        });

    }

    public void verifyPasswordCode(String code, RecoverPasswordCallback recoverPasswordCallback){
        passwordRepository.verifyPasswordCode(code, new PasswordRepository.PasswordVerificationCodeCallback() {

            @Override
            public void onSucces(String message) {
                recoverPasswordCallback.onSucces(message);
            }

            @Override
            public void onError(String errorMessage) {
                recoverPasswordCallback.onError(errorMessage);
            }
        });
    }

    public void changePasswordWithCode(String token, String newPassword, String email,  RecoverPasswordCallback recoverPasswordCallback){
        passwordRepository.changePasswordWithCode(token, newPassword, email, new PasswordRepository.PasswordVerificationCodeCallback() {
            @Override
            public void onSucces(String message) {
                recoverPasswordCallback.onSucces(message);
            }

            @Override
            public void onError(String errorMessage) {
                recoverPasswordCallback.onError(errorMessage);
            }
        });
    }

}
