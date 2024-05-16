package com.sdi.hostedin.domain;

import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.data.repositories.UsersRepository;

public class GetAccountUseCase {

    public interface GetAccountCallback {
        void onSuccess(User user, String token);
        void onError(String errorMessage);
    }

    UsersRepository usersRepository = new UsersRepository();

    public void getUserById(String userId, GetAccountUseCase.GetAccountCallback getAccountCallback) {
        usersRepository.getUserById(userId, new UsersRepository.GetAccountCallback() {
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
}
