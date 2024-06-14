package com.sdi.hostedin.data.repositories;

import com.sdi.hostedin.data.callbacks.AccommodationCallback;
import com.sdi.hostedin.data.callbacks.AccommodationsCallback;
import com.sdi.hostedin.data.callbacks.BookedAccommodationsCallBack;
import com.sdi.hostedin.data.callbacks.GuestBookedAccommodationCallBack;
import com.sdi.hostedin.data.callbacks.PasswordCodeCallback;
import com.sdi.hostedin.data.datasource.remote.RemoteAccommodationsDataSource;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.BookedAccommodation;
import com.sdi.hostedin.data.model.GuestBooking;

import java.util.List;

public class AccommodationsRepository {

    private RemoteAccommodationsDataSource remoteAccommodationsDataSource = new RemoteAccommodationsDataSource();

    public void createAccommodation(Accommodation accommodation, String token, AccommodationCallback accommodationCallback) {
        remoteAccommodationsDataSource.createAccommodation(accommodation, token, new AccommodationCallback() {
            @Override
            public void onSuccess(Accommodation accommodation,String message, String newToken) {
                accommodationCallback.onSuccess(accommodation, message, newToken);
            }

            @Override
            public void onError(String errorMessage, String newToken) {
                accommodationCallback.onError(errorMessage, newToken);
            }
        });
    }

    public void updateAccommodation(Accommodation accommodation, String token, AccommodationCallback accommodationCallback) {
        remoteAccommodationsDataSource.updateAccommodation(accommodation, token, new AccommodationCallback() {
            @Override
            public void onSuccess(Accommodation accommodation, String message, String newToken) {
                accommodationCallback.onSuccess(accommodation, message, newToken);
            }

            @Override
            public void onError(String errorMessage, String newToken) {
                accommodationCallback.onError(errorMessage, newToken);
            }
        });
    }

    public  void getAllAccommodations(String token, AccommodationsCallback accommodationsCallback) {
        remoteAccommodationsDataSource.getAllAccommodations(token, new AccommodationsCallback() {
            @Override
            public void onSuccess(List<Accommodation> accommodations, String token) {
                accommodationsCallback.onSuccess(accommodations, token);
            }

            @Override
            public void onError(String errorMessage, String newToken) {
                accommodationsCallback.onError(errorMessage, newToken);
            }
        });
    }

    public  void getAllAccommodationsExceptUserAccommodations(String idUser, String token, AccommodationsCallback accommodationsCallback) {
        remoteAccommodationsDataSource.getAllAccommodationsExceptUserAccommodations(idUser, token, new AccommodationsCallback() {
            @Override
            public void onSuccess(List<Accommodation> accommodations, String token) {
                accommodationsCallback.onSuccess(accommodations, token);
            }

            @Override
            public void onError(String errorMessage, String newToken) {
                accommodationsCallback.onError(errorMessage, newToken);
            }
        });
    }


    public  void getAllAccommodationsByLocationExceptUserAccommodations(String idUser, double lat, double lng, String token, AccommodationsCallback accommodationsCallback) {
        remoteAccommodationsDataSource.getAllAccommodationsByLocationExceptUserAccommodations(idUser, lat, lng, token, new AccommodationsCallback() {
            @Override
            public void onSuccess(List<Accommodation> accommodations, String token) {
                accommodationsCallback.onSuccess(accommodations, token);
            }

            @Override
            public void onError(String errorMessage, String newToken) {
                accommodationsCallback.onError(errorMessage, newToken);
            }
        });
    }

    public void getAlLHostOwnedAccommodations(String userId, String token, AccommodationsCallback accommodationsCallback){
        remoteAccommodationsDataSource.getAllHostOwnedAccommodations(userId, token, new AccommodationsCallback() {
            @Override
            public void onSuccess(List<Accommodation> accommodations, String token) {
                accommodationsCallback.onSuccess(accommodations, token);
            }

            @Override
            public void onError(String errorMessage, String newToken) {
                accommodationsCallback.onError(errorMessage, newToken);
            }
        });
    }

    public void getHostBookedAccommodations(String userId, String token, BookedAccommodationsCallBack accommodationsCallback){
        remoteAccommodationsDataSource.getALLHostAccommodationsWithAtLeastOneBooking(userId, token, new BookedAccommodationsCallBack() {
            @Override
            public void onSuccess(List<BookedAccommodation> accommodations, String newToken) {
                accommodationsCallback.onSuccess(accommodations, newToken);
            }

            @Override
            public void onError(String errorMessage, String newToken) {
                accommodationsCallback.onError(errorMessage, newToken);
            }
        });
    }

    public void getGuestBookedAccommodations(String userId,String bookingStatus,String token, GuestBookedAccommodationCallBack accommodationsCallback){
        remoteAccommodationsDataSource.getGuestBookedAccommodations(userId, bookingStatus, token, new GuestBookedAccommodationCallBack() {
            @Override
            public void onSuccess(List<GuestBooking> accommodations, String newToken) {
                accommodationsCallback.onSuccess(accommodations, newToken);
            }

            @Override
            public void onError(String errorMessage, String newToken) {
                accommodationsCallback.onError(errorMessage, newToken);
            }
        });
    }

    public void deleteAcommodation(String accommodationId, String token, PasswordCodeCallback passwordCodeCallback){
        remoteAccommodationsDataSource.deleteAccommodationById(accommodationId, token, new PasswordCodeCallback() {
            @Override
            public void onSucces(String token) {
                passwordCodeCallback.onSucces(token);
            }

            @Override
            public void onError(String errorMessage) {
                passwordCodeCallback.onError(errorMessage);
            }
        });
    }
}
