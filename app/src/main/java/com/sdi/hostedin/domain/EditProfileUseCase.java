package com.sdi.hostedin.domain;

import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.data.repositories.UsersRepository;

public class EditProfileUseCase {

    public interface EditProfileCallback {
        void onSuccess(User user, String token);
        void onError(String errorMessage);
    }

    UsersRepository usersRepository = new UsersRepository();

    public void editProfile(User user, EditProfileCallback editProfileCallback) {
        usersRepository.editProfile(user, new UsersRepository.EditProfileCallback() {
            @Override
            public void onSuccess(User user, String token) {
                editProfileCallback.onSuccess(user, token);
            }

            @Override
            public void onError(String errorMessage) {
                editProfileCallback.onError(errorMessage);
            }
        });
    }

    public void getUserById(String userId, EditProfileCallback editProfileCallback) {
        usersRepository.getUserById(userId, new UsersRepository.EditProfileCallback() {

            @Override
            public void onSuccess(User user, String token) {
                editProfileCallback.onSuccess(user, token);
            }

            @Override
            public void onError(String errorMessage) {
                editProfileCallback.onError(errorMessage);
            }
        });
    }
}
