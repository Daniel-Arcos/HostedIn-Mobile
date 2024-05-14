package com.sdi.hostedin.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sdi.hostedin.feature.guest.explore.ExploreViewModel;
import com.sdi.hostedin.feature.password.RecoverPasswordViewModel;
import com.sdi.hostedin.feature.signup.SignupViewModel;

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
        }
        return null;
    }
}