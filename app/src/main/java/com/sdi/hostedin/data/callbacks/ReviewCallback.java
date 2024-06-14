package com.sdi.hostedin.data.callbacks;

import com.sdi.hostedin.data.model.Review;

public interface ReviewCallback {
    void onSuccess(Review review, String newToken);
    void onError(String errorMessage, String newToken);
}
