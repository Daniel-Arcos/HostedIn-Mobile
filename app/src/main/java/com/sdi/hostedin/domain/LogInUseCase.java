package com.sdi.hostedin.domain;

import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.data.repositories.UsersRepository;

public class LogInUseCase {
    public interface LoginCallback {
        void onSuccess(User user, String token);
        void onError(String errorMessage);
    }

    UsersRepository usersRepository = new UsersRepository();

    public void LogIn(User user, LoginCallback loginCallback) {
        usersRepository.signIn(user, new UsersRepository.AuthCallback() {
            @Override
            public void onSuccess(User user, String token) {
                loginCallback.onSuccess(user, token);
            }

            @Override
            public void onError(String errorMessage) {
                loginCallback.onError(errorMessage);
            }
        });
    }
}
