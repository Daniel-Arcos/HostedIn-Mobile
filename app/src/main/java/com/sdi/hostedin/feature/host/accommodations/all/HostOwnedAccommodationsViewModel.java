package com.sdi.hostedin.feature.host.accommodations.all;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.data.callbacks.AccommodationsCallback;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.domain.GetAccommodationsUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

import java.util.List;

public class HostOwnedAccommodationsViewModel extends AndroidViewModel {
    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Accommodation>> accommodationsList = new MutableLiveData<>();

    public HostOwnedAccommodationsViewModel(@NonNull Application application) {
        super(application);
    }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public MutableLiveData<List<Accommodation>> getAccommodations() {
        return accommodationsList;
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
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }

}
