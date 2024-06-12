package com.sdi.hostedin.feature.host.accommodations.all;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.MyApplication;
import com.sdi.hostedin.data.callbacks.AccommodationsCallback;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.repositories.MultimediasRepository;
import com.sdi.hostedin.domain.GetAccommodationsUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

public class HostOwnedAccommodationsViewModel extends AndroidViewModel {
    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Accommodation>> accommodationsList = new MutableLiveData<>();

    private MutableLiveData<Boolean> isNew = new MutableLiveData<>();

    private final ExecutorService executorService;
    private final Handler mainHandler;

    public HostOwnedAccommodationsViewModel(@NonNull Application application) {
        super(application);
        isNew.setValue(true);
        MyApplication myApplication = (MyApplication) application;
        executorService = myApplication.getExecutorService();
        mainHandler = myApplication.getMainThreadHandler();
    }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public MutableLiveData<List<Accommodation>> getAccommodations() {
        return accommodationsList;
    }

    public MutableLiveData<Boolean> getIsNew() {
        return isNew;
    }

    public void setIsNew(Boolean isNew) {
        this.isNew.setValue(isNew);
    }

    public void getAllHostOwnedAccommodations(){
        GetAccommodationsUseCase getAccommodationsUseCase = new GetAccommodationsUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));
        String token = DataStoreAccess.accessToken(this.getApplication());
        String userId = DataStoreAccess.accessUserId(this.getApplication());
        getAccommodationsUseCase.getAlHostOwnedAccommodations(userId, token, new AccommodationsCallback() {
            @Override
            public void onSuccess(List<Accommodation> accommodations, String token) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, token));
                accommodationsList.setValue(accommodations);
                fetchMainImageAccommodation(accommodations);
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }


    public void fetchMainImageAccommodation(List<Accommodation> accommodations) {
        MultimediasRepository multimediasRepository = new MultimediasRepository(executorService);
        for (Accommodation accommodation: accommodations) {
            multimediasRepository.loadMainImageAccommodation(accommodation.getId(), new MultimediasRepository.LoadMainImageAccommodationCallback() {
                @Override
                public void onSuccess(byte[] image) {
                    accommodation.setMainImage(image);
                    List<Accommodation> list = new ArrayList<>(accommodations);
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
