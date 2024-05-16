package com.sdi.hostedin.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sdi.hostedin.feature.guest.explore.ExploreViewModel;
import com.sdi.hostedin.feature.password.RecoverPasswordViewModel;
import com.sdi.hostedin.feature.login.SigninViewModel;
import com.sdi.hostedin.feature.signup.SignupViewModel;
import com.sdi.hostedin.feature.user.DeleteAccountViewModel;
import com.sdi.hostedin.feature.user.EditProfileViewModel;
import com.sdi.hostedin.feature.user.ProfileViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final Application application;
    public ViewModelFactory(Application application) {
        this.application = application;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass.equals(ExploreViewModel.class)) {
            return (T) new ExploreViewModel(application);
        } else if (modelClass.equals(SignupViewModel.class)) {
            return (T) new SignupViewModel(application);
        } else if (modelClass.equals(RecoverPasswordViewModel.class)){
            return (T) new RecoverPasswordViewModel(application);
        } else if (modelClass.equals(SigninViewModel.class)) {
            return (T) new SigninViewModel(application);
        } else if (modelClass.equals(EditProfileViewModel.class)) {
            return (T) new EditProfileViewModel(application);
        } else if (modelClass.equals(DeleteAccountViewModel.class)) {
            return (T) new DeleteAccountViewModel(application);
        } else if (modelClass.equals(ProfileViewModel.class)) {
            return (T) new ProfileViewModel(application);
        }

        return null;
    }
}