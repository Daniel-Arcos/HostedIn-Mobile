package com.sdi.hostedin.feature.guest.bookings.booked_accommodations_list;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.data.callbacks.GuestBookedAccommodationCallBack;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.GuestBooking;
import com.sdi.hostedin.domain.GetAccommodationsUseCase;
import com.sdi.hostedin.enums.BookingSatuses;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

import java.util.List;

public class GuestBookingsViewModel extends AndroidViewModel {

    private MutableLiveData<List<GuestBooking>> bookedAccommodations = new MutableLiveData<>();
    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNew = new MutableLiveData<>();
    public GuestBookingsViewModel(@NonNull Application application) {
        super(application);
        isNew.setValue(false);
    }

    public MutableLiveData<List<GuestBooking>> getBookedAccommodations() {
        return bookedAccommodations;
    }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public MutableLiveData<Boolean> getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew.setValue(isNew);
    }

    public void getCurrentBookedAccommodations(){
        GetAccommodationsUseCase getAccommodationsUseCase = new GetAccommodationsUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, "Recuperando alojamientos"));
        String userId = DataStoreAccess.accessUserId(this.getApplication());
        if(userId != null) {
            getAccommodationsUseCase.getGuestBookedAccommodations(userId, BookingSatuses.CURRENT.getDescription(), new GuestBookedAccommodationCallBack() {

                @Override
                public void onSuccess(List<GuestBooking> accommodations, String message) {
                    requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, message));
                    bookedAccommodations.setValue(accommodations);
                }

                @Override
                public void onError(String errorMessage) {
                    requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
                }
            });
        }
    }

    public void getOverDueBookedAccommodations(){
        GetAccommodationsUseCase getAccommodationsUseCase = new GetAccommodationsUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, "Recuperando alojamientos"));
        String userId = DataStoreAccess.accessUserId(this.getApplication());
        if(userId != null) {
            getAccommodationsUseCase.getGuestBookedAccommodations(userId, BookingSatuses.OVERDUE.getDescription(), new GuestBookedAccommodationCallBack() {

                @Override
                public void onSuccess(List<GuestBooking> accommodations, String message) {
                    requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, message));
                    bookedAccommodations.setValue(accommodations);
                }

                @Override
                public void onError(String errorMessage) {
                    requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
                }
            });
        }
    }

}
