package com.sdi.hostedin.feature.host.bookings;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.MyApplication;
import com.sdi.hostedin.data.callbacks.BookedAccommodationsCallBack;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.BookedAccommodation;
import com.sdi.hostedin.data.model.User;
import com.sdi.hostedin.data.repositories.MultimediasRepository;
import com.sdi.hostedin.domain.GetAccommodationsUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class HostBookedAccommodationsViewModel extends AndroidViewModel {
    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();
    private final MutableLiveData<List<BookedAccommodation>> accommodationsList = new MutableLiveData<>();
    private MutableLiveData<Boolean> isNew = new MutableLiveData<>();
    private MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();
    private final ExecutorService executorService;
    private final Handler mainHandler;

    public HostBookedAccommodationsViewModel(@NonNull Application application) {
        super(application);
        this.isNew.setValue(true);
        MyApplication myApplication = (MyApplication) application;
        executorService = myApplication.getExecutorService();
        mainHandler = myApplication.getMainThreadHandler();
    }

    public MutableLiveData<Boolean> getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew.setValue(isNew);
    }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public MutableLiveData<List<BookedAccommodation>> getAccommodations() {
        return accommodationsList;
    }

    public MutableLiveData<User> getUserMutableLiveData() {
        return userMutableLiveData;
    }

    public void setUserMutableLiveData(User user) {
        this.userMutableLiveData.setValue(user);
    }
    
    public void getHostBookedAccommodations(){
        GetAccommodationsUseCase getAccommodationsUseCase = new GetAccommodationsUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, "Recuperando alojamientos"));
        String userId = DataStoreAccess.accessUserId(this.getApplication());
        String token = DataStoreAccess.accessToken(getApplication());
        if(userId != null) {
            getAccommodationsUseCase.getHostBookedAccommodations(userId, token, new BookedAccommodationsCallBack() {

                @Override
                public void onSuccess(List<BookedAccommodation> accommodations, String message) {
                    requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, message));
                    accommodationsList.setValue(accommodations);
                    fetchMainImageAccommodation(accommodations);
                }

                @Override
                public void onError(String errorMessage) {
                    requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
                }
            });
        }
    }

    public void fetchMainImageAccommodation(List<BookedAccommodation> accommodations) {
        MultimediasRepository multimediasRepository = new MultimediasRepository(executorService);
        for (BookedAccommodation accommodation: accommodations) {
            multimediasRepository.loadMainImageAccommodation(accommodation.get_id(), new MultimediasRepository.LoadMainImageAccommodationCallback() {
                @Override
                public void onSuccess(byte[] image) {
                    accommodation.setMainImage(image);
                    List<BookedAccommodation> list = new ArrayList<>(accommodations);
                    accommodationsList.setValue(new ArrayList<>(list));
                }

                @Override
                public void onError(String message) {
                    System.out.println(message);
                }
            }, mainHandler);
        }
    }
}
