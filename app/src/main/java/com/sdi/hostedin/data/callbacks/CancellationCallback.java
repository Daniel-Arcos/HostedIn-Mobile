package com.sdi.hostedin.data.callbacks;

import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.Cancellation;

import java.util.List;

public interface CancellationCallback {

    void onSuccess(Cancellation cancellation, String token);
    void onError(String errorMessage, String token);
}
