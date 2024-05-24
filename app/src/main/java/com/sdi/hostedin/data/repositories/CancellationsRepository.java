package com.sdi.hostedin.data.repositories;

import com.sdi.hostedin.data.callbacks.CancellationCallback;
import com.sdi.hostedin.data.datasource.remote.RemoteCancellationsDataSource;
import com.sdi.hostedin.data.model.Cancellation;

public class CancellationsRepository {

    RemoteCancellationsDataSource remoteCancellationsDataSource = new RemoteCancellationsDataSource();

    public void cancelBooking(Cancellation cancellation, String token, CancellationCallback cancellationCallback) {
        remoteCancellationsDataSource.cancelBooking(cancellation, token, new CancellationCallback() {
            @Override
            public void onSuccess(Cancellation cancellation, String token) {
                cancellationCallback.onSuccess(cancellation, token);
            }

            @Override
            public void onError(String errorMessage, String token) {
                cancellationCallback.onError(errorMessage, token);
            }
        });
    }
}
