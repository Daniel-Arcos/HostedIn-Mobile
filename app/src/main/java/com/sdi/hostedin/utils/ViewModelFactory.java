package com.sdi.hostedin.utils;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.sdi.hostedin.feature.guest.bookings.accommodationbooking.AccommodationBookingViewModel;
import com.sdi.hostedin.feature.guest.bookings.booked_accommodations_list.GuestBookingsViewModel;
import com.sdi.hostedin.feature.guest.bookings.review.ReviewAccommodationViewModel;
import com.sdi.hostedin.feature.guest.explore.accommodationdetails.AccommodationDetailsViewModel;
import com.sdi.hostedin.feature.guest.explore.accommodations.ExploreViewModel;
import com.sdi.hostedin.feature.host.accommodations.accommodationform.AccommodationFormViewModel;
import com.sdi.hostedin.feature.host.bookings.HostBookedAccommodationsViewModel;
import com.sdi.hostedin.feature.host.bookings.list.HostAccBookingsListViewModel;
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
        } else if (modelClass.equals(AccommodationFormViewModel.class)) {
            return (T) new AccommodationFormViewModel(application);
        } else if (modelClass.equals(HostAccBookingsListViewModel.class)) {
            return (T) new HostAccBookingsListViewModel(application);
        } else if (modelClass.equals(AccommodationDetailsViewModel.class)) {
            return (T) new AccommodationDetailsViewModel(application);
        } else if (modelClass.equals(AccommodationBookingViewModel.class)) {
            return (T) new AccommodationBookingViewModel(application);
        } else if (modelClass.equals(HostBookedAccommodationsViewModel.class)) {
            return (T) new HostBookedAccommodationsViewModel(application);
        } else if(modelClass.equals(GuestBookingsViewModel.class)){
            return  (T) new GuestBookingsViewModel(application);
        }else  if(modelClass.equals(ReviewAccommodationViewModel.class)){
            return (T) new ReviewAccommodationViewModel(application);
        }

        return null;
    }
}