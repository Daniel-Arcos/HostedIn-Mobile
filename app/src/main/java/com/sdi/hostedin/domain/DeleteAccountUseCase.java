package com.sdi.hostedin.domain;

import com.sdi.hostedin.data.repositories.UsersRepository;

public class DeleteAccountUseCase {

    public interface DeleteAccountCallback {
        void onSuccess(String userId);
        void onError(String errorMessage);
    }

    UsersRepository usersRepository = new UsersRepository();

    public void deleteAccount(String userId, DeleteAccountCallback deleteAccountCallback) {
        usersRepository.deleteAccount(userId, new UsersRepository.DeleteAccountCallback() {
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
