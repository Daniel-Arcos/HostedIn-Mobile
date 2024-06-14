package com.sdi.hostedin.data.callbacks;

import com.sdi.hostedin.data.model.Review;

import java.util.List;

public interface ReviewsCallback {
    void onSuccess(List<Review> reviews, String newToken);
    void onError(String errorMessage, String newToken);
}
