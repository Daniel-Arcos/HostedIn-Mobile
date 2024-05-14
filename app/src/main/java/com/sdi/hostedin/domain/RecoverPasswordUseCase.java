package com.sdi.hostedin.domain;

import com.sdi.hostedin.data.repositories.UsersRepository;

public class RecoverPasswordUseCase {
    public interface RecoverPasswordCallback{
        void onSucces(String message);
        void onError(String errorMessage);
    }

    UsersRepository usersRepository = new UsersRepository();

    public void sendEmailCode(String email, RecoverPasswordCallback recoverPasswordCallback){
        usersRepository.sendEmailPasswordCode(email, new UsersRepository.SendVerificationCodeCallback() {
            @Override
            public void onSucces(String message) {
                recoverPasswordCallback.onSucces("Success");
            }

            @Override
            public void onError(String errorMessage) {
                recoverPasswordCallback.onError(errorMessage);
            }
        });

    }


}
