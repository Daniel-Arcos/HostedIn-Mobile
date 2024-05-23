package com.sdi.hostedin.data.callbacks;

import com.sdi.hostedin.data.model.Accommodation;
import com.sdi.hostedin.data.model.Review;

import java.util.List;

public interface ReviewsCallback {
    void onSuccess(List<Review> reviews, String token);
    void onError(String errorMessage);
}
