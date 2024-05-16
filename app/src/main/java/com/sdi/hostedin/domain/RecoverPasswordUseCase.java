package com.sdi.hostedin.domain;

import com.sdi.hostedin.data.repositories.PasswordRepository;

public class RecoverPasswordUseCase {
    public interface RecoverPasswordCallback{
        void onSucces(String message);
        void onError(String errorMessage);
    }

    PasswordRepository passwordRepository = new PasswordRepository();

    public void confirmEmail(String email, RecoverPasswordCallback recoverPasswordCallback){
        passwordRepository.confirmEmail(email, new PasswordRepository.PasswordRecoveryCallBack() {
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
        passwordRepository.verifyPasswordCode(code, new PasswordRepository.PasswordRecoveryCallBack() {

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

    public void updatePassword(String token, String newPassword, String email, RecoverPasswordCallback recoverPasswordCallback){
        passwordRepository.updatePassword(token, newPassword, email, new PasswordRepository.PasswordRecoveryCallBack() {
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
