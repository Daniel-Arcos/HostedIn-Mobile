package com.sdi.hostedin.feature.host.accommodations.edition;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.R;
import com.sdi.hostedin.data.callbacks.PasswordCodeCallback;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.domain.DeleteAccommodationUseCase;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

public class EditAccommodationViewModel extends AndroidViewModel {
    private MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();

    public EditAccommodationViewModel(@NonNull Application application) {
        super(application);
    }


    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public void deleteAccommodation(String accommodationId){
        DeleteAccommodationUseCase deleteAccommodationUseCase = new DeleteAccommodationUseCase();
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, getApplication().getString(R.string.messg_generic_loading)));
        String token = DataStoreAccess.accessToken(getApplication());
        deleteAccommodationUseCase.deleteAccommodation(accommodationId, token, new PasswordCodeCallback() {
            @Override
            public void onSucces(String newToken) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, getApplication().getString(R.string.messg_delete_acc)));
                if(newToken != null && !newToken.isEmpty()){
                    DataStoreAccess.saveToken(getApplication(), newToken);
                }
            }

            @Override
            public void onError(String errorMessage) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
            }
        });
    }

}
