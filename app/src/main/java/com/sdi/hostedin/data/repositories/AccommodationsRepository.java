package com.sdi.hostedin.data.repositories;

import com.sdi.hostedin.data.callbacks.AccommodationCallback;
import com.sdi.hostedin.data.callbacks.AccommodationsCallback;
import com.sdi.hostedin.data.callbacks.BookedAccommodationsCallBack;
import com.sdi.hostedin.data.callbacks.GuestBookedAccommodationCallBack;
import com.sdi.hostedin.data.datasource.remote.RemoteAccommodationsDataSource;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.BookedAccommodation;
import com.sdi.hostedin.data.model.GuestBooking;

import java.util.List;

public class AccommodationsRepository {

    private RemoteAccommodationsDataSource remoteAccommodationsDataSource = new RemoteAccommodationsDataSource();

    public void createAccommodation(Accommodation accommodation, AccommodationCallback accommodationCallback) {
        remoteAccommodationsDataSource.createAccommodation(accommodation, new AccommodationCallback() {
            @Override
            public void onSuccess(Accommodation accommodation, String token) {
                accommodationCallback.onSuccess(accommodation, token);
            }

            @Override
            public void onError(String errorMessage) {
                accommodationCallback.onError(errorMessage);
            }
        });
    }

    public  void getAllAccommodations(AccommodationsCallback accommodationsCallback) {
        remoteAccommodationsDataSource.getAllAccommodations(new AccommodationsCallback() {
            @Override
            public void onSuccess(List<Accommodation> accommodations, String token) {
                accommodationsCallback.onSuccess(accommodations, token);
            }

            @Override
            public void onError(String errorMessage) {
                accommodationsCallback.onError(errorMessage);
            }
        });
    }

    public  void getAllAccommodationsExceptUserAccommodations(String idUser, AccommodationsCallback accommodationsCallback) {
        remoteAccommodationsDataSource.getAllAccommodationsExceptUserAccommodations(idUser, new AccommodationsCallback() {
            @Override
            public void onSuccess(List<Accommodation> accommodations, String token) {
                accommodationsCallback.onSuccess(accommodations, token);
            }

            @Override
            public void onError(String errorMessage) {
                accommodationsCallback.onError(errorMessage);
            }
        });
    }


    public  void getAllAccommodationsByLocationExceptUserAccommodations(String idUser, double lat, double lng, AccommodationsCallback accommodationsCallback) {
        remoteAccommodationsDataSource.getAllAccommodationsByLocationExceptUserAccommodations(idUser, lat, lng, new AccommodationsCallback() {
            @Override
            public void onSuccess(List<Accommodation> accommodations, String token) {
                accommodationsCallback.onSuccess(accommodations, token);
            }

            @Override
            public void onError(String errorMessage) {
                accommodationsCallback.onError(errorMessage);
            }
        });
    }

    public void getHostBoookedAccommodations(String userId, BookedAccommodationsCallBack accommodationsCallback){
        remoteAccommodationsDataSource.getALLHostAccommodationsWithAtLeastOneBooking(userId, new BookedAccommodationsCallBack() {
            @Override
            public void onSuccess(List<BookedAccommodation> accommodations, String token) {
                accommodationsCallback.onSuccess(accommodations, token);
            }

            @Override
            public void onError(String errorMessage) {
                accommodationsCallback.onError(errorMessage);
            }
        });
    }

    public void getGuestBookedAccommodations(String userId,String bookingStatus, GuestBookedAccommodationCallBack accommodationsCallback){
        remoteAccommodationsDataSource.getGuestBookedAccommodations(userId, bookingStatus,  new GuestBookedAccommodationCallBack() {
            @Override
            public void onSuccess(List<GuestBooking> accommodations, String message) {
                accommodationsCallback.onSuccess(accommodations, message);
            }

            @Override
            public void onError(String errorMessage) {
                accommodationsCallback.onError(errorMessage);
            }
        });
    }
}
