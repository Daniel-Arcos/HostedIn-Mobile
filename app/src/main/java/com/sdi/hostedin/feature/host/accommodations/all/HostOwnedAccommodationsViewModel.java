package com.sdi.hostedin.feature.host.accommodations.all;

import android.app.Application;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.MyApplication;
import com.sdi.hostedin.R;
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
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, getApplication().getString(R.string.messg_generic_loading)));
        String token = DataStoreAccess.accessToken(this.getApplication());
        String userId = DataStoreAccess.accessUserId(this.getApplication());
        getAccommodationsUseCase.getAlHostOwnedAccommodations(userId, token, new AccommodationsCallback() {
            @Override
            public void onSuccess(List<Accommodation> accommodations, String newToken) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, getApplication().getString(R.string.messg_acommodations)));
                accommodationsList.setValue(accommodations);
                if(newToken != null && !newToken.isEmpty()){
                    DataStoreAccess.saveToken(getApplication(), newToken);
                }
                fetchMainImageAccommodation(accommodations);
            }

            @Override
            public void onError(String errorMessage, String newToken) {
                if(newToken != null && !newToken.isEmpty()){
                    DataStoreAccess.saveToken(getApplication(), newToken);
                }
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
                    List<Accommodation> currentList = accommodationsList.getValue();
                    if (currentList != null) {
                        List<Accommodation> updatedList = new ArrayList<>(currentList);
                        int index = updatedList.indexOf(accommodation);
                        if (index != -1) {
                            updatedList.set(index, accommodation);
                            accommodationsList.postValue(updatedList);
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
