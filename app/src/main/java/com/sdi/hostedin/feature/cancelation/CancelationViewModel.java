package com.sdi.hostedin.feature.cancelation;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import com.sdi.hostedin.data.callbacks.CancellationCallback;
import com.sdi.hostedin.data.datasource.local.DataStoreAccess;
import com.sdi.hostedin.data.model.Cancellation;
import com.sdi.hostedin.data.repositories.CancellationsRepository;
import com.sdi.hostedin.ui.RequestStatus;
import com.sdi.hostedin.ui.RequestStatusValues;

public class CancelationViewModel extends AndroidViewModel {

    MutableLiveData<String> reasonsCancellation = new MutableLiveData<>();
    MutableLiveData<Cancellation> cancellationResponse = new MutableLiveData<>();
    MutableLiveData<RequestStatus> requestStatusMutableLiveData = new MutableLiveData<>();

    public CancelationViewModel(@NonNull Application application) {
        super(application);
        reasonsCancellation.setValue("");
    }

    public MutableLiveData<String> getReasonsCancellation() {
        return reasonsCancellation;
    }

    public MutableLiveData<RequestStatus> getRequestStatusMutableLiveData() {
        return requestStatusMutableLiveData;
    }

    public MutableLiveData<Cancellation> getCancellationResponse() {
        return cancellationResponse;
    }

    public void cancelBooking(Cancellation cancellation) {
        CancellationsRepository cancellationsRepository = new CancellationsRepository();
        String token = DataStoreAccess.accessToken(getApplication());
        requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.LOADING, ""));
        cancellationsRepository.cancelBooking(cancellation, token, new CancellationCallback() {
            @Override
            public void onSuccess(Cancellation cancellation, String token) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.DONE, "Reservacion cancelada con exito"));
                cancellationResponse.setValue(cancellation);
                if (token != null && !token.equals("")) {
                    DataStoreAccess.saveToken(getApplication(), token);
                }
            }

            @Override
            public void onError(String errorMessage, String token) {
                requestStatusMutableLiveData.setValue(new RequestStatus(RequestStatusValues.ERROR, errorMessage));
                if (token != null && !token.equals("")) {
                    DataStoreAccess.saveToken(getApplication(), token);
                }
            }
        });
    }
}
