package com.sdi.hostedin.feature.guest.bookings.booked_accommodations_list;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.MyApplication;
import com.sdi.hostedin.data.callbacks.GuestBookedAccommodationCallBack;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.GuestBooking;
import com.sdi.hostedin.data.repositories.MultimediasRepository;
import com.sdi.hostedin.domain.GetAccommodationsUseCase;
import com.sdi.hostedin.enums.BookingSatuses;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class GuestBookingsViewModel extends AndroidViewModel {

    private MutableLiveData<List<GuestBooking>> bookedAccommodations = new MutableLiveData<>();
    private MutableLiveData<List<GuestBooking>> currentAccommodations = new MutableLiveData<>();
    private MutableLiveData<List<GuestBooking>> overdueAccommodations = new MutableLiveData<>();
    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNew = new MutableLiveData<>();
    private MutableLiveData<Boolean> currentAccView = new MutableLiveData<>();
    private MutableLiveData<Boolean> currentAccWereRecovered  = new MutableLiveData<>();
    private MutableLiveData<Boolean> overdueAccWereRecovered = new MutableLiveData<>();

    private final ExecutorService executorService;
    private final Handler mainHandler;

    public GuestBookingsViewModel(@NonNull Application application) {
        super(application);
        isNew.setValue(true);
        currentAccView.setValue(true);
        currentAccWereRecovered.setValue(false);
        overdueAccWereRecovered.setValue(false);
        MyApplication myApplication = (MyApplication) application;
        executorService = myApplication.getExecutorService();
        mainHandler = myApplication.getMainThreadHandler();
    }

    public MutableLiveData<List<GuestBooking>> getBookedAccommodations() {
        return bookedAccommodations;
    }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public MutableLiveData<Boolean> getCurrentAccView() {
        return currentAccView;
    }

    public MutableLiveData<Boolean> getIsNew() {
        return isNew;
    }

    public MutableLiveData<Boolean> getCurrentAccWereRecovered() {
        return currentAccWereRecovered;
    }

    public MutableLiveData<Boolean> getOverdueAccWereRecovered() {
        return overdueAccWereRecovered;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew.setValue(isNew);
    }

    public void getCurrentBookedAccommodations(){
        GetAccommodationsUseCase getAccommodationsUseCase = new GetAccommodationsUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, "Recuperando alojamientos"));
        String userId = DataStoreAccess.accessUserId(this.getApplication());
        String token = DataStoreAccess.accessToken(getApplication());
        if(userId != null) {
            if(Boolean.FALSE.equals(currentAccWereRecovered.getValue())) {
                getAccommodationsUseCase.getGuestBookedAccommodations(userId, BookingSatuses.CURRENT.getDescription(), token, new GuestBookedAccommodationCallBack() {

                    @Override
                    public void onSuccess(List<GuestBooking> accommodations, String message) {
                        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, message));
                        currentAccommodations.setValue(accommodations);
                        List<GuestBooking> list = new ArrayList<>(currentAccommodations.getValue());
                        bookedAccommodations.setValue(new ArrayList<>(list));
                        currentAccWereRecovered.setValue(true);
                        fetchMainImageAccommodation(accommodations, true);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
                    }
                });
            }else {
                List<GuestBooking> list = new ArrayList<>(currentAccommodations.getValue());
                bookedAccommodations.setValue(new ArrayList<>(list));
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, ""));
            }

        }
    }

    public void getOverDueBookedAccommodations(){
        GetAccommodationsUseCase getAccommodationsUseCase = new GetAccommodationsUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, "Recuperando alojamientos"));
        String userId = DataStoreAccess.accessUserId(this.getApplication());
        String token = DataStoreAccess.accessToken(getApplication());
        if(userId != null) {
            if(Boolean.FALSE.equals(overdueAccWereRecovered.getValue())){
                getAccommodationsUseCase.getGuestBookedAccommodations(userId, BookingSatuses.OVERDUE.getDescription(), token, new GuestBookedAccommodationCallBack() {

                    @Override
                    public void onSuccess(List<GuestBooking> accommodations, String message) {
                        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, message));
                        overdueAccommodations.setValue(accommodations);
                        List<GuestBooking> list = new ArrayList<>(overdueAccommodations.getValue());
                        bookedAccommodations.setValue(new ArrayList<>(list));
                        overdueAccWereRecovered.setValue(true);
                        fetchMainImageAccommodation(accommodations, false);
                    }

                    @Override
                    public void onError(String errorMessage) {
                        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
                    }
                });
            }
            else {
                List<GuestBooking> list = new ArrayList<>(overdueAccommodations.getValue());
                bookedAccommodations.setValue(new ArrayList<>(list));
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, ""));
            }

        }
    }

    public void fetchMainImageAccommodation(List<GuestBooking> accommodations, boolean isCurrent) {
        MultimediasRepository multimediasRepository = new MultimediasRepository(executorService);
        for (GuestBooking accommodation: accommodations) {
            multimediasRepository.loadMainImageAccommodation(accommodation.getAccommodation().getId(), new MultimediasRepository.LoadMainImageAccommodationCallback() {
                @Override
                public void onSuccess(byte[] image) {
                    accommodation.getAccommodation().setMainImage(image);
                    List<GuestBooking> currentList;
                    if (isCurrent){
                        currentList = currentAccommodations.getValue();
                    }
                    else{
                        currentList = overdueAccommodations.getValue();
                    }
                    if (currentList != null) {
                        List<GuestBooking> updatedList = new ArrayList<>(currentList);
                        int index = updatedList.indexOf(accommodation);
                        if (index != -1) {
                            updatedList.set(index, accommodation);
                            if (isCurrent){
                                currentAccommodations.postValue(updatedList);
                                List<GuestBooking> list = new ArrayList<>(currentAccommodations.getValue());
                                bookedAccommodations.setValue(new ArrayList<>(list));
                            }
                            else{
                                overdueAccommodations.postValue(updatedList);
                                List<GuestBooking> list = new ArrayList<>(overdueAccommodations.getValue());
                                bookedAccommodations.setValue(new ArrayList<>(list));
                            }
                        }
                    }
                }

                @Override
                public void onError(String message) {
                    System.out.println(message);
                }
            }, mainHandler);
        }
    }


}
