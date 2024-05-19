package com.sdi.hostedin.feature.host.bookings.list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.data.callbacks.BookingCallBackStore;
import com.sdi.hostedin.data.model.Booking;
import com.sdi.hostedin.domain.GetBookingsOfAccommodationUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

import java.util.List;

public class HostAccBookingsListViewModel extends AndroidViewModel {

    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();

    private final MutableLiveData<List<Booking>> bookingsList = new MutableLiveData<>();

    public MutableLiveData<List<Booking>> getBookingList() {
        return bookingsList;
    }

    public HostAccBookingsListViewModel(@NonNull Application application) {   super(application); }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public void getBookingsOfAccommodation(String accommodationId){
        GetBookingsOfAccommodationUseCase getBookingsOfAccommodation = new GetBookingsOfAccommodationUseCase();

        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, "Cargando"));
        getBookingsOfAccommodation.getBookingsOfSpecificAccommodation(accommodationId, new BookingCallBackStore.BookingsListCallback() {

            @Override
            public void onSuccess(List<Booking> bookingList, String message) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, message));
                bookingsList.setValue(bookingList);
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }


}
