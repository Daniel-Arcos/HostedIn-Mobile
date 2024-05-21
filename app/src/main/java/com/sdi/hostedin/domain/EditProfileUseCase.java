package com.sdi.hostedin.domain;

import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.data.repositories.UsersRepository;

public class EditProfileUseCase {

    public interface EditProfileCallback {
        void onSuccess(User user, String token);
        void onError(String errorMessage);
    }

    UsersRepository usersRepository = new UsersRepository();

    public void editProfile(User user, String token, EditProfileCallback editProfileCallback) {
        usersRepository.editProfile(user, token, new UsersRepository.EditProfileCallback() {
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
