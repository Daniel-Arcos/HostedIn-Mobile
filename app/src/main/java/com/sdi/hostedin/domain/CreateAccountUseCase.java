package com.sdi.hostedin.domain;

import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.data.repositories.UsersRepository;

public class CreateAccountUseCase {

    public interface CreateAccountCallback {
        void onSuccess(User user, String token);
        void onError(String errorMessage);
    }

    UsersRepository usersRepository = new UsersRepository();

    public void createAccount(User user, CreateAccountCallback createAccountCallback) {
        usersRepository.signUp(user, new UsersRepository.SignupCallback() {
            @Override
            public void onSuccess(User user, String token) {
                createAccountCallback.onSuccess(user, token);
            }

            @Override
            public void onError(String errorMessage) {
                createAccountCallback.onError(errorMessage);
            }
        });
    }

}
